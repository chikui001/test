package com.example.jiao.myapplication.views;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
 * @date 2016年11月29日
 */
public class BluetoothDialog extends BaseDialogFragment {

	private boolean cancelable = false;
	private boolean canceledOnTouchOutside = false;

	private RecyclerViewPager recyclerView;
	private BluetoothGuideAdapter bluetoothGuideAdapter;
	private View rootView;

	private List<BluetoothConnectGuideType> list = new ArrayList<>();

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
		rootView = inflater.inflate(R.layout.dialog_bluetooth_connect, container, false);
		recyclerView = (RecyclerViewPager) rootView.findViewById(R.id.bluetooth_guide_ry);
		LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
		recyclerView.setLayoutManager(linearLayoutManager);
		list.add(BluetoothConnectGuideType.LOW_BATTERY);
		list.add(BluetoothConnectGuideType.SETTING);
		list.add(BluetoothConnectGuideType.SETTING_BLUETOOTH);
		list.add(BluetoothConnectGuideType.BLUETOOTH_CONNECTED_LIST);
		list.add(BluetoothConnectGuideType.RESET);
		list.add(BluetoothConnectGuideType.REBOOT_BLUETOOTH);
		list.add(BluetoothConnectGuideType.WRONG_DEVICE);
		list.add(BluetoothConnectGuideType.LOCATION);
		list.add(BluetoothConnectGuideType.REBOOT_PHONE);
		list.add(BluetoothConnectGuideType.STOP_WIFI);
		bluetoothGuideAdapter = new BluetoothGuideAdapter(list, new ClickListener() {

			@Override
			public void onClick(View view, BluetoothGuideViewHolder bluetoothGuideViewHolder) {
				Log.d("xxx", "v:" + view);
				int position = bluetoothGuideViewHolder.getAdapterPosition();
				BluetoothConnectGuideType type = list.get(position);
				switch (view.getId()) {
					case R.id.forward_tv:
						switch (type) {
							case LOW_BATTERY:
								//点击去充电
								break;
							case SETTING:
								break;
							case SETTING_BLUETOOTH:
								break;
							case BLUETOOTH_CONNECTED_LIST:
								recyclerView.scrollToLast();
								break;
							case RESET:
								break;
							case REBOOT_BLUETOOTH:
								recyclerView.scrollToLast();
								break;
							case WRONG_DEVICE:
								recyclerView.scrollToLast();
								break;
							case LOCATION:
								recyclerView.scrollToLast();
								break;
							case REBOOT_PHONE:
								recyclerView.scrollToLast();
								break;
							case STOP_WIFI:
								recyclerView.scrollToLast();
								break;
						}
						break;
					case R.id.backward_tv:
						switch (type) {
							case LOW_BATTERY:
								//点击继续排查
								recyclerView.scrollToNext();
								break;
							case SETTING:
								recyclerView.scrollToNext();
								break;
							case SETTING_BLUETOOTH:
								recyclerView.scrollToNext();
								break;
							case BLUETOOTH_CONNECTED_LIST:
								//点击去设置
								Intent intent = new Intent(Settings.ACTION_BLUETOOTH_SETTINGS);
								startActivity(intent);
								break;
							case RESET:
								recyclerView.scrollToNext();
								break;
							case REBOOT_BLUETOOTH:
								recyclerView.scrollToNext();
								break;
							case WRONG_DEVICE:
								recyclerView.scrollToNext();
								break;
							case LOCATION:
								recyclerView.scrollToNext();
								break;
							case REBOOT_PHONE:
								recyclerView.scrollToNext();
								break;
							case STOP_WIFI:
								dismissAllowingStateLoss();
								break;
						}
						break;
				}
			}
		});
		recyclerView.setAdapter(bluetoothGuideAdapter);
		setCancelable(cancelable);
		return rootView;
	}

	private interface ClickListener {

		void onClick(View view, BluetoothGuideViewHolder bluetoothGuideViewHolder);
	}

	private static class BluetoothGuideAdapter extends RecyclerView.Adapter<BluetoothGuideViewHolder> {

		private List<BluetoothConnectGuideType> list = new ArrayList<>();

		private ClickListener clickListener;

		public BluetoothGuideAdapter(List<BluetoothConnectGuideType> list, ClickListener clickListener) {
			this.clickListener = clickListener;
			if (CollectionUtil.isNotEmpty(list)) {
				this.list = list;
			}
		}

		@Override
		public BluetoothGuideViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
			View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewpager_item_bluetooth, parent, false);
			BluetoothGuideViewHolder bluetoothGuideViewHolder = new BluetoothGuideViewHolder(view, clickListener);
			return bluetoothGuideViewHolder;
		}

		@Override
		public void onBindViewHolder(BluetoothGuideViewHolder holder, int position) {
			BluetoothConnectGuideType type = list.get(position);
			switch (type) {
				case LOW_BATTERY:
					holder.backImageView.setImageResource(R.drawable.chongdian_card);
					holder.contentTextView.setText("萌妈您好, 搜索不到萌动？请先给萌动充满电量, 若电量充足, 请点击继续排查哦～");
					holder.forwardTextView.setVisibility(View.VISIBLE);
					holder.backwardTextView.setVisibility(View.VISIBLE);
					holder.forwardTextView.setText("去充电~");
					holder.backwardTextView.setText("继续排查");
					break;
				case SETTING:
					holder.backImageView.setImageResource(R.drawable.settings_card);
					holder.contentTextView.setText("第一步：点击桌面上设置按钮");
					holder.forwardTextView.setVisibility(View.GONE);
					holder.backwardTextView.setVisibility(View.VISIBLE);
					holder.backwardTextView.setText("下一步");
					break;
				case SETTING_BLUETOOTH:
					holder.backImageView.setImageResource(R.drawable.bluetooth_card);
					holder.contentTextView.setText("第二步：点击蓝牙图标");
					holder.forwardTextView.setVisibility(View.GONE);
					holder.backwardTextView.setVisibility(View.VISIBLE);
					holder.backwardTextView.setText("下一步");
					break;
				case BLUETOOTH_CONNECTED_LIST:
					holder.backImageView.setImageResource(R.drawable.shebei_card);
					holder.contentTextView
							.setText("第三步：查看萌动设备是否为已连接，若如上图所示显示为“已连接”字样，则表示萌动设备已被其他APP连接上。请先关闭除萌动之外的APP，然后将萌动放置充电器上重启，即可连接成功～");
					holder.forwardTextView.setVisibility(View.VISIBLE);
					holder.backwardTextView.setVisibility(View.VISIBLE);
					holder.forwardTextView.setText("上一步");
					holder.backwardTextView.setText("去设置");
					break;
				case RESET:
					holder.backImageView.setImageResource(R.drawable.chongdian_card);
					holder.contentTextView.setText("蓝牙连接失败，将萌动放在充电器上，停留5s蓝光亮起重启一下试试呢～万一成功了呢");
					holder.forwardTextView.setVisibility(View.GONE);
					holder.backwardTextView.setVisibility(View.VISIBLE);
					holder.backwardTextView.setText("下一步");
					break;
				case REBOOT_BLUETOOTH:
					holder.backImageView.setImageResource(R.drawable.restart_bluetooth_card);
					holder.contentTextView.setText("蓝牙连接失败，萌妈请上拉系统菜单试试重启蓝牙吧～");
					holder.forwardTextView.setVisibility(View.VISIBLE);
					holder.backwardTextView.setVisibility(View.VISIBLE);
					holder.forwardTextView.setText("上一步");
					holder.backwardTextView.setText("下一步");
					break;
				case WRONG_DEVICE:
					holder.backImageView.setImageResource(R.drawable.jiechu_card);
					holder.contentTextView.setText(
							"请确认您连接的设备与当初绑定的为同一设备；如绑定设备不在身边，需连接当前新设备，请前往【我的】－【我的设备】先进行【解除绑定】操作，再重新绑定当前设备；如您绑定和连接的为同一个设备，请跳过此步骤～");
					holder.forwardTextView.setVisibility(View.VISIBLE);
					holder.backwardTextView.setVisibility(View.VISIBLE);
					holder.forwardTextView.setText("上一步");
					holder.backwardTextView.setText("下一步");
					break;
				case LOCATION:
					holder.backImageView.setImageResource(R.drawable.dingwei_card);
					holder.contentTextView.setText("没有开启定位也会导致蓝牙连接失败呢～快去去看看定位有没有打开吧");
					holder.forwardTextView.setVisibility(View.VISIBLE);
					holder.backwardTextView.setVisibility(View.VISIBLE);
					holder.forwardTextView.setText("上一步");
					holder.backwardTextView.setText("下一步");
					break;
				case REBOOT_PHONE:
					holder.backImageView.setImageResource(R.drawable.dingwei_card);
					holder.contentTextView.setText("如果进行以上操作后，蓝牙连接依旧失败，萌妈请重启手机试试吧～");
					holder.forwardTextView.setVisibility(View.VISIBLE);
					holder.backwardTextView.setVisibility(View.VISIBLE);
					holder.forwardTextView.setText("上一步");
					holder.backwardTextView.setText("下一步");
					break;
				case STOP_WIFI:
					holder.backImageView.setImageResource(R.drawable.restart_phone_card);
					holder.contentTextView.setText("wifi有时候也会干扰萌动工作哦～萌妈请关闭wifi后再次尝试～");
					holder.forwardTextView.setVisibility(View.VISIBLE);
					holder.backwardTextView.setVisibility(View.VISIBLE);
					holder.forwardTextView.setText("上一步");
					holder.backwardTextView.setText("知道了");
					break;
			}
		}

		@Override
		public int getItemCount() {
			return CollectionUtil.size(list);
		}
	}

	private static class BluetoothGuideViewHolder extends RecyclerView.ViewHolder {

		private ImageView backImageView;

		private TextView contentTextView;

		private TextView forwardTextView;

		private TextView backwardTextView;

		public BluetoothGuideViewHolder(View itemView, final ClickListener clickListener) {
			super(itemView);
			backImageView = (ImageView) itemView.findViewById(R.id.back_iv);
			contentTextView = (TextView) itemView.findViewById(R.id.content_tv);
			forwardTextView = (TextView) itemView.findViewById(R.id.forward_tv);
			backwardTextView = (TextView) itemView.findViewById(R.id.backward_tv);
			forwardTextView.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					if (null != clickListener) {
						clickListener.onClick(v, BluetoothGuideViewHolder.this);
					}
				}
			});
			backwardTextView.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					if (null != clickListener) {
						clickListener.onClick(v, BluetoothGuideViewHolder.this);
					}
				}
			});
		}
	}
}
