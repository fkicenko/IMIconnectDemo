/*******************************************************************************
 * NotificationData.java
 * NotificationData
 * <p>
 * Created by Ashish Das on 17/01/17.
 * Copyright © 2017 IMImobile. All rights reserved.
 ******************************************************************************/

package com.imimobile.connectdemo.datas;

import com.imimobile.connectdemo.datas.enums.MessageReadStatus;

import java.util.Date;

public class NotificationData
{
	private String mSenderId;
	private String mMessageId;
	private String mPushmessage;
	private Date mCreatedTime;
	private MessageReadStatus mReadStatus;

	public String getSenderId()
	{
		return mSenderId;
	}

	public void setSenderId(final String senderId)
	{
		mSenderId = senderId;
	}

	public String getMessageId()
	{
		return mMessageId;
	}

	public void setMessageId(final String messageId)
	{
		mMessageId = messageId;
	}

	public String getPushmessage()
	{
		return mPushmessage;
	}

	public void setPushmessage(final String pushmessage)
	{
		mPushmessage = pushmessage;
	}

	public Date getCreatedTime()
	{
		return mCreatedTime;
	}

	public void setCreatedTime(final Date createdTime)
	{
		mCreatedTime = createdTime;
	}

	public MessageReadStatus getReadStatus()
	{
		return mReadStatus;
	}

	public void setReadStatus(final MessageReadStatus readStatus)
	{
		mReadStatus = readStatus;
	}

	@Override
	public String toString()
	{
		return "NotificationData{" +
				"mSenderId='" + mSenderId + '\'' +
				", mMessageId='" + mMessageId + '\'' +
				", mPushmessage='" + mPushmessage + '\'' +
				", mCreatedTime=" + mCreatedTime +
				", mReadStatus=" + mReadStatus +
				'}';
	}
}


