/*
 * 文 件 名:  CookieUtil.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  jiaohongyun
 * 修改时间:  2014年12月23日
 */
package com.dm.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 管理Cookie
 * @author  jiaohongyun
 * @version  [版本号, 2014年12月23日]
 */
public class CookieUtil
{
	/**
	 * Cookie字符串
	 */
	private static String mCookie;
	
	private static SharedPreferences sp;
	public static String getmCookie(Context context)
	{
		if (mCookie == null)
		{
			if (sp == null)
			{
				sp = context.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
			}
			mCookie = sp.getString("cookie", "");
		}
		return mCookie;
	}
	
	public static void setmCookie(String mCookie, Context context)
	{
		//解决退出登录时崩溃的问题
		if (null == mCookie)
		{
			CookieUtil.mCookie = null;
			return;
		}
		int temp = mCookie.indexOf(";");
		if (temp > 0)
		{
			CookieUtil.mCookie = mCookie.substring(0, temp + 1);
		}
		else
		{
			CookieUtil.mCookie = mCookie;
		}
		if (sp == null)
		{
			sp = context.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
		}
		sp.edit().putString("cookie", CookieUtil.mCookie).apply();
	}
}
