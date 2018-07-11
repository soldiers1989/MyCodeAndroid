package com.hxjr.p2p.ad5.bean;

import java.io.Serializable;

public class RepaymentDetail implements Serializable
{
	/**
	 * 注释内容
	 */
	private static final long serialVersionUID = 1L;
	
	private String periods;
	
	private String status;
	
	private String date;
	
	private String money;
	
	public String getPeriods()
	{
		return periods;
	}
	
	public void setPeriods(String periods)
	{
		this.periods = periods;
	}
	
	public String getStatus()
	{
		return status;
	}
	
	public void setStatus(String status)
	{
		this.status = status;
	}
	
	public String getDate()
	{
		return date;
	}
	
	public void setDate(String date)
	{
		this.date = date;
	}
	
	public String getMoney()
	{
		return money;
	}
	
	public void setMoney(String money)
	{
		this.money = money;
	}
	
	public static long getSerialversionuid()
	{
		return serialVersionUID;
	}
	
}
