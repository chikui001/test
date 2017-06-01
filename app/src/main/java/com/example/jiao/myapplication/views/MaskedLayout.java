package com.example.jiao.myapplication.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.NinePatchDrawable;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import com.example.jiao.myapplication.R;

public class MaskedLayout extends FrameLayout {

	private NinePatchDrawable mMask;

	public MaskedLayout(Context context) {
		this(context, null);
	}

	public MaskedLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public MaskedLayout(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		mMask = (NinePatchDrawable) getResources().getDrawable(R.drawable.green_dialog);
		mMask.getPaint().setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
		setWillNotDraw(false);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		mMask.setBounds(0, 0, w, h);
	}

	@Override
	public void draw(Canvas canvas) {
		canvas.saveLayer(null, null, Canvas.ALL_SAVE_FLAG);
		super.draw(canvas);
		mMask.draw(canvas);
		canvas.restore();
	}
}
