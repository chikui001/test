package com.example.jiao.myapplication.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * 时间相关的倒计时控件
 *
 * @author jiao, <wenliang.jiao@extantfuture.com>
 * @date 2017年02月15日
 */
public class CountDownCircleView extends View {

	/**
	 * internal＝ 1000/25 ＝ 40；
	 * fps = 25
	 * 40毫秒一刷新
	 */
	private long interval = 40l;

	private long currentTimeInMill;
	/**
	 * 一圈几秒：20s
	 * 比如20s转完一圈
	 */
	private long oneCircleMill;
	/**
	 * 目标多少秒
	 */
	private long targetTimeInMill;

	private float currentSweepAngle = 0;

	private RectF mArcRect;

	private int circleColor = Color.BLACK;
	private int circleWidth = 10;

	private boolean mClockwise = true;

	private Paint paint;

	private CountDownTimer countDownTimer;

	public void pause() {
		if (null != countDownTimer) {
			countDownTimer.cancel();
		}
	}

	public void restart() {
		start(oneCircleMill, targetTimeInMill - currentTimeInMill);
	}

	public void start(long testOneCircleMill, long testTargetTimeInMill) {
		this.oneCircleMill = testOneCircleMill;
		this.targetTimeInMill = testTargetTimeInMill;

		countDownTimer = new CountDownTimer(targetTimeInMill, interval) {

			@Override
			public void onTick(long millisUntilFinished) {
				currentTimeInMill = targetTimeInMill - millisUntilFinished;
				long offset = currentTimeInMill % oneCircleMill;
				currentSweepAngle = (((float) offset / (float) oneCircleMill)) * 360f;
				invalidate();
				Log.d("xxx", "刷新:currentSweepAngle:" + currentSweepAngle);
			}

			@Override
			public void onFinish() {

			}
		};
		countDownTimer.start();
	}

	public CountDownCircleView(Context context) {
		this(context, null);
	}

	public CountDownCircleView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public CountDownCircleView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context, attrs);
	}

	@TargetApi(21)
	public CountDownCircleView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		init(context, attrs);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		mArcRect = null;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (null == mArcRect) {
			mArcRect = new RectF(getPaddingLeft(), getPaddingTop(), getWidth() - getPaddingRight(),
								 getHeight() - getPaddingBottom());
			float dxDy = (float) circleWidth / 2f;
			mArcRect.inset(dxDy, dxDy);
		}
		canvas.drawArc(mArcRect, -90, (mClockwise ? 1 : -1) * currentSweepAngle, false, paint);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int pleft = getPaddingLeft();
		int pright = getPaddingRight();
		int ptop = getPaddingTop();
		int pbottom = getPaddingBottom();

		int defaultWidth = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
		int defaultHeight = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);
		int circleDiameter = 0;
		if (0 == defaultWidth && 0 != defaultHeight) {
			int remainWidthForCircle = defaultWidth - pleft - pright;
			int remainHeightForCircle = defaultHeight - ptop - pbottom;
			circleDiameter = Math.min(remainWidthForCircle, remainHeightForCircle);
		} else if (0 == defaultWidth) {
			circleDiameter = defaultHeight - ptop - pbottom;
		} else {
			circleDiameter = defaultWidth - pleft - pright;
		}
		setMeasuredDimension(pleft + pright + circleDiameter, ptop + pbottom + circleDiameter);
	}

	private void init(Context context, AttributeSet attrs) {
		if (null != attrs) {
			//			TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MainHomeCircle);
			//			outerCircleWidth = a.getDimensionPixelSize(R.styleable.MainHomeCircle_outer_width, 4);
			//			centerCircleWidth = a.getDimensionPixelSize(R.styleable.MainHomeCircle_center_width, 15);
			//			innerCircleWidth = a.getDimensionPixelSize(R.styleable.MainHomeCircle_inner_width, 8);
			//			gap = a.getDimensionPixelSize(R.styleable.MainHomeCircle_circle_gap, 5);
			//			if (null != a) {
			//				a.recycle();
			//			}
		}
		paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setColor(circleColor);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(circleWidth);
	}
}
