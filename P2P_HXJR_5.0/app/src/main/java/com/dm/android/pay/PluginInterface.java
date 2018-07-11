package com.dm.android.pay;

import com.dm.android.pay.DMPayPlugin.PayPluginCallBack;

import android.app.Activity;
import android.content.Intent;

/**
 * 支付插件接口
 * @author jiaohongyun
 *
 */
public interface PluginInterface
{
	/**
	 * 开始支付操作
	 * @param activity
	 * @param payData
	 */
	void startPay(Activity activity, String payData);
	
	/**
	 * 开始支付操作
	 * @param activity
	 * @param payData
	 * @param callBack
	 */
	void startPay(Activity activity, String payData, PayPluginCallBack callBack);
	
	/**
	 * 结果操作
	 * @param requestCode
	 * @param resultCode
	 * @param data
	 * @param activity
	 * @param callBack
	 */
	void onActivityResult(int requestCode, int resultCode, Intent data, Activity activity, PayPluginCallBack callBack);
}
