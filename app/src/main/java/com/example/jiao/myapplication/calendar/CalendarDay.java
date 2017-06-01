package com.example.jiao.myapplication.calendar;

/**
 * Created by jiao on 16/7/18.
 */
public class CalendarDay {

	public int year;
	public int month;
	public int day;

	public CalendarDay(int year, int month, int day) {
		this.year = year;
		this.month = month;
		this.day = day;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof CalendarDay) {
			CalendarDay other = (CalendarDay) o;
			if (other.year == this.year && other.month == this.month && other.day == this.day) {
				return true;
			}
		}
		return false;
	}

	public int getId() {
		int id = (year << 16) + (month << 8) + day;
		return id;
	}
}
