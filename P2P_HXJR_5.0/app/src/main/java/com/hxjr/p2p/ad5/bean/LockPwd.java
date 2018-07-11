package com.hxjr.p2p.ad5.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 手势密码
 * @author  jiaohongyun
 * @date 2015年5月26日
 */
public class LockPwd implements Parcelable
{
	
	private int id;
	
	/**
	 * 用户ID
	 */
	private int userId;
	
	/**
	 * 手势密码
	 */
	private String pwd;
	
	/**
	 * 登录帐号
	 */
	private String account;
	
	/**
	 * 登录密码
	 */
	private String loginPwd;
	
	/***
	 * 是否已经设置了手势密码
	 */
	private boolean hasSetLockPwd;
	
	/***
	 * 是否使用手势密码，默认是true
	 */
	private boolean isUseLockPwd = true;
	
	/**
	 * 用来判断该用户是否自动登陆，默认是true
	 */
	private boolean isAutoLogin = true;
	
	public int getId()
	{
		return id;
	}
	
	public void setId(int id)
	{
		this.id = id;
	}
	
	public int getUserId()
	{
		return userId;
	}
	
	public void setUserId(int userId)
	{
		this.userId = userId;
	}
	
	public String getPwd()
	{
		return pwd;
	}
	
	public void setPwd(String pwd)
	{
		this.pwd = pwd;
	}
	
	public String getAccount()
	{
		return account;
	}
	
	public void setAccount(String account)
	{
		this.account = account;
	}
	
	public String getLoginPwd()
	{
		return loginPwd;
	}
	
	public void setLoginPwd(String loginPwd)
	{
		this.loginPwd = loginPwd;
	}
	
	/** {@inheritDoc} */
	
	@Override
	public int describeContents()
	{
		return 0;
	}
	
	/** {@inheritDoc} */
	
	@Override
	public void writeToParcel(Parcel dest, int flags)
	{
		dest.writeInt(id);
		dest.writeInt(userId);
		dest.writeString(pwd);
		dest.writeString(account);
		dest.writeString(loginPwd);
	}
	
	public static final Creator<LockPwd> CREATOR = new Creator<LockPwd>()
	{
		@Override
		public LockPwd[] newArray(int size)
		{
			return new LockPwd[size];
		}
		
		@Override
		public LockPwd createFromParcel(Parcel in)
		{
			return new LockPwd(in);
		}
	};
	
	public LockPwd(Parcel in)
	{
		id = in.readInt();
		userId = in.readInt();
		pwd = in.readString();
		account = in.readString();
		loginPwd = in.readString();
	}
	
	public LockPwd()
	{
	
	}
	
	public boolean isHasSetLockPwd()
	{
		return hasSetLockPwd;
	}
	
	public void setHasSetLockPwd(boolean hasSetLockPwd)
	{
		this.hasSetLockPwd = hasSetLockPwd;
	}
	
	public boolean isUseLockPwd()
	{
		return isUseLockPwd;
	}
	
	public void setUseLockPwd(boolean isUseLockPwd)
	{
		this.isUseLockPwd = isUseLockPwd;
	}
	
	public boolean isAutoLogin()
	{
		return isAutoLogin;
	}
	
	public void setAutoLogin(boolean isAutoLogin)
	{
		this.isAutoLogin = isAutoLogin;
	}
}
