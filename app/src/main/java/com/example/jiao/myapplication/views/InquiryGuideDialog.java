package com.example.jiao.myapplication.views;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.jiao.myapplication.R;
import com.extantfuture.util.CollectionUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jiao, <wenliang.jiao@extantfuture.com>
 * @date 2016年12月06日
 */
public class InquiryGuideDialog extends BaseDialogFragment {

	private View rootView;
	private boolean cancelable = false;
	private boolean canceledOnTouchOutside = false;
	private RecyclerViewPager recyclerViewPager;

	private InquiryGuideAdapter inquiryGuideAdapter;

	@Override
	protected int getSelfTheme() {
		return 0;
	}

	@Override
	protected void reThemeWindow() {
		Dialog dialog = getDialog();
		if (null != dialog) {
			dialog.setCanceledOnTouchOutside(canceledOnTouchOutside);
		}
	}

	@Override
	protected void reSizeWindow() {

	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.dialog_inquiry_guide, null);
		recyclerViewPager = (RecyclerViewPager) rootView.findViewById(R.id.inquiry_guide_rvp);
		LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
		recyclerViewPager.setLayoutManager(linearLayoutManager);
		inquiryGuideAdapter = new InquiryGuideAdapter(null, new ClickListener() {

			@Override
			public void onClick(View view, InquiryGuideViewHolder bluetoothGuideViewHolder) {

			}
		});
		recyclerViewPager.setAdapter(inquiryGuideAdapter);
		setCancelable(cancelable);
		return rootView;
	}

	private static class InquiryGuideAdapter extends RecyclerView.Adapter<InquiryGuideViewHolder> {

		private List<InquiryGuideType> list = new ArrayList<>();

		private ClickListener clickListener;

		public InquiryGuideAdapter(List<InquiryGuideType> list, ClickListener clickListener) {
			this.clickListener = clickListener;
			if (CollectionUtil.isNotEmpty(list)) {
				this.list = list;
			}
		}

		@Override
		public InquiryGuideViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
			View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewpager_item_bluetooth, parent, false);
			InquiryGuideViewHolder inquiryGuideViewHolder = new InquiryGuideViewHolder(view, clickListener);
			return inquiryGuideViewHolder;
		}

		@Override
		public void onBindViewHolder(InquiryGuideViewHolder holder, int position) {
			InquiryGuideType type = list.get(position);
			if (null != type) {
				switch (type) {
					case COPY_INQUIRY_TAB:
						holder.backImageView.setImageResource(R.drawable.chongdian_card);
						holder.contentTextView.setText("萌妈您好, 搜索不到萌动？请先给萌动充满电量, 若电量充足, 请点击继续排查哦～");
						holder.forwardTextView.setVisibility(View.VISIBLE);
						holder.backwardTextView.setVisibility(View.VISIBLE);
						holder.forwardTextView.setText("去充电~");
						holder.backwardTextView.setText("继续排查");
						break;
					case COPY_PRE_INQUIRY:
						holder.backImageView.setImageResource(R.drawable.settings_card);
						holder.contentTextView.setText("第一步：点击桌面上设置按钮");
						holder.forwardTextView.setVisibility(View.GONE);
						holder.backwardTextView.setVisibility(View.VISIBLE);
						holder.backwardTextView.setText("下一步");
						break;
					case COPY_INQUIRY:
						holder.backImageView.setImageResource(R.drawable.bluetooth_card);
						holder.contentTextView.setText("第二步：点击蓝牙图标");
						holder.forwardTextView.setVisibility(View.GONE);
						holder.backwardTextView.setVisibility(View.VISIBLE);
						holder.backwardTextView.setText("下一步");
						break;
					case DISCLAIMER:
						holder.backImageView.setImageResource(R.drawable.shebei_card);
						holder.contentTextView
								.setText("第三步：查看萌动设备是否为已连接，若如上图所示显示为“已连接”字样，则表示萌动设备已被其他APP连接上。请先关闭除萌动之外的APP，然后将萌动放置充电器上重启，即可连接成功～");
						holder.forwardTextView.setVisibility(View.VISIBLE);
						holder.backwardTextView.setVisibility(View.VISIBLE);
						holder.forwardTextView.setText("上一步");
						holder.backwardTextView.setText("去设置");
						break;
				}
			}
		}

		@Override
		public int getItemCount() {
			return CollectionUtil.size(list);
		}
	}

	private static class InquiryGuideViewHolder extends RecyclerView.ViewHolder {

		private ImageView backImageView;

		private TextView contentTextView;

		private TextView forwardTextView;

		private TextView backwardTextView;

		public InquiryGuideViewHolder(View itemView, final ClickListener clickListener) {
			super(itemView);
			backImageView = (ImageView) itemView.findViewById(R.id.back_iv);
			contentTextView = (TextView) itemView.findViewById(R.id.content_tv);
			forwardTextView = (TextView) itemView.findViewById(R.id.forward_tv);
			backwardTextView = (TextView) itemView.findViewById(R.id.backward_tv);
			forwardTextView.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					if (null != clickListener) {
						clickListener.onClick(v, InquiryGuideViewHolder.this);
					}
				}
			});
			backwardTextView.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					if (null != clickListener) {
						clickListener.onClick(v, InquiryGuideViewHolder.this);
					}
				}
			});
		}
	}

	private interface ClickListener {

		void onClick(View view, InquiryGuideViewHolder bluetoothGuideViewHolder);
	}
}
