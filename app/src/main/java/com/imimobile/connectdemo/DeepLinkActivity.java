/*******************************************************************************
 * DeepLinkActivity.java
 * DeepLinkActivity
 * <p>
 * Created by Ashish Das on 17/01/17.
 * Copyright © 2017 IMImobile. All rights reserved.
 ******************************************************************************/

package com.imimobile.connectdemo;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.imimobile.connect.core.IMIconnect;
import com.imimobile.connectdemo.utils.Utils;

public class DeepLinkActivity extends BaseAppCompatActivity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_deep_link);

		if (IMIconnect.isRegistered()
				&& !TextUtils.isEmpty(Utils.getLoggedUserId(this)))
		{

			Intent intent = new Intent(this, HomeActivity.class);
			intent.setData(getIntent().getData());
			startActivity(intent);
		}

		finish();
	}

	public static String getDeepLink(Intent intent)
	{
		if (intent != null && intent.getData() != null)
		{
			return intent.getData().getPath();
		}
		return null;
	}
}
