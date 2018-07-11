package com.hxjr.p2p.ad5.bean;

import org.json.JSONException;

import com.dm.utils.DMJsonObject;

/**
 * 广告
 * 
 * @author jiaohongyun
 *
 */
public class Advert
{
	
	/**
	 * 广告标题
	 */
	private String advTitle;
	
	/**
	 * 广告图片URL
	 */
	private String advImg;
	
	/**
	 * 广告内容URL,用于跳转到广告网页
	 */
	private String advUrl;
	
	public Advert()
	{
	
	}
	
	public Advert(DMJsonObject data)
	{
		try
		{
			advTitle = data.getString("advTitle");
			advImg = data.getString("advImg");
			advUrl = data.getString("advUrl");
		}
		catch (JSONException e)
		{
			e.printStackTrace();
		}
	}
	
	public String getAdvTitle()
	{
		return advTitle;
	}
	
	public void setAdvTitle(String advTitle)
	{
		this.advTitle = advTitle;
	}
	
	public String getAdvImg()
	{
		return advImg;
	}
	
	public void setAdvImg(String advImg)
	{
		this.advImg = advImg;
	}
	
	public String getAdvUrl()
	{
		return advUrl;
	}
	
	public void setAdvUrl(String advUrl)
	{
		this.advUrl = advUrl;
	}
	
}
