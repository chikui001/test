package com.example.jiao.myapplication;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import com.extantfuture.util.CollectionUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BluetoothGattWrapper {

	private static final String TAG = "BluetoothGattWrapper";
	public static final int CONN_STATE_IDLE = 1;//abandon
	public static final int CONN_STATE_CONNECTING = 2;
	public static final int CONN_STATE_CONNECTED = 3;
	public static final int CONN_STATE_DISCONNECTING = 4;//abandon
	public static final int CONN_STATE_CLOSED = 5;//abandon

	public static final int SERVICE_DISCOVERY_IDLE = 0;
	public static final int SERVICE_DISCOVERY_SUCCESS = 6;
	public static final int SERVICE_DISCOVERY_FAIL = 7;//abandon

	private int serviceDiscoveryState = SERVICE_DISCOVERY_IDLE;
	private int state = CONN_STATE_IDLE;

	private BluetoothGatt bluetoothGatt;

	public Map<String, BluetoothGattCharacteristic> getUuidCharacteristicMap() {
		return uuidCharacteristicMap;
	}

	private Map<String, BluetoothGattCharacteristic> uuidCharacteristicMap = new HashMap<>();

	public BluetoothGatt getBluetoothGatt() {
		return bluetoothGatt;
	}

	public BluetoothGattWrapper(BluetoothGatt bluetoothGatt) {
		this.bluetoothGatt = bluetoothGatt;
		this.state = CONN_STATE_CONNECTING;
	}

	public int getState() {
		return state;
	}

	/**
	 * 连接中，或者正在初始化服务
	 *
	 * @return
	 */
	public boolean isConnectingOrDiscovering() {
		if (state == CONN_STATE_CONNECTING || (state == CONN_STATE_CONNECTED && serviceDiscoveryState == SERVICE_DISCOVERY_IDLE)) {
			return true;
		}
		return false;
	}

	public boolean isConnected() {
		if (state == CONN_STATE_CONNECTED) {
			return true;
		}
		return false;
	}

	public boolean isDisconnected() {
		if (state == CONN_STATE_IDLE || state == CONN_STATE_CLOSED || state == CONN_STATE_DISCONNECTING) {
			return true;
		}
		return false;
	}

	public void disconnect() {
		if (null != bluetoothGatt) {
			bluetoothGatt.disconnect();
			this.state = CONN_STATE_DISCONNECTING;
		}
	}

	public void close() {
		if (null != bluetoothGatt) {
			bluetoothGatt.close();
			this.state = CONN_STATE_CLOSED;
		}
	}

	public void onConnectionStateChange(int status, int newState) {
		switch (status) {
			case 0x08:
				this.state = CONN_STATE_IDLE;
				break;
			case 0x85:
				this.state = CONN_STATE_IDLE;
				break;
			default:
				if (newState == BluetoothProfile.STATE_DISCONNECTED) {
					this.state = CONN_STATE_IDLE;
				} else if (newState == BluetoothProfile.STATE_CONNECTED) {
					this.state = CONN_STATE_CONNECTED;
					if (null != bluetoothGatt) {
						boolean operateFlag = bluetoothGatt.discoverServices();
						if (!operateFlag) {
							serviceDiscoveryState = SERVICE_DISCOVERY_FAIL;
						}
					}
				}
		}
	}

	public void onServicesDiscovered(int status) {
		if (status == BluetoothGatt.GATT_SUCCESS) {
			List<BluetoothGattService> serviceList = bluetoothGatt.getServices();
			if (CollectionUtil.isNotEmpty(serviceList)) {
				serviceDiscoveryState = SERVICE_DISCOVERY_SUCCESS;
			} else {
				serviceDiscoveryState = SERVICE_DISCOVERY_FAIL;
			}
		} else {
			serviceDiscoveryState = SERVICE_DISCOVERY_FAIL;
		}
	}

	public boolean isServiceInitSuccess() {
		return (state == CONN_STATE_CONNECTED && serviceDiscoveryState == SERVICE_DISCOVERY_SUCCESS);
	}

	public boolean isServiceInitFail() {
		return (serviceDiscoveryState == SERVICE_DISCOVERY_FAIL);
	}
}