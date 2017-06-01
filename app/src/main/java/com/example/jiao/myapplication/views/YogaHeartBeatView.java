package com.example.jiao.myapplication.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import com.extantfuture.util.CollectionUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 瑜伽结果页，曲线
 *
 * @author jiao, <wenliang.jiao@extantfuture.com>
 * @date 2017年02月17日
 */
public class YogaHeartBeatView extends View {

	/**
	 * 默认竖直朝向
	 */
	private boolean orientationPortrait = true;
	/**
	 * y轴刻度值，pixel/value
	 * x轴刻度值
	 */
	private float xScale = 0;
	private float yScale = 0;

	/**
	 * y轴最大值
	 */
	private int yMaxValue = 160;

	/**
	 * y轴最小值
	 */
	private int yMinValue = 40;

	private int shadowMin = 80;
	private int shadowMax = 145;

	/**
	 * x轴一个格子是多少value
	 */
	private int xPerGridValue = 6;

	/**
	 * portrait---->>>>>>>一屏，展示10.5个宽度，多处的0.5让用户了解到可以滑动
	 */
	private float numOfXGrid = 10.5f;

	private int realNumOfXGrid;

	/**
	 * 心率值
	 */
	private List<Integer> mValues = new ArrayList<>();

	private Paint paint;

	private int curveChartColor = Color.parseColor("#9013FE");
	private int shadowColor = Color.parseColor("#EFEAFA");
	private int verticalLineColor = Color.parseColor("#c6b5ed");
	private int verticalLineWidth = 4;
	private int mCurveSize = 4;

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		//高度横竖屏不变，specific
		int measuredHeight = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);
		int count = CollectionUtil.size(mValues);

		int parentWidth = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);

		realNumOfXGrid = count / xPerGridValue + 1;
		int minNumOfXGrid = (int) Math.ceil(numOfXGrid);
		if (realNumOfXGrid < minNumOfXGrid) {
			realNumOfXGrid = minNumOfXGrid;
		}
		xScale = (float) parentWidth / (numOfXGrid * (float) xPerGridValue);
		yScale = (float) measuredHeight / (float) (yMaxValue - yMinValue);

		int measuredWidth = (int) (realNumOfXGrid * xPerGridValue * xScale);
		Log.d("test-onMeasure", "realNumOfXGrid:" + realNumOfXGrid + ";xPerGridValue:" + xPerGridValue + ";xScale:" + xScale);
		setMeasuredDimension(measuredWidth, measuredHeight);
	}

	private PointF[] mPointFs;

	public void setmValues(List<Integer> mValues) {
		this.mValues = mValues;
		mPointFs = null;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		paint.setColor(shadowColor);
		float left = 0;
		float top = (int) ((yMaxValue - shadowMax) * yScale);
		float right = getMeasuredWidth();
		float bottom = (int) ((yMaxValue - shadowMin) * yScale);
		canvas.drawRect(left, top, right, bottom, paint);
		paint.setColor(verticalLineColor);
		paint.setStrokeWidth(verticalLineWidth);
		for (int i = 0; i < realNumOfXGrid; i++) {
			int startX = (int) (i * xPerGridValue * xScale);
			int startY = 0;
			int endX = startX;
			int endY = getHeight();
			canvas.drawLine(startX, startY, endX, endY, paint);
			Log.d("xxx", "startX:" + startX + ";startY:" + 0 + ";endX:" + endX + ";endY" + endY);
		}
		if (mPointFs == null && CollectionUtil.isNotEmpty(mValues)) {
			mPointFs = new PointF[mValues.size()];
			for (int i = 0; i < mValues.size(); i++) {
				int value = yMaxValue - mValues.get(i);
				float y = value * yScale;
				float x = i * xScale;
				mPointFs[i] = new PointF(x, y);
			}
		}
		if (null != mPointFs) {
			paint.setColor(curveChartColor);
			paint.setStrokeWidth(mCurveSize);
			for (int i = 0; i < mPointFs.length - 1; i++) {
				int first = mValues.get(i);
				int second = mValues.get(i + 1);
				if (numberIsAbnormalSection(first)) {
					if (((i + 1) == mPointFs.length - 1) && (numberIsNormalSection(second))) {
						canvas.drawPoint(mPointFs[i + 1].x, mPointFs[i + 1].y, paint);
					}
				} else if (numberIsAbnormalSection(second)) {
					canvas.drawPoint(mPointFs[i].x, mPointFs[i].y, paint);
				} else {
					canvas.drawLine(mPointFs[i].x, mPointFs[i].y, mPointFs[i + 1].x, mPointFs[i + 1].y, paint);
				}
			}
		}

	}

	private boolean numberIsAbnormalSection(int value) {
		return false;
	}

	private boolean numberIsNormalSection(int value) {
		return true;
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		Log.d("xxx", "left:" + left + ";top:" + top + ";right:" + right + ";bottom");
	}

	public YogaHeartBeatView(Context context) {
		this(context, null);
	}

	public YogaHeartBeatView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public YogaHeartBeatView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context, attrs);
	}

	@TargetApi(21)
	public YogaHeartBeatView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		init(context, attrs);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		mPointFs = null;
	}

	private void init(Context context, AttributeSet attrs) {
		paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setStyle(Paint.Style.FILL_AND_STROKE);
	}
}
