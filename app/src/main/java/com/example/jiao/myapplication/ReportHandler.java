package com.example.jiao.myapplication;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * Created by jiao on 16/8/18.
 */
public class ReportHandler extends Handler {

	public ReportHandler() {
	}

	@Override
	public void handleMessage(Message msg) {
		super.handleMessage(msg);
		Log.d("jiao", msg.what + "");

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
		}
	}
}
