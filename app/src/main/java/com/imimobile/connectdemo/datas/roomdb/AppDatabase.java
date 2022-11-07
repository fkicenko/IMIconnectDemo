/*******************************************************************************
 * MessageDatabase.java
 * MessageDatabase
 * <p>
 * Created by Ashish Das on 13/07/20.
 * Copyright © 2020 IMImobile. All rights reserved.
 ******************************************************************************/

package com.imimobile.connectdemo.datas.roomdb;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {MessageEntity.class}, version = 1, exportSchema = false)
@TypeConverters(DateConverter.class)
public abstract class AppDatabase extends RoomDatabase
{
	public abstract MessageDao messageDao();
}