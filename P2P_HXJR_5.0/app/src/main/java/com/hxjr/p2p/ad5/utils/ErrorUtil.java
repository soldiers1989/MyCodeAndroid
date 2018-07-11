/*
 * 文 件 名:  ErrorUtil.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  jiaohongyun
 * 修改时间:  2014年12月8日
 */
package com.hxjr.p2p.ad5.utils;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;

import com.dm.utils.AppManager;
import com.dm.widgets.utils.ToastUtil;
import com.hxjr.p2p.ad5.DMApplication;
import com.hxjr.p2p.ad5.ui.MainActivity;
import com.hxjr.p2p.ad5.ui.mine.user.LoginActivity;

/**
 * 错误处理
 * @author  jiaohongyun
 * @version  [版本号, 2014年12月8日]
 */
public class ErrorUtil
{
	/**
	 * 错误码处理
	 * @author  jiaohongyun
	 * @version  [版本号, 2015年1月26日]
	 */
	public interface ErroreCode
	{
		/**
		 * 成功
		 */
		public static String SUCCESS = "000000";
		
		/**
		 * 未知异常
		 */
		public static String ERROR_000001 = "000001";
		
		/**
		 * 未登录
		 */
		public static String ERROR_000002 = "000002";
		
		/**
		 * 用户名或者密码错误，登录失败
		 */
		public static String ERROR_000003 = "000003";
		
		/**
		 * 未知错误
		 */
		public static String ERROR_000004 = "000004";
		
		/**
		 * 两次密码不一样
		 */
		public static String ERROR_000005 = "000005";
		
		/**
		 * 验证码输入错误
		 */
		public static String ERROR_000006 = "000006";
		
		/**
		 * 注册用户必须先退出登录
		 */
		public static String ERROR_000007 = "000007";
		
		/**
		 * 没有查出数据
		 */
		public static String ERROR_000008 = "000008";
		
		/**
		 * 账户已经存在
		 */
		public static String ERROR_000009 = "000009";
		
		/**
		 * 不能和登录密码相同
		 */
		public static String ERROR_000010 = "000010";
		
		/**
		 * 支付密码已经存在
		 */
		public static String ERROR_000011 = "000011";
		
		/**
		 * 密码不能为空
		 */
		public static String ERROR_000012 = "000012";
		
		/**
		 * 两次密码输入不一
		 */
		public static String ERROR_000013 = "000013";
		
		/**
		 * 支付密码错误
		 */
		public static String ERROR_000014 = "000014";
		
		/**
		 * 未设置支付密码错误
		 */
		public static String ERROR_000015 = "000015";
		
		/**
		 * 自动登录sign信息错误
		 */
		public static String ERROR_000016 = "000016";
		
		/**
		 * 自动登录time核对错误
		 */
		public static String ERROR_000017 = "000017";
		
		/**
		 * 手机号码错误
		 */
		public static String ERROR_000018 = "000018";
		
		/**
		 * 手机号码已经存在
		 */
		public static String ERROR_000019 = "000019";
		
		/**
		 * 手机验证码错误
		 */
		public static String ERROR_000020 = "000020";
		
		/**
		 * 实名认证失败
		 */
		public static String ERROR_000021 = "000021";
		
		/**
		 * 实名认证已经存在
		 */
		public static String ERROR_000022 = "000022";
		
		/**
		 * 邮箱地址错误
		 */
		public static String ERROR_000023 = "000023";
		
		/**
		 * 邮箱验证码错误
		 */
		public static String ERROR_000024 = "000024";
		
		/**
		 * 邮箱已经存在
		 */
		public static String ERROR_000025 = "000025";
		
		/**
		 * 手机号码不存在
		 */
		public static String ERROR_000026 = "000026";
		
		/**
		 * 验证码类型不能为空
		 */
		public static String ERROR_000027 = "000027";
		
		/**
		 * 验证码类型错误
		 */
		public static String ERROR_000028 = "000028";
		
		/**
		 * 验证码生成错误
		 */
		public static String ERROR_000029 = "000029";
		
		/**
		 * 不能和支付密码相同
		 */
		public static String ERROR_000030 = "000030";
		
		/**
		 * 密码错误
		 */
		public static String ERROR_000031 = "000031";
		
