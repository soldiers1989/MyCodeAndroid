package com.dm.android.pay;

import com.dm.android.pay.p.BaoFuPay;
import com.dm.android.pay.p.GuoFuBaoPay;
import com.dm.android.pay.p.LianLianGate;
import com.dm.android.pay.p.PayAllinpay;
import com.dm.android.pay.p.YinLianPay;

import android.app.Activity;
import android.content.Intent;

/**
 * 支付总插件交付项目不使用的部分可以删除
 * @author jiaohongyun
 *
 */
public class DMPayPlugin
{
	private static void startPay(Activity activity, String payData, PaymentType paymentType)
	{
		switch (paymentType)
		{
			case ALLINPAY:
				//通联支付
				PayAllinpay.getInstance().startPay(activity, payData);
				break;
			case GFBPAY:
				//国付宝支付
				GuoFuBaoPay.getInstance().startPay(activity, payData);
				break;
			case BAOFU:
				//宝付支付
				BaoFuPay.getInstance().startPay(activity, payData);
				break;
			case YINLIANPAY:
				//银联支付
				YinLianPay.getInstance().startPay(activity, payData);
				break;
			default:
				break;
		}
	}
	
	/**
	 * 不使用onActivityResult的支付方式
	 * @param activity
	 * @param payData
	 * @param paymentType
	 * @param callBack 非连连支持可以传null
	 */
	public static void startPay(Activity activity, String payData, PaymentType paymentType, PayPluginCallBack callBack)
	{
		switch (paymentType)
		{
			case LIANLIANAUTHGATE:
				//连连认证支付
				LianLianGate.getInstance().startPay(activity, payData, callBack);
				break;
			default:
				startPay(activity, payData, paymentType);
				break;
		}
	}
	public static void onActivityResult(int requestCode, int resultCode, Intent data,Activity activity,PayPluginCallBack callBack,PaymentType paymentType)
	{
		switch (paymentType)
		{
			case ALLINPAY:
				//通联支付
				PayAllinpay.getInstance().onActivityResult(requestCode, resultCode, data, activity, callBack);
				break;
			default:
				break;
		}
	}
	
	/**
	 * 充值结果回调接口
	 * @author jiaohongyun
	 *
	 */
	public interface PayPluginCallBack
	{
		public void onSuccess();
		
		public void onFaild(String message);
	}
}
