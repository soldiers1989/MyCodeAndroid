/*
 * 文 件 名:  Message.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  jiaohongyun
 * 修改时间:  2014年11月28日
 */
package com.hxjr.p2p.ad5.bean;

import org.json.JSONException;

import com.dm.utils.DMJsonObject;

/**
 * 站内信
 * @author  jiaohongyun
 * @version  [版本号, 2014年11月28日]
 */
public class Message
{
	/**
	 * 编号
	 */
	private int id;
	
	/**
	 * 标题
	 */
	private String title;
	
	/**
	 * 时间
	 */
	private String sendTime;
	
	/**
	 * 内容
	 */
	private String content;
	
	/**
	 * 状态 已读：YD,未读：WD
	 */
	private String status;
	
	public Message(DMJsonObject data)
	{
		try
		{
			id = data.getInt("id");
			title = data.getString("title");
			sendTime = data.getString("sendTime");
			content = data.getString("content");
			status = data.getString("status");
		}
		catch (JSONException e)
		{
			e.printStackTrace();
		}
	}
	
	public int getId()
	{
		return id;
	}
	
	public void setId(int id)
	{
		this.id = id;
	}
	
	public String getTitle()
	{
		return title;
	}
	
	public void setTitle(String title)
	{
		this.title = title;
	}
	
	public String getSendTime()
	{
		return sendTime;
	}
	
	public void setSendTime(String sendTime)
	{
		this.sendTime = sendTime;
	}
	
	public String getContent()
	{
		return content;
	}
	
	public void setContent(String content)
	{
		this.content = content;
	}
	
	public String getStatus()
	{
		return status;
	}
	
	public void setStatus(String status)
	{
		this.status = status;
	}
	
}
