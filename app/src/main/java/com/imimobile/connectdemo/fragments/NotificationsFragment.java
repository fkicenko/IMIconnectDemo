/*******************************************************************************
 * NotificationsFragment.java
 * NotificationsFragment
 * <p>
 * Created by Ashish Das on 17/01/17.
 * Copyright © 2017 IMImobile. All rights reserved.
 ******************************************************************************/

package com.imimobile.connectdemo.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.imimobile.connect.core.callbacks.ICSetMessageStatusCallback;
import com.imimobile.connect.core.exception.ICException;
import com.imimobile.connect.core.messaging.ICMessaging;
import com.imimobile.connectdemo.R;
import com.imimobile.connectdemo.adapters.NotificationAdapter;
import com.imimobile.connectdemo.datas.NotificationData;
import com.imimobile.connectdemo.datas.enums.MessageReadStatus;
import com.imimobile.connectdemo.datas.events.PushMessageEvent;
import com.imimobile.connectdemo.datas.roomdb.AppExecutors;
import com.imimobile.connectdemo.datas.roomdb.DBClient;
import com.imimobile.connectdemo.datas.roomdb.MessageEntity;
import com.imimobile.connectdemo.recyclerview.ICMessageRecyclerView;
import com.imimobile.connectdemo.utils.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

public class NotificationsFragment extends Fragment
{
	private static final String TAG = "NotificationsFragment";
	private static final String ARG_PARAM1 = "param1";

	private ICMessageRecyclerView mRecyclerView;
	private NotificationAdapter mAdapter;
	private List<NotificationData> mMessageDatas = new ArrayList<>();

	/**
	 * Use this factory method to create a new instance of
	 * this fragment using the provided parameters.
	 *
	 * @return A new instance of fragment.
	 */
	public static NotificationsFragment newInstance(int position)
	{
		NotificationsFragment fragment = new NotificationsFragment();
		Bundle args = new Bundle();
		args.putInt(ARG_PARAM1, position);
		fragment.setArguments(args);
		return fragment;
	}

	public NotificationsFragment()
	{

	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.fragment_notification, container, false);
		mRecyclerView = (ICMessageRecyclerView) view.findViewById(R.id.rv_messages);
		mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
		mRecyclerView.setOnItemClickListener(mOnItemClickListener);

		mAdapter = new NotificationAdapter(getActivity());
		mRecyclerView.setAdapter(mAdapter);

