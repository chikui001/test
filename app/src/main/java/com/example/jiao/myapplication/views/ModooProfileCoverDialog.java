package com.example.jiao.myapplication.views;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.*;
import com.example.jiao.myapplication.R;

/**
 * @author jiao, <wenliang.jiao@extantfuture.com>
 * @date 2016年12月05日
 */
public class ModooProfileCoverDialog extends BaseDialogFragment {

	private View rootView;
	private boolean cancelable = false;
	private boolean canceledOnTouchOutside = false;

	@Override
	protected int getSelfTheme() {
		return 0;
	}

	@Override
	protected void reThemeWindow() {
		Dialog dialog = getDialog();
		if (null != dialog) {
			dialog.setCanceledOnTouchOutside(canceledOnTouchOutside);
		}
	}

	@Override
	protected void reSizeWindow() {
		Dialog dialog = getDialog();
		if (null != dialog) {
			Window window = dialog.getWindow();
			window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
		}
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.layout_modoo_profile_cover, null);
		return rootView;
	}
}
