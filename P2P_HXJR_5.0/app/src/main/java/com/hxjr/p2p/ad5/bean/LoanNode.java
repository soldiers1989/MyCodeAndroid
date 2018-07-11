package com.hxjr.p2p.ad5.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * 回款日历--用户日历标信息
 * 
 * @author  sushuo
 * @date 2015年8月19日
 */
public class LoanNode
{
	private List<LoanNode> children = new ArrayList<LoanNode>();// 子节点集合
	
	private String title; // 标题
	
	private int periodNo; // 当前期数
	
	private int periodTotal; // 总期数
	
	private double principal; // 待收本金
	
	private double interest; // 待收利息
	
	/**
	 * @return 返回 children
	 */
	public List<LoanNode> getChildren()
	{
		return children;
	}
	
	/**
	 * @param 对children进行赋值
	 */
	public void setChildren(List<LoanNode> children)
	{
		this.children = children;
	}
	
	/**
	 * @return 返回 title
	 */
	public String getTitle()
	{
		return title;
	}
	
	/**
	 * @param 对title进行赋值
	 */
	public void setTitle(String title)
	{
		this.title = title;
	}
	
	/**
	 * @return 返回 periodNo
	 */
	public int getPeriodNo()
	{
		return periodNo;
	}
	
	/**
	 * @param 对periodNo进行赋值
	 */
	public void setPeriodNo(int periodNo)
	{
		this.periodNo = periodNo;
	}
	
	/**
	 * @return 返回 periodTotal
	 */
	public int getPeriodTotal()
	{
		return periodTotal;
	}
	
	/**
	 * @param 对periodTotal进行赋值
	 */
	public void setPeriodTotal(int periodTotal)
	{
		this.periodTotal = periodTotal;
	}
	
	/**
	 * @return 返回 principal
	 */
	public double getPrincipal()
	{
		return principal;
	}
	
	/**
	 * @param 对principal进行赋值
	 */
	public void setPrincipal(double principal)
	{
		this.principal = principal;
	}
	
	/**
	 * @return 返回 interest
	 */
	public double getInterest()
	{
		return interest;
	}
	
	/**
	 * @param 对interest进行赋值
	 */
	public void setInterest(double interest)
	{
		this.interest = interest;
	}
	
	/**
	 * 添加子节点
	 *
	 * @param node
	 */
	public void add(LoanNode node)
	{
		if (!children.contains(node))
		{
			children.add(node);
		}
	}
	
}
