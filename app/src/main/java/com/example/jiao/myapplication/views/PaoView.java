package com.example.jiao.myapplication.views;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * @author jiao, <wenliang.jiao@extantfuture.com>
 * @date 2017年05月11日
 */
public class PaoView extends View {

	public PaoView(Context context) {
		this(context, null);
	}

	public PaoView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public PaoView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setColor(Color.BLACK);
		valueAnimator = ObjectAnimator.ofFloat(this, "time", 0f, halfTime);
		valueAnimator.setDuration(halfTime);
		paint.setStrokeWidth(20);
	}

	long entireTime = 2000;
	long halfTime = entireTime / 2;

	public void start() {
		if (!valueAnimator.isStarted()) {
			valueAnimator.start();
		}
	}

	void setTime(float time) {
		this.time = time;
		invalidate();
		Log.d("PaoView", "setTime:" + time);
	}

	float time = 0f;
	float g;
	private ValueAnimator valueAnimator;
	private Paint paint;

	float temp;

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		float width = getWidth();
		float height = getHeight();
		float h = height / 2;

		float hV = width / entireTime;

		g = (float) (2f * h / Math.pow(halfTime, 2));
		float vV = g * halfTime;

		//		Log.d("xxx", "speed:" + (vV - g * time));
		float currentHeight = vV * time - (float) (0.5f * g * Math.pow(time, 2));
		temp = currentHeight;

		float x = width - hV * time;
		float y = 0.5f * height - currentHeight;
		canvas.drawPoint(x, y, paint);

		Log.d("PaoView", "t:" + time);

	}
}
