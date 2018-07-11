package com.hxjr.p2p.ad5.bean;

import org.json.JSONException;

import com.dm.utils.DMJsonObject;

/**
 * @author  huangkaibo
 * @date 2015-10-27
 */
public class HomeNotice
{
	private String time = "";
	
	private String title;
	
	private String content;
	
	private int id;

	@Override
	public String toString() {
		return "HomeNotice{" +
				"time='" + time + '\'' +
				", title='" + title + '\'' +
				", content='" + content + '\'' +
				", id=" + id +
				'}';
	}

	/**
	 */
	public HomeNotice()
	{
		
	}

	public HomeNotice(DMJsonObject data)
	{
		try
		{
			id = data.getInt("id");
			time = data.getString("releaseTime");//time
			title = data.getString("title");
			content = data.getString("content");
		}
		catch (JSONException e)
		{
			e.printStackTrace();
		}
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
	 * @return 返回 time
	 */
	public String getTime()
	{
		return time;
	}
	
	/**
	 * @param 对time进行赋值
	 */
	public void setTime(String time)
	{
		this.time = time;
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
}
