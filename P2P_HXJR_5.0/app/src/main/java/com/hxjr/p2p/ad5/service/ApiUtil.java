package com.hxjr.p2p.ad5.service;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.dm.http.HttpCallBack;
import com.dm.http.HttpUtil;
import com.dm.utils.DMJsonObject;
import com.dm.utils.DMLog;
import com.dm.widgets.utils.ToastUtil;
import com.hxjr.p2p.ad5.DMApplication;
import com.hxjr.p2p.ad5.bean.Fee;
import com.hxjr.p2p.ad5.bean.UserInfo;
import com.hxjr.p2p.ad5.ui.mine.bank.BankCardWebActivity;
import com.hxjr.p2p.ad5.ui.mine.setting.TradePwdActivity;
import com.hxjr.p2p.ad5.utils.DMConstant;
import com.hxjr.p2p.ad5.utils.HttpParams;

import org.json.JSONException;
import org.json.JSONObject;

public class ApiUtil
{
	/**
	 * 获取充值提现费用及其它设置
	 * @param context
	 * @param feeCallBack
	 */
	public static void getFee(Context context, final OnPostCallBack feeCallBack)
	{
		HttpParams params = new HttpParams();
		HttpUtil.getInstance().post(context, DMConstant.API_Url.USER_FEE, params, new HttpCallBack()
		{
			@Override
			public void onSuccess(JSONObject result)
			{
				try
				{
					String code = result.getString("code");
					if (DMConstant.ResultCode.SUCCESS.equals(code))
					{
						JSONObject data = result.getJSONObject("data");
						Fee fee = new Fee(new DMJsonObject(data.toString()));
						DMLog.e(data.toString());
						feeCallBack.onSuccess(fee);
					}
					else
					{
						feeCallBack.onFailure();
					}
				}
				catch (JSONException e)
				{
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(Throwable t, Context context)
			{
				//				super.onFailure(t, context);
				feeCallBack.onFailure();
			}

			//			@Override
			//			public void onConnectFailure(DMException dmE, Context context)
			//			{
			//				ToastUtil.getInstant().show(context, dmE.getDescription());
			//			}
		});
	}

	/***
	 * 获取用户基本信息
	 * @param context
	 * @param
	 */
	public static void getUserInfo(final Context context)
	{
		HttpUtil.getInstance().post(context, DMConstant.API_Url.USER_USERINFO, new HttpCallBack()
		{
			@Override
			public void onSuccess(JSONObject result)
			{
				try
				{
					String code = result.getString("code");
					if (code.equals(DMConstant.ResultCode.SUCCESS))
					{
						DMJsonObject data = new DMJsonObject(result.getString("data"));
						UserInfo userInfo = new UserInfo(data);
						DMLog.e("getUserInfo------USERINFO",userInfo.toString());
						DMApplication.getInstance().setUserInfo(userInfo);
//						if (DMApplication.getInstance().islogined()) {
//							TimeUtils.diffTime(DMApplication.getInstance().getUserInfo().getRegisterTime());
//							if (DMApplication.getInstance().getUserInfo().getIsExperienceBid().equals("F")) {
//								DMLog.e(TimeUtils.getCurrentTimeInString()+"");
//								DMLog.e(DMApplication.getInstance().getUserInfo().getRegisterTime());
//								 if(TimeUtils.diffTime(DMApplication.getInstance().getUserInfo().getRegisterTime()).equals("注册未超过6小时"))
//								 {
////                                     AppManager.getAppManager().finishActivity(RedPacketActivity.class);
//									 Intent intent = new Intent(context, RedPacketActivity.class);
//									 context.startActivity(intent);
//								 }
//							}
//						}
					}
				}
				catch (JSONException e)
				{
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(Throwable t, Context context)
			{
			}

			@Override
			public void onStart()
			{
				setShowProgress(false);
			}
		});
	}

	/***
	 * 获取用户基本信息
	 * @param context
	 * @param
	 */
	public static void getUserInfoWebRegister(final BankCardWebActivity context)
	{
		HttpUtil.getInstance().post(context, DMConstant.API_Url.USER_USERINFO, new HttpCallBack()
		{
			@Override
			public void onSuccess(JSONObject result)
			{
				try
				{
					String code = result.getString("code");
					if (code.equals(DMConstant.ResultCode.SUCCESS))
					{
						DMJsonObject data = new DMJsonObject(result.getString("data"));
						UserInfo userInfo = new UserInfo(data);
						DMLog.e("getUserInfo------USERINFO",userInfo.toString());
						DMApplication.getInstance().setUserInfo(userInfo);
						                if ("".equals(DMApplication.getInstance().getUserInfo().getUsrCustId())){
						                    ToastUtil.getInstant().show(context,"注册失败，请重试！");
											context.finish();
						                }
						                else{
						                    Intent intent = new Intent(context, TradePwdActivity.class);
											context.startActivity(intent);
											context.finish();
						                }
					}
				}
				catch (JSONException e)
				{
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(Throwable t, Context context)
			{
			}

			@Override
			public void onStart()
			{
				setShowProgress(false);
			}
		});
	}




	/***
	 * 获取用户基本信息
	 * @param context
	 * @param
	 */
	public static void getUserInfo(Context context, final OnGetUserInfoCallBack onGetUserInfoCallBack)
	{
		HttpUtil.getInstance().post(context, DMConstant.API_Url.USER_USERINFO, new HttpCallBack()
		{
			@Override
			public void onSuccess(JSONObject result)
			{
				try
				{
					String code = result.getString("code");
					if (code.equals(DMConstant.ResultCode.SUCCESS))
					{
						DMJsonObject data = new DMJsonObject(result.getString("data"));
						UserInfo userInfo = new UserInfo(data);
						DMApplication.getInstance().setUserInfo(userInfo);
						onGetUserInfoCallBack.onSuccess();
					}
				}
				catch (JSONException e)
				{
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(Throwable t, Context context)
			{
				onGetUserInfoCallBack.onFailure();
			}
		});
	}
//	/***
//	 * 获取用户基本信息(注册用)
//	 * @param context
//	 * @param
//	 */
//	public static void getUserInfoRed(final Context context)
//	{
//		HttpUtil.getInstance().post(context, DMConstant.API_Url.USER_USERINFO, new HttpCallBack()
//		{
//			@Override
//			public void onSuccess(JSONObject result)
//			{
//				try
//				{
//					String code = result.getString("code");
//					if (code.equals(DMConstant.ResultCode.SUCCESS))
//					{
//						DMJsonObject data = new DMJsonObject(result.getString("data"));
//						UserInfo userInfo = new UserInfo(data);
//						DMLog.i("USERINFO",userInfo.toString());
//						DMApplication.getInstance().setUserInfo(userInfo);
//                        if (DMApplication.getInstance().islogined()) {
//                            TimeUtils.diffTime(DMApplication.getInstance().getUserInfo().getRegisterTime());
//                            if (DMApplication.getInstance().getUserInfo().getIsExperienceBid().equals("F")) {
//                                DMLog.e(TimeUtils.getCurrentTimeInString()+"");
//                                DMLog.e(DMApplication.getInstance().getUserInfo().getRegisterTime());
//                                if(TimeUtils.diffTime(DMApplication.getInstance().getUserInfo().getRegisterTime()).equals("注册未超过6小时"))
//                                {
//                                    //                                     AppManager.getAppManager().finishActivity(RedPacketActivity.class);
//                                    Intent intent = new Intent(context, RedPacketActivity.class);
//                                    context.startActivity(intent);
//                                }
//                            }
//                        }
//					}
//				}
//				catch (JSONException e)
//				{
//					e.printStackTrace();
//				}
//			}
//
//			@Override
//			public void onFailure(Throwable t, Context context)
//			{
//			}
//
//			@Override
//			public void onStart()
//			{
//				setShowProgress(false);
//			}
//		});
//	}
	public interface OnPostCallBack
	{
		public void onSuccess(Fee fee);

		public void onFailure();
	}

	public interface OnGetUserInfoCallBack
	{
		public void onSuccess();

		public void onFailure();
	}
}
