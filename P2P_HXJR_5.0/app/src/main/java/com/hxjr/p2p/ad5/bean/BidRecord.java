package com.hxjr.p2p.ad5.bean;

import org.json.JSONException;

import com.dm.utils.DMJsonObject;

/**
 * 投资记录
 * @author  jiaohongyun
 * @date 2015年11月24日
 */
public class BidRecord
{
	/**
	 * 账号名(显示3位，其他用*代替)
	 */
	private String accountName;
	
	/**
	 * 投标金额
	 */
	private String bidAmount;
	
	/**
	 * 投标时间
	 */
	private String bidTime;
	
	/**
	 * 省份
	 */
	private String province;
	
	public BidRecord()
	{
		// TODO Auto-generated constructor stub
	}
	
	public BidRecord(DMJsonObject data)
	{
		try
		{
			accountName = data.getString("accountName");
			bidAmount = data.getString("bidAmount");
			bidTime = data.getString("bidTime");
			province = data.getString("province");
		}
		catch (JSONException e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * @return 返回 accountName
	 */
	public String getAccountName()
	{
		return accountName;
	}
	
	/**
	 * @param 对accountName进行赋值
	 */
	public void setAccountName(String accountName)
	{
		this.accountName = accountName;
	}
	
	/**
	 * @return 返回 bidAmount
	 */
	public String getBidAmount()
	{
		return bidAmount;
	}
	
	/**
	 * @param 对bidAmount进行赋值
	 */
	public void setBidAmount(String bidAmount)
	{
		this.bidAmount = bidAmount;
	}
	
	/**
	 * @return 返回 bidTime
	 */
	public String getBidTime()
	{
		return bidTime;
	}
	
	/**
	 * @param 对bidTime进行赋值
	 */
	public void setBidTime(String bidTime)
	{
		this.bidTime = bidTime;
	}
	
	/**
	 * @return 返回 province
	 */
	public String getProvince()
	{
		return province;
	}
	
	/**
	 * @param 对province进行赋值
	 */
	public void setProvince(String province)
	{
		this.province = province;
	}
}
