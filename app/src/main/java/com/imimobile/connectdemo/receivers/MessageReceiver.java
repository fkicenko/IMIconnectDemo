/*******************************************************************************
 * MessageReceiver.java
 * MessageReceiver
 * <p>
 * Created by Ashish Das on 17/01/17.
 * Copyright © 2017 IMImobile. All rights reserved.
 ******************************************************************************/

package com.imimobile.connectdemo.receivers;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.imimobile.connect.core.IMIconnect;
import com.imimobile.connect.core.enums.ICConnectionStatus;
import com.imimobile.connect.core.enums.ICMessageChannel;
import com.imimobile.connect.core.exception.ICException;
import com.imimobile.connect.core.messaging.ICMessage;
import com.imimobile.connect.core.messaging.ICMessagingReceiver;
import com.imimobile.connectdemo.HomeActivity;
import com.imimobile.connectdemo.constants.Constants;
import com.imimobile.connectdemo.datas.enums.MessageReadStatus;
import com.imimobile.connectdemo.datas.events.PushMessageEvent;
import com.imimobile.connectdemo.datas.roomdb.AppDatabase;
import com.imimobile.connectdemo.datas.roomdb.DBClient;
import com.imimobile.connectdemo.datas.roomdb.MessageEntity;
import com.imimobile.connectdemo.utils.Utils;

import org.greenrobot.eventbus.EventBus;

public class MessageReceiver extends ICMessagingReceiver
{
	private static final String TAG = "MessageReceiver";

	@Override
	protected void onMessageReceived(final Context context, final ICMessage icMessage)
	{
		String message = icMessage.getMessage();
		String msgtid = icMessage.getTransactionId();
		Log.d(TAG, "onMessageArrived msgtid =" + msgtid + "message =" + message);
		if (icMessage.getChannel() == ICMessageChannel.Push)
		{
			handlePushMessage(icMessage, context);
		}

		handleCustomTagAction(context, icMessage);
	}

	@Override
	protected void onConnectionStatusChanged(final Context context, final ICConnectionStatus state, final ICException e)
	{
		super.onConnectionStatusChanged(context, state, e);
		// only for RTM Connecting status
		Log.d(TAG, "onConnectionChange for RT state = " + state.toString());
	}

	public void handlePushMessage(ICMessage icMessage, Context context)
	{
		try
		{
			MessageEntity newInboxMsg = new MessageEntity(Utils.getLoggedUserId(context), icMessage, MessageReadStatus.UNREAD);
			DBClient.insert(newInboxMsg);

			EventBus.getDefault().post(new PushMessageEvent(newInboxMsg));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void handleCustomTagAction(Context context, ICMessage message)
	{
		if (message != null && message.getCustomTags() != null)
		{
			if (IMIconnect.isRegistered()
					|| !TextUtils.isEmpty(Utils.getLoggedUserId(context)))
			{

				Bundle bundle = message.getCustomTags();

				boolean isLogoutAction = false;
				boolean isRemoveprofileAction = false;
				try
				{
					isLogoutAction = "1".equals(bundle.getString("logout"));
				}
				catch (Exception e)
				{
				}
				try
				{
					isRemoveprofileAction = "1".equals(bundle.getString("removeprofile"));
				}
				catch (Exception e)
				{

				}
				if (isLogoutAction || isRemoveprofileAction)
				{
					try
					{
						Intent intent = new Intent(context, HomeActivity.class);

						intent.setData(Uri.parse("imi://command" + (isLogoutAction ? Constants.DEEPLINK_ACTION_LOGOUT : Constants.DEEPLINK_ACTION_REMOVEPROFILE)));

						context.startActivity(intent);
					}
					catch (Exception e)
					{

					}
				}
			}
		}
	}
}