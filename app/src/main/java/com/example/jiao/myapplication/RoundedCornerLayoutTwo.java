package com.example.jiao.myapplication;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.FrameLayout;

/**
 * 圆角抠图方案1
 *
 * @author jiao, <wenliang.jiao@extantfuture.com>
 * @date 2016年10月27日
 */
public class RoundedCornerLayoutTwo extends FrameLayout {

	private Context mContext;

	public RoundedCornerLayoutTwo(Context context) {
		this(context, null);
	}

	public RoundedCornerLayoutTwo(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public RoundedCornerLayoutTwo(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context, attrs, defStyleAttr);
	}

	@TargetApi(21)
	public RoundedCornerLayoutTwo(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		init(context, attrs, defStyleAttr);

	}

	private void init(Context context, AttributeSet attrs, int defStyleAttr) {
		this.mContext = context;
		setWillNotDraw(false);
	}

	private int x = 4;
	private int y = 10;
	private int z = 3;
	private Paint paint;

	@Override
	public void draw(Canvas canvas) {
		paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setColor(Color.RED);
		Path path = new Path();
		RectF roundF = new RectF(0, 0, canvas.getWidth() - dp2px(x), getHeight());
		path.addRoundRect(roundF, dp2px(z), dp2px(z), Path.Direction.CCW);
		//		path.addCircle(getWidth() / 2, getHeight() / 2, 200, Path.Direction.CCW);
		canvas.clipPath(path);
		super.draw(canvas);
	}

	public int dp2px(float value) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, mContext.getResources().getDisplayMetrics());
	}

}
