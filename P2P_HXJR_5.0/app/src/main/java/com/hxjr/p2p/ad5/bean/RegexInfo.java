package com.hxjr.p2p.ad5.bean;

import org.json.JSONException;

import android.text.TextUtils;

import com.dm.utils.DMJsonObject;

/**
 * @author zhoulantao
 *
 */
public class RegexInfo
{
    // 是否需要验证码
    private String registerFlage;
    
    // 用户名校验
    private String newUserNameRegex;
    
    // 用户名错误提示
    private String userNameRegexContent;
    
    // 用户密码校验
    private String newPasswordRegex;
    
    // 用户密码错误提示
    private String passwordRegexContent;
	
	//用户交易密码校验
	private String txPwdRegex;
	
	//用户交易密码错误提示
	private String txPwdContent;
	
	public RegexInfo()
	{
	
	}
	
	public RegexInfo(DMJsonObject data)
	{
		try
		{
			registerFlage = data.getString("registerFlage");
			String temp1 = data.getString("newUserNameRegex");
			if (!TextUtils.isEmpty(temp1))
			{
				temp1 = temp1.replace("/^", "^").replace("$/", "$");
			}
			newUserNameRegex = temp1;
			userNameRegexContent = data.getString("userNameRegexContent");
			String temp2 = data.getString("newPasswordRegex");
			if (!TextUtils.isEmpty(temp2))
			{
				temp2 = temp2.replace("/^", "^").replace("$/", "$");
			}
			newPasswordRegex = temp2;
			passwordRegexContent = data.getString("passwordRegexContent");
			String temp3 = data.getString("txPwdRegex"); //newPasswordRegex
			if (!TextUtils.isEmpty(temp3))
			{
				temp3 = temp3.replace("/^", "^").replace("$/", "$");
			}
			txPwdRegex = temp3;
			txPwdContent = data.getString("txPwdContent");
		}
		catch (JSONException e)
		{
			e.printStackTrace();
		}
	}
    public String getRegisterFlage()
    {
        return registerFlage;
    }
    
    public void setRegisterFlage(String registerFlage)
    {
        this.registerFlage = registerFlage;
    }
    
    public String getNewUserNameRegex()
    {
        return newUserNameRegex;
    }
    
    public void setNewUserNameRegex(String newUserNameRegex)
    {
        this.newUserNameRegex = newUserNameRegex;
    }
    
    public String getUserNameRegexContent()
    {
        return userNameRegexContent;
    }
    
    public void setUserNameRegexContent(String userNameRegexContent)
    {
        this.userNameRegexContent = userNameRegexContent;
    }
    
    public String getNewPasswordRegex()
    {
        return newPasswordRegex;
    }
    
    public void setNewPasswordRegex(String newPasswordRegex)
    {
        this.newPasswordRegex = newPasswordRegex;
    }
    
    public String getPasswordRegexContent()
    {
		return passwordRegexContent;
    }
    
    public void setPasswordRegexContent(String passwordRegexContent)
    {
        this.passwordRegexContent = passwordRegexContent;
    }
	
	public String getTxPwdRegex()
	{
		return txPwdRegex;
	}
	
	public void setTxPwdRegex(String txPwdRegex)
	{
		this.txPwdRegex = txPwdRegex;
	}
	
	public String getTxPwdContent()
	{
		return txPwdContent;
	}
	
	public void setTxPwdContent(String txPwdContent)
	{
		this.txPwdContent = txPwdContent;
	}
}
