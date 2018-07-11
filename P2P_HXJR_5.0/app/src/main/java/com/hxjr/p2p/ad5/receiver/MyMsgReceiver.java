package com.hxjr.p2p.ad5.receiver;
import android.content.Context;
import android.util.Log;

import com.alibaba.sdk.android.push.MessageReceiver;
import com.alibaba.sdk.android.push.notification.CPushMessage;

import java.util.Map;

public class MyMsgReceiver extends MessageReceiver {
	
	@Override
	protected void onMessage(Context arg0, CPushMessage arg1) {
		// TODO Auto-generated method stub
		Log.e("MyMsgReceice=========", "onMessage");
		Log.e("MyMsgReceice=========", "onMessage"+arg1.getContent());
		super.onMessage(arg0, arg1);
	}

	@Override
	protected void onNotification(Context context, String title,
		String summary, Map<String, String> extraMap) {
//		AliCloudPushApplication.countNum++;
//		Log.e("MyMsgReceice=============",
//			AliCloudPushApplication.countNum + "");
		Log.e("onNotification", title + " " + summary + " " + extraMap);
		super.onNotification(context, title, summary, extraMap);
	}

	@Override
	public void onNotificationOpened(Context context, String title,
		String summary, String extraMap) {

		super.onNotificationOpened(context, title, summary, extraMap);
		Log.e("MyMsgReceice=======", title + " " + summary
			+ " " + extraMap);
	}

	@Override
	public void onNotificationRemoved(Context arg0, String arg1) {
		Log.e("MyMsgReceice=======", "onNotificationRemoved");
		// TODO Auto-generated method stub
		super.onNotificationRemoved(arg0, arg1);
	}

}
