// Copyright (c) 2015. ExtantFuture Inc. All Rights Reserved.
/**
 *
 */
package com.example.jiao.myapplication.calendar;

import com.extantfuture.util.CollectionUtil;
import com.extantfuture.util.StringUtil;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author jiao, <wenliang.jiao@extantfuture.com>
 * @date 2015年12月1日
 */
public class SubTimeUtil extends TimeUtil {

	private static final int TOTAL_PREGNANT_WEEKS = 39;

	private static final int WHICH_DAY_IS_FIRST_DAY = Calendar.MONDAY;

	/**
	 * 根据
	 */
	public static int[] getEnableExpectedTs() {
		int startTs = getSecondsFromMills(getCurrentDayZeroTs());
		int endTs = startTs + (int) TimeUnit.DAYS.toSeconds(279);
		return new int[] { startTs, endTs };
	}

	/**
	 * 孕10周
	 *
	 * @param specificTime
	 * @param strFormat
	 * @return
	 */
	public static String getPregWeeksString(String strFormat, int specificTime) {
		return String.format(strFormat, getPregWeeksSpecificDays(specificTime)[0]);
	}

	/**
	 * 获取给定天的那一孕周的第一天
	 *
	 * @param specificTime
	 * @return
	 */
	public static int getFirstDayPregWeeks(int specificTime) {
		int expectedTime = expectedBornTs;
		return SubTimeUtil.getTsBaseWeeksDays(expectedTime, SubTimeUtil.getPregWeeksSpecificDays(expectedTime, specificTime)[0], 1);
	}

	/**
	 * 获取给定天的那一孕周的第一天
	 *
	 * @param expectedTime
	 * @param specificTime
	 * @return
	 */
	public static int getFirstDayPregWeeks(int expectedTime, int specificTime) {
		return SubTimeUtil.getTsBaseWeeksDays(expectedTime, SubTimeUtil.getPregWeeksSpecificDays(expectedTime, specificTime)[0], 1);
	}

	/**
	 * 获取指定天的前一孕周的第一天
	 *
	 * @param currentTs
	 * @return
	 */
	public static int getFirstDayOfBeforePregWeeks(int currentTs) {
		int expectedTime = expectedBornTs;
		return getFirstDayOfBeforePregWeeks(expectedTime, currentTs);
	}

	/**
	 * 获取指定天的前一孕周的第一天
	 *
	 * @param expectedTime
	 * @param specificTime
	 * @return
	 */
	public static int getFirstDayOfBeforePregWeeks(int expectedTime, int specificTime) {
		return SubTimeUtil
				.getTsBaseWeeksDays(expectedTime, SubTimeUtil.getPregWeeksSpecificDays(expectedTime, specificTime)[0] - 1, 1);
	}

	/**
	 * 获取当前孕周第一天时间戳
	 *
	 * @return
	 */
	public static int getFirstDayOfCurrentPregWeeks() {
		int expectedTime = expectedBornTs;
		return getFirstDayOfCurrentPregWeeks(expectedTime);
	}

	/**
	 * 获取当前孕周第一天时间戳
	 *
	 * @param expectedTime
	 * @return
	 */
	public static int getFirstDayOfCurrentPregWeeks(int expectedTime) {// 1452096000--42/0
		return SubTimeUtil.getTsBaseWeeksDays(expectedTime, SubTimeUtil.getPregWeeksDays(expectedTime)[0], 1);
	}

	/**
	 * 根据预产期和孕周，孕天求那一天的时候
	 *
	 * @param weeks
	 * @param days
	 * @return
	 */
	public static int getTsBaseWeeksDays(int weeks, int days) {
		int expectedTime = expectedBornTs;
		return getTsBaseWeeksDays(expectedTime, weeks, days);
	}

	/**
	 * 根据预产期和孕周，孕天求那一天的时候
	 * fixed
	 *
	 * @param expectedTime
	 * @param weeks
	 * @param days
	 * @return
	 */
	public static int getTsBaseWeeksDays(int expectedTime, int weeks, int days) {
		// 预产期对当前不是0点，转成0点
		int timeZoneRelatedExpectedTime = getZeroTsOfDay(expectedTime);
		if (weeks >= 40) {
			return timeZoneRelatedExpectedTime + (int) ((weeks - 40) * TimeUnit.DAYS.toSeconds(7) + days * TimeUnit.DAYS
					.toSeconds(1));
		}
		int weeksOffset = (int) ((TOTAL_PREGNANT_WEEKS - (weeks)) * TimeUnit.DAYS.toSeconds(7) + (7 - days) * TimeUnit.DAYS
				.toSeconds(1));
		return timeZoneRelatedExpectedTime - weeksOffset;
	}

