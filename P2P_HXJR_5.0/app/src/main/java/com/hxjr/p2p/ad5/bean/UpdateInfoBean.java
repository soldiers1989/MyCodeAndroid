/*
 * 文 件 名:  UpdateInfoBean.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  jiaohongyun
 * 修改时间:  2014年12月4日
 */
package com.hxjr.p2p.ad5.bean;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.text.TextUtils;

import com.dm.utils.DMJsonObject;
import com.hxjr.p2p.ad5.DMApplication;

import org.json.JSONException;

/**
 * 版本更新信息
 * @author  jiaohongyun
 * @version  [版本号, 2014年12月4日]
 */
public class UpdateInfoBean
{
	/**
	 * 线上最新版本
	 */
	private String newVersion;
	
	/**
	 * 新版本下载地址
	 */
	private String fileUrl;
	
	/**
	 * 是否必须更新
	 */
	private boolean isMustUpdate;
	
	/**
	 * 是否需要更新
	 */
	private boolean needUpdate;
	
	public UpdateInfoBean()
	{
	
	}
	
	public UpdateInfoBean(DMJsonObject data)
	{
		try
		{
			fileUrl = data.getString("localUrl");
			String url = data.getString("url");
			//如果url为空则取localUrl的值，否则取url的值
			String urlStr = TextUtils.isEmpty(url) ? fileUrl : url;
			fileUrl = urlStr;
			newVersion = data.getString("verNO");
			int isMust = data.getInt("isMust");
			
			if (isMust == 0)
			{
				isMustUpdate = false;
			}
			else if (isMust == 1)
			{
				isMustUpdate = true;
			}
			// 获取当前APP版本号
			String nowVersion = "1.0.0";
			PackageManager packageManager = DMApplication.getInstance().getPackageManager();
			// getPackageName()是你当前类的包名，0代表是获取版本信息
			try
			{
				PackageInfo packInfo = packageManager.getPackageInfo(DMApplication.getInstance().getPackageName(), 0);
				nowVersion = packInfo.versionName;
			}
			catch (NameNotFoundException e)
			{
				e.printStackTrace();
			}
			
			// 比较版本号看是否需要更新
			String[] newV = newVersion.split("\\.");
			String[] nowV = nowVersion.split("\\.");
			for (int i = 0; i < newV.length && i < nowV.length; i++)
			{
				int a = Integer.valueOf(newV[i]);
				int b = Integer.valueOf(nowV[i]);
				if (a < b)
				{
					needUpdate = false;
					break;
				}
				if (a > b)
				{
					needUpdate = true;
					break;
				}
			}
		}
		catch (JSONException e)
		{
			e.printStackTrace();
		}
	}
	
	public String getNewVersion()
	{
		return newVersion;
	}
	
	public void setNewVersion(String newVersion)
	{
		this.newVersion = newVersion;
	}
	
	public String getFileUrl()
	{
		return fileUrl;
	}
	
	public void setFileUrl(String fileUrl)
	{
		this.fileUrl = fileUrl;
	}
	
	public boolean isMustUpdate()
	{
		return isMustUpdate;
	}
	
	public void setMustUpdate(boolean isMustUpdate)
	{
		this.isMustUpdate = isMustUpdate;
	}
	
	public boolean isNeedUpdate()
	{
		return needUpdate;
	}
	
	public void setNeedUpdate(boolean needUpdate)
	{
		this.needUpdate = needUpdate;
	}
}
