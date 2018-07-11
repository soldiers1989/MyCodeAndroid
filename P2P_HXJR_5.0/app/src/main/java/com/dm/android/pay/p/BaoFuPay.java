package com.dm.android.pay.p;

import com.baofoo.sdk.BaofooPayActivity;
import com.dm.android.pay.DMPayPlugin.PayPluginCallBack;
import com.dm.android.pay.PluginInterface;

import android.app.Activity;
import android.content.Intent;

/**
 * 宝付支付
 * @author jiaohongyun
 *
 */
public class BaoFuPay implements PluginInterface
{
	public static final int REQUEST_CODE_BAOFOO_SDK = 100;
	/**
	 * 单例
	 */
	private static BaoFuPay instance;
	
	private BaoFuPay()
	{
	
	}
	
	public static BaoFuPay getInstance()
	{
		if (instance == null)
		{
			instance = new BaoFuPay();
		}
		return instance;
	}
	
	@Override
	public void startPay(Activity activity, String payData)
	{
		Intent payintent = new Intent(activity, BaofooPayActivity.class);
		// 通过业务流水请求报文获得的流水号
		payintent.putExtra(BaofooPayActivity.PAY_TOKEN, payData);
		// 标记是否为测试，传True 为测试环境，不传或者传False 则为正式调用
		payintent.putExtra(BaofooPayActivity.PAY_BUSINESS, false);
		activity.startActivityForResult(payintent, REQUEST_CODE_BAOFOO_SDK);
	}
	
	@Override
	public void startPay(Activity activity, String payData, PayPluginCallBack callBack)
	{
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data, Activity activity, PayPluginCallBack callBack)
	{
		if (requestCode == REQUEST_CODE_BAOFOO_SDK)
		{
			String result = "", msg = "";
			if (data == null || data.getExtras() == null)
			{
				msg = "支付已被取消";
			}
			else
			{
				//result 返回值判断-1:失败0:取消1:成功10:处理中
				result = data.getExtras().getString(BaofooPayActivity.PAY_RESULT);
				msg = data.getExtras().getString(BaofooPayActivity.PAY_MESSAGE);
				if (result.equals("-1"))
				{
					//失败
					callBack.onFaild("支付失败");
				}
				if (result.equals("0"))
				{
					//支付已被取消
					callBack.onFaild("支付已被取消");
				}
				else if (result.equals("1"))
				{
					//成功
					callBack.onSuccess();
				}
			}
		}
	}
}
