package com.example.jiao.myapplication.calendar;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;
import com.example.jiao.myapplication.R;
import com.example.jiao.myapplication.calendar.listener.CalendarClickListener;

import java.util.Calendar;

/**
 * Created by jiao on 16/7/16.
 */
public class MonthView extends View {

	/**
	 * {@link Calendar#JANUARY} <p>1月:0</p>
	 * {@link Calendar#FEBRUARY}<p>2月:1</p>
	 * {@link Calendar#MARCH}<p>3月:2</p>
	 * {@link Calendar#APRIL}<p>4月:3</p>
	 * {@link Calendar#MAY}<p>5月:4</p>
	 * {@link Calendar#JUNE}<p>6月:5</p>
	 * {@link Calendar#JULY}<p>7月:6</p>
	 * {@link Calendar#AUGUST}<p>8月:7</p>
	 * {@link Calendar#SEPTEMBER}<p>9月:8</p>
	 * {@link Calendar#OCTOBER}<p>10月:9</p>
	 * {@link Calendar#NOVEMBER}<p>11月:10</p>
	 * {@link Calendar#DECEMBER}<p>12月:11</p>
	 */
	private CalendarMonth calendarMonth;

	private Paint titlePaint;

	private Paint specialPaint;

	public void setCalendarController(CalendarController calendarController) {
		this.calendarController = calendarController;
	}

	private CalendarController calendarController;

	public MonthView(Context context) {
		super(context);

	}

	private int attrDayItemWidth;
	private int attrDayItemHeight;

	private int attrDayTitleItemWidth;
	private int attrDayTitleItemHeight;

	private int attrSelectedOvalRadius;

	private int attrSelectedOvalColor;

	private int attrDayTitleTextSize;

	private int attrDayTextSize;

	private int attrPregnantTextSize;

	private int attrTextColor;

	private int attrAbleClickTextColor;

	private int attrSelectedDayTextSize;

	private int attrSelectedPregnantTextSize;

	private int attrSelectedTextColor;

	private CalendarClickListener calendarClickListener;

