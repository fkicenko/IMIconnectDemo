/*******************************************************************************
 * BaseAppCompatActivity.java
 * BaseAppCompatActivity
 * <p>
 * Created by Ashish Das on 17/01/17.
 * Copyright © 2017 IMImobile. All rights reserved.
 ******************************************************************************/

package com.imimobile.connectdemo;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Created by Ashish Das on 17/01/17.
 */
public class BaseAppCompatActivity extends AppCompatActivity
{
	private Context mBaseAppContext;
	private ProgressDialog mProgressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		mBaseAppContext = BaseAppCompatActivity.this;
	}

	public void showProgressDialog(String title)
	{
		showProgressDialog(title, null);
	}

	public void showProgressDialog(String title, DialogInterface.OnClickListener cancelListener)
	{
		dismissProgressDialog();
		try
		{
			mProgressDialog = new ProgressDialog(mBaseAppContext, R.style.DialogTheme);
			mProgressDialog.setMessage(title);
			mProgressDialog.setIndeterminate(true);
			mProgressDialog.setCancelable(false);

			if (cancelListener != null)
			{
				mProgressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, getResources().getString(R.string.button_title_cancel), cancelListener);
			}
			mProgressDialog.show();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void dismissProgressDialog()
	{
		if (mProgressDialog != null)
		{
			mProgressDialog.dismiss();
		}
		mProgressDialog = null;
	}

	public void showToast(String message)
	{
		Toast.makeText(mBaseAppContext, message, Toast.LENGTH_SHORT).show();
	}

	public boolean assertConnectionAvailability()
	{
		if (!isNetworkAvailable())
		{
			showToast("Please turn on your connectivity");
			return true;
		}
		return false;
	}

	private boolean isNetworkAvailable()
	{
		ConnectivityManager connectivityManager
				= (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}

	protected void hideSoftInput(EditText editText)
	{
		if (editText != null)
		{
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
		}
	}
}
