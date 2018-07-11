package com.hxjr.p2p.ad5.bean;

import org.json.JSONException;

import com.dm.utils.DMJsonObject;
import com.hxjr.p2p.ad5.utils.FormatUtil;

/**
 * 我的投资 - 回款中
 * @author  huangkaibo
 * @date 2015-11-27
 */
public class MyInvestRepaymentIn
{
	// 标ID
	private int bidId;
	
	// 债权编码
	private String creditorId;

	// 债权ID
	private int zqId;

	// 标的标题
	private String bidName;
	
	// 购买价格
	private double gmjg;
	
	// 原始债权价值
	private double sourceZqPrice;
	
	// 年化利率
	private String nhl;
	
	// 加息利率
	private double jxl;
	
	// 待收本息
	private double dsbx;
	
	// 剩余期数
	private int syqs;
	
	// 还款期数
	private int hkqs;
	
	// 下个还款日
	private String xghkr;
	
	// 状态
	private String status;
	
	// 借款周期
	private int jkzq;
	
	// 进度
	private double process;
	
	// 剩余时间
	private String surTime;
	
	// 借款周期是否为天
	private boolean isDay = false;
	
	// 已赚金额
	private double yzje;
	
	// 结算时间
	private String jqsj;
	
	private double transferRate;
	
	//是否可以转让
	private boolean isCanTrans;
	
	//是否正在转让
	private String isTransfered;
	
	public String isTransfered()
	{
		return isTransfered;
	}
	
	public void setTransfered(String isTransfered)
	{
		this.isTransfered = isTransfered;
	}
	
	public boolean isCanTrans()
	{
		return isCanTrans;
	}
	
	public void setCanTrans(boolean isCanTrans)
	{
		this.isCanTrans = isCanTrans;
	}
	
	public MyInvestRepaymentIn()
	{
	}
	
