/*******************************************************************************
 * ICMessageRecyclerView.java
 * ICMessageRecyclerView
 * <p>
 * Created by Ashish Das on 17/01/17.
 * Copyright © 2017 IMImobile. All rights reserved.
 ******************************************************************************/

package com.imimobile.connectdemo.recyclerview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Ashish Das on 17/01/17.
 */
public class ICMessageRecyclerView extends RecyclerView
{
	public interface OnItemClickListener
	{
		void onItemClick(View view, int position);

		void onItemLongClick(View view, int position);
	}

	private OnItemClickListener mItemClickListener;

	public ICMessageRecyclerView(final Context context)
	{
		this(context, null, 0);
	}

	public ICMessageRecyclerView(final Context context, @Nullable final AttributeSet attrs)
	{
		this(context, attrs, 0);
	}

	public ICMessageRecyclerView(final Context context, @Nullable final AttributeSet attrs, final int defStyle)
	{
		super(context, attrs, defStyle);
		init();
	}

	public void setOnItemClickListener(final OnItemClickListener itemClickListener)
	{
		mItemClickListener = itemClickListener;
	}

	@Override
	public void addItemDecoration(final ItemDecoration decor)
	{
		return;
	}

	@Override
	public void addOnItemTouchListener(final OnItemTouchListener listener)
	{
		return;
	}

	private void init()
	{
		super.addOnItemTouchListener(new RecyclerViewItemTouchListener());
	}

	private class RecyclerViewItemTouchListener implements RecyclerView.OnItemTouchListener
	{
		private GestureDetector mGestureDetector;

		public RecyclerViewItemTouchListener()
		{
			mGestureDetector = new GestureDetector(getContext(), mOnGestureListener);
		}

		@Override
		public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e)
		{
			View child = rv.findChildViewUnder(e.getX(), e.getY());

			if (child != null && mItemClickListener != null && mGestureDetector.onTouchEvent(e))
			{
				mItemClickListener.onItemClick(child, getChildAdapterPosition(child));
			}
			return false;
		}

		@Override
		public void onTouchEvent(RecyclerView rv, MotionEvent e)
		{
		}

		@Override
		public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept)
		{

		}

		private GestureDetector.SimpleOnGestureListener mOnGestureListener = new GestureDetector.SimpleOnGestureListener()
		{
			@Override
			public boolean onSingleTapUp(MotionEvent e)
			{
				return true;
			}

			@Override
			public void onLongPress(MotionEvent e)
			{
				View child = findChildViewUnder(e.getX(), e.getY());

				if (child != null && mItemClickListener != null)
				{
					mItemClickListener.onItemLongClick(child, getChildAdapterPosition(child));
				}
			}
		};
	}

}
