/*******************************************************************************
 * MessageDao.java
 * MessageDao
 * <p>
 * Created by Ashish Das on 13/07/20.
 * Copyright © 2020 IMImobile. All rights reserved.
 ******************************************************************************/

package com.imimobile.connectdemo.datas.roomdb;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.room.Update;

@Dao
public interface MessageDao
{
	@Insert(onConflict = OnConflictStrategy.IGNORE)
	void insert(MessageEntity message);

	@Update
	void updateMessage(MessageEntity message);

	@Delete
	void delete(MessageEntity message);

	@Query("DELETE FROM " + DBConstants.TABLE_NAME_MESSAGE)
	void deleteAll();

	@Query("SELECT * from " + DBConstants.TABLE_NAME_MESSAGE + " WHERE " + DBConstants.COLUMN_MESSAGE_USER_ID + " IN(:userId) AND "
			+ DBConstants.COLUMN_MESSAGE_CHANNEL + " IN(:channel)" + " ORDER BY " + DBConstants.COLUMN_MESSAGE_CREATED_TIME + " DESC")
	List<MessageEntity> getMessages(String userId, String channel);

	@Query("DELETE FROM " + DBConstants.TABLE_NAME_MESSAGE + " WHERE " + DBConstants.COLUMN_MESSAGE_USER_ID + " IN(:userId) AND "
			+ DBConstants.COLUMN_MESSAGE_CHANNEL + " IN(:channel)")
	void deleteNotifications(String userId, String channel);

	@Query("DELETE FROM " + DBConstants.TABLE_NAME_MESSAGE + " WHERE " + DBConstants.COLUMN_MESSAGE_USER_ID + " IN(:userId) AND "
			+ DBConstants.COLUMN_MESSAGE_CHANNEL + " IN(:channel) AND " + DBConstants.COLUMN_MESSAGE_MESSAGE_ID + " IN(:messageId)")
	void deleteNotification(String userId, String channel, String messageId);

	@Query("SELECT * from " + DBConstants.TABLE_NAME_MESSAGE + " WHERE " + DBConstants.COLUMN_MESSAGE_MESSAGE_ID + " IN(:msgIds)")
	List<MessageEntity> getMessagesByIds(String... msgIds);
}
