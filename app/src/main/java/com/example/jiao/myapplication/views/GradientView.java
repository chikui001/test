package com.example.jiao.myapplication.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by jiao on 16/9/2.
 */
public class GradientView extends View {

	private Rect drawableRect;

	private GradientDrawable mDrawable;

	public GradientView(Context context) {
		this(context, null);
	}

	public GradientView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public GradientView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	private void init() {
		mDrawable = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, new int[] { 0x7F000000, 0x00000000, 0x7F000000 });
		mDrawable.setShape(GradientDrawable.LINEAR_GRADIENT);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (null != drawableRect) {
			mDrawable.setBounds(drawableRect);
			mDrawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);
			mDrawable.draw(canvas);
		}
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		drawableRect = new Rect(0, 0, getMeasuredWidth(), getMeasuredHeight());
	}
}
