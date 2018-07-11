package com.hxjr.p2p.ad5.bean;

import java.io.Serializable;
import java.util.List;

import com.dm.utils.StringUtils;

public class SpreadInfo implements Serializable
{
	
	/**
	 * 注释内容
	 */
	private static final long serialVersionUID = 3138716591489614646L;
	
	private String shareCode; //推广码
	
	private String shareMessage; //推广内容
	
	private String czjs;
	
	private String tghl;
	
	private String tgsx;
	
	private String tzcs;
	
	private String tzjl;
	
	private int yqCount; //推广客户总数
	
	private double rewardTotal; //推广奖励总计
	
	private double rewardCxTotal; //持续奖励总计
	
	private double rewardYxTotal; //有效推广奖励
	
	public String getShareCode()
	{
		return shareCode;
	}
	
	public void setShareCode(String shareCode)
	{
		this.shareCode = shareCode;
	}
	
	public String getShareMessage()
	{
		return shareMessage;
	}
	
	public void setShareMessage(String shareMessage)
	{
		this.shareMessage = shareMessage;
	}
	
	public String getCzjs()
	{
		return czjs;
	}
	
	public void setCzjs(String czjs)
	{
		this.czjs = czjs;
	}
	
	public String getTghl()
	{
		return StringUtils.isEmptyOrNull(tghl) ? "0.00" : tghl;
	}
	
	public void setTghl(String tghl)
	{
		this.tghl = tghl;
	}
	
	public String getTgsx()
	{
		return StringUtils.isEmptyOrNull(tgsx) ? "0.00" : tgsx;
	}
	
	public void setTgsx(String tgsx)
	{
		this.tgsx = tgsx;
	}
	
	public String getTzcs()
	{
		return StringUtils.isEmptyOrNull(tzcs) ? "0.00" : tzcs;
	}
	
	public void setTzcs(String tzcs)
	{
		this.tzcs = tzcs;
	}
	
	public String getTzjl()
	{
		return StringUtils.isEmptyOrNull(tzjl) ? "0.00" : tzjl;
	}
	
	public void setTzjl(String tzjl)
	{
		this.tzjl = tzjl;
	}
	
	public int getYqCount()
	{
		return yqCount;
	}
	
	public void setYqCount(int yqCount)
	{
		this.yqCount = yqCount;
	}
	
	public double getRewardTotal()
	{
		return rewardTotal;
	}
	
	public void setRewardTotal(double rewardTotal)
	{
		this.rewardTotal = rewardTotal;
	}
	
	public double getRewardCxTotal()
	{
		return rewardCxTotal;
	}
	
	public void setRewardCxTotal(double rewardCxTotal)
	{
		this.rewardCxTotal = rewardCxTotal;
	}
	
	public double getRewardYxTotal()
	{
		return rewardYxTotal;
	}
	
	public void setRewardYxTotal(double rewardYxTotal)
	{
		this.rewardYxTotal = rewardYxTotal;
	}
	
	/**
	 * 被邀请过的用户
	 */
	private List<SpreadEntity> spreadEntitys;
	
	public List<SpreadEntity> getSpreadEntity()
	{
		return spreadEntitys;
	}
	
	public void setSpreadEntity(List<SpreadEntity> spreadEntitys)
	{
		this.spreadEntitys = spreadEntitys;
	}
	
	/**
	 * 被邀请的用户
	 * @author  tangjian
	 * @date 2015年12月17日
	 */
	public class SpreadEntity implements Serializable
	{
		/**
		 * 注释内容
		 */
		private static final long serialVersionUID = -4272430717643401955L;
		
		private String userName;
		
		private String registerTime;
		
		public String getUserName()
		{
			return userName;
		}
		
		public void setUserName(String userName)
		{
			this.userName = userName;
		}
		
		public String getRegisterTime()
		{
			return registerTime;
		}
		
		public void setRegisterTime(String registerTime)
		{
			this.registerTime = registerTime;
		}
	}
}
