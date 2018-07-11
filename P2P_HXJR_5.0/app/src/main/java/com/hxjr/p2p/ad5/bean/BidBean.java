package com.hxjr.p2p.ad5.bean;

import org.json.JSONException;

import com.dm.utils.DMJsonObject;
import com.hxjr.p2p.ad5.utils.FormatUtil;

/**
 * 借款标
 * 
 * @author jiaohongyun
 *
 */
public class BidBean
{
	
	public BidBean()
	{
		
	}
	
	public BidBean(DMJsonObject data)
	{
		try
		{
			amount = data.getString("amount");
			id = data.getInt("id");
			cycle = data.getInt("cycle");
			rate = data.getString("rate");
			flag = data.getString("flag");
			status = data.getString("status");
			remainAmount = data.getString("remainAmount");
			term = data.getInt("term");
			paymentType = data.getString("paymentType");
			isDay = data.getString("isDay");
			bidTitle = data.getString("bidTitle");
			financialType = data.getString("financialType");
			publishDate = data.getString("publicDate");
			isJlb = data.getBoolean("isJlb", false);
			isXsb = data.getBoolean("isXsb", false);
			jlRate = data.getString("jlRate");
		}
		catch (JSONException e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * 借款标题
	 */
	private String bidTitle;
	
	/**
	 * 标id
	 */
	private int id;
	
	/**
	 * 年利率
	 */
	private String rate;
	
	/**
	 * 筹款期限（天）
	 */
	private int term;
	
	/**
	 * 贷款周期（月）
	 */
	private int cycle;
	
	/**
	 * 贷款金额
	 */
	private String amount;
	
	/**
	 * 可投金额
	 */
	private String remainAmount;
	
	/**
	 * 理财方式（指：信用认证标、实地认证标、机构担保标等类型）
	 */
	private String financialType;
	
	/**
	 * 贷款用途（贷款用途文字超过8个，请作截断处理，以“...”显示）
	 */
	private String bidUse;
	
	/**
	 * 还款方式（还款方式,DEBX:等额本息;MYFX:每月付息,到期还本;YCFQ:本息到期一次付清;DEBJ:等额本金;）
	 */
	private String paymentType;
	
	/**
	 * 标的状态（SQZ:申请中;DSH:待审核;DFB:待发布;YFB:预发布;TBZ:投标中;DFK:待放款;HKZ:还款中;YJQ:已结清;YLB:
	 * 已流标;YDF:已垫付;YZF:已作废）
	 */
	private String status;
	
	/**
	 * 标的担保类型
	 */
	private String flag;
	
	/**
	 * 是否按天计算期限
	 */
	private String isDay;
	
	/**
	 * 发布日期
	 */
	private String publishDate;
	
	/**
	 * 是否为新手标
	 */
	private Boolean isXsb = false;
	
	/**
	 * 是否为奖励标
	 */
	private Boolean isJlb = false;
	
	private String jlRate;

	public String getBidTitle()
	{
		return bidTitle;
	}
	
	public void setBidTitle(String bidTitle)
	{
		this.bidTitle = bidTitle;
	}
	
	public int getId()
	{
		return id;
	}
	
	public void setId(int id)
	{
		this.id = id;
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
	
	public int getTerm()
	{
		return term;
	}
	
	public void setTerm(int term)
	{
		this.term = term;
	}
	
	public int getCycle()
	{
		return cycle;
	}
	
	public void setCycle(int cycle)
	{
		this.cycle = cycle;
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
	
	public String getFinancialType()
	{
		return financialType;
	}
	
	public void setFinancialType(String financialType)
	{
		this.financialType = financialType;
	}
	
	public String getBidUse()
	{
		return bidUse;
	}
	
	public void setBidUse(String bidUse)
	{
		this.bidUse = bidUse;
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
	
	public String getFlag()
	{
		return flag;
	}
	
	public void setFlag(String flag)
	{
		this.flag = flag;
	}
	
	public String getIsDay()
	{
		return isDay;
	}
	
	public void setIsDay(String isDay)
	{
		this.isDay = isDay;
	}
	
	@Override
	public String toString() {
		return "BidBean [bidTitle=" + bidTitle + ", id=" + id
			+ ", rate=" + rate + ", term=" + term + ", cycle="
			+ cycle + ", amount=" + amount + ", remainAmount="
			+ remainAmount + ", financialType=" + financialType
			+ ", bidUse=" + bidUse + ", paymentType=" + paymentType
			+ ", status=" + status + ", flag=" + flag + ", isDay="
			+ isDay + ", publishDate=" + publishDate + ", isXsb="
			+ isXsb + ", isJlb=" + isJlb + ", jlRate=" + jlRate + "]";
	}

	public String getPublishDate()
	{
		return publishDate;
	}
	
	public void setPublishDate(String publishDate)
	{
		this.publishDate = publishDate;
	}
	
	/**
	 * @return 返回 isXsb
	 */
	public Boolean getIsXsb()
	{
		return isXsb;
	}
	
	/**
	 * @param 对isXsb进行赋值
	 */
	public void setIsXsb(Boolean isXsb)
	{
		this.isXsb = isXsb;
	}
	
	/**
	 * @return 返回 isJlb
	 */
	public Boolean getIsJlb()
	{
		return isJlb;
	}
	
	/**
	 * @param 对isJlb进行赋值
	 */
	public void setIsJlb(Boolean isJlb)
	{
		this.isJlb = isJlb;
	}
	
	public String getJlRate()
	{
		double temp = Double.valueOf(jlRate) * 100;
		if (temp == 0)
		{
			return "";
		}
		else
		{
			return "+" + FormatUtil.get2String(temp) + "%";
		}
	}
	
	public void setJlRate(String jlRate)
	{
		this.jlRate = jlRate;
	}
	
}
