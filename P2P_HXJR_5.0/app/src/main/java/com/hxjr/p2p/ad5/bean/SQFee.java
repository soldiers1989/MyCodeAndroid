package com.hxjr.p2p.ad5.bean;

import java.io.Serializable;

import org.json.JSONException;

import com.dm.utils.DMJsonObject;

/**
 * 双乾托管费率
 * @author jiaohongyun
 *
 */
public class SQFee implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4438949792289515287L;
	
	/**
	 * 提现手续费
	 */
	private double withdrawRate;
	
	/**	
	 * 快捷支付手续费
	 */
	private double kuaiJieRate;
	
	public SQFee()
	{
		
	}
	
	public SQFee(DMJsonObject data)
	{
		try
		{
			withdrawRate = data.getDouble("withdrawRate");
			kuaiJieRate = data.getDouble("kuaiJieRate");
		}
		catch (JSONException e)
		{
			e.printStackTrace();
		}
	}
	
	public double getWithdrawRate()
	{
		return withdrawRate;
	}
	
	public void setWithdrawRate(double withdrawRate)
	{
		this.withdrawRate = withdrawRate;
	}
	
	public double getKuaiJieRate()
	{
		return kuaiJieRate / 100;
	}
	
	public void setKuaiJieRate(double kuaiJieRate)
	{
		this.kuaiJieRate = kuaiJieRate;
	}
	
}
