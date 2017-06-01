package com.example.jiao.myapplication.views;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.*;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.View;
import com.example.jiao.myapplication.R;

/**
 * @author jiao, <wenliang.jiao@extantfuture.com>
 * @date 2017年01月22日
 */
public class MainHomeCircle extends View {

	private Context context;
	private int outerCircleWidth = 3;
	private int outerCircleColor = Color.WHITE;

	private int centerCircleWidth = 15;
	private int centerCircleColor = Color.parseColor("#50E3C2");

	private int innerCircleWidth = 10;
	private int innerCircleColor = Color.WHITE;

	private int gap = 5;

	private int progress = 100;

	private Rect outRect;

	private RectF centerRect;

	private Rect shadowRect;

	private GradientDrawable mDrawable;

	private boolean stopped = true;

	private ProgressListener progressListener;

	private float centerAlpha = 1f;

	public MainHomeCircle(Context context) {
		this(context, null);
	}

	public MainHomeCircle(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public MainHomeCircle(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context, attrs);
	}

	@TargetApi(21)
	public MainHomeCircle(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		init(context, attrs);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		outRect = null;
		centerRect = null;
		shadowRect = null;
		centerAlpha = 1;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		Paint paint = new Paint();
		paint.setStyle(Paint.Style.STROKE);
		paint.setColor(Color.parseColor("#40FFFFFF"));
		initOutRectIfNeed();
		canvas.drawRect(outRect, paint);

		//draw outer circle
		paint.setColor(outerCircleColor);
		paint.setStrokeWidth(outerCircleWidth);
		canvas.drawCircle(outRect.centerX(), outRect.centerY(), getOutRadius(), paint);

		//draw center progress circle
		if (stopped) {
			progress = 100;
			if (null != progressListener) {
				progressListener.getProgress(true, progress);
			}
		} else if (progress < 100) {
			progress += 1;
			invalidate();
			if (null != progressListener) {
				progressListener.getProgress(false, progress);
			}
		} else if (100 == progress) {
			stopped = true;
			if (null != progressListener) {
				progressListener.getProgress(false, progress);
			}
		}
		paint.setColor(centerCircleColor);
		paint.setStrokeWidth(centerCircleWidth);
		paint.setAlpha((int) (255 * centerAlpha));
		canvas.drawArc(getCenterRect(), -90, 360 * progress / 100, false, paint);

		paint.setAlpha(255);
		//draw inner circle
		paint.setColor(innerCircleColor);
		paint.setStrokeWidth(innerCircleWidth);
		canvas.drawCircle(outRect.centerX(), outRect.centerY(), getInnerRadius(), paint);

		//draw shadow
		mDrawable.setBounds(getShadowRect());
		mDrawable.setGradientRadius(getShadowRect().width() / 4 * 3);
		mDrawable.setGradientType(GradientDrawable.RADIAL_GRADIENT);
		mDrawable.draw(canvas);
	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		if (null != centerBlinkAnimator) {
			centerBlinkAnimator.cancel();
		}
	}

	private ObjectAnimator centerBlinkAnimator;

	private ObjectAnimator getCenterBlinkAnimator() {
		if (null == centerBlinkAnimator) {
			centerBlinkAnimator = ObjectAnimator.ofFloat(this, "centerAlpha", 0f, 1.0f);
			centerBlinkAnimator.setRepeatCount(ValueAnimator.INFINITE);
			centerBlinkAnimator.setRepeatMode(ValueAnimator.REVERSE);
			centerBlinkAnimator.setDuration(500);
		}
		return centerBlinkAnimator;
	}

	private void setCenterAlpha(float centerAlpha) {
		this.centerAlpha = centerAlpha;
		invalidate();
	}

	public void restartDraw() {
		stopped = false;
		if (progress == 100) {
			progress = 0;
		}
		invalidate();
	}

	public void stop() {
		if (!stopped) {
			stopped = true;
		}
		invalidate();
	}

	public void startBlink() {
		stop();
		ObjectAnimator objectAnimator = getCenterBlinkAnimator();
		if (!objectAnimator.isStarted()) {
			objectAnimator.start();
		}
	}

	public void stopBlink() {
		if (null != centerBlinkAnimator) {
			centerBlinkAnimator.cancel();
		}
		setCenterAlpha(1);
	}

	private RectF getCenterRect() {
		initOutRectIfNeed();
		if (null == centerRect) {
			int radius = outRect.width() / 2 - outerCircleWidth - gap - centerCircleWidth / 2;
			int left = outRect.centerX() - radius;
			int right = outRect.centerX() + radius;
			int top = outRect.centerY() - radius;
			int bottom = outRect.centerY() + radius;
			centerRect = new RectF(left, top, right, bottom);
		}
		return centerRect;
	}

	private Rect getShadowRect() {
		initOutRectIfNeed();
		if (null == shadowRect) {
			int radius = outRect.width() / 2 - outerCircleWidth - gap - centerCircleWidth - gap - innerCircleWidth;
			int offset = 2;
			int left = outRect.centerX() - radius + offset;
			int right = outRect.centerX() + radius - offset;
			int top = outRect.centerY() - radius + offset;
			int bottom = outRect.centerY() + radius - offset;
			shadowRect = new Rect(left, top, right, bottom);
		}
		return shadowRect;
	}

	private int getInnerRadius() {
		initOutRectIfNeed();
		return outRect.width() / 2 - outerCircleWidth - gap - centerCircleWidth - gap - innerCircleWidth / 2;
	}

	private int getOutRadius() {
		initOutRectIfNeed();
		return outRect.width() / 2 - outerCircleWidth / 2;
	}

	private void initOutRectIfNeed() {
		if (null == outRect) {
			outRect = new Rect(getPaddingLeft(), getPaddingTop(), getWidth() - getPaddingRight(), getHeight() - getPaddingBottom());
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int pleft = getPaddingLeft();
		int pright = getPaddingRight();
		int ptop = getPaddingTop();
		int pbottom = getPaddingBottom();

		int defaultWidth = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
		int defaultHeight = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);

		int remainWidthForCircle = defaultWidth - pleft - pright;
		int remainHeightForCircle = defaultHeight - ptop - pbottom;

		int circleDiameter = Math.min(remainWidthForCircle, remainHeightForCircle);

		setMeasuredDimension(pleft + pright + circleDiameter, ptop + pbottom + circleDiameter);
	}

	private void init(Context context, AttributeSet attrs) {
		this.context = context;
//		if (null != attrs) {
//			TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MainHomeCircle);
//			if (null != a) {
//				a.recycle();
//			}
//		}
		mDrawable = new GradientDrawable(GradientDrawable.Orientation.TL_BR, new int[] { 0x00000000, 0x61C1C2C3 });
		mDrawable.setShape(GradientDrawable.OVAL);
	}

	public interface ProgressListener {

		void getProgress(boolean forced, int progress);
	}
}
