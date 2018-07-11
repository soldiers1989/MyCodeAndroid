package com.dm.android.pay.p;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.dm.android.pay.DMPayPlugin.PayPluginCallBack;
import com.dm.android.pay.PluginInterface;
import com.dm.android.pay.p.lianlian.BaseHelper;
import com.dm.android.pay.p.lianlian.Constants;
import com.dm.android.pay.p.lianlian.MobileSecurePayer;

/**
 * 连连支付
 * @author jiaohongyun
 *
 */
public class LianLianGate implements PluginInterface
{
	/*
	 * 支付验证方式 0：标准版本， 1：卡前置方式，此两种支付方式接入时，只需要配置一种即可，Demo为说明用。可以在menu中选择支付方式。
	 */
	private int pay_type_flag = 1;
	
	/**
	 * 单例
	 */
	private static LianLianGate instance;
	
	private PayPluginCallBack callBack;
	
	private LianLianGate()
	{
		
	}
	
	public static LianLianGate getInstance()
	{
		if (instance == null)
		{
			instance = new LianLianGate();
		}
		return instance;
	}
	
	private Handler mHandler = null;
	
	private Handler createHandler(Activity activity)
	{
		return new Handler()
		{
			@Override
			public void handleMessage(Message msg)
			{
				String strRet = (String)msg.obj;
				switch (msg.what)
				{
					case Constants.RQF_PAY:
					{
						JSONObject objContent = BaseHelper.string2JSON(strRet);
						String retCode = objContent.optString("ret_code");
						String retMsg = objContent.optString("ret_msg");
						// TODO 先判断状态码，状态码为 成功或处理中 的需要 验签
						if (Constants.RET_CODE_SUCCESS.equals(retCode))
						{
							String resulPay = objContent.optString("result_pay");
							if (Constants.RESULT_PAY_SUCCESS.equalsIgnoreCase(resulPay))
							{
								// TODO 支付成功后续处理
								if (callBack != null)
								{
									callBack.onSuccess();
								}
							}
							else
							{
								if (callBack != null)
								{
									callBack.onFaild(retMsg + "");
								}
							}
							
						}
						else if (Constants.RET_CODE_PROCESS.equals(retCode))
						{
							String resulPay = objContent.optString("result_pay");
							if (Constants.RESULT_PAY_PROCESSING.equalsIgnoreCase(resulPay))
							{
								if (callBack != null)
								{
									callBack.onFaild(retMsg + "");
								}
							}
							
						}
						else
						{
							if (callBack != null)
							{
								callBack.onFaild(retMsg + "");
							}
						}
					}
						break;
				}
				super.handleMessage(msg);
			}
		};
		
	}
	
	@Override
	public void startPay(Activity activity, String payData)
	{
		startPay(activity, payData, null);
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data, Activity activity, PayPluginCallBack callBack)
	{
		
	}
	
	@Override
	public void startPay(Activity activity, String payData, PayPluginCallBack callBack)
	{
		mHandler = createHandler(activity);
		this.callBack = callBack;
		if (pay_type_flag == 0)
		{
			MobileSecurePayer msp = new MobileSecurePayer();
			boolean bRet = msp.pay(payData, mHandler, Constants.RQF_PAY, activity, false);
		}
		else if (pay_type_flag == 1)
		{
			MobileSecurePayer msp = new MobileSecurePayer();
			boolean bRet = msp.pay(payData, mHandler, Constants.RQF_PAY, activity, false);
		}
	}
}
