package com.example.jiao.myapplication;

import android.support.v7.widget.RecyclerView;

public interface ViewClick {

	void onClick(int id, RecyclerView.ViewHolder viewHolder);

	void onClick(int id, RecyclerView.ViewHolder parentHolder, RecyclerView.ViewHolder childHolder);
}
