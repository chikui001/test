package com.example.jiao.myapplication;

import android.annotation.TargetApi;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.VideoView;

/**
 * @author jiao, <wenliang.jiao@extantfuture.com>
 * @date 2016年12月05日
 */
public class CustomVideoView extends VideoView {

	private VideoPlayListener videoPlayListener;

	public void setVideoPlayListener(VideoPlayListener videoPlayListener) {
		this.videoPlayListener = videoPlayListener;
	}

	public CustomVideoView(Context context) {
		super(context);
	}

	public CustomVideoView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public CustomVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	@TargetApi(21)
	public CustomVideoView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
	}

	@Override
	public void pause() {
		super.pause();
		if (null != videoPlayListener) {
			videoPlayListener.onPause();
		}
	}

	interface VideoPlayListener {

		void onPause();
	}
}
