package com.hxjr.p2p.ad5.bean;

import org.json.JSONException;

import com.dm.utils.DMJsonObject;
import com.hxjr.p2p.ad5.DMApplication;

/**
 * 帐户信息
 * @author jiaohongyun
 *
 */
public class AccountBean
{
	private int id;
	
	/**
	 * 用户昵称
	 */
	private String userName;
	
	/**
	 * 安全等级
	 */
	private int safeLevel;
	
	/**
	 * 照片路径
	 */
	private String photo;
	
	/**
	 * 账户总金额
	 */
	private String totalAmount;
	
	/**
	 * 冻结金额
	 */
	private String freezeAmount;
	
	/**
	 * 账户净余额
	 */
	private String merelyAmount;
	
	/**
	 * 投资资产
	 */
	private String investAmount;
	
	/**
	 * 借款总金额
	 */
	private String loanAmount;
	
	/**
	 * 待还总金额
	 */
	private String alsoAmount;
	
	/**
	 * 已赚总金额
	 */
	private double earnAmount;
	
	/**
	 * 账户余额
	 */
	private String overAmount;
	
	/**
	 * 手机验证状态
	 */
	private boolean mobileVerified;
	
	/**
	 * email验证状态
	 */
	private boolean emailVerified;
	
	/**
	 * 身份证验证状态
	 */
	private boolean idcardVerified;
	
	/**
	 * 是否设置交易密码
	 */
	private boolean withdrawPsw;
	
	/**
	 * 是否托管
	 */
	private boolean tg;
	
	/**
	 * 第三方托管的用户帐号
	 */
	private String usrCustId;
	
	/**
	 * 体验金
	 */
	private double experienceAmount;
	
	/**
	 * 未使用加息券张数
	 */
	private int unUserJxqCount;
	
	/**
	 * 未使用红包金额
	 */
	private String hbJe;
	
	public AccountBean()
	{
	
	}
	
	public AccountBean(DMJsonObject data)
	{
		try
		{
			UserInfo userInfo = DMApplication.getInstance().getUserInfo();
			id = data.getInt("Id");
			userName = data.getString("userName");
			userInfo.setAccountName(userName);
			safeLevel = data.getInt("safeLevel");
			userInfo.setSafeLevel(safeLevel);
			photo = data.getString("photo");
			totalAmount = data.getString("totalAmount");
			freezeAmount = data.getString("freezeAmount");
			merelyAmount = data.getString("merelyAmount");
			investAmount = data.getString("investAmount");
			loanAmount = data.getString("loanAmount");
			alsoAmount = data.getString("alsoAmount");
			earnAmount = data.getDouble("earnAmount");
			overAmount = data.getString("overAmount");
			mobileVerified = data.getBoolean("mobileVerified");
			emailVerified = data.getBoolean("emailVerified");
			idcardVerified = data.getBoolean("idcardVerified");
			//			userInfo.setMobileVerified(mobileVerified);
			//			userInfo.setEmailVerified(emailVerified);
			//			userInfo.setIdcardVerified(idcardVerified);
			withdrawPsw = data.getBoolean("withdrawPsw");
			userInfo.setWithdrawPsw(withdrawPsw);
			tg = data.getBoolean("tg");
			userInfo.setTg(tg);
			usrCustId = data.getString("usrCustId");
			userInfo.setUsrCustId(usrCustId);
			experienceAmount = data.getDouble("experienceAmount");
			unUserJxqCount = data.getInt("unUserJxqCount");
			hbJe = data.getString("hbJe");
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
	
	public String getUserName()
	{
		return userName;
	}
	
	public void setUserName(String userName)
	{
		this.userName = userName;
	}
	
	public int getSafeLevel()
	{
		return safeLevel;
	}
	
	public void setSafeLevel(int safeLevel)
	{
		this.safeLevel = safeLevel;
	}
	
	public String getPhoto()
	{
		return photo;
	}
	
	public void setPhoto(String photo)
	{
		this.photo = photo;
	}
	
	public String getTotalAmount()
	{
		return totalAmount;
	}
	
	public void setTotalAmount(String totalAmount)
	{
		this.totalAmount = totalAmount;
	}
	
	public String getFreezeAmount()
	{
		return freezeAmount;
	}
	
	public void setFreezeAmount(String freezeAmount)
	{
		this.freezeAmount = freezeAmount;
	}
	
	public String getMerelyAmount()
	{
		return merelyAmount;
	}
	
	public void setMerelyAmount(String merelyAmount)
	{
		this.merelyAmount = merelyAmount;
	}
	
	public String getInvestAmount()
	{
		return investAmount;
	}
	
	public void setInvestAmount(String investAmount)
	{
		this.investAmount = investAmount;
	}
	
	public String getLoanAmount()
	{
		return loanAmount;
	}
	
	public void setLoanAmount(String loanAmount)
	{
		this.loanAmount = loanAmount;
	}
	
	public String getAlsoAmount()
	{
		return alsoAmount;
	}
	
	public void setAlsoAmount(String alsoAmount)
	{
		this.alsoAmount = alsoAmount;
	}
	
	public double getEarnAmount()
	{
		return earnAmount;
	}
	
	public void setEarnAmount(double earnAmount)
	{
		this.earnAmount = earnAmount;
	}
	
	public String getOverAmount()
	{
		return overAmount;
	}
	
	public void setOverAmount(String overAmount)
	{
		this.overAmount = overAmount;
	}
	
	public boolean isMobileVerified()
	{
		return mobileVerified;
	}
	
	public void setMobileVerified(boolean mobileVerified)
	{
		this.mobileVerified = mobileVerified;
	}
	
	public boolean isEmailVerified()
	{
		return emailVerified;
	}
	
	public void setEmailVerified(boolean emailVerified)
	{
		this.emailVerified = emailVerified;
	}
	
	public boolean isIdcardVerified()
	{
		return idcardVerified;
	}
	
	public void setIdcardVerified(boolean idcardVerified)
	{
		this.idcardVerified = idcardVerified;
	}
	
	public boolean isWithdrawPsw()
	{
		return withdrawPsw;
	}
	
	public void setWithdrawPsw(boolean withdrawPsw)
	{
		this.withdrawPsw = withdrawPsw;
	}
	
	public boolean isTg()
	{
		return tg;
	}
	
	public void setTg(boolean tg)
	{
		this.tg = tg;
	}
	
	public String getUsrCustId()
	{
		return usrCustId;
	}
	
	public void setUsrCustId(String usrCustId)
	{
		this.usrCustId = usrCustId;
	}
	
	/**
	 * @return 返回 experienceAmount
	 */
	public double getExperienceAmount()
	{
		return experienceAmount;
	}
	
	/**
	 * @param 对experienceAmount进行赋值
	 */
	public void setExperienceAmount(double experienceAmount)
	{
		this.experienceAmount = experienceAmount;
	}
	
	/**
	 * @return 返回 unUserJxqCount
	 */
	public int getUnUserJxqCount()
	{
		return unUserJxqCount;
	}
	
	/**
	 * @param 对unUserJxqCount进行赋值
	 */
	public void setUnUserJxqCount(int unUserJxqCount)
	{
		this.unUserJxqCount = unUserJxqCount;
	}
	
	/**
	 * @return 返回 hbJe
	 */
	public String getHbJe()
	{
		return hbJe;
	}
	
	/**
	 * @param 对hbJe进行赋值
	 */
	public void setHbJe(String hbJe)
	{
		this.hbJe = hbJe;
	}
	
}
