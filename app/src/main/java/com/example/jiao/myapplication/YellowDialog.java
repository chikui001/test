package com.example.jiao.myapplication;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.extantfuture.util.StringUtil;

/**
 * if showDialog is called in onCreate etc ..
 * java.lang.IllegalStateException: Can not perform this action after onSaveInstanceState
 * http://www.androiddesignpatterns.com/2013/08/fragment-transaction-commit-state-loss.html
 * Created by jiao on 16/4/8.
 */
public class YellowDialog extends DialogFragment {

	private View rootView;
	private String title;
	private String content;
	private String topButtonString;
	private String bottomButtonString;

	private TextView titleView;
	private TextView contentView;
	private TextView topTextView;
	private TextView bottomTextView;

	private View.OnClickListener topClickListener;
	private View.OnClickListener bottomClickListener;

	/**
	 * Sets whether this dialog is cancelable with the
	 * {@link KeyEvent#KEYCODE_BACK BACK} key.
	 */
	private boolean cancelable = true;

	private boolean canceledOnTouchOutside = true;

	public YellowDialog() {
		super();
	}

	@Override
	public void show(FragmentManager manager, String tag) {
		setStyle(DialogFragment.STYLE_NO_TITLE, 0);
		super.show(manager, tag);
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		Dialog dialog = getDialog();
		if (null != dialog) {
			dialog.getWindow().setWindowAnimations(R.style.AnimBottom);
			dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
			dialog.setCanceledOnTouchOutside(canceledOnTouchOutside);
		}
		rootView = inflater.inflate(R.layout.template_popwindow, container, false);
		titleView = (TextView) rootView.findViewById(R.id.title_tv);
		if (StringUtil.isEmpty(title)) {
			titleView.setVisibility(View.GONE);
		} else {
			titleView.setText(title);
		}
		contentView = (TextView) rootView.findViewById(R.id.content_tv);
		if (StringUtil.isEmpty(content)) {
			contentView.setVisibility(View.GONE);
		} else {
			contentView.setText(content);
		}

		topTextView = (TextView) rootView.findViewById(R.id.top_tv);
		if (StringUtil.isEmpty(topButtonString)) {
			topTextView.setVisibility(View.GONE);
		} else {
			topTextView.setText(topButtonString);
			topTextView.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					dismiss();
					if (null != topClickListener) {
						topClickListener.onClick(v);
					}
				}
			});
		}

		bottomTextView = (TextView) rootView.findViewById(R.id.bottom_tv);
		if (StringUtil.isEmpty(bottomButtonString)) {
			bottomTextView.setVisibility(View.GONE);
		} else {
			bottomTextView.setText(bottomButtonString);
			bottomTextView.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					dismiss();
					if (null != bottomClickListener) {
						bottomClickListener.onClick(v);
					}
				}
			});
		}
		setCancelable(cancelable);
		return rootView;
	}

	public YellowDialog setMyCanceledOnTouchOutside(boolean canceledOnTouchOutside) {
		this.canceledOnTouchOutside = canceledOnTouchOutside;
		return this;
	}

	public YellowDialog setMyCancelable(boolean cancelable) {
		this.cancelable = cancelable;
		return this;
	}

	public YellowDialog setTopButtonListener(View.OnClickListener topButtonListener) {
		if (null != topButtonListener) {
			this.topClickListener = topButtonListener;
		}
		return this;
	}

	public YellowDialog setBottomButtonListener(View.OnClickListener bottomButtonListener) {
		if (null != bottomButtonListener) {
			this.bottomClickListener = bottomButtonListener;
		}
		return this;
	}

	public YellowDialog setBottomButtonString(String bottomButtonString) {
		this.bottomButtonString = bottomButtonString;
		return this;
	}

	public YellowDialog setTopButtonString(String topButtonString) {
		this.topButtonString = topButtonString;
		return this;
	}

	public YellowDialog setContent(String content) {
		this.content = content;
		return this;
	}

	public YellowDialog setTitle(String title) {
		this.title = title;
		return this;
	}

	public void refreshContent() {
		if (null != contentView) {
			contentView.setText("焦");
		}
	}
}
