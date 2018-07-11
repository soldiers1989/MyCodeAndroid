package com.dm.android.pay.p;

import org.json.JSONException;
import org.json.JSONObject;

import com.allinpay.appayassistex.APPayAssistEx;
import com.dm.android.pay.DMPayPlugin.PayPluginCallBack;
import com.dm.android.pay.PluginInterface;

import android.app.Activity;
import android.content.Intent;

/**
 * 通联支付
 * @author jiaohongyun
 *
 */
public class PayAllinpay implements PluginInterface
{
	/**
	 * 00生产环境，01测试环境
	 */
	private static final String serverMode = "01";
	
	/**
	 * 单例
	 */
	private static PayAllinpay instance;
	
	private PayAllinpay()
	{
	
	}
	
	public static PayAllinpay getInstance()
	{
		if (instance == null)
		{
			instance = new PayAllinpay();
		}
		return instance;
	}
	
	@Override
	public void startPay(Activity activity, String payData)
	{
		APPayAssistEx.startPay(activity, payData, serverMode);
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data, Activity activity, PayPluginCallBack callBack)
	{
		if (APPayAssistEx.REQUESTCODE == requestCode)
		{
			if (null != data)
			{
				String payRes = null;
				String payAmount = null;
				String payTime = null;
				String payOrderId = null;
				try
				{
					JSONObject resultJson = new JSONObject(data.getExtras().getString("result"));
					payRes = resultJson.getString(APPayAssistEx.KEY_PAY_RES);
					payAmount = resultJson.getString("payAmount");
					payTime = resultJson.getString("payTime");
					payOrderId = resultJson.getString("payOrderId");
				}
				catch (JSONException e)
				{
					e.printStackTrace();
				}
				if (null != payRes && payRes.equals(APPayAssistEx.RES_SUCCESS))
				{
					//支付成功！
					callBack.onSuccess();
				}
				else
				{
					//支付失败！
					callBack.onFaild("充值失败");
				}
			}
		}
	}

	@Override
	public void startPay(Activity activity, String payData, PayPluginCallBack callBack)
	{
		// 不需要实现
	}
	
}
