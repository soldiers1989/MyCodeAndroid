/*
 * 文 件 名:  WzggBean.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  jiaohongyun
 * 修改时间:  2015年3月17日
 */
package com.hxjr.p2p.ad5.bean;

import org.json.JSONException;

import com.dm.utils.DMJsonObject;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  jiaohongyun
 * @version  [版本号, 2015年3月17日]
 */
public class WzggBean
{
	private int id;
	
	private String title;
	
	private String releaseTime;
	
	//    private String desc;
	
	private String type;
	
	public WzggBean(DMJsonObject data)
	{
		try
		{
			id = data.getInt("id");
			title = data.getString("title");
			releaseTime = data.getString("releaseTime");
			//            desc = data.getString("desc");
			type = data.getString("type");
		}
		catch (JSONException e)
		{
			e.printStackTrace();
		}
	}
	
	public WzggBean()
	{
		super();
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
	
	//    public String getDesc()
	//    {
	//        return desc;
	//    }
	//    
	//    public void setDesc(String desc)
	//    {
	//        this.desc = desc;
	//    }
	
	public String getType()
	{
		return type;
	}
	
	public void setType(String type)
	{
		this.type = type;
	}
	
}
