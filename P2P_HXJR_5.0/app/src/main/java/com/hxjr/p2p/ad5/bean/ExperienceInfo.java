/*
 * 文 件 名:  ExperienceInfo.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  zhoulantao
 * 修改时间:  2015年12月2日
 */
package com.hxjr.p2p.ad5.bean;

import org.json.JSONException;

import com.dm.utils.DMJsonObject;

/**
 * 体验金信息
 * 
 * @author  zhoulantao
 * @version  [版本号, 2015年12月2日]
 */
public class ExperienceInfo
{
    /**
     * 体验金ID
     */
    private int experienceId;
    
    /**
     * 体验金金额
     */
    private String expAmount;
    
    /**
     * 赠送时间
     */
    private String beginDate;
    
    /**
     * 失效时间
     */
    private String endDate;
    
    /**
     * 体验金状态
     */
    private String status;
    
    /**
     * 体验金状态(中文)
     */
    private String statusDes;
    
    /**
     * 体验金投标的年化利率
     */
    private String rate;
    
    /**
     * 借款标名称
     */
    private String bidTitile;
    
    /**
     * 待收金额
     */
    private String dueInAmount;
    
    /**
     * 已收金额
     */
    private String receivedAmount;
    
    /**
     * 有效收益月份数
     */
    private int months;
    
    /**
     * 结清日期
     */
    private String receivedDate;
    
    /**
     * 体验金投标时间
     */
    private String bidDate;
    
    /**
     * 下一还款日
     */
    private String nextRepaymentDate;
    
    public ExperienceInfo()
    {
    }
    
    public ExperienceInfo(DMJsonObject data)
    {
    	try
		{
			experienceId = data.getInt("experienceId");
			expAmount = data.getString("expAmount");
			beginDate = data.getString("beginDate");
			endDate = data.getString("endDate");
			status = data.getString("status");
			statusDes = data.getString("statusDes");
			rate = data.getString("rate");
			bidTitile = data.getString("bidTitile");
			dueInAmount = data.getString("dueInAmount");
			receivedAmount = data.getString("receivedAmount");
			months = data.getInt("months");
			receivedDate = data.getString("receivedDate");
			bidDate = data.getString("bidDate");
			nextRepaymentDate = data.getString("nextRepaymentDate");
		}
		catch (JSONException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    
    public int getExperienceId()
    {
        return experienceId;
    }
    
    public void setExperienceId(int experienceId)
    {
        this.experienceId = experienceId;
    }
    
    public String getExpAmount()
    {
        return expAmount;
    }
    
    public void setExpAmount(String expAmount)
    {
        this.expAmount = expAmount;
    }
    
    public String getBeginDate()
    {
        return beginDate;
    }
    
    public void setBeginDate(String beginDate)
    {
        this.beginDate = beginDate;
    }
    
    public String getEndDate()
    {
        return endDate;
    }
    
    public void setEndDate(String endDate)
    {
        this.endDate = endDate;
    }
    
    public String getStatus()
    {
        return status;
    }
    
    public void setStatus(String status)
    {
        this.status = status;
    }
    
    public String getRate()
    {
        return rate;
    }
    
    public void setRate(String rate)
    {
        this.rate = rate;
    }
    
    public String getBidTitile()
    {
        return bidTitile;
    }
    
    public void setBidTitile(String bidTitile)
    {
        this.bidTitile = bidTitile;
    }
    
    public String getDueInAmount()
    {
        return dueInAmount;
    }
    
    public void setDueInAmount(String dueInAmount)
    {
        this.dueInAmount = dueInAmount;
    }
    
    public String getReceivedAmount()
    {
        return receivedAmount;
    }
    
    public void setReceivedAmount(String receivedAmount)
    {
        this.receivedAmount = receivedAmount;
    }
    
    public int getMonths()
    {
        return months;
    }
    
    public void setMonths(int months)
    {
        this.months = months;
    }
    
    public String getReceivedDate()
    {
        return receivedDate;
    }
    
    public void setReceivedDate(String receivedDate)
    {
        this.receivedDate = receivedDate;
    }
    
    public String getStatusDes()
    {
        return statusDes;
    }
    
    public void setStatusDes(String statusDes)
    {
        this.statusDes = statusDes;
    }
    
    public String getBidDate()
    {
        return bidDate;
    }
    
    public void setBidDate(String bidDate)
    {
        this.bidDate = bidDate;
    }
    
    public String getNextRepaymentDate()
    {
        return nextRepaymentDate;
    }
    
    public void setNextRepaymentDate(String nextRepaymentDate)
    {
        this.nextRepaymentDate = nextRepaymentDate;
    }
    
}
