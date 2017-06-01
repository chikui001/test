package com.example.jiao.myapplication.views;

import android.content.Context;
import android.graphics.*;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.LinearLayout.LayoutParams;
import com.example.jiao.myapplication.R;

import java.util.Calendar;

/**
 * 日期内容部分
 *
 * @author WangShanShan
 * @explain
 * @date 2012-12-14
 */
public class DateWidgetDayCell extends View {

	// types
	public interface OnItemClick {

		public void OnClick(DateWidgetDayCell item);
	}

	public static int ANIM_ALPHA_DURATION = 100;
	// fields
	private final static float fTextSize = 32;
	private final static float fGoGoTextSize = 25;
	private final static int iMargin = -20;
	private final static int iAlphaInactiveMonth = 0x88;

	// fields
	private int iDateYear = 0;
	private int iDateMonth = 0;
	private int iDateDay = 0;
	private int iDayOfWeek = 0;

	// fields
	private OnItemClick itemClick = null;
	private Paint pt = new Paint();
	private Paint paint = new Paint();
	private RectF rect = new RectF();
	private String sDate = "";

	// fields
	private boolean bSelected = false;
	private boolean bIsActiveMonth = false;
	private boolean bToday = false;
	private boolean bHoliday = false;
	private boolean bTouchedDown = false;

	private Context mContext;

	private int planTimes = -1;

	// methods
	public DateWidgetDayCell(Context context, int iWidth, int iHeight) {
		super(context);
		mContext = context;
		setFocusable(true);
		setLayoutParams(new LayoutParams(iWidth, iHeight));
		pt.setTextAlign(Paint.Align.CENTER);
		pt.setTypeface(null);
		pt.setAntiAlias(true);
		pt.setShader(null);
		pt.setFakeBoldText(true);
		pt.setTextSize(fTextSize);
		pt.setUnderlineText(false);

		paint.setTextAlign(Paint.Align.CENTER);
		paint.setTypeface(null);
		paint.setAntiAlias(true);
		paint.setShader(null);
		paint.setFakeBoldText(true);
		paint.setTextSize(fGoGoTextSize);
		paint.setUnderlineText(false);
	}

	public boolean getSelected() {
		return this.bSelected;
	}

	@Override
	public void setSelected(boolean bEnable) {
		if (this.bSelected != bEnable) {
			this.bSelected = bEnable;
			this.invalidate();
		}
	}

	public void setPlanTimes(int planTimes) {
		this.planTimes = planTimes;
	}

	public void setData(int iYear, int iMonth, int iDay, boolean bToday, boolean bHoliday, int iActiveMonth, int iDayOfWeek) {
		iDateYear = iYear;
		iDateMonth = iMonth;
		iDateDay = iDay;

		this.sDate = Integer.toString(iDateDay);
		this.bIsActiveMonth = (iDateMonth == iActiveMonth);
		this.bToday = bToday;
		this.bHoliday = bHoliday;
		this.iDayOfWeek = iDayOfWeek;
	}

	public void setItemClick(OnItemClick itemClick) {
		this.itemClick = itemClick;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		boolean bResult = super.onKeyDown(keyCode, event);
		if ((keyCode == KeyEvent.KEYCODE_DPAD_CENTER) || (keyCode == KeyEvent.KEYCODE_ENTER)) {
			doItemClick();
		}
		return bResult;
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		boolean bResult = super.onKeyUp(keyCode, event);
		return bResult;
	}

	public void doItemClick() {
		if (itemClick != null)
			itemClick.OnClick(this);
	}

	@Override
	protected void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {
		super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
		invalidate();
	}

	public Calendar getDate() {
		Calendar calDate = Calendar.getInstance();
		calDate.clear();
		calDate.set(Calendar.YEAR, iDateYear);
		calDate.set(Calendar.MONTH, iDateMonth);
		calDate.set(Calendar.DAY_OF_MONTH, iDateDay);
		return calDate;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		// init rectangles
		rect.set(0, 0, this.getWidth(), this.getHeight());
		rect.inset(1, 1);

		// drawing
		final boolean bFocused = IsViewFocused();

		drawDayView(canvas, bFocused);
		drawDayNumber(canvas, bFocused);
	}

