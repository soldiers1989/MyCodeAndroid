package com.hxjr.p2p.ad5.service;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.json.JSONException;
import org.json.JSONObject;

import com.dm.http.HttpCallBack;
import com.dm.http.HttpUtil;
import com.dm.utils.DMJsonObject;
import com.hxjr.p2p.ad5.DMApplication;
import com.hxjr.p2p.ad5.bean.RegexInfo;
import com.hxjr.p2p.ad5.utils.DMConstant;
import com.hxjr.p2p.ad5.utils.SharedPreferenceUtils;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

public class RegisterInfoService extends Service
{
	private ScheduledExecutorService ses;
	
	/**
	 * 错误重试次数
	 */
	private int i;
	
	private boolean isFirst = true;
	
	@Override
	public IBinder onBind(Intent intent)
	{
		return null;
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{
		int resI = super.onStartCommand(intent, flags, startId);
		getRegisterInfo();
		return resI;
	}
	
	private void getRegisterInfo()
	{
		HttpUtil.getInstance().post(this, DMConstant.API_Url.REGISTER_INFO, new HttpCallBack()
		{
			@Override
			public void onSuccess(JSONObject result)
			{
				try
				{
					String code = result.getString("code");
					if (code.equals(DMConstant.ResultCode.SUCCESS))
					{
						// 成功
						DMJsonObject data = new DMJsonObject(result.getString("data"));
						RegexInfo regexInfo = new RegexInfo(data);
						SharedPreferenceUtils.put(RegisterInfoService.this,
							SharedPreferenceUtils.REGEXINFO_FILE_NAME,
							"isNetConfig",
							true);
						SharedPreferenceUtils.put(RegisterInfoService.this,
							SharedPreferenceUtils.REGEXINFO_FILE_NAME,
							"registerFlage",
							regexInfo.getRegisterFlage());
						SharedPreferenceUtils.put(RegisterInfoService.this,
							SharedPreferenceUtils.REGEXINFO_FILE_NAME,
							"newUserNameRegex",
							regexInfo.getNewUserNameRegex());
						SharedPreferenceUtils.put(RegisterInfoService.this,
							SharedPreferenceUtils.REGEXINFO_FILE_NAME,
							"userNameRegexContent",
							regexInfo.getUserNameRegexContent());
						SharedPreferenceUtils.put(RegisterInfoService.this,
							SharedPreferenceUtils.REGEXINFO_FILE_NAME,
							"newPasswordRegex",
							regexInfo.getNewPasswordRegex());
						SharedPreferenceUtils.put(RegisterInfoService.this,
							SharedPreferenceUtils.REGEXINFO_FILE_NAME,
							"passwordRegexContent",
							regexInfo.getPasswordRegexContent());
						SharedPreferenceUtils.put(RegisterInfoService.this,
							SharedPreferenceUtils.REGEXINFO_FILE_NAME,
							"txPwdRegex",
							regexInfo.getTxPwdRegex());
						SharedPreferenceUtils.put(RegisterInfoService.this,
							SharedPreferenceUtils.REGEXINFO_FILE_NAME,
							"txPwdContent",
							regexInfo.getTxPwdContent());
						DMApplication.getInstance().setRegexInfo(regexInfo);
					}
					else
					{
						doLooper();
						return;
					}
				}
				catch (JSONException e)
				{
					e.printStackTrace();
				}
				stopSelf();
			}
			
			@Override
			public void onFailure(Throwable t, Context context)
			{
				doLooper();
			}
			
			@Override
			public void onStart()
			{
				setShowProgress(false);
			}
		});
	}
	
	/**
	 * 失败重试
	 */
	private void doLooper()
	{
		SharedPreferenceUtils.put(RegisterInfoService.this, SharedPreferenceUtils.REGEXINFO_FILE_NAME, "isNetConfig", false);
		if (ses == null)
		{
			ses = Executors.newSingleThreadScheduledExecutor();
		}
		if (isFirst)
		{
			isFirst = false;
			ses.scheduleAtFixedRate(new Runnable()
			{
				@Override
				public void run()
				{
					i++;
					if (i < 10)
					{
						getRegisterInfo();
					}
					else
					{
						isFirst = true;
						ses.shutdownNow();
						stopSelf();
					}
					
				}
			}, 10, 20, TimeUnit.SECONDS);
		}
	}
	
}
