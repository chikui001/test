package com.example.jiao.myapplication.views;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import com.example.jiao.myapplication.R;

/**
 * Created by jiao on 16/7/30.
 */
public abstract class BaseDialogFragment extends DialogFragment {

	private void hideSystemUI() {
		// Set the IMMERSIVE flag.
		// Set the content to appear under the system bars so that the content
		// doesn't resize when the system bars hide and show.

	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (null == savedInstanceState) {
			if (0 == getSelfTheme()) {
				setStyle(DialogFragment.STYLE_NO_FRAME, R.style.Theme_BaseDialog);
			} else {
				setStyle(DialogFragment.STYLE_NO_FRAME, getSelfTheme());
			}
		}
	}

	@NonNull
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		Dialog dialog = super.onCreateDialog(savedInstanceState);
		reThemeWindow();
		return dialog;
	}

	@Override
	public void onDismiss(DialogInterface dialog) {
		super.onDismiss(dialog);
	}

	private String TAG = "JIAO";

	@Override
	public void onStart() {
		super.onStart();
		reSizeWindow();
	}

	@Override
	public void onResume() {
		super.onResume();
		hideSystemUI();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		//		MengdongApp.getInstance().getRefWatcher().watch(this);
	}

	protected abstract int getSelfTheme();

	/**
	 * 重新设置windowSize,因为window WindowManager.LayoutParams默认是match_parent
	 */
	protected abstract void reThemeWindow();

	protected abstract void reSizeWindow();

}
