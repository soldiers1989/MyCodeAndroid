package com.hxjr.p2p.ad5.bean;

import java.util.List;

/**
 * 用户日历信息
 * 
 * @author  sushuo
 * @date 2015年8月24日
 */
public class CalendarInfo
{
	
	private double calendarAmount;// 总回款
	
	private List<CalendarInfoDetail> list;// 回款列表
	
	/**
	 * @return 返回 calendarAmount
	 */
	public double getCalendarAmount()
	{
		return calendarAmount;
	}
	
	/**
	 * @param 对calendarAmount进行赋值
	 */
	public void setCalendarAmount(double calendarAmount)
	{
		this.calendarAmount = calendarAmount;
	}
	
	/**
	 * @return 返回 list
	 */
	public List<CalendarInfoDetail> getList()
	{
		return list;
	}
	
	/**
	 * @param 对list进行赋值
	 */
	public void setList(List<CalendarInfoDetail> list)
	{
		this.list = list;
	}
	
}
