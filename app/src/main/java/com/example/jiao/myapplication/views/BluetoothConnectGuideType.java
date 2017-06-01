package com.example.jiao.myapplication.views;

/**
 * @author jiao, <wenliang.jiao@extantfuture.com>
 * @date 2016年11月29日
 */
public enum BluetoothConnectGuideType {
	/**
	 * 设备没电了
	 */
	LOW_BATTERY(1),
	/**
	 * 点击设置
	 */
	SETTING(2),
	/**
	 * 点击设置中的蓝牙图标
	 */
	SETTING_BLUETOOTH(3),
	/**
	 * 设置中的蓝牙已经连接的设备list
	 */
	BLUETOOTH_CONNECTED_LIST(4),

	/**
	 * 连接失败，将萌动放在充电器上reset
	 */
	RESET(5),
	/**
	 * 重启蓝牙
	 */
	REBOOT_BLUETOOTH(6),
	/**
	 * 附近的设备不是'目标设备'，需要去解绑
	 */
	WRONG_DEVICE(7),
	/**
	 * android需要开启定位才能扫描到
	 */
	LOCATION(8),
	/**
	 * 重启手机
	 */
	REBOOT_PHONE(9),
	/**
	 * 关闭wifi
	 */
	STOP_WIFI(10),;

	private int type;

	BluetoothConnectGuideType(int type) {
		this.type = type;
	}
}
