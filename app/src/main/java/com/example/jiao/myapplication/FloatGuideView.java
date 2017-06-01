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
public class FloatGuideView extends View {

	private Context mContext;

	public List<Integer> ids = new ArrayList<Integer>();
	private List<Target> targetList = new ArrayList<Target>();
	private View parentView = null;

	private Paint paint = new Paint();

	public void addId(int id) {
		if (!ids.contains(Integer.valueOf(id))) {
			ids.add(id);
		}
	}

	public FloatGuideView(Context context) {
		super(context);
		initView(context, null);
	}

	public FloatGuideView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context, attrs);
	}

	public FloatGuideView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initView(context, attrs);
	}

	@TargetApi(21)
	public FloatGuideView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		initView(context, attrs);
		
	}

	private Bitmap mBitmap = null;

	private int mOldHeight;
	private int mOldWidth;

	private Canvas mCanvas;

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		final int width = getMeasuredWidth();
		final int height = getMeasuredHeight();
		if (width <= 0 || height <= 0)
			return;
		if (mBitmap == null || mCanvas == null || mOldHeight != height || mOldWidth != width) {

			if (mBitmap != null)
				mBitmap.recycle();

			mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

			mCanvas = new Canvas(mBitmap);
		}
		// clear canvas
		mCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
		// draw solid background
		mCanvas.drawColor(Color.parseColor("#59000000"));

		paint.setColor(Color.RED);
		paint.setFlags(Paint.ANTI_ALIAS_FLAG);
		for (int i = 0; i < targetList.size(); i++) {
			CircleShape circleShape = new CircleShape(targetList.get(i).getBounds());
			paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
			Point point = targetList.get(i).getPoint();
			circleShape.draw(mCanvas, paint, point.x, point.y, 0);
		}
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
