package com.example.jiao.myapplication.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.SeekBar;

/**
 * @author jiao, <wenliang.jiao@extantfuture.com>
 * @date 2017年03月01日
 */
public class MySeekBar extends SeekBar {

	public MySeekBar(Context context) {
		super(context);
	}

	public MySeekBar(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MySeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	@TargetApi(21)
	public MySeekBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
	}

	@Override
	protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int width = MeasureSpec.getSize(widthMeasureSpec);
		int height = MeasureSpec.getSize(heightMeasureSpec);
		Log.d("before", "width:" + width + ";height:" + height);
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		Log.d("before", "getWidth:" + getMeasuredWidth() + ";getHeight:" + getMeasuredHeight());
	}
}
