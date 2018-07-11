/*
 * 文 件 名:  DYSXSBean.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  jiaohongyun
 * 修改时间:  2014年12月8日
 */
package com.hxjr.p2p.ad5.bean;

import org.json.JSONException;

import com.dm.utils.DMJsonObject;

/**
 * 抵押物信息
 * @author  jiaohongyun
 * @version  [版本号, 2014年12月8日]
 */
public class DYSXSBean
{
	/**
	 * 抵押物属性值
	 */
	private String dxsxVal;
	
	/**
	 * 抵押物属性名称
	 */
	private String dxsxName;
	
	/** <默认构造函数>
	 */
	public DYSXSBean(DMJsonObject data)
	{
		try
		{
			dxsxVal = data.getString("dxsxVal");
			dxsxName = data.getString("dxsxName");
		}
		catch (JSONException e)
		{
			e.printStackTrace();
		}
	}
	
	public String getDxsxVal()
	{
		return dxsxVal;
	}
	
	public void setDxsxVal(String dxsxVal)
	{
		this.dxsxVal = dxsxVal;
	}
	
	public String getDxsxName()
	{
		return dxsxName;
	}
	
	public void setDxsxName(String dxsxName)
	{
		this.dxsxName = dxsxName;
	}
	
}
