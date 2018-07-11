package com.hxjr.p2p.ad5.receiver;

import com.dm.utils.NetworkUtils;
import com.hxjr.p2p.ad5.service.LoginService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NetWorkReceiver extends BroadcastReceiver
{
	@Override
	public void onReceive(Context context, Intent intent)
	{
		if (NetworkUtils.isNetworkAvailable(context))
		{
			//如果网络可用
			Intent loginIntent = new Intent(context, LoginService.class);
			context.startService(loginIntent);
		}
	}
}