	public MonthView(Context context, AttributeSet attrs) {
		super(context, attrs);

		titlePaint = new Paint();
		titlePaint.setAntiAlias(true);
		titlePaint.setColor(Color.BLACK);
		titlePaint.setStyle(Paint.Style.STROKE);
		titlePaint.setTextAlign(Paint.Align.CENTER);

		specialPaint = new Paint();
		specialPaint.setAntiAlias(true);
		specialPaint.setStyle(Paint.Style.FILL);
		specialPaint.setTextAlign(Paint.Align.CENTER);
		specialPaint.setTextSize(35);
		if (null != attrs) {
			TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MonthView);
			attrDayItemWidth = a.getDimensionPixelSize(R.styleable.MonthView_dayItemWidth, 20);
			attrDayItemHeight = a.getDimensionPixelSize(R.styleable.MonthView_dayItemHeight, 20);
			attrDayTitleItemWidth = a.getDimensionPixelSize(R.styleable.MonthView_dayTitleItemWidth, 20);
			attrDayTitleItemHeight = a.getDimensionPixelSize(R.styleable.MonthView_dayTitleItemHeight, 20);
			attrSelectedOvalRadius = a.getDimensionPixelSize(R.styleable.MonthView_selectedOvalRadius, 20);
			attrSelectedOvalColor = a.getColor(R.styleable.MonthView_selectedOvalColor, Color.parseColor("#50E3C2"));
			attrDayTitleTextSize = a.getDimensionPixelSize(R.styleable.MonthView_dayTitleTextSize, 20);

			attrDayTextSize = a.getDimensionPixelSize(R.styleable.MonthView_dayTextSize, 20);
			attrPregnantTextSize = a.getDimensionPixelSize(R.styleable.MonthView_pregnantTextSize, 20);
			attrTextColor = a.getColor(R.styleable.MonthView_textColor, Color.parseColor("#50E3C2"));
			attrAbleClickTextColor = a.getColor(R.styleable.MonthView_ableClickTextColor, Color.parseColor("#50E3C2"));

			attrSelectedDayTextSize = a.getDimensionPixelSize(R.styleable.MonthView_selectedDayTextSize, 20);
			attrSelectedPregnantTextSize = a.getDimensionPixelSize(R.styleable.MonthView_selectedPregnantTextSize, 20);

			attrSelectedTextColor = a.getColor(R.styleable.MonthView_selectedTextColor, Color.WHITE);

		}
	}

	public MonthView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	@TargetApi(21)
	public MonthView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_UP) {
			getDayFromLocation(event.getX(), event.getY());
		}
		return true;
	}

	private void getDayFromLocation(float eventX, float eventY) {
		for (int i = 0; i < dayRects.length; i++) {
			for (int j = 0; j < dayRects[i].length; j++) {
				if (dayRects[i][j].contains((int) eventX, (int) eventY)) {
					CalendarDay[][] twoDimensionDayArray = calendarMonth.getTwoDimensionDayArray();
					CalendarDay selectedCalendarDay = twoDimensionDayArray[i][j];
					if (null != selectedCalendarDay) {
						Toast.makeText(getContext(),
									   "year" + selectedCalendarDay.year + ";month" + selectedCalendarDay.month + ";day"
											   + selectedCalendarDay.day, Toast.LENGTH_SHORT).show();
						if (null != calendarClickListener) {
							calendarClickListener.onSelectedDay(selectedCalendarDay);
						}
						if (null != calendarController) {
							calendarController.setSelectedCalendarDay(selectedCalendarDay);
						}
						invalidate(dayRects[i][j]);
					}
					break;
				}
			}
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		checkRects();
		drawTitle(canvas);
		drawDays(canvas);
	}

	private void drawTitle(Canvas canvas) {
		titlePaint.setTextSize(attrDayTitleTextSize);
		for (int i = Calendar.SUNDAY - 1; i < Calendar.SATURDAY; i++) {
			String someDay = getSomeDay(i + 1);
			Rect rectNoData = new Rect();
			titlePaint.getTextBounds(someDay, 0, someDay.length(), rectNoData);
			int fontHeight = rectNoData.height();
			canvas.drawText(someDay, dayTitleRects[i].centerX(), dayTitleRects[i].centerY() + fontHeight / 2, titlePaint);
		}
	}

	private String getSomeDay(int day) {
		String dayStr = null;
		switch (day) {
			case Calendar.SUNDAY:
				dayStr = "日";
				break;
			case Calendar.MONDAY:
				dayStr = "一";
				break;
			case Calendar.TUESDAY:
				dayStr = "二";
				break;
			case Calendar.WEDNESDAY:
				dayStr = "三";
				break;
			case Calendar.THURSDAY:
				dayStr = "四";
				break;
			case Calendar.FRIDAY:
				dayStr = "五";
				break;
			case Calendar.SATURDAY:
				dayStr = "六";
				break;
		}
		return dayStr;
	}

	private void drawDays(Canvas canvas) {
		CalendarDay[][] twoDimensionDayArray = calendarMonth.getTwoDimensionDayArray();
		CalendarDay selectedCalendarDay = (null != calendarController) ? calendarController.getSelectedCalendarDay() : null;
		for (int i = 0; i < dayRects.length; i++) {
			for (int j = 0; j < dayRects[i].length; j++) {
				CalendarDay currentCalendarDay = twoDimensionDayArray[i][j];
				if (null != currentCalendarDay) {
					String dayStr = Integer.toString(currentCalendarDay.day);
					int pregnantWeek = CalendarUtils.isSomeCalendarDayIsFirstPregnantDay(currentCalendarDay);
					String pregnantWeekStr = pregnantWeek + "周";
					if (calendarController.ableToClick(currentCalendarDay)) {
						if (currentCalendarDay.equals(selectedCalendarDay)) {
							specialPaint.setColor(attrSelectedOvalColor);
							canvas.drawCircle(dayRects[i][j].centerX(), dayRects[i][j].centerY(), attrSelectedOvalRadius,
											  specialPaint);
							specialPaint.setColor(attrSelectedTextColor);
							if (-1 != pregnantWeek) {
								specialPaint.setTextSize(attrSelectedDayTextSize);
								Rect dayRect = new Rect();
								specialPaint.getTextBounds(dayStr, 0, dayStr.length(), dayRect);
								int dayFontHeight = dayRect.height();

								specialPaint.setTextSize(attrSelectedPregnantTextSize);
								Rect pregnantWeekRect = new Rect();
								specialPaint.getTextBounds(pregnantWeekStr, 0, pregnantWeekStr.length(), pregnantWeekRect);
								int pregnantFontHeight = pregnantWeekRect.height();

								int totalHeight = dayFontHeight + pregnantFontHeight;

								specialPaint.setTextSize(attrSelectedDayTextSize);
								canvas.drawText(dayStr, dayRects[i][j].centerX(),
												dayRects[i][j].centerY() + (dayFontHeight - totalHeight / 2), specialPaint);
								specialPaint.setTextSize(attrSelectedPregnantTextSize);
								canvas.drawText(pregnantWeekStr, dayRects[i][j].centerX(),
												dayRects[i][j].centerY() + totalHeight / 2, specialPaint);
							} else {
								specialPaint.setTextSize(attrSelectedDayTextSize);
								Rect dayRect = new Rect();
								specialPaint.getTextBounds(dayStr, 0, dayStr.length(), dayRect);
								int dayFontHeight = dayRect.height();
								canvas.drawText(dayStr, dayRects[i][j].centerX(), dayRects[i][j].centerY() + dayFontHeight / 2,
												specialPaint);

							}
						} else {
							specialPaint.setColor(attrAbleClickTextColor);
							if (-1 != pregnantWeek) {
								specialPaint.setTextSize(attrDayTextSize);
								Rect dayRect = new Rect();
								specialPaint.getTextBounds(dayStr, 0, dayStr.length(), dayRect);
								int dayFontHeight = dayRect.height();

								Rect pregnantWeekRect = new Rect();
								specialPaint.setTextSize(attrPregnantTextSize);
								specialPaint.getTextBounds(pregnantWeekStr, 0, pregnantWeekStr.length(), pregnantWeekRect);
								int pregnantFontHeight = pregnantWeekRect.height();

								int totalHeight = dayFontHeight + pregnantFontHeight;
								specialPaint.setTextSize(attrDayTextSize);
								canvas.drawText(dayStr, dayRects[i][j].centerX(),
												dayRects[i][j].centerY() + (dayFontHeight - totalHeight / 2), specialPaint);
								specialPaint.setTextSize(attrPregnantTextSize);
								canvas.drawText(pregnantWeekStr, dayRects[i][j].centerX(),
												dayRects[i][j].centerY() + totalHeight / 2, specialPaint);
							} else {
								specialPaint.setTextSize(attrDayTextSize);
								Rect dayRect = new Rect();
								specialPaint.getTextBounds(dayStr, 0, dayStr.length(), dayRect);
								int dayFontHeight = dayRect.height();
								canvas.drawText(dayStr, dayRects[i][j].centerX(), dayRects[i][j].centerY() + dayFontHeight / 2,
												specialPaint);
							}
						}
					} else {
						if (-1 != pregnantWeek) {
							specialPaint.setColor(attrTextColor);
							specialPaint.setTextSize(attrDayTextSize);
							Rect dayRect = new Rect();
							specialPaint.getTextBounds(dayStr, 0, dayStr.length(), dayRect);
							int dayFontHeight = dayRect.height();

							Rect pregnantWeekRect = new Rect();
							specialPaint.setTextSize(attrPregnantTextSize);
							specialPaint.getTextBounds(pregnantWeekStr, 0, pregnantWeekStr.length(), pregnantWeekRect);
							int pregnantFontHeight = pregnantWeekRect.height();

							int totalHeight = dayFontHeight + pregnantFontHeight;
							specialPaint.setTextSize(attrDayTextSize);
							canvas.drawText(dayStr, dayRects[i][j].centerX(),
											dayRects[i][j].centerY() + (dayFontHeight - totalHeight / 2), specialPaint);
							specialPaint.setTextSize(attrPregnantTextSize);
							canvas.drawText(pregnantWeekStr, dayRects[i][j].centerX(), dayRects[i][j].centerY() + totalHeight / 2,
											specialPaint);
						} else {
							specialPaint.setColor(attrTextColor);
							specialPaint.setTextSize(attrDayTextSize);
							Rect dayRect = new Rect();
							specialPaint.getTextBounds(dayStr, 0, dayStr.length(), dayRect);
							int dayFontHeight = dayRect.height();
							canvas.drawText(dayStr, dayRects[i][j].centerX(), dayRects[i][j].centerY() + dayFontHeight / 2,
											specialPaint);

						}
					}
				}
			}
		}
	}

	public void setCalendarMonth(CalendarMonth calendarMonth) {
		this.calendarMonth = calendarMonth;
		this.calendarController.initCalendarMonth(calendarMonth);
		invalidate();
	}

	private int dayItemWidth;

	private int dayItemHeight;

	private int dayTitleItemWidth;

	private int dayTitleItemHeight;

	private void checkRects() {
		if (null == calendarMonth || null == calendarMonth.getTwoDimensionDayArray()) {
			throw new IllegalArgumentException("you must init CalendarMonth before calculate draw Rect");
		}
		CalendarDay[][] twoDimensionDayArray = calendarMonth.getTwoDimensionDayArray();
		dayRects = new Rect[twoDimensionDayArray.length][CalendarUtils.MaxDayOfWeek];
		for (int i = 0; i < dayRects.length; i++) {
			for (int j = 0; j < dayRects[i].length; j++) {
				dayRects[i][j] = new Rect(getPaddingLeft() + j * dayItemWidth,
										  getPaddingTop() + dayTitleItemHeight + i * dayItemHeight,
										  getPaddingLeft() + (j + 1) * dayItemWidth,
										  getPaddingTop() + dayTitleItemHeight + (i + 1) * dayItemHeight);
			}
		}
		dayTitleRects = new Rect[CalendarUtils.MaxDayOfWeek];
		for (int i = 0; i < dayTitleRects.length; i++) {
			dayTitleRects[i] = new Rect(getPaddingLeft() + i * dayTitleItemWidth, getPaddingTop(),
										getPaddingLeft() + (i + 1) * dayTitleItemWidth, getPaddingTop() + dayTitleItemHeight);
		}
	}

	/**
	 * day rect
	 */
	private Rect[][] dayRects;

	/**
	 * week title rect
	 */
	private Rect[] dayTitleRects;

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec));
	}

	/**
	 * 宽被指定大小时，dayItemWidth计算获取
	 *
	 * @param specifiedWidth
	 */
	private void calculateDayItemWidthExactly(int specifiedWidth) {
		dayItemWidth = (specifiedWidth - getPaddingLeft() - getPaddingRight()) / CalendarUtils.MaxDayOfWeek;
		dayTitleItemWidth = dayItemWidth;
	}

	/**
	 * 宽没有指定大小时，使用attr中的设置
	 */
	private void calculateDayItemWidthUnSpecified() {
		dayItemWidth = attrDayItemWidth;
		dayTitleItemWidth = attrDayTitleItemWidth;
	}

	/**
	 * 高没有指定大小时，使用attr中的设置
	 */
	private void calculateDayItemHeightUnSpecified() {
		dayItemHeight = attrDayItemHeight;
		dayTitleItemHeight = attrDayTitleItemHeight;
	}

	private int measureWidth(int widthMeasureSpec) {
		int widthMeasureSize = MeasureSpec.getSize(widthMeasureSpec);
		int widthMeasureType = MeasureSpec.getMode(widthMeasureSpec);
		int width = widthMeasureSize;
		switch (widthMeasureType) {
			case MeasureSpec.EXACTLY:
				calculateDayItemWidthExactly(widthMeasureSize);
				break;
			case MeasureSpec.AT_MOST:
				break;
			case MeasureSpec.UNSPECIFIED:
				calculateDayItemWidthUnSpecified();
				width = getRealWidth();
				break;
		}
		return width;
	}

	private int getRealHeight() {
		return getPaddingTop() + getPaddingBottom() + dayTitleItemHeight
				+ calendarMonth.getTwoDimensionDayArray().length * dayItemHeight;
	}

	private int getRealWidth() {
		return getPaddingLeft() + getPaddingBottom() + CalendarUtils.MaxDayOfWeek * dayItemWidth;
	}

	/**
	 * 高，必须是wrap_content(不指定)，
	 *
	 * @param heightMeasureSpec
	 * @return
	 */
	private int measureHeight(int heightMeasureSpec) {
		int heightMeasureSize = MeasureSpec.getSize(heightMeasureSpec);
		calculateDayItemHeightUnSpecified();
		return getRealHeight();
	}
}
