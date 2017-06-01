package com.example.jiao.myapplication;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by jiao on 16/4/20.
 */
public class ZoomTextView extends TextView {

	public ZoomTextView(Context context) {
		super(context);
	}

	public ZoomTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ZoomTextView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	@TargetApi(21)
	public ZoomTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
	}

	@Override
	public void setTextSize(int unit, float size) {
		float oldSize = getTextSize();
		super.setTextSize(unit, size);
		float newSize = getTextSize();
		if (oldSize != newSize) {
			float scale = newSize / oldSize;
			Drawable[] drawables = getCompoundDrawables();
			for (int i = 0; i < drawables.length; i++) {
				Drawable drawable = drawables[i];
				if (null != drawable) {
					drawable.setBounds(0, 0, (int) (drawable.getBounds().right * scale),
									   (int) (drawable.getBounds().bottom * scale));
				}
			}
			//setCompoundDrawables(drawables[0], drawables[1], drawables[2], drawables[3]);
			int oldPadding = getCompoundDrawablePadding();
			setCompoundDrawablePadding((int) (oldPadding * scale));
			setCompoundDrawablesRelative(drawables[0], drawables[1], drawables[2], drawables[3]);
		}
	}
}
