package com.example.jiao.myapplication.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.FrameLayout;

/**
 * @author jiao, <wenliang.jiao@extantfuture.com>
 * @date 2016年10月27日
 */
public class ChatPictureFrameLayout extends FrameLayout {

	private Context mContext;

	public ChatPictureFrameLayout(Context context) {
		super(context);
		init(context, null, 0);
	}

	public ChatPictureFrameLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs, 0);
	}

	public ChatPictureFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context, attrs, defStyleAttr);
	}

	@TargetApi(23)
	public ChatPictureFrameLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);

	}

	@Override
	public void setWillNotDraw(boolean willNotDraw) {
		super.setWillNotDraw(willNotDraw);
	}

	private void init(Context context, AttributeSet attrs, int defStyleAttr) {
		this.mContext = context;
		setWillNotDraw(false);
	}

	Paint paint = new Paint();
	private int x = 4;
	private int y = 10;
	private int z = 3;

	@Override
	protected void onDraw(Canvas canvas) {
		Path path = new Path();
		RectF roundF = new RectF(0, 0, canvas.getWidth() - dp2px(x), getHeight());
		path.addRoundRect(roundF, dp2px(z), dp2px(z), Path.Direction.CCW);
		path.moveTo(canvas.getWidth() - dp2px(x), dp2px(y));
		path.lineTo(canvas.getWidth() - dp2px(x) + dp2px(x), dp2px(y) + dp2px(x));
		path.lineTo(canvas.getWidth() - dp2px(x), dp2px(y) + dp2px(x) + dp2px(x));

		canvas.clipPath(path);
		super.onDraw(canvas);
	}

	public int dp2px(float value) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, mContext.getResources().getDisplayMetrics());
	}

}
