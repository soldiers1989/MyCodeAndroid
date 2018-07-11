package com.hxjr.p2p.ad5.widgets.calendar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.res.Resources;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.hxjr.p2p.ad5.bean.CalendarInfo;
import com.hxjr.p2p.ad5.bean.CalendarInfoDetail;
import com.hxjr.p2p.ad5.ui.mine.RepaymentCalendarActivity.OnCellClickedListener;
import com.hxjr.p2p.ad5.utils.TimeUtils;
import com.hxjr.p2p.ad5.R;

public class CalendarGridViewAdapter extends BaseAdapter
{
	
	private Calendar calStartDate = Calendar.getInstance();// 当前显示的日历
	
	private Calendar calToday = Calendar.getInstance(); // 今日
	
	private int iMonthViewCurrentMonth = 0; // 当前视图月
	
	private int iYearViewCurrentYear = 0;
	
	private OnCellClickedListener onCellClickedListener;
	
	private boolean isMainInit;
	
	// 根据改变的日期更新日历
	// 填充日历控件用
	private void UpdateStartDateForMonth()
	{
		calStartDate.set(Calendar.DATE, 1); // 设置成当月第一天
		iMonthViewCurrentMonth = calStartDate.get(Calendar.MONTH);// 得到当前日历显示的月
		iYearViewCurrentYear = calStartDate.get(Calendar.YEAR);// 得到当前日历显示的年
		
		// 星期一是2 星期天是1 填充剩余天数
		int iDay = 0;
		int iFirstDayOfWeek = Calendar.MONDAY;
		int iStartDay = iFirstDayOfWeek;
		if (iStartDay == Calendar.MONDAY)
		{
			iDay = calStartDate.get(Calendar.DAY_OF_WEEK) - Calendar.MONDAY;
			if (iDay < 0)
				iDay = 6;
		}
		if (iStartDay == Calendar.SUNDAY)
		{
			iDay = calStartDate.get(Calendar.DAY_OF_WEEK) - Calendar.SUNDAY;
			if (iDay < 0)
				iDay = 6;
		}
		calStartDate.add(Calendar.DAY_OF_WEEK, -iDay);
		// calStartDate.add(Calendar.DAY_OF_MONTH, -1);// 周日第一位
		calStartDate.add(Calendar.DAY_OF_MONTH, CalendarUtils.FIRST_VISIBLE_DAY - 2);
		
	}
	
	ArrayList<MonthCellDescriptor> titles;
	
	CalendarInfo calendarInfo;
	
	List<MonthCellDescriptor> selectedDates;
	
	private ArrayList<MonthCellDescriptor> getDates()
	{
		
		UpdateStartDateForMonth();
		
		ArrayList<MonthCellDescriptor> alArrayList = new ArrayList<MonthCellDescriptor>();
		
		for (int i = 1; i <= 42; i++)
		{
			alArrayList.add(new MonthCellDescriptor(calStartDate.getTime()));
			calStartDate.add(Calendar.DAY_OF_MONTH, 1);
		}
		
		return alArrayList;
	}
	
	private Activity activity;
	
	Resources resources;
	
	// construct
	public CalendarGridViewAdapter(Activity a, Calendar cal)
	{
		calStartDate = cal;
		activity = a;
		resources = activity.getResources();
		titles = getDates();
	}
	
	public CalendarGridViewAdapter(Activity a, Calendar cal, CalendarInfo calendarInfo, OnCellClickedListener onCellClickedListener)
	{
		calStartDate = cal;
		activity = a;
		resources = activity.getResources();
		titles = getDates();
		this.calendarInfo = calendarInfo;
		this.onCellClickedListener = onCellClickedListener;
	}
	
	public CalendarGridViewAdapter(Activity a, Calendar cal, CalendarInfo calendarInfo, OnCellClickedListener onCellClickedListener, boolean isMainInit, List<MonthCellDescriptor> selectedDates)
	{
		calStartDate = cal;
		activity = a;
		resources = activity.getResources();
		titles = getDates();
		this.calendarInfo = calendarInfo;
		this.onCellClickedListener = onCellClickedListener;
		this.isMainInit = isMainInit;
		this.selectedDates = selectedDates;
	}
	
	public CalendarGridViewAdapter(Activity a)
	{
		activity = a;
		resources = activity.getResources();
	}
	
	@Override
	public int getCount()
	{
		return titles.size();
	}
	
	@Override
	public Object getItem(int position)
	{
		return titles.get(position);
	}
	
