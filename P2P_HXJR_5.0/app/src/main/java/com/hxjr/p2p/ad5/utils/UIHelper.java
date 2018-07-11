/*
 * 文 件 名:  UIHelper.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  jiaohongyun
 * 修改时间:  2015年1月7日
 */
package com.hxjr.p2p.ad5.utils;

import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hxjr.p2p.ad5.DMApplication;
import com.hxjr.p2p.ad5.bean.UserInfo;
import com.hxjr.p2p.ad5.ui.MainActivity;
import com.hxjr.p2p.ad5.ui.mine.setting.SecurityInfoActivity;
import com.hxjr.p2p.ad5.R;
import com.dm.http.HttpCallBack;
import com.dm.http.HttpUtil;
import com.dm.utils.CookieUtil;
import com.dm.utils.StringUtils;
import com.dm.widgets.utils.AlertDialogUtil;
import com.dm.widgets.utils.AlertDialogUtil.ConfirmListener;
import com.dm.widgets.utils.ToastUtil;

/**
 * 公共UI处理
 * @author  jiaohongyun
 * @version  [版本号, 2015年1月7日]
 */
public class UIHelper
{
	/**
	 * 弹出托管未注册第三方帐号对话框
	 */
	public static void confirmRegister(final Context context, final String url)
	{
		//		DMComfirmDialogConfig config = DMConfirmDialog.getConfigInstance();
		//		config.setTitle(context.getString(R.string.wenxi_tishi));
		//		config.setContent(context.getString(R.string.no_3_register));
		//		config.setOkStr(context.getString(R.string.no_3_register_ok));
		//		config.setCancelStr(context.getString(R.string.no_3_register_cancle));
		//		DMConfirmDialog.show(context, config, new DMComfirmDialogListener()
		//		{
		//			@Override
		//			public void onComfirmOk(AlertDialog dlg)
		//			{
		//				if (ConfigUtil.isTgVerify())
		//				{
		//					//托管方处理身份、手机、邮箱验证
		//					Intent intent = new Intent(DMConstant.TgAction.REGISTER_ACTION);
		//					intent.putExtra("thirdRegistUrlString", url);
		//					intent.putExtra("cookies", CookieUtil.getmCookie());
		//					context.sendBroadcast(intent);
		//				}
		//				else
		//				{
		//					UserInfo userInfo = DMApplication.getInstance().getUserInfo();
		//					if (userInfo.isIdcardVerified() && userInfo.isEmailVerified() && userInfo.isMobileVerified())
		//					{
		//						Intent intent = new Intent(DMConstant.TgAction.REGISTER_ACTION);
		//						intent.putExtra("thirdRegistUrlString", url);
		//						intent.putExtra("cookies", CookieUtil.getmCookie());
		//						context.sendBroadcast(intent);
		//					}
		//					else
		//					{
		//						ToastUtil.getInstant().show(context, "第三方账号开户必须先手机号码认证、电子邮箱认证、真空身份认证全部通过！");
		//						Intent intent = new Intent(context, UserInfoActivity.class);
		//						context.startActivity(intent);
		//					}
		//				}
		//				dlg.dismiss();
		//				
		//			}
		//			
		//			@Override
		//			public void onComfirmCancel()
		//			{
		//				//不做处理
		//			}
		//		});
	}
	
	/**
	 * 弹出托管已注册但未转账授权对话框
	 */
	public static void confirmDebitAuth(final Context context, final String url)
	{
		//		DMComfirmDialogConfig config = DMConfirmDialog.getConfigInstance();
		//		config.setTitle(context.getString(R.string.wenxi_tishi));
		//		config.setContent(context.getString(R.string.no_3_debit_auth));
		//		config.setOkStr(context.getString(R.string.no_3_debit_auth_ok));
		//		config.setCancelStr(context.getString(R.string.no_3_debit_auth_cancle));
		//		DMConfirmDialog.show(context, config, new DMComfirmDialogListener()
		//		{
		//			@Override
		//			public void onComfirmOk(AlertDialog dlg)
		//			{
		//				//托管方处理身份、手机、邮箱验证
		//				Intent intent = new Intent(DMConstant.TgAction.DEBITAUTH_ACTION);
		//				intent.putExtra("debitAuthUrl", url);
		//				intent.putExtra("cookies", CookieUtil.getmCookie());
		//				context.sendBroadcast(intent);
		//				
		//				dlg.dismiss();
		//			}
		//			
		//			@Override
		//			public void onComfirmCancel()
		//			{
		//				//不做处理
		//			}
		//		});
	}
	
