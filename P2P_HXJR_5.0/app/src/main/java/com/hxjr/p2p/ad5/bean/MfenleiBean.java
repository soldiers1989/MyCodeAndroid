package com.hxjr.p2p.ad5.bean;

import org.json.JSONException;

import com.dm.utils.DMJsonObject;

public class MfenleiBean {
	private String id;
	private String name;
	private String icon;
	private String maxInterestRates;
	private String minInterestRates;
	private String maxLoanLife;
	private String minLoanLife;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getMaxInterestRates() {
		return maxInterestRates;
	}

	public void setMaxInterestRates(String maxInterestRates) {
		this.maxInterestRates = maxInterestRates;
	}

	public String getMinInterestRates() {
		return minInterestRates;
	}

	public void setMinInterestRates(String minInterestRates) {
		this.minInterestRates = minInterestRates;
	}

	public String getMaxLoanLife() {
		return maxLoanLife;
	}

	public void setMaxLoanLife(String maxLoanLife) {
		this.maxLoanLife = maxLoanLife;
	}

	public String getMinLoanLife() {
		return minLoanLife;
	}

	public void setMinLoanLife(String minLoanLife) {
		this.minLoanLife = minLoanLife;
	}
	public MfenleiBean(DMJsonObject data)
	{
		try
		{
			id = data.getString("id");
			name = data.getString("name");
			icon	 = data.getString("icon");
			maxInterestRates  = data.getString("maxInterestRates");
			minInterestRates  = data.getString("minInterestRates");
			maxLoanLife  = data.getString("maxLoanLife");
			minLoanLife  = data.getString("minLoanLife");
		}
		catch (JSONException e)
		{
			e.printStackTrace();
		}
	}
}
