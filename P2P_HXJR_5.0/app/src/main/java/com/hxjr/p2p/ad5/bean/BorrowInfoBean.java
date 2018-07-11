package com.hxjr.p2p.ad5.bean;

import org.json.JSONException;

import com.dm.utils.DMJsonObject;
import com.dm.utils.DMLog;
import com.hxjr.p2p.ad5.utils.FormatUtil;

/**
 * 
 * @author luxiao
 * 借款信息
 */

public class BorrowInfoBean
{
	/**
	 * 标id
	 */
	private int bidId = 0;
	
	/**
	 * 借款标题
	 */
	private String bidTitle = "";
	
	/**
	 * 借款总金额
	 */
	private Double totalAmount = 0.0d;
	
	/**
	 * 年利率
	 */
	private Double rate = 0.0d;
	
	/**
	 * 借款期限
	 */
	private int totalTerm = 0;
	
	/**
	 * 已还期限
	 */
	private int backTerm = 0;
	
	/**
	 * 下个还款日期
	 */
	private String backTime = "";
	
	/**
	 * 当期还款金额
	 */
	private Double curBackAmount = 0.0d;
	
	/**
	 * 状态
	 */
	private String status = "";
	
	/**
	 * 还款总额
	 */
	private Double totalBackAmount = 0.0d;
	
	/**
	 * 还清日期
	 */
	private String cleanTime = "";
	
	/**
	 * 是否按天计算
	 */
	private String isDay;
	
	/**
	 * 期限
	 */
	private String theterm;
	
	public BorrowInfoBean()
	{
	
	}
	
	/**
	 * 通过 json构建bean
	 * @param data
	 */
	public BorrowInfoBean(DMJsonObject data)
	{
		if (data == null)
		{
			return;
		}
		try
		{
			bidId = data.getInt("bidId");
			bidTitle = data.getString("bidTitle");
			totalAmount = FormatUtil.get2Double(data.getDouble("totalAmount"));
			rate = data.getDouble("rate");
			totalTerm = data.getInt("totalTerm");
			backTerm = data.getInt("backTerm");
			backTime = data.getString("backTime");
			curBackAmount = FormatUtil.get2Double(data.getDouble("curBackAmount"));
			status = data.getString("status");
			totalBackAmount = FormatUtil.get2Double(data.getDouble("totalBackAmount"));
			cleanTime = data.getString("cleanTime");
			isDay = data.getString("isDay");
			theterm = data.getString("theterm");
		}
		catch (JSONException e)
		{
			DMLog.e("BorrowInfoBean creat failed " + e.getMessage());
		}
		
	}
	
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
	
	public Double getRate()
	{
		return rate;
	}
	
	public void setRate(Double rate)
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
	
	public Double getCurBackAmount()
	{
		return curBackAmount;
	}
	
	public void setCurBackAmount(Double curBackAmount)
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
	
	public Double getTotalAmount()
	{
		return totalAmount;
	}
	
	public void setTotalAmount(Double totalAmount)
	{
		this.totalAmount = totalAmount;
	}
	
	public Double getTotalBackAmount()
	{
		return totalBackAmount;
	}
	
	public void setTotalBackAmount(Double totalBackAmount)
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
	
	public String getTheterm()
	{
		return theterm;
	}
	
	public void setTheterm(String theterm)
	{
		this.theterm = theterm;
	}
	
}
