package com.example.jiao.myapplication.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by jiao on 16/9/28.
 */

public class FloatCircleView extends ViewGroup {

	Paint paint = new Paint();

	public FloatCircleView(Context context) {
		this(context, null);
	}

	public FloatCircleView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public FloatCircleView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	@TargetApi(21)
	public FloatCircleView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		init();
	}

	@Override
	protected void dispatchDraw(Canvas canvas) {
		super.dispatchDraw(canvas);

		Path path = new Path();
		int width = getWidth();
		int height = getHeight();
		int innerRadius = Math.min(width / 2, height / 2) - 20;
		paint.setStrokeWidth(innerRadius);
		paint.setColor(Color.parseColor("#59000000"));
		path.addCircle(getWidth() / 2, getHeight() / 2, Math.min(getWidth() / 2, getHeight() / 2), Path.Direction.CW);
		path.addCircle(getWidth() / 2, getHeight() / 2, Math.min(getWidth() / 2, getHeight() / 2) - 40, Path.Direction.CW);
		path.addCircle(getWidth() / 2, getHeight() / 2, Math.min(getWidth() / 2, getHeight() / 2) - 80, Path.Direction.CW);
		path.setFillType(Path.FillType.INVERSE_EVEN_ODD);
		canvas.drawPath(path, paint);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {

	}

	private void init() {
		paint.setAntiAlias(true);
	}
}
