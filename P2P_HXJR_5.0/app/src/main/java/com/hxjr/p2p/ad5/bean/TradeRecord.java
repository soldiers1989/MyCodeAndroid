package com.hxjr.p2p.ad5.bean;

import org.json.JSONException;

import com.dm.utils.DMJsonObject;

/**
 * 交易记录基本信息
 * @author  huangkaibo
 * @date 2015-11-2
 */
public class TradeRecord
{
	/**
	 * 交易时间
	 */
	private String tranTime;
	
	/**
	 * 交易类型
	 */
	private String tranType;
	
	/**
	 * 结余金额
	 */
	private double revAmount;
	
	/**
	 * 备注
	 */
	private String remark;
	
	/**
	 * 收入
	 */
	private double inAmount;
	
	/**
	 * 支出
	 */
	private double expAmount;
	
	public TradeRecord()
	{
	}
	
	public TradeRecord(DMJsonObject data)
	{
		try
		{
			tranTime = data.getString("tranTime");
			tranType = data.getString("tranType");
			revAmount = data.getDouble("revAmount");
			inAmount = data.getDouble("inAmount");
			expAmount = data.getDouble("expAmount");
			remark = data.getString("remark");
		}
		catch (JSONException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * @return 返回 tranTime
	 */
	public String getTranTime()
	{
		return tranTime;
	}
	
	/**
	 * @param 对tranTime进行赋值
	 */
	public void setTranTime(String tranTime)
	{
		this.tranTime = tranTime;
	}
	
	/**
	 * @return 返回 tranType
	 */
	public String getTranType()
	{
		return tranType;
	}
	
	/**
	 * @param 对tranType进行赋值
	 */
	public void setTranType(String tranType)
	{
		this.tranType = tranType;
	}
	
	/**
	 * @return 返回 revAmount
	 */
	public double getRevAmount()
	{
		return revAmount;
	}
	
	/**
	 * @param 对revAmount进行赋值
	 */
	public void setRevAmount(double revAmount)
	{
		this.revAmount = revAmount;
	}
	
	/**
	 * @return 返回 remark
	 */
	public String getRemark()
	{
		return remark;
	}
	
	/**
	 * @param 对remark进行赋值
	 */
	public void setRemark(String remark)
	{
		this.remark = remark;
	}
	
	/**
	 * @return 返回 inAmount
	 */
	public double getInAmount()
	{
		return inAmount;
	}
	
	/**
	 * @param 对inAmount进行赋值
	 */
	public void setInAmount(double inAmount)
	{
		this.inAmount = inAmount;
	}
	
	/**
	 * @return 返回 expAmount
	 */
	public double getExpAmount()
	{
		return expAmount;
	}
	
	/**
	 * @param 对expAmount进行赋值
	 */
	public void setExpAmount(double expAmount)
	{
		this.expAmount = expAmount;
	}
	
}
