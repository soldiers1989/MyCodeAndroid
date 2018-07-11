package com.hxjr.p2p.ad5.bean;

import com.dm.utils.DMJsonObject;

import org.json.JSONException;

import java.io.Serializable;

/**
 * 银行卡
 * @author  jiaohongyun
 * @version  [版本号, 2015年1月23日]
 */
public class BankCard implements Serializable
{
	/**
	 * 注释内容
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 数据库编号
	 */
	private int id;
	
	/**
	 * 用户id
	 */
	private int account;
	
	/**
	 * 开户城市id
	 */
	private int cityId;
	
	/**
	 * 银行名称
	 */
	private String bankname;

	public String getBankUnionCode() {
		return bankUnionCode;
	}

	public void setBankUnionCode(String bankUnionCode) {
		this.bankUnionCode = bankUnionCode;
	}

	/**
	 * 银联号
	 */
	private String bankUnionCode;

	/**
	 * 银行id
	 */
	private int bankID;
	
	/**
	 * 银行卡号
	 */
	private String bankNumber;
	
	private boolean isSelected;
	
	public BankCard()
	{
	
	}

	@Override
	public String toString() {
		return "BankCard{" + "id=" + id +
		", account=" + account +
		", cityId=" + cityId +
		", bankname='" + bankname + '\'' +
		", bankUnionCode='" + bankUnionCode + '\'' +
		", bankID=" + bankID +
		", bankNumber='" + bankNumber + '\'' +
		", isSelected=" + isSelected +
		'}';
}

	public BankCard(DMJsonObject data)
	{
		try
		{
			id = data.getInt("id");
			account = data.getInt("account");
			cityId = data.getInt("cityId");
			bankname = data.getString("bankname");
			bankID = data.getInt("bankID");
			bankNumber = data.getString("bankNumber");
			bankUnionCode=data.getString("bankUnionCode");
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
	
	public int getAccount()
	{
		return account;
	}
	
	public void setAccount(int account)
	{
		this.account = account;
	}
	
	public int getCityId()
	{
		return cityId;
	}
	
	public void setCityId(int cityId)
	{
		this.cityId = cityId;
	}
	
	public String getBankname()
	{
		return bankname;
	}
	
	public void setBankname(String bankname)
	{
		this.bankname = bankname;
	}
	
	public int getBankID()
	{
		return bankID;
	}
	
	public void setBankID(int bankID)
	{
		this.bankID = bankID;
	}

	public String getBankNumber()
	{
		return bankNumber;
	}
	
	public void setBankNumber(String bankNumber)
	{
		this.bankNumber = bankNumber;
	}
	
	/**
	 * 获取简写
	 * @return
	 */
	public String toShortStr()
	{
		int length = bankNumber.length();
		String str = bankname + " 尾号" + getBankNumber().substring(length - 4, length);
		return str;
	}
	
	/**
	 * 尾巴kg
	 * @return
	 */
	public String getWeiHao()
	{
		int length = bankNumber.length();
		String str = "尾号" + getBankNumber().substring(length - 4, length);
		return str;
	}
	public boolean isSelected()
	{
		return isSelected;
	}
	
	public void setSelected(boolean isSelected)
	{
		this.isSelected = isSelected;
	}


}
