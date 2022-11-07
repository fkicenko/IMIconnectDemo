/*******************************************************************************
 * HomeActivity.java
 * HomeActivity
 * <p>
 * Created by Ashish Das on 17/01/17.
 * Copyright © 2017 IMImobile. All rights reserved.
 ******************************************************************************/

package com.imimobile.connectdemo;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.imimobile.connect.core.IMIconnect;
import com.imimobile.connect.core.callbacks.ICRegistrationCallback;
import com.imimobile.connect.core.callbacks.ICUpdateProfileDataCallback;
import com.imimobile.connect.core.enums.ICDeviceProfileParam;
import com.imimobile.connect.core.enums.ICMessageSynchronizationMode;
import com.imimobile.connect.core.exception.ICException;
import com.imimobile.connect.core.messaging.ICMessageSynchronizationPolicy;
import com.imimobile.connect.core.messaging.ICMessageSynchronizer;
import com.imimobile.connect.core.messaging.ICMessaging;
import com.imimobile.connect.core.messaging.store.ICDefaultMessageStore;
import com.imimobile.connect.ui.ICUIConfig;
import com.imimobile.connect.ui.IMIconnectUI;
import com.imimobile.connect.ui.conversation.ICConversationActivity;
import com.imimobile.connect.ui.conversation.ICConversationCategory;
import com.imimobile.connect.ui.data.ICCustomerDetails;
import com.imimobile.connect.ui.interfaces.ICUIStartupCallback;

import com.imimobile.connectdemo.constants.Constants;
import com.imimobile.connectdemo.fragments.NotificationsFragment;
import com.imimobile.connectdemo.utils.Utils;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.List;


public class HomeActivity extends BaseAppCompatActivity
		implements NavigationView.OnNavigationItemSelectedListener
{
	private Context mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_home);

		mContext = HomeActivity.this;

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
				this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
		drawer.setDrawerListener(toggle);
		toggle.syncState();

		IMIconnectUI.startup(this, new ICUIConfig("com.imimobile.connectdemo.utils.FileProvider"), new ICUIStartupCallback()
		{
			@Override
			public void onStartupComplete(final ICException e)
			{
				if (e != null)
				{
					Log.e("IMIconnectUI", "Failed to initialize UI SDK", e);
				}
			}
		});
		ICMessaging.getInstance().setMessageStore(new ICDefaultMessageStore(this));
		ArrayList<ICConversationCategory> categories = new ArrayList<>();
		Uri imageUri; //Assign a Uri instance to a local image resource

		categories.add(new ICConversationCategory("Live Chat", null)); //imageUri));
		categories.add(new ICConversationCategory("Technical Support", null));
		ICCustomerDetails customerDetails = ICCustomerDetails.builder().setFirstName("John").setLastName("Doe").build();
		Bundle bundle = new Bundle();
		ICConversationActivity.start(this, categories, customerDetails, bundle);

		ICMessageSynchronizationPolicy policy = new ICMessageSynchronizationPolicy();
		policy.setMode(ICMessageSynchronizationMode.Full);
		ICMessageSynchronizer.setPolicy(policy);

		NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
		navigationView.setNavigationItemSelectedListener(this);
		showUserDetails(navigationView);

		getSupportFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).replace(R.id.content_home,
		                                                                                                                NotificationsFragment.newInstance(1)).commit();

		handleDeepLinkAction();
	}

	@Override
	public void onBackPressed()
	{
		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		if (drawer.isDrawerOpen(GravityCompat.START))
		{
			drawer.closeDrawer(GravityCompat.START);
		}
		else
		{
			super.onBackPressed();
		}
	}

	@SuppressWarnings("StatementWithEmptyBody")
	@Override
	public boolean onNavigationItemSelected(MenuItem item)
	{
		// Handle navigation view item clicks here.
		int id = item.getItemId();

		if (id == R.id.nav_logout)
		{
			showConfirmationAlert(true);
		}
		else if (id == R.id.nav_remove_profile)
		{
			showConfirmationAlert(false);
		}

		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		drawer.closeDrawer(GravityCompat.START);
		return true;
	}

	private void showUserDetails(NavigationView navigationView)
	{
		try
		{
			View headerView = navigationView.getHeaderView(0);
			((TextView) headerView.findViewById(R.id.tv_userid)).setText(Utils.getLoggedUserId(this));
			((TextView) headerView.findViewById(R.id.tv_username)).setText("IMIconnect");
			((TextView) headerView.findViewById(R.id.tv_useremail)).setText("https://imimobile.com");
		}
		catch (Exception e)
		{
		}
	}


	private void handleDeepLinkAction()
	{
		// Parse the deep link
		String deepLink = DeepLinkActivity.getDeepLink(getIntent());

		if (Constants.DEEPLINK_ACTION_OPEN_NAVBAR.equals(deepLink))
		{
			DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
			drawer.openDrawer(GravityCompat.START);
		}
		else if (Constants.DEEPLINK_ACTION_LOGOUT.equals(deepLink))
		{
			doLogout();
		}
		else if (Constants.DEEPLINK_ACTION_REMOVEPROFILE.equals(deepLink))
		{
			removeProfile();
		}
	}

	private void showConfirmationAlert(final boolean isLogoutAction)
	{
		String title;
		String message;

		Resources resources = mContext.getResources();
		if (isLogoutAction)
		{
			title = resources.getString(R.string.menu_title_logout);
			message = resources.getString(R.string.logout_confirmation_message);
		}
		else
		{
			title = resources.getString(R.string.menu_title_remove_profile);
			message = resources.getString(R.string.remove_profile_confirmation_message);
		}


		new AlertDialog.Builder(mContext, R.style.DialogTheme_Alert)
				.setTitle(title)
				.setMessage(message)
				.setPositiveButton(
						getResources().getString(R.string.button_title_no), new DialogInterface.OnClickListener()
						{
							@Override
							public void onClick(final DialogInterface dialog, final int which)
							{

							}
						})
				.setNegativeButton(
						getResources().getString(R.string.button_title_yes), new DialogInterface.OnClickListener()
						{
							@Override
							public void onClick(final DialogInterface dialog, final int which)
							{
								if (isLogoutAction)
								{
									doLogout();
								}
								else
								{
									removeProfile();
								}
							}
						}).show();
	}

	private void doLogout()
	{
		if (!assertConnectionAvailability())
		{
			showProgressDialog("Logging out");

			IMIconnect.removeProfileData(ICDeviceProfileParam.UserId, new ICUpdateProfileDataCallback()
			{
				@Override
				public void onUpdateComplete(final Bundle bundle, final ICException e)
				{
					startLaunchActivity();
				}
			});
		}
	}

	private void removeProfile()
	{
		if (!assertConnectionAvailability())
		{
			showProgressDialog("Removing profile");

			IMIconnect.unregister(new ICRegistrationCallback()
			{
				@Override
				public void onRegistrationComplete(final Bundle bundle, final ICException e)
				{
					startLaunchActivity();
				}
			});
		}
	}

	private void startLaunchActivity()
	{
		Utils.storeLoggedUserId(mContext, "");
		dismissProgressDialog();

		Intent intent = new Intent(mContext, LaunchActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
				                | Intent.FLAG_ACTIVITY_NEW_TASK);

		startActivity(intent);

		finish();
	}
}