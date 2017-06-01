package com.example.jiao.myapplication.views.jiao;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.*;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.example.jiao.myapplication.R;

/**
 * Created by jiao on 16/9/28.
 */

public class MaskView extends ViewGroup {

	private static final int DEFAULT_CHILD_GRAVITY = Gravity.CENTER;
	private Context mContext;
	private static final long ANIMATION_DURATION = 2000;
	private View parentView = null;
	private SparseArray<MaskViewType> targetIds = new SparseArray<>();
	private SparseArray<ViewTarget> targetViews = new SparseArray<>();

	public void addMask(int id, MaskViewType viewType) {
		targetIds.put(id, viewType);
	}

	public MaskView(Context context) {
		this(context, null);
	}

	public MaskView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public MaskView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context);
	}

	@TargetApi(23)
	public MaskView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		init(context);
	}

	private Paint paint = new Paint();

	@Override
	protected void dispatchDraw(Canvas canvas) {
		super.dispatchDraw(canvas);
		paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setStrokeJoin(Paint.Join.MITER);
		paint.setColor(Color.RED); // Change the boundary color
		paint.setStyle(Paint.Style.FILL);
		paint.setStrokeWidth(10);
		Path path = new Path();

		float length = 100;
		float x = canvas.getWidth() / 2;
		float y = canvas.getHeight() / 2;

		paint.setColor(Color.RED); // Change the boundary color
		path.moveTo(x, y);
		path.lineTo(x - length, y);
		path.arcTo(new RectF(x - length - (length / 2), y - length, x - (length / 2), y), 90, 180);
		path.arcTo(new RectF(x - length, y - length - (length / 2), x, y - (length / 2)), 180, 180);
		path.lineTo(x, y);

		canvas.drawPath(path, paint);

		paint.setColor(Color.parseColor("#70000000")); // Change the boundary color
		canvas.drawRect(new RectF(x - length - (length / 2), y - length - (length / 2), x, y), paint);
		Matrix mMatrix = new Matrix();
		RectF bounds = new RectF();
		path.computeBounds(bounds, true);
		mMatrix.postRotate(45, x - length / 2, y - length - (length / 2));
		path.transform(mMatrix);
		Matrix mMatrixTransform = new Matrix();
		mMatrixTransform.postTranslate((length * 3 ) / 2, 0);
		path.transform(mMatrixTransform);
		canvas.drawPath(path, paint);
	}

	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		parentView = getRootView();
		for (int i = 0; i < targetIds.size(); i++) {
			int viewId = targetIds.keyAt(i);
			// get the object by the key.
			View targetView = parentView.findViewById(viewId);
			if (null != targetView) {
				targetViews.put(viewId, new ViewTarget(targetView));
			}
		}
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		layoutChildren(l, t, r, b, false);
	}

	private void layoutChildren(int l, int t, int r, int b, boolean forceLeftGravity) {
		Log.d("jiao", "l:" + l + ";t;" + t + ";r;" + r + ";b:" + b);
		final int count = getChildCount();
		final int parentLeft = getLeft();
		final int parentRight = getRight();

		final int parentTop = getTop();
		final int parentBottom = getBottom();
		for (int i = 0; i < count; i++) {
			final View child = getChildAt(i);
			if (child.getVisibility() != GONE) {
				final LayoutParams lp = (LayoutParams) child.getLayoutParams();
				final int width = child.getMeasuredWidth();
				final int height = child.getMeasuredHeight();

				int childLeft = 0;
				int childTop = 0;

				int gravity = lp.gravity;
				int anchor = lp.anchor;
				if (-1 == gravity) {
					//如果没有设置anchor,使用DEFAULT_CHILD_GRAVITY布局
					gravity = DEFAULT_CHILD_GRAVITY;
				}
				if (-1 != anchor) {
					int viewId = lp.anchorId;
					ViewTarget viewTarget = targetViews.get(viewId, null);
					if (null == viewTarget) {
						View view = findViewById(viewId);
						if (null != view) {
							viewTarget = new ViewTarget(view);
						}
					}
					Log.d("jiao", viewTarget.getPoint() + "");
					if (null != viewTarget) {
						switch (lp.getHorizontal()) {
							case LayoutParams.RIGHT:
								childLeft = viewTarget.getShowBounds(dp2px(16)).right + lp.leftMargin - lp.rightMargin;
								switch (lp.getVertical()) {
									case LayoutParams.BOTTOM:
										childTop = viewTarget.getShowBounds(dp2px(16)).bottom + lp.topMargin;
										break;
									case LayoutParams.TOP:
										childTop = viewTarget.getShowBounds(dp2px(16)).top + height - lp.bottomMargin;
										break;
									default:
										childTop = viewTarget.getPoint().y - height / 2 + lp.topMargin - lp.bottomMargin;

								}
								break;
							case LayoutParams.LEFT:
								childLeft = viewTarget.getShowBounds(dp2px(16)).left + lp.leftMargin - lp.rightMargin;
								switch (lp.getVertical()) {
									case LayoutParams.BOTTOM:
										childTop = viewTarget.getShowBounds(dp2px(16)).bottom + lp.topMargin;
										break;
									case LayoutParams.TOP:
										childTop = viewTarget.getShowBounds(dp2px(16)).top + height - lp.bottomMargin;
										break;
									default:
										childTop = viewTarget.getPoint().y - height / 2 + lp.topMargin - lp.bottomMargin;

								}
								break;
						}
						Log.d("jiao",
							  "child-left:" + childLeft + ";top:" + childTop + ";right:" + (childLeft + width) + ";bottom:" + (
									  childTop + height));
						child.layout(childLeft, childTop, childLeft + width, childTop + height);
					}
				} else {
					//gravity!=-1,把MaskView当成FrameLayout,使用FrameLayout的gravity布局
					final int layoutDirection = getLayoutDirection();
					final int absoluteGravity = Gravity.getAbsoluteGravity(gravity, layoutDirection);
					final int verticalGravity = gravity & Gravity.VERTICAL_GRAVITY_MASK;

					switch (absoluteGravity & Gravity.HORIZONTAL_GRAVITY_MASK) {
						case Gravity.CENTER_HORIZONTAL:
							childLeft = parentLeft + (parentRight - parentLeft - width) / 2 +
									lp.leftMargin - lp.rightMargin;
							break;
						case Gravity.RIGHT:
							if (!forceLeftGravity) {
								childLeft = parentRight - width - lp.rightMargin;
								break;
							}
						case Gravity.LEFT:
						default:
							childLeft = parentLeft + lp.leftMargin;
					}

					switch (verticalGravity) {
						case Gravity.TOP:
							childTop = parentTop + lp.topMargin;
							break;
						case Gravity.CENTER_VERTICAL:
							childTop = parentTop + (parentBottom - parentTop - height) / 2 +
									lp.topMargin - lp.bottomMargin;
							break;
						case Gravity.BOTTOM:
							childTop = parentBottom - height - lp.bottomMargin;
							break;
						default:
							childTop = parentTop + lp.topMargin;
					}

					child.layout(childLeft, childTop, childLeft + width, childTop + height);
				}
			}
		}
	}

	@Override
	public LayoutParams generateLayoutParams(AttributeSet attrs) {
		return new LayoutParams(getContext(), attrs);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		final int w = MeasureSpec.getSize(widthMeasureSpec);
		final int h = MeasureSpec.getSize(heightMeasureSpec);
		setMeasuredDimension(w, h);
		final int count = getChildCount();
		View child;
		for (int i = 0; i < count; i++) {
			child = getChildAt(i);
			if (child != null) {
				LayoutParams lp = (LayoutParams) child.getLayoutParams();
				if (lp == null) {
					child.setLayoutParams(lp);
				}
				measureChild(child, w + MeasureSpec.AT_MOST, h + MeasureSpec.AT_MOST);
			}
		}
	}

	private void init(Context context) {
		this.mContext = context;
		paint.setColor(Color.parseColor("#7F000000"));
		paint.setAntiAlias(true);
	}

	public enum MaskViewType {

		DOUBLE_CIRCLE(1),
		OVAL(2),
		RECT(3),;

		MaskViewType(int cmd) {
			this.cmd = (byte) cmd;
		}

		private byte cmd;

		public byte getCmd() {
			return cmd;
		}

		public static MaskViewType getValue(byte cmd) {
			for (MaskViewType type : values()) {
				if (type.getCmd() == cmd) {
					return type;
				}
			}
			return null;
		}
	}

	public void show() {
		if (mContext instanceof Activity) {
			((ViewGroup) ((Activity) mContext).getWindow().getDecorView()).addView(this);
			fadeIn();
		}
	}

	private AnimationFactory mAnimationFactory;

	private void fadeIn() {
		if (null == mAnimationFactory) {
			mAnimationFactory = new AnimationFactory();
		}
		mAnimationFactory.fadeInView(this, ANIMATION_DURATION, new IAnimationFactory.AnimationStartListener() {

			@Override
			public void onAnimationStart() {
			}
		});
	}

	static class LayoutParams extends LinearLayout.LayoutParams {

		public int getHorizontal() {
			return anchor & HORIZONTAL;
		}

		public int getVertical() {
			return anchor & VERTICAL;
		}

		public static final int LEFT = 0x0001;
		public static final int RIGHT = 0x0002;

		public static final int HORIZONTAL = LEFT | RIGHT;

		public static final int TOP = 0x0004;
		public static final int BOTTOM = 0x0008;

		public static final int VERTICAL = TOP | BOTTOM;

		public int anchor = -1;

		public int anchorId = 0;

		public LayoutParams(Context c, AttributeSet attrs) {
			super(c, attrs);
			TypedArray a = c.obtainStyledAttributes(attrs, R.styleable.MaskView_Layout);
			anchor = a.getInt(R.styleable.MaskView_Layout_mask_layout_anchor, -1);
			anchorId = a.getResourceId(R.styleable.MaskView_Layout_mask_layout_anchorId, View.NO_ID);
			a.recycle();
		}
	}

	public int dp2px(float value) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, mContext.getResources().getDisplayMetrics());
	}
}
