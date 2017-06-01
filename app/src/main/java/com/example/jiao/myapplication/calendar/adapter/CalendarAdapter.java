package com.example.jiao.myapplication.calendar.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.example.jiao.myapplication.R;
import com.example.jiao.myapplication.calendar.CalendarController;
import com.example.jiao.myapplication.calendar.CalendarDay;
import com.example.jiao.myapplication.calendar.CalendarMonth;
import com.extantfuture.util.CollectionUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by jiao on 16/7/18.
 */
public class CalendarAdapter extends RecyclerView.Adapter<CalendarViewHolder> {

	/**
	 * for ui show
	 */
	private List<CalendarMonth> list = new ArrayList<CalendarMonth>();

	public CalendarController getController() {
		return controller;
	}

	/**
	 * for data distinguish selected hasData
	 */
	private CalendarController controller = new CalendarController(2016, 7, 1, 2016, 8, 20);

	public CalendarAdapter(List<CalendarMonth> list, CalendarController controller) {
		this.list = list;
		this.controller = controller;
	}

	public int getDefaultPosition() {
		TreeMap<Integer, CalendarDay> map = controller.getHasDataCalendarDays();
		Map.Entry<Integer, CalendarDay> entry = map.entrySet().iterator().next();
		if (null != entry && null != entry.getValue()) {
			CalendarDay calendarDay = entry.getValue();
			int month = calendarDay.month;
			for (int i = list.size() - 1; i >= 0; i--) {
				CalendarMonth calendarMonth = list.get(i);
				if (null != calendarMonth && calendarMonth.getMonth() == month) {
					return i;
				}
			}
		}
		int size = CollectionUtil.size(list);
		return size == 0 ? 0 : (size - 1);
	}

	@Override
	public CalendarViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		CalendarViewHolder calendarViewHolder = new CalendarViewHolder(
				LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_month_view, parent, false));
		calendarViewHolder.monthView.setCalendarController(controller);
		return calendarViewHolder;
	}

	@Override
	public void onBindViewHolder(CalendarViewHolder holder, int position) {
		holder.monthView.setCalendarMonth(list.get(position));
	}

	@Override
	public int getItemCount() {
		return CollectionUtil.size(list);
	}

	public CalendarMonth getItem(int position) {
		if (position >= 0 && position < list.size()) {
			return list.get(position);
		}
		return null;
	}
}
