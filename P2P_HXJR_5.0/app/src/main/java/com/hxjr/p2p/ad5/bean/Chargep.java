/*
 * 文 件 名:  Chargep.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  jiaohongyun
 * 修改时间:  2014年11月19日
 */
package com.hxjr.p2p.ad5.bean;

import java.io.Serializable;

import org.json.JSONException;

import com.dm.utils.DMJsonObject;
import com.dm.utils.StringUtils;

/**
 * 充值手续费
 * @author  jiaohongyun
 * @version  [版本号, 2014年11月19日]
 */
public class Chargep implements Serializable
{
	/**
	 * 注释内容
	 */
	private static final long serialVersionUID = 7067611220229543719L;
	
	/**
	 * 充值最低金额（元）
	 */
	private double min;
	
	/**
	 * 充值最高金额（元）
	 */
	private double max;
	
	/**
	 * 用户充值手续费率
	 */
	private double p;
	
	/**
	 * 充值最高手续费（元）
	 */
	private int pMax;
	
	/**是否需要邮箱认证*/
	private boolean isNeedEmail;
	
	/**是否需要实名认证*/
	private boolean isNeedNciic;
	
	/**是否需要手机认证*/
	private boolean isNeedPhone;
	
	/**是否需要交易密码*/
	private boolean isNeedPsd;
	
	private double chargeRate;
	/**
     * 充值温馨提示
     */
    private String cwxts;
	
	public Chargep()
	{
	
	}
	
	public Chargep(DMJsonObject data)
	{
		try
		{
			min = data.getInt("min");
			max = data.getInt("max");
			p = data.getDouble("p");
			pMax = data.getInt("pMax");
			cwxts = data.getString("cwxts");
			chargeRate = data.getDouble("chargeRate");
			isNeedEmail =
				(StringUtils.isEmptyOrNull(data.getString("isNeedEmail")) || data.getString("isNeedEmail").equals("false"))
					? false : true;
			isNeedNciic =
				(StringUtils.isEmptyOrNull(data.getString("isNeedNciic")) || data.getString("isNeedNciic").equals("false"))
					? false : true;
			isNeedPhone =
				(StringUtils.isEmptyOrNull(data.getString("isNeedPhone")) || data.getString("isNeedPhone").equals("false"))
					? false : true;
			isNeedPsd = (StringUtils.isEmptyOrNull(data.getString("isNeedPsd")) || data.getString("isNeedPsd").equals("false"))
				? false : true;
		}
		catch (JSONException e)
		{
			e.printStackTrace();
		}
	}
	
	public double getMin()
	{
		return min;
	}
	
	public void setMin(double min)
	{
		this.min = min;
	}
	
	public double getMax()
	{
		return max;
	}
	
	public void setMax(double max)
	{
		this.max = max;
	}
	
	public double getP()
	{
		return p;
	}
	
	public void setP(double p)
	{
		this.p = p;
	}
	
	public int getpMax()
	{
		return pMax;
	}
	
	public void setpMax(int pMax)
	{
		this.pMax = pMax;
	}
	
	public boolean isNeedEmail()
	{
		return isNeedEmail;
	}
	
	public void setNeedEmail(boolean isNeedEmail)
	{
		this.isNeedEmail = isNeedEmail;
	}
	
	public boolean isNeedNciic()
	{
		return isNeedNciic;
	}
	
	public void setNeedNciic(boolean isNeedNciic)
	{
		this.isNeedNciic = isNeedNciic;
	}
	
	public boolean isNeedPhone()
	{
		return isNeedPhone;
	}
	
	public void setNeedPhone(boolean isNeedPhone)
	{
		this.isNeedPhone = isNeedPhone;
	}
	
	public boolean isNeedPsd()
	{
		return isNeedPsd;
	}
	
	public void setNeedPsd(boolean isNeedPsd)
	{
		this.isNeedPsd = isNeedPsd;
	}

	/**
	 * @return 返回 cwxts
	 */
	public String getCwxts()
	{
		return cwxts;
	}

	/**
	 * @param 对cwxts进行赋值
	 */
	public void setCwxts(String cwxts)
	{
		this.cwxts = cwxts;
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
