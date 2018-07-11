package com.hxjr.p2p.ad5.widgets.calendar;

import java.util.Date;

public class MonthCellDescriptor
{
	private Date date;
	
	private boolean isCurrentMonth;
	
	private boolean isSelected;
	
	private boolean isToday;
	
	private boolean isSelectable;
	
	
	/** 
	 */
	public MonthCellDescriptor()
	{
		super();
	}

	/** 
	 */
	public MonthCellDescriptor(Date date)
	{
		super();
		this.date = date;
	}

	/**
	 * @return 返回 date
	 */
	public Date getDate()
	{
		return date;
	}
	
	/**
	 * @param 对date进行赋值
	 */
	public void setDate(Date date)
	{
		this.date = date;
	}
	
	/**
	 * @return 返回 isCurrentMonth
	 */
	public boolean isCurrentMonth()
	{
		return isCurrentMonth;
	}
	
	/**
	 * @param 对isCurrentMonth进行赋值
	 */
	public void setCurrentMonth(boolean isCurrentMonth)
	{
		this.isCurrentMonth = isCurrentMonth;
	}
	
	/**
	 * @return 返回 isSelected
	 */
	public boolean isSelected()
	{
		return isSelected;
	}
	
	/**
	 * @param 对isSelected进行赋值
	 */
	public void setSelected(boolean isSelected)
	{
		this.isSelected = isSelected;
	}
	
	/**
	 * @return 返回 isToday
	 */
	public boolean isToday()
	{
		return isToday;
	}
	
	/**
	 * @param 对isToday进行赋值
	 */
	public void setToday(boolean isToday)
	{
		this.isToday = isToday;
	}
	
	/**
	 * @return 返回 isSelectable
	 */
	public boolean isSelectable()
	{
		return isSelectable;
	}
	
	/**
	 * @param 对isSelectable进行赋值
	 */
	public void setSelectable(boolean isSelectable)
	{
		this.isSelectable = isSelectable;
	}
	
}
