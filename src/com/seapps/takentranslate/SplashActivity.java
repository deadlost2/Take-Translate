package com.seapps.takentranslate;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.TimeInterpolator;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.BounceInterpolator;
import android.widget.ImageView;

public class SplashActivity extends Activity {

	private ImageView im;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);

		im = (ImageView) findViewById(R.id.im_splash);

		im.setTranslationX(-300);
		im.setTranslationY(-900);
		im.setRotation(-300);

		TimeInterpolator i = new AnticipateOvershootInterpolator(3f);
		im.animate().translationX(0).translationY(0).rotation(0)
				.setDuration(2000).setListener(l1).setInterpolator(i).start();

	}

	AnimatorListener l1 = new AnimatorListener() {
		@Override
		public void onAnimationStart(Animator animation) {
		}

		@Override
		public void onAnimationRepeat(Animator animation) {
		}

		@Override
		public void onAnimationEnd(Animator animation) {
			TimeInterpolator i = new BounceInterpolator();
			im.animate().alpha(0).setDuration(2000).setListener(l2)
					.setInterpolator(i).start();
		}

		@Override
		public void onAnimationCancel(Animator animation) {
		}
	};
	AnimatorListener l2 = new AnimatorListener() {
		@Override
		public void onAnimationStart(Animator animation) {
		}

		@Override
		public void onAnimationRepeat(Animator animation) {
		}

		@Override
		public void onAnimationEnd(Animator animation) {
			finishSplash(im);
		}

		@Override
		public void onAnimationCancel(Animator animation) {
		}
	};

	public void finishSplash(View v) {
		startActivity(new Intent(this, MainActivity.class));
		this.finish();
	}
}
