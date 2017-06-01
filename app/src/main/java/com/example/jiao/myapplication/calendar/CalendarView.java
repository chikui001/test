package com.example.jiao.myapplication.calendar;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.example.jiao.myapplication.calendar.adapter.CalendarAdapter;
import com.example.jiao.myapplication.calendar.listener.CalendarScrollListener;

/**
 * Created by jiao on 16/7/18.
 */
public class CalendarView extends RecyclerView {

	public CalendarView(Context context) {
		super(context);
	}

	public CalendarView(Context context, @Nullable AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public CalendarView(Context context, @Nullable AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public void setAdapter(Adapter adapter) {
		super.setAdapter(adapter);
		int defaultPosition = ((CalendarAdapter) adapter).getDefaultPosition();
		scrollStop(defaultPosition);
		smoothScrollToPosition(defaultPosition);
	}

	@Override
	public boolean fling(int velocityX, int velocityY) {
		return super.fling(velocityX, velocityY);
	}

	@Override
	protected void onMeasure(int widthSpec, int heightSpec) {
		super.onMeasure(widthSpec, heightSpec);
		judgeX = getWidth() / 2;
	}

	private void init() {
		addOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
				super.onScrollStateChanged(recyclerView, newState);
				if (newState == RecyclerView.SCROLL_STATE_IDLE) {
					//滑动停止
					Log.d("jiao", "computerOffset");
					computerOffset();
				}
			}

			@Override
			public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
				super.onScrolled(recyclerView, dx, dy);
			}
		});
	}

	/**
	 * should be called before setAdapter
	 *
	 * @param calendarScrollListener
	 */
	public void setCalendarScrollListener(CalendarScrollListener calendarScrollListener) {
		if (null != getAdapter()) {
			throw new IllegalArgumentException("should be before after setAdapter");
		}
		this.calendarScrollListener = calendarScrollListener;
	}

	private CalendarScrollListener calendarScrollListener;
	private int judgeX;

	private void computerOffset() {
		LinearLayoutManager linearLayoutManager = ((LinearLayoutManager) getLayoutManager());
		View firstChild = getChildAt(0);
		float offset = getX() - firstChild.getX();
		if (offset > firstChild.getMeasuredWidth() / 2) {
			View secondChild = getChildAt(1);
			int index = linearLayoutManager.getPosition(secondChild);
			scrollStop(index);
			stopScroll();
			smoothScrollBy((int) (secondChild.getX() - getX()), 0);
		} else {
			int index = linearLayoutManager.getPosition(firstChild);
			scrollStop(index);
			stopScroll();
			smoothScrollBy(-(int) offset, 0);
		}
		//		LinearLayoutManager linearLayoutManager = ((LinearLayoutManager) getLayoutManager());
		//		for (int i = 0; i < linearLayoutManager.getChildCount(); i++) {
		//			View child = linearLayoutManager.getChildAt(i);
		//			int childStart = linearLayoutManager.getDecoratedLeft(child);
		//			int childEnd = linearLayoutManager.getDecoratedRight(child);
		//			if (childStart <= judgeX && childEnd >= judgeX) {
		//				int index = linearLayoutManager.getPosition(child);
		//				int left = judgeX - childStart;
		//				int right = childEnd - judgeX;
		//				int width = child.getWidth();
		//				Log.d("jiao", "left:" + left);
		//				Log.d("jiao", "right:" + right);
		//				if (left < width / 2) {
		//					stopScroll();
		//					smoothScrollBy(width / 2 - left, 0);
		//				} else {
		//					stopScroll();
		//					smoothScrollBy(width / 2 - left, 0);
		//				}
		//				scrollStop(index);
		//				break;
		//			}
		//		}
	}

	private void scrollStop(int position) {
		CalendarMonth calendarMonth = ((CalendarAdapter) getAdapter()).getItem(position);
		if (null != calendarMonth && null != calendarScrollListener) {
			calendarScrollListener.onScrollStop(calendarMonth);
		}
	}

	public void scrollToNext() {
		int currentPosition = -1;
		LinearLayoutManager linearLayoutManager = ((LinearLayoutManager) getLayoutManager());
		for (int i = 0; i < linearLayoutManager.getChildCount(); i++) {
			View child = linearLayoutManager.getChildAt(i);
			int childStart = linearLayoutManager.getDecoratedLeft(child);
			int childEnd = linearLayoutManager.getDecoratedRight(child);
			if (childStart <= judgeX && childEnd >= judgeX) {
				currentPosition = linearLayoutManager.getPosition(child);
				break;
			}
		}
		if (currentPosition < getAdapter().getItemCount() - 1) {
			scrollStop(currentPosition + 1);
			smoothScrollToPosition(currentPosition + 1);
		} else {
			Toast.makeText(getContext(), "没有下一个月了", Toast.LENGTH_SHORT).show();
		}
	}

	public void scrollToLast() {
		int currentPosition = -1;
		LinearLayoutManager linearLayoutManager = ((LinearLayoutManager) getLayoutManager());
		for (int i = 0; i < linearLayoutManager.getChildCount(); i++) {
			View child = linearLayoutManager.getChildAt(i);
			int childStart = linearLayoutManager.getDecoratedLeft(child);
			int childEnd = linearLayoutManager.getDecoratedRight(child);
			if (childStart <= judgeX && childEnd >= judgeX) {
				currentPosition = linearLayoutManager.getPosition(child);
				break;
			}
		}
		if (currentPosition > 0) {
			scrollStop(currentPosition - 1);
			stopScroll();
			smoothScrollToPosition(currentPosition - 1);
		} else {
			Toast.makeText(getContext(), "没有上一个月了", Toast.LENGTH_SHORT).show();
		}
	}

}
