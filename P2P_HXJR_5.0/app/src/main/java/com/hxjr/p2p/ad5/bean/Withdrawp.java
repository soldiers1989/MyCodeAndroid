/*
 * 文 件 名:  Withdrawp.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  jiaohongyun
 * 修改时间:  2014年11月19日
 */
package com.hxjr.p2p.ad5.bean;

import java.io.Serializable;

import org.json.JSONException;

import com.dm.utils.DMJsonObject;

/**
 * 提现手续费
 * @author  jiaohongyun
 * @version  [版本号, 2014年11月19日]
 */
public class Withdrawp implements Serializable
{
	/**
	 * 注释内容
	 */
	private static final long serialVersionUID = -8001723408166430746L;
	
	/**
	 * 提现手续费计算方式, ED:按额度(默认方式);BL:按比例
	 */
	private String way;
	
	/**
	 * [按比例]提现手续费比例值
	 */
	private double proportion;
	
	/**
	 * [按额度]提现手续费1-5万收费
	 */
	private int poundage1;
	
	/**
	 * [按额度]提现手续费5-20万收费
	 */
	private int poundage2;
	
	/**
	 * 提现最低金额（元）
	 */
	private int min;
	
	/**
	 * 提现最高金额（元）
	 */
	private int max;
	
	/**
	 * 提现手续费扣除方式(true:内扣，false：外扣)"
	 */
	private boolean txkfType;
	
	/**
     * 提现温馨提示
     */
    private String twxts;
	
	public Withdrawp()
	{
		way = "";
	}
	
	public Withdrawp(DMJsonObject data)
	{
		try
		{
			way = data.getString("way");
			proportion = data.getDouble("proportion");
			poundage1 = data.getInt("poundage1");
			poundage2 = data.getInt("poundage2");
			min = data.getInt("min");
			max = data.getInt("max");
			txkfType = data.getBoolean("txkfType");
			twxts = data.getString("twxts");
		}
		catch (JSONException e)
		{
			e.printStackTrace();
		}
	}
	
	public String getWay()
	{
		return way;
	}
	
	public void setWay(String way)
	{
		this.way = way;
	}
	
	public double getProportion()
	{
		return proportion;
	}
	
	public void setProportion(double proportion)
	{
		this.proportion = proportion;
	}
	
	public int getPoundage1()
	{
		return poundage1;
	}
	
	public void setPoundage1(int poundage1)
	{
		this.poundage1 = poundage1;
	}
	
	public int getPoundage2()
	{
		return poundage2;
	}
	
	public void setPoundage2(int poundage2)
	{
		this.poundage2 = poundage2;
	}
	
	public int getMin()
	{
		return min;
	}
	
	public void setMin(int min)
	{
		this.min = min;
	}
	
	public int getMax()
	{
		return max;
	}
	
	public void setMax(int max)
	{
		this.max = max;
	}
	
	public boolean isTxkfType()
	{
		return txkfType;
	}
	
	public void setTxkfType(boolean txkfType)
	{
		this.txkfType = txkfType;
	}

	/**
	 * @return 返回 twxts
	 */
	public String getTwxts()
	{
		return twxts;
	}

	/**
	 * @param 对twxts进行赋值
	 */
	public void setTwxts(String twxts)
	{
		this.twxts = twxts;
	}
	
	
	
}