	/**
	 * 是否已经完成了安全信息认证
	 * @param context
	 * @param accountInfo
	 * @param isNeedEmailRZ
	 * @param isNeedPsw
	 * @param isTg  是否托管
	 * @return
	 */
	public static boolean hasCompletedSecurityInfo(final Context context, UserInfo userInfo, boolean isNeedEmailRZ,
		final boolean isNeedPsw)
	{// 安全信息认证完成
		if (null == userInfo)
		{
			return false;
		}
		if ((StringUtils.isEmptyOrNull(userInfo.getRealName())) || StringUtils.isEmptyOrNull(userInfo.getPhone())
			|| (StringUtils.isEmptyOrNull(userInfo.getEmail()) && isNeedEmailRZ) || (!userInfo.isWithdrawPsw() && isNeedPsw))
		{
			String securityStr = getSecurityStr(userInfo, isNeedEmailRZ, isNeedPsw);
			AlertDialogUtil.confirm(context, securityStr, "认证", "确定", new ConfirmListener()
			{ //R.string.bid_donation_complete_security_info
					@Override
					public void onOkClick()
					{
						context.startActivity(new Intent(context, SecurityInfoActivity.class).putExtra("isNeedPwd", isNeedPsw));
					}
					
					@Override
					public void onCancelClick()
					{
					}
				});
			return false;
		}
		return true;
	}
	
	/***
	 * 获取用户安全信息完成的情况
	 * @param userInfo
	 * @param isNeedEmailRZ
	 * @param isNeedPsw
	 * @return
	 */
	public static String getSecurityStr(UserInfo userInfo, boolean isNeedEmailRZ, boolean isNeedPsw)
	{
		String securityInfo = "您必须先完成";
		if (StringUtils.isEmptyOrNull(userInfo.getRealName()))
		{//为完成实名认证，并且真实名字为空，则表示没有完成实名认证
			securityInfo = securityInfo + "实名认证、";
		}
		if (StringUtils.isEmptyOrNull(userInfo.getPhone()))
		{
			securityInfo = securityInfo + "手机号认证、";
		}
		if (StringUtils.isEmptyOrNull(userInfo.getEmail()) && isNeedEmailRZ)
		{
			securityInfo = securityInfo + "邮箱认证、";
		}
		if (!userInfo.isWithdrawPsw() && isNeedPsw)
		{
			securityInfo = securityInfo + "设置交易密码、";
		}
		securityInfo = securityInfo.substring(0, securityInfo.length() - 1) + "！";
		return securityInfo;
	}
	
	/**
	 * 退出登录的具体操作
	 */
	public static void doLoginOut(final Activity activity, final boolean isFromModify)
	{
		HttpParams httpParams = new HttpParams();
		HttpUtil.getInstance().post(activity, DMConstant.API_Url.USER_LOGINOUT, httpParams, new HttpCallBack()
		{
			@Override
			public void onSuccess(JSONObject result)
			{
				doAfterLoginOut(activity, isFromModify);
				activity.finish();
			}
			
			@Override
			public void onFailure(Throwable t, Context context)
			{
				super.onFailure(t, context);
				doAfterLoginOut(activity, isFromModify);
				activity.finish();
			}
			
			@Override
			public void onStart()
			{
				setShowProgress(false);
			}
		});
	}
	
	/**
	 * 退出登录
	 */
	private static void doAfterLoginOut(Context context, boolean isFromModify)
	{
		CookieUtil.setmCookie(null, context);
		DMApplication.getInstance().setUserInfo(null);
		Intent intent = new Intent(DMConstant.BroadcastActions.USER_SESSION_LOGOUT);
		context.sendBroadcast(intent);
		SharedPreferenceUtils.put(context, SharedPreferenceUtils.KEY_HAS_LOCKED, false);
		MainActivity.index = 0;
		
		//		if (isFromModify)
		//		{
		//			AppManager.getAppManager().finishActivity(PwdManagerActivity.class);
		//			AppManager.getAppManager().finishActivity(SettingActivity.class);
		//			AppManager.getAppManager().finishActivity(ModifyLoginPwdActivity.class);
		//		}
	}
	
	/**
	 * 标的类型标识（实、信、保、抵）
	 */
	public static HashMap<String, Integer> bidTyeImgs = new HashMap<String, Integer>(4);
	
	public static void initBidTyepImgs()
	{
		if (UIHelper.bidTyeImgs == null)
		{
			UIHelper.bidTyeImgs = new HashMap<String, Integer>(4);
		}
		UIHelper.bidTyeImgs.put("实", R.drawable.icon_shi);
		UIHelper.bidTyeImgs.put("信", R.drawable.icon_xin);
		UIHelper.bidTyeImgs.put("保", R.drawable.icon_bao);
		UIHelper.bidTyeImgs.put("抵", R.drawable.icon_di);
	}
	
