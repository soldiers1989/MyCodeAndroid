/*
 * Copyright 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hxjr.p2p.ad5.widgets.calendar;

import java.util.Calendar;
import java.util.List;

import org.json.JSONObject;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.GridView;

import com.hxjr.p2p.ad5.bean.CalendarInfo;
import com.hxjr.p2p.ad5.ui.mine.RepaymentCalendarActivity.OnCalendarChangeListener;
import com.hxjr.p2p.ad5.ui.mine.RepaymentCalendarActivity.OnCellClickedListener;
import com.hxjr.p2p.ad5.utils.DMConstant;
import com.hxjr.p2p.ad5.utils.HttpParams;
import com.hxjr.p2p.ad5.utils.TimeUtils;
import com.hxjr.p2p.ad5.R;
import com.dm.http.HttpCallBack;
import com.dm.http.HttpUtil;
import com.dm.utils.DMLog;

public class CalendarFragment extends Fragment
{
	public static final String ARG_PAGE = "page";
	
	public static final String ARG_IS_MAIN_INIT = "isMainInit";
	
	private View rootView;
	
	private int mPageNumber;
	
	private Calendar mCalendar;
	
	private CalendarGridViewAdapter calendarGridViewAdapter;
	
	private OnCellClickedListener onCellClickedListener;
	
	private OnCalendarChangeListener onCalendarChangeListener;
	
	private CalendarInfo calendarInfo = null;
	
	private GridView calendarView;
	
	boolean isMainInit = false;
	
	List<MonthCellDescriptor> selectedDates;
	
	public static CalendarFragment create(int pageNumber, boolean isMainInit)
	{
		CalendarFragment fragment = new CalendarFragment();
		Bundle args = new Bundle();
		args.putInt(ARG_PAGE, pageNumber);
		args.putBoolean(ARG_IS_MAIN_INIT, isMainInit);
		fragment.setArguments(args);
		return fragment;
	}
	
	public CalendarFragment()
	{
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		mPageNumber = getArguments().getInt(ARG_PAGE);
		isMainInit = getArguments().getBoolean(ARG_IS_MAIN_INIT);
		mCalendar = CalendarUtils.getSelectCalendar(mPageNumber);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		// Inflate the layout containing a title and body text.
		rootView = (ViewGroup)inflater.inflate(R.layout.calendar_view, container, false);
		if(isMainInit)
		{
			new Handler().post(new Runnable()
			{
				
				@Override
				public void run()
				{
					// TODO Auto-generated method stub
					createView();
				}
			});
		}
		else
		{
			createView();
		}
		return rootView;
	}
	
	public void setSelectedDates(List<MonthCellDescriptor> selectedDates)
	{
		this.selectedDates = selectedDates;
	}
	
	public void setOnCellClickedListener(OnCellClickedListener onCellClickedListener)
	{
		this.onCellClickedListener = onCellClickedListener;
	}
	
	public void setOnCalendarChangeListener(OnCalendarChangeListener onCalendarChangeListener)
	{
		this.onCalendarChangeListener = onCalendarChangeListener;
	}
	
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser)
	{
		super.setUserVisibleHint(isVisibleToUser);
		if (isVisibleToUser)
		{
			initData();
		}
		
	}
	
	private void initData()
	{/*
		mCalendar = CalendarUtils.getSelectCalendar(mPageNumber);
//		DMLog.e("POSITION", "initData====" + mPageNumber + "---" + TimeUtils.stringToDateDetail(mCalendar.getTime()) + "---" + getUserVisibleHint());
		HttpParams httpParams = new HttpParams();
		mCalendar.set(Calendar.DAY_OF_MONTH, 1); // 设置日为当月1日
		httpParams.put("start", TimeUtils.stringToDateDetail(mCalendar.getTime()));// 开始时间
		mCalendar.set(Calendar.DAY_OF_MONTH, mCalendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		Calendar c = CalendarUtils.setNextViewItem(mCalendar);
		httpParams.put("end", TimeUtils.stringToDateDetail(c.getTime()));// 结束时间  下一个月1号
		HttpUtil.getInstance().post(DMConstant.API_URLS.FEED_CALENDAR, httpParams, new HttpCallBack()
		{
			
			@Override
			public void onSuccess(JSONObjectFetchResult result)
			{
				DMLog.e("result", result.toString());
				JSONObject jsonObj = (JSONObject)result.data;
				calendarInfo = AnalyDataUtil.getInstance().analyCalendarInfo(jsonObj.toString());
				if (calendarInfo != null)
				{
					mCalendar = CalendarUtils.getSelectCalendar(mPageNumber);
					calendarGridViewAdapter = new CalendarGridViewAdapter(getActivity(), mCalendar, calendarInfo, onCellClickedListener, isMainInit, selectedDates);
					initGridView(calendarView, calendarGridViewAdapter);
					onCalendarChangeListener.change(calendarInfo);
				}
				
				dimissLoadingDialog();
				
			}
			
			@Override
			public void onFailure(Throwable t, Context context)
			{
				super.onFailure(t, context);
				dimissLoadingDialog();
			}
			
			@Override
			public void onStart()
			{
				showLoadingDialog();
			}
			
			@Override
			public void onConnectFailure(DMException dmE)
			{
				// TODO Auto-generated method stub
				dimissLoadingDialog();
			}
			
		});
	*/}
	
	private void createView()
	{
		GridView titleGridView = (GridView)rootView.findViewById(R.id.gridview);
		TitleGridAdapter titleAdapter = new TitleGridAdapter(getActivity());
		initGridView(titleGridView, titleAdapter);
		calendarView = (GridView)rootView.findViewById(R.id.calendarView);
		mCalendar = CalendarUtils.getSelectCalendar(mPageNumber);
//		DMLog.e("POSITION", "createView====" + mPageNumber + "---" + TimeUtils.stringToDateDetail(mCalendar.getTime()) + "---" + getUserVisibleHint());
		calendarGridViewAdapter = new CalendarGridViewAdapter(getActivity(), mCalendar, calendarInfo, onCellClickedListener, isMainInit, selectedDates);
		initGridView(calendarView, calendarGridViewAdapter);
		onCalendarChangeListener.change(calendarInfo);
	}
	
	private void initGridView(GridView gridView, BaseAdapter adapter)
	{
		gridView = setGirdView(gridView);
		gridView.setAdapter(adapter);// 设置菜单Adapter
	}
	
	@SuppressWarnings("deprecation")
	private GridView setGirdView(GridView gridView)
	{
		gridView.setNumColumns(7);// 设置每行列数
		gridView.setGravity(Gravity.CENTER_VERTICAL);// 位置居中
		gridView.setVerticalSpacing(1);// 垂直间隔
		gridView.setHorizontalSpacing(1);// 水平间隔
		gridView.setBackgroundColor(getResources().getColor(R.color.calendar_background));
		
		WindowManager windowManager = getActivity().getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		int i = display.getWidth() / 7;
		int j = display.getWidth() - (i * 7);
		int x = j / 2;
		gridView.setPadding(x, 0, 0, 0);// 居中
		
		return gridView;
	}
}
