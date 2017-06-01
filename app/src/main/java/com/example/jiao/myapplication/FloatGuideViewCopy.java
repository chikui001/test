package com.example.jiao.myapplication;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jiao on 16/4/18.
 */
public class FloatGuideViewCopy extends View {

	private Context mContext;

	private List<Integer> ids = new ArrayList<Integer>();
	private List<Target> targetList = new ArrayList<Target>();
	private View parentView = null;

	private Paint paint = new Paint();

	public FloatGuideViewCopy(Context context) {
		super(context);
		initView(context, null);
	}

	public FloatGuideViewCopy(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context, attrs);
	}

	public FloatGuideViewCopy(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initView(context, attrs);
	}

	@TargetApi(21)
	public FloatGuideViewCopy(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		initView(context, attrs);
	}

	private Bitmap mBitmap = null;

	private int mOldHeight;
	private int mOldWidth;

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		final int width = getMeasuredWidth();
		final int height = getMeasuredHeight();
		if (width <= 0 || height <= 0)
			return;
		// clear canvas
		//canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
		// draw solid background
		canvas.drawColor(Color.parseColor("#59000000"));

		paint.setColor(Color.RED);
		paint.setFlags(Paint.ANTI_ALIAS_FLAG);

		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
		CircleShape circleShape = new CircleShape();
		circleShape.draw(canvas, paint, 100, 100, 50);
		canvas.drawBitmap(mBitmap, 0, 0, null);
	}

	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		parentView = getRootView();
		if (0 != ids.size()) {
			for (int i = 0; i < ids.size(); i++) {
				View view = parentView.findViewById(ids.get(i));
				targetList.add(new ViewTarget(view));
			}
		}
	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		super.onLayout(changed, left, top, right, bottom);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	private final void initView(Context context, AttributeSet attrs) {
		this.mContext = context;

	}
}
