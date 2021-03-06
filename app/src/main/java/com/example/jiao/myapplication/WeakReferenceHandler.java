package com.example.jiao.myapplication;

import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

public abstract class WeakReferenceHandler<T> extends Handler {

	private final WeakReference<T> mReference;

	public WeakReferenceHandler(T reference) {
		mReference = new WeakReference<T>(reference);
	}


	@Override
	public void handleMessage(Message msg) {
		if (mReference.get() == null) {
			return;
		}
		handleMessage(mReference.get(), msg);
	}

	protected abstract void handleMessage(T reference, Message msg);
}
