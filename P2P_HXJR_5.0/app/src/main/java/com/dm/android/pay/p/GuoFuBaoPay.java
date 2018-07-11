package com.dm.android.pay.p;

import java.util.HashMap;
import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;

import com.dm.android.pay.DMPayPlugin.PayPluginCallBack;
import com.dm.android.pay.PluginInterface;
import com.gopay.mobilepaybygopay_wap.GopayByWap;
import com.gopay.mobilepaybygopay_wap.GopayTools;
import com.gopay.mobilepaybygopay_wap.MD5Encrypt;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class GuoFuBaoPay implements PluginInterface
{
	/**
	 * 单例
	 */
	private static GuoFuBaoPay instance;
	
	private GuoFuBaoPay()
	{
	
	}
	
	public static GuoFuBaoPay getInstance()
	{
		if (instance == null)
		{
			instance = new GuoFuBaoPay();
		}
		return instance;
	}
	
	@Override
	public void startPay(Activity activity, String payData)
	{
		HashMap<String, String> authInfo = new HashMap<String, String>();
		try
		{
			JSONObject jsonObject = new JSONObject(payData);
			
			for (Iterator<String> keys = jsonObject.keys(); keys.hasNext();)
			{
				String key = keys.next();
				authInfo.put(key, jsonObject.getString(key));
			}
		}
		catch (JSONException e)
		{
			e.printStackTrace();
		}
		GopayTools gopayTools = new GopayTools();
		String token = gopayTools.getGopayIdentifierForPayment();
		
		authInfo.put("buyerContact", token);
		authInfo.put("buyerName", "MWAP");
		String sign_value = MD5Encrypt.getMD5("version=[" + authInfo.get("version") + "]tranCode=[" + authInfo.get("tranCode")
			+ "]merchantID=[" + authInfo.get("merchantID") + "]merOrderNum=[" + authInfo.get("merOrderNum") + "]tranAmt=["
			+ authInfo.get("tranAmt") + "]feeAmt=[" + authInfo.get("feeAmt") + "]" + "tranDateTime=["
			+ authInfo.get("tranDateTime") + "]frontMerUrl=[" + authInfo.get("frontMerUrl") + "]backgroundMerUrl=["
			+ authInfo.get("backgroundMerUrl") + "]orderId=[" + "" + "]gopayOutOrderId=[" + "" + "]tranIP=["
			+ authInfo.get("tranIP") + "]" + "respCode=[" + "" + "]gopayServerTime=[" + "" + "]VerficationCode=["
			+ authInfo.get("verify_code") + "]");
		authInfo.put("sign_value", sign_value);
		Intent i = new Intent(activity, GopayByWap.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable("AuthInfo", authInfo);
		i.putExtras(bundle);
		activity.startActivity(i);
	}
	
	@Override
	public void startPay(Activity activity, String payData, PayPluginCallBack callBack)
	{
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data, Activity activity, PayPluginCallBack callBack)
	{
		//
	}
}
