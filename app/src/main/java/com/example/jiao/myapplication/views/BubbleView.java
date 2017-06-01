package com.example.jiao.myapplication.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author jiao, <wenliang.jiao@extantfuture.com>
 * @date 2016年10月27日
 */
public class BubbleView extends View {

	public BubbleView(Context context) {
		super(context);
	}

	public BubbleView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public BubbleView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	@TargetApi(23)
	public BubbleView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
	}

	Paint paint = new Paint();

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		test3(canvas);
	}

	Path mPath = new Path();

	private void test3(Canvas canvas) {
		canvas.drawRoundRect(new RectF(getLeft(), getTop(), getRight(), getBottom()), 10, 10, paint);
	}

	private void test2(Canvas canvas) {
		canvas.drawRect(new RectF(0, 0, 500, 100), paint);
		canvas.save();
		canvas.translate(100, 100);
		canvas.drawRect(new RectF(0, 0, 500, 100), paint);
		canvas.restore();
	}

	private void test1(Canvas canvas) {
		//设置背景色
		canvas.drawARGB(255, 139, 197, 186);
		int canvasWidth = canvas.getWidth();
		int canvasHeight = canvas.getHeight();
		int layerId = canvas.saveLayer(0, 0, canvasWidth, canvasHeight, null, Canvas.ALL_SAVE_FLAG);
		int r = canvasWidth / 3;
		//正常绘制黄色的圆形
		paint.setColor(0xFFFFCC44);
		canvas.drawCircle(r, r, r, paint);
		//使用CLEAR作为PorterDuffXfermode绘制蓝色的矩形
		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
		paint.setColor(0xFF66AAFF);
		canvas.drawRect(r, r, r * 2.7f, r * 2.7f, paint);
		//最后将画笔去除Xfermode
		paint.setXfermode(null);
		canvas.restoreToCount(layerId);
	}

	private void test(Canvas canvas) {
		canvas.drawColor(Color.GRAY);
		canvas.save();
		canvas.translate(10, 10);
		drawScene(canvas);
		canvas.restore();
		canvas.save();
		canvas.translate(160, 10);
		canvas.clipRect(10, 10, 90, 90);
		canvas.clipRect(30, 30, 70, 70, Region.Op.DIFFERENCE);
		drawScene(canvas);
		canvas.restore();
		canvas.save();
		canvas.translate(10, 160);
		mPath.reset();
		canvas.clipPath(mPath); // makes the clip empty
		mPath.addCircle(50, 50, 50, Path.Direction.CCW);
		canvas.clipPath(mPath, Region.Op.REPLACE);
		drawScene(canvas);
		canvas.restore();
		canvas.save();
		canvas.translate(160, 160);
		canvas.clipRect(0, 0, 60, 60);
		canvas.clipRect(40, 40, 100, 100, Region.Op.UNION);
		drawScene(canvas);
		canvas.restore();
		canvas.save();
		canvas.translate(10, 310);
		canvas.clipRect(0, 0, 60, 60);
		canvas.clipRect(40, 40, 100, 100, Region.Op.XOR);
		drawScene(canvas);
		canvas.restore();
		canvas.save();
		canvas.translate(160, 310);
		canvas.clipRect(0, 0, 60, 60);
		canvas.clipRect(40, 40, 100, 100, Region.Op.REVERSE_DIFFERENCE);
		drawScene(canvas);
		canvas.restore();
	}

	private void drawScene(Canvas canvas) {
		canvas.clipRect(0, 0, 100, 100);
		canvas.drawColor(Color.WHITE);
		paint.setColor(Color.RED);
		canvas.drawLine(0, 0, 100, 100, paint);
		paint.setColor(Color.GREEN);
		canvas.drawCircle(30, 70, 30, paint);
		paint.setColor(Color.BLUE);
		canvas.drawText("Clipping", 100, 30, paint);
	}
}
