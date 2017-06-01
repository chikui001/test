package com.example.jiao.myapplication;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

/**
 * @author jiao, <wenliang.jiao@extantfuture.com>
 * @date 2016年12月29日
 */
public class MyImageView extends ImageView {

	public MyImageView(Context context) {
		this(context, null);
	}

	public MyImageView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public MyImageView(Context context, AttributeSet attrs, int defStyleAttr) {
		this(context, attrs, defStyleAttr, 0);
	}

	@TargetApi(21)
	public MyImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		laserDrawable = new GradientDrawable(GradientDrawable.Orientation.RIGHT_LEFT, new int[] { 0x50E3C2, 0xA050E3C2, 0x50E3C2 });
		laserDrawable.setShape(GradientDrawable.RECTANGLE);

	}

	@Override
	public void setImageDrawable(Drawable drawable) {
		super.setImageDrawable(drawable);
		Log.d("xxx", "setImageDrawable:" + drawable);
	}

	/**
	 * 扫描线
	 */
	private GradientDrawable laserDrawable;

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		laserDrawable.setBounds(new Rect(200, 200, 400, 220));
		//		laserDrawable.setGradientRadius(getShadowRect().width() / 4 * 3);
		laserDrawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);
		laserDrawable.draw(canvas);
	}
}
