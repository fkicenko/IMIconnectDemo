/*******************************************************************************
 * LaunchActivity.java
 * LaunchActivity
 * <p>
 * Created by Ashish Das on 17/01/17.
 * Copyright © 2017 IMImobile. All rights reserved.
 ******************************************************************************/

package com.imimobile.connectdemo;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.imimobile.connect.core.IMIconnect;

public class LaunchActivity extends BaseAppCompatActivity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		getWindow().getAttributes().windowAnimations = R.style.Fade;

		setContentView(R.layout.activity_launch);

		((TextView) findViewById(R.id.tv_sdk_version)).setText("v" + IMIconnect.getSDKVersion());

		findViewById(R.id.btn_login).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(final View v)
			{
				Intent intent = new Intent(LaunchActivity.this, LoginActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
						                | Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
				finish();
			}
		});

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
		{
			findViewById(R.id.btn_login).setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
		}
	}
}
