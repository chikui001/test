package com.example.jiao.myapplication.calendar;

import java.util.Calendar;
import java.util.Comparator;
import java.util.List;
import java.util.TreeMap;

/**
 * Created by jiao on 16/7/18.
 */
public class CalendarController {

	private int startYear;
	private int endYear;
	//月份按生活中的月份
	private int startMonth;
	//月份按生活中的月份
	private int endMonth;
	private int startDay;
	private int endDay;

	/**
	 * key : {@link CalendarDay#getId()}
	 */
	private TreeMap<Integer, CalendarDay> hasDataCalendarDays = new TreeMap<Integer, CalendarDay>(new Comparator<Integer>() {

		@Override
		public int compare(Integer lhs, Integer rhs) {
			return rhs - lhs;
		}
	});

	private CalendarDay selectedCalendarDay;

	/**
	 * 有数据才能点
	 *
	 * @param someDay
	 * @return
	 */
	public boolean ableToClick(CalendarDay someDay) {
		if (null != hasDataCalendarDays && hasDataCalendarDays.containsKey(Integer.valueOf(someDay.getId())) && 0 != someDay
				.getId()) {
			return true;
		}
		return false;
	}

	public void pushHasDataDayTs(List<Integer> tsList) {
		if (null != tsList) {
			for (int i = 0; i < tsList.size(); i++) {
				int ts = tsList.get(i);
				Calendar calendar = Calendar.getInstance();
				calendar.setTimeInMillis(TimeUtil.getMillsFromSeconds(ts));
				int year = calendar.get(Calendar.YEAR);
				int month = calendar.get(Calendar.MONTH) + 1;
				int day = calendar.get(Calendar.DAY_OF_MONTH);
				CalendarDay calendarDay = new CalendarDay(year, month, day);
				int id = calendarDay.getId();
				hasDataCalendarDays.put(id, calendarDay);
			}
		}
	}

	public void pushHasDataDay() {
		CalendarDay calendarDay1 = new CalendarDay(2016, 4, 11);
		CalendarDay calendarDay2 = new CalendarDay(2016, 5, 12);
		CalendarDay calendarDay3 = new CalendarDay(2016, 6, 13);
		CalendarDay calendarDay4 = new CalendarDay(2016, 6, 14);
		CalendarDay calendarDay5 = new CalendarDay(2016, 6, 15);
		CalendarDay calendarDay6 = new CalendarDay(2016, 7, 16);
		CalendarDay calendarDay7 = new CalendarDay(2016, 8, 1);
		hasDataCalendarDays.put(calendarDay1.getId(), calendarDay1);
		hasDataCalendarDays.put(calendarDay2.getId(), calendarDay2);
		hasDataCalendarDays.put(calendarDay3.getId(), calendarDay3);
		hasDataCalendarDays.put(calendarDay4.getId(), calendarDay4);
		hasDataCalendarDays.put(calendarDay5.getId(), calendarDay5);
		hasDataCalendarDays.put(calendarDay6.getId(), calendarDay6);
		hasDataCalendarDays.put(calendarDay7.getId(), calendarDay7);

	}

	public TreeMap<Integer, CalendarDay> getHasDataCalendarDays() {
		return hasDataCalendarDays;
	}

	public void setSelectedCalendarDay(CalendarDay selectedCalendarDay) {
		this.selectedCalendarDay = selectedCalendarDay;
	}

	public CalendarDay getSelectedCalendarDay() {
		return selectedCalendarDay;
	}

	public CalendarController(int startYear, int startMonth, int startDay, int endYear, int endMonth, int endDay) {
		assertIfStartEndTimeIllegal(startYear, startMonth, startDay, endYear, endMonth, endDay);
		this.startYear = startYear;
		this.endYear = endYear;
		this.startMonth = startMonth;
		this.endMonth = endMonth;
		this.startDay = startDay;
		this.endDay = endDay;
		pushHasDataDay();
	}

	private void assertIfStartEndTimeIllegal(int startYear, int startMonth, int startDay, int endYear, int endMonth, int endDay) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(startYear, startMonth - 1, startDay);
		int startMaxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		if (startDay <= 0 || startDay > startMaxDay) {
			throw new IllegalArgumentException("开始天越界");
		}

		long startTs = calendar.getTimeInMillis();
		calendar.set(endYear, endMonth - 1, endDay);
		int endMaxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		long endTs = calendar.getTimeInMillis();
		if (endDay <= 0 || endDay > endMaxDay) {
			throw new IllegalArgumentException("结束天越界");
		}
		if (startTs > endTs) {
			throw new IllegalArgumentException("结束时间必须大于开始时间");
		}
	}

	public void initCalendarMonth(CalendarMonth calendarMonth) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, startYear);
		calendar.set(Calendar.MONTH, startMonth - 1);

		long startTs = calendar.getTimeInMillis();

		calendar.set(Calendar.YEAR, endYear);
		calendar.set(Calendar.MONTH, endMonth - 1);
		long endTs = calendar.getTimeInMillis();

		calendar.set(Calendar.YEAR, calendarMonth.getYear());
		calendar.set(Calendar.MONTH, calendarMonth.getMonth() - 1);
		long currentMonthTs = calendar.getTimeInMillis();
		if (currentMonthTs < startTs) {
			throw new IllegalArgumentException("当前月小于开始时间");
		}
		if (currentMonthTs > endTs) {
			throw new IllegalArgumentException("当前月大于结束时间");
		}
		//和开始月份同月
		if (currentMonthTs == startTs) {
			calendarMonth.setStartDay(startDay);
		} else {
			calendarMonth.setStartDay(1);
		}
		if (currentMonthTs == endTs) {
			calendarMonth.setEndDay(endDay);
		} else {
			int endDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
			calendarMonth.setEndDay(endDay);
		}
		calendarMonth.setTwoDimensionDayArray(CalendarUtils.generateCalendarDays(calendarMonth));
	}
}
