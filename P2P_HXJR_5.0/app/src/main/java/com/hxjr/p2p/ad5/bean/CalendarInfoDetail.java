package com.hxjr.p2p.ad5.bean;

/**
 * 回款列表明细
 * 
 * @author  sushuo
 * @date 2015年8月24日
 */
public class CalendarInfoDetail
{
	
	private String start;// 时间
	
	private double amount;// 回款金额
	
	/**
	 * @return 返回 start
	 */
	public String getStart()
	{
		return start;
	}
	
	/**
	 * @param 对start进行赋值
	 */
	public void setStart(String start)
	{
		this.start = start;
	}
	
	/**
	 * @return 返回 amount
	 */
	public double getAmount()
	{
		return amount;
	}
	
	/**
	 * @param 对amount进行赋值
	 */
	public void setAmount(double amount)
	{
		this.amount = amount;
	}
	
}
