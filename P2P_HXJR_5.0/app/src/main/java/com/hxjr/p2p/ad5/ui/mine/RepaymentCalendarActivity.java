package com.hxjr.p2p.ad5.ui.mine;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hxjr.p2p.ad5.bean.CalendarInfo;
import com.hxjr.p2p.ad5.bean.CalendarInfoDetail;
import com.hxjr.p2p.ad5.ui.BaseActivity;
import com.hxjr.p2p.ad5.utils.AmountUtil;
import com.hxjr.p2p.ad5.utils.TimeUtils;
import com.hxjr.p2p.ad5.widgets.calendar.CalendarFragment;
import com.hxjr.p2p.ad5.widgets.calendar.CalendarUtils;
import com.hxjr.p2p.ad5.widgets.calendar.CustomExpandableListView;
import com.hxjr.p2p.ad5.widgets.calendar.LazyViewPager;
import com.hxjr.p2p.ad5.widgets.calendar.MonthCellDescriptor;
import com.hxjr.p2p.ad5.widgets.calendar.NData;
import com.hxjr.p2p.ad5.widgets.calendar.WrapContentHeightViewPager;
import com.hxjr.p2p.ad5.R;
import com.dm.utils.DMLog;

/**
 * 回款日历
 * 
 * @author  sushuo
 * @date 2015年8月7日
 */
public class RepaymentCalendarActivity extends BaseActivity implements OnClickListener
{
	
	private WrapContentHeightViewPager viewPager;
	
	private LinearLayout calendar_title_ll;
	
	private TextView tvMonth;
	
	private ImageView left_img;
	
	private ImageView right_img;
	
	private LinearLayout none_ll;
	
	private LinearLayout day_amount_ll;
	
	private TextView none_day_tv;
	
	private TextView day_tv;
	
	private TextView day_amount_tv;
	
	private TextView month_tv;
	
	private TextView calendar_total_amount_tv;
	
	private LinearLayout total_amount_ll;
	
	private CustomExpandableListView expandListView;
	
	private LinearLayout repayment_day_tip_ll;
	
	private String month;
	
	private int currentPosition = 500;
	
	private Map<Integer, CalendarInfo> map = new HashMap<Integer, CalendarInfo>();
	
