/*******************************************************************************
 * PushMessageEvent.java
 * PushMessageEvent
 * <p>
 * Created by Ashish Das on 17/01/17.
 * Copyright © 2017 IMImobile. All rights reserved.
 ******************************************************************************/

package com.imimobile.connectdemo.datas.events;

import com.imimobile.connectdemo.datas.roomdb.MessageEntity;

public class PushMessageEvent
{
	public MessageEntity getMessage()
	{
		return message;
	}

	public void setMessage(MessageEntity message)
	{
		this.message = message;
	}

	private MessageEntity message;

	public PushMessageEvent(MessageEntity message)
	{
		this.message = message;
	}

}
