package com.hxjr.p2p.ad5.bean;

import java.io.Serializable;

import org.json.JSONException;

import com.dm.utils.DMJsonObject;
import com.hxjr.p2p.ad5.utils.FormatUtil;

/**
 * 债权转让基本信息
 * 
 * @author jiaohongyun
 *
 */
public class CreDetailBase implements Serializable
{
	/**
	 * 注释内容
	 */
	private static final long serialVersionUID = -980548008752557246L;
	
	public CreDetailBase()
	{
	
	}
	
	public CreDetailBase(DMJsonObject data)
	{
		try
		{
			cycle = data.getInt("cycle");
			days = data.getInt("days");
			publishTime = data.getString("publishTime");
			status = data.getString("status");
			remainAmount = data.getString("remainAmount");
			paymentType = data.getString("paymentType");
			isDay = data.getString("isDay");
			financialType = data.getString("financialType");
			peopleNum = data.getInt("peopleNum");
			endTime = data.getString("endTime");
			type = data.getInt("type");
			amount = data.getString("amount");
			alrAmount = data.getString("alrAmount");
			rate = data.getString("rate");
			term = data.getInt("term");
			guarantee = data.getString("guarantee");
			bidTitle = data.getString("bidTitle");
			setDhjeAmount(data.getString("dhjeAmount"));
			creditorVal = data.getString("creditorVal");
			salePrice = data.getString("salePrice");
			guaSch = data.getString("guaSch");
			isDanBao = data.getString("isDanBao");
			hkDate = data.getString("hkDate");
			jlRate = data.getString("jlRate");
		}
		catch (JSONException e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * 待收本息
	 */
	private String dhjeAmount;
	
	/**剩余期数*/
	private int days;
	
	/**
	 * 债权借款标题
	 */
	private String bidTitle;
	
	/**
	 * 投资人次
	 */
	private int peopleNum;
	
	/**
	 * 年利率
	 */
	private String rate;
	
	/**
	 * 借款周期
	 */
	private int cycle;
	
	/**
	 * 借款期限
	 */
	private int term;
	
	/**
	 * 标的类型，1企业标，2个人标
	 */
	private int type;
	
	/**
	 * 借款总金额
	 */
	private String amount;
	
	/**
	 * 剩余可投金额
	 */
	private String remainAmount;
	
	/**
	 * 已投金额
	 */
	private String alrAmount;
	
	/**
	 * 还款方式方式
	 */
	private String paymentType;
	
	/**
	 * 还款状态
	 */
	private String status;
	
	/**
	 * 发布时间
	 */
	private String publishTime;
	
	/**
	 * 结束时间
	 */
	private String endTime;
	
	/**
	 * 借款类型
	 */
	private String financialType;
	
	/**
	 * 是否按天计算（S是，F否）
	 */
	private String isDay;
	
	/**
	 * 债券价格
	 */
	private String creditorVal;
	
	/**
	 * 出售价格
	 */
	private String salePrice;
	
	/**
	 * 担保机构
	 */
	private String guarantee;
	
	/**
	 * 担保方式
	 */
	private String guaSch;
	
	/**
	 * 是否有担保（S：是，F：否）
	 */
	private String isDanBao;
	
	/**
	 * 还款日期
	 */
	private String hkDate;
	
	/**
	 * 奖励利率
	 */
	private String jlRate;
	
	public int getDays()
	{
		return days;
	}
	
	public void setDays(int days)
	{
		this.days = days;
	}
	
	public String getBidTitle()
	{
		return bidTitle;
	}
	
	public void setBidTitle(String bidTitle)
	{
		this.bidTitle = bidTitle;
	}
	
	public int getPeopleNum()
	{
		return peopleNum;
	}
	
	public void setPeopleNum(int peopleNum)
	{
		this.peopleNum = peopleNum;
	}
	
	public String getRate()
	{
		double temp = Double.valueOf(rate) * 100;
		return FormatUtil.formatStr2("" + temp) + "%";
	}
	
	public void setRate(String rate)
	{
		this.rate = rate;
	}
	
	public int getCycle()
	{
		return cycle;
	}
	
	public void setCycle(int cycle)
	{
		this.cycle = cycle;
	}
	
	public int getTerm()
	{
		return term;
	}
	
	public void setTerm(int term)
	{
		this.term = term;
	}
	
	public int getType()
	{
		return type;
	}
	
	public void setType(int type)
	{
		this.type = type;
	}
	
	public String getAmount()
	{
		return amount;
	}
	
	public void setAmount(String amount)
	{
		this.amount = amount;
	}
	
	public String getRemainAmount()
	{
		return remainAmount;
	}
	
	public void setRemainAmount(String remainAmount)
	{
		this.remainAmount = remainAmount;
	}
	
	public String getAlrAmount()
	{
		return alrAmount;
	}
	
	public void setAlrAmount(String alrAmount)
	{
		this.alrAmount = alrAmount;
	}
	
	public String getPaymentType()
	{
		return paymentType;
	}
	
	public void setPaymentType(String paymentType)
	{
		this.paymentType = paymentType;
	}
	
	public String getStatus()
	{
		return status;
	}
	
	public void setStatus(String status)
	{
		this.status = status;
	}
	
	public String getPublishTime()
	{
		return publishTime;
	}
	
	public void setPublishTime(String publishTime)
	{
		this.publishTime = publishTime;
	}
	
	public String getEndTime()
	{
		return endTime;
	}
	
	public void setEndTime(String endTime)
	{
		this.endTime = endTime;
	}
	
	public String getFinancialType()
	{
		return financialType;
	}
	
	public void setFinancialType(String financialType)
	{
		this.financialType = financialType;
	}
	
	public String getIsDay()
	{
		return isDay;
	}
	
	public void setIsDay(String isDay)
	{
		this.isDay = isDay;
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
	
	public String getGuarantee()
	{
		return guarantee;
	}
	
	public void setGuarantee(String guarantee)
	{
		this.guarantee = guarantee;
	}
	
	public String getIsDanBao()
	{
		return isDanBao;
	}
	
	public void setIsDanBao(String isDanBao)
	{
		this.isDanBao = isDanBao;
	}
	
	public String getGuaSch()
	{
		return guaSch;
	}
	
	public void setGuaSch(String guaSch)
	{
		this.guaSch = guaSch;
	}
	
	public String getHkDate()
	{
		return hkDate;
	}
	
	public void setHkDate(String hkDate)
	{
		this.hkDate = hkDate;
	}
	
	public String getDhjeAmount()
	{
		return dhjeAmount;
	}
	
	public void setDhjeAmount(String dhjeAmount)
	{
		this.dhjeAmount = dhjeAmount;
	}
	
	public String getJlRate()
	{
		
		if (null == jlRate || "".equals(jlRate) || "0".equals(jlRate) || "null".equalsIgnoreCase(jlRate))
		{
			return "";
		}
		//		else
		//		{
		//			double temp = Double.valueOf(jlRate) * 100;
		//			return "+" + FormatUtil.get2String(temp) + "%";
		//		}
		double temp = Double.valueOf(jlRate) * 100;
		return "+" + FormatUtil.formatStr2("" + temp) + "%";
	}
	
	public void setJlRate(String jlRate)
	{
		this.jlRate = jlRate;
	}
}
