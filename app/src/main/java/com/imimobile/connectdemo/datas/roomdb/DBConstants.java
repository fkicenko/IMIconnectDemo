/*******************************************************************************
 * DBConstants.java
 * DBConstants
 * <p>
 * Created by Ashish Das on 13/07/20.
 * Copyright © 2020 IMImobile. All rights reserved.
 ******************************************************************************/

package com.imimobile.connectdemo.datas.roomdb;

public class DBConstants
{
	public static final String DATABASE_NAME = "app_db";

	/*Table names START*/
	public static final String TABLE_NAME_MESSAGE = "messages_tbl";
	/*Table names END*/

	/*MESSAGE Table columns START*/
	public static final String COLUMN_MESSAGE_MESSAGE_ID = "message_id";
	public static final String COLUMN_MESSAGE_MESSAGE_TEXT = "message_text";
	public static final String COLUMN_MESSAGE_USER_ID = "userId";
	public static final String COLUMN_MESSAGE_CREATED_TIME = "created_time";
	public static final String COLUMN_MESSAGE_CHANNEL = "channel";
	public static final String COLUMN_MESSAGE_READ_STATUS = "read_status";
	/*MESSAGE Table columns END*/
}
