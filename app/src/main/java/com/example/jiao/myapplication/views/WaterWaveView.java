package com.example.jiao.myapplication.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import com.example.jiao.myapplication.R;

import java.lang.ref.WeakReference;

/**
 * 正弦型函数解析式：y=Asin（ωx+φ）+h
 * 各常数值对函数图像的影响：
 * φ（初相位）：决定波形与X轴位置关系或横向移动距离（左加右减）
 * ω：决定周期（最小正周期T=2π/|ω|）
 * A：决定峰值（即纵向拉伸压缩的倍数）
 * h：表示波形在Y轴的位置关系或纵向移动距离（上加下减）
 * 所以此题的
 * 1.它的最高点和最低点绝对值相等,可以看出h=0的
 * 2.图像的最高点和最低点绝对值等于3,所以A=3
 * 3.从图像看出此函数的周期T=2π,根据公式T=2π/|ω|得到ω=1
 * 4.去图像中的点（π/3,0）带入解析式的y=3sin(2π/3+φ)=0,所以2π/3+φ=kπ.
 * k可以去任意值代入.当取k=1,则φ=π/3,解析式y=3sin(2x+π/3)
 * 峰值
 * Created by jiao on 16/6/30.
 */
public class WaterWaveView extends View {

	private int waterWaveColor;

	private int containerColor;

	private Paint waterPaint;

	private Paint containerPaint;

	/**
	 * 纵向拉伸宽度
	 */
	private float A = 50;
	/**
	 * 最小周期是2π，对应的w ＝ 1
	 * <p/>
	 * 图像中，周期决定单位x距离的波的密度的大小，w越小，横向一个波的距离越宽，周期越大
	 * 所以w可以xml配置，w越小，波浪越悉数
	 */
	private float w;
	/**
	 * y轴偏移距离,h应是view高度的一半
	 */
	private float h;

	private int targetH;
	/**
	 * 初相位，可以xml配置
	 */
	private float φ;

	/**
	 * 圆中心坐标
	 */
	private PointF centerPointF;

	/**
	 * 圆半径
	 */
	private float radius;

	/**
	 * 几个周期
	 */
	private float numbers = 0.5f;

	public WaterWaveView(Context context) {
		super(context);
		init(context, null);
	}

	public WaterWaveView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	public WaterWaveView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context, attrs);
	}

	@TargetApi(21)
	public WaterWaveView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		init(context, attrs);
	}

	private void init(Context context, AttributeSet attrs) {
		if (null != attrs) {
			TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.WaterWaveViewAppearance);
			waterWaveColor = a.getColor(R.styleable.WaterWaveViewAppearance_wave_color, Color.parseColor("#50E3C2"));
			containerColor = a.getColor(R.styleable.WaterWaveViewAppearance_container_color, Color.WHITE);
			numbers = a.getFloat(R.styleable.WaterWaveViewAppearance_wave_cycle_number_in_container, 0.5f);
			A = a.getDimension(R.styleable.WaterWaveViewAppearance_wave_sin_A, 25);
		}
		waterPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		waterPaint.setColor(waterWaveColor);
		waterPaint.setStyle(Paint.Style.FILL);

		containerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		containerPaint.setColor(containerColor);
		containerPaint.setStyle(Paint.Style.STROKE);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		float startX = centerPointF.x - radius;
		float endX = centerPointF.x + radius;

		float start = startX;
		canvas.drawCircle(centerPointF.x, centerPointF.y, radius, containerPaint);
		while (start < endX) {
			//float startX, float startY, float stopX, float stopY,
			//@NonNull Paint paint
			double sinY = A * Math.sin((w * start + φ) * Math.PI / 180) + h;
			float lineStartX = start;
			float lineEndX = start;
			float lineStartY = (float) (getHeight() - sinY);
			float lineEndY =
					centerPointF.y + (float) Math.sqrt(Math.pow(radius, 2) - Math.pow(Math.abs(lineStartX - centerPointF.x), 2));
			if (lineStartY > lineEndY) {
				lineStartY = lineEndY;
			}

			canvas.drawLine(lineStartX, lineStartY, lineEndX, lineEndY, waterPaint);
			start++;

		}

		if (null == runnable) {
			shouldRefresh = true;
			runnable = new MyRunnable(this);
			new Thread(runnable).start();
		}
	}

	private static class MyRunnable implements Runnable {

		private WeakReference<WaterWaveView> weakReference;

		public MyRunnable(WaterWaveView waterWaveView) {
			this.weakReference = new WeakReference<>(waterWaveView);
		}

		@Override
		public void run() {
			while (null != weakReference.get() && weakReference.get().shouldRefresh) {
				if (weakReference.get().h < weakReference.get().targetH) {
					weakReference.get().h = weakReference.get().h + 15;
				}
				weakReference.get().φ = weakReference.get().φ + 5;
				if (weakReference.get().φ >= 360) {
					weakReference.get().φ = 0;
				}
				weakReference.get().postInvalidate();
				try {
					Thread.sleep(25);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		shouldRefresh = false;
	}

	private boolean shouldRefresh = false;
	private Runnable runnable;

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int specType = MeasureSpec.getMode(heightMeasureSpec);
		switch (specType) {
			//match_parent or 200dp
			case MeasureSpec.EXACTLY:
				Log.d("jiao", "EXACTLY");
				break;
			case MeasureSpec.AT_MOST:
				Log.d("jiao", "AT_MOST");
				break;
			case MeasureSpec.UNSPECIFIED:
				Log.d("jiao", "UNSPECIFIED");
				break;
		}

		int mWidthWithPadding = getSize(widthMeasureSpec);
		int mHeightWithPadding = getSize(heightMeasureSpec);
		int paddingLeft = getPaddingLeft();
		int paddingRight = getPaddingRight();
		int paddingTop = getPaddingTop();
		int paddingBottom = getPaddingBottom();

		int realWidth = mWidthWithPadding - paddingLeft - paddingRight;
		int realHeight = mHeightWithPadding - paddingBottom - paddingTop;
		radius = Math.min(realWidth, realHeight) / 2;
		centerPointF = new PointF(paddingLeft + realWidth / 2, paddingTop + realHeight / 2);
		h = paddingBottom;
		targetH = (int) (realHeight * 0.4)+paddingBottom;
		setMeasuredDimension(mWidthWithPadding, mHeightWithPadding);

		w = (float) 180 * numbers / (radius);
	}

	private int getSize(int measureSpec) {
		return MeasureSpec.getSize(measureSpec);
	}

	private static final int SET_H = 101;

	private static final int SET_X_OFFSET = 102;

}
