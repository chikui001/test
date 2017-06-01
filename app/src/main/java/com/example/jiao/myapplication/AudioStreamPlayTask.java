// Copyright (c) 2015. ExtantFuture Inc. All Rights Reserved.
package com.example.jiao.myapplication;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.audiofx.Equalizer;
import android.text.TextUtils;
import android.util.Log;
import com.extantfuture.util.CollectionUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author Rambo, <rambo@extantfuture.com>
 * @date 2015年12月29日
 */
public class AudioStreamPlayTask implements Runnable {

	private static final String TAG = AudioStreamPlayTask.class.getSimpleName();

	// 播放用的采样率
	protected static final int PLAY_SAMPLE_HZ = 44100;
	// 原始数据采样率
	protected static final int ORIGIN_SAMPLE_HZ = 44100;

	protected static final int NUMBER = 60 * PLAY_SAMPLE_HZ;
	protected static final short DEFAULT = 0;

	private static final int mFrequency = PLAY_SAMPLE_HZ; // 采样率
	private static final int mChannel = AudioFormat.CHANNEL_OUT_MONO; // 声道 AudioFormat.CHANNEL_OUT_STEREO
	private static final int mSampBit = AudioFormat.ENCODING_PCM_16BIT; // 采样精度 AudioFormat.ENCODING_PCM_16BIT
	protected static final List<Short> SOUND_DATA = new ArrayList<Short>();

	private AudioTrack audioTrack;
	private Equalizer mEqualizer;
	protected boolean finished;// 是否已结束
	private LinkedBlockingQueue<byte[]> playStreamQueue = new LinkedBlockingQueue<byte[]>();

	public AudioStreamPlayTask() {
		super();
		init();
	}

	private void init() {
		int bufferSize = AudioTrack.getMinBufferSize(mFrequency, mChannel, mSampBit);
		audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, mFrequency, mChannel, mSampBit, bufferSize, AudioTrack.MODE_STREAM);
		Log.d(TAG, "getAudioSessionId" + audioTrack.getAudioSessionId());
		mEqualizer = new Equalizer(0, audioTrack.getAudioSessionId());
		mEqualizer.setEnabled(true);
		short bands = mEqualizer.getNumberOfBands();
		if (bands > 0 && null != mEqualizer.getBandLevelRange() && 2 == mEqualizer.getBandLevelRange().length) {
			final short minEQLevel = mEqualizer.getBandLevelRange()[0];
			final short maxEQLevel = mEqualizer.getBandLevelRange()[1];
			for (short i = 0; i < bands; i++) {
				if (i < 2) {
					mEqualizer.setBandLevel(i, maxEQLevel);
				} else if (2 == i) {
					mEqualizer.setBandLevel(i, (short) 0);
				} else {
					mEqualizer.setBandLevel(i, minEQLevel);
				}
			}
		}
	}

	@Override
	public void run() {
		audioTrack.play();
		Log.d(TAG, "audio play start");
		while (true) {
			try {
				byte[] audioData = playStreamQueue.take();
				if (finished) {
					if (null != playStreamQueue) {
						playStreamQueue.clear();
						playStreamQueue = null;
					}
					if (null != audioTrack) {
						audioTrack.stop();
						Log.d(TAG, "audio play stop");
						if (null != mEqualizer) {
							mEqualizer.release();
						}
						audioTrack.release();
						Log.d(TAG, "audio track release");
					}
					break;
				}
				if (CollectionUtil.isNotEmpty(audioData)) {
					int size = audioTrack.write(audioData, 0, audioData.length);
					Log.d(TAG, "audioTrack write size=" + size);
				}
			} catch (Exception e) {
				Log.e(TAG, "audio stream play exception", e);
			}
		}
	}

	public void sendBpm(int bpm) {
		if (!finished && bpm > 0) {
			buildDataAndOffer(bpm);
		}
	}

	/**
	 * 停止播放
	 */
	public void stop() {
		finished = true;
		playStreamQueue.offer(new byte[0]);
	}

	protected LinkedList<Short> builtDataBuffer = new LinkedList<Short>();

	/**
	 * 产生本轮播放足够多的播放数据
	 */
	protected void buildDataAndOffer(int bpm) {
		Log.d(TAG, "buildData bpm:" + bpm);
		if (bpm > 0) {
			int n = NUMBER / bpm;
			do {
				genData(n);
			} while (CollectionUtil.size(builtDataBuffer) < ORIGIN_SAMPLE_HZ);
		} else {// 补全
			// if (EfLog.isDebug()) {
			// EfLog.d(TAG, "buildData bpm=" + bpm + ", gen data size=" + (ORIGIN_SAMPLE_HZ - builtDataBuffer.size()));
			// }
			buildEnvBuffer();
		}
		if (CollectionUtil.size(builtDataBuffer) >= ORIGIN_SAMPLE_HZ) {
			// 转播放流
			ByteBuffer playBuffer = ByteBuffer.allocate(ORIGIN_SAMPLE_HZ * 2);// 每个short 2字节
			for (int i = 0; i < ORIGIN_SAMPLE_HZ; i++) {// 取前ORIGIN_SAMPLE_HZ个数据来播放
				Short data = builtDataBuffer.poll();
				if (null != data) {
					byte[] convertArray = formalshort2Bytes(data.shortValue());
					if (CollectionUtil.isNotEmpty(convertArray)) {
						playBuffer.put(convertArray[1]);
						playBuffer.put(convertArray[0]);
					}
				}
			}
			byte[] audioDataArray = playBuffer.array();
			if (CollectionUtil.isNotEmpty(audioDataArray)) {
				playStreamQueue.offer(audioDataArray);
				// if (EfLog.isDebug()) {
				// EfLog.d("buildData playStreamQueue offer suc, len=" + CollectionUtil.getLength(audioDataArray));
				// }
			}
		} else {
			Log.e(TAG, "builtDataBuffer size is blow ORIGIN_SAMPLE_HZ!");
		}
	}

	protected void buildEnvBuffer() {
		while (CollectionUtil.size(builtDataBuffer) < ORIGIN_SAMPLE_HZ) {
			builtDataBuffer.offer(DEFAULT);
		}
	}

	/**
	 * 产生n个数据
	 *
	 * @param n
	 */
	protected void genData(int n) {
		int i = 0;
		int size = CollectionUtil.size(SOUND_DATA);
		while (i < n) {
			if (i < n - size) {
				builtDataBuffer.offer(DEFAULT);
			} else {
				builtDataBuffer.offer(SOUND_DATA.get(i - (n - size)));
			}
			i++;
		}
	}

	static {
		initData();
	}

	private static void initData() {
		BufferedReader br = null;
		try {
			String line = null;
			br = new BufferedReader(new InputStreamReader(AudioStreamPlayTask.class.getResourceAsStream("sdata")));
			do {
				line = br.readLine();
				if (null == line) {
					break;
				}
				if (!TextUtils.isEmpty(line)) {
					try {
						short value = (short) (Short.parseShort(line) * 2);
						SOUND_DATA.add(value);
					} catch (Exception e) {
					}
				}
			} while (true);
		} catch (Exception e) {
			Log.e(TAG, "initData exception", e);
		} finally {
			Log.d(TAG, "SOUND_DATA size=" + CollectionUtil.size(SOUND_DATA));
			if (null != br) {
				try {
					br.close();
				} catch (IOException e) {
				}
			}
		}
	}

	protected byte[] formalshort2Bytes(short num) {
		byte[] byteNum = new byte[2];
		for (int ix = 0; ix < 2; ix++) {
			int offset = 16 - (ix + 1) * 8;
			byteNum[ix] = (byte) ((num >> offset) & 0xff);
		}
		return byteNum;
	}
}
