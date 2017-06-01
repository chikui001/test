package com.example.jiao.myapplication.views;

import android.content.Context;
import android.graphics.Color;

import java.util.Calendar;

/**
 * 日历颜色样式
 *
 * @author WangShanShan
 * @explain
 * @date 2012-12-14
 */
public class DayStyle {

	// methods
	private static String[] getWeekDayNames(Context context) {
		String[] vec = new String[10];

		vec[Calendar.SUNDAY] = "SUN";
		vec[Calendar.MONDAY] = "MON";
		vec[Calendar.TUESDAY] = "TUE";
		vec[Calendar.WEDNESDAY] = "WED";
		vec[Calendar.THURSDAY] = "THU";
		vec[Calendar.FRIDAY] = "FRI";
		vec[Calendar.SATURDAY] = "SAT";
		return vec;
	}

	public static String getWeekDayName(int iDay, Context context) {
		String[] vecStrWeekDayNames = getWeekDayNames(context);
		return vecStrWeekDayNames[iDay];
	}

	// fields
	public final static int iColorFrameHeader = 0xffcccccc;
	public final static int iColorFrameHeaderHoliday = 0xffcccccc;
	public final static int iColorTextHeader = 0xff000000;

	public final static int iColorText = 0xff333333;
	public final static int iColorBkg = 0xffffffff;
	public final static int iColorBkgHoliday = 0xffffffff;

	public final static int iColorTextToday = 0xff002200;
	public final static int iColorBkgToday = 0xff88bb88;

	public final static int iColorTextSelected = 0xff001122;

	public final static int iColorTextFocused = 0xff221100;

	// methods
	public static int getColorFrameHeader(boolean bHoliday) {
		if (bHoliday) {
			return iColorFrameHeaderHoliday;
		}
		return iColorFrameHeader;
	}

	public static int getColorTextHeader(boolean bHoliday, int iWeekDay) {
		if (bHoliday) {
			if (iWeekDay == Calendar.SATURDAY)
				return Color.RED;
			else
				return Color.RED;
		}
		return iColorTextHeader;
	}

	public static int getColorText(boolean bHoliday, boolean bToday, int iDayOfWeek) {
		if (bToday)
			return iColorTextToday;
		if (bHoliday) {
			if (iDayOfWeek == Calendar.SATURDAY) {
				return Color.RED;
			} else
				return Color.RED;
		}
		return iColorText;
	}

	public static int getColorBkg(boolean bHoliday, boolean bToday) {
		if (bToday)
			return iColorBkgToday;
		if (bHoliday)
			return iColorBkgHoliday;
		return iColorBkg;
	}

	public static int getWeekDay(int index, int iFirstDayOfWeek) {
		int iWeekDay = -1;
		if (iFirstDayOfWeek == Calendar.MONDAY) {
			iWeekDay = index + Calendar.MONDAY;
			if (iWeekDay > Calendar.SATURDAY)
				iWeekDay = Calendar.SUNDAY;
		}
		if (iFirstDayOfWeek == Calendar.SUNDAY) {
			iWeekDay = index + Calendar.SUNDAY;
		}
		return iWeekDay;
	}

}
