/*******************************************************************************
 * LoginActivity.java
 * LoginActivity
 * <p>
 * Created by Ashish Das on 17/01/17.
 * Copyright © 2017 IMImobile. All rights reserved.
 ******************************************************************************/

package com.imimobile.connectdemo;

import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.imimobile.connect.core.ICDeviceProfile;
import com.imimobile.connect.core.IMIconnect;
import com.imimobile.connect.core.authentication.ICUserAuthentication;
import com.imimobile.connect.core.callbacks.ICRegistrationCallback;
import com.imimobile.connect.core.callbacks.ICUpdateProfileDataCallback;
import com.imimobile.connect.core.callbacks.ICUserAuthenticationCallback;
import com.imimobile.connect.core.enums.ICDeviceProfileParam;
import com.imimobile.connect.core.enums.ICErrorCode;
import com.imimobile.connect.core.exception.ICException;
import com.imimobile.connectdemo.utils.Utils;

import androidx.appcompat.widget.Toolbar;

/**
 * A login screen that offers login via user id.
 */
public class LoginActivity extends BaseAppCompatActivity
{
	private static final String TAG = "LoginActivity";

	private static final boolean WITHOUT_USER_AUTHENTICATION = true;

	private Context mContext;

	private EditText mInputPin;
	private EditText mInputCustomerID;

	private ICUserAuthentication mUserAuthentication;
	private String mStoredUserId;

