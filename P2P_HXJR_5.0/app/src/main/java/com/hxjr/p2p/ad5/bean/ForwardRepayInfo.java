package com.hxjr.p2p.ad5.bean;

import java.io.Serializable;

public class ForwardRepayInfo implements Serializable
{
	/**
	 * 注释内容
	 */
	private static final long serialVersionUID = 77523223950264487L;
	
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
	 * 当期应还总本息
	 */
	private double loanMustMoney;
	
	/**
	 * 剩余本金
	 */
	private double sybj;
	
	/**
	 * 提前还款手续费
	 */
	private double loanManageAmount;
	
	/**
	 * 违约金
	 */
	private double loanPenalAmount;
	
	/**
	 * 剩余利息
	 */
	private double sylx;
	
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
	
	public double getSybj()
	{
		return sybj;
	}
	
	public void setSybj(double sybj)
	{
		this.sybj = sybj;
	}
	
	public double getLoanManageAmount()
	{
		return loanManageAmount;
	}
	
	public void setLoanManageAmount(double loanManageAmount)
	{
		this.loanManageAmount = loanManageAmount;
	}
	
	public double getLoanPenalAmount()
	{
		return loanPenalAmount;
	}
	
	public void setLoanPenalAmount(double loanPenalAmount)
	{
		this.loanPenalAmount = loanPenalAmount;
	}
	
	public double getSylx()
	{
		return sylx;
	}
	
	public void setSylx(double sylx)
	{
		this.sylx = sylx;
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
