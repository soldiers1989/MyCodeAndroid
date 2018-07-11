package com.hxjr.p2p.ad5.bean;

import org.json.JSONException;

import com.dm.utils.DMJsonObject;

/**
 * 站内信息
 * @author  huangkaibo
 * @date 2015年11月25日
 */
public class Letter
{
	/**
	 * 站内信id
	 */
	private int id;
	
	/**
	 * 发送时间
	 */
	private String sendTime;
	
	/**
	 * 标题
	 */
	private String title;
	
	/**
	 * 状态
	 */
	private String status;
	
	/**
	 * 内容
	 */
	private String content;
	
	private boolean letterShowing;
	
	public Letter()
	{
		
	}
	
	public Letter(DMJsonObject data)
	{
		try
		{
			id = data.getInt("id");
			sendTime = data.getString("sendTime");
			title = data.getString("title");
			content = data.getString("content");
			status = data.getString("status");
		}
		catch (JSONException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * @return 返回 id
	 */
	public int getId()
	{
		return id;
	}
	
	/**
	 * @param 对id进行赋值
	 */
	public void setId(int id)
	{
		this.id = id;
	}
	
	/**
	 * @return 返回 sendTime
	 */
	public String getSendTime()
	{
		return sendTime;
	}
	
	/**
	 * @param 对sendTime进行赋值
	 */
	public void setSendTime(String sendTime)
	{
		this.sendTime = sendTime;
	}
	
	/**
	 * @return 返回 title
	 */
	public String getTitle()
	{
		return title;
	}
	
	/**
	 * @param 对title进行赋值
	 */
	public void setTitle(String title)
	{
		this.title = title;
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
	 * @return 返回 content
	 */
	public String getContent()
	{
		return content;
	}
	
	/**
	 * @param 对content进行赋值
	 */
	public void setContent(String content)
	{
		this.content = content;
	}

	/**
	 * @return 返回 letterShowing
	 */
	public boolean isLetterShowing()
	{
		return letterShowing;
	}

	/**
	 * @param 对letterShowing进行赋值
	 */
	public void setLetterShowing(boolean letterShowing)
	{
		this.letterShowing = letterShowing;
	}
	
	
	
}
