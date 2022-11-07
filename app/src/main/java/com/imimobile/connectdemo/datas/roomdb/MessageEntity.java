/*******************************************************************************
 * DBConstants.java
 * DBConstants
 * <p>
 * Created by Ashish Das on 13/07/20.
 * Copyright © 2020 IMImobile. All rights reserved.
 ******************************************************************************/

package com.imimobile.connectdemo.datas.roomdb;

import com.imimobile.connect.core.enums.ICMessageChannel;
import com.imimobile.connect.core.messaging.ICMessage;
import com.imimobile.connectdemo.datas.enums.MessageReadStatus;

import java.util.Date;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = DBConstants.TABLE_NAME_MESSAGE)
public class MessageEntity
{
	public static final String CHANNEL_PUSH = "push";
	public static final String CHANNEL_REALTIME = "realtime";

	@PrimaryKey(autoGenerate = true)
	public int id;

	@ColumnInfo(name = DBConstants.COLUMN_MESSAGE_MESSAGE_ID)
	public String messageId;

	@ColumnInfo(name = DBConstants.COLUMN_MESSAGE_MESSAGE_TEXT)
	public String messageText;

	@ColumnInfo(name = DBConstants.COLUMN_MESSAGE_CREATED_TIME)
	public Date createdTime;

	@ColumnInfo(name = DBConstants.COLUMN_MESSAGE_CHANNEL)
	public String channel;

	@ColumnInfo(name = DBConstants.COLUMN_MESSAGE_USER_ID)
	public String userId;

	@ColumnInfo(name = DBConstants.COLUMN_MESSAGE_READ_STATUS)
	public int messageReadStatus;

	public MessageEntity()
	{
	}

	public MessageEntity(String userId, ICMessage message, MessageReadStatus readStatus)
	{
		this.userId = userId;
		this.messageText = message.getMessage();

		if (message.getChannel() == ICMessageChannel.Push)
		{
			this.channel = CHANNEL_PUSH;
		}
		else
		{
			this.channel = CHANNEL_REALTIME;
		}

		this.messageId = message.getTransactionId();
		this.createdTime = new Date();

		this.messageReadStatus = readStatus.getCode();
	}
}
