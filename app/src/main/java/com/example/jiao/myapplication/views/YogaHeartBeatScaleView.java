package com.example.jiao.myapplication.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.FrameLayout;

/**
 * @author jiao, <wenliang.jiao@extantfuture.com>
 * @date 2017年02月22日
 */
public class YogaHeartBeatScaleView extends FrameLayout {

	private Paint paint;

	public YogaHeartBeatScaleView(Context context) {
		this(context, null);
	}

	public YogaHeartBeatScaleView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public YogaHeartBeatScaleView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context, attrs);
	}

	@TargetApi(21)
	public YogaHeartBeatScaleView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		init(context, attrs);
	}

	@Override
	protected void dispatchDraw(Canvas canvas) {
		super.dispatchDraw(canvas);
		paint.setTextAlign(Paint.Align.LEFT);
		canvas.drawText("160", 0, 20, paint);
		Log.d("xxx", "getWidth:" + getWidth());
	}

	private void init(Context context, AttributeSet attrs) {
		paint = new Paint(Paint.ANTI_ALIAS_FLAG);
	}
}