		/**
		 * 充值类型错误
		 */
		public static String ERROR_000032 = "000032";
		
		/**
		 * 用户银行卡数量超出最大
		 */
		public static String ERROR_000033 = "000033";
		
		/**
		 * 银行卡号错误
		 */
		public static String ERROR_000034 = "000034";
		
		/**
		 * 银行卡号不存在
		 */
		public static String ERROR_000035 = "000035";
		
		/**
		 * 银行卡号已经存在
		 */
		public static String ERROR_000036 = "000036";
		
		/**
		 * 银行错误
		 */
		public static String ERROR_000037 = "000037";
		
		/**
		 * 未实名认证
		 */
		public static String ERROR_000038 = "000038";
		
		/**
		 * 金额输入错误
		 */
		public static String ERROR_000039 = "000039";
		
		/**
		 * 银行卡解除绑定失败
		 */
		public static String ERROR_000040 = "000040";
		
		/**
		 * 交易密码错误
		 */
		public static String ERROR_000044 = "000044";
		
		/**
		 * 注册第三方支付，但未转账授权
		 */
		public static String ERROR_000047 = "000047";
		
		/**
		 * 帐号已经被锁定
		 */
		public static String ERROR_000048 = "000048";
		
		/**
		 * 帐号已经被拉黑
		 */
		public static String ERROR_000049 = "000049";
		
		/**
		 * 未注册第三方支付
		 */
		public static String ERROR_000201 = "000201";
		
		/**
		 * 第三方支付注册用户URL生成错误
		 */
		public static String ERROR_000202 = "000202";
		
		/**
		 * 第三方支付充值金额输入错误
		 */
		public static String ERROR_000203 = "000203";
	}
	
	/**
	 * 是否正在跳转到登录页面
	 */
	public static boolean isGoToLogin = false;
	
	public static void showError(JSONObject result) throws JSONException
	{
		String description = result.getString("description");
		String code = result.getString("code");
		if (null == code)
		{
			return;
		}
		if (code.equals(ErroreCode.ERROR_000002) && DMApplication.getInstance().getUserInfo() != null)
		{ //超时处理
			goToLogin();
		}
		else if (code.equals(ErroreCode.ERROR_000047))
		{
			String url = result.getJSONObject("data").getString("url");
			UIHelper.confirmDebitAuth(DMApplication.getInstance(), url);
		}
		else if (code.equals(ErroreCode.ERROR_000048))
		{ //用户被锁定不能登录
			goToLogin();
		}
		else if (code.equals(ErroreCode.ERROR_000044) && (description != null && description.contains("锁定")))
		{ //用户被锁定不能登录
			goToLogin();
			ToastUtil.getInstant().show(DMApplication.getInstance(), description);
		}
		else if (code.equals(ErroreCode.ERROR_000049))
		{ //拉黑，按超时处理
			//			goToLogin();
			ToastUtil.getInstant().show(DMApplication.getInstance(), description);
		}
		else
		{
			ToastUtil.getInstant().show(DMApplication.getInstance(), description);
		}
	}
	
	/**
	 * 跳转到登录页面
	 */
	private static void goToLogin()
	{
		if (isGoToLogin)
		{
			//防止多个接口同时产生返回登录的请求
			return;
		}
		else
		{
			//跳转到登录页面
			final Activity activity = AppManager.getAppManager().currentActivity();
			if (activity.getClass().equals(LoginActivity.class))
			{
				//如果当前页面在登录页面，则不做任何处理
				isGoToLogin = false;
				return;
			}
			Intent intent = new Intent(activity, LoginActivity.class);
			intent.putExtra("timeOut", true);
			activity.startActivity(intent);
			if (!activity.getClass().equals(MainActivity.class))
			{
				//不在主页面时需要结束当前页面
				new Handler().postDelayed(new Runnable()
				{
					@Override
					public void run()
					{
						activity.finish();
					}
				}, 300);
			}
			//如果是需要跳转到登陆页面，那么index应该为0，即回到首页。同样，userInfo应该设置为null, 表示未登录状态
			MainActivity.index = 0;
			DMApplication.getInstance().setUserInfo(null);
			isGoToLogin = true;
		}
	}
}
