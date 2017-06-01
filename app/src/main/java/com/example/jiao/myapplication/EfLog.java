// Copyright (c) 2015. ExtantFuture Inc. All Rights Reserved.
package com.example.jiao.myapplication;

/**
 * 控制台日志
 *
 * @author Rambo, <rambo@extantfuture.com>
 * @date 2015年10月16日
 */
public class EfLog {

	private static final String LOG_TAG = "Modoo";

	private static boolean isDebug = true;// 是否开启控制台日志

	// 输出debug日志之前可用这个方法判定是否执行debug日志输出操作...
	public static boolean isDebug() {
		return isDebug;
	}

	public static void setDebug(boolean debug) {
		isDebug = debug;
	}

	// Log.v
	public static void v(String tag, String msg) {
		if (isDebug && null != msg) {
			android.util.Log.v(tag, msg);
		}
	}

	public static void v(String tag, String msg, Throwable tr) {
		if (isDebug && null != msg) {
			android.util.Log.v(tag, msg, tr);
		}
	}

	public static void v(String msg) {
		v(LOG_TAG, msg);
	}

	// Log.d
	public static void d(String tag, String msg) {
		if (isDebug && null != msg) {
			android.util.Log.i(tag, msg);
		}
	}

	public static void d(String tag, String msg, Throwable tr) {
		if (isDebug && null != msg) {
			android.util.Log.i(tag, msg, tr);
		}
	}

	public static void d(String msg) {
		d(LOG_TAG, msg);
	}

	// Log.i
	public static void i(String tag, String msg) {
		if (isDebug && null != msg) {
			android.util.Log.i(tag, msg);
		}
	}

	public static void i(String tag, String msg, Throwable tr) {
		if (isDebug && null != msg) {
			android.util.Log.i(tag, msg, tr);
		}
	}

	public static void i(String msg) {
		i(LOG_TAG, msg);
	}

	// Log.w
	public static void w(String tag, String msg) {
		if (isDebug && null != msg) {
			android.util.Log.w(tag, msg);
		}
	}

	public static void w(String tag, String msg, Throwable tr) {
		if (isDebug && null != msg) {
			android.util.Log.w(tag, msg, tr);
		}
	}

	public static void w(String msg) {
		w(LOG_TAG, msg);
	}

	// Log.e
	public static void e(String tag, String msg) {
		if (isDebug && null != msg) {
			android.util.Log.e(tag, msg);
		}
	}

	public static void e(String tag, String msg, Throwable tr) {
		if (isDebug && null != msg) {
			android.util.Log.e(tag, msg, tr);
		}
	}

	public static void e(String msg) {
		e(LOG_TAG, msg);
	}

	public static void e(String msg, Throwable e) {
		e(LOG_TAG, msg, e);
	}
}
