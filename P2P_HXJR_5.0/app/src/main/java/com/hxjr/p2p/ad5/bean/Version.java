package com.hxjr.p2p.ad5.bean;

import org.json.JSONException;

import com.dm.utils.DMJsonObject;

/**
 * 版本
 * @author  jiaohongyun
 * @date 2015年7月24日
 */
public class Version
{
	/**
	 * 类型
	 */
	private String type;
	
	/**
	 * 版本号
	 */
	private int versionId;
	
	/**
	 * 版本名称
	 */
	private String versionName;
	
	/**
	 * 是否更新 yes/no
	 */
	private String ifUpdate;
	
	/**
	 * 升级描述
	 */
	private String descr;
	
	/**
	 * 是否使用 yes/no
	 */
	private String isUsed;
	
	/**
	 * 发布时间
	 */
	private String publishTime;
	
	/**
	 * 发布者
	 */
	private String publisher;
	
	/**
	 * 修改时间
	 */
	private String modifyTime;
	
	/**
	 * 修改者
	 */
	private String modifyBy;
	
	/**
	 * 网络URL地址
	 */
	private String url;
	
	/** 
	 */
	public Version()
	{
	}

	public Version(DMJsonObject data)
	{
		try
		{
			type  = data.getString("type");
			versionId = data.getInt("versionId");
			versionName = data.getString("versionName");
			ifUpdate = data.getString("ifUpdate");
			descr = data.getString("descr");
			isUsed = data.getString("isUsed");
			publishTime = data.getString("publishTime");
			publisher = data.getString("publisher");
			modifyTime = data.getString("modifyTime");
			modifyBy = data.getString("modifyBy");
			url = data.getString("url");
		}
		catch (JSONException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
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
	 * @return 返回 versionId
	 */
	public int getVersionId()
	{
		return versionId;
	}
	
	/**
	 * @param 对versionId进行赋值
	 */
	public void setVersionId(int versionId)
	{
		this.versionId = versionId;
	}
	
	/**
	 * @return 返回 versionName
	 */
	public String getVersionName()
	{
		return versionName;
	}
	
	/**
	 * @param 对versionName进行赋值
	 */
	public void setVersionName(String versionName)
	{
		this.versionName = versionName;
	}
	
	/**
	 * @return 返回 ifUpdate
	 */
	public String getIfUpdate()
	{
		return ifUpdate;
	}
	
	/**
	 * @param 对ifUpdate进行赋值
	 */
	public void setIfUpdate(String ifUpdate)
	{
		this.ifUpdate = ifUpdate;
	}
	
	/**
	 * @return 返回 descr
	 */
	public String getDescr()
	{
		return descr;
	}
	
	/**
	 * @param 对descr进行赋值
	 */
	public void setDescr(String descr)
	{
		this.descr = descr;
	}
	
	/**
	 * @return 返回 isUsed
	 */
	public String getIsUsed()
	{
		return isUsed;
	}
	
	/**
	 * @param 对isUsed进行赋值
	 */
	public void setIsUsed(String isUsed)
	{
		this.isUsed = isUsed;
	}
	
	/**
	 * @return 返回 publishTime
	 */
	public String getPublishTime()
	{
		return publishTime;
	}
	
	/**
	 * @param 对publishTime进行赋值
	 */
	public void setPublishTime(String publishTime)
	{
		this.publishTime = publishTime;
	}
	
	/**
	 * @return 返回 publisher
	 */
	public String getPublisher()
	{
		return publisher;
	}
	
	/**
	 * @param 对publisher进行赋值
	 */
	public void setPublisher(String publisher)
	{
		this.publisher = publisher;
	}
	
	/**
	 * @return 返回 modifyTime
	 */
	public String getModifyTime()
	{
		return modifyTime;
	}
	
	/**
	 * @param 对modifyTime进行赋值
	 */
	public void setModifyTime(String modifyTime)
	{
		this.modifyTime = modifyTime;
	}
	
	/**
	 * @return 返回 modifyBy
	 */
	public String getModifyBy()
	{
		return modifyBy;
	}
	
	/**
	 * @param 对modifyBy进行赋值
	 */
	public void setModifyBy(String modifyBy)
	{
		this.modifyBy = modifyBy;
	}
	
	/**
	 * @return 返回 url
	 */
	public String getUrl()
	{
		return url;
	}
	
	/**
	 * @param 对url进行赋值
	 */
	public void setUrl(String url)
	{
		this.url = url;
	}
}
