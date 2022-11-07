/*******************************************************************************
 * DemoApplication.java
 * DemoApplication
 * <p>
 * Created by Ashish Das on 17/01/17.
 * Copyright © 2017 IMImobile. All rights reserved.
 ******************************************************************************/

package com.imimobile.connectdemo;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import com.imimobile.connect.core.ICConstants;
import com.imimobile.connect.core.ICLogger;
import com.imimobile.connect.core.IMIconnect;
import com.imimobile.connect.core.enums.ICLogTarget;
import com.imimobile.connect.core.enums.ICLogType;
import com.imimobile.connect.core.exception.ICException;
import com.imimobile.connect.core.inappnotification.ICInAppNotificationManager;
import com.imimobile.connect.core.inappnotification.views.ICInAppBannerNotificationViewBinderFactory;
import com.imimobile.connect.core.inappnotification.views.ICInAppModalNotificationViewBinderFactory;
import com.imimobile.connect.core.messaging.ICMessaging;
import com.imimobile.connectdemo.datas.roomdb.AppDatabase;
import com.imimobile.connectdemo.datas.roomdb.DBClient;

import androidx.annotation.RequiresApi;

import static com.imimobile.connectdemo.utils.NotificationUtils.getChannel;

public class DemoApplication extends Application
{
	@Override
	public void onCreate()
	{
		super.onCreate();

		DBClient.startup(this);

		// Enabling logs for core sdk
		ICLogger.startup(this);
		ICLogger.setLogOptions(ICLogType.Debug, ICLogTarget.Console);

		// The IMIconnect Core should always be initialized within onCreate
		// This ensures the SDK is in the correct state where ever the app is started from (User launch / Background service etc)
		try
		{
			// Call startup api to initialize the IMIconnect SDK
			IMIconnect.startup(this);
		}
		catch (ICException e)
		{
			e.printStackTrace();
		}

		// For Android O & above, add this code to register default Notification channelID
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
		{
			registerNotificationChannel();
		}

		// Setup in-app notification
		ICInAppNotificationManager icInAppNotificationManager = ICInAppNotificationManager.getInstance(this);
		icInAppNotificationManager.registerViewFactory(new ICInAppBannerNotificationViewBinderFactory());
		icInAppNotificationManager.registerViewFactory(new ICInAppModalNotificationViewBinderFactory());
		icInAppNotificationManager.setEnabled(true);
	}

	@RequiresApi(api = Build.VERSION_CODES.O)
	private void registerNotificationChannel()
	{
		// Provide your channel name & description that can be seen by User.
		CharSequence name = getString(R.string.channel_name);
		String description = getString(R.string.channel_description);

		NotificationChannel notificationChannel = getChannel(this, ICConstants.DEFAULT_CHANNEL_ID, name.toString(), description);

		// Register the channel with the system
		NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		if (notificationManager != null)
		{
			notificationManager.createNotificationChannel(notificationChannel);
		}
	}
}