	/**
	 * 获取当前孕周时间
	 *
	 * @return 如 孕18周第6天
	 */
	public static String getCurrentPregWeekDay() {
		int[] pair = getPregWeeksDays();
		if (null != pair && 2 == pair.length) {
			int week = pair[0];
			int day = pair[1];
			if (week > 0 || day > 0) {
				if (7 == day) {
					return StringUtil.concat("孕", week + 1, "周整");
				} else {
					return StringUtil.concat("孕", week, "周第", day, "天");
				}
			}
		}
		return null;
	}

	public static String getPregWeeksSpecificDaysStr(int ts) {
		int[] pair = getPregWeeksSpecificDays(ts);
		if (null != pair && 2 == pair.length) {
			int week = pair[0];
			int day = pair[1];
			if (week > 0 || day > 0) {
				if (7 == day) {
					return StringUtil.concat("孕", week + 1, "周整");
				} else {
					return StringUtil.concat("孕", week, "周第", day, "天");
				}
			}
		}
		return null;
	}

	/**
	 * 获取当前孕周孕天
	 *
	 * @return
	 */
	public static int[] getPregWeeksDays() {
		return getPregWeeksDays(expectedBornTs);
	}

	/**
	 * 获取当前孕周孕天
	 *
	 * @param expectedTime
	 * @return
	 */
	public static int[] getPregWeeksDays(int expectedTime) {
		int specificTime = getSecondsFromMills(getCurrentDayZeroTs());
		return getPregWeeksSpecificDays(expectedTime, specificTime);
	}

	private static int expectedBornTs;

	private static int registerTs;

	/**
	 * 获取指定天的孕周和孕天
	 *
	 * @param specificTime
	 * @return
	 */
	public static int[] getPregWeeksSpecificDays(int specificTime) {
		int expectedTime = expectedBornTs;
		return getPregWeeksSpecificDays(expectedTime, specificTime);
	}

	/**
	 * 获取指定天的孕周和孕天
	 * <p/>
	 * fixed
	 *
	 * @param expectedTime
	 * @param specificTime
	 * @return
	 */
	public static int[] getPregWeeksSpecificDays(int expectedTime, int specificTime) {
		// 预产期对当前不是0点，转成0点
		int timeZoneRelatedExpectedTime = getZeroTsOfDay(expectedTime);
		if (specificTime < timeZoneRelatedExpectedTime) {
			int dayCount = (int) Math.ceil((timeZoneRelatedExpectedTime - specificTime) / (1.0 * 24 * 3600));
			int week = 39 - dayCount / 7;
			int day = 7 - dayCount % 7;
			return new int[] { week, day };
		} else {
			int passedDayCount = (int) Math.floor((specificTime - timeZoneRelatedExpectedTime) / (1.0 * 24 * 3600));// 59174
			int week = passedDayCount / 7;
			int day = passedDayCount % 7;
			if (day == 0) {
				return new int[] { week + 39, 7 };
			}
			return new int[] { week + 40, day };
		}
	}

	/**
	 * 指定的两天是同孕周
	 *
	 * @param stringValue
	 * @param stringValue2
	 * @return
	 */
	public static boolean twoDateIsSamePregWeek(int stringValue, int stringValue2) {
		int expectedTime = expectedBornTs;
		return twoDateIsSamePregWeek(expectedTime, stringValue, stringValue2);
	}

	/**
	 * 指定的两天是同孕周
	 *
	 * @param expectedTime
	 * @param stringValue
	 * @param stringValue2
	 * @return
	 */
	public static boolean twoDateIsSamePregWeek(int expectedTime, int stringValue, int stringValue2) {
		int pregWeek1 = getPregWeeksSpecificDays(stringValue)[0];
		int pregWeek2 = getPregWeeksSpecificDays(stringValue2)[0];
		return pregWeek1 == pregWeek2 ? true : false;
	}

