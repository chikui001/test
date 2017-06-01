package com.example.jiao.myapplication.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ImageView;

/**
 * @author jiao, <wenliang.jiao@extantfuture.com>
 * @date 2016年10月27日
 */
public class BubbleImageView extends ImageView {

	private Context mContext;

	public BubbleImageView(Context context) {
		super(context);
		this.mContext = context;
	}

	public BubbleImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
	}

	public BubbleImageView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		this.mContext = context;
	}

	@TargetApi(21)
	public BubbleImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		this.mContext = context;
	}

	private int x = 4;
	private int y = 10;
	private int z = 3;

	Paint paint = new Paint();

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		Path path = new Path();
		paint.setColor(Color.parseColor("#ffffff"));
		RectF roundF = new RectF(getLeft(), getTop(), getRight() - dp2px(x), getBottom());
		path.addRoundRect(roundF, dp2px(z), dp2px(z), Path.Direction.CCW);
		path.moveTo(getRight() - dp2px(x), getTop() + dp2px(y));
		path.lineTo(getRight() - dp2px(x) + dp2px(x), getTop() + dp2px(y) + dp2px(x));
		path.lineTo(getRight() - dp2px(x), getTop() + dp2px(y) + dp2px(x) + dp2px(x));
		canvas.clipPath(path, Region.Op.REVERSE_DIFFERENCE);
		canvas.drawPath(path, paint);
	}

	public int dp2px(float value) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, mContext.getResources().getDisplayMetrics());
	}
}
