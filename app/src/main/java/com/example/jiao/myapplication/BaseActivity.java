package com.example.jiao.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.jiao.myapplication.utils.PermissionManager;
import com.extantfuture.util.CollectionUtil;

import java.util.List;

/**
 * Created by jiao on 16/3/10.
 */
public abstract class BaseActivity extends AppCompatActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Log.d(getClass().getSimpleName(), "lifeCircle-onCreate");

	}

	public static class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

		public MyAdapter(List<String> list) {
			this.list = list;
		}

		private List<String> list;

		public void setViewClick(ViewClick viewClick) {
			this.viewClick = viewClick;

		}

		private ViewClick viewClick;

		@Override
		public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
			MyViewHolder viewHolder = new MyViewHolder(
					LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item, parent, false), viewClick);
			return viewHolder;
		}

		@Override
		public void onBindViewHolder(MyViewHolder holder, int position) {
			String text = list.get(position);
			holder.textView.setText(text);
			Log.d("xxx", text);
		}

		@Override
		public int getItemCount() {
			return CollectionUtil.size(list);
		}
	}

	public static class MyViewHolder extends RecyclerView.ViewHolder {

		/**
		 * a
		 */
		private ViewClick viewClick;

		public TextView textView;

		public MyViewHolder(View itemView, ViewClick itemClick) {
			super(itemView);
			this.textView = (TextView) itemView.findViewById(R.id.text_view);
			this.viewClick = itemClick;
			textView.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					viewClick.onClick(v.getId(), MyViewHolder.this);
				}
			});
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		Log.d(getClass().getSimpleName(), "lifeCircle-onPause");
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.d(getClass().getSimpleName(), "lifeCircle-onResume");
	}

	@Override
	protected void onStop() {
		super.onStop();
		Log.d(getClass().getSimpleName(), "lifeCircle-onStop");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.d(getClass().getSimpleName(), "lifeCircle-onDestroy");
	}

	@Override
	protected void onStart() {
		super.onStart();
		Log.d(getClass().getSimpleName(), "lifeCircle-onStart");
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		Log.d(getClass().getSimpleName(), "lifeCircle-onRestart");
	}
}
