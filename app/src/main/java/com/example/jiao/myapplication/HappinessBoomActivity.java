package com.example.jiao.myapplication;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @author jiao, <wenliang.jiao@extantfuture.com>
 * @date 2016年12月21日
 */
public class HappinessBoomActivity extends AppCompatActivity {

	private FrameLayout startLayout;

	private FrameLayout secondLayout;

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

	private void showPageTwo() {
		ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(startLayout, "translationX", 0, -300);
		objectAnimator.setDuration(400);
		objectAnimator.start();
		objectAnimator.addListener(new Animator.AnimatorListener() {

			@Override
			public void onAnimationStart(Animator animation) {

			}

			@Override
			public void onAnimationEnd(Animator animation) {
				startLayout.setVisibility(View.GONE);
			}

			@Override
			public void onAnimationCancel(Animator animation) {

			}

			@Override
			public void onAnimationRepeat(Animator animation) {

			}
		});
		secondLayout.setVisibility(View.VISIBLE);
		ObjectAnimator objectAnimatorB = ObjectAnimator.ofFloat(secondLayout, "alpha", 0, 1);
		objectAnimatorB.setDuration(1000);
		objectAnimatorB.start();
		objectAnimatorB.addListener(new Animator.AnimatorListener() {

			@Override
			public void onAnimationStart(Animator animation) {

			}

			@Override
			public void onAnimationEnd(Animator animation) {

			}

			@Override
			public void onAnimationCancel(Animator animation) {

			}

			@Override
			public void onAnimationRepeat(Animator animation) {

			}
		});

	}

	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.jump_tv:
				showPageTwo();
				break;
			case R.id.daughter_bottom_tv:
				if (state != SHOW_TAG_SELECT) {
					state = SHOW_TAG_SELECT;
					showTagSelect();
				}
				break;
			case R.id.son_bottom_tv:
				if (state != SHOW_TAG_SELECT) {
					state = SHOW_TAG_SELECT;
					showTagSelect();
				}
				break;
			case R.id.daughter_layout:
				if (state == SHOW_TAG_SELECT) {
					daughterTagImageView.setVisibility(View.VISIBLE);
					dividerView.setVisibility(View.GONE);
					sonLayout.setVisibility(View.GONE);
					rightEnter();
					state = SHOW_TAKE_PHOTO;
					selectState = SELECT_DAUGHTER;
				} else {
					//take photo for daughter
				}
				break;
			case R.id.son_layout:
				if (state == SHOW_TAG_SELECT) {
					sonTagImageView.setVisibility(View.VISIBLE);
					dividerView.setVisibility(View.GONE);
					daughterLayout.setVisibility(View.GONE);
					leftEnter();
					state = SHOW_TAKE_PHOTO;
					selectState = SELECT_SON;
				} else {
					//take photo for son

				}
				break;
			case R.id.input_name_layout:

				break;
			case R.id.input_birthday_layout:

				break;
			case R.id.input_weight_layout:

				break;
			case R.id.input_height_layout:

				break;
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_happiness_boom);
		startLayout = (FrameLayout) findViewById(R.id.start_fl);
		secondLayout = (FrameLayout) findViewById(R.id.second_fl);

		dividerView = findViewById(R.id.divider_view);
		sonLayout = (LinearLayout) findViewById(R.id.son_layout);
		daughterLayout = (LinearLayout) findViewById(R.id.daughter_layout);

		sonImageView = (ImageView) findViewById(R.id.son_iv);
		sonTagImageView = (ImageView) findViewById(R.id.son_tag_iv);
		sonBottomTextView = (TextView) findViewById(R.id.son_bottom_tv);

		daughterImageView = (ImageView) findViewById(R.id.daughter_iv);
		daughterTagImageView = (ImageView) findViewById(R.id.daughter_tag_iv);
		daughterBottomTextView = (TextView) findViewById(R.id.daughter_bottom_tv);

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
}
