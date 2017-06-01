package com.example.jiao.myapplication.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author fei, <tongfei.mao@extantfuture.com>
 * @date 2015-11-23
 */
public class WaterPull extends View {

	private float A = 15;
	// 最小正周期
	private float w = 1;
	// y轴偏移距离
	private float h = -300;
	// 初相位
	private float φ = 100;
	// 圆心x坐标
	private int centerX;
	// 圆心y坐标
	private int centerY;
	// 半径
	private int radius;
	private Integer water_wave_color;

	// 是否在波浪
	private boolean isRunning;

	private boolean isRectangle;
	private Canvas mCanvas;

	private Paint backGPaint;
	private Paint strokePaint;

	// 正弦型函数解析式：y=Asin（ωx+φ）+h
	// 各常数值对函数图像的影响：
	// φ（初相位）：决定波形与X轴位置关系或横向移动距离（左加右减）
	// ω：决定周期（最小正周期T=2π/|ω|）
	// A：决定峰值（即纵向拉伸压缩的倍数）
	// h：表示波形在Y轴的位置关系或纵向移动距离（上加下减）
	// 所以此题的
	// 1.它的最高点和最低点绝对值相等,可以看出h=0的
	// 2.图像的最高点和最低点绝对值等于3,所以A=3
	// 3.从图像看出此函数的周期T=π,根据公式T=2π/|ω|得到ω=2
	// 4.去图像中的点（π/3,0）带入解析式的y=3sin(2π/3+φ)=0,所以2π/3+φ=kπ.
	// k可以去任意值代入.当取k=1,则φ=π/3,解析式y=3sin(2x+π/3)
	// 峰值
	public WaterPull(Context context) {
		this(context, null);
	}

	public WaterPull(Context context, AttributeSet attrs) {
		super(context, attrs);
		getAttrs(context, attrs);
		init();
	}

	private void init() {
		backGPaint = new Paint();
		strokePaint = new Paint();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		setMeasuredDimension(measure(widthMeasureSpec, true), measure(heightMeasureSpec, false));
		mesureCenterCirle();

	}

	/**
	 * @param measureSpec
	 * @param isWidth
	 * @return
	 * @category 测量
	 */
	private int measure(int measureSpec, boolean isWidth) {
		int result;
		int mode = MeasureSpec.getMode(measureSpec);
		int size = MeasureSpec.getSize(measureSpec);
		int padding = isWidth ? getPaddingLeft() + getPaddingRight() : getPaddingTop() + getPaddingBottom();
		if (mode == MeasureSpec.EXACTLY) {
			result = size;
		} else {
			result = isWidth ? getSuggestedMinimumWidth() : getSuggestedMinimumHeight();
			result += padding;
			if (mode == MeasureSpec.AT_MOST) {
				if (isWidth) {
					result = Math.max(result, size);
				} else {
					result = Math.min(result, size);
				}
			}
		}
		return result;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		mCanvas = canvas;
		if (!isRectangle) {
			drawCircle(mCanvas, centerX, centerY, radius);
			drawCircle(mCanvas, centerX, centerY, radius, 2);
			drawSinaLine(mCanvas, φ);
		} else {
			setBackgroundColor(Color.WHITE);
			drawRectangleWaterLine(mCanvas, φ);
		}
	}

	/**
	 * 画圆
	 *
	 * @param canvas
	 * @param x
	 * @param y
	 * @param radius
	 */
	private void drawCircle(Canvas canvas, float x, float y, float radius) {

		backGPaint.setAntiAlias(true);
		backGPaint.setColor(Color.WHITE);
		canvas.drawCircle(x, y, radius, backGPaint);

	}

	private void drawCircle(Canvas canvas, int centerX2, int centerY2, int radius2, int i) {
		strokePaint.setAntiAlias(true);
		strokePaint.setFilterBitmap(true);
		strokePaint.setDither(true);
		strokePaint.setColor(water_wave_color);
		strokePaint.setStyle(Paint.Style.STROKE);
		strokePaint.setStrokeWidth(1);
		canvas.drawCircle(centerX2, centerY2, radius, strokePaint);

	}

	/**
	 * 测量圆心坐标
	 */
	private void mesureCenterCirle() {

		int width = getWidth();
		int height = getHeight();

		// 取得圆心坐标
		centerX = width / 2;
		centerY = height / 2;

		// 计算半径
		if (width < height)
			radius = width / 2 - 1;
		radius = height / 2 - 1;
	}

	// Asin（ωx+φ）+h
	private void drawSinaLine(Canvas canvas, float φ) {

		// 波浪开始的左边起始x坐标
		int startX = (int) (centerX - radius);

		// 在圆的直径范围内才有波浪
		while (startX < centerX + radius) {

			// 正弦函数的Y轴坐标
			double sinaY;
			// 圆上的+Y轴坐标
			double topY;
			// 圆上的-Y轴坐标
			double bottomY;
			{
				int dinstance = centerX - startX;
				int item = radius * radius - dinstance * dinstance;
				// 圆上的点距离 中心x轴的距离
				double resulty = Math.sqrt(item);
				bottomY = centerY + resulty;
				topY = centerY - resulty;
			}
			{

				sinaY = A * Math.sin((w * startX + φ) * Math.PI / 180) + h;
				sinaY = centerY - sinaY;

				// 保证波浪会在圆内
				if (topY > sinaY && sinaY < centerY) {
					sinaY = topY;
				}
				if (bottomY < sinaY && sinaY > centerY) {
					sinaY = bottomY;
				}
			}

			{
				Paint paint = new Paint();
				paint.setStrokeWidth(1);
				paint.setColor(water_wave_color);
				paint.setAntiAlias(true);
				canvas.drawLine((float) startX, (float) bottomY, (float) startX, (float) sinaY, paint);
			}

			startX++;
		}

	}

	private void drawRectangleWaterLine(Canvas canvas, float φ) {
		float startX = centerX - getWidth() / 2;
		float startY = centerY - getHeight() / 2;
		float endY = centerY + getHeight() / 2;
		float endX = centerX + getWidth() / 2;
		while (startX < endX) {
			double sinaY;

			sinaY = A * Math.sin((w * startX + φ) * Math.PI / 180) + h;
			sinaY = centerY - sinaY;

			{
				Paint paint = new Paint();
				paint.setStrokeWidth(1);
				paint.setColor(water_wave_color);
				paint.setAntiAlias(true);
				canvas.drawLine((float) startX, (float) endY, (float) startX, (float) sinaY, paint);
			}
			startX++;

		}
	}

	/**
	 * 关闭波浪
	 */
	public void closeWave() {
		isRunning = false;
	}

	/**
	 * 开始波浪
	 */
	public void startWave() {
		isRunning = true;

		new Thread(new Runnable() {

			public void run() {
				while (isRunning) {
					if (φ == 360) {
						φ = -2;
					}
					φ++;
					if (h != -50) {
						h += 1;
					}
					postInvalidate();
					try {
						Thread.sleep(4);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}

	/**
	 * 得到属性值
	 *
	 * @param context
	 * @param attrs
	 */
	private void getAttrs(Context context, AttributeSet attrs) {
		water_wave_color = Color.parseColor("#50E3C2");
		isRectangle = true;
	}
}
