package com.hxjr.p2p.ad5.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/***
 * 投标和债权转让成功的广播
 * @author  tangjian
 * @date 2015年12月29日
 */
public class BidAndCreReceiver extends BroadcastReceiver
{
	public static String BID_SUCCESS_RECEIVER = "com.hxjr.p2p.ad5.ui.investment.BidFragment";
	
	public static String CRE_SUCCESS_RECEIVER = "com.hxjr.p2p.ad5.ui.investment.CreFragment";
	
	public static String LOAN_SUCCESS_RECEIVER = "com.hxjr.p2p.ad5.ui.investment.MyLoanRepaymentFragment";
	
	private OnSuccessListener mOnSuccessListener;
	
	public void setOnSuccessListener(OnSuccessListener onSuccessListener)
	{
		this.mOnSuccessListener = onSuccessListener;
	}
	
	@Override
	public void onReceive(Context context, Intent intent)
	{
		String action = intent.getAction();
		if (null != action)
		{
			if (action.equals(BID_SUCCESS_RECEIVER) || action.equals(CRE_SUCCESS_RECEIVER)
				|| action.equals(LOAN_SUCCESS_RECEIVER))
			{
				mOnSuccessListener.onSuccessToUpdateUi(intent);
			}
		}
	}
	
	/***
	 * 投标或购买债权成功后的回调接口
	 * @param intent
	 */
	public interface OnSuccessListener
	{
		/***
		 * 投标或购买债权成功后的回调
		 * 更新UI界面
		 * @param intent
		 */
		public void onSuccessToUpdateUi(Intent intent);
	}
}
