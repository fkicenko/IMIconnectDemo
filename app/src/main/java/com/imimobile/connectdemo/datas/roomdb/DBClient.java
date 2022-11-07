/*******************************************************************************
 * DBClient.java
 * DBClient
 * <p>
 * Created by Ashish Das on 13/07/20.
 * Copyright © 2020 IMImobile. All rights reserved.
 ******************************************************************************/

package com.imimobile.connectdemo.datas.roomdb;

import android.content.Context;
import android.util.Log;

import com.imimobile.connectdemo.datas.enums.MessageReadStatus;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

public class DBClient
{
	private static final String LOG_TAG = DBClient.class.getSimpleName();
	private static final Object LOCK = new Object();

	private static volatile DBClient sInstance;

	private AppDatabase mAppDatabase;

	public static DBClient startup(final Context context)
	{
		if (sInstance == null)
		{
			synchronized (LOCK)
			{
				Log.d(LOG_TAG, "Creating new DBClient instance");
				sInstance = new DBClient(context);
			}
		}
		Log.d(LOG_TAG, "Getting the DBClient instance");
		return sInstance;
	}

	private DBClient(final Context context)
	{
		mAppDatabase = Room.databaseBuilder(context.getApplicationContext(),
		                                    AppDatabase.class, DBConstants.DATABASE_NAME)
		                   .addCallback(sRoomDatabaseCallback)
		                   .build();
	}

	/**
	 * Override the onOpen method to populate the database.
	 */
	private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback()
	{
		@Override
		public void onOpen(@NonNull SupportSQLiteDatabase db)
		{
			super.onOpen(db);

		}
	};

	public static AppDatabase getAppDatabase()
	{
		return sInstance.mAppDatabase;
	}

	public static MessageDao getMessageDao()
	{
		return getAppDatabase().messageDao();
	}

	public static void insert(final MessageEntity messageEntity)
	{
		AppExecutors.getInstance().diskIO().execute(new Runnable()
		{
			@Override
			public void run()
			{
				getMessageDao().insert(messageEntity);
			}
		});
	}

	public static List<MessageEntity> getNotifications(String userId)
	{
		return getMessageDao().getMessages(userId, MessageEntity.CHANNEL_PUSH);
	}

	public static void deleteNotifications(String userId, String channel)
	{
		getMessageDao().deleteNotifications(userId, channel);
	}

	public static void deleteNotification(String userId, String channel, String messageId)
	{
		getMessageDao().deleteNotification(userId, channel, messageId);
	}

	public static void updateMessageStatusByIds(final String[] messageIds, final MessageReadStatus msgReadStatus)
	{
		AppExecutors.getInstance().diskIO().execute(new Runnable()
		{
			@Override
			public void run()
			{
				List<MessageEntity> messages = getMessageDao().getMessagesByIds(messageIds);
				for (MessageEntity messageEntity : messages)
				{
					messageEntity.messageReadStatus = msgReadStatus.getCode();
					getMessageDao().updateMessage(messageEntity);
				}
			}
		});
	}
}
