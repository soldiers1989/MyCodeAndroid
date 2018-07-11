package com.hxjr.p2p.ad5.bean;

import org.json.JSONException;

import com.dm.utils.DMJsonObject;

/**
 * 投标统计
 * @author  huangkaibo
 * @date 2015年11月11日
 */
public class HomeBidCount
{
	/**
	 * 累计成交
	 */
	private double ljcj;
	
	/**
	 * 借款总额
	 */
	private double jkze;
	
	/**
	 * 已还本息
	 */
	private double yhbx;
	
	/**
	 * 待还本息
	 */
	private double dhbx;
	
	/**
	 * 逾期还款
	 */
	private double yqhk;
	
	/**
	 * 昨日成交
	 */
	private double zrcj;
	
	/**
	 * 本年成交
	 */
	private double bncj;
	
	/**
	 * 累计投资金额
	 */
	private double total;
	
	/**
	 * 累计投资总收益
	 */
	private double total_income;
	
	/**
	 * 累计投资人数
	 */
	private double total_number;
	
	/**
	 * 注册总人数
	 */
	private int zczrs;
	
	public HomeBidCount()
	{
	}
	
	public HomeBidCount(DMJsonObject data)
	{
		try
		{
			ljcj = data.getDouble("ljcj");
			jkze = data.getDouble("jkze");
			yhbx = data.getDouble("yhbx");
			dhbx = data.getDouble("dhbx");
			yqhk = data.getDouble("yqhk");
			zrcj = data.getDouble("zrcj");
			bncj = data.getDouble("bncj");
			total = data.getDouble("rzzje");
			total_income = data.getDouble("yhzsy");
			total_number = data.getDouble("yhjys");
			zczrs=data.getInt("zczrs");
		}
		catch (JSONException e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * @return 返回 zczrs
	 */
	public int getZczrs()
	{
		return zczrs;
	}	
	
	/**
	 * @param 对zczrs进行赋值
	 */
	public void setZczrs(int zczrs)
	{
		this.zczrs = zczrs;
	}
	
	/**
	 * @return 返回 ljcj
	 */
	public double getLjcj()
	{
		return ljcj;
	}
	
	/**
	 * @param 对ljcj进行赋值
	 */
	public void setLjcj(double ljcj)
	{
		this.ljcj = ljcj;
	}
	
	/**
	 * @return 返回 jkze
	 */
	public double getJkze()
	{
		return jkze;
	}
	
	/**
	 * @param 对jkze进行赋值
	 */
	public void setJkze(double jkze)
	{
		this.jkze = jkze;
	}
	
	/**
	 * @return 返回 yhbx
	 */
	public double getYhbx()
	{
		return yhbx;
	}
	
	/**
	 * @param 对yhbx进行赋值
	 */
	public void setYhbx(double yhbx)
	{
		this.yhbx = yhbx;
	}
	
	/**
	 * @return 返回 dhbx
	 */
	public double getDhbx()
	{
		return dhbx;
	}
	
	/**
	 * @param 对dhbx进行赋值
	 */
	public void setDhbx(double dhbx)
	{
		this.dhbx = dhbx;
	}
	
	/**
	 * @return 返回 yqhk
	 */
	public double getYqhk()
	{
		return yqhk;
	}
	
	/**
	 * @param 对yqhk进行赋值
	 */
	public void setYqhk(double yqhk)
	{
		this.yqhk = yqhk;
	}
	
	/**
	 * @return 返回 zrcj
	 */
	public double getZrcj()
	{
		return zrcj;
	}
	
	/**
	 * @param 对zrcj进行赋值
	 */
	public void setZrcj(double zrcj)
	{
		this.zrcj = zrcj;
	}
	
	/**
	 * @return 返回 bncj
	 */
	public double getBncj()
	{
		return bncj;
	}
	
	/**
	 * @param 对bncj进行赋值
	 */
	public void setBncj(double bncj)
	{
		this.bncj = bncj;
	}
	
	/**
	 * @return 返回 total
	 */
	public double getTotal()
	{
		return total;
	}
	
	/**
	 * @param 对total进行赋值
	 */
	public void setTotal(double total)
	{
		this.total = total;
	}
	
	/**
	 * @return 返回 total_income
	 */
	public double getTotal_income()
	{
		return total_income;
	}
	
	/**
	 * @param 对total_income进行赋值
	 */
	public void setTotal_income(double total_income)
	{
		this.total_income = total_income;
	}
	
	/**
	 * @return 返回 total_number
	 */
	public double getTotal_number()
	{
		return total_number;
	}
	
	/**
	 * @param 对total_number进行赋值
	 */
	public void setTotal_number(double total_number)
	{
		this.total_number = total_number;
	}
	
}
