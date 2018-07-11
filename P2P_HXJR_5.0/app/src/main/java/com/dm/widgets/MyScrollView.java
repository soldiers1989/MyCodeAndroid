package com.dm.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

/*
 * ScrollView的滚动Y值进行监听
 */
public class MyScrollView extends ScrollView
{
	private ScrollListener listener = null;
	
	public MyScrollView(Context context)
	{
		super(context);
	}
	
	public MyScrollView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}
	
	public MyScrollView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
	}
	
	public void setScrollListener(ScrollListener listener)
	{
		this.listener = listener;
	}
	
	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt)
	{
		super.onScrollChanged(l, t, oldl, oldt);
		if (listener != null)
		{
			listener.scrollChanged(getScrollX(), getScrollY());
		}
	}
	
	public interface ScrollListener
	{
		void scrollChanged(int x, int y);
	}
	
	private boolean isFirstTouch = true;
	
	private int clickY = -1;
	
	/**
	 * 计算第一次点击时可点击高度，解决标的信息， 投资记录， 还款计划三个tab第一次点击失效的问题
	 * @return
	 */
	private int getClickY()
	{
		int count = getChildCount();
		if (count > 0)
		{//这里面的代码逻辑需要根据不同的布局做相应的处理
			View firstLevelView = getChildAt(0);
			if (firstLevelView instanceof ViewGroup)
			{
				int secondLevelChildCount = ((ViewGroup)firstLevelView).getChildCount();
				if (secondLevelChildCount > 0)
				{//解决标的信息， 投资记录， 还款计划三个tab第一次点击失效的问题
					return ((ViewGroup)firstLevelView).getChildAt(0).getMeasuredHeight();
				}
				else
				{
					return -1;
				}
			}
			else
			{
				return -1;
			}
		}
		else
		{
			return -1;
		}
	}
	
	private float mx;
	
	private float my;
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev)
	{
		float dx = ev.getX();
		float dy = ev.getY();
		float tx = Math.abs(mx - dx);
		float ty = Math.abs(my - dy);
		switch (ev.getAction())
		{
			case MotionEvent.ACTION_MOVE://在移动的过程中也拦截事件
			{
				if (mx == 0 || my == 0)
				{
					mx = dx;
					my = dy;
					return super.onInterceptTouchEvent(ev);
				}
				if (tx > 50 || ty > 50)
				{
					mx = dx;
					my = dy;
					return true;
				}
				else
				{
					mx = dx;
					my = dy;
					return super.onInterceptTouchEvent(ev);
				}
			}
			case MotionEvent.ACTION_UP:
			{
				mx = 0;
				my = 0;
				return super.onInterceptTouchEvent(ev);
			}
		}
		
		if (isFirstTouch)
		{//第一次按下的时候就拦截事件，不让事件传递到子控件里面。
			isFirstTouch = false;
			if (clickY == -1)
			{
				clickY = getClickY();
			}
			if (clickY >= ev.getY())
			{//解决标的信息， 投资记录， 还款计划三个tab第一次点击失效的问题
				return super.onInterceptTouchEvent(ev);
			}
			return true;
		}
		return super.onInterceptTouchEvent(ev);
	}
}