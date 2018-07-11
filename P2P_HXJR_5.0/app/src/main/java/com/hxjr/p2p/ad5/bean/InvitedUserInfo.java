package com.hxjr.p2p.ad5.bean;

/**
 * 用户所邀请的好友信息
 * @author  tangjian
 * @date 2015年11月20日
 */
public class InvitedUserInfo
{
	private String name;
	
	private String invitedTime;
	
	public String getName()
	{
		return name;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public String getInvitedTime()
	{
		return invitedTime;
	}
	
	public void setInvitedTime(String invitedTime)
	{
		this.invitedTime = invitedTime;
	}
}
