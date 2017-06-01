package com.example.jiao.myapplication.views;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.jiao.myapplication.R;

/**
 * @author jiao, <wenliang.jiao@extantfuture.com>
 * @date 2016年11月28日
 */
public class GiftXDialogFragment extends BaseDialogFragment {

	private boolean cancelable = true;

	private boolean canceledOnTouchOutside = true;

	private LinearLayout sonLayout;
	private LinearLayout daughterLayout;

	private View dividerView;

	private ImageView sonImageView;
	private ImageView sonTagImageView;
	private TextView sonBottomTextView;

	private ImageView daughterImageView;
	private ImageView daughterTagImageView;
	private TextView daughterBottomTextView;

	private static final int SHOW_TAG_SELECT = 1;
	private static final int SHOW_TAKE_PHOTO = 2;

	private static final int SELECT_SON = 1;
	private static final int SELECT_DAUGHTER = 2;
	private int state = SHOW_TAG_SELECT;
	private int selectState = SELECT_SON;

	public void setNewBabyInputListener(NewBabyInputListener newBabyInputListener) {
		this.newBabyInputListener = newBabyInputListener;
	}

	private NewBabyInputListener newBabyInputListener;

	@NonNull
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AdDialog dialog = new GiftADialog(getActivity());
		dialog.setCloseOnTouchOutside(canceledOnTouchOutside);
		setCancelable(cancelable);
		return dialog;
	}

	@Override
	public void onDismiss(DialogInterface dialog) {
		super.onDismiss(dialog);
	}

	@Override
	protected int getSelfTheme() {
		return 0;
	}

	@Override
	protected void reThemeWindow() {

	}

	@Override
	protected void reSizeWindow() {

	}

	private void leftEnter() {
		ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(sonLayout, "translationX", -100, 0);
		objectAnimator.setDuration(400);
		objectAnimator.start();
		objectAnimator.addListener(new Animator.AnimatorListener() {

			@Override
			public void onAnimationStart(Animator animation) {

			}

			@Override
			public void onAnimationEnd(Animator animation) {
				//替换为拍照的icon
				sonImageView.setImageResource(R.drawable.shangchuan_btn);
				sonBottomTextView.setText("请上传小王子的照片");

				sonTagImageView.setVisibility(View.GONE);
			}

			@Override
			public void onAnimationCancel(Animator animation) {

			}

			@Override
			public void onAnimationRepeat(Animator animation) {

			}
		});

	}

	private void rightEnter() {
		ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(daughterLayout, "translationX", 100, 0);
		objectAnimator.setDuration(400);
		objectAnimator.start();
		objectAnimator.addListener(new Animator.AnimatorListener() {

			@Override
			public void onAnimationStart(Animator animation) {

			}

			@Override
			public void onAnimationEnd(Animator animation) {
				//替换为拍照的icon
				daughterImageView.setImageResource(R.drawable.shangchuan_btn);
				daughterBottomTextView.setText("请上传小公主的照片");

				daughterTagImageView.setVisibility(View.GONE);
			}

			@Override
			public void onAnimationCancel(Animator animation) {

			}

			@Override
			public void onAnimationRepeat(Animator animation) {

			}
		});
	}

	private void showTagSelect() {
		dividerView.setVisibility(View.VISIBLE);
		if (selectState == SELECT_SON) {
			daughterLayout.setVisibility(View.VISIBLE);

			sonTagImageView.setVisibility(View.VISIBLE);
			daughterTagImageView.setVisibility(View.GONE);

			sonImageView.setImageResource(R.drawable.son_icon);
			sonBottomTextView.setText("小王子");
		} else {
			sonLayout.setVisibility(View.VISIBLE);

			daughterTagImageView.setVisibility(View.VISIBLE);
			sonTagImageView.setVisibility(View.GONE);

			daughterImageView.setImageResource(R.drawable.daughter_icon);
			daughterBottomTextView.setText("小公主");
		}
		ObjectAnimator objectAnimatorSon = ObjectAnimator.ofFloat(sonLayout, "translationX", 100, 0);
		objectAnimatorSon.setDuration(400);
		objectAnimatorSon.start();

		ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(daughterLayout, "translationX", -100, 0);
		objectAnimator.setDuration(400);
		objectAnimator.start();
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.layout_happiness_boom_fill_info, null);
		dividerView = view.findViewById(R.id.divider_view);
		sonLayout = (LinearLayout) view.findViewById(R.id.son_layout);
		daughterLayout = (LinearLayout) view.findViewById(R.id.daughter_layout);

		sonImageView = (ImageView) view.findViewById(R.id.son_iv);
		sonTagImageView = (ImageView) view.findViewById(R.id.son_tag_iv);
		sonBottomTextView = (TextView) view.findViewById(R.id.son_bottom_tv);

		daughterImageView = (ImageView) view.findViewById(R.id.daughter_iv);
		daughterTagImageView = (ImageView) view.findViewById(R.id.daughter_tag_iv);
		daughterBottomTextView = (TextView) view.findViewById(R.id.daughter_bottom_tv);

		sonLayout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (state == SHOW_TAG_SELECT) {
					sonTagImageView.setVisibility(View.VISIBLE);
					dividerView.setVisibility(View.GONE);
					daughterLayout.setVisibility(View.GONE);
					leftEnter();
					state = SHOW_TAKE_PHOTO;
					selectState = SELECT_SON;
				} else {
					//take photo for son
					newBabyInputListener.onTakeBabyPhoto();
				}
			}
		});

		daughterLayout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (state == SHOW_TAG_SELECT) {
					daughterTagImageView.setVisibility(View.VISIBLE);
					dividerView.setVisibility(View.GONE);
					sonLayout.setVisibility(View.GONE);
					rightEnter();
					state = SHOW_TAKE_PHOTO;
					selectState = SELECT_DAUGHTER;
				} else {
					//take photo for daughter
					newBabyInputListener.onTakeBabyPhoto();
				}
			}
		});
		sonBottomTextView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (state != SHOW_TAG_SELECT) {
					state = SHOW_TAG_SELECT;
					showTagSelect();
				}
			}
		});
		daughterBottomTextView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (state != SHOW_TAG_SELECT) {
					state = SHOW_TAG_SELECT;
					showTagSelect();
				}
			}
		});

		return view;
	}

	private static class GiftADialog extends AdDialog {

		public GiftADialog(Context context) {
			super(context);
		}

		@Override
		public AnimatorSet onCreateShowAnimationSet(View rootView) {
			AnimatorSet animatorSet = new AnimatorSet();
			animatorSet.playTogether(ObjectAnimator.ofFloat(rootView, "scaleX", 0.1f, 0.475f, 1),
									 ObjectAnimator.ofFloat(rootView, "scaleY", 0.1f, 0.475f, 1),
									 ObjectAnimator.ofFloat(rootView, "translationY", -1000, -200, 0),
									 ObjectAnimator.ofFloat(rootView, "alpha", 0, 1, 1));
			animatorSet.setDuration(2000);
			return animatorSet;
		}

		@Override
		public AnimatorSet onCreateDismissAnimationSet(View rootView) {
			AnimatorSet animatorSet = new AnimatorSet();
			animatorSet.playTogether(ObjectAnimator.ofFloat(rootView, "scaleX", 1f, 0.2f),
									 ObjectAnimator.ofFloat(rootView, "scaleY", 1f, 0.2f),
									 ObjectAnimator.ofFloat(rootView, "translationY", 0, 400),
									 ObjectAnimator.ofFloat(rootView, "translationX", 0, 400),
									 ObjectAnimator.ofFloat(rootView, "alpha", 1, 0.5f));
			animatorSet.setDuration(1000);
			return animatorSet;
		}
	}

	public interface NewBabyInputListener {

		void onTakeBabyPhoto();

		void onInputBabyName();

		void onInputBabyBirthday();

		void onInputBabyWeight();

		void onInputBabyHeight();
	}

}
