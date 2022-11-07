/*******************************************************************************
 * Utils.java
 * Utils
 * <p>
 * Created by Ashish Das on 17/01/17.
 * Copyright © 2017 IMImobile. All rights reserved.
 ******************************************************************************/

package com.imimobile.connectdemo.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.imimobile.connectdemo.R;
import com.imimobile.connectdemo.constants.Constants;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Utils
{
	private static final int TIME_HOURS_24 = 24 * 60 * 60 * 1000;

	public static String getLoggedUserId(Context context)
	{
		return getFromStore(Constants.LOGGED_USER_ID, context);
	}

	public static String getFromStore(String key, Context context)
	{
		SharedPreferences pref = context.getSharedPreferences(context.getPackageName(), 0);
		String res = pref.getString(key, "");
		return res;
	}

	public static void storeLoggedUserId(Context context, String userId)
	{
		addToStore(Constants.LOGGED_USER_ID, userId, context);
	}

	public static void addToStore(String key, String value, Context context)
	{
		SharedPreferences pref = context.getSharedPreferences(context.getPackageName(), 0);
		SharedPreferences.Editor editor = pref.edit();
		editor.putString(key, value);
		editor.commit();
	}

	public static String getCSVForInClause(List<String> strings)
	{
		String result = "(";

		for (int i = 0; i < strings.size(); i++)
		{
			result += "'" + (strings.get(i)) + "'";
			if (i != (strings.size() - 1))
			{
				result += ",";
			}

		}
		result += ")";
		return result;
	}

	public static String formatTime(Context context, Date date, DateFormat timeFormat, DateFormat dateFormat)
	{
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		long todayMidnight = cal.getTimeInMillis();
		long yesterMidnight = todayMidnight - TIME_HOURS_24;
		long weekAgoMidnight = todayMidnight - TIME_HOURS_24 * 7;

		String timeText;
		if (date.getTime() > todayMidnight)
		{
			return timeFormat.format(date.getTime());
		}
		else if (date.getTime() > yesterMidnight)
		{
			timeText = context.getString(R.string.app_time_yesterday);
		}
		else if (date.getTime() > weekAgoMidnight)
		{
			cal.setTime(date);
			timeText = context.getResources().getStringArray(R.array.app_time_days_of_week)[cal.get(Calendar.DAY_OF_WEEK) - 1];
		}
		else
		{
			timeText = dateFormat.format(date);
		}
		return dateFormat.format(date);
	}
}
