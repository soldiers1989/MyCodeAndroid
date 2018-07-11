/*
 * 文 件 名:  HbInfo.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  zhoulantao
 * 修改时间:  2015年12月3日
 */
package com.hxjr.p2p.ad5.bean;

/**
 * 奖励信息
 * 
 * @author  zhoulantao
 * @version  [版本号, 2015年12月3日]
 */
public class RewardBean
{
	/**
	 * 奖励名称
	 */
	private String title;
	
	/**
	 * 奖励类型
	 */
	private String type;
	
	/**
	 * id
	 */
	private String id;
	
	/**
	 * 金额
	 */
	private String amount;
	
	/**
	 * 使用规则
	 */
	private String rule;
	
	public String getAmount()
	{
		return amount;
	}
	
	public void setAmount(String amount)
	{
		this.amount = amount;
	}
	
	public String getRule()
	{
		return rule;
	}
	
	public void setRule(String rule)
	{
		this.rule = rule;
	}
	
	public String getId()
	{
		return id;
	}
	
	public void setId(String id)
	{
		this.id = id;
	}
	
	public String getTitle()
	{
		return title;
	}
	
	public void setTitle(String title)
	{
		this.title = title;
	}
	
	public String getType()
	{
		return type;
	}
	
	public void setType(String type)
	{
		this.type = type;
	}
	
}