	private boolean mRegenrateOptionVisible;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_login);
		mContext = LoginActivity.this;

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		if (toolbar != null)
		{
			toolbar.setTitleTextColor(Color.WHITE);
			setSupportActionBar(toolbar);
		}

		mInputPin = (EditText) findViewById(R.id.et_input_pin);
		mInputCustomerID = (EditText) findViewById(R.id.et_input_customer_id);

		findViewById(R.id.btn_register).setOnClickListener(mOnClickListener);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.activity_login_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu)
	{
		if (WITHOUT_USER_AUTHENTICATION)
		{
			menu.findItem(R.id.action_register).setVisible(false);
			menu.findItem(R.id.action_regenrate).setVisible(false);
		}
		else
		{
			menu.findItem(R.id.action_regenrate).setVisible(mRegenrateOptionVisible);
		}

		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
			case R.id.action_register:
				handleAction(true);
				break;
			case R.id.action_regenrate:
				handleAction(false);
				break;
		}
		return super.onOptionsItemSelected(item);
	}

	private void doRegistration(final ICDeviceProfile profile)
	{
		showProgressDialog("Please wait...");
		IMIconnect.register(profile, new ICRegistrationCallback()
		{
			@Override
			public void onRegistrationComplete(final Bundle bundle, final ICException exception)
			{
				dismissProgressDialog();
				if (exception != null)
				{
					showToast("Registration Failed - " + exception.getLocalizedMessage());

					showRegenrate(false);
					mInputCustomerID.setVisibility(View.VISIBLE);
					mInputPin.setVisibility(View.GONE);
				}
				else
				{
					updateUserSuccess(profile.getAppUserId());
				}
			}
		});
	}

	private void updateUserSuccess(String userId)
	{
		try
		{
			NotificationManager nMgr = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
			nMgr.cancelAll();
		}catch (Exception e)
		{
		}
		//Add this code for updating customerId parameter of the current device profile within the IMIconnect platform.
		IMIconnect.updateProfileData(ICDeviceProfileParam.CustomerId, userId, new ICUpdateProfileDataCallback()
		{
			@Override
			public void onUpdateComplete(final Bundle bundle, final ICException exception)
			{
				if (exception != null)
				{
					Log.e(TAG, "SetCustomerId failed exception Message:" +exception.getLocalizedMessage());

					Toast.makeText(LoginActivity.this, "SetCustomerId failed", Toast.LENGTH_SHORT).show();
					return;
				}
			}
		});
		Utils.storeLoggedUserId(mContext, IMIconnect.getDeviceProfile().getAppUserId());

		new Handler().postDelayed(new Runnable()
		{
			@Override
			public void run()
			{
				Intent intent = new Intent(mContext, HomeActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
						                | Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
				LoginActivity.this.finish();
			}
		}, 200);
	}

	private void handleAction(boolean isRegisterAction)
	{
		final String userId = getUserId();

		if (!isEmptyUserId(userId))
		{
			dismissProgressDialog();

			if (WITHOUT_USER_AUTHENTICATION)
			{
				if(!IMIconnect.isRegistered())
				{
					doRegistration(new ICDeviceProfile(ICDeviceProfile.getDefaultDeviceId(), userId));
				}
				else{
					IMIconnect.updateProfileData(ICDeviceProfileParam.UserId,userId, new ICUpdateProfileDataCallback()
					{
						@Override
						public void onUpdateComplete(final Bundle bundle, final ICException e)
						{
							if(e!=null){
								showToast("Update profile Data Failed - " + e.getLocalizedMessage());
							}
							else{
								updateUserSuccess(userId);
							}

						}
					});
				}
			}
			else
			{
				if (isRegisterAction)
				{
					if (mInputPin.isShown())
					{
						String pin = mInputPin.getText().toString().trim();
						if (TextUtils.isEmpty(pin))
						{
							showToast("Please enter pin");
						}
						else
						{
							doPinValidation(userId, pin);
						}
					}
					else
					{
						doPinGeneration(userId);
					}
				}
				else
				{
					doPinGeneration(userId);

					showRegenrate(false);
					mInputPin.setVisibility(View.GONE);
				}
			}
		}
	}

	private String getUserId()
	{
		return mInputCustomerID.getText().toString();
	}

	private boolean isEmptyUserId(String userId)
	{
		if (TextUtils.isEmpty(userId))
		{
			showToast(getResources().getString(R.string.user_id_required));
			return true;
		}
		return false;
	}

	private View.OnClickListener mOnClickListener = new View.OnClickListener()
	{
		@Override
		public void onClick(final View v)
		{
			switch (v.getId())
			{
				case R.id.btn_register:
				{
					handleAction(true);
				}
			}
		}
	};

	private void doPinGeneration(String userId)
	{
		if (!assertConnectionAvailability())
		{
			hideSoftInput(mInputCustomerID);

			try
			{
				getUserAuthentication(userId).generatePin(true);

				showProgressDialog("Waiting for OTP...", onCancelClickListener);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	private void doPinValidation(String userId, String pin)
	{
		if (!assertConnectionAvailability())
		{
			hideSoftInput(mInputCustomerID);
			hideSoftInput(mInputPin);

			try
			{
				getUserAuthentication(userId).validatePin(pin);
				showProgressDialog("Validating pin....", onCancelClickListener);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	private ICUserAuthenticationCallback mUserAuthenticationCallback = new ICUserAuthenticationCallback()
	{
		@Override
		public void onPinGenerated()
		{

		}

		@Override
		public void onPinValidated()
		{
			doRegistration(new ICDeviceProfile(ICDeviceProfile.getDefaultDeviceId(), mStoredUserId));
		}

		@Override
		public void onPinReceived()
		{
			//need to handle this API if we are listening SMS from application side instead of SDK end
		}

		@Override
		public void onError(final ICException exception)
		{
			Log.d(TAG, "Error Message : " + exception.getLocalizedMessage());

			dismissProgressDialog();

			//Handle OTP feature not required scenario
			if (exception != null && exception.getErrorCode() == ICErrorCode.FeatureNotSupported)
			{
				onPinValidated();
			}
			else
			{
				showToast(exception.getLocalizedMessage());
				showRegenrate(true);
			}
		}
	};

	private ICUserAuthentication getUserAuthentication(String userId)
	{
		if (mUserAuthentication == null
				|| TextUtils.isEmpty(mStoredUserId)
				|| userId.equals(mStoredUserId))
		{
			try
			{
				mUserAuthentication = new ICUserAuthentication(userId, mUserAuthenticationCallback);
				mStoredUserId = userId;
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}

		return mUserAuthentication;
	}

	private void showRegenrate(boolean visible)
	{
		mRegenrateOptionVisible = visible;
		invalidateOptionsMenu();
	}

	private DialogInterface.OnClickListener onCancelClickListener = new DialogInterface.OnClickListener()
	{
		@Override
		public void onClick(DialogInterface dialog, int which)
		{
			dismissProgressDialog();
			if (mUserAuthentication != null)
			{
				mUserAuthentication.stopListening();
			}
			mInputPin.setVisibility(View.VISIBLE);

			showRegenrate(true);
		}
	};

}

