package com.hxjr.p2p.ad5.bean;

import org.json.JSONException;

import com.dm.utils.DMJsonObject;

/**
 * 用户信息
 * 
 * @author jiaohongyun
 *
 */
public class UserInfo
{
	/**
	 * 安全等级
	 */
	private int safeLevel;

	/**
	 * 注册时间时间戳
	 */
	private String registerTime;

	/**
	 * 是否已投体验标
	 */
	private String isExperienceBid;

	/**
	 * 手机号码
	 */
	private String phone;
	
	/**
	 * 身份证
	 */
	private String idCard;
	
	/**
	 * 是否通过身份认证
	 */
	private boolean idcardVerified;

	/**
	 * 是否通过身份认证反面
	 */
	private String idcardInverseVerified;

	/**
	 * 是否通过身份认证正面
	 */
	private String idcardFrontVerified;

	
	public String getIdcardInverseVerified() {
		return idcardInverseVerified;
	}

	public void setIdcardInverseVerified(String idcardInverseVerified) {
		this.idcardInverseVerified = idcardInverseVerified;
	}

	public String getIdcardFrontVerified() {
		return idcardFrontVerified;
	}

	public void setIdcardFrontVerified(String idcardFrontVerified) {
		this.idcardFrontVerified = idcardFrontVerified;
	}

	/**
	 * 是否托管
	 */
	private boolean tg;
	
	/**
	 * 是否通过了邮件认证
	 */
	//	private boolean emailVerified;
	
	/**
	 * 用户ID
	 */
	private int id;
	
	/**
	 * 用户名
	 */
	private String accountName;
	
	//	/**
	//	 * 是否有交易密码
	//	 */
	//	private boolean tranPsw;
	
	/**
	 * 是否手机认证
	 */
	//	private boolean mobileVerified;
	
	/**
	 * 第三方支付id（如果为“”或者null就是未注册）
	 */
	private String usrCustId;
	
	/**
	 * 邮箱
	 */
	private String email;
	
	/**
	 * 用户真实姓名
	 */
	private String realName;
	
	/**
	 * 是否设置提现密码
	 */
	private boolean withdrawPsw;
	
	/**
	 * 如果为托管，第三方注册url
	 */
	private String tgRegisUrl;
	
	/**
	 * 未读消息数量
	 */
	private int letterCount;
	
	/**
	 * 银行卡数量
	 */
	private int bankCount;
	
	/**
	 * 登录密码
	 */
	private String loginPwd;
	
	/**
	 * 用户姓名（没有打码的）
	 */
	private String name;

	/**
	 * 用户评估结果（没有打码的）
	 */
	private String assessment;

	public String getAssessment() {
		return assessment;
	}

	public void setAssessment(String assessment) {
		this.assessment = assessment;
	}

	@Override
	public String toString() {
		return "UserInfo [safeLevel=" + safeLevel + ", registerTime=" + registerTime
			+ ", isExperienceBid=" + isExperienceBid + ", phone=" + phone + ", idCard=" + idCard
			+ ", idcardVerified=" + idcardVerified + ", idcardInverseVerified="
			+ idcardInverseVerified + ", idcardFrontVerified=" + idcardFrontVerified + ", tg=" + tg
			+ ", id=" + id + ", accountName=" + accountName + ", usrCustId=" + usrCustId
			+ ", email=" + email + ", realName=" + realName + ", withdrawPsw=" + withdrawPsw
			+ ", tgRegisUrl=" + tgRegisUrl + ", letterCount=" + letterCount + ", bankCount="
			+ bankCount + ", loginPwd=" + loginPwd + ", name=" + name + ", assessment="
			+ assessment + ", phoneNumber=" + phoneNumber + "]";
	}

	public UserInfo() {
		
	}

	private String phoneNumber;

	public UserInfo(DMJsonObject data) {
		try {

			isExperienceBid = data.getString("isExperienceBid");
			registerTime = data.getString("registerTime");
			assessment = data.getString("assessment");
			safeLevel = data.getInt("safeLevel");
			phone = data.getString("phone");
			idCard = data.getString("idCard");
			tg = data.getBoolean("tg");
			idcardVerified = data.getBoolean("idcardVerified");
			idcardInverseVerified = data.getString("idcardInverseVerified");
			idcardFrontVerified = data.getString("idcardFrontVerified");
			// emailVerified = data.getBoolean("emailVerified");
			// mobileVerified = data.getBoolean("mobileVerified");
			id = data.getInt("id");
			accountName = data.getString("accountName");
			//			tranPsw = data.getBoolean("tranPsw");
			usrCustId = data.getString("usrCustId");
			email = data.getString("email");
			realName = data.getString("realName");
			withdrawPsw = data.getBoolean("withdrawPsw");
			tgRegisUrl = data.getString("tgRegisUrl");
			letterCount = data.getInt("letterCount");
			bankCount = data.getInt("bankCount");
			name = data.getString("name");
			phoneNumber = data.getString("phoneNumber");
		}
		catch (JSONException e)
		{
			e.printStackTrace();
		}
	}

