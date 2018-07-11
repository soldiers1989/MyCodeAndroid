package com.dm.http;

import org.json.JSONObject;

import com.dm.widgets.utils.ToastUtil;

import android.content.Context;

/**
 * Http回调接口
 * @author jiaohongyun
 *
 */
public abstract class HttpCallBack
{
	private boolean isShowProgress = true;
	
	public abstract void onSuccess(JSONObject result);
	
	public void onFailure(Throwable t, Context context)
	{
		if (t instanceof DMException)
		{
			DMException dmE = (DMException)t;
			switch (dmE.getCode())
			{
				case DMException.NET_CONNECTION_ERROR:
					ToastUtil.getInstant().show(context, dmE.getDescription());
					onConnectFailure(dmE, context);
					break;
				case DMException.CAN_NOT_CONNECT_TO_SERVER:
					ToastUtil.getInstant().show(context, dmE.getDescription());
					onServerError(dmE, context);
					break;
				case DMException.CONNECT_TIME_OUT:
					ToastUtil.getInstant().show(context, dmE.getDescription());
					//					onConnectFailure(dmE);
					break;
				default:
					onServerError(dmE, context);
					break;
			}
		}
		else
		{
			onOtherError(t, context);
		}
	}
	
	public void onLoading(Integer integer)
	{
	}
	
	public void onStart()
	{
	}
	
	public boolean isShowProgress()
	{
		return isShowProgress;
	}
	
	public void setShowProgress(boolean isShowProgress)
	{
		this.isShowProgress = isShowProgress;
	}
	
	/**
	 * 网络连接失败
	 */
	public void onConnectFailure(DMException dmE, Context context)
	{
	
	}
	
	/**
	 * 服务器内部错误
	 * @param dmE
	 */
	public void onServerError(DMException dmE, Context context)
	{
	
	}
	
	/**
	 * 其它错误
	 * @param t
	 */
	public void onOtherError(Throwable t, Context context)
	{
	
	}
}
