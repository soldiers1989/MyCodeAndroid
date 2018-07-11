package com.hxjr.p2p.ad5.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class LoginService extends Service
{
	
	@Override
	public IBinder onBind(Intent intent)
	{
		return null;
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{
		//检查session
		//如果失效 且 保存过用户名和密码 则自动登录
		return super.onStartCommand(intent, flags, startId);
	}
	
}
