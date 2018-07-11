/*
 * 文 件 名:  DYSBean.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  jiaohongyun
 * 修改时间:  2014年12月8日
 */
package com.hxjr.p2p.ad5.bean;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import com.dm.utils.DMJsonObject;

/**
 * 抵押物
 * @author  jiaohongyun
 * @version  [版本号, 2014年12月8日]
 */
public class DYSBean
{
	/**
	 * 抵押物名称
	 */
	private String dyName;
	
	/**
	 * 抵押物属性
	 */
	private List<DYSXSBean> dysxs;
	
	/** <默认构造函数>
	 */
	public DYSBean(DMJsonObject data)
	{
		try
		{
			dyName = data.getString("dyName");
			JSONArray myArray = data.getJSONArray("dysxs");
			List<DYSXSBean> list = new ArrayList<DYSXSBean>();
			for (int i = 0; i < myArray.length(); i++)
			{
				DMJsonObject item = new DMJsonObject(myArray.getJSONObject(i).toString());
				DYSXSBean dysBean = new DYSXSBean(item);
				list.add(dysBean);
			}
			dysxs = list;
		}
		catch (JSONException e)
		{
			e.printStackTrace();
		}
	}
	
	public String getDyName()
	{
		return dyName;
	}
	
	public void setDyName(String dyName)
	{
		this.dyName = dyName;
	}
	
	public List<DYSXSBean> getDysxs()
	{
		return dysxs;
	}
	
	public void setDysxs(List<DYSXSBean> dysxs)
	{
		this.dysxs = dysxs;
	}
	
}
