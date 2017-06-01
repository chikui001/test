package com.example.jiao.myapplication.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.bumptech.glide.Glide;
import com.example.jiao.myapplication.*;
import com.example.jiao.myapplication.views.PaoView;
import com.example.jiao.myapplication.views.SuperLoadingProgress;
import com.facebook.drawee.view.SimpleDraweeView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.squareup.picasso.Picasso;

import java.io.File;

/**
 * @author jiao, <wenliang.jiao@extantfuture.com>
 * @date 2017年04月05日
 */
public class SecondActivity extends BaseActivity implements ProgressResponseBody.ProgressListener {

	File file;
	private ProgressDownloader progressDownloader;

	@Bind(R.id.test_progress)
	ProgressBar progressBar;

	@Bind(R.id.picasso_iv)
	ImageView picassoImageView;
	@Bind(R.id.glide_iv)
	ImageView glideImageView;
	@Bind(R.id.imageload_iv)
	ImageView imageLoaderImageView;
	@Bind(R.id.fresco_iv)
	SimpleDraweeView frescoImageView;
	@Bind(R.id.super_progress)
	SuperLoadingProgress superProgress;

	@Bind(R.id.pao)
	PaoView paoView;

	private long totalBytes;
	private long breakPoints;
	private long contentLength;

	@Override
	public void onPreExecute(long contentLength) {
		if (this.contentLength == 0L) {
			this.contentLength = contentLength;
		}
	}

	@Override
	public void update(long totalBytess, boolean done) {
		// 注意加上断点的长度
		this.totalBytes = totalBytess + breakPoints;
		Log.d("xxx", "totalBytes:" + totalBytes);
		Log.d("xxx", "contentLength:" + contentLength);
		int progress = (int) (totalBytes * 100 / contentLength);
		Log.d("xxx", "progress:" + progress);
		progressBar.setProgress(progress);
	}

	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.pao_start:
			case R.id.pao:
				Log.d("xxx", ImageLoader.getInstance().getMemoryCache() + "");
				startActivity(new Intent(SecondActivity.this, MainActivity2.class));
				finish();
				break;
			case R.id.restart:
				superProgress.recoveryAndRestart();
				break;
			case R.id.start:
				breakPoints = 0;
				progressDownloader.download(0);
				Log.d("xxx", "开始下载");
				break;
			case R.id.pause:
				progressDownloader.pause();
				breakPoints = totalBytes;
				break;
			case R.id.download_continue:
				progressDownloader.download(breakPoints);
				break;
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		file = new File(getCacheDir(), "modoo_intro.mp4");
		progressDownloader = new ProgressDownloader("http://ef-operate.oss-cn-qingdao.aliyuncs.com/video/modoo_intro.mp4", file,
													this);
		setContentView(R.layout.activity_second);
		ButterKnife.bind(this);
		Picasso.with(this).load("http://d.5857.com/gqyhx_131102/004.jpg").into(picassoImageView);
		Glide.with(this).load("http://d.5857.com/gqyhx_131102/004.jpg").into(glideImageView);
		Uri uri = Uri.parse("http://d.5857.com/gqyhx_131102/004.jpg");
		frescoImageView.setImageURI(uri);

		DisplayImageOptions displayImageOptions = new DisplayImageOptions.Builder().cacheInMemory(true).build();
		ImageLoader.getInstance().displayImage("http://d.5857.com/gqyhx_131102/004.jpg", imageLoaderImageView, displayImageOptions);

		Action action = new Action(imageLoaderImageView, MyApplication.getInstance().referenceTest);

		MyApplication.getInstance().referenceTest.targetToAction.put(imageLoaderImageView, action);

		progressBar = (ProgressBar) findViewById(R.id.test_progress);

	}

}
