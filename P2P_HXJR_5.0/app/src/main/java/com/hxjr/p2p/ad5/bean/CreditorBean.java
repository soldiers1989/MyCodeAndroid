package com.hxjr.p2p.ad5.bean;

import org.json.JSONException;

import com.dm.utils.DMJsonObject;
import com.hxjr.p2p.ad5.utils.FormatUtil;

/**
 * 
 * @author luxiao 我的债权
 */
public class CreditorBean
{
	/**
	 * 债权编码
	 */
	private String creditorNo = "";
	
	/**
	 * 标id
	 */
	private int bidId = 0;
	
	/**
	 * 利率
	 */
	private Double rate = 0.0d;
	
	/**
	 * 借款总金额
	 */
	private Double totalAmount = 0.0d;
	
	/**
	 * 投资金额
	 */
	private Double invAmount = 0.0d;
	
	/**
	 * 剩余可投金额
	 */
	private Double remainAmount = 0.0d;
	
	/**
	 * 已赚金额
	 */
	private Double earnAmount = 0.0d;
	
	/**
	 * 结清日期
	 */
	private String endDate = "";
	
	/**
	 * 待收本息
	 */
	private Double revInterest = 0.0d;
	
	/**
	 * 总期数
	 */
	private int totalTerm = 0;
	
	/**
	 * 已还期数
	 */
	private int backTerm = 0;
	
	/**
	 * 下个还款日期
	 */
	private String backTime = "";
	
	/**
	 * 是否为按天借款,S:是;F:否
	 */
	private String isDay = "";
	
	/**
	 * 剩余时间
	 */
	private String surTime = "";
	
	public CreditorBean()
	{
	
	}
	
	public CreditorBean(DMJsonObject data)
	{
		if (data == null)
		{
			return;
		}
		try
		{
			
			creditorNo = data.getString("creditorNo");
			bidId = data.getInt("bidId");
			rate = data.getDouble("rate");
			
			totalAmount = FormatUtil.get2Double(data.getDouble("totalAmount"));
			invAmount = FormatUtil.get2Double(data.getDouble("invAmount"));
			remainAmount = FormatUtil.get2Double(data.getDouble("remainAmount"));
			earnAmount = FormatUtil.get2Double(data.getDouble("earnAmount"));
			endDate = data.getString("endDate");
			revInterest = FormatUtil.get2Double(data.getDouble("revInterest"));
			totalTerm = data.getInt("totalTerm");
			backTerm = data.getInt("backTerm");
			backTime = data.getString("backTime");
			isDay = data.getString("isDay");
			surTime = data.getString("surTime");
		}
		catch (JSONException e)
		{
			e.printStackTrace();
		}
	}
	
	public String getSurTime()
	{
		return surTime;
	}
	
	public void setSurTime(String surTime)
	{
		this.surTime = surTime;
	}
	
	public String getCreditorNo()
	{
		return creditorNo;
	}
	
	public void setCreditorNo(String creditorNo)
	{
		this.creditorNo = creditorNo;
	}
	
	public int getBidId()
	{
		return bidId;
	}
	
	public void setBidId(int bidId)
	{
		this.bidId = bidId;
	}
	
	public Double getRate()
	{
		return rate;
	}
	
	public void setRate(Double rate)
	{
		this.rate = rate;
	}
	
	public Double getTotalAmount()
	{
		return totalAmount;
	}
	
	public void setTotalAmount(Double totalAmount)
	{
		this.totalAmount = totalAmount;
	}
	
	public Double getInvAmount()
	{
		return invAmount;
	}
	
	public void setInvAmount(Double invAmount)
	{
		this.invAmount = invAmount;
	}
	
	public Double getRemainAmount()
	{
		return remainAmount;
	}
	
	public void setRemainAmount(Double remainAmount)
	{
		this.remainAmount = remainAmount;
	}
	
	public Double getEarnAmount()
	{
		return earnAmount;
	}
	
	public void setEarnAmount(Double earnAmount)
	{
		this.earnAmount = earnAmount;
	}
	
	public String getEndDate()
	{
		return endDate;
	}
	
	public void setEndDate(String endDate)
	{
		this.endDate = endDate;
	}
	
	public Double getRevInterest()
	{
		return revInterest;
	}
	
	public void setRevInterest(Double revInterest)
	{
		this.revInterest = revInterest;
	}
	
	public int getTotalTerm()
	{
		return totalTerm;
	}
	
	public void setTotalTerm(int totalTerm)
	{
		this.totalTerm = totalTerm;
	}
	
	public int getBackTerm()
	{
		return backTerm;
	}
	
	public void setBackTerm(int backTerm)
	{
		this.backTerm = backTerm;
	}
	
	public String getBackTime()
	{
		return backTime;
	}
	
	public void setBackTime(String backTime)
	{
		this.backTime = backTime;
	}
	
	public String getIsDay()
	{
		return isDay;
	}
	
	public void setIsDay(String isDay)
	{
		this.isDay = isDay;
	}
	
}
