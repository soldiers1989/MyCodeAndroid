package com.hxjr.p2p.ad5.widgets.calendar;

public class NData
{
	
	private long time;
	
	private boolean isCurrMonth;
	
	private boolean isPreMonth;
	
	private boolean isNextMonth;
	
	/**
	 * @return 返回 time
	 */
	public long getTime()
	{
		return time;
	}
	
	/**
	 * @param 对time进行赋值
	 */
	public void setTime(long time)
	{
		this.time = time;
	}
	
	/**
	 * @return 返回 isPreMonth
	 */
	public boolean isPreMonth()
	{
		return isPreMonth;
	}
	
	/**
	 * @param 对isPreMonth进行赋值
	 */
	public void setPreMonth(boolean isPreMonth)
	{
		this.isPreMonth = isPreMonth;
	}
	
	/**
	 * @return 返回 isNextMonth
	 */
	public boolean isNextMonth()
	{
		return isNextMonth;
	}
	
	/**
	 * @param 对isNextMonth进行赋值
	 */
	public void setNextMonth(boolean isNextMonth)
	{
		this.isNextMonth = isNextMonth;
	}

	/**
	 * @return 返回 isCurrMonth
	 */
	public boolean isCurrMonth()
	{
		return isCurrMonth;
	}

	/**
	 * @param 对isCurrMonth进行赋值
	 */
	public void setCurrMonth(boolean isCurrMonth)
	{
		this.isCurrMonth = isCurrMonth;
	}

	 
	@Override
	public String toString()
	{
		return "NData [time=" + time + ", isCurrMonth=" + isCurrMonth + ", isPreMonth=" + isPreMonth + ", isNextMonth="
			+ isNextMonth + "]";
	}
	
}