		return view;
	}

	private ICMessageRecyclerView.OnItemClickListener mOnItemClickListener = new ICMessageRecyclerView.OnItemClickListener()
	{
		@Override
		public void onItemClick(final View view, final int position)
		{
			NotificationData notificationData = mMessageDatas.get(position);
			if (notificationData.getReadStatus() == MessageReadStatus.UNREAD)
			{
				setMessageAsRead(notificationData);
			}
			showMesage(notificationData.getPushmessage(), view.getContext());
		}

		@Override
		public void onItemLongClick(final View view, final int position)
		{
			NotificationData notificationData = mMessageDatas.get(position);
			if (notificationData != null
					&& !TextUtils.isEmpty(notificationData.getMessageId()))
			{
				showPopupMenu(view, notificationData);
			}
		}
	};

	private void showPopupMenu(View view, final NotificationData notificationData)
	{
		PopupMenu popup = new PopupMenu(view.getContext(), view);
		MenuInflater inflater = popup.getMenuInflater();
		inflater.inflate(R.menu.fragment_push_notification_menu, popup.getMenu());
		popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener()
		{
			@Override
			public boolean onMenuItemClick(final MenuItem item)
			{
				switch (item.getItemId())
				{
					case R.id.action_delete:
					{
						AppExecutors.getInstance().diskIO().execute(new Runnable()
						{
							@Override
							public void run()
							{
								DBClient.deleteNotification(Utils.getLoggedUserId(getActivity()),
								                            MessageEntity.CHANNEL_PUSH,
								                            notificationData.getMessageId());
								reloadData();
							}
						});
					}
					return true;
					default:
						return false;
				}
			}
		});
		popup.setGravity(Gravity.CENTER);
		popup.show();
	}

	private void setMessageAsRead(final NotificationData data)
	{
		if (data != null && !TextUtils.isEmpty(data.getMessageId()))
		{
			String[] transIds = new String[]{data.getMessageId()};
			ICMessaging client = ICMessaging.getInstance();
			if (client != null)
			{
				Log.d(TAG, "message read reciept sent channel push");

				client.setMessagesAsRead(transIds, new ICSetMessageStatusCallback()
				{
					@Override
					public void onSetMessageStatusComplete(final String[] messageTransactionIds, final ICException exception)
					{
						if (exception != null)
						{
							exception.printStackTrace();
						}
					}
				});

				DBClient.updateMessageStatusByIds(transIds, MessageReadStatus.READ);
			}
		}
	}

	@Override
	public void onDetach()
	{
		super.onDetach();
		setMenuVisibility(false);
	}

	@Subscribe(threadMode = ThreadMode.MAIN)
	public void onMessageEvent(PushMessageEvent event)
	{
		reloadData();
	}

	@Override
	public void onStart()
	{
		super.onStart();
		EventBus.getDefault().register(this);
	}

	@Override
	public void onStop()
	{
		super.onStop();
		EventBus.getDefault().unregister(this);
	}

	@Override
	public void onResume()
	{
		super.onResume();

		retrieveTasks();
	}

	private void retrieveTasks()
	{
		reloadData();
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
	{

		super.onCreateOptionsMenu(menu, inflater);
		menu.clear();
		inflater.inflate(R.menu.fragment_push_notification_menu, menu);
	}

	@Override
	public boolean onOptionsItemSelected(final MenuItem item)
	{
		int id = item.getItemId();
		if (id == R.id.action_delete)
		{
			deleteNotification(getActivity());
		}
		return super.onOptionsItemSelected(item);
	}

	private void reloadData()
	{
		AppExecutors.getInstance().diskIO().execute(new Runnable()
		{
			@Override
			public void run()
			{
				List<MessageEntity> dbUsers = DBClient.getNotifications(Utils.getLoggedUserId(getActivity()));
				mMessageDatas = new ArrayList<>();
				for (int i = 0; i < dbUsers.size(); i++)
				{
					MessageEntity message = dbUsers.get(i);

					NotificationData notificationData = new NotificationData();
					notificationData.setSenderId(MessageEntity.CHANNEL_PUSH);
					notificationData.setPushmessage(message.messageText);
					notificationData.setCreatedTime(message.createdTime);
					notificationData.setReadStatus(MessageReadStatus.get(message.messageReadStatus));
					notificationData.setMessageId(message.messageId);

					mMessageDatas.add(notificationData);
				}

				getActivity().runOnUiThread(new Runnable()
				{
					@Override
					public void run()
					{
						mAdapter.setData(mMessageDatas);
					}
				});
			}
		});
	}

	public void deleteNotification(final Context context)
	{
		AlertDialog.Builder alert = new AlertDialog.Builder(context, R.style.DialogTheme_Alert);
		alert.setMessage(getResources().getString(R.string.clear_all_messages));
		alert.setCancelable(true);
		alert.setPositiveButton(getResources().getString(R.string.button_title_cancel), new DialogInterface.OnClickListener()
		                        {
			                        @Override
			                        public void onClick(DialogInterface dialog, int which)
			                        {
				                        dialog.dismiss();

			                        }
		                        }
		);
		alert.setNegativeButton(getResources().getString(R.string.button_title_ok), new DialogInterface.OnClickListener()
		                        {
			                        @Override
			                        public void onClick(DialogInterface dialog, int which)
			                        {
				                        AppExecutors.getInstance().diskIO().execute(new Runnable()
				                        {
					                        @Override
					                        public void run()
					                        {
						                        DBClient.deleteNotifications(Utils.getLoggedUserId(getActivity()), MessageEntity.CHANNEL_PUSH);
						                        reloadData();
					                        }
				                        });
				                        dialog.dismiss();
			                        }
		                        }
		);
		alert.create();
		alert.show();
	}

	public void showMesage(String message, Context context)
	{
		AlertDialog.Builder alert = new AlertDialog.Builder(context, R.style.DialogTheme_Alert);
		alert.setMessage(message);
		alert.setCancelable(false);
		alert.setPositiveButton(getResources().getString(R.string.button_title_ok), new DialogInterface.OnClickListener()
		                        {
			                        @Override
			                        public void onClick(DialogInterface dialog, int which)
			                        {
				                        dialog.dismiss();

			                        }
		                        }
		);

		alert.create();
		alert.show();
		alert.setCancelable(true);
	}
}
