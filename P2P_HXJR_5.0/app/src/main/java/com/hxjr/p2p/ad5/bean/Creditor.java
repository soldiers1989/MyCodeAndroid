package com.hxjr.p2p.ad5.bean;

import org.json.JSONException;

import com.dm.utils.DMJsonObject;
import com.hxjr.p2p.ad5.utils.FormatUtil;

/**
 * 债权
 * @author  jiaohongyun
 * @version  [版本号, 2015年1月22日]
 */
public class Creditor
{
	
	public Creditor()
	{
	
	}
	
	public Creditor(DMJsonObject data)
	{
		try
		{
			id = data.getInt("id");
			creId = data.getInt("creId");
			bidId = data.getInt("bidId");
			flag = data.getString("flag");
			creditorTitle = data.getString("creditorTitle");
			rate = data.getString("rate");
			cycle = data.getInt("cycle");
			remainCycle = data.getInt("remainCycle");
			financialType = data.getString("financialType");			
			status = data.getString("status");
			paymentType = data.getString("paymentType");
			salePrice = data.getString("salePrice");			
			creditorVal = data.getString("creditorVal");
			isDay = data.getString("isDay");			
			revInterest = data.getDouble("revInterest");
			days = data.getString("days");
			jlRate = data.getString("jlRate");
		}
		catch (JSONException e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * 债权借款标题
	 */
	private String creditorTitle;
	
	/**
	 * 债权id
	 */
	private int id;
	
	/**
	 * 标Id
	 */
	private int bidId;
	
	/**
	 * 年利率
	 */
	private String rate;
	
	/**
	 * 剩余周期（月）
	 */
	private int remainCycle;
	
	/**
	 * 理财方式（指：信用认证标、实地认证标、机构担保标等类型）
	 */
	private String financialType;
	
	/**
	 * 债权价值
	 */
	private String creditorVal;
	
	/**
	 * 出售价格
	 */
	private String salePrice;
	
	/**
	 * 还款方式（还款方式,DEBX:等额本息;MYFX:每月付息,到期还本;YCFQ:本息到期一次付清;DEBJ:等额本金;）
	 */
	private String paymentType;
	
	/**
	 * 期数
	 */
	private int cycle;
	
	/**
	 * 信，实，保
	 */
	private String flag;
	
	/**
	 * 状态
	 */
	private String status;
	
	/**
	 * 债权ID
	 */
	private int creId;
	
	/**
	 * S,F
	 */
	private String isDay;
	
	/**
	 * 待收本息
	 */
	private double revInterest;
	
	/**
	 * 天标用的投资天数
	 */
	private String days;
	
	/**
	 * 奖励利率
	 */
	private String jlRate;
	
	public String getCreditorTitle()
	{
		return creditorTitle;
	}
	
	public void setCreditorTitle(String creditorTitle)
	{
		this.creditorTitle = creditorTitle;
	}
	
	public int getId()
	{
		return id;
	}
	
	public void setId(int id)
	{
		this.id = id;
	}
	
	public int getBidId()
	{
		return bidId;
	}
	
	public void setBidId(int bidId)
	{
		this.bidId = bidId;
	}
	
	public String getRate()
	{
		double temp = Double.valueOf(rate) * 100;
		return FormatUtil.formatStr2("" + temp);
	}
	
	public void setRate(String rate)
	{
		this.rate = rate;
	}
	
	public int getRemainCycle()
	{
		return remainCycle;
	}
	
	public void setRemainCycle(int remainCycle)
	{
		this.remainCycle = remainCycle;
	}
	
	public String getFinancialType()
	{
		return financialType;
	}
	
	public void setFinancialType(String financialType)
	{
		this.financialType = financialType;
	}
	
	public String getCreditorVal()
	{
		return creditorVal;
	}
	
	public void setCreditorVal(String creditorVal)
	{
		this.creditorVal = creditorVal;
	}
	
	public String getSalePrice()
	{
		return salePrice;
	}
	
	public void setSalePrice(String salePrice)
	{
		this.salePrice = salePrice;
	}
	
	public String getPaymentType()
	{
		return paymentType;
	}
	
	public void setPaymentType(String paymentType)
	{
		this.paymentType = paymentType;
	}
	
	public int getCycle()
	{
		return cycle;
	}
	
	public void setCycle(int cycle)
	{
		this.cycle = cycle;
	}
	
	public String getFlag()
	{
		return flag;
	}
	
	public void setFlag(String flag)
	{
		this.flag = flag;
	}
	
	public String getStatus()
	{
		return status;
	}
	
	public void setStatus(String status)
	{
		this.status = status;
	}
	
	public int getCreId()
	{
		return creId;
	}
	
	public void setCreId(int creId)
	{
		this.creId = creId;
	}
	
	public String getIsDay()
	{
		return isDay;
	}
	
	public void setIsDay(String isDay)
	{
		this.isDay = isDay;
	}
	
	public double getRevInterest()
	{
		return revInterest;
	}
	
	public void setRevInterest(double revInterest)
	{
		this.revInterest = revInterest;
	}
	
	public String getDays()
	{
		return days;
	}
	
	public void setDays(String days)
	{
		this.days = days;
	}
	
	public String getJlRate()
	{
		if (null == jlRate || "".equals(jlRate) || "0".equals(jlRate) || "null".equalsIgnoreCase(jlRate))
		{
			return "";
		}
		double temp = Double.valueOf(jlRate) * 100;
		return "+" + FormatUtil.formatStr2("" + temp) + "%";
	}
	
	public void setJlRate(String jlRate)
	{
		this.jlRate = jlRate;
	}
}
