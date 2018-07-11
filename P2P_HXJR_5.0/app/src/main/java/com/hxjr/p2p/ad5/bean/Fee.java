package com.hxjr.p2p.ad5.bean;

import java.io.Serializable;

import org.json.JSONException;

import android.text.TextUtils;

import com.dm.utils.DMJsonObject;

/**
 * 充值、提现及其它相关设置
 * @author jiaohongyun
 *
 */
public class Fee implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4343229748149230054L;
	
	private Chargep chargep;
	
	private Withdrawp withdrawp;
	
	private BaseInfo baseInfo;
	
	private SQFee sqFee;
	
	private YbFee ybFee;
	
	public Fee(DMJsonObject data)
	{
		try
		{
			String chargepStr = data.getString("chargep");
			String withdrawpStr = data.getString("withdrawp");
			String baseInfoStr = data.getString("baseInfo");
			String sqFeeStr = data.getString("sqFee");
			String ybFeeStr = data.getString("ybFee");
			if (!TextUtils.isEmpty(chargepStr))
			{
				chargep = new Chargep(new DMJsonObject(chargepStr));
			}
			if (!TextUtils.isEmpty(withdrawpStr))
			{
				withdrawp = new Withdrawp(new DMJsonObject(withdrawpStr));
			}
			if (!TextUtils.isEmpty(baseInfoStr))
			{
				baseInfo = new BaseInfo(new DMJsonObject(baseInfoStr));
			}
			if (!TextUtils.isEmpty(sqFeeStr))
			{
				sqFee = new SQFee(new DMJsonObject(data.getString("sqFee")));
			}
			if(!TextUtils.isEmpty(ybFeeStr))
			{
				ybFee = new YbFee(new DMJsonObject(data.getString("ybFee")));
			}
		}
		catch (JSONException e)
		{
			e.printStackTrace();
		}
	}
	
	public Chargep getChargep()
	{
		return chargep;
	}
	
	public void setChargep(Chargep chargep)
	{
		this.chargep = chargep;
	}
	
	public Withdrawp getWithdrawp()
	{
		return withdrawp;
	}
	
	public void setWithdrawp(Withdrawp withdrawp)
	{
		this.withdrawp = withdrawp;
	}
	
	public BaseInfo getBaseInfo()
	{
		return baseInfo;
	}
	
	public void setBaseInfo(BaseInfo baseInfo)
	{
		this.baseInfo = baseInfo;
	}
	
	public SQFee getSqFee()
	{
		return sqFee;
	}
	
	public void setSqFee(SQFee sqFee)
	{
		this.sqFee = sqFee;
	}

	/**
	 * @return 返回 ybFee
	 */
	public YbFee getYbFee()
	{
		return ybFee;
	}

	/**
	 * @param 对ybFee进行赋值
	 */
	public void setYbFee(YbFee ybFee)
	{
		this.ybFee = ybFee;
	}
	
	
	
}
