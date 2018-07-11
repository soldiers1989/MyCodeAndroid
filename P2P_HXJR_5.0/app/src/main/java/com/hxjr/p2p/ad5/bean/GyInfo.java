/*
 * 文 件 名:  Gyinfo.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  zhoulantao
 * 修改时间:  2015年11月25日
 */
package com.hxjr.p2p.ad5.bean;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import com.dm.utils.DMJsonObject;

/**
 * 公益标基础信息
 * 
 * @author  zhoulantao
 * @version  [版本号, 2015年11月25日]
 */
public class GyInfo
{
	/**
	 * 公益标ID
	 */
	private int loadId;
	
	/**
	 * 公益标名称
	 */
	private String loanName;
	
	/**
	 * 公益标金额
	 */
	private String loanAmount;
	
	/**
	 * 开始投标时间
	 */
	private String loanStartTime;
	
	/**
	 * 结束投标时间
	 */
	private String loanEndTime;
	
	/**
	 * 最低起投金额 单位元
	 */
	private String minAmount;
	
	/**
	 * 公益机构 举办方
	 */
	private String organisers;
	
	/**
	 * 剩余可投金额 单位元
	 */
	private String remaindAmount;
	
	/**
	 * 进度
	 */
	private double progress;
	
	/**
	 * 简介
	 */
	private String introduction;
	
	/**
	 * 总共捐赠人数
	 */
	private int totalNum;
	
	/**
	 * 总共捐赠金额
	 */
	private String donationsAmount;
	
	/**
	 * 活动是否结束标识
	 */
	private boolean isTimeEnd;
	
	/**
	 * 倡导书
	 */
	private String advocacyContent;
	
	/**
	 * 进展
	 */
	private List<BidProgress> bidProgresList = new ArrayList<BidProgress>();
	
	/**
	 * 状态
	 */
	private String status;
	
	/**
	 * 中文状态
	 */
	private String statusCn;
	
	public GyInfo()
	{
	}
	
	public GyInfo(DMJsonObject data)
	{
		try
		{
			loadId = data.getInt("bidId");
			loanName = data.getString("loanName");
			loanAmount = data.getString("loanAmount");
			loanStartTime = data.getString("loanStartTime");
			loanEndTime = data.getString("loanEndTime");
			minAmount = data.getString("minAmount");
			organisers = data.getString("organisers");
			remaindAmount = data.getString("remaindAmount");
			progress = data.getDouble("progress");
			introduction = data.getString("introduction");
			totalNum = data.getInt("totalNum");
			donationsAmount = data.getString("donationsAmount");
			isTimeEnd = data.getBoolean("isTimeEnd");
			advocacyContent = data.getString("advocacyContent");
			JSONArray dataList = data.getJSONArray("bidProgres");// 进展
			for (int i = 0; i < dataList.length(); i++)
			{
				DMJsonObject bpdata = new DMJsonObject(dataList.getString(i));
				BidProgress bp = new BidProgress(bpdata);
				bidProgresList.add(bp);
			}
			status = data.getString("status");
			statusCn = data.getString("statusCn");
		}
		catch (JSONException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public int getLoadId()
	{
		return loadId;
	}

	public void setLoadId(int loadId)
	{
		this.loadId = loadId;
	}

	public String getLoanName()
	{
		return loanName;
	}
	
	public void setLoanName(String loanName)
	{
		this.loanName = loanName;
	}
	
	public String getLoanAmount()
	{
		return loanAmount;
	}
	
	public void setLoanAmount(String loanAmount)
	{
		this.loanAmount = loanAmount;
	}
	
	public String getLoanStartTime()
	{
		return loanStartTime;
	}
	
	public void setLoanStartTime(String loanStartTime)
	{
		this.loanStartTime = loanStartTime;
	}
	
	public String getLoanEndTime()
	{
		return loanEndTime;
	}
	
	public void setLoanEndTime(String loanEndTime)
	{
		this.loanEndTime = loanEndTime;
	}
	
	public String getMinAmount()
	{
		return minAmount;
	}
	
	public void setMinAmount(String minAmount)
	{
		this.minAmount = minAmount;
	}
	
	public String getOrganisers()
	{
		return organisers;
	}
	
	public void setOrganisers(String organisers)
	{
		this.organisers = organisers;
	}
	
	public String getRemaindAmount()
	{
		return remaindAmount;
	}
	
	public void setRemaindAmount(String remaindAmount)
	{
		this.remaindAmount = remaindAmount;
	}
	
	public double getProgress()
	{
		return progress;
	}
	
	public void setProgress(double progress)
	{
		this.progress = progress;
	}
	
	public String getIntroduction()
	{
		return introduction;
	}
	
	public void setIntroduction(String introduction)
	{
		this.introduction = introduction;
	}
	
	public int getTotalNum()
	{
		return totalNum;
	}
	
	public void setTotalNum(int totalNum)
	{
		this.totalNum = totalNum;
	}
	
	public String getDonationsAmount()
	{
		return donationsAmount;
	}
	
	public void setDonationsAmount(String donationsAmount)
	{
		this.donationsAmount = donationsAmount;
	}
	
	public boolean isTimeEnd()
	{
		return isTimeEnd;
	}
	
	public void setTimeEnd(boolean isTimeEnd)
	{
		this.isTimeEnd = isTimeEnd;
	}
	
	public String getAdvocacyContent()
	{
		return advocacyContent;
	}
	
	public void setAdvocacyContent(String advocacyContent)
	{
		this.advocacyContent = advocacyContent;
	}
	
	public List<BidProgress> getBidProgresList()
	{
		return bidProgresList;
	}
	
	public void setBidProgresList(List<BidProgress> bidProgresList)
	{
		this.bidProgresList = bidProgresList;
	}
	
	/**
	 * @return 返回 status
	 */
	public String getStatus()
	{
		return status;
	}
	
	/**
	 * @param 对status进行赋值
	 */
	public void setStatus(String status)
	{
		this.status = status;
	}
	
	/**
	 * @return 返回 statusCn
	 */
	public String getStatusCn()
	{
		return statusCn;
	}
	
	/**
	 * @param 对statusCn进行赋值
	 */
	public void setStatusCn(String statusCn)
	{
		this.statusCn = statusCn;
	}
	
}