	/**
	 * 隐藏输入法然后执行某些操作
	 * @param runnable
	 */
	public static void hideSrfAndRun(Activity activity, Runnable runnable)
	{
		// 隐藏输入法
		InputMethodManager im1 = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
		im1.hideSoftInputFromWindow(activity.getCurrentFocus().getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		new Handler().postDelayed(runnable, 100);
	}
	
	/***
	 * 支付密码输入弹框
	 * @param context
	 */
	public static void showPayPwdEditDialog(final Context context, String okText, final OnDealPwdOkListener onDealPwdOkListener)
	{
		View view = View.inflate(context, R.layout.pay_pwd_alert, null);
		final AlertDialog dlg = new AlertDialog.Builder(context).create();
		dlg.setView(view);
		dlg.show();
		Window window = dlg.getWindow();
		window.setContentView(R.layout.pay_pwd_alert);
		final EditText deal_pwd_et = (EditText)window.findViewById(R.id.deal_pwd_et);
		
		TextView pay_tv = (TextView)window.findViewById(R.id.pay_tv);
		if (null != okText)
		{
			pay_tv.setText(okText);
		}
		//支付
		pay_tv.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				String dealPwd = deal_pwd_et.getText().toString().trim();
				if (dealPwd.length() == 0)
				{
					ToastUtil.getInstant().show(context, "请输入交易密码");
				}
				else if (!FormatUtil.validateDealPwd(dealPwd))
				{
					ToastUtil.getInstant().show(context, "交易密码由8-16位的字母+数字组成");
				}
				else
				{
					onDealPwdOkListener.onDealPwdOk(dealPwd);
					dlg.dismiss();
				}
			}
		});
		//取消支付
		window.findViewById(R.id.close_iv).setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				dlg.dismiss();
			}
		});
		dlg.setCancelable(false);
		dlg.setCanceledOnTouchOutside(false);
	}
	
	public interface OnDealPwdOkListener
	{
		public void onDealPwdOk(String dealPassword);
	}
	
	public interface OnOkClickListener
	{
		public void onOkClick();
		
		public void onCanceClick();
	}
	
	/***
	 * 弹出文本列表框
	 * @param context
	 * @param textListDatas
	 * @param onDealPwdOkListener
	 */
	public static void showTextListDialog(Context context, String okBtn, String cancelBtn, List<String> textListDatas,
		final OnOkClickListener onOkClickListener)
	{
		View view = View.inflate(context, R.layout.my_loan_repayment, null);
		final AlertDialog dlg = new AlertDialog.Builder(context).create();
		dlg.setView(view);
		dlg.show();
		Window window = dlg.getWindow();
		window.setContentView(R.layout.my_loan_repayment);
		TextView btn_ok = (TextView)window.findViewById(R.id.btn_ok);
		if (null != okBtn)
		{
			btn_ok.setText(okBtn);
		}
		TextView btn_cancel = (TextView)window.findViewById(R.id.btn_cancel);
		if (null != cancelBtn)
		{
			btn_cancel.setText(cancelBtn);
		}
		
		LinearLayout content_ll = (LinearLayout)window.findViewById(R.id.content_ll);
		if (null != textListDatas && textListDatas.size() > 0)
		{
			for (String textItem : textListDatas)
			{
				TextView textView = (TextView)LayoutInflater.from(context).inflate(R.layout.text_view_item, content_ll, false);
				textView.setText(textItem);
				content_ll.addView(textView);
			}
		}
		btn_ok.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				onOkClickListener.onOkClick();
				dlg.dismiss();
			}
		});
		btn_cancel.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				onOkClickListener.onCanceClick();
				dlg.dismiss();
			}
		});
		dlg.setCancelable(false);
		dlg.setCanceledOnTouchOutside(false);
	}
	
	/**
	 * 转让弹出框
	 * @param context
	 * @param okBtn
	 * @param cancelBtn
	 * @param textArray
	 * @param onOkClickListener
	 */
	public static void showTurnDialog(final Context context, String okBtn, String cancelBtn, String[] textArray,
		final boolean isNeedPwd, final OnTurnClickListener OnturnClickListener)
	{
		View view = View.inflate(context, R.layout.my_invest_payment_turn_dialog, null);
		final AlertDialog dlg = new AlertDialog.Builder(context).create();
		dlg.setView(view);
		dlg.show();
		Window window = dlg.getWindow();
		window.setContentView(R.layout.my_invest_payment_turn_dialog);
		TextView creditorRight_value_tv = (TextView)window.findViewById(R.id.creditorRight_value_tv);
		TextView transferRate_tv = (TextView)window.findViewById(R.id.transferRate_tv);
		final EditText price_et = (EditText)window.findViewById(R.id.input_price_text);
		final EditText tradPwd_et = (EditText)window.findViewById(R.id.input_traPwd_text);
		tradPwd_et.setVisibility(isNeedPwd ? View.VISIBLE : View.GONE);
		creditorRight_value_tv.setText(context.getString(R.string.creditor_right_value, textArray[0]));
		transferRate_tv.setText(context.getString(R.string.creditor_transfer_rate, textArray[1]));
		
		price_et.addTextChangedListener(new TextWatcher()
		{
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after)
			{
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count)
			{
				if (price_et.getText().toString().trim().startsWith("0") || price_et.getText().toString().trim().startsWith(".")) // 开头不能是0或者.
				{
					price_et.setText("");
					ToastUtil.getInstant().show(context, "转让金额不能小于1");
				}
				
			}
			
			@Override
			public void afterTextChanged(Editable s)
			{
				// TODO Auto-generated method stub
				
			}
		});
		
		TextView btn_ok = (TextView)window.findViewById(R.id.btn_turn);
		if (null != okBtn)
		{
			btn_ok.setText(okBtn);
		}
		TextView btn_cancel = (TextView)window.findViewById(R.id.btn_cancel);
		if (null != cancelBtn)
		{
			btn_cancel.setText(cancelBtn);
		}
		
		btn_ok.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				String price = price_et.getText().toString().trim();
				String tradPwd = tradPwd_et.getText().toString().trim();
				
				if (price == null || "".equals(price))
				{
					ToastUtil.getInstant().show(context, "请输入转让价格");
					return;
				}
				if (isNeedPwd && (tradPwd == null || "".equals(tradPwd)))
				{
					ToastUtil.getInstant().show(context, "请输入交易密码");
					return;
				}
				OnturnClickListener.onOkClick(price, tradPwd);
				dlg.dismiss();
				
			}
		});
		btn_cancel.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				dlg.dismiss();
			}
		});
		dlg.setCancelable(false);
		dlg.setCanceledOnTouchOutside(false);
	}
	
	public interface OnTurnClickListener
	{
		public void onOkClick(String salePrice, String tradPwd);
	}
	
	/**
	 * 改变金额字体大小
	 * @param textView
	 * @param amount
	 */
	public static void formatMoneyTextSize(TextView textView, String amount, float size)
	{
		SpannableString amountSB = null;
		if (amount.endsWith("万元") || amount.endsWith("百万"))
		{
			amountSB = new SpannableString(amount);
			amountSB.setSpan(new RelativeSizeSpan(size),
				amount.length() - 2,
				amount.length(),
				SpannableString.SPAN_INCLUSIVE_EXCLUSIVE);
		}
		else if (amount.endsWith("元") || amount.endsWith("亿"))
		{
			amountSB = new SpannableString(amount);
			amountSB.setSpan(new RelativeSizeSpan(size),
				amount.length() - 1,
				amount.length(),
				SpannableString.SPAN_INCLUSIVE_EXCLUSIVE);
		}
		textView.setText(amountSB);
	}
	
	/**
	 * 格式化日期文字大小
	 * @param textView
	 * @param terms
	 */
	public static void formatDateTextSize(TextView textView, String terms)
	{
		SpannableString termsSB = null;
		if (terms.endsWith("个月"))
		{
			termsSB = new SpannableString(terms);
			termsSB.setSpan(new RelativeSizeSpan(0.5f),
				terms.length() - 2,
				terms.length(),
				SpannableString.SPAN_INCLUSIVE_EXCLUSIVE);
		}
		else if (terms.endsWith("天"))
		{
			termsSB = new SpannableString(terms);
			termsSB.setSpan(new RelativeSizeSpan(0.5f),
				terms.length() - 1,
				terms.length(),
				SpannableString.SPAN_INCLUSIVE_EXCLUSIVE);
		}
		textView.setText(termsSB);
	}
	
	/**
	 * 控件抖动效果
	 * @param mContext
	 * @param v
	 */
	public static void showViewShake(Context mContext, View v)
	{
		Animation shake = AnimationUtils.loadAnimation(mContext, R.anim.shake);//加载动画资源文件
		v.startAnimation(shake); //给组件播放动画效果
	}
	
	/**
	 * 设置数字颜色
	 * @param str
	 * @param a
	 * @param b
	 * @return
	 */
	public static SpannableString makeSpannableStr(Context mContext, String str, int a, int b)
	{
		SpannableString spanStr = new SpannableString(str);
		spanStr.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.orange)),
			a,
			b,
			SpannableString.SPAN_INCLUSIVE_EXCLUSIVE);
		if (b != str.length())
		{
			spanStr.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.text_black_6)),
				b,
				str.length(),
				SpannableString.SPAN_INCLUSIVE_EXCLUSIVE);
		}
		return spanStr;
	}
}