	private void drawDayView(Canvas canvas, boolean bFocused) {
		//		if (bSelected || bFocused) {
		//			LinearGradient lGradBkg = null;
		//
		//			if (bFocused) {
		//				lGradBkg = new LinearGradient(rect.left, 0, rect.right, 0,
		//						DayStyle.iColorBkgFocusDark,
		//						DayStyle.iColorBkgFocusLight, Shader.TileMode.CLAMP);
		//			}
		//
		//			if (bSelected) {
		//				lGradBkg = new LinearGradient(rect.left, 0, rect.right, 0,
		//						DayStyle.iColorBkgSelectedDark,
		//						DayStyle.iColorBkgSelectedLight, Shader.TileMode.MIRROR);
		//			}
		//
		//			if (lGradBkg != null) {
		//				pt.setShader(lGradBkg);
		//				canvas.drawRect(rect, pt);
		//			}
		//
		//			pt.setShader(null);
		//		} else {
		//
		//			pt.setColor(DayStyle.getColorBkg(bHoliday, bToday));
		//			if (!bIsActiveMonth)
		//				pt.setAlpha(iAlphaInactiveMonth);
		//			canvas.drawRect(rect, pt);
		//		}
		this.setBackgroundResource(R.mipmap.calendar_button_calendar01);

		if (bToday) {
			//			canvas.drawBitmap(((BitmapDrawable) getResources().getDrawable(R.drawable.calendar_today)).getBitmap(), 0, 0, pt);
			this.setBackgroundResource(R.mipmap.calendar_button_calendar02);
		}
		if (bSelected) {
			this.setBackgroundResource(R.mipmap.calendar_button_calendar02_click);
		}
	}

	public void drawDayNumber(Canvas canvas, boolean bFocused) {
		// draw day number
		if (bToday)
			pt.setUnderlineText(true);

		Paint.FontMetricsInt fontMetrics = pt.getFontMetricsInt();
		Paint.FontMetricsInt gogoFontMetrics = paint.getFontMetricsInt();

		int baseline = (int) (rect.top + (rect.bottom - rect.top - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top);
		int gogoBaseline = (int) (rect.top + (rect.bottom - rect.top - gogoFontMetrics.bottom + gogoFontMetrics.top) / 2
				- gogoFontMetrics.top);
		// draw text
		if (bSelected || bFocused) {
			if (bSelected)
				pt.setColor(DayStyle.iColorTextSelected);
			if (bFocused)
				pt.setColor(DayStyle.iColorTextFocused);
		} else {
			pt.setColor(DayStyle.getColorText(bHoliday, bToday, iDayOfWeek));
		}

		if (!bIsActiveMonth)
			pt.setAlpha(iAlphaInactiveMonth);
		paint.setAlpha(iAlphaInactiveMonth);

		canvas.drawText(sDate, rect.centerX(), baseline + iMargin, pt);
		if (planTimes != -1) {
			paint.setColor(Color.GREEN);
			canvas.drawText("第" + planTimes + "次", rect.centerX(), gogoBaseline - iMargin, paint);
		} else {
			if (bToday) {
				paint.setColor(Color.RED);
				canvas.drawText("Today", rect.centerX(), gogoBaseline - iMargin, paint);
			}
		}
		pt.setUnderlineText(false);
	}

	public boolean IsViewFocused() {
		return (this.isFocused() || bTouchedDown);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		boolean bHandled = false;
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			bHandled = true;
			bTouchedDown = true;
			invalidate();
			startAlphaAnimIn(DateWidgetDayCell.this);
		}
		if (event.getAction() == MotionEvent.ACTION_CANCEL) {
			bHandled = true;
			bTouchedDown = false;
			invalidate();
		}
		if (event.getAction() == MotionEvent.ACTION_UP) {
			bHandled = true;
			bTouchedDown = false;
			invalidate();
			doItemClick();
		}
		return bHandled;
	}

	public static void startAlphaAnimIn(View view) {
		AlphaAnimation anim = new AlphaAnimation(0.5F, 1);
		anim.setDuration(ANIM_ALPHA_DURATION);
		anim.startNow();
		view.startAnimation(anim);
	}

	public int getiDateMonth() {
		return iDateMonth;
	}

	public void setiDateMonth(int iDateMonth) {
		this.iDateMonth = iDateMonth;
	}

}
