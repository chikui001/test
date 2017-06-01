package com.example.jiao.myapplication.activity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.example.jiao.myapplication.BaseActivity;
import com.example.jiao.myapplication.R;
import com.example.jiao.myapplication.utils.MyLifecycleHandler;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.squareup.picasso.Picasso;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MainActivity2 extends BaseActivity {

	private boolean mFragmentAbleToCommit = true;

	@Bind(R.id.test)
	ImageView testView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		EventBus.getDefault().register(this);
		getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
		mFragmentAbleToCommit = true;
		setContentView(R.layout.activity_test);
		ButterKnife.bind(this);
		Picasso.with(this).setLoggingEnabled(true);
		DisplayImageOptions displayImageOptions = new DisplayImageOptions.Builder().cacheInMemory(true).build();
		ImageLoader.getInstance().displayImage("https://img3.doubanio.com/view/photo/lphoto/public/p2457863315.webp", testView,
											   displayImageOptions);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	@Override
	protected void onStop() {
		super.onStop();
		mFragmentAbleToCommit = false;
		Log.d("xxx", ImageLoader.getInstance().getMemoryCache() + "");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
		Log.d("xxx", ImageLoader.getInstance().getMemoryCache() + "");
	}

	@Subscribe(threadMode = ThreadMode.MAIN)
	public void onAppSwitchMessageEvent(MyLifecycleHandler.AppSwitchMessageEvent messageEvent) {
		Log.d("xxx", "messageEvent:" + messageEvent + ";thread:" + Thread.currentThread().getName());
	}

	@Override
	public void onStateNotSaved() {
		super.onStateNotSaved();
		mFragmentAbleToCommit = true;
	}

	@Override
	protected void onResumeFragments() {
		super.onResumeFragments();
		mFragmentAbleToCommit = true;
	}

	@Override
	protected void onStart() {
		super.onStart();
		mFragmentAbleToCommit = true;
	}

}
