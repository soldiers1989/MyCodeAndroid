package com.hxjr.p2p.ad5.bean;

import org.json.JSONException;

import com.dm.utils.DMJsonObject;
import com.hxjr.p2p.ad5.utils.FormatUtil;

/**
 * 
 * @author luxiao
 * 还款详情
 */

public class BorrowRepayBean
{
	/**
	 * 期号
	 */
	private int term = 0;
	
	/**
	 * 还款日期
	 */
	private String repayDate = "";
	
	/**
	 * 还款类型
	 */
	private String repayType = "";
	
	/**
	 * 还款金额
	 */
	private Double amount = 0.0d;
	
	/**
	 * 真实还款日期
	 */
	private String realDate = "";
	
	/**
	 * 状态
	 */
	private String status = "";
	
	/**
	 * 下个还款日期
	 */
	private String backTime = "";
	
	/**
	 * 当期还款金额
	 */
	private Double curBackAmount = 0.0d;
	
	public BorrowRepayBean()
	{
	
	}
	
	/**
	 * 通过 json构建bean
	 * @param data
	 */
	public BorrowRepayBean(DMJsonObject data)
	{
		if (data == null)
		{
			return;
		}
		try
		{
			term = data.getInt("term");
			repayDate = data.getString("repayDate");
			repayType = data.getString("repayType");
			amount = FormatUtil.get2Double(data.getDouble("amount"));
			realDate = data.getString("realDate");
			status = data.getString("status");
			backTime = data.getString("backTime");
			curBackAmount = FormatUtil.get2Double(data.getDouble("curBackAmount"));
			
		}
		catch (JSONException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public int getTerm()
	{
		return term;
	}
	
	public void setTerm(int term)
	{
		this.term = term;
	}
	
	public String getRepayDate()
	{
		return repayDate;
	}
	
	public void setRepayDate(String repayDate)
	{
		this.repayDate = repayDate;
	}
	
	public String getRepayType()
	{
		return repayType;
	}
	
	public void setRepayType(String repayType)
	{
		this.repayType = repayType;
	}
	
	public Double getAmount()
	{
		return amount;
	}
	
	public void setAmount(Double amount)
	{
		this.amount = amount;
	}
	
	public String getRealDate()
	{
		return realDate;
	}
	
	public void setRealDate(String realDate)
	{
		this.realDate = realDate;
	}
	
	public String getStatus()
	{
		return status;
	}
	
	public void setStatus(String status)
	{
		this.status = status;
	}
	
	public String getBackTime()
	{
		return backTime;
	}
	
	public void setBackTime(String backTime)
	{
		this.backTime = backTime;
	}
	
	public Double getCurBackAmount()
	{
		return curBackAmount;
	}
	
	public void setCurBackAmount(Double curBackAmount)
	{
		this.curBackAmount = curBackAmount;
	}
	
}
