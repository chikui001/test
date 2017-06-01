package com.example.jiao.myapplication;

import java.lang.ref.WeakReference;

/**
 * Created by jiao on 16/6/13.
 */
public class CallBack {

	public CallBack(Listener listener, int id) {
		this.listener = new WeakReference<Listener>(listener);
		this.id = id;
	}

	public WeakReference<Listener> getListener() {
		return listener;
	}

	private WeakReference<Listener> listener;

	public CallBack(int id) {
		this.id = id;
	}

	int id;

	public void onStart(String msg) {

	}

	interface Listener {

		void onClick();
	}

}
