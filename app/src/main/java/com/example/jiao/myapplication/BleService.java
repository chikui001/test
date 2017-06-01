package com.example.jiao.myapplication;

import android.app.Service;
import android.bluetooth.*;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import com.extantfuture.util.StringUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * @author jiao, <wenliang.jiao@extantfuture.com>
 * @date 2016年12月15日
 */
public class BleService extends Service {

	private static final String TAG = "BleService";
	private BluetoothAdapter mBluetoothAdapter;
	BluetoothManager mBluetoothManager;
	private BleConnectionCompat bleConnectionCompat;
	private final IBinder mBinder = new LocalBinder();

	public class LocalBinder extends Binder {

		public BleService getService() {
			return BleService.this;
		}
	}

	@Override
	public void onCreate() {
		super.onCreate();
		bleConnectionCompat = new BleConnectionCompat(this);
		stopSelf();
	}

	@Nullable
	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}

	/**
	 * 当前连接的蓝牙的address
	 */
	private String mBluetoothDeviceAddress;
	private Map<String, BluetoothGattWrapper> addressGattMap = new HashMap<>();

	public boolean initialize() {
		// For API level 18 and above, get a reference to BluetoothAdapter through
		// BluetoothManager.
		if (mBluetoothManager == null) {
			mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
			if (mBluetoothManager == null) {
				EfLog.e(TAG, "Unable to initialize BluetoothManager.");
				return false;
			}
		}
		if (null == mBluetoothAdapter) {
			mBluetoothAdapter = mBluetoothManager.getAdapter();
			if (mBluetoothAdapter == null) {
				EfLog.e(TAG, "Unable to obtain a BluetoothAdapter.");
				return false;
			}
		}

		return true;
	}

	/**
	 * 开始连接
	 *
	 * @param deviceAddress
	 * @return
	 */
	public boolean startConnect(String deviceAddress) {
		if (StringUtil.isEmpty(deviceAddress)) {
			if (EfLog.isDebug()) {
				EfLog.w(TAG, "unspecified address.");
			}
			return false;
		}
		if (!initialize()) {
			if (EfLog.isDebug()) {
				EfLog.w(TAG, "BluetoothAdapter not initialized");
			}
			return false;
		}
		//is connecting or something just can not (or need not) recreate more
		if (addressGattMap.containsKey(deviceAddress)) {
			BluetoothGattWrapper bluetoothGattWrapper = addressGattMap.get(deviceAddress);
			if (bluetoothGattWrapper.isConnectingOrDiscovering()) {
				return false;
			}
		}
		mBluetoothDeviceAddress = deviceAddress;
		final BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(deviceAddress);
		BluetoothGatt bluetoothGatt = bleConnectionCompat.connectGatt(device, false, mGattCallback);
		addressGattMap.put(deviceAddress, new BluetoothGattWrapper(bluetoothGatt));

		// 开始蓝牙连接统计
		return true;
	}

	private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {

		/**
		 * Callback indicating when GATT client has connected/disconnected to/from a remote
		 * GATT server.
		 *
		 * @param gatt
		 *            GATT client
		 * @param status
		 *            Status of the connect or disconnect operation. {@link BluetoothGatt#GATT_SUCCESS} if the operation succeeds.
		 * @param newState
		 *            Returns the new connection state. Can be one of {@link BluetoothProfile#STATE_DISCONNECTED} or
		 *            {@link BluetoothProfile#STATE_CONNECTED}
		 */
		@Override
		public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
			BluetoothDevice device = gatt.getDevice();
			String address = device.getAddress();
			if (addressGattMap.containsKey(address)) {
				BluetoothGattWrapper bluetoothGattWrapper = addressGattMap.get(address);
				bluetoothGattWrapper.onConnectionStateChange(status, newState);
				if (bluetoothGattWrapper.isDisconnected()) {
					broadcastUpdate("ACTION_GATT_DISCONNECTED");
				}
				if (bluetoothGattWrapper.isConnected()) {
					broadcastUpdate("ACTION_GATT_CONNECTED");
				}
			}
		}

		@Override
		public void onServicesDiscovered(BluetoothGatt gatt, int status) {
			super.onServicesDiscovered(gatt, status);
			BluetoothDevice device = gatt.getDevice();
			String address = device.getAddress();
			if (addressGattMap.containsKey(address)) {
				BluetoothGattWrapper bluetoothGattWrapper = addressGattMap.get(address);
				bluetoothGattWrapper.onServicesDiscovered(status);
				if (bluetoothGattWrapper.isServiceInitSuccess()) {
					broadcastUpdate("ACTION_GATT_SERVICES_DISCOVERED");
				} else {
					broadcastUpdate("ACTION_GATT_SERVICES_DISCOVERED_FAIL");
				}
			}
		}

		private void broadcastUpdate(final String action) {
			if (StringUtil.isNotEmpty(action)) {
				final Intent intent = new Intent(action);
				sendBroadcast(intent);
			}
		}

		/**
		 * BLE终端数据被读的事件
		 * Callback reporting the result of a characteristic read operation.
		 *
		 * @param gatt
		 *            GATT client invoked {@link BluetoothGatt#readCharacteristic}
		 * @param characteristic
		 *            Characteristic that was read from the associated
		 *            remote device.
		 * @param status
		 *            {@link BluetoothGatt#GATT_SUCCESS} if the read operation
		 *            was completed successfully.
		 */
		@Override
		public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {

		}

		/**
		 * 收到BLE终端写入数据回调
		 * Callback indicating the result of a characteristic write operation.
		 *
		 * <p>
		 * If this callback is invoked while a reliable write transaction is in progress, the value of the characteristic represents
		 * the value reported by the remote device. An application should compare this value to the desired value to be written. If
		 * the values don't match, the application must abort the reliable write transaction.
		 *
		 * @param gatt
		 *            GATT client invoked {@link BluetoothGatt#writeCharacteristic}
		 * @param characteristic
		 *            Characteristic that was written to the associated
		 *            remote device.
		 * @param status
		 *            The result of the write operation {@link BluetoothGatt#GATT_SUCCESS} if the operation succeeds.
		 */
		@Override
		public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {

		}

		/**
		 * 收到通知数据
		 * Callback triggered as a result of a remote characteristic notification.
		 *
		 * @param gatt
		 *            GATT client the characteristic is associated with
		 * @param characteristic
		 *            Characteristic that has been updated as a result
		 *            of a remote notification event.
		 */
		@Override
		public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
			// EfLog.d(TAG, "onCharacteristicChanged characteristic=" + (null != characteristic ? characteristic.getUuid() : null));
		}
	};
}
