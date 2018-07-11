package com.dm.android.pay.p;

import com.dm.android.pay.DMPayPlugin.PayPluginCallBack;
import com.dm.android.pay.PluginInterface;
import com.unionpay.UPPayAssistEx;
import com.unionpay.uppay.PayActivity;

import android.app.Activity;
import android.content.Intent;

public class YinLianPay implements PluginInterface
{
	/**
	 * 00生产环境，01测试环境
	 */
	private static final String serverMode = "00";
	
	/**
	 * 单例
	 */
	private static YinLianPay instance;
	
	private YinLianPay()
	{
	
	}
	
	public static YinLianPay getInstance()
	{
		if (instance == null)
		{
			instance = new YinLianPay();
		}
		return instance;
	}
	
	@Override
	public void startPay(Activity activity, String payData)
	{
		UPPayAssistEx.startPayByJAR(activity, PayActivity.class, null, null, payData, serverMode);
	}
	
	@Override
	public void startPay(Activity activity, String payData, PayPluginCallBack callBack)
	{
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data, Activity activity, PayPluginCallBack callBack)
	{
		// 此方法用来监听支付插件返回结果
		if (data == null)
		{
			return;
		}
		String str = data.getExtras().getString("pay_result");
		String msg = "";
		if (str.equalsIgnoreCase("success"))
		{
			//			msg = "支付成功";
			callBack.onSuccess();
		}
		else if (str.equalsIgnoreCase("fail"))
		{
			//			msg = "支付失败";
			callBack.onFaild("充值失败!");
		}
		else if (str.equalsIgnoreCase("cancel"))
		{
			//			msg = "支付已被取消";
			callBack.onFaild("支付已被取消!");
		}
	}
}
