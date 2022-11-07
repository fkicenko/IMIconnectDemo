/*******************************************************************************
 * NotificationAdapter.java
 * NotificationAdapter
 * <p>
 * Created by Ashish Das on 17/01/17.
 * Copyright © 2017 IMImobile. All rights reserved.
 ******************************************************************************/

package com.imimobile.connectdemo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.imimobile.connectdemo.R;
import com.imimobile.connectdemo.datas.NotificationData;
import com.imimobile.connectdemo.utils.Utils;

import java.text.DateFormat;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder>
{
	private final DateFormat mDateFormat;
	private final DateFormat mTimeFormat;

	private Context mContext;
	private List<NotificationData> mNotificationsCardList;

	public class MyViewHolder extends RecyclerView.ViewHolder
	{
		TextView title, time;

		public MyViewHolder(View view)
		{
			super(view);
			title = (TextView) view.findViewById(R.id.tv_title);
			time = (TextView) view.findViewById(R.id.tv_time);
		}
	}

	public NotificationAdapter(Context context)
	{
		mContext = context;
		mDateFormat = android.text.format.DateFormat.getDateFormat(mContext);
		mTimeFormat = android.text.format.DateFormat.getTimeFormat(mContext);
	}

	@Override
	public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
	{
		View itemView = LayoutInflater.from(parent.getContext())
		                              .inflate(R.layout.notification_item, parent, false);

		return new MyViewHolder(itemView);
	}

	@Override
	public void onBindViewHolder(MyViewHolder holder, int position)
	{
		NotificationData notificationsCard = mNotificationsCardList.get(position);
		holder.title.setText(notificationsCard.getPushmessage());
		holder.time.setText(Utils.formatTime(mContext, notificationsCard.getCreatedTime(), mTimeFormat, mDateFormat));
	}

	@Override
	public int getItemCount()
	{
		if (mNotificationsCardList == null)
		{
			return 0;
		}

		return mNotificationsCardList.size();
	}

	public void setData(List<NotificationData> cards)
	{
		this.mNotificationsCardList = cards;
		this.notifyDataSetChanged();
	}

}
