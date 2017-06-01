package com.example.jiao.myapplication.views.jiao;

import android.animation.Animator;
import android.view.View;

public interface IAnimationFactory {

	void fadeInView(View target, long duration, AnimationStartListener listener);

	void fadeInView(View target, long duration, Animator.AnimatorListener listener);

	void fadeOutView(View target, long duration, AnimationEndListener listener);

	void fadeOutView(View target, long duration, Animator.AnimatorListener listener);

	public interface AnimationStartListener {

		void onAnimationStart();
	}

	public interface AnimationEndListener {

		void onAnimationEnd();
	}
}

