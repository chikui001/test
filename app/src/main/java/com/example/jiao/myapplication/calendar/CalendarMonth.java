package com.example.jiao.myapplication.calendar;

/**
 * Created by jiao on 16/7/18.
 */
public class CalendarMonth {

	private int year;

	//月份按生活中的月份
	private int month;

	public int getStartDay() {
		return startDay;
	}

	public int getEndDay() {
		return endDay;
	}

	private int startDay;
	private int endDay;

	public CalendarDay[][] getTwoDimensionDayArray() {
		return twoDimensionDayArray;
	}

	public void setTwoDimensionDayArray(CalendarDay[][] twoDimensionDayArray) {
		this.twoDimensionDayArray = twoDimensionDayArray;
	}

	private CalendarDay[][] twoDimensionDayArray;

	public void setStartDay(int startDay) {
		this.startDay = startDay;
	}

	public void setEndDay(int endDay) {
		this.endDay = endDay;
	}

	public int getYear() {
		return year;
	}

	public int getMonth() {
		return month;
	}

	public CalendarMonth(int year, int month) {
		this.year = year;
		this.month = month;
	}

	public void setStartDayByMonthSelf() {

	}

	public void setEndDayByMonthSelf() {

	}
}
