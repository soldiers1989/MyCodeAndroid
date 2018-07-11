package com.hxjr.p2p.ad5.bean;

import org.json.JSONException;

import com.dm.utils.DMJsonObject;

/**
 * 债权转让
 * @author  huangkaibo
 * @date 2015年12月3日
 */
public class MyCreditorAssignment
{
	/**
	 * 标ID
	 */
	private int bidId;
	
	/**
	 * 债权编码
	 */
	private String creditorId;
	
	/**
	 * 标的标题
	 */
	private String bidName;
	
	/**
	 * 剩余期数
	 */
	private int subTerm;
	
	/**
	 * 总期数
	 */
	private int totalTerm;
	
	/**
	 * 年化利率
	 */
	private String rate;
	
	/**
	 * 债权价格
	 */
	private double creditorVal;
	
	/**
	 * 转让价格
	 */
	private double salePrice;
	
	/**
	 * 状态
	 */
	private String status;
	
	/**
	 * 转让手续费
	 */
	private double transferPrice;
	
	/**
	 * 转出盈亏
	 */
	private double turnOutBalance;
	
	/**
	 * 转入盈亏
	 */
	private double turnInBalance;
	
	/**
	 * 买入时间
	 */
	private String BuyingDate;
	
	public MyCreditorAssignment()
	{
	}
	
	/***
	 * @param  isYZC  是否是已转出
	 */
	public MyCreditorAssignment(DMJsonObject data, boolean isYZC)
	{
		try
		{
			bidId = data.getInt("bidId");
			creditorId = data.getString("creditorId");
			bidName = data.getString("bidName");
			subTerm = data.getInt("subTerm");
			totalTerm = data.getInt("totalTerm");
			rate = data.getString("rate");
			creditorVal = data.getDouble("creditorVal");
			salePrice = data.getDouble("salePrice");
			status = data.getString("status");
			transferPrice = data.getDouble("transferPrice");
			turnOutBalance = data.getDouble("turnOutBalance");
			turnInBalance = data.getDouble("turnInBalance");
			if (isYZC)
			{
				BuyingDate = data.getString("saleTime");
			}
			else
			{
				BuyingDate = data.getString("BuyingDate");
			}
		}
		catch (JSONException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public MyCreditorAssignment(DMJsonObject data)
	{
		try
		{
			bidId = data.getInt("bidId");
			creditorId = data.getString("creditorId");
			bidName = data.getString("bidName");
			subTerm = data.getInt("subTerm");
			totalTerm = data.getInt("totalTerm");
			rate = data.getString("rate");
			creditorVal = data.getDouble("creditorVal");
			salePrice = data.getDouble("salePrice");
			status = data.getString("status");
			transferPrice = data.getDouble("transferPrice");
			turnOutBalance = data.getDouble("turnOutBalance");
			turnInBalance = data.getDouble("turnInBalance");
			BuyingDate = data.getString("BuyingDate");
		}
		catch (JSONException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/**
	 * @return 返回 bidId
	 */
	public int getBidId()
	{
		return bidId;
	}
	
	/**
	 * @param 对bidId进行赋值
	 */
	public void setBidId(int bidId)
	{
		this.bidId = bidId;
	}
	
	/**
	 * @return 返回 creditorId
	 */
	public String getCreditorId()
	{
		return creditorId;
	}
	
	/**
	 * @param 对creditorId进行赋值
	 */
	public void setCreditorId(String creditorId)
	{
		this.creditorId = creditorId;
	}
	
	/**
	 * @return 返回 bidName
	 */
	public String getBidName()
	{
		return bidName;
	}
	
	/**
	 * @param 对bidName进行赋值
	 */
	public void setBidName(String bidName)
	{
		this.bidName = bidName;
	}
	
	/**
	 * @return 返回 subTerm
	 */
	public int getSubTerm()
	{
		return subTerm;
	}
	
	/**
	 * @param 对subTerm进行赋值
	 */
	public void setSubTerm(int subTerm)
	{
		this.subTerm = subTerm;
	}
	
	/**
	 * @return 返回 totalTerm
	 */
	public int getTotalTerm()
	{
		return totalTerm;
	}
	
	/**
	 * @param 对totalTerm进行赋值
	 */
	public void setTotalTerm(int totalTerm)
	{
		this.totalTerm = totalTerm;
	}
	
	/**
	 * @return 返回 rate
	 */
	public String getRate()
	{
		//		double temp = Double.valueOf(StringUtils.isEmptyOrNull(rate) ? "0.00" : rate) * 100;
		//		DecimalFormat df = new DecimalFormat("0.00");
		//		return df.format(temp);
		return rate;
	}
	
	/**
	 * @param 对rate进行赋值
	 */
	public void setRate(String rate)
	{
		this.rate = rate;
	}
	
	/**
	 * @return 返回 creditorVal
	 */
	public double getCreditorVal()
	{
		return creditorVal;
	}
	
	/**
	 * @param 对creditorVal进行赋值
	 */
	public void setCreditorVal(double creditorVal)
	{
		this.creditorVal = creditorVal;
	}
	
	/**
	 * @return 返回 salePrice
	 */
	public double getSalePrice()
	{
		return salePrice;
	}
	
	/**
	 * @param 对salePrice进行赋值
	 */
	public void setSalePrice(double salePrice)
	{
		this.salePrice = salePrice;
	}
	
	/**
	 * @return 返回 status
	 */
	public String getStatus()
	{
		return status;
	}
	
	/**
	 * @param 对status进行赋值
	 */
	public void setStatus(String status)
	{
		this.status = status;
	}
	
	/**
	 * @return 返回 transferPrice
	 */
	public double getTransferPrice()
	{
		return transferPrice;
	}
	
	/**
	 * @param 对transferPrice进行赋值
	 */
	public void setTransferPrice(double transferPrice)
	{
		this.transferPrice = transferPrice;
	}
	
	/**
	 * @return 返回 turnOutBalance
	 */
	public double getTurnOutBalance()
	{
		return turnOutBalance;
	}
	
	/**
	 * @param 对turnOutBalance进行赋值
	 */
	public void setTurnOutBalance(double turnOutBalance)
	{
		this.turnOutBalance = turnOutBalance;
	}
	
	/**
	 * @return 返回 turnInBalance
	 */
	public double getTurnInBalance()
	{
		return turnInBalance;
	}
	
	/**
	 * @param 对turnInBalance进行赋值
	 */
	public void setTurnInBalance(double turnInBalance)
	{
		this.turnInBalance = turnInBalance;
	}
	
	/**
	 * @return 返回 buyingDate
	 */
	public String getBuyingDate()
	{
		return BuyingDate;
	}
	
	/**
	 * @param 对buyingDate进行赋值
	 */
	public void setBuyingDate(String buyingDate)
	{
		BuyingDate = buyingDate;
	}
	
}
