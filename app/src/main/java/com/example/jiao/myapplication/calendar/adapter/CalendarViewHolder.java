package com.example.jiao.myapplication.calendar.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import com.example.jiao.myapplication.calendar.MonthView;

/**
 * Created by jiao on 16/7/18.
 */
public class CalendarViewHolder extends RecyclerView.ViewHolder {

	public MonthView monthView;

	public CalendarViewHolder(View itemView) {
		super(itemView);
		monthView = (MonthView) itemView;
	}
}
