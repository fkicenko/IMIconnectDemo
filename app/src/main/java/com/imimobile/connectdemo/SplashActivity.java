/*******************************************************************************
 * SplashActivity.java
 * SplashActivity
 * <p>
 * Created by Ashish Das on 17/01/17.
 * Copyright © 2017 IMImobile. All rights reserved.
 ******************************************************************************/

package com.imimobile.connectdemo;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;

import com.imimobile.connect.core.IMIconnect;
import com.imimobile.connectdemo.utils.Utils;

public class SplashActivity extends BaseAppCompatActivity
{
	private static int SPLASH_TIME_OUT = 200;
	private static int SPLASH_ANIMATION_DURATION = 2000;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		getWindow().getAttributes().windowAnimations = R.style.Fade;

		setContentView(R.layout.activity_splash);
		startAnimation();
	}

	private void startAnimation()
	{
		Animation fadeIn = new AlphaAnimation(0, 1);
		fadeIn.setInterpolator(new DecelerateInterpolator());
		fadeIn.setDuration(SPLASH_ANIMATION_DURATION);

		AnimationSet animation = new AnimationSet(false); //change to false
		animation.addAnimation(fadeIn);

		fadeIn.setAnimationListener(new Animation.AnimationListener()
		{
			@Override
			public void onAnimationStart(final Animation animation)
			{
			}

			@Override
			public void onAnimationEnd(final Animation animation)
			{
				startActivity();
			}

			@Override
			public void onAnimationRepeat(final Animation animation)
			{

			}
		});

		findViewById(R.id.rl_branding).setAnimation(animation);
	}

	private void startActivity()
	{
		new Handler().postDelayed(new Runnable()
		{
			@Override
			public void run()
			{
				Class tClass = HomeActivity.class;

				if (!IMIconnect.isRegistered()
						|| TextUtils.isEmpty(Utils.getLoggedUserId(SplashActivity.this)))
				{
					tClass = LaunchActivity.class;
				}
				Intent intent = new Intent(SplashActivity.this, tClass);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
						                | Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
				finish();

			}
		}, SPLASH_TIME_OUT);
	}
}
