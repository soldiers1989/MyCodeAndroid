package com.hxjr.p2p.ad5.bean;

import org.json.JSONException;

import com.dm.utils.DMJsonObject;

/**
 * 还款计划
 * 
 * @author jiaohongyun
 *
 */
public class HkjhBean
{
	
	public HkjhBean()
	{
	
	}
	
	public HkjhBean(DMJsonObject data)
	{
		try
		{
			repayDate = data.getString("repayDate");
			amount = data.getString("amount");
			repayType = data.getString("repayType");
			realDate = data.getString("realDate");
			status = data.getString("status");
		}
		catch (JSONException e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * 还款日期
	 */
	private String repayDate;
	
	/**
	 * 还款金额
	 */
	private String amount;
	
	/**
	 * 还款类型（利息,本金）
	 */
	private String repayType;
	
	/**
	 * 实际还款时间
	 */
	private String realDate;
	
	/**
	 * 还款状态
	 */
	private String status;
	
	public String getRepayDate()
	{
		return repayDate;
	}
	
	public void setRepayDate(String repayDate)
	{
		this.repayDate = repayDate;
	}
	
	public String getAmount()
	{
		return amount;
	}
	
	public void setAmount(String amount)
	{
		this.amount = amount;
	}
	
	public String getRepayType()
	{
		return repayType;
	}
	
	public void setRepayType(String repayType)
	{
		this.repayType = repayType;
	}
	
	public String getRealDate()
	{
		return realDate;
	}
	
	public void setRealDate(String realDate)
	{
		this.realDate = realDate;
	}
	
	public String getStatus()
	{
		return status;
	}
	
	public void setStatus(String status)
	{
		this.status = status;
	}
	
}
