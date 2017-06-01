package com.example.jiao.myapplication.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by jiao on 16/8/4.
 */
public class PathView extends View {

	private Path mPath;
	private Paint mPaint;
	private Rect mRect;
	private GradientDrawable mDrawable;

	public PathView(Context context) {
		super(context);
	}

	public PathView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setFocusable(true);

		mPath = new Path();
		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mRect = new Rect(0, 0, 480, 480);

		mDrawable = new GradientDrawable(GradientDrawable.Orientation.TL_BR, new int[] { 0x00000000, 0x71C1C2C3 });
		mDrawable.setShape(GradientDrawable.OVAL);
		mDrawable.setGradientRadius(380);
	}

	static void setCornerRadii(GradientDrawable drawable, float r0, float r1, float r2, float r3) {
		drawable.setCornerRadii(new float[] { r0, r0, r1, r1, r2, r2, r3, r3 });
	}

	public PathView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	@TargetApi(21)
	public PathView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		mDrawable.setBounds(mRect);

		float r = 16;

		canvas.save();
		canvas.translate(10, 10);
		mDrawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);
		setCornerRadii(mDrawable, r, r, 0, 0);
		mDrawable.draw(canvas);
		canvas.restore();

		canvas.save();
		canvas.translate(10 + mRect.width() + 10, 10);
		mDrawable.setGradientType(GradientDrawable.RADIAL_GRADIENT);
		setCornerRadii(mDrawable, 0, 0, r, r);
		mDrawable.draw(canvas);
		canvas.restore();

		canvas.translate(0, mRect.height() + 10);

		canvas.save();
		canvas.translate(10, 10);
		mDrawable.setGradientType(GradientDrawable.SWEEP_GRADIENT);
		setCornerRadii(mDrawable, 0, r, r, 0);
		mDrawable.draw(canvas);
		canvas.restore();

		canvas.save();
		canvas.translate(10 + mRect.width() + 10, 10);
		mDrawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);
		setCornerRadii(mDrawable, r, 0, 0, r);
		mDrawable.draw(canvas);
		canvas.restore();

		canvas.translate(0, mRect.height() + 10);

		canvas.save();
		canvas.translate(10, 10);
		mDrawable.setGradientType(GradientDrawable.RADIAL_GRADIENT);
		setCornerRadii(mDrawable, r, 0, r, 0);
		mDrawable.draw(canvas);
		canvas.restore();

		canvas.save();
		canvas.translate(10 + mRect.width() + 10, 10);
		mDrawable.setGradientType(GradientDrawable.SWEEP_GRADIENT);
		setCornerRadii(mDrawable, 0, r, 0, r);
		mDrawable.draw(canvas);
		canvas.restore();
	}
}
