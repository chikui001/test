package com.example.jiao.myapplication.utils;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;
import org.greenrobot.eventbus.EventBus;

/**
 * 进程activity栈的前后台切换，
 * 不准的情况:
 * 1，如果有activity的onTrimMemory的时候，onStop不一定执行
 * 2，Does this work for configuration changes? －－ no
 * Created by jiao on 16/8/4.
 */
public class MyLifecycleHandler implements Application.ActivityLifecycleCallbacks {

	// I use four separate variables here. You can, of course, just use two and
	// increment/decrement them instead of using four and incrementing them all.
	private int resumed;
	private int paused;
	private int started;
	private int stopped;

	public static final int ON_BACKGROUND = 1;
	public static final int ON_FOREGROUND = 2;

	@Override
	public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
	}

	@Override
	public void onActivityDestroyed(Activity activity) {
	}

	@Override
	public void onActivityResumed(Activity activity) {
		++resumed;
		Log.w("lifeCircle", "application is in active: " + (resumed > paused));
	}

	@Override
	public void onActivityPaused(Activity activity) {
		++paused;
		Log.w("lifeCircle", "application is in active: " + (resumed > paused));
	}

	@Override
	public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
	}

	@Override
	public void onActivityStarted(Activity activity) {
		++started;
		if (started > stopped) {
			Log.w("lifeCircle", "application is visible: " + (started > stopped));
			EventBus.getDefault().post(new AppSwitchMessageEvent(ON_FOREGROUND));
		}
	}

	@Override
	public void onActivityStopped(Activity activity) {
		++stopped;
		if (started <= stopped) {
			Log.w("lifeCircle", "application is visible: " + (started > stopped));
			EventBus.getDefault().post(new AppSwitchMessageEvent(ON_FOREGROUND));
		}
	}

	// If you want a static function you can use to check if your application is
	// foreground/background, you can use the following:
	/*
	// Replace the four variables above with these four
    private static int resumed;
    private static int paused;
    private static int started;
    private static int stopped;

    // And these two public static functions

    */
	public boolean isApplicationVisible() {
		return started > stopped;
	}

	public boolean isApplicationInForeground() {
		Log.d("APP", "resumed" + resumed + ";paused" + paused);
		return resumed > paused;
	}

	public static class AppSwitchMessageEvent {

		public AppSwitchMessageEvent(int message) {
			this.message = message;
		}

		public int getMessage() {
			return message;
		}

		public void setMessage(int message) {
			this.message = message;
		}

		private int message;

	}
}