	public String getRegisterTime() {
		return registerTime;
	}

	public void setRegisterTime(String registerTime) {
		this.registerTime = registerTime;
	}

	public String getIsExperienceBid() {
		return isExperienceBid;
	}

	public void setIsExperienceBid(String isExperienceBid) {
		this.isExperienceBid = isExperienceBid;
	}

	public int getSafeLevel() {
		return safeLevel;
	}
	
	public void setSafeLevel(int safeLevel)
	{
		this.safeLevel = safeLevel;
	}
	
	public String getPhone()
	{
		return phone;
	}
	
	public void setPhone(String phone)
	{
		this.phone = phone;
	}
	
	public String getIdCard()
	{
		return idCard;
	}
	
	public void setIdCard(String idCard)
	{
		this.idCard = idCard;
	}

	 public boolean getIdcardVerified()
	 {
	 return idcardVerified;
	 }
	
	//	public boolean isIdcardVerified()
	//	{
	//		return idcardVerified;
	//	}	
		public void setIdcardVerified(boolean idcardVerified)
		{
			this.idcardVerified = idcardVerified;
		}
	
	public boolean isTg()
	{
		return tg;
	}
	
	public void setTg(boolean tg)
	{
		this.tg = tg;
	}
	
	//	public boolean isEmailVerified()
	//	{
	//		return emailVerified;
	//	}
	//	
	//	public void setEmailVerified(boolean emailVerified)
	//	{
	//		this.emailVerified = emailVerified;
	//	}
	
	public int getId()
	{
		return id;
	}
	
	public void setId(int id)
	{
		this.id = id;
	}
	
	public String getAccountName()
	{
		return accountName;
	}
	
	public void setAccountName(String accountName)
	{
		this.accountName = accountName;
	}
	
	//	public boolean isTranPsw()
	//	{
	//		return tranPsw;
	//	}
	//	
	//	public void setTranPsw(boolean tranPsw)
	//	{
	//		this.tranPsw = tranPsw;
	//	}
	
	//	public boolean isMobileVerified()
	//	{
	//		return mobileVerified;
	//	}
	
	//	public void setMobileVerified(boolean mobileVerified)
	//	{
	//		this.mobileVerified = mobileVerified;
	//	}
	
	public String getUsrCustId()
	{
		return null == usrCustId ? "" : usrCustId;
	}
	
	public void setUsrCustId(String usrCustId)
	{
		this.usrCustId = usrCustId;
	}
	
	public String getEmail()
	{
		return email;
	}
	
	public void setEmail(String email)
	{
		this.email = email;
	}
	
	public String getRealName()
	{
		return realName;
	}
	
	public void setRealName(String realName)
	{
		this.realName = realName;
	}
	
	public boolean isWithdrawPsw()
	{
		return withdrawPsw;
	}
	
	public void setWithdrawPsw(boolean withdrawPsw)
	{
		this.withdrawPsw = withdrawPsw;
	}
	
	public String getTgRegisUrl()
	{
		return tgRegisUrl;
	}
	
	public void setTgRegisUrl(String tgRegisUrl)
	{
		this.tgRegisUrl = tgRegisUrl;
	}
	
	public int getLetterCount()
	{
		return letterCount;
	}
	
	public void setLetterCount(int letterCount)
	{
		this.letterCount = letterCount;
	}
	
	public int getBankCount()
	{
		return bankCount;
	}
	
	public void setBankCount(int bankCount)
	{
		this.bankCount = bankCount;
	}
	
	public String getLoginPwd()
	{
		return loginPwd;
	}
	
	public void setLoginPwd(String loginPwd)
	{
		this.loginPwd = loginPwd;
	}
	
	public String getName()
	{
		return name;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public String getPhoneNumber()
	{
		return phoneNumber;
	}
	
	public void setPhoneNumber(String phoneNumber)
	{
		this.phoneNumber = phoneNumber;
	}
}
