/*******************************************************************************
 * NotificationUtils.java
 * NotificationUtils
 * <p>
 * Created by Ashish Das on 01/11/18.
 * Copyright © 2018 IMImobile. All rights reserved.
 ******************************************************************************/

package com.imimobile.connectdemo.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.text.TextUtils;

import androidx.annotation.RequiresApi;

public class NotificationUtils
{
	@RequiresApi(api = Build.VERSION_CODES.O)
	public static NotificationChannel getChannel(Context context, String id, String name)
	{
		return getChannel(context, id, name, "");
	}

	@RequiresApi(api = Build.VERSION_CODES.O)
	public static NotificationChannel getChannel(Context context, String id, String name, String description)
	{
		int importance = NotificationManager.IMPORTANCE_HIGH;
		NotificationChannel notificationChannel = new NotificationChannel(id, name, importance);
		// Configure the notification channel.
		if (!TextUtils.isEmpty(description))
		{
			notificationChannel.setDescription(description);
		}
		notificationChannel.enableLights(true);
		// Sets the notification light color for notifications posted to this
		// channel, if the device supports this feature.
		notificationChannel.setLightColor(Color.RED);
		notificationChannel.enableVibration(true);
		notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});

		return notificationChannel;
	}
}
