package com.example.jiao.myapplication.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.ViewGroup;

/**
 * Created by jiao on 16/9/28.
 */

public class BeginnerGuideView extends ViewGroup {

	public BeginnerGuideView(Context context) {
		super(context);
	}

	public BeginnerGuideView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public BeginnerGuideView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	@TargetApi(23)
	public BeginnerGuideView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {

	}

	@Override
	public void onDrawForeground(Canvas canvas) {
		super.onDrawForeground(canvas);
		canvas.drawColor(Color.parseColor("#59000000"));
	}
}