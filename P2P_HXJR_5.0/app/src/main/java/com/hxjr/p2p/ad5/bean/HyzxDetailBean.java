/*
 * 文 件 名:  HyzxDetailBean.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  jiaohongyun
 * 修改时间:  2015年3月17日
 */
package com.hxjr.p2p.ad5.bean;

import org.json.JSONException;

import com.dm.utils.DMJsonObject;

/**
 * 
 * @author  jiaohongyun
 * @version  [版本号, 2015年3月17日]
 */
public class HyzxDetailBean
{
	private int id;
	
	private String title;
	
	private String releaseTime;
	
	private String from;
	
	private String shareUrl;
	
	private String content;
	
	public HyzxDetailBean(DMJsonObject data)
	{
		super();
		try
		{
			id = data.getInt("id");
			title = data.getString("title");
			releaseTime = data.getString("releaseTime");
			from = data.getString("from");
			shareUrl = data.getString("shareUrl");
			content = data.getString("content");
		}
		catch (JSONException e)
		{
			e.printStackTrace();
		}
		
	}
	
	public HyzxDetailBean()
	{
		super();
		// TODO Auto-generated constructor stub
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
	
	public String getReleaseTime()
	{
		return releaseTime;
	}
	
	public void setReleaseTime(String releaseTime)
	{
		this.releaseTime = releaseTime;
	}
	
	public String getFrom()
	{
		return from;
	}
	
	public void setFrom(String from)
	{
		this.from = from;
	}
	
	public String getShareUrl()
	{
		return shareUrl;
	}
	
	public void setShareUrl(String shareUrl)
	{
		this.shareUrl = shareUrl;
	}
	
	public String getContent()
	{
		return content;
	}
	
	public void setContent(String content)
	{
		this.content = content;
	}
	
}