	private boolean isMainInit = false;// 是否初始化
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.repayment_calendar);
		initView();
		isMainInit = true;
	}
	
	@Override
	protected void initView()
	{
		super.initView();
		((TextView)findViewById(R.id.title_text)).setText(R.string.repayment_plan);// 设置标题
		calendar_title_ll = (LinearLayout)findViewById(R.id.calendar_title_ll);
		none_ll = (LinearLayout)findViewById(R.id.none_ll);
		day_amount_ll = (LinearLayout)findViewById(R.id.day_amount_ll);
		none_day_tv = (TextView)findViewById(R.id.none_day_tv);
		day_tv = (TextView)findViewById(R.id.day_tv);
		day_amount_tv = (TextView)findViewById(R.id.day_amount_tv);
		month_tv = (TextView)findViewById(R.id.month_tv);
		calendar_total_amount_tv = (TextView)findViewById(R.id.calendar_total_amount_tv);
		total_amount_ll = (LinearLayout)findViewById(R.id.total_amount_ll);
		expandListView = (CustomExpandableListView)findViewById(R.id.expandListView);
		repayment_day_tip_ll = (LinearLayout)findViewById(R.id.repayment_day_tip_ll);
		calendar_title_ll.setVisibility(View.GONE);
		day_amount_ll.setVisibility(View.GONE);
		total_amount_ll.setVisibility(View.GONE);
		repayment_day_tip_ll.setVisibility(View.GONE);
		
		viewPager = (WrapContentHeightViewPager)this.findViewById(R.id.viewpager);
		final ScreenSlidePagerAdapter screenSlidePagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
		viewPager.setAdapter(screenSlidePagerAdapter);
		viewPager.setCurrentItem(500);
		tvMonth = (TextView)this.findViewById(R.id.tv_month);
		left_img = (ImageView)findViewById(R.id.left_img);
		left_img.setOnClickListener(this);
		right_img = (ImageView)findViewById(R.id.right_img);
		right_img.setOnClickListener(this);
		month = Calendar.getInstance().get(Calendar.YEAR) + "年" + (Calendar.getInstance().get(Calendar.MONTH) + 1) + "月";
		tvMonth.setText(month);
		month_tv.setText((Calendar.getInstance().get(Calendar.MONTH) + 1) + "");
		viewPager.setOnPageChangeListener(new LazyViewPager.OnPageChangeListener()
		{
			
			@Override
			public void onPageSelected(int position)
			{
				currentPosition = position;
				Calendar calendar = CalendarUtils.getSelectCalendar(position);
				month = calendar.get(Calendar.YEAR) + "年" + (calendar.get(Calendar.MONTH) + 1) + "月";
				tvMonth.setText(month);
				
				day_tv.setText("");
				none_day_tv.setText("");
				day_amount_tv.setText("0.00");
				
				expandListView.setVisibility(View.GONE);
				total_amount_ll.setBackgroundColor(getResources().getColor(R.color.transparent));
				
				month_tv.setText((calendar.get(Calendar.MONTH) + 1) + "");
				calendar_total_amount_tv.setText("0.00");
			}
			
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
			{
				
			}
			
			@Override
			public void onPageScrollStateChanged(int state)
			{
				
			}
		});
		
	}
	
	List<MonthCellDescriptor> selectedDates = new ArrayList<MonthCellDescriptor>();
	
	private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter
	{
		
		public ScreenSlidePagerAdapter(FragmentManager fm)
		{
			super(fm);
		}
		
		@Override
		public Fragment getItem(final int position)
		{
			CalendarFragment f = CalendarFragment.create(position, isMainInit);
			f.setOnCellClickedListener(cellClickedListener);
			f.setSelectedDates(selectedDates);
			f.setOnCalendarChangeListener(new OnCalendarChangeListener()
			{
				
				@Override
				public void change(CalendarInfo calendarInfo)
				{
					// TODO Auto-generated method stub
					if(isMainInit)
					{
						calendar_title_ll.setVisibility(View.VISIBLE);
						day_amount_ll.setVisibility(View.VISIBLE);
						total_amount_ll.setVisibility(View.VISIBLE);
						repayment_day_tip_ll.setVisibility(View.VISIBLE);
					}
					isMainInit = false;
					
					if (calendarInfo != null)
					{
						map.put(position, calendarInfo);
						calendar_total_amount_tv.setText(AmountUtil.numKbPointFormat(calendarInfo.getCalendarAmount()));
					}
				}
			});
			return f;
		}
		
		@Override
		public int getCount()
		{
			return 1000;
		}
	}
	
	@Override
	public void onClick(View v)
	{
		// TODO Auto-generated method stub
		switch (v.getId())
		{
			case R.id.left_img:
				viewPager.setCurrentItem(currentPosition - 1);
				break;
			case R.id.right_img:
				viewPager.setCurrentItem(currentPosition + 1);
				break;
		}
	}
	
	
	OnCellClickedListener cellClickedListener = new OnCellClickedListener()
	{
		
		@Override
		public void handleClick(NData ndata)
		{
			// TODO Auto-generated method stub
			DMLog.e("ndata", ndata.toString());
			if (ndata.isPreMonth())
			{
				viewPager.setCurrentItem(currentPosition - 1);
			}
			if (ndata.isNextMonth())
			{
				viewPager.setCurrentItem(currentPosition + 1);
			}
			
			CalendarInfo calendarInfo = map.get(currentPosition);
			boolean f = false;
			if (calendarInfo != null && calendarInfo.getList() != null)
			{
				for (CalendarInfoDetail cid : calendarInfo.getList())
				{
					if (cid.getStart().equals(TimeUtils.stringToDateDetail(new Date(ndata.getTime()))))
					{
						f = true;
						day_amount_tv.setText(AmountUtil.numKbPointFormat(cid.getAmount()));
						break;
					}
				}
			}
			if(!ndata.isCurrMonth())
			{
				day_amount_ll.setVisibility(View.GONE);
				none_ll.setVisibility(View.VISIBLE);
				none_day_tv.setText("");
				total_amount_ll.setBackgroundColor(getResources().getColor(R.color.transparent));
				return;
			}
			
			if (!f)
			{
				day_amount_ll.setVisibility(View.GONE);
				none_ll.setVisibility(View.VISIBLE);
				none_day_tv.setText(TimeUtils.stringToDateDetail2(new Date(ndata.getTime())));
				total_amount_ll.setBackgroundColor(getResources().getColor(R.color.transparent));
			}
			else
			{/*
				day_amount_ll.setVisibility(View.VISIBLE);
				none_ll.setVisibility(View.GONE);
				day_tv.setText(TimeUtils.stringToDateDetail2(new Date(ndata.getTime())));
				HttpParams httpParams = new HttpParams();
				httpParams.put("start", TimeUtils.stringToDateDetail(new Date(ndata.getTime())));
				HttpUtil.getInstance().post(DMConstant.API_URLS.GET_CALENDAR_LOAN_INFO, httpParams, new HttpCallBack()
				{
					
					@Override
					public void onSuccess(JSONObjectFetchResult result)
					{
						DMLog.e("result", result.toString());
						JSONArray jsonArray = (JSONArray)result.data;
						List<LoanNode> loanNodeList = AnalyDataUtil.getInstance().analyLoanNodes(jsonArray.toString());
						for (LoanNode ln : loanNodeList)
						{
							ln.add(new LoanNode());
						}
						total_amount_ll.setBackgroundColor((loanNodeList.size() % 2 == 1) ? getResources().getColor(R.color.white)
							: getResources().getColor(R.color.transparent));
						RepaymentListAdapter adapter = new RepaymentListAdapter(RepaymentCalendarActivity.this, loanNodeList);
						expandListView.setAdapter(adapter);
						expandListView.setVisibility(View.VISIBLE);
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
		}
	};
	
	
	public interface OnCalendarChangeListener
	{
		void change(CalendarInfo calendarInfo);
	}
	
	public interface OnCellClickedListener
	{
		void handleClick(NData ndata);
	}
	
}
