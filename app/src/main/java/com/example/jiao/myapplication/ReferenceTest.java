package com.example.jiao.myapplication;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.util.Log;

import java.lang.ref.ReferenceQueue;
import java.util.Map;
import java.util.WeakHashMap;

import static android.os.Process.THREAD_PRIORITY_BACKGROUND;

/**
 * @author jiao, <wenliang.jiao@extantfuture.com>
 * @date 2017年05月08日
 */
public class ReferenceTest {

	CleanupThread cleanupThread = null;
	Handler handler;
	final ReferenceQueue<Object> referenceQueue = new ReferenceQueue<>();
	static final int THREAD_LEAK_CLEANING_MS = 1000;
	static final int REQUEST_GCED = 100;
	public final Map<Object, Action> targetToAction;

	public ReferenceTest() {
		targetToAction = new WeakHashMap<>();
		handler = new Handler(Looper.getMainLooper()) {

			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
					case REQUEST_GCED:
						Log.d("xxx", "REQUEST_GCED");
						break;
				}
			}
		};
		cleanupThread = new CleanupThread(this.referenceQueue, handler);
		cleanupThread.start();
	}

	private static class CleanupThread extends Thread {

		private final ReferenceQueue<Object> referenceQueue;
		private final Handler handler;

		CleanupThread(ReferenceQueue<Object> referenceQueue, Handler handler) {
			this.referenceQueue = referenceQueue;
			this.handler = handler;
			setDaemon(true);
			setName("refQueue");
		}

		@Override
		public void run() {
			Process.setThreadPriority(THREAD_PRIORITY_BACKGROUND);
			while (true) {
				try {
					// Prior to Android 5.0, even when there is no local variable, the result from
					// remove() & obtainMessage() is kept as a stack local variable.
					// We're forcing this reference to be cleared and replaced by looping every second
					// when there is nothing to do.
					// This behavior has been tested and reproduced with heap dumps.
					Action.RequestWeakReference<?> remove = (Action.RequestWeakReference<?>) referenceQueue
							.remove(THREAD_LEAK_CLEANING_MS);

					Message message = handler.obtainMessage();
					if (remove != null) {
						message.what = REQUEST_GCED;
						message.obj = remove.action;
						handler.sendMessage(message);
					} else {
						message.recycle();
					}
				} catch (InterruptedException e) {
					break;
				} catch (final Exception e) {
					handler.post(new Runnable() {

						@Override
						public void run() {
							throw new RuntimeException(e);
						}
					});
					break;
				}
			}
		}

		void shutdown() {
			interrupt();
		}
	}
}