	/**
	 * @param stringValue
	 * @param stringValue2
	 * @return
	 */
	public static boolean twoDateIsSameDay(int stringValue, int stringValue2) {
		return twoDateIsSameDay(new Date(getMillsFromSeconds(stringValue)), new Date(getMillsFromSeconds(stringValue2)));
	}

	/**
	 * @param stringValue
	 * @param stringValue2
	 * @return
	 * @deprecated
	 */
	public static boolean twoDateIsSameWeek(int stringValue, int stringValue2) {
		return twoDateIsSameWeek(new Date(getMillsFromSeconds(stringValue)), new Date(getMillsFromSeconds(stringValue2)));
	}

	public static boolean twoDateIsSameDay(Date dateA, Date dateB) {
		Calendar calendarA = Calendar.getInstance();
		Calendar calendarB = Calendar.getInstance();
		calendarA.setTime(dateA);
		calendarB.setTime(dateB);
		int aYear = calendarA.get(Calendar.YEAR);
		int bYear = calendarB.get(Calendar.YEAR);

		int aDay = calendarA.get(Calendar.DAY_OF_YEAR);
		int bDay = calendarB.get(Calendar.DAY_OF_YEAR);
		return aYear == bYear && aDay == bDay;
	}

	/**
	 * @deprecated
	 */
	public static int getFirstDayCurrentWeek() {
		Calendar calendar = Calendar.getInstance();
		calendar.setFirstDayOfWeek(WHICH_DAY_IS_FIRST_DAY);
		calendar.set(Calendar.DAY_OF_WEEK, WHICH_DAY_IS_FIRST_DAY);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return getSecondsFromMills(calendar.getTimeInMillis());
	}

	/**
	 * @deprecated
	 */
	public static int getFirstDaySpecific(int time) {
		Calendar calendar = Calendar.getInstance();
		calendar.setFirstDayOfWeek(WHICH_DAY_IS_FIRST_DAY);
		calendar.setTimeInMillis(getMillsFromSeconds(time));
		calendar.set(Calendar.DAY_OF_WEEK, WHICH_DAY_IS_FIRST_DAY);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return getSecondsFromMills(calendar.getTimeInMillis());
	}

	/**
	 * @deprecated
	 */
	public static int getBeforeWeekFirstDay(int mCurrentTs) {
		Calendar calendar = Calendar.getInstance();
		calendar.setFirstDayOfWeek(WHICH_DAY_IS_FIRST_DAY);
		calendar.setTimeInMillis(getMillsFromSeconds(mCurrentTs));
		calendar.set(Calendar.DAY_OF_WEEK, WHICH_DAY_IS_FIRST_DAY);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.add(Calendar.WEEK_OF_YEAR, -1);
		return getSecondsFromMills(calendar.getTimeInMillis());
	}

	public static int getWeeksBetween(Date a, Date b) {

		if (b.before(a)) {
			return -getWeeksBetween(b, a);
		}
		a = resetTime(a);
		b = resetTime(b);

		Calendar calA = new GregorianCalendar();
		calA.setFirstDayOfWeek(WHICH_DAY_IS_FIRST_DAY);
		calA.setTime(a);
		int weeks = 0;

		Calendar calB = new GregorianCalendar();
		calB.setFirstDayOfWeek(WHICH_DAY_IS_FIRST_DAY);
		calB.setTime(b);

		while (calA.getTime().before(b) && calA.get(Calendar.WEEK_OF_YEAR) != calB.get(Calendar.WEEK_OF_YEAR)) {
			// add another week
			calA.add(Calendar.WEEK_OF_YEAR, 1);
			weeks++;
		}
		return weeks;
	}

	public static Date resetTime(Date d) {
		Calendar cal = new GregorianCalendar();
		cal.setTime(d);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}

	/**
	 * 两个日期差的天数
	 *
	 * @param oldDate
	 * @param newDate
	 * @return
	 */
	public static int getDaysBetweenTwoDate(Date oldDate, Date newDate) {
		Calendar calendarA = Calendar.getInstance();
		calendarA.setTime(oldDate);

		Calendar calendarB = Calendar.getInstance();
		calendarB.setTime(newDate);
		if (calendarA.get(Calendar.YEAR) > calendarB.get(Calendar.YEAR)) {
			return 0;
		} else if (calendarA.get(Calendar.YEAR) == calendarB.get(Calendar.YEAR)) {
			if (calendarA.get(Calendar.DAY_OF_YEAR) >= calendarB.get(Calendar.DAY_OF_YEAR)) {
				return calendarB.get(Calendar.DAY_OF_YEAR) - calendarA.get(Calendar.DAY_OF_YEAR);
			} else {
				return calendarB.get(Calendar.DAY_OF_YEAR) - calendarA.get(Calendar.DAY_OF_YEAR);
			}
		} else {
			int years = calendarB.get(Calendar.YEAR) - calendarA.get(Calendar.YEAR);// 1
			int startDays = calendarA.getActualMaximum(Calendar.DAY_OF_YEAR) - calendarA.get(Calendar.DAY_OF_YEAR);
			int endDays = calendarB.get(Calendar.DAY_OF_YEAR);
			int betweenDays = 0;
			for (int i = 0; i < years - 1; i++) {
				Calendar tempCalendar = Calendar.getInstance();
				tempCalendar.set(Calendar.YEAR, calendarA.get(Calendar.YEAR) + 1 + i);
				betweenDays += tempCalendar.getActualMaximum(Calendar.DAY_OF_YEAR);
			}
			return startDays + betweenDays + endDays;
		}
	}

	public static String getTimePointHM(int seconds) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(TimeUnit.SECONDS.toMillis(seconds));

