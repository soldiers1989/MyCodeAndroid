/*
 * 文 件 名:  HyzxBean.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  jiaohongyun
 * 修改时间:  2015年3月16日
 */
package com.hxjr.p2p.ad5.bean;

import org.json.JSONException;

import com.dm.utils.DMJsonObject;

/**
 * 行业资讯
 * @author  jiaohongyun
 * @version  [版本号, 2015年3月16日]
 */
public class HyzxBean
{
	private int id;
	
	private String title;
	
	private String releaseTime;
	
	private String desc;
	
	public HyzxBean()
	{
	
	}
	
	public HyzxBean(DMJsonObject data)
	{
		try
		{
			id = data.getInt("id");
			title = data.getString("title");
			releaseTime = data.getString("releaseTime");
			desc = data.getString("desc");
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
	
	public String getReleaseTime()
	{
		return releaseTime;
	}
	
	public void setReleaseTime(String releaseTime)
	{
		this.releaseTime = releaseTime;
	}
	
	public String getDesc()
	{
		return desc;
	}
	
	public void setDesc(String desc)
	{
		this.desc = desc;
	}
	
}
