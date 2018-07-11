package com.dm.widgets;

import java.lang.reflect.Field;

import com.hxjr.p2p.ad5.R;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;

/**
 * 下拉刷新容器控件
 * @author  jiaohongyun
 * @date 2015年10月20日
 */
public class DMSwipeRefreshLayout extends SwipeRefreshLayout
{
	
	public DMSwipeRefreshLayout(Context context)
	{
		super(context);
		init();
	}
	
	public DMSwipeRefreshLayout(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		init();
	}
	
	/**
	 * 一些初始化设置
	 */
	private void init()
	{
		this.setColorSchemeResources(R.color.swipe_color1, R.color.swipe_color2, R.color.swipe_color3, R.color.swipe_color4);
		Field f = null;
		try
		{
			f = SwipeRefreshLayout.class.getDeclaredField("mTouchSlop");
			f.setAccessible(true);
			f.set(this, 150);
		}
		catch (NoSuchFieldException e)
		{
			e.printStackTrace();
		}
		catch (IllegalAccessException e)
		{
			e.printStackTrace();
		}
		catch (IllegalArgumentException e)
		{
			e.printStackTrace();
		}
	}
}