		return HOUR_MIN_SDF(TimeZone.getDefault()).format(calendar.getTime());
	}

	/**
	 * dateA
	 *
	 * @param dateA
	 * @param dateB
	 * @return
	 */
	public static boolean isBefore(Date dateA, Date dateB) {
		Calendar calA = Calendar.getInstance();
		calA.setTime(dateA);
		Calendar calB = Calendar.getInstance();
		calB.setTime(dateB);
		if (calA.get(Calendar.YEAR) == calB.get(Calendar.YEAR)) {
			if (calA.get(Calendar.MONTH) < calB.get(Calendar.MONTH)) {
				return true;
			} else if (calA.get(Calendar.MONTH) == calB.get(Calendar.MONTH)) {
				if (calA.get(Calendar.DATE) < calB.get(Calendar.DATE)) {
					return true;
				}
			}

		} else if (calA.get(Calendar.YEAR) < calB.get(Calendar.YEAR)) {
			return true;
		}
		return false;
	}

	/**
	 * 根据某一天，求出和它同一周的所有Date
	 *
	 * @return
	 */
	public static Date[] getDateInSameWeek(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		Date[] dates = new Date[7];
		calendar.setFirstDayOfWeek(WHICH_DAY_IS_FIRST_DAY);

		int size = 0;
		;
		for (int i = WHICH_DAY_IS_FIRST_DAY; size < 7; i++) {
			size++;
			calendar.set(Calendar.DAY_OF_WEEK, i);
			dates[size] = calendar.getTime();
		}
		return dates;
	}

	/**
	 * basicDate之后
	 *
	 * @param date
	 * @return
	 */
	public static boolean isAfter(Date basicDate, Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(basicDate);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.add(Calendar.DAY_OF_YEAR, 1);
		if (date.getTime() >= calendar.getTimeInMillis()) {
			return true;
		}
		return false;

	}

	public static boolean isAfter(int basicTime, int time) {
		return isAfter(new Date(getMillsFromSeconds(basicTime)), new Date(getMillsFromSeconds(time)));
	}

	/**
	 * a,b同一天或者 a在b前1天
	 *
	 * @return
	 */
	public static boolean sameDateOrBefore(Date dateA, Date dateB) {
		Calendar calA = Calendar.getInstance();
		calA.setTime(dateA);
		Calendar calB = Calendar.getInstance();
		calB.setTime(dateB);
		if (calA.get(Calendar.YEAR) == calB.get(Calendar.YEAR)) {
			if (calA.get(Calendar.MONTH) < calB.get(Calendar.MONTH)) {
				return true;
			} else if (calA.get(Calendar.MONTH) == calB.get(Calendar.MONTH)) {
				if (calA.get(Calendar.DATE) <= calB.get(Calendar.DATE)) {
					return true;
				}
			}

		} else if (calA.get(Calendar.YEAR) < calB.get(Calendar.YEAR)) {
			return true;
		}
		return false;
	}

	/**
	 * 03'20"
	 *
	 * @param time
	 * @return
	 */
	public static String secondToTimeForPace(int time) {
		int minutesOffset = time % 3600;
		int minutes = minutesOffset / 60;
		int secondOffset = minutesOffset % 60;
		int second = secondOffset;
		return (minutes < 10 ? ("0" + minutes) : minutes) + "'" + (second < 10 ? ("0" + second) : second) + "\"";
	}

	/**
	 * 00h03'20"
	 *
	 * @param time
	 * @return
	 */
	public static String secondToTimeHour(int time) {
		int hour = time / 3600;
		int minutesOffset = time % 3600;
		int minutes = minutesOffset / 60;
		int secondOffset = minutesOffset % 60;
		int second = secondOffset;
		return (hour < 10 ? ("0" + hour) : hour) + "h" + (minutes < 10 ? ("0" + minutes) : minutes) + "'" + (second < 10 ?
				("0" + second) :
				second) + "\"";
	}

	/**
	 * 00:03:20
	 *
	 * @param time
	 * @return
	 */
	public static String secondToTimeHourOldFormat(int time) {
		int hour = time / 3600;
		int minutesOffset = time % 3600;
		int minutes = minutesOffset / 60;
		int secondOffset = minutesOffset % 60;
		int second = secondOffset;
		return (hour < 10 ? ("0" + hour) : hour) + ":" + (minutes < 10 ? ("0" + minutes) : minutes) + ":" + (second < 10 ?
				("0" + second) :
				second);
	}

	/**
	 * 03'20"
	 *
	 * @param time
	 * @return
	 */
	public static String secondToTimeMinutes(int time) {
		int hour = time / 3600;
		int minutesOffset = time % 3600;
		int minutes = minutesOffset / 60;
		int secondOffset = minutesOffset % 60;
		int second = secondOffset;
		return (minutes < 10 ? ("0" + minutes) : minutes) + "'" + (second < 10 ? ("0" + second) : second) + "\"";
	}

	/**
	 * 03:20
	 *
	 * @param time
	 * @return
	 */
	public static String secondToTimeMinutesOldFormat(int time) {
		int hour = time / 3600;
		int minutesOffset = time % 3600;
		int minutes = minutesOffset / 60;
		int secondOffset = minutesOffset % 60;
		int second = secondOffset;
		return (minutes < 10 ? ("0" + minutes) : minutes) + ":" + (second < 10 ? ("0" + second) : second);
	}

	public static String secondsToHourMinutesChina(int time) {
		int hour = time / 3600;
		int minutesOffset = time % 3600;
		int minutes = minutesOffset / 60;
		int secondOffset = minutesOffset % 60;
		int second = secondOffset;
		return (hour < 10 ? ("0" + hour) : hour) + "时" + (minutes < 10 ? ("0" + minutes) : minutes) + "分";
	}

	/**
	 * @param dateA
	 * @param dateB
	 * @return 0，同一周， 1，b是a的上周 -1 其他
	 */
	public static int verifyTwoWeek(Date dateA, Date dateB) {

		Calendar calDateA = Calendar.getInstance();
		Calendar calDateB = Calendar.getInstance();
		calDateA.setFirstDayOfWeek(WHICH_DAY_IS_FIRST_DAY);
		calDateA.setTime(dateA);
		calDateB.setFirstDayOfWeek(WHICH_DAY_IS_FIRST_DAY);
		calDateB.setTime(dateB);
		if ((Math.abs(calDateA.getTimeInMillis() - calDateB.getTimeInMillis()) < 7 * 24 * 3600 * 1000) && (
				calDateA.get(Calendar.WEEK_OF_YEAR) == calDateB.get(Calendar.WEEK_OF_YEAR))) {
			return 0;
		}
		// b往前一周后
		calDateA.add(Calendar.WEEK_OF_YEAR, -1);
		// 是同一周
		if ((Math.abs(calDateA.getTimeInMillis() - calDateB.getTimeInMillis()) < 7 * 24 * 3600 * 1000) && (
				calDateA.get(Calendar.WEEK_OF_YEAR) == calDateB.get(Calendar.WEEK_OF_YEAR))) {
			return 1;
		}
		return -1;

	}

	/**
	 * @param dateA
	 * @param dateB
	 * @return
	 */
	public static boolean twoDateIsSameWeek(Date dateA, Date dateB) {
		Calendar calDateA = Calendar.getInstance();
		calDateA.setFirstDayOfWeek(WHICH_DAY_IS_FIRST_DAY);
		calDateA.setTime(dateA);

		Calendar calDateB = Calendar.getInstance();
		calDateB.setFirstDayOfWeek(WHICH_DAY_IS_FIRST_DAY);
		calDateB.setTime(dateB);
		return (Math.abs(calDateA.getTimeInMillis() - calDateB.getTimeInMillis()) < 7 * 24 * 3600 * 1000) && (
				calDateA.get(Calendar.WEEK_OF_YEAR) == calDateB.get(Calendar.WEEK_OF_YEAR));
	}

	public static int getSecondByGMT(Date date) {
		return (int) ((date.getTime() + TimeZone.getDefault().getRawOffset()) / 1000);
	}

	public static Date getDateFromGMTSecond(int time) {
		return new Date(time * 1000l - TimeZone.getDefault().getRawOffset());
	}

	/**
	 * 把整形日期转成 11/29的格式
	 *
	 * @param ts
	 * @return
	 */
	public static String getSeparatorDate(int ts) {
		Date date = new Date(TimeUtil.getMillsFromSeconds(ts));
		return TimeUtil.MONTH_SEPARATOR_DAY(TimeZone.getDefault()).format(date);
	}

	/**
	 * @param minutes
	 * @param startHour
	 * @param endHour
	 * @param startMinutes
	 * @param endMinutes
	 * @return
	 */
	public static List<String> getHourMinutesArray(int minutes, int startHour, int endHour, int startMinutes, int endMinutes) {
		if (startHour < endHour) {
			Calendar startCalendar = Calendar.getInstance();
			startCalendar.setTimeInMillis(TimeUtil.getCurrentDayZeroTs());
			startCalendar.set(Calendar.HOUR_OF_DAY, startHour);
			startCalendar.set(Calendar.MINUTE, startMinutes);

			Calendar endCalendar = Calendar.getInstance();
			endCalendar.setTimeInMillis(TimeUtil.getCurrentDayZeroTs());
			endCalendar.set(Calendar.HOUR_OF_DAY, endHour);
			endCalendar.set(Calendar.MINUTE, endMinutes);
			List<String> timeString = new ArrayList<String>();
			while (startCalendar.getTimeInMillis() <= endCalendar.getTimeInMillis()) {
				timeString.add(HOUR_MIN_SDF(TimeZone.getDefault()).format(startCalendar.getTime()));
				startCalendar.add(Calendar.MINUTE, minutes);
			}
			return timeString;
		} else {
			Calendar startCalendar = Calendar.getInstance();
			startCalendar.setTimeInMillis(TimeUtil.getCurrentDayZeroTs());
			startCalendar.set(Calendar.HOUR_OF_DAY, startHour);
			startCalendar.set(Calendar.MINUTE, startMinutes);

			Calendar endCalendar = Calendar.getInstance();
			endCalendar.setTimeInMillis(TimeUtil.getCurrentDayZeroTs());
			endCalendar.add(Calendar.DAY_OF_MONTH, 1);

			endCalendar.set(Calendar.HOUR_OF_DAY, endHour);
			endCalendar.set(Calendar.MINUTE, endMinutes);
			List<String> timeString = new ArrayList<String>();
			while (startCalendar.getTimeInMillis() <= endCalendar.getTimeInMillis()) {
				timeString.add(HOUR_MIN_SDF(TimeZone.getDefault()).format(startCalendar.getTime()));
				startCalendar.add(Calendar.MINUTE, minutes);
			}
			return timeString;
		}
	}

	/**
	 * "1:20转成{1，20}的数组"
	 *
	 * @param time
	 * @return
	 * @throws ParseException
	 */
	public static int[] getHMFromTime(String time) throws ParseException {
		int[] timeArray = new int[2];
		Date date = HOUR_MIN_SDF(TimeZone.getDefault()).parse(time);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		timeArray[0] = calendar.get(Calendar.HOUR_OF_DAY);
		timeArray[1] = calendar.get(Calendar.MINUTE);
		return timeArray;
	}

	public static int getSecondsFromHMTime(String time) {
		try {
			return TimeUtil.getSecondsFromMills(HOUR_MIN_SDF(TimeZone.getDefault()).parse(time).getTime());
		} catch (ParseException e) {
			return 0;
		}
	}

	public static int[] getHMFromCurrent() {
		int[] timeArray = new int[2];
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		timeArray[0] = calendar.get(Calendar.HOUR_OF_DAY);
		timeArray[1] = calendar.get(Calendar.MINUTE);
		return timeArray;
	}

	/**
	 * 时间戳是今天
	 *
	 * @return
	 */
	public static boolean timeTsIsInToday(int ts) {
		int todayTs = getSecondsFromMills(getCurrentDayZeroTs());
		if (ts >= todayTs && ts < todayTs + TimeUnit.DAYS.toSeconds(1)) {
			return true;
		}
		return false;
	}

	/**
	 * @param list
	 * @param object
	 * @return
	 */
	public static int getIndexFromStringList(List<String> list, String object) {
		if (null == list || null == object || !list.contains(object)) {
			return 0;
		}
		for (int i = 0; i < list.size(); i++) {
			if (object.equals(list.get(i))) {
				return i;
			}
		}
		return 0;
	}

	public static int double2Scale6Int(Double cor) {
		if (Double.isInfinite(cor) || Double.isNaN(cor)) {
			return 0;
		}
		DecimalFormat df = new DecimalFormat("#.000000");
		return (int) (Double.parseDouble(df.format(cor)) * 1000000);
	}

	public static double int2Scale6Double(int cor) {
		return ((double) cor) / 1000000;
	}

	/**
	 * 从注册以来的所有天ts的list
	 *
	 * @return
	 */
	public static List<Integer> getDaysFromRegisterTs() {
		int registerZeroTs = getZeroTsOfDay(registerTs);
		int todayTs = TimeUtil.getSecondsFromMills(TimeUtil.getCurrentDayZeroTs());
		int pregnantZeroTs = SubTimeUtil.getTsBaseWeeksDays(0, 1);
		int ts = 0;
		if (registerZeroTs > pregnantZeroTs) {
			ts = registerZeroTs;
		} else {
			ts = pregnantZeroTs;
		}
		if (ts > 0) {
			List<Integer> dayList = new ArrayList<Integer>();
			while (ts <= todayTs) {
				dayList.add(ts);
				ts += TimeUnit.DAYS.toSeconds(1);
			}
			return dayList;
		}
		return new ArrayList<>();
	}

	/**
	 * 注册以来所有孕周ts的list
	 *
	 * @return
	 */
	public static List<String> getDaysStringFromRegisterTs() {
		List<Integer> list = getDaysFromRegisterTs();
		if (CollectionUtil.isNotEmpty(list)) {
			List<String> daysStringList = new ArrayList<String>(list.size());
			for (int i = 0; i < list.size(); i++) {
				daysStringList
						.add(M_SEPARATOR_DD(TimeZone.getDefault()).format(new Date(TimeUtil.getMillsFromSeconds(list.get(i)))));
			}
			return daysStringList;
		}
		return null;
	}

	public static String getDayString(int dayTs) {
		return M_SEPARATOR_DD(TimeZone.getDefault()).format(new Date(TimeUtil.getMillsFromSeconds(dayTs)));
	}

	public static List<Integer> getWeeksFromRegisterTs() {
		// 获取注册时的那天的孕周
		int registerZeroTs = getZeroTsOfDay(registerTs);
		int pregnantZeroTs = SubTimeUtil.getTsBaseWeeksDays(0, 1);
		int ts = 0;
		if (registerZeroTs > pregnantZeroTs) {
			ts = registerZeroTs;
		} else {
			ts = pregnantZeroTs;
		}
		if (ts > 0) {
			List<Integer> dayList = new ArrayList<Integer>();
			int startWeek = getPregWeeksSpecificDays(ts)[0];
			int currentWeek = getPregWeeksDays()[0];
			while (startWeek <= currentWeek) {
				dayList.add(getTsBaseWeeksDays(startWeek, 1));
				startWeek++;
			}
			return dayList;
		}
		return new ArrayList<>();
	}

	public static List<String> getWeeksStringFromRegisterTs(String format) {
		List<Integer> list = getWeeksFromRegisterTs();
		if (CollectionUtil.isNotEmpty(list)) {
			List<String> daysStringList = new ArrayList<String>(list.size());
			for (int i = 0; i < list.size(); i++) {
				daysStringList.add(String.format(format, getPregWeeksSpecificDays(list.get(i))[0]));
			}
			return daysStringList;
		}
		return null;
	}

	public static boolean tsIsSpecificHour(int ts, int hour) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(TimeUtil.getMillsFromSeconds(ts));
		if (calendar.get(Calendar.HOUR_OF_DAY) == hour) {
			return true;
		}
		return false;
	}

	/**
	 * 孕天是该孕周的第i天
	 *
	 * @param value
	 * @param i
	 * @return
	 */
	public static boolean tsIsSpecificDay(int value, int i) {
		if (getPregWeeksSpecificDays(value)[1] == i) {
			return true;
		}
		return false;
	}

	public static String second2HM(int time) {
		int hour = time / 3600;
		int minutesOffset = time % 3600;
		int minutes = minutesOffset / 60;
		int secondOffset = minutesOffset % 60;
		int second = secondOffset;
		if (second > 0) {
			minutes++;
		}
		return hour + "时" + minutes + "分";
	}

	/**
	 * 孕周是25周后
	 */
	public static boolean pregWeekIsAfter25Week() {
		int[] weekDays = SubTimeUtil.getPregWeeksDays();
		if (CollectionUtil.isNotEmpty(weekDays)) {
			int week = weekDays[0];
			int day = weekDays[1];
			if (day == 7) {
				week = week + 1;
			}
			if (week >= 25) {
				return true;
			}
		}
		return false;
	}

	public static int getAge(int birthdayTs) {
		Calendar birthday = Calendar.getInstance();
		birthday.setTimeInMillis(TimeUtil.getMillsFromSeconds(birthdayTs));

		Calendar today = Calendar.getInstance();
		int age = today.get(Calendar.YEAR) - birthday.get(Calendar.YEAR);
		if (today.get(Calendar.MONTH) < birthday.get(Calendar.MONTH)) {
			age--;
		} else if (today.get(Calendar.MONTH) == birthday.get(Calendar.MONTH)) {
			if (today.get(Calendar.DAY_OF_MONTH) < birthday.get(Calendar.DAY_OF_MONTH)) {
				age--;
			}
		}
		return age;

	}

	public static int getMinutes(int seconds) {
		int minutes = 0;
		if (seconds % 60 == 0) {
			minutes = seconds / 60;
		} else {
			minutes = seconds / 60 + 1;
		}
		return minutes;
	}

	public static String getTestTime(String format, int start, int end) {
		Calendar calendarStart = Calendar.getInstance();
		calendarStart.setTimeInMillis(TimeUtil.getMillsFromSeconds(start));
		calendarStart.set(Calendar.SECOND, 0);

		Calendar calendarEnd = Calendar.getInstance();
		calendarEnd.setTimeInMillis(TimeUtil.getMillsFromSeconds(end));
		calendarEnd.set(Calendar.SECOND, 0);

		long startTs = calendarStart.getTimeInMillis();
		long endTs = calendarEnd.getTimeInMillis();
		long offset = endTs - startTs;
		if (0 == offset) {
			return "不足一分钟";
		}
		return String.format(format, (int) offset / TimeUnit.MINUTES.toMillis(1));
	}

	public static String getTestTimeSection(int startTs, int endTs) {
		String startDate = SubTimeUtil.Y_M_R(TimeZone.getDefault()).format(new Date(TimeUtil.getMillsFromSeconds(startTs)));
		String startHM = SubTimeUtil.HOUR_MIN_SDF(TimeZone.getDefault()).format(new Date(TimeUtil.getMillsFromSeconds(startTs)));

		String endDate = SubTimeUtil.Y_M_R(TimeZone.getDefault()).format(new Date(TimeUtil.getMillsFromSeconds(endTs)));
		String endHM = SubTimeUtil.HOUR_MIN_SDF(TimeZone.getDefault()).format(new Date(TimeUtil.getMillsFromSeconds(endTs)));
		StringBuilder sb = new StringBuilder();
		sb.append(startDate);
		sb.append(" ");
		sb.append(startHM);
		sb.append("-");

		if (!startDate.equals(endDate)) {
			sb.append(startDate);
			sb.append(" ");
		}
		sb.append(endHM);
		return sb.toString();
	}
}
