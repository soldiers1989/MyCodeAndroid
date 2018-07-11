package com.hxjr.p2p.ad5.bean;

import java.io.Serializable;

public class RepayInfo implements Serializable
{
	/**
	 * 注释内容
	 */
	private static final long serialVersionUID = 8402167392663666969L;
	
	/**
	 * 还款标ID
	 */
	private int loanID;
	
	/**
	 * 当期还款总需
	 */
	private double loanTotalMoney;
	
	/**
	 * 用户账户余额
	 */
	private double accountAmount;
	
	/**
	 * 当期应还本息
	 */
	private double loanMustMoney;
	
	/**
	 * 当期应还本金
	 */
	private double yhbj;
	
	/**
	 * 当期借款管理费
	 */
	private double loanManageAmount;
	
	/**
	 * 逾期罚息
	 */
	private double overdueInterest;
	
	/**
	 * 逾期管理费
	 */
	private double overdueManage;
	
	/**
	 * 逾期费用
	 */
	private double loanArrMoney;
	
	/**
	 * 当前期号
	 */
	private int number;
	
	public int getLoanID()
	{
		return loanID;
	}
	
	public void setLoanID(int loanID)
	{
		this.loanID = loanID;
	}
	
	public double getLoanTotalMoney()
	{
		return loanTotalMoney;
	}
	
	public void setLoanTotalMoney(double loanTotalMoney)
	{
		this.loanTotalMoney = loanTotalMoney;
	}
	
	public double getAccountAmount()
	{
		return accountAmount;
	}
	
	public void setAccountAmount(double accountAmount)
	{
		this.accountAmount = accountAmount;
	}
	
	public double getLoanMustMoney()
	{
		return loanMustMoney;
	}
	
	public void setLoanMustMoney(double loanMustMoney)
	{
		this.loanMustMoney = loanMustMoney;
	}
	
	public double getYhbj()
	{
		return yhbj;
	}
	
	public void setYhbj(double yhbj)
	{
		this.yhbj = yhbj;
	}
	
	public double getLoanManageAmount()
	{
		return loanManageAmount;
	}
	
	public void setLoanManageAmount(double loanManageAmount)
	{
		this.loanManageAmount = loanManageAmount;
	}
	
	public double getOverdueInterest()
	{
		return overdueInterest;
	}
	
	public void setOverdueInterest(double overdueInterest)
	{
		this.overdueInterest = overdueInterest;
	}
	
	public double getOverdueManage()
	{
		return overdueManage;
	}
	
	public void setOverdueManage(double overdueManage)
	{
		this.overdueManage = overdueManage;
	}
	
	public double getLoanArrMoney()
	{
		return loanArrMoney;
	}
	
	public void setLoanArrMoney(double loanArrMoney)
	{
		this.loanArrMoney = loanArrMoney;
	}
	
	public int getNumber()
	{
		return number;
	}
	
	public void setNumber(int number)
	{
		this.number = number;
	}
	
}
