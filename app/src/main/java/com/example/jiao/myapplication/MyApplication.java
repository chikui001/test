package com.example.jiao.myapplication;

import android.app.ActivityManager;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import com.example.jiao.myapplication.activity.MyService;
import com.example.jiao.myapplication.utils.ContextUtil;
import com.example.jiao.myapplication.utils.Foreground;
import com.example.jiao.myapplication.utils.PermissionManager;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.nostra13.universalimageloader.cache.memory.impl.LRULimitedMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.squareup.leakcanary.RefWatcher;

/**
 * Created by jiao on 16/5/6.
 */
public class MyApplication extends Application {

	private ServiceSub serviceSub;

	private Handler handler = new Handler();

	private static MyApplication instance;

	private CallBack callBack;

	public static MyApplication getInstance() {
		return instance;
	}

	public void set(CallBack callBack) {
		this.callBack = callBack;
	}

	private RefWatcher refWatcher;

	private ReportThread reportThread;

	public Handler getReportHandler() {
		return reportThread.getHandler();
	}

	public RefWatcher getRefWatcher() {
		return refWatcher;
	}

	public PermissionManager getPermissionManager() {
		return permissionManager;
	}

	private PermissionManager permissionManager;

	private static final String APATCH_PATH = "/out.apatch";

	public ReferenceTest referenceTest;

	@Override
	public void onCreate() {
		super.onCreate();
		referenceTest = new ReferenceTest();
		Fresco.initialize(this);
		Foreground.init(this);
		instance = this;
		permissionManager = new PermissionManager(this);

		ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(getApplicationContext())
				.memoryCache(new LRULimitedMemoryCache(10 * 1024 * 1024)).build();

		//Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(configuration);
		//		patchManager = new PatchManager(this);
		//		PackageInfo packageInfo = null;
		//		try {
		//			packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
		//			String versionName = packageInfo.versionName;
		//			patchManager.init(versionName);
		//			patchManager.loadPatch();
		//		} catch (PackageManager.NameNotFoundException e) {
		//		}
		//		try {
		//			// .apatch file path
		//			String patchFileString = Environment.getExternalStorageDirectory().getAbsolutePath() + APATCH_PATH;
		//			patchManager.addPatch(patchFileString);
		//		} catch (IOException e) {
		//		}
	}

	private String getCurProcessName(Context context) {
		int pid = android.os.Process.myPid();
		ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		for (ActivityManager.RunningAppProcessInfo appProcess : mActivityManager.getRunningAppProcesses()) {
			if (appProcess.pid == pid) {
				return appProcess.processName;
			}
		}
		return null;
	}

	private void registerWhenInMainProcess() {
		if (ContextUtil.isMainProcess(this)) {
			Intent serviceIntent = new Intent(this, MyService.class);
			bindService(serviceIntent, conn, Context.BIND_AUTO_CREATE);
		}
	}

	public String getApplicationName() {
		PackageManager packageManager = null;
		ApplicationInfo applicationInfo = null;
		try {
			packageManager = getApplicationContext().getPackageManager();
			applicationInfo = packageManager.getApplicationInfo(getPackageName(), 0);
		} catch (PackageManager.NameNotFoundException e) {
			applicationInfo = null;
		}
		String applicationName = (String) packageManager.getApplicationLabel(applicationInfo);
		return applicationName;
	}

	private ServiceConnection conn = new ServiceConnection() {

		@Override
		public void onServiceDisconnected(ComponentName name) {
			serviceSub = null;
		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			serviceSub = ServiceSub.Stub.asInterface(service);
			try {
				serviceSub.register(localStub);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
			new Thread() {

				@Override
				public void run() {
					try {
						serviceSub.read(new int[] { 1, 2, 3 }, new int[] {});
					} catch (RemoteException e) {
						e.printStackTrace();
					}
				}
			}.start();
		}
	};

	public static String getCurrentProcessName(Context context) {
		int pid = android.os.Process.myPid();
		ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		for (ActivityManager.RunningAppProcessInfo appProcess : mActivityManager.getRunningAppProcesses()) {
			if (appProcess.pid == pid) {
				return appProcess.processName;
			}
		}
		return null;
	}

	private LocalStub.Stub localStub = new LocalStub.Stub() {

		@Override
		public void onReceive(byte[] payload) throws RemoteException {
		}
	};

}
