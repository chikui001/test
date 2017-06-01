package com.example.jiao.myapplication;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

/**
 * audio synthesizer engine
 */
public class Audio {

	// audio track object
	private AudioTrack track;

	// length of buffer in shorts
	private int bufferLength;

	// sampling rate in Hz
	private int sampleRate;

	// audio hardware buffer
	private short[] buffer;

	// tone generator
	private Whistler whistler;

	/**
	 * ctor
	 */
	public Audio() {
		new Thread() {

			public void run() {
				init();
				while (true) {
					loop();
				}
			}
		}.start();

	}

	/**
	 * startup and initialization
	 */
	private void init() {
		sampleRate = AudioTrack.getNativeOutputSampleRate(AudioManager.STREAM_MUSIC);

		bufferLength = AudioTrack.getMinBufferSize(sampleRate, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT);

		buffer = new short[bufferLength];

		whistler = new Whistler(0.002f, 0.75f);
		whistler.setPitch(440);

		track = new AudioTrack(AudioManager.STREAM_MUSIC, sampleRate, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT,
							   bufferLength * 2, // length in bytes
							   AudioTrack.MODE_STREAM);

		if (track.getState() != AudioTrack.STATE_INITIALIZED) {
			throw new RuntimeException("Couldn't initialize AudioTrack object");
		}

		track.setStereoVolume(1, 1);
		track.play();
	}

	/**
	 * main audio pump loop
	 */
	private void loop() {
		for (int i = 0; i < buffer.length; i++) {
			buffer[i] = (short) (whistler.get() * 32767);
		}
		track.write(buffer, 0, buffer.length);
	}

	/**
	 * generates a breathy tone at specified freqency
	 */
	class Whistler {

		float noiseFactor, limiter, qFactor;
		float t0, t1, t2;

		public Whistler(float nf, float lm) {
			noiseFactor = nf;
			limiter = lm;
			t2 = 0.001f;
		}

		public void setPitch(float f) {
			qFactor = (float) (1 / Math.pow(sampleRate / (2 * Math.PI * f), 2));
		}

		public float get() {
			t2 = (float) (-qFactor * t0 + noiseFactor * Math.random() * Math.signum(-t2) - t2 * limiter);
			t1 += t2;
			t0 += t1;
			return t0;
		}
	}
}