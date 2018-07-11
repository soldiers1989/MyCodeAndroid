package com.hxjr.p2p.ad5.bean;

/**
 * 计算结果
 * @author  huangkaibo
 * @date 2015年11月18日
 */
public class CalculatorResult
{
	private String periods;
	
	private String repaymentPrincipalAndInterest;
	
	private String repaymentPrincipal;
	
	private String repaymentInterest;
	
	private String remainPrincipal;
	
	public String getPeriods()
	{
		return periods;
	}
	
	public void setPeriods(String periods)
	{
		this.periods = periods;
	}
	
	public String getRepaymentPrincipalAndInterest()
	{
		return repaymentPrincipalAndInterest;
	}
	
	public void setRepaymentPrincipalAndInterest(String repaymentPrincipalAndInterest)
	{
		this.repaymentPrincipalAndInterest = repaymentPrincipalAndInterest;
	}
	
	public String getRepaymentPrincipal()
	{
		return repaymentPrincipal;
	}
	
	public void setRepaymentPrincipal(String repaymentPrincipal)
	{
		this.repaymentPrincipal = repaymentPrincipal;
	}
	
	public String getRepaymentInterest()
	{
		return repaymentInterest;
	}
	
	public void setRepaymentInterest(String repaymentInterest)
	{
		this.repaymentInterest = repaymentInterest;
	}
	
	public String getRemainPrincipal()
	{
		return remainPrincipal;
	}
	
	public void setRemainPrincipal(String remainPrincipal)
	{
		this.remainPrincipal = remainPrincipal;
	}
}
