package com.example.jiao.myapplication.views;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.webkit.WebView;

/**
 * @author jiao, <wenliang.jiao@extantfuture.com>
 * @date 2017年04月20日
 */
public class MyWebView extends WebView {

	public MyWebView(Context context) {
		super(context);
	}

	public MyWebView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MyWebView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public MyWebView(Context context, AttributeSet attrs, int defStyleAttr, boolean privateBrowsing) {
		super(context, attrs, defStyleAttr, privateBrowsing);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (MotionEventCompat.findPointerIndex(event, 0) != -1) {
			requestDisallowInterceptTouchEvent(true);
		} else {
			requestDisallowInterceptTouchEvent(false);
		}
		return super.onTouchEvent(event);
	}

	@Override
	protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
		super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);
		if (clampedY) {
			requestDisallowInterceptTouchEvent(false);
		}
	}
}
