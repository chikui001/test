package com.example.jiao.myapplication;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.CountDownLatch;

/**
 * Created by jiao on 16/8/18.
 */
final class ReportThread extends Thread {

	private Handler handler;
	private final CountDownLatch handlerInitLatch;

	ReportThread() {
		handlerInitLatch = new CountDownLatch(1);
	}

	Handler getHandler() {
		try {
			handlerInitLatch.await();
		} catch (InterruptedException ie) {
			// continue?
		}
		return handler;
	}

	@Override
	public void run() {
		Looper.prepare();
		handler = new ReportHandler();
		handlerInitLatch.countDown();
		Looper.loop();
	}

}