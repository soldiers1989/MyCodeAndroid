package com.dm.widgets.bannerviewpager;

/**
 * 描述：广告信息</br>
 */
public class AdvertisementInfo
{
	
	private String id = "";
	
	private String title = "";
	
	private String imgUrl = "";
	
	private String linkUrl = "";
	
	public String getId()
	{
		return id;
	}
	
	public void setId(String id)
	{
		this.id = id;
	}
	
	public String getImgUrl()
	{
		return imgUrl;
	}
	
	public void setImgUrl(String imgUrl)
	{
		this.imgUrl = imgUrl;
	}
	
	public String getTitle()
	{
		return title;
	}
	
	public void setTitle(String title)
	{
		this.title = title;
	}
	
	public String getLinkUrl()
	{
		return linkUrl;
	}
	
	public void setLinkUrl(String linkUrl)
	{
		if (linkUrl.endsWith("/"))
		{
			int length = linkUrl.length() - 1;
			linkUrl = linkUrl.substring(0, length);
		}
		this.linkUrl = linkUrl;
	}
	
}