	public MyInvestRepaymentIn(DMJsonObject data)
	{
		try
		{
			bidId = data.getInt("bidId");
			creditorId = data.getString("creditorId");
			zqId = data.getInt("zqId");
			bidName = data.getString("bidName");
			gmjg = data.getDouble("gmjg");
			sourceZqPrice = data.getDouble("sourceZqPrice");
			nhl = data.getString("nhl");
			jxl = data.getDouble("jxl");
			dsbx = data.getDouble("dsbx");
			syqs = data.getInt("syqs");
			hkqs = data.getInt("hkqs");
			xghkr = data.getString("xghkr");
			status = data.getString("status");
			jkzq = data.getInt("jkzq");
			process = data.getDouble("process");
			surTime = data.getString("surTime");
			isDay = data.getBoolean("isDay");
			isCanTrans = data.getBoolean("isCanTrans");
			isTransfered = data.getString("isTransfered");
			yzje = data.getDouble("yzje");
			jqsj = data.getString("jqsj");
			transferRate = data.getDouble("transferRate");
		}
		catch (JSONException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * @return 返回 bidId
	 */
	public int getBidId()
	{
		return bidId;
	}
	
	/**
	 * @param 对bidId进行赋值
	 */
	public void setBidId(int bidId)
	{
		this.bidId = bidId;
	}
	
	/**
	 * @return 返回 creditorId
	 */
	public String getCreditorId()
	{
		return creditorId;
	}
	
	/**
	 * @param 对creditorId进行赋值
	 */
	public void setCreditorId(String creditorId)
	{
		this.creditorId = creditorId;
	}
	
	/**
	 * @return 返回 zqId
	 */
	public int getZqId()
	{
		return zqId;
	}
	
	/**
	 * @param 对zqId进行赋值
	 */
	public void setZqId(int zqId)
	{
		this.zqId = zqId;
	}
	
	/**
	 * @return 返回 bidName
	 */
	public String getBidName()
	{
		return bidName;
	}
	
	/**
	 * @param 对bidName进行赋值
	 */
	public void setBidName(String bidName)
	{
		this.bidName = bidName;
	}
	
	/**
	 * @return 返回 gmjg
	 */
	public double getGmjg()
	{
		return gmjg;
	}
	
	/**
	 * @param 对gmjg进行赋值
	 */
	public void setGmjg(double gmjg)
	{
		this.gmjg = gmjg;
	}
	
	/**
	 * @return 返回 sourceZqPrice
	 */
	public double getSourceZqPrice()
	{
		return sourceZqPrice;
	}
	
	/**
	 * @param 对sourceZqPrice进行赋值
	 */
	public void setSourceZqPrice(double sourceZqPrice)
	{
		this.sourceZqPrice = sourceZqPrice;
	}
	
	/**
	 * @return 返回 nhl
	 */
	public String getNhl()
	{
		double temp = Double.valueOf(nhl);
		return FormatUtil.formatStr2("" + temp);
	}
	
	/**
	 * @param 对nhl进行赋值
	 */
	public void setNhl(String nhl)
	{
		this.nhl = nhl;
	}
	
	/**
	 * @return 返回 jxl
	 */
	public double getJxl()
	{
		return jxl;
	}
	
	/**
	 * @param 对jxl进行赋值
	 */
	public void setJxl(double jxl)
	{
		this.jxl = jxl;
	}
	
	/**
	 * @return 返回 dsbx
	 */
	public double getDsbx()
	{
		return dsbx;
	}
	
	/**
	 * @param 对dsbx进行赋值
	 */
	public void setDsbx(double dsbx)
	{
		this.dsbx = dsbx;
	}
	
	/**
	 * @return 返回 syqs
	 */
	public int getSyqs()
	{
		return syqs;
	}
	
	/**
	 * @param 对syqs进行赋值
	 */
	public void setSyqs(int syqs)
	{
		this.syqs = syqs;
	}
	
	/**
	 * @return 返回 hkqs
	 */
	public int getHkqs()
	{
		return hkqs;
	}
	
	/**
	 * @param 对hkqs进行赋值
	 */
	public void setHkqs(int hkqs)
	{
		this.hkqs = hkqs;
	}
	
	/**
	 * @return 返回 xghkr
	 */
	public String getXghkr()
	{
		return xghkr;
	}
	
	/**
	 * @param 对xghkr进行赋值
	 */
	public void setXghkr(String xghkr)
	{
		this.xghkr = xghkr;
	}
	
	/**
	 * @return 返回 status
	 */
	public String getStatus()
	{
		return status;
	}
	
	/**
	 * @param 对status进行赋值
	 */
	public void setStatus(String status)
	{
		this.status = status;
	}
	
	/**
	 * @return 返回 jkzq
	 */
	public int getJkzq()
	{
		return jkzq;
	}
	
	/**
	 * @param 对jkzq进行赋值
	 */
	public void setJkzq(int jkzq)
	{
		this.jkzq = jkzq;
	}
	
	/**
	 * @return 返回 process
	 */
	public double getProcess()
	{
		return process;
	}
	
	/**
	 * @param 对process进行赋值
	 */
	public void setProcess(double process)
	{
		this.process = process;
	}
	
	/**
	 * @return 返回 surTime
	 */
	public String getSurTime()
	{
		return surTime;
	}
	
	/**
	 * @param 对surTime进行赋值
	 */
	public void setSurTime(String surTime)
	{
		this.surTime = surTime;
	}
	
	/**
	 * @return 返回 isDay
	 */
	public boolean isDay()
	{
		return isDay;
	}
	
	/**
	 * @param 对isDay进行赋值
	 */
	public void setDay(boolean isDay)
	{
		this.isDay = isDay;
	}
	
	/**
	 * @return 返回 yzje
	 */
	public double getYzje()
	{
		return yzje;
	}
	
	/**
	 * @param 对yzje进行赋值
	 */
	public void setYzje(double yzje)
	{
		this.yzje = yzje;
	}
	
	/**
	 * @return 返回 jqsj
	 */
	public String getJqsj()
	{
		return jqsj;
	}
	
	/**
	 * @param 对jqsj进行赋值
	 */
	public void setJqsj(String jqsj)
	{
		this.jqsj = jqsj;
	}
	
	/**
	 * @return 返回 transferRate
	 */
	public double getTransferRate()
	{
		return transferRate;
	}
	
	/**
	 * @param 对transferRate进行赋值
	 */
	public void setTransferRate(double transferRate)
	{
		this.transferRate = transferRate;
	}
	
}