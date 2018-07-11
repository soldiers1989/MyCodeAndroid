package com.hxjr.p2p.ad5.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;

import com.dm.utils.AppManager;
import com.hxjr.p2p.ad5.DMApplication;
import com.hxjr.p2p.ad5.R;
import com.hxjr.p2p.ad5.utils.SharedPreferenceUtils;

import java.util.ArrayList;

/**
 * 欢迎、指引页面
 * @author  jiaohongyun
 * @date 2015年5月22日
 */
public class WelcomeActivity extends BaseActivity implements OnClickListener
{
	
	private ViewPager viewPager;
	
	private MyPagerAdapter myAdapter;
	
	private LayoutInflater mInflater;
	
	private ArrayList<View> views;
	
	private View layout1 = null;
	
	private View layout2 = null;
	
	private View layout3 = null;
	
	private ImageView point1;
	
	private ImageView point2;
	
	private ImageView point3;
	
	private ImageView start;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.welcome);
		initView();
	}
	
	/**
	 * 初始化视图
	 */
	@Override
	protected void initView()
	{
		findViewById(R.id.closeBtn).setOnClickListener(this);
//		point1 = (ImageView)findViewById(R.id.loading_point_1);
//		point2 = (ImageView)findViewById(R.id.loading_point_2);
//		point3 = (ImageView)findViewById(R.id.loading_point_3);
//
//		point1.setOnClickListener(this);
//		point2.setOnClickListener(this);
//		point3.setOnClickListener(this);
		
		views = new ArrayList<View>(2);
		myAdapter = new MyPagerAdapter(views);
		viewPager = (ViewPager)findViewById(R.id.welcome_viewpager);
		mInflater = getLayoutInflater();
		layout1 = mInflater.inflate(R.layout.welcome_item_first, null);
		layout2 = mInflater.inflate(R.layout.welcome_itme_second, null);
		layout3 = mInflater.inflate(R.layout.welcome_item_three, null);
		start = (ImageView)layout3.findViewById(R.id.start_btn);
		start.setOnClickListener(this);
		views.add(layout1);
		views.add(layout2);
		views.add(layout3);
		viewPager.setAdapter(myAdapter);
		//初始化当前显示的view
		viewPager.setCurrentItem(0);
		
		viewPager.setOnPageChangeListener(new OnPageChangeListener()
		{
			@Override
			public void onPageSelected(int arg0)
			{
//				if (arg0 == 0)
//				{
//					point1.setImageResource(R.drawable.page_btn_on);
//					point2.setImageResource(R.drawable.page_btn);
//					point3.setImageResource(R.drawable.page_btn);
//				}
//				else if (arg0 == 1)
//				{
//					point2.setImageResource(R.drawable.page_btn_on);
//					point1.setImageResource(R.drawable.page_btn);
//					point3.setImageResource(R.drawable.page_btn);
//				}
//				else if (arg0 == 2)
//				{
//					point3.setImageResource(R.drawable.page_btn_on);
//					point1.setImageResource(R.drawable.page_btn);
//					point2.setImageResource(R.drawable.page_btn);
//				}
//				else
//				{
//					point1.setImageResource(R.drawable.page_btn);
//					point2.setImageResource(R.drawable.page_btn);
//					point3.setImageResource(R.drawable.page_btn);
//				}
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2)
			{
			
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0)
			{
			
			}
		});
	}
	
	private class MyPagerAdapter extends PagerAdapter
	{
		// 界面列表
		private ArrayList<View> views;
		
		public MyPagerAdapter(ArrayList<View> views)
		{
			this.views = views;
		}
		
		/**
		 * 获得当前界面数
		 */
		@Override
		public int getCount()
		{
			if (views != null)
			{
				return views.size();
			}
			return 0;
		}
		
		/**
		 * 初始化position位置的界面
		 */
		@Override
		public Object instantiateItem(View view, int position)
		{
			((ViewPager)view).addView(views.get(position), 0);
			View temp = views.get(position);
			temp.setVisibility(View.VISIBLE);
			return temp;
		}
		
		/**
		 * 判断是否由对象生成界面
		 */
		@Override
		public boolean isViewFromObject(View view, Object arg1)
		{
			return (view == arg1);
		}
		
		/**
		 * 销毁position位置的界面
		 */
		@Override
		public void destroyItem(View view, int position, Object arg2)
		{
			((ViewPager)view).removeView(views.get(position));
		}
	}
	
	@Override
	public void onClick(View v)
	{
		if (!checkClick(v.getId()))
		{
			return;
		}
		switch (v.getId())
		{
//			case R.id.loading_point_1:
//				viewPager.setCurrentItem(0);
//				point1.setImageResource(R.drawable.page_btn_on);
//				point2.setImageResource(R.drawable.page_btn);
//				point3.setImageResource(R.drawable.page_btn);
//				break;
//			case R.id.loading_point_2:
//				viewPager.setCurrentItem(1);
//				point2.setImageResource(R.drawable.page_btn_on);
//				point1.setImageResource(R.drawable.page_btn);
//				point3.setImageResource(R.drawable.page_btn);
//				break;
//			case R.id.loading_point_3:
//				viewPager.setCurrentItem(2);
//				point3.setImageResource(R.drawable.page_btn_on);
//				point1.setImageResource(R.drawable.page_btn);
//				point2.setImageResource(R.drawable.page_btn);
//				break;
			case R.id.start_btn:
				gotoMainPage();
				break;
			case R.id.closeBtn:
				gotoMainPage();
				break;
			default:
				break;
		}
	}
	
	/**
	 * 跳转到首页
	 */
	private void gotoMainPage()
	{
		SharedPreferenceUtils.put(this, SharedPreferenceUtils.KEY_IS_FIRST_RUN, false);
		DMApplication.getInstance().isFirstRun = false;
		//跳转到首页
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
		finish();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
			//退出应用
			AppManager.getAppManager().AppExit(this);
		}
		return super.onKeyDown(keyCode, event);
	}
	
}
