package com.hxjr.p2p.ad5.widgets.calendar;

import java.util.Calendar;

public class CalendarUtils
{
	
	public static final int FIRST_VISIBLE_DAY = Calendar.SUNDAY;
	
	/**
	 * @param mPageNumber 
	 * @param calendar2 
	 * @return 
	 */
	public static Calendar getSelectCalendar(int mPageNumber)
	{
		Calendar calendar = Calendar.getInstance();
		if (mPageNumber > 500)
		{
			for (int i = 0; i < mPageNumber - 500; i++)
			{
				calendar = setNextViewItem(calendar);
			}
		}
		else if (mPageNumber < 500)
		{
			for (int i = 0; i < 500 - mPageNumber; i++)
			{
				calendar = setPrevViewItem(calendar);
			}
		}
		else
		{
			
		}
		return calendar;
	}
	
	// 上一个月
	public static Calendar setPrevViewItem(Calendar calendar)
	{
		int iMonthViewCurrentMonth = calendar.get(Calendar.MONTH);
		int iMonthViewCurrentYear = calendar.get(Calendar.YEAR);
		iMonthViewCurrentMonth--;// 当前选择月--
		
		// 如果当前月为负数的话显示上一年
		if (iMonthViewCurrentMonth == -1)
		{
			iMonthViewCurrentMonth = 11;
			iMonthViewCurrentYear--;
		}
		calendar.set(Calendar.DAY_OF_MONTH, 1); // 设置日为当月1日
		calendar.set(Calendar.MONTH, iMonthViewCurrentMonth); // 设置月
		calendar.set(Calendar.YEAR, iMonthViewCurrentYear); // 设置年
		return calendar;
	}
	
	public static Calendar setNextViewItem(Calendar calendar)
	{
		int iMonthViewCurrentMonth = calendar.get(Calendar.MONTH);
		int iMonthViewCurrentYear = calendar.get(Calendar.YEAR);
		iMonthViewCurrentMonth++;
		if (iMonthViewCurrentMonth == 12)
		{
			iMonthViewCurrentMonth = 0;
			iMonthViewCurrentYear++;
		}
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.MONTH, iMonthViewCurrentMonth);
		calendar.set(Calendar.YEAR, iMonthViewCurrentYear);
		return calendar;
	}
	
}
