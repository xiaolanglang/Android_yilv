package com.yilvtzj.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;

import com.yilvtzj.R;
import com.yilvtzj.activity.common.BaseActivity;

public class WelcomeActivity extends BaseActivity {
	private ImageView welcomeImg = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);

		welcomeImg = (ImageView) this.findViewById(R.id.welcome_img);

		AlphaAnimation anima = new AlphaAnimation(1.0f, 1.0f);
		anima.setDuration(3000);// 设置动画显示时间
		anima.setAnimationListener(new AnimationImpl());
		welcomeImg.startAnimation(anima);

	}

	private class AnimationImpl implements AnimationListener {

		@Override
		public void onAnimationStart(Animation animation) {
			welcomeImg.setBackgroundResource(R.drawable.welcome);
		}

		@Override
		public void onAnimationEnd(Animation animation) {
			skip(); // 动画结束后跳转到别的页面
		}

		@Override
		public void onAnimationRepeat(Animation animation) {

		}

	}

	private void skip() {
		startActivity(new Intent(this, MainActivity.class));
		finish();
	}
}
