package com.hxjr.p2p.ad5.utils;

import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.support.v4.view.ViewPager;

/***
 * ViewPager轮播帮助类
 * @author  tangjian
 * @date 2015年11月19日
 */
public class CircleViewsHelper
{
	private ViewPager viewPagerBanner;
	
	private Timer timer;
	
	private TimerTask task;
	
	@SuppressLint("HandlerLeak") private Handler mHandler = new Handler()
	{
		public void handleMessage(android.os.Message msg)
		{
			viewPagerBanner.setCurrentItem(viewPagerBanner.getCurrentItem() + 1);
		};
	};
	
	public CircleViewsHelper(ViewPager viewPager)
	{
		viewPagerBanner = viewPager;
	}
	
	/**
	 * 启动定时器
	 */
	public void startTimer()
	{
		if (null == timer)
		{
			timer = new Timer();
		}
		if (null == task)
		{
			task = new TimerTask()
			{
				@Override
				public void run()
				{
					if (null == mHandler)
					{
						mHandler = new Handler();
					}
					mHandler.sendEmptyMessage(0);
				}
			};
		}
		timer.schedule(task, 3000, 3000);
	}
	
	/**
	 * 停止定时器
	 */
	public void stopTimer()
	{
		if (null != timer)
		{
			timer.cancel();
			timer = null;
		}
		if (null != task)
		{
			task.cancel();
			task = null;
		}
	}
}
