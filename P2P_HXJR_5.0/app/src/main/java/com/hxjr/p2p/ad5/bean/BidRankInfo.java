package com.hxjr.p2p.ad5.bean;

import org.json.JSONException;

import com.dm.utils.DMJsonObject;

/**
 * 土豪榜
 * @author  huangkaibo
 * @date 2015年11月7日
 */
public class BidRankInfo
{
	/**
	 * 排行榜名次
	 */
	private int rankId;
	
	/**
	 * 用户名
	 */
	private String userName;
	
	/**
	 * 用户投资金额
	 */
	private String amount;
	
	public BidRankInfo()
	{
	}
	
	public BidRankInfo(DMJsonObject data)
	{
		try
		{
			rankId = data.getInt("rankId");
			userName = data.getString("userName");
			amount = data.getString("amount");
		}
		catch (JSONException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public int getRankId()
	{
		return rankId;
	}
	
	public void setRankId(int rankId)
	{
		this.rankId = rankId;
	}
	
	public String getUserName()
	{
		return userName;
	}
	
	public void setUserName(String userName)
	{
		this.userName = userName;
	}
	
	public String getAmount()
	{
		return amount;
	}
	
	public void setAmount(String amount)
	{
		this.amount = amount;
	}
	
}
