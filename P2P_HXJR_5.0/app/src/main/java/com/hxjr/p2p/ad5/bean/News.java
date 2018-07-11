package com.hxjr.p2p.ad5.bean;

import org.json.JSONException;

import com.dm.utils.DMJsonObject;

/**
 * 资讯
 * @author  huangkaibo
 * @date 2015年11月6日
 */
public class News
{
	/** ID */
	private int id;
	
	/** 标题 */
	private String title;
	
	/** 类型 */
	private String type;
	
	/** 内容 */
	private String content;
	
	/** 来源 */
	private String from;
	
	/** 摘要 */
	private String desc;
	
	/** 发布时间 */
	private String releaseTime;
	
	/** 分享链接 */
	private String shareUrl;
	
	public News()
	{
	}
	
	public News(DMJsonObject data)
	{
		try
		{
			id = data.getInt("id");
			title = data.getString("title");
			type = data.getString("type");
			content = data.getString("content");
			from = data.getString("from");
			desc = data.getString("desc");
			releaseTime = data.getString("releaseTime");
			shareUrl = data.getString("shareUrl");
		}
		catch (JSONException e)
		{
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
	 * @return 返回 type
	 */
	public String getType()
	{
		return type;
	}
	
	/**
	 * @param 对type进行赋值
	 */
	public void setType(String type)
	{
		this.type = type;
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
	 * @return 返回 from
	 */
	public String getFrom()
	{
		return from;
	}
	
	/**
	 * @param 对from进行赋值
	 */
	public void setFrom(String from)
	{
		this.from = from;
	}
	
	/**
	 * @return 返回 desc
	 */
	public String getDesc()
	{
		return desc;
	}
	
	/**
	 * @param 对desc进行赋值
	 */
	public void setDesc(String desc)
	{
		this.desc = desc;
	}
	
	/**
	 * @return 返回 releaseTime
	 */
	public String getReleaseTime()
	{
		return releaseTime;
	}
	
	/**
	 * @param 对releaseTime进行赋值
	 */
	public void setReleaseTime(String releaseTime)
	{
		this.releaseTime = releaseTime;
	}
	
	/**
	 * @return 返回 shareUrl
	 */
	public String getShareUrl()
	{
		return shareUrl;
	}
	
	/**
	 * @param 对shareUrl进行赋值
	 */
	public void setShareUrl(String shareUrl)
	{
		this.shareUrl = shareUrl;
	}
	
}
