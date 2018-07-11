package com.hxjr.p2p.ad5.bean;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import com.dm.utils.DMJsonObject;
import com.hxjr.p2p.ad5.utils.FormatUtil;

/**
 * 借款方的信息
 * 
 * @author jiaohongyun
 *
 */
public class Enterprise
{
	
	public Enterprise()
	{
	
	}
	
	public Enterprise(DMJsonObject data)
	{
		try
		{
			qyName = data.getString("qyName");
			amount = FormatUtil.formatMoney(data.getDouble("amount"));
			area = data.getString("area");
			rate = data.getString("rate");
			endDate = data.getString("endDate");
			repayDate = data.getString("repayDate");
			bidUse = data.getString("bidUse");
			repaySource = data.getString("repaySource");
			desc = data.getString("desc");
			regYear = data.getString("regYear");
			if (regYear != null && !regYear.isEmpty())
			{
				regYear += "年";
			}
			regAmount = FormatUtil.formatMoney(data.getDouble("regAmount"));
			earnAmount = FormatUtil.formatMoney(data.getDouble("earnAmount"));
			cash = data.getString("cash");
			business = data.getString("business");
			operation = data.getString("operation");
			complaints = data.getString("complaints");
			credit = data.getString("credit");
			
			JSONArray array = data.getJSONArray("qyFinanceList");
			List<QyFinance> list = new ArrayList<QyFinance>();
			for (int i = 0; i < array.length(); i++)
			{
				DMJsonObject item = new DMJsonObject(array.getJSONObject(i).toString());
				QyFinance qyFinance = new QyFinance(item);
				list.add(qyFinance);
			}
			setQyFinanceList(list);
			//抵押物信息
			JSONArray dysArray = data.getJSONArray("dys");
			List<DYSBean> dysList = new ArrayList<DYSBean>();
			for (int i = 0; i < dysArray.length(); i++)
			{
				DMJsonObject item = new DMJsonObject(dysArray.getJSONObject(i).toString());
				DYSBean dysBean = new DYSBean(item);
				dysList.add(dysBean);
			}
			dys = dysList;
			//风控信息
			dbjg = data.getString("dbjg");
			dbdesc = data.getString("dbdesc");
			dbinfo = data.getString("dbinfo");
			fkcs = data.getString("fkcs");
			fdbinfo = data.getString("fdbinfo");
			
		}
		catch (JSONException e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * 担保机构
	 */
	private String dbjg;
	
	/**
	 * 担保机构介绍
	 */
	private String dbdesc;
	
	/**
	 * 担保情况（html）
	 */
	private String dbinfo;
	
	/**
	 * 风险控制措施（html）
	 */
	private String fkcs;
	
	/**
	 * 反担保情况（html）
	 */
	private String fdbinfo;
	
	/**
	 * 抵押物信息
	 */
	private List<DYSBean> dys;
	
	/**
	 * 企业名称
	 */
	private String qyName;
	
	/**
	 * 本次借款金额
	 */
	private String amount;
	
	/**
	 * 区域
	 */
	private String area;
	
	/**
	 * 年利率
	 */
	private String rate;
	
	/**
	 * 投标截止时间
	 */
	private String endDate;
	
	/**
	 * 借款用途
	 */
	private String bidUse;
	
	/**
	 * 还款来源
	 */
	private String repaySource;
	
	/**
	 * 借款详情(html)
	 */
	private String desc;
	
	/**
	 * 企业净资产
	 */
	private String regYear;
	
	/**
	 * 注册金额
	 */
	private String regAmount;
	
	/**
	 * 企业净资产
	 */
	private String earnAmount;
	
	/**
	 * 现金流
	 */
	private String cash;
	
	/**
	 * 行业
	 */
	private String business;
	
	/**
	 * 经营情况
	 */
	private String operation;
	
	/**
	 * 投诉情况
	 */
	private String complaints;
	
	/**
	 * 征信记录
	 */
	private String credit;
	
	/**
	 * 企业财务（企业近几年的财政）
	 */
	private List<QyFinance> qyFinanceList;
	
	/**
	 * 还款日期
	 */
	private String repayDate;
	
	public String getQyName()
	{
		return qyName;
	}
	
	public void setQyName(String qyName)
	{
		this.qyName = qyName;
	}
	
	public String getAmount()
	{
		return amount;
	}
	
	public void setAmount(String amount)
	{
		this.amount = amount;
	}
	
	public String getArea()
	{
		return area;
	}
	
	public void setArea(String area)
	{
		this.area = area;
	}
	
	public String getRate()
	{
		double temp = Double.valueOf(rate) * 100;
		return FormatUtil.formatStr2("" + temp) + "%";
	}
	
	public void setRate(String rate)
	{
		this.rate = rate;
	}
	
	public String getEndDate()
	{
		return endDate;
	}
	
	public void setEndDate(String endDate)
	{
		this.endDate = endDate;
	}
	
	public String getBidUse()
	{
		return bidUse;
	}
	
	public void setBidUse(String bidUse)
	{
		this.bidUse = bidUse;
	}
	
	public String getRepaySource()
	{
		return repaySource;
	}
	
	public void setRepaySource(String repaySource)
	{
		this.repaySource = repaySource;
	}
	
	public String getDesc()
	{
		return desc;
	}
	
	public void setDesc(String desc)
	{
		this.desc = desc;
	}
	
	public String getRegYear()
	{
		return regYear;
	}
	
	public void setRegYear(String regYear)
	{
		this.regYear = regYear;
	}
	
	public String getRegAmount()
	{
		return regAmount;
	}
	
	public void setRegAmount(String regAmount)
	{
		this.regAmount = regAmount;
	}
	
	public String getEarnAmount()
	{
		return earnAmount;
	}
	
	public void setEarnAmount(String earnAmount)
	{
		this.earnAmount = earnAmount;
	}
	
	public String getCash()
	{
		return cash;
	}
	
	public void setCash(String cash)
	{
		this.cash = cash;
	}
	
	public String getBusiness()
	{
		return business;
	}
	
	public void setBusiness(String business)
	{
		this.business = business;
	}
	
	public String getOperation()
	{
		return operation;
	}
	
	public void setOperation(String operation)
	{
		this.operation = operation;
	}
	
	public String getComplaints()
	{
		return complaints;
	}
	
	public void setComplaints(String complaints)
	{
		this.complaints = complaints;
	}
	
	public String getCredit()
	{
		return credit;
	}
	
	public void setCredit(String credit)
	{
		this.credit = credit;
	}
	
	public List<QyFinance> getQyFinanceList()
	{
		return qyFinanceList;
	}
	
	public void setQyFinanceList(List<QyFinance> qyFinanceList)
	{
		this.qyFinanceList = qyFinanceList;
	}
	
	public List<DYSBean> getDys()
	{
		return dys;
	}
	
	public void setDys(List<DYSBean> dys)
	{
		this.dys = dys;
	}
	
	public String getDbjg()
	{
		return dbjg;
	}
	
	public void setDbjg(String dbjg)
	{
		this.dbjg = dbjg;
	}
	
	public String getDbdesc()
	{
		return dbdesc;
	}
	
	public void setDbdesc(String dbdesc)
	{
		this.dbdesc = dbdesc;
	}
	
	public String getDbinfo()
	{
		return dbinfo;
	}
	
	public void setDbinfo(String dbinfo)
	{
		this.dbinfo = dbinfo;
	}
	
	public String getFkcs()
	{
		return fkcs;
	}
	
	public void setFkcs(String fkcs)
	{
		this.fkcs = fkcs;
	}
	
	public String getFdbinfo()
	{
		return fdbinfo;
	}
	
	public void setFdbinfo(String fdbinfo)
	{
		this.fdbinfo = fdbinfo;
	}
	
	public String getRepayDate()
	{
		return repayDate;
	}
	
	public void setRepayDate(String repayDate)
	{
		this.repayDate = repayDate;
	}
	
}
