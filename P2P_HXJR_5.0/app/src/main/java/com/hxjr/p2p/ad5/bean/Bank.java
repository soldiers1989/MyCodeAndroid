/*
 * 文 件 名:  Bank.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  jiaohongyun
 * 修改时间:  2014年11月18日
 */
package com.hxjr.p2p.ad5.bean;

import java.io.Serializable;

import org.json.JSONException;

import com.dm.utils.DMJsonObject;

/**
 * 银行
 * @author  jiaohongyun
 * @version  [版本号, 2014年11月18日]
 */
public class Bank implements Serializable
{
	/**
	 * 注释内容
	 */
	private static final long serialVersionUID = -6637054643217589353L;
	
	/**
	 * id
	 */
	private int id;
	
	/**
	 * 银行名
	 */
	private String bankName;
	
	public Bank(DMJsonObject data)
	{
		try
		{
			id = data.getInt("id");
			bankName = data.getString("bankName");
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
	
	public String getBankName()
	{
		return bankName;
	}
	
	public void setBankName(String bankName)
	{
		this.bankName = bankName;
	}
	
}
