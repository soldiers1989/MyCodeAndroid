/*
 * 文 件 名:  GyRecordList.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  zhoulantao
 * 修改时间:  2015年11月25日
 */
package com.hxjr.p2p.ad5.bean;

import org.json.JSONException;

import com.dm.utils.DMJsonObject;

/**
 * 公益标投资记录基础信息
 * 
 * @author  zhoulantao
 * @version  [版本号, 2015年11月25日]
 */
public class GyRecordList
{
	/**
	 * 投资人
	 */
	private String userName;
	
	/**
	 * 投资金额
	 */
	private String loanAmount;
	
	/**
	 * 投资时间
	 */
	private String locanTime;
	
	public GyRecordList()
	{
	}
	
	public GyRecordList(DMJsonObject data)
	{
		try
		{
			userName = data.getString("userName");
			loanAmount = data.getString("loanAmount");
			locanTime = data.getString("locanTime");
		}
		catch (JSONException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public String getUserName()
	{
		return userName;
	}
	
	public void setUserName(String userName)
	{
		this.userName = userName;
	}
	
	public String getLoanAmount()
	{
		return loanAmount;
	}
	
	public void setLoanAmount(String loanAmount)
	{
		this.loanAmount = loanAmount;
	}
	
	public String getLocanTime()
	{
		return locanTime;
	}
	
	public void setLocanTime(String locanTime)
	{
		this.locanTime = locanTime;
	}
	
}
