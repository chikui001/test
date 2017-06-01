package com.example.jiao.myapplication.activity;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import com.example.jiao.myapplication.LocalStub;
import com.example.jiao.myapplication.ServiceSub;

/**
 * Created by jiao on 16/5/6.
 */
public class MyService extends Service {

	private Handler handler = new Handler();

	@Override
	public void onCreate() {
		super.onCreate();

	}

	@Nullable
	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}

	private final RemoteCallbackList<LocalStub> mCallbacks = new RemoteCallbackList<LocalStub>();
	private ServiceSub.Stub mBinder = new ServiceSub.Stub() {

		@Override
		public void read(int[] request, int[] result) throws RemoteException {
			int N = mCallbacks.beginBroadcast();
			int start = 0;
			int end = request.length - 1;
			while (start != end) {
				int temp = request[start];
				request[start] = request[end];
				request[end] = temp;
			}
			result = request;
		}

		@Override
		public void register(LocalStub localStub) throws RemoteException {
			mCallbacks.register(localStub);
		}

		@Override
		public void unReigister(LocalStub localStub) throws RemoteException {
			mCallbacks.unregister(localStub);
		}
	};

}
