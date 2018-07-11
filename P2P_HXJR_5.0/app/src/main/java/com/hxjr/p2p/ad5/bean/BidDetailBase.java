package com.hxjr.p2p.ad5.bean;

import com.dm.utils.DMJsonObject;
import com.hxjr.p2p.ad5.utils.FormatUtil;

import org.json.JSONException;

import java.io.Serializable;

/**
 * 标的详情
 * @author  jiaohongyun
 * @version  [版本号, 2015年1月22日]
 */
public class BidDetailBase implements Serializable
{
	
	public BidDetailBase()
	{
		
	}
	
	public BidDetailBase(DMJsonObject data)
	{
		try
		{
			cycle = data.getInt("cycle");
			publishTime = data.getString("publishTime");
			status = data.getString("status");
			remainAmount = data.getString("remainAmount");
			paymentType = data.getString("paymentType");
			guaSch = data.getString("guaSch");
			isDay = data.getString("isDay");
			isJlb = data.getBoolean("isJlb");
			isXsb = data.getBoolean("isXsb");
			peopleNum = data.getInt("peopleNum");
			endTime = data.getString("endTime");
			type = data.getInt("type");
			amount = data.getString("amount");
			alrAmount = data.getString("alrAmount");
			rate = data.getString("rate");
			jxType=data.getInt("jxType");
			term = data.getInt("term");
			guarantee = data.getString("guarantee");
			bidTitle = data.getString("bidTitle");
			minBidingAmount = Double.parseDouble((data.getString("minBidingAmount").replace(",", "")));
			isDanBao = data.getString("isDanBao");
			hkDate = data.getString("hkDate");
			maxBidingAmount = Double.parseDouble((data.getString("maxBidingAmount").replace(",", "")));
			flag = data.getString("flag");
			jlRate = data.getString("jlRate");
			dbjg = data.getString("guarantee");
			endTime = data.getString("endTime");
			isXgwj = data.getBoolean("isXgwj");
		}
		catch (JSONException e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -4000862223286954945L;
	
	/**
	 * 奖励表
	 */
	private boolean isJlb;
	
	/**
	 * 新手标
	 */
	private boolean isXsb;

	/**
	 * 起息类型
	 */
	private int jxType;
	
	/**
	 * 借款标题
	 */
	private String bidTitle;
	
	/**
	 * 标的类型，1企业标，2个人标
	 */
	private int type;
	
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
	 * 投标人次
	 */
	private int peopleNum;
	
	/**
	 * 可投金额
	 */
	private String remainAmount;
	
	/**
	 * 还款方式（还款方式,DEBX:等额本息;MYFX:每月付息,到期还本;YCFQ:本息到期一次付清;DEBJ:等额本金;）
	 */
	private String paymentType;
	
	/**
	 * 标的状态
	 * 标的状态（SQZ:申请中;DSH:待审核;DFB:待发布;YFB:预发布;TBZ:投标中;DFK:待放款;HKZ:还款中;YJQ:已结清;YLB:已流标;YDF:已垫付;YZF:已作废）
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
	 * 担保方式
	 */
	private String guaSch;
	
	/**
	 * 是否按天
	 */
	private String isDay;
	
	/**
	 * 已投金额
	 */
	private String alrAmount;
	
	/**
	 * 担保机构
	 */
	private String guarantee;
	
	/**
	 * 最小可投金额
	 */
	private double minBidingAmount;
	
	/**
	 * 最大可投金额
	 */
	private double maxBidingAmount;
	
	/**
	 * 是否有担保
	 */
	private String isDanBao;
	
	/**
	 * 还款日期
	 */
	private String hkDate;
	
	/**
	 * 标的担保类型
	 */
	private String flag;
	
	/**
	 * 奖励利率
	 */
	private String jlRate;
	
	/**
	 * 担保机构
	 */
	private String dbjg;
	
	/**
	 * 是否显示相关文件tab
	 */
	private boolean isXgwj;
	
	public boolean getIsJlb()
	{
		return isJlb;
	}
	
	public void setIsJlb(boolean isJlb)
	{
		this.isJlb = isJlb;
	}
	
	public boolean getIsXsb()
	{
		return isXsb;
	}
	
	public void setIsXsb(boolean isXsb)
	{
		this.isXsb = isXsb;
	}
	
	public String getDbjg()
	{
		return dbjg;
	}
	
	public void setDbjg(String dbjg)
	{
		this.dbjg = dbjg;
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
		return "+" + jlRate;
	}
	
	public void setJlRate(String jlRate)
	{
		this.jlRate = jlRate;
	}

	public void setJxType(int jxType){
		this.jxType=jxType;
	}

	public int getJxType(){
		return jxType;
	}
	
	public String getFlag()
	{
		return flag;
	}
	
	public void setFlag(String flag)
	{
		this.flag = flag;
	}
	
	public String getBidTitle()
	{
		return bidTitle;
	}
	
	public void setBidTitle(String bidTitle)
	{
		this.bidTitle = bidTitle;
	}
	
	public int getType()
	{
		return type;
	}
	
	public void setType(int type)
	{
		this.type = type;
	}
	
	public String getRate()
	{
		double temp = Double.valueOf(rate) * 100;
		return FormatUtil.formatStr2("" + temp) + "%";
	}
	
	public String getRateValue()
	{
		return rate;
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
	
	public int getPeopleNum()
	{
		return peopleNum;
	}
	
	public void setPeopleNum(int peopleNum)
	{
		this.peopleNum = peopleNum;
	}
	
	public String getRemainAmount()
	{
		return remainAmount;
	}
	
	public void setRemainAmount(String remainAmount)
	{
		this.remainAmount = remainAmount;
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
	
	public String getGuaSch()
	{
		return guaSch;
	}
	
	public void setGuaSch(String guaSch)
	{
		this.guaSch = guaSch;
	}
	
	public String getIsDay()
	{
		return isDay;
	}
	
	public void setIsDay(String isDay)
	{
		this.isDay = isDay;
	}
	
	public String getAlrAmount()
	{
		return alrAmount;
	}
	
	public void setAlrAmount(String alrAmount)
	{
		this.alrAmount = alrAmount;
	}
	
	public String getGuarantee()
	{
		return guarantee;
	}
	
	public void setGuarantee(String guarantee)
	{
		this.guarantee = guarantee;
	}
	
	public double getMinBidingAmount()
	{
		return minBidingAmount;
	}
	
	public void setMinBidingAmount(double minBidingAmount)
	{
		this.minBidingAmount = minBidingAmount;
	}
	
	public String getIsDanBao()
	{
		return isDanBao;
	}
	
	public void setIsDanBao(String isDanBao)
	{
		this.isDanBao = isDanBao;
	}
	
	public String getHkDate()
	{
		return hkDate;
	}
	
	public void setHkDate(String hkDate)
	{
		this.hkDate = hkDate;
	}
	
	public double getMaxBidingAmount()
	{
		return maxBidingAmount;
	}
	
	public void setMaxBidingAmount(double maxBidingAmount)
	{
		this.maxBidingAmount = maxBidingAmount;
	}

	/**
	 * @return 返回 isXgwj
	 */
	public boolean isXgwj()
	{
		return isXgwj;
	}

	/**
	 * @param  isXgwj 进行赋值
	 */
	public void setXgwj(boolean isXgwj)
	{
		this.isXgwj = isXgwj;
	}


	@Override
	public String toString() {
		return "BidDetailBase{" +
				"isJlb=" + isJlb +
				", isXsb=" + isXsb +
				", jxType=" + jxType +
				", bidTitle='" + bidTitle + '\'' +
				", type=" + type +
				", rate='" + rate + '\'' +
				", term=" + term +
				", cycle=" + cycle +
				", amount='" + amount + '\'' +
				", peopleNum=" + peopleNum +
				", remainAmount='" + remainAmount + '\'' +
				", paymentType='" + paymentType + '\'' +
				", status='" + status + '\'' +
				", publishTime='" + publishTime + '\'' +
				", endTime='" + endTime + '\'' +
				", guaSch='" + guaSch + '\'' +
				", isDay='" + isDay + '\'' +
				", alrAmount='" + alrAmount + '\'' +
				", guarantee='" + guarantee + '\'' +
				", minBidingAmount=" + minBidingAmount +
				", maxBidingAmount=" + maxBidingAmount +
				", isDanBao='" + isDanBao + '\'' +
				", hkDate='" + hkDate + '\'' +
				", flag='" + flag + '\'' +
				", jlRate='" + jlRate + '\'' +
				", dbjg='" + dbjg + '\'' +
				", isXgwj=" + isXgwj +
				'}';
	}
}
