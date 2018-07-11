package com.hxjr.p2p.ad5.bean;

import org.json.JSONException;

import com.dm.utils.DMJsonObject;

/**
 * 
 * @author luxiao
 * 交易记录bean
 */
public class DealBean
{
	/**
	 * 交易时间
	 */
	private String tranTime = "";
	
	/**
	 * 交易类型
	 */
	private String tranType = "";
	
	/**
	 * 收入或支出
	 */
	private String revAmount = "";
	
	public DealBean()
	{
	
	}
	
	public DealBean(DMJsonObject data)
	{
		try
		{
			tranTime = data.getString("tranTime");
			tranType = data.getString("tranType");
			revAmount = data.getString("revAmount");
		}
		catch (JSONException e)
		{
			e.printStackTrace();
		}
	}
	
	public String getTranTime()
	{
		return tranTime;
	}
	
	public void setTranTime(String tranTime)
	{
		this.tranTime = tranTime;
	}
	
	public String getTranType()
	{
		return tranType;
	}
	
	public void setTranType(String tranType)
	{
		this.tranType = tranType;
	}
	
	public String getRevAmount()
	{
		return revAmount;
	}
	
	public void setRevAmount(String revAmount)
	{
		this.revAmount = revAmount;
	}
	
}
