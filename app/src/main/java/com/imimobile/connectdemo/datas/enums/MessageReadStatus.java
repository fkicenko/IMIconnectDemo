/*******************************************************************************
 * MessageReadStatus.java
 * MessageReadStatus
 * <p>
 * Created by Ashish Das on 17/01/17.
 * Copyright © 2017 IMImobile. All rights reserved.
 ******************************************************************************/

package com.imimobile.connectdemo.datas.enums;

public enum MessageReadStatus
{
	UNREAD(0),
	READ(1);

	private int mCode;

	MessageReadStatus(final int code)
	{
		this.mCode = code;
	}

	public int getCode()
	{
		return mCode;
	}

	public static MessageReadStatus get(final int code)
	{
		if (code == MessageReadStatus.UNREAD.getCode())
		{
			return MessageReadStatus.UNREAD;
		}
		else if (code == MessageReadStatus.READ.getCode())
		{
			return MessageReadStatus.READ;
		}
		return null;
	}

	@Override
	public String toString()
	{
		return "MessageReadStatus = " + mCode;
	}
}