	@Override
	public long getItemId(int position)
	{
		return position;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		LinearLayout iv = new LinearLayout(activity);
		iv.setGravity(Gravity.CENTER);
		iv.setOrientation(LinearLayout.VERTICAL);
		iv.setBackgroundColor(resources.getColor(R.color.white));
		
		final Date myDate = ((MonthCellDescriptor)getItem(position)).getDate();
		Calendar calCalendar = Calendar.getInstance();
		calCalendar.setTime(myDate);
		
		final int iYear = calCalendar.get(Calendar.YEAR);
		final int iMonth = calCalendar.get(Calendar.MONTH);
		final int iDay = calCalendar.get(Calendar.DAY_OF_WEEK);
		
		// 判断周六周日
		//		iv.setBackgroundColor(resources.getColor(R.color.white));
		//		if (iDay == 7) {
		//			// 周六
		//			iv.setBackgroundColor(resources.getColor(R.color.text_6));
		//		} else if (iDay == 1) {
		//			// 周日
		//			iv.setBackgroundColor(resources.getColor(R.color.text_7));
		//		} else {
		//
		//		}
		// 判断周六周日结束
		
		final NData ndata = new NData();
		ndata.setTime(myDate.getTime());
		if (iYear > iYearViewCurrentYear)
		{
			ndata.setNextMonth(true);
		}
		else if (iYear < iYearViewCurrentYear)
		{
			ndata.setPreMonth(true);
		}
		else
		{
			if (iMonth > iMonthViewCurrentMonth)
			{
				ndata.setNextMonth(true);
			}
			else if (iMonth < iMonthViewCurrentMonth)
			{
				ndata.setPreMonth(true);
			}
			else
			{
				ndata.setCurrMonth(true);
			}
		}
		iv.setTag(ndata);
		
		// 日期开始
		final TextView txtDay = new TextView(activity);// 日期
		txtDay.setGravity(Gravity.CENTER);
		TextView txtToDay = new TextView(activity);
		txtToDay.setGravity(Gravity.CENTER_HORIZONTAL);
		txtToDay.setTextSize(9);
		boolean isToday = false;
		
		
		
		if (equalsDate(calToday.getTime(), myDate) && (iMonth == iMonthViewCurrentMonth))
		{
			// 当前日期
			txtToDay.setText("今天");
			txtToDay.setTextColor(resources.getColor(R.color.text_black_6));
//			if(isMainInit)
//				onCellClickedListener.handleClick(ndata);
			isToday = true;
		}
		
		boolean f = false;
		if (calendarInfo != null && calendarInfo.getList() != null)
		{
			for (CalendarInfoDetail cid : calendarInfo.getList())
			{
				if (cid.getStart().equals(TimeUtils.stringToDateDetail(myDate)))
				{
					f = true;
					break;
				}
			}
		}
		
		
		
		// 判断是否是当前月
		if (iMonth == iMonthViewCurrentMonth)
		{
			txtDay.setTextColor(f ? resources.getColor(R.color.white) : resources.getColor(R.color.text_black_6));
			txtToDay.setTextColor(resources.getColor(R.color.text_black_6));
			if(isToday)
			{
				txtDay.setBackgroundResource(f ? R.drawable.circle_solid : R.drawable.circle_ring_red);
			}
			else
			{
//				txtDay.setBackgroundResource(f ? R.drawable.circle_solid : R.drawable.circle_solid_gray);
				txtDay.setBackgroundResource(f ? R.drawable.circle_solid : R.drawable.circle_solid_white_normal);
			}
			
			if(selectedDates.size() > 0 && equalsDate(selectedDates.get(0).getDate(), new Date(ndata.getTime())))
			{
				txtDay.setTextColor(resources.getColor(R.color.white));
				txtDay.setBackgroundResource(R.drawable.circle_checked);
			}
		}
		else
		{
			txtDay.setTextColor(resources.getColor(R.color.noMonth));
			txtToDay.setTextColor(resources.getColor(R.color.noMonth));
			txtDay.setBackgroundResource(R.drawable.circle_solid_gray);
		}

		

		final int day = myDate.getDate(); // 日期
		txtDay.setText(String.valueOf(day));
		
		LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
//		lp.setMargins(0, 10, 0, 0);
		lp.setMargins(5, 5, 5, 5);
		iv.addView(txtDay, lp);
		
		LayoutParams lp1 = new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
//		lp1.setMargins(0, 0, 0, 5);
		iv.addView(txtToDay, lp1);
		
		iv.setOnClickListener(new View.OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				onCellClickedListener.handleClick(ndata);
				
				selectedDates.clear();
				MonthCellDescriptor monthCellDescriptor = new MonthCellDescriptor(new Date(ndata.getTime()));
				monthCellDescriptor.setSelected(true);
				selectedDates.add(monthCellDescriptor);
				notifyDataSetChanged();
			}
		});
		
		return iv;
	}
	
	
	@SuppressWarnings("deprecation")
	private Boolean equalsDate(Date date1, Date date2)
	{
		if (date1.getYear() == date2.getYear() && date1.getMonth() == date2.getMonth() && date1.getDate() == date2.getDate())
		{
			return true;
		}
		else
		{
			return false;
		}
	}
}
