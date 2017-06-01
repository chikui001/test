package com.example.jiao.myapplication.calendar;

import com.extantfuture.util.StringUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by jiao on 16/7/18.
 */
public class CalendarUtils {

	/**
	 * 一周七天
	 * calendar.getMax
	 */
	public static final int MaxDayOfWeek = 7;

	public static CalendarDay[][] generateCalendarDays(CalendarMonth calendarMonth) {
		if (0 == calendarMonth.getStartDay() || 0 == calendarMonth.getStartDay()) {
			throw new IllegalArgumentException("you must get the accurate start and end day before generate twoDimensionDayArray");
		}
		Calendar calendarA = Calendar.getInstance();
		calendarA.set(calendarMonth.getYear(), calendarMonth.getMonth() - 1, calendarMonth.getStartDay());

		Calendar calendarB = Calendar.getInstance();
		calendarB.set(calendarMonth.getYear(), calendarMonth.getMonth() - 1, calendarMonth.getEndDay());

		LinkedHashMap<Integer, CalendarDay[]> map = new LinkedHashMap<>();
		while (calendarA.getTimeInMillis() <= calendarB.getTimeInMillis()) {
			int weekOfYear = calendarA.get(Calendar.WEEK_OF_YEAR);
			int dayOfWeek = calendarA.get(Calendar.DAY_OF_WEEK);

			if (!map.containsKey(Integer.valueOf(weekOfYear))) {
				map.put(Integer.valueOf(weekOfYear), new CalendarDay[CalendarUtils.MaxDayOfWeek]);
			}
			CalendarDay[] day = map.get(Integer.valueOf(weekOfYear));
			day[dayOfWeek - Calendar.SUNDAY] = new CalendarDay(calendarMonth.getYear(), calendarMonth.getMonth(),
															   calendarA.get(Calendar.DAY_OF_MONTH));
			calendarA.add(Calendar.DAY_OF_MONTH, 1);
		}
		List<Integer> weeks = new ArrayList<Integer>(map.keySet());
		CalendarDay[][] twoDimensionDayArray = new CalendarDay[weeks.size()][CalendarUtils.MaxDayOfWeek];
		for (int i = 0; i < weeks.size(); i++) {
			twoDimensionDayArray[i] = map.get(weeks.get(i));
		}
		return twoDimensionDayArray;
	}

	public static int isSomeCalendarDayIsFirstPregnantDay(CalendarDay calendarDay) {
		if (null != calendarDay) {
			int[] pregnantWeekDay = getPregnantWeekArray(calendarDay);
			if (null != pregnantWeekDay && 2 == pregnantWeekDay.length) {
				int week = pregnantWeekDay[0];
				int day = pregnantWeekDay[1];
				if (7 == day) {
					return week + 1;
				}
			}
		}
		return -1;
	}

	public static int[] getPregnantWeekArray(CalendarDay calendarDay) {
		if (null != calendarDay) {
			Calendar calendar = Calendar.getInstance();
			calendar.set(calendarDay.year, calendarDay.month, calendarDay.day);
			int specificTs = TimeUtil.getSecondsFromMills(TimeUtil.getZeroTsOfDay(calendar.getTimeInMillis()));
			int[] pregnantWeekDay = SubTimeUtil.getPregWeeksSpecificDays(specificTs);
			return pregnantWeekDay;
		}
		return null;
	}

	public static String getPregnantWeek(CalendarDay calendarDay) {
		if (null != calendarDay) {
			int[] pregnantWeekDay = getPregnantWeekArray(calendarDay);
			if (null != pregnantWeekDay && 2 == pregnantWeekDay.length) {
				int week = pregnantWeekDay[0];
				int day = pregnantWeekDay[1];
				if (7 == day) {
					return StringUtil.concat(week + 1, "周");
				} else {
					return StringUtil.concat(week, "周");
				}
			}
		}
		return null;
	}

}
