package com.example.jiao.myapplication.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrUIHandler;
import in.srain.cube.views.ptr.indicator.PtrIndicator;

/**
 * Created by jiao on 16/5/31.
 */
public class MengdongView extends ImageView implements PtrUIHandler {

	private AnimationDrawable animationDrawable;

	public MengdongView(Context context) {
		super(context);
	}

	public MengdongView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MengdongView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	@TargetApi(21)
	public MengdongView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
	}

	private AnimationDrawable getAnimationDrawable() {
		if (null == animationDrawable) {
			Drawable drawable = getDrawable();
			if (drawable instanceof AnimationDrawable) {
				animationDrawable = (AnimationDrawable) drawable;
			}
		}
		return animationDrawable;
	}

	@Override
	public void onUIReset(PtrFrameLayout frame) {
		AnimationDrawable animationDrawable = getAnimationDrawable();
		if (null != animationDrawable) {
			animationDrawable.stop();
		}
	}

	@Override
	public void onUIRefreshPrepare(PtrFrameLayout frame) {

	}

	@Override
	public void onUIRefreshBegin(PtrFrameLayout frame) {
		AnimationDrawable animationDrawable = getAnimationDrawable();
		if (null != animationDrawable) {
			animationDrawable.start();
		}
	}

	@Override
	public void onUIRefreshComplete(PtrFrameLayout frame) {
		AnimationDrawable animationDrawable = getAnimationDrawable();
		if (null != animationDrawable) {
			animationDrawable.stop();
		}
	}

	@Override
	public void onUIPositionChange(PtrFrameLayout frame, boolean isUnderTouch, byte status, PtrIndicator ptrIndicator) {

	}
}
