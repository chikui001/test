// Copyright (c) 2015. ExtantFuture Inc. All Rights Reserved.
package com.example.jiao.myapplication;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author fei, <tongfei.mao@extantfuture.com>
 * @date 2015-12-2
 */
public class MainCircleSportView extends View {

	private Paint sportiveCirclePaint;// 内环动态圆环
	private int progress = 0;
	private RectF rectF;
	public ProgressListener progressListener;

	private float mCircleHeight;
	private float mCircleWidth;

	public MainCircleSportView(Context context) {
		super(context);
		init();
	}

	public MainCircleSportView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public MainCircleSportView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	private void init() {
		sportiveCirclePaint = new Paint();
		sportiveCirclePaint.setColor(Color.parseColor("#50E3C2"));
		sportiveCirclePaint.setStyle(Style.STROKE);
		sportiveCirclePaint.setStrokeWidth(15);
		sportiveCirclePaint.setAntiAlias(true);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (progress < 100) {
			progress++;
		} else if (progress > 100) {
			progress = 0;
		}
		float x = mCircleWidth / 2 + (float) Math.sin((Math.PI * 360 * progress / 100) / 180) * mCircleWidth / 2;
		float y = mCircleWidth - (float) Math.cos((Math.PI * 360 * progress / 100) / 180) * mCircleWidth / 2;
		canvas.drawCircle(x, y, 10, sportiveCirclePaint);
		canvas.drawArc(rectF, -90, 360 * progress / 100, false, sportiveCirclePaint);
		invalidate();

	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		super.onLayout(changed, left, top, right, bottom);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int mViewWidth = measureWidth(widthMeasureSpec);
		int mViewHeight = measureHeight(heightMeasureSpec);
		mCircleHeight = mViewWidth;
		mCircleWidth = mViewHeight;
		float mCircleSize = Math.max(mCircleHeight, mCircleWidth);
		mCircleHeight = mCircleWidth = mCircleSize;

		setMeasuredDimension(getRealWeight(), getRealHeight());
		if (null == rectF) {
			//(float left, float top, float right, float bottom)
			rectF = new RectF(0, 0, mCircleWidth, mCircleHeight);
		}
	}

	private int measureWidth(int measureSpec) {
		int preferred = getRealWeight();
		return getMeasurement(measureSpec, preferred);
	}

	/**
	 * @return
	 */
	private int getRealWeight() {
		return (int) (mCircleWidth);
	}

	private int measureHeight(int measureSpec) {
		int preferred = getRealHeight();
		return getMeasurement(measureSpec, preferred);
	}

	/**
	 * @return
	 */
	private int getRealHeight() {
		return (int) (mCircleHeight);
	}

	private int getMeasurement(int measureSpec, int preferred) {
		int specSize = MeasureSpec.getSize(measureSpec);
		int measurement;
		switch (MeasureSpec.getMode(measureSpec)) {
			case MeasureSpec.EXACTLY:
				measurement = specSize;
				break;
			case MeasureSpec.AT_MOST:
				measurement = Math.min(preferred, specSize);
				break;
			default:
				measurement = preferred;
				break;
		}
		return measurement;
	}

	public interface ProgressListener {

		void getProgress(boolean forced, int progress);
	}
}
