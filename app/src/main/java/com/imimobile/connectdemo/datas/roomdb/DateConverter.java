/*******************************************************************************
 * DateConverter.java
 * DateConverter
 * <p>
 * Created by Ashish Das on 13/07/20.
 * Copyright © 2020 IMImobile. All rights reserved.
 ******************************************************************************/

package com.imimobile.connectdemo.datas.roomdb;

import java.util.Date;

import androidx.room.TypeConverter;

public class DateConverter
{
	@TypeConverter
	public static Date toDate(Long timestamp) {
		return timestamp == null ? null : new Date(timestamp);
	}

	@TypeConverter
	public static Long toTimestamp(Date date) {
		return date == null ? null : date.getTime();
	}
}
