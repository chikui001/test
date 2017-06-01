package com.example.jiao.myapplication.calendar;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

/**
 * 时间相关工具类
 *
 * @author rambodu
 * @date 2014-8-28 下午6:12:11
 */
public class TimeUtil {

	private static final SimpleDateFormat DAY_SDF = new SimpleDateFormat("yyyyMMdd");
	private static final SimpleDateFormat MONTH_DAY_SDF = new SimpleDateFormat("MM月dd日");
	private static final SimpleDateFormat HOUR_MIN_SDF = new SimpleDateFormat("HH:mm");
	private static final SimpleDateFormat DAY_MIN_SDF = new SimpleDateFormat("MM月dd日HH:mm");// 8月29日11:00
	private static final SimpleDateFormat DAYTIME_SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static final SimpleDateFormat MONTH_DAY_TIME = new SimpleDateFormat("MM-dd HH:mm:ss");
	private static final SimpleDateFormat GET_STRING_TODATE = new SimpleDateFormat("yyyy-MM-dd");
	private static final SimpleDateFormat MONTH_SEPARATOR_DAY = new SimpleDateFormat("MM/dd");

	private static final SimpleDateFormat H_SHI_M_FEN_SDF = new SimpleDateFormat("HH时mm分");
	private static final SimpleDateFormat Y_M_R = new SimpleDateFormat("yyyy/MM/dd");
	private static final SimpleDateFormat M_SEPARATOR_DD = new SimpleDateFormat("M/dd");

	private static final SimpleDateFormat Y_M_R_H_M = new SimpleDateFormat("yyyy/MM/dd HH:mm");

	private static final SimpleDateFormat Y_M_D = new SimpleDateFormat("yyyy年MM月dd日");

	private static final SimpleDateFormat Y_M_D_H_M_CN = new SimpleDateFormat("yyyy年MM月dd日HH:mm");

	public static SimpleDateFormat Y_M_D_H_M_CN(TimeZone timeZone) {
		if (null != timeZone && timeZone != Y_M_D_H_M_CN.getTimeZone()) {
			Y_M_D_H_M_CN.setTimeZone(TimeZone.getDefault());
		}
		return Y_M_D_H_M_CN;
	}

	public static SimpleDateFormat Y_M_D(TimeZone timeZone) {
		if (null != timeZone && timeZone != Y_M_D.getTimeZone()) {
			Y_M_D.setTimeZone(TimeZone.getDefault());
		}
		return Y_M_D;
	}

	public static SimpleDateFormat DAY_SDF(TimeZone timeZone) {
		if (null != timeZone && timeZone != DAY_SDF.getTimeZone()) {
			DAY_SDF.setTimeZone(TimeZone.getDefault());
		}
		return DAY_SDF;
	}

	public static SimpleDateFormat MONTH_DAY_SDF(TimeZone timeZone) {
		if (null != timeZone && timeZone != MONTH_DAY_SDF.getTimeZone()) {
			MONTH_DAY_SDF.setTimeZone(TimeZone.getDefault());
		}
		return MONTH_DAY_SDF;
	}

	public static SimpleDateFormat HOUR_MIN_SDF(TimeZone timeZone) {
		if (null != timeZone && timeZone != HOUR_MIN_SDF.getTimeZone()) {
			HOUR_MIN_SDF.setTimeZone(TimeZone.getDefault());
		}
		return HOUR_MIN_SDF;
	}

	public static SimpleDateFormat DAY_MIN_SDF(TimeZone timeZone) {
		if (null != timeZone && timeZone != DAY_MIN_SDF.getTimeZone()) {
			DAY_MIN_SDF.setTimeZone(TimeZone.getDefault());
		}
		return DAY_MIN_SDF;
	}

	public static SimpleDateFormat DAYTIME_SDF(TimeZone timeZone) {
		if (null != timeZone && timeZone != DAYTIME_SDF.getTimeZone()) {
			DAYTIME_SDF.setTimeZone(TimeZone.getDefault());
		}
		return DAYTIME_SDF;
	}

	public static SimpleDateFormat MONTH_DAY_TIME(TimeZone timeZone) {
		if (null != timeZone && timeZone != MONTH_DAY_TIME.getTimeZone()) {
			MONTH_DAY_TIME.setTimeZone(TimeZone.getDefault());
		}
		return MONTH_DAY_TIME;
	}

	public static SimpleDateFormat GET_STRING_TODATE(TimeZone timeZone) {
		if (null != timeZone && timeZone != GET_STRING_TODATE.getTimeZone()) {
			GET_STRING_TODATE.setTimeZone(TimeZone.getDefault());
		}
		return GET_STRING_TODATE;
	}

	public static SimpleDateFormat MONTH_SEPARATOR_DAY(TimeZone timeZone) {
		if (null != timeZone && timeZone != MONTH_SEPARATOR_DAY.getTimeZone()) {
			MONTH_SEPARATOR_DAY.setTimeZone(TimeZone.getDefault());
		}
		return MONTH_SEPARATOR_DAY;
	}

	public static SimpleDateFormat H_SHI_M_FEN_SDF(TimeZone timeZone) {
		if (null != timeZone && timeZone != H_SHI_M_FEN_SDF.getTimeZone()) {
			H_SHI_M_FEN_SDF.setTimeZone(TimeZone.getDefault());
		}
		return H_SHI_M_FEN_SDF;
	}

	public static SimpleDateFormat Y_M_R(TimeZone timeZone) {
		if (null != timeZone && timeZone != Y_M_R.getTimeZone()) {
			Y_M_R.setTimeZone(TimeZone.getDefault());
		}
		return Y_M_R;
	}

	public static SimpleDateFormat M_SEPARATOR_DD(TimeZone timeZone) {
		if (null != timeZone && timeZone != M_SEPARATOR_DD.getTimeZone()) {
			M_SEPARATOR_DD.setTimeZone(TimeZone.getDefault());
		}
		return M_SEPARATOR_DD;
	}

	public static SimpleDateFormat Y_M_R_H_M(TimeZone timeZone) {
		if (null != timeZone && timeZone != Y_M_R_H_M.getTimeZone()) {
			Y_M_R_H_M.setTimeZone(TimeZone.getDefault());
		}
		return Y_M_R_H_M;
	}

	/**
	 * 毫秒转秒
	 *
	 * @param ts
	 * @return
	 */
	public static int getSecondsFromMills(long ts) {
		return (int) TimeUnit.MILLISECONDS.toSeconds(ts);
	}

	/**
	 * 秒转毫秒
	 *
	 * @param ts
	 * @return
	 */
	public static long getMillsFromSeconds(int ts) {
		return TimeUnit.SECONDS.toMillis(ts);
	}

	/**
	 * 某个时间截对应的当天0点时间截
	 *
	 * @param ts 时间截
	 * @return
	 */
	public static long getZeroTsOfDay(long ts) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(ts);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTimeInMillis();
	}

	/**
	 * 获取当天0点时间截，秒
	 *
	 * @param ts 秒
	 * @return
	 */
	public static int getZeroTsOfDay(int ts) {
		return getSecondsFromMills(getZeroTsOfDay(getMillsFromSeconds(ts)));
	}

	public static final long ONE_WEEK_TS = TimeUnit.DAYS.toMillis(7);
	public static final long ONE_DAY_TS = TimeUnit.DAYS.toMillis(1);

	/**
	 * 当天0点时间截
	 *
	 * @return
	 */
	public static long getCurrentDayZeroTs() {
		return getZeroTsOfDay(System.currentTimeMillis());
	}

	public static final String genMMSSStr(int seconds) {
		int minute = seconds / 60;
		int sec = seconds % 60;
		StringBuilder s = new StringBuilder();
		if (minute > 0) {
			s.append(minute).append("分钟");
		}
		if (sec > 0) {
			s.append(sec).append("秒");
		}
		return s.toString();
	}

	/**
	 * 查星期几
	 *
	 * @param dayOfWeek 星期中的第几天，从Calendar.DAY_OF_WEEK中来
	 * @return
	 */
	public static String getWeekText(int dayOfWeek) {
		switch (dayOfWeek) {
			case 2:
				return "星期一";
			case 3:
				return "星期二";
			case 4:
				return "星期三";
			case 5:
				return "星期四";
			case 6:
				return "星期五";
			case 7:
				return "星期六";
			case 1:
				return "星期日";
			default:
				return "星期一";
		}
	}

	/**
	 * 查星期几
	 *
	 * @param timestamp 时间截，单位：毫秒
	 * @return
	 */
	public static String getWeekText(long timestamp) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(timestamp);
		return getWeekText(calendar.get(Calendar.DAY_OF_WEEK));
	}

	/* 时间戳转换成字符窜 */
	public static String getDateToString(long time) {
		if (0L != time) {
			Date d = new Date(time);
			return GET_STRING_TODATE.format(d);
		}
		return null;
	}

	/**
	 * 时间戳是今年
	 *
	 * @param ts
	 * @return
	 */
	public static boolean isCurrentYear(int ts) {
		Calendar calendar = Calendar.getInstance();
		int currentYear = calendar.get(Calendar.YEAR);
		calendar.setTimeInMillis(getMillsFromSeconds(ts));
		int year = calendar.get(Calendar.YEAR);
		return currentYear == year;
	}
}
