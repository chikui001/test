package com.example.jiao.myapplication.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;

/**
 * @author jiao, <wenliang.jiao@extantfuture.com>
 * @date 2017年02月22日
 */
public class YogaRespectScrollView extends RespectWidthHorizontalScrollView {

	public YogaRespectScrollView(Context context) {
		super(context);
	}

	public YogaRespectScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public YogaRespectScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public YogaRespectScrollView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
	}

	@Override
	protected void dispatchDraw(Canvas canvas) {
		super.dispatchDraw(canvas);
		Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setTextAlign(Paint.Align.LEFT);
		paint.setColor(Color.BLACK);
		paint.setTextSize(20);
		canvas.drawText("160", 0, 20, paint);
	}
}
