package com.example.jiao.myapplication.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

/**
 * @author jiao, <wenliang.jiao@extantfuture.com>
 * @date 2016年11月25日
 */
public class InterceptionRelativeLayout extends RelativeLayout {

	public void setInterceptTouchEvent(boolean interceptTouchEvent) {
		this.interceptTouchEvent = interceptTouchEvent;
	}

	public boolean isInterceptTouchEvent() {
		return interceptTouchEvent;
	}

	private boolean interceptTouchEvent = false;

	public InterceptionRelativeLayout(Context context) {
		super(context);
	}

	public InterceptionRelativeLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public InterceptionRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	@TargetApi(21)
	public InterceptionRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		if (!interceptTouchEvent) {
			return super.onInterceptTouchEvent(ev);
		}
		return interceptTouchEvent;
	}
}
