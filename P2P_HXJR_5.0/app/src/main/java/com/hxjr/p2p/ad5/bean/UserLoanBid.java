package com.hxjr.p2p.ad5.bean;

import java.io.Serializable;

public class UserLoanBid implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	/**标id*/
	private int bidId;
	
	/**借款标题*/
	private String bidTitle;
	
	/**借款金额*/
	private String totalAmount;
	
	/**利率*/
	private String rate;
	
	/**总期数*/
	private int totalTerm;
	
	/**已还期数*/
	private int backTerm;
	
	/**借款期限*/
	private int theterm;
	
	/**下个还款日期*/
	private String backTime;
	
	/**当期还款金额*/
	private String curBackAmount;
	
	/**状态*/
	private String status;
	
	/**总已还款金额*/
	private String totalBackAmount;
	
	/**还清日期*/
	private String cleanTime;
	
	/*** 是否为按天借款,S:是;F:否*/
	private String isDay;
	
	/**
	* 还款信息
	*/
	private RepayInfo repayInfo;
	
	/**
	 * 提前还款信息
	 */
	private ForwardRepayInfo forwardRepayInfo;
	
	public int getBidId()
	{
		return bidId;
	}
	
	public void setBidId(int bidId)
	{
		this.bidId = bidId;
	}
	
	public String getBidTitle()
	{
		return bidTitle;
	}
	
	public void setBidTitle(String bidTitle)
	{
		this.bidTitle = bidTitle;
	}
	
	public String getTotalAmount()
	{
		return totalAmount;
	}
	
	public void setTotalAmount(String totalAmount)
	{
		this.totalAmount = totalAmount;
	}
	
	public int getTheterm()
	{
		return theterm;
	}
	
	public void setTheterm(int theterm)
	{
		this.theterm = theterm;
	}
	
	public String getRate()
	{
		return rate;
	}
	
	public void setRate(String rate)
	{
		this.rate = rate;
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
	
	public String getCurBackAmount()
	{
		return curBackAmount;
	}
	
	public void setCurBackAmount(String curBackAmount)
	{
		this.curBackAmount = curBackAmount;
	}
	
	public String getStatus()
	{
		return status;
	}
	
	public void setStatus(String status)
	{
		this.status = status;
	}
	
	public String getTotalBackAmount()
	{
		return totalBackAmount;
	}
	
	public void setTotalBackAmount(String totalBackAmount)
	{
		this.totalBackAmount = totalBackAmount;
	}
	
	public String getCleanTime()
	{
		return cleanTime;
	}
	
	public void setCleanTime(String cleanTime)
	{
		this.cleanTime = cleanTime;
	}
	
	public String getIsDay()
	{
		return isDay;
	}
	
	public void setIsDay(String isDay)
	{
		this.isDay = isDay;
	}
	
	public RepayInfo getRepayInfo()
	{
		return repayInfo;
	}
	
	public void setRepayInfo(RepayInfo repayInfo)
	{
		this.repayInfo = repayInfo;
	}
	
	public ForwardRepayInfo getForwardRepayInfo()
	{
		return forwardRepayInfo;
	}
	
	public void setForwardRepayInfo(ForwardRepayInfo forwardRepayInfo)
	{
		this.forwardRepayInfo = forwardRepayInfo;
	}
}
