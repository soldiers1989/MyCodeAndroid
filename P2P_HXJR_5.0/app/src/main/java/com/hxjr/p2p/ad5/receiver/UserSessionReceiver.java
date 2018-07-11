/*
 * 文 件 名:  UserSessionReceiver.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  jiaohongyun
 * 修改时间:  2014年11月24日
 */
package com.hxjr.p2p.ad5.receiver;

import org.json.JSONException;
import org.json.JSONObject;

import com.dm.http.HttpCallBack;
import com.dm.http.HttpUtil;
import com.dm.utils.DMLog;
import com.dm.utils.NetworkUtils;
import com.hxjr.p2p.ad5.DMApplication;
import com.hxjr.p2p.ad5.utils.DMConstant;
import com.hxjr.p2p.ad5.utils.ErrorUtil;
import com.hxjr.p2p.ad5.utils.HttpParams;
import com.hxjr.p2p.ad5.utils.SharedPreferenceUtils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * 接收登录和退出登录消息，以及定时发送请求，保持cookie
 * 
 * @author  jiaohongyun
 * @version  [版本号, 2014年11月24日]
 */
public class UserSessionReceiver extends BroadcastReceiver
{
	private static final String LOG_TAG = UserSessionReceiver.class.getCanonicalName();
	
	@Override
	public void onReceive(Context context, Intent intent)
	{
		DMLog.i(LOG_TAG, "onReceive()");
		String action = intent.getAction();
		DMLog.d(LOG_TAG, "action=" + action);
		if (action != null && action.equals(DMConstant.BroadcastActions.USER_SESSION_LOGIN))
		{
			startUpdate(context);
		}
		else if (action != null && action.equals(DMConstant.BroadcastActions.USER_SESSION_LOGOUT))
		{
			stopUpdate(context);
		}
		else if (action != null && action.equals(DMConstant.BroadcastActions.USER_SESSION_UPDATE_COOKIE))
		{
			if (NetworkUtils.isNetworkAvailable(context))
			{
				update(context);
			}
		}
	}
	
	private void update(Context context)
	{
		DMLog.i(LOG_TAG, "onReceive()");
		String url = DMConstant.API_Url.SYS_KEEPSESSION;
		HttpParams params = new HttpParams();
		params.put("verType", "1");
		HttpUtil.getInstance().post(context, url, params, new HttpCallBack()
		{
			@Override
			public void onSuccess(JSONObject result)
			{
				try
				{
					String code = result.getString("code");
					long cTime = System.currentTimeMillis();
					if (code.equals(DMConstant.ResultCode.SUCCESS))
					{
						JSONObject data = result.getJSONObject("data");
						long sTime = data.getLong("time");
						DMApplication.getInstance().diffTime = cTime - sTime;
						boolean hasSession = data.getBoolean("result");
						String serviceTel = data.getString("tel");
						String kfQQ = data.getString("kfQQ");
						SharedPreferenceUtils.put(DMApplication.getInstance(), "consts", "tel", serviceTel);
						SharedPreferenceUtils.put(DMApplication.getInstance(), "consts", "kfQQ", kfQQ);
						if (hasSession)
						{
							return;
						}
						else
						{
							//                            //已经超时,作超时处理
							//							DMApplication.getInstance().login(lockPwd, time);
						}
					}
					else
					{
						ErrorUtil.showError(result);
					}
				}
				catch (JSONException e)
				{
					e.printStackTrace();
				}
			}
			
			@Override
			public void onStart()
			{
				this.setShowProgress(false);
			}
			
			@Override
			public void onFailure(Throwable t, Context context)
			{
			}
		});
	}
	
	/**
	 * 开始定时任务
	 * @param context
	 */
	private void startUpdate(Context context)
	{
		DMLog.i(LOG_TAG, "startUpdate()");
		Intent intent = new Intent(context, UserSessionReceiver.class);
		intent.setAction(DMConstant.BroadcastActions.USER_SESSION_UPDATE_COOKIE);
		PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
		
		//开始时间        
		AlarmManager am = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
		//900秒一个周期，不停的发送广播
		am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 900 * 1000, sender);
	}
	
	/**
	 * 停止定时任务
	 * @param context
	 */
	private void stopUpdate(Context context)
	{
		DMLog.i(LOG_TAG, "stopUpdate()");
		Intent intent = new Intent(context, UserSessionReceiver.class);
		intent.setAction(DMConstant.BroadcastActions.USER_SESSION_UPDATE_COOKIE);
		PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
		AlarmManager alarm = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
		alarm.cancel(sender);
	}
}
