package com.hxjr.p2p.ad5.bean;

import org.json.JSONException;

import com.dm.utils.DMJsonObject;
/**
 * 易宝费率
 * @author  huangkaibo
 * @date 2016年3月18日
 */
public class YbFee
{
    private boolean chargeISPT;// 是否由平台承担充值成本  true 由平台,false 由用户 对个人用户设置
    
    private double chargeCosts;// 个人充值成本(第三方收取费用)
    
    private double withdarwCosts;// 个人提现成本费

    private boolean withdarwISPT;// "true", "是否由平台承担提现成本 true 由平台,false 由用户 对个人用户设置"
    
    private double urgentRate;
    private double chargeRate;

    public YbFee()
    {
    	
    }
    
    public YbFee(DMJsonObject data)
    {
    	try
		{
			chargeISPT = data.getBoolean("chargeISPT");
			chargeCosts = data.getDouble("chargeCosts");
			withdarwCosts = data.getDouble("withdarwCosts");
			withdarwISPT = data.getBoolean("withdarwISPT");
			urgentRate = data.getDouble("urgentRate");
			chargeRate = data.getDouble("chargeRate");
		}
		catch (JSONException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
	/**
	 * @return 返回 chargeISPT
	 */
	public boolean isChargeISPT()
	{
		return chargeISPT;
	}

	/**
	 * @param 对chargeISPT进行赋值
	 */
	public void setChargeISPT(boolean chargeISPT)
	{
		this.chargeISPT = chargeISPT;
	}

	/**
	 * @return 返回 chargeCosts
	 */
	public double getChargeCosts()
	{
		return chargeCosts;
	}

	/**
	 * @param 对chargeCosts进行赋值
	 */
	public void setChargeCosts(double chargeCosts)
	{
		this.chargeCosts = chargeCosts;
	}

	/**
	 * @return 返回 withdarwCosts
	 */
	public double getWithdarwCosts()
	{
		return withdarwCosts;
	}

	/**
	 * @param 对withdarwCosts进行赋值
	 */
	public void setWithdarwCosts(double withdarwCosts)
	{
		this.withdarwCosts = withdarwCosts;
	}

	/**
	 * @return 返回 withdarwISPT
	 */
	public boolean isWithdarwISPT()
	{
		return withdarwISPT;
	}

	/**
	 * @param 对withdarwISPT进行赋值
	 */
	public void setWithdarwISPT(boolean withdarwISPT)
	{
		this.withdarwISPT = withdarwISPT;
	}

	/**
	 * @return 返回 urgentRate
	 */
	public double getUrgentRate()
	{
		return urgentRate;
	}

	/**
	 * @param 对urgentRate进行赋值
	 */
	public void setUrgentRate(double urgentRate)
	{
		this.urgentRate = urgentRate;
	}
	/**
	 * @return 返回 chargeRate
	 */
	public double getChargeRate()
	{
		return chargeRate;
	}

	/**
	 * @param 对chargeRate进行赋值
	 */
	public void setChargeRate(double chargeRate)
	{
		this.chargeRate = chargeRate;
	}
	
    
	
    
}
