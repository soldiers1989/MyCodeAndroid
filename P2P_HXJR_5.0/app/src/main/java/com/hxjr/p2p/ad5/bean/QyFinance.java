package com.hxjr.p2p.ad5.bean;

import org.json.JSONException;

import com.dm.utils.DMJsonObject;

/**
 * 企业还款信息
 * 
 * @author jiaohongyun
 *
 */
public class QyFinance
{
	
	public QyFinance()
	{
	
	}
	
	public QyFinance(DMJsonObject data)
	{
		try
		{
			years = data.getString("years");
			revenue = data.getString("revenue");
			profit = data.getString("profit");
			totalAssets = data.getString("totalAssets");
			netAssets = data.getString("netAssets");
			remark = data.getString("remark");
		}
		catch (JSONException e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * 年份
	 */
	private String years;
	
	/**
	 * 主营收入
	 */
	private String revenue;
	
	/**
	 * 净利润
	 */
	private String profit;
	
	/**
	 * 总资产
	 */
	private String totalAssets;
	
	/**
	 * 净资产
	 */
	private String netAssets;
	
	/**
	 * 备注
	 */
	private String remark;
	
	public String getYears()
	{
		return years;
	}
	
	public void setYears(String years)
	{
		this.years = years;
	}
	
	public String getRevenue()
	{
		return revenue;
	}
	
	public void setRevenue(String revenue)
	{
		this.revenue = revenue;
	}
	
	public String getProfit()
	{
		return profit;
	}
	
	public void setProfit(String profit)
	{
		this.profit = profit;
	}
	
	public String getTotalAssets()
	{
		return totalAssets;
	}
	
	public void setTotalAssets(String totalAssets)
	{
		this.totalAssets = totalAssets;
	}
	
	public String getNetAssets()
	{
		return netAssets;
	}
	
	public void setNetAssets(String netAssets)
	{
		this.netAssets = netAssets;
	}
	
	public String getRemark()
	{
		return remark;
	}
	
	public void setRemark(String remark)
	{
		this.remark = remark;
	}
	
}
