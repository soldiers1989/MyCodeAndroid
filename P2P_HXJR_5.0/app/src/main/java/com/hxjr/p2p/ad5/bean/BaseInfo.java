package com.hxjr.p2p.ad5.bean;

import org.json.JSONException;

import com.dm.utils.DMJsonObject;

/**
 * Fee中的
 * @author jiaohongyun
 *
 */
public class BaseInfo
{
	private boolean isNeedEmailRZ;
	
	private int maxBankCardNum;
	
	public BaseInfo(DMJsonObject data)
	{
		try
		{
			isNeedEmailRZ = data.getBoolean("isNeedEmailRZ");
			maxBankCardNum = data.getInt("maxBankCardNum");
		}
		catch (JSONException e)
		{
			e.printStackTrace();
		}
	}
	
	public boolean isNeedEmailRZ()
	{
		return isNeedEmailRZ;
	}
	
	public void setNeedEmailRZ(boolean isNeedEmailRZ)
	{
		this.isNeedEmailRZ = isNeedEmailRZ;
	}
	
	public int getMaxBankCardNum()
	{
		return maxBankCardNum;
	}
	
	public void setMaxBankCardNum(int maxBankCardNum)
	{
		this.maxBankCardNum = maxBankCardNum;
	}
	
	
}
