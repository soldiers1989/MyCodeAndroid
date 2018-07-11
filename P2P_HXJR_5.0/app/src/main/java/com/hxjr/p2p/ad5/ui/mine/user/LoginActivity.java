package com.hxjr.p2p.ad5.ui.mine.user;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.dm.db.DbUtils;
import com.dm.db.exception.DbException;
import com.dm.db.sqlite.Selector;
import com.dm.http.HttpCallBack;
import com.dm.http.HttpUtil;
import com.dm.utils.AppManager;
import com.dm.utils.DMJsonObject;
import com.dm.utils.DMLog;
import com.dm.utils.EncrypUtil;
import com.dm.utils.MD5Util;
import com.dm.utils.NetworkUtils;
import com.dm.utils.ProgressDialogShowing;
import com.dm.widgets.utils.AlertDialogUtil;
import com.dm.widgets.utils.ToastUtil;
import com.hxjr.p2p.ad5.DMApplication;
import com.hxjr.p2p.ad5.R;
import com.hxjr.p2p.ad5.bean.LockPwd;
import com.hxjr.p2p.ad5.bean.UserInfo;
import com.hxjr.p2p.ad5.ui.BaseActivity;
import com.hxjr.p2p.ad5.ui.MainActivity;
import com.hxjr.p2p.ad5.ui.mine.setting.gesture.SetGesturePasswordActivity;
import com.hxjr.p2p.ad5.ui.mine.setting.gesture.UnlockGesturePasswordActivity;
import com.hxjr.p2p.ad5.utils.DMConstant;
import com.hxjr.p2p.ad5.utils.ErrorUtil;
import com.hxjr.p2p.ad5.utils.HttpParams;
import com.hxjr.p2p.ad5.utils.SharedPreferenceUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;

/**
 * 登录页面
 * @author jiaohongyun
 */
public class LoginActivity extends BaseActivity implements OnClickListener, Callback, PlatformActionListener
{
	private static final int MSG_AUTH_CANCEL = 1;
	
	private static final int MSG_AUTH_ERROR = 2;
	
	private static final int MSG_AUTH_COMPLETE = 3;
	
	/**
	 * 用户名输入框
	 */
	private EditText nameEditText;
	
	/**
	 * 密码输入框
	 */
	private EditText pwdEditText;
	
	/**
	 * 忘记密码
	 */
	private TextView forgetPwd;
	
	/**
	 * 登录按钮
	 */
	private Button loginBtn;
	
	/**
	 * 注册
	 */
	private Button regBtn;
	
	private Button qq_btn;
	
	private Button xl_btn;
	
	private Handler handler;
	
	private String phoneNumber;
	
	/**
	 * 是否忘记手势密码， 是则需要删除手势密码
	 */
	protected boolean forgetLockPwd = false;
	
	private boolean isfromMine;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		forgetLockPwd = getIntent().getBooleanExtra("forgetLockPwd", false);
		phoneNumber = getIntent().getStringExtra("phoneNumber");
		isfromMine = getIntent().getBooleanExtra("isfromMine", false);
		//		checkTimeOUt(null);
		initView();
	}
	
	@Override
	protected void onNewIntent(Intent intent)
	{
		super.onNewIntent(intent);
		phoneNumber = intent.getStringExtra("phoneNumber");
		isfromMine = intent.getBooleanExtra("isfromMine", false);
		if (!TextUtils.isEmpty(phoneNumber))
		{
			nameEditText.setText(phoneNumber);
			pwdEditText.requestFocus();
		}
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		ShareSDK.initSDK(this);
	}
	
	@Override
	protected void onDestroy()
	{
		DMLog.e("Login onDestroy");
		ShareSDK.stopSDK(this);
		super.onDestroy();
	}
	
	private void checkTimeOUt(TimingCallBack callBack)
	{
		DMApplication.getInstance().autoLogin(false, null, callBack);
	}
	
	/**
	 * 初始化页面
	 */
	@Override
	protected void initView()
	{
		super.initView();
		((TextView)findViewById(R.id.title_text)).setText(R.string.login);
		nameEditText = (EditText)findViewById(R.id.login_name);
		pwdEditText = (EditText)findViewById(R.id.login_pwd);
		forgetPwd = (TextView)findViewById(R.id.login_forget_pwd);
		loginBtn = (Button)findViewById(R.id.login_ok_btn);
		regBtn = (Button)findViewById(R.id.register_btn);
		qq_btn = (Button)findViewById(R.id.qq_btn);
		xl_btn = (Button)findViewById(R.id.xl_btn);
		
		forgetPwd.setOnClickListener(this);
		loginBtn.setOnClickListener(this);
		regBtn.setOnClickListener(this);
		qq_btn.setOnClickListener(this);
		xl_btn.setOnClickListener(this);
		
		handler = new Handler(this);
		if (!TextUtils.isEmpty(phoneNumber))
		{
			nameEditText.setText(phoneNumber);
		}
	}
	
	@Override
	public void onClick(View v)
	{
		if (checkClick(v.getId()))
		{
			switch (v.getId())
			{
				case R.id.login_forget_pwd:
				{
					// 忘记密码
					startActivity(new Intent(this, FindPwdActivity.class));
					break;
				}
				case R.id.login_ok_btn:
					// 登录
					if (validate())
					{
						SharedPreferenceUtils.put(LoginActivity.this, "isThirdLogin", false);
						checkTimeOUt(new TimingCallBack()
						{
							@Override
							public void onServerError()
							{
								((ProgressDialogShowing)LoginActivity.this).dismiss();
								// 服务器内部错误
								ToastUtil.getInstant().show(LoginActivity.this, "服务器内部错误");
							}
							
							@Override
							public void onConnectError()
							{
								((ProgressDialogShowing)LoginActivity.this).dismiss();
								//网络错误	
								ToastUtil.getInstant().show(LoginActivity.this, R.string.net_connection_error);
							}
							
							@Override
							public void doTimingCallBack()
							{
								login();
							}
							
							@Override
							public void onStart()
							{
								((ProgressDialogShowing)LoginActivity.this).show();
							}
						});
					}
					break;
				case R.id.register_btn:
					// 注册
					startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
				break;
				case R.id.xl_btn:
					//新浪微博
					if (NetworkUtils.isNetworkAvailable(this))
					{
						authorize(ShareSDK.getPlatform(SinaWeibo.NAME));
					}
					else
					{
						ToastUtil.getInstant().show(LoginActivity.this, R.string.net_connection_error);
					}
					break;
				
				case R.id.qq_btn:
					//QQ空间
					if (NetworkUtils.isNetworkAvailable(this))
					{
						authorize(ShareSDK.getPlatform(QQ.NAME));
					}
					else
					{
						ToastUtil.getInstant().show(LoginActivity.this, R.string.net_connection_error);
					}
					break;
				default:
					break;
			}
		}
	}
	
	/**
	 * 登录
	 */
	private void login()
	{
		String url = DMConstant.API_Url.USER_LOGIN;
		HttpParams httpParams = new HttpParams();
		long time = System.currentTimeMillis() - DMApplication.getInstance().diffTime;
		
		String flag = MD5Util.getFlag(nameEditText.getText().toString(), pwdEditText.getText().toString(), time + "");
		final String password = pwdEditText.getText().toString();
		final String accountName = nameEditText.getText().toString();
		httpParams.put("flag", flag);
		httpParams.put("accountName", accountName);
		httpParams.put("time", time);
		httpParams.put("password", password);
		HttpUtil.getInstance().post(this, url, httpParams, new HttpCallBack()
		{
			@Override
			public void onSuccess(JSONObject result)
			{
				ErrorUtil.isGoToLogin = false;
				((ProgressDialogShowing)LoginActivity.this).dismiss();
				//保存用户信息
				saveUserInfo(result, accountName, password);
				//主要用于更新登录状态和更新客户端和服务器端时间差
				Intent intent = new Intent();
				intent.setAction(DMConstant.BroadcastActions.USER_SESSION_LOGIN);
				sendBroadcast(intent);
			}
			
			@Override
			public void onFailure(Throwable t, Context context)
			{
				super.onFailure(t, context);
				((ProgressDialogShowing)LoginActivity.this).dismiss();
			}
			
			@Override
			public void onStart()
			{
				super.onStart();
				setShowProgress(false);
			}
		});
	}
	
	/**
	 * 解析并保存用户对象
	 * 
	 * @param result
	 * @param accountName 
	 * @param password 
	 */
	private void saveUserInfo(JSONObject result, String accountName, String password)
	{
		try
		{
			String code = result.getString("code");
			if (DMConstant.ResultCode.SUCCESS.equals(code))
			{
				// 成功
				DMJsonObject data = new DMJsonObject(result.getString("data"));
				final UserInfo userInfo = new UserInfo(data);
				DMApplication.getInstance().setUserInfo(userInfo);
				Intent intent = null;
				LockPwd lockPwd = null;
				DbUtils db = DbUtils.create(this);
				//将手势密码保存到数据库
				try
				{
					db.createTableIfNotExist(LockPwd.class);
					lockPwd = db.findFirst(Selector.from(LockPwd.class).where("userId", "=", userInfo.getId()));
					if (lockPwd != null)
					{//表示之前已经设置过手势密码，即已经锁定，在SplashActivity的redirectTo()方法中用到
						SharedPreferenceUtils.put(LoginActivity.this, SharedPreferenceUtils.KEY_HAS_LOCKED, true);
						SharedPreferenceUtils.put(LoginActivity.this, SharedPreferenceUtils.KEY_USER_ID, userInfo.getId());
						lockPwd.setLoginPwd(EncrypUtil.encrypt(password, DMConstant.StringConstant.ENCRYP_SEND));
						db.saveOrUpdate(lockPwd);
					}
				}
				catch (DbException e)
				{
					e.printStackTrace();
				}
				finally
				{
					db.close();
				}
				if (lockPwd == null || (lockPwd != null && userInfo.getId() == lockPwd.getUserId() && forgetLockPwd))
				{//如果没有设置过手势密码，或者在解锁界面点击了忘记手势密码但是登录的用户和上次登录的用户相同
					AppManager.getAppManager().finishActivity(UnlockGesturePasswordActivity.class);
					intent = new Intent(LoginActivity.this, SetGesturePasswordActivity.class);
					intent.putExtra("from", "login");
					intent.putExtra("account", accountName);
					intent.putExtra("password", password);
					startActivity(intent);
				}
				else
				{
					if (null == AppManager.getAppManager().getActivity(MainActivity.class))
					{
						Intent intent1 = new Intent(LoginActivity.this, MainActivity.class);
						startActivity(intent1);
					}
					Activity unlock = AppManager.getAppManager().getActivity(UnlockGesturePasswordActivity.class);
					if (null != unlock)
					{
						AppManager.getAppManager().finishActivity(UnlockGesturePasswordActivity.class);
					}
				}
				if (isfromMine)
				{
					MainActivity.index = 3;
				}
				finish();
			}
			else
			{
				ErrorUtil.showError(result);
			}
		}
		catch (JSONException e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * 参数校验
	 * 
	 * @return
	 */
	private boolean validate()
	{
		//		boolean flag = false;
		if (nameEditText.getText().toString().isEmpty() || nameEditText.getText().toString().trim().equals(""))
		{
			//用户名不能为空
			AlertDialogUtil.alert(this, getString(R.string.user_name_empty));
			nameEditText.findFocus();
			return false;
		}
		
		String password = pwdEditText.getText().toString();
		
		if (password.isEmpty() || password.trim().equals(""))
		{
			//密码不能为空
			AlertDialogUtil.alert(this, getString(R.string.pwd_empty));
			pwdEditText.findFocus();
			return false;
		}
		
		//		// 密码需要满足的格式
		//		if (FormatUtil.validateLoginPwd(password))
		//		{
		//			flag = true;
		//		}
		//		else
		//		{
		//			flag = false;
		//			pwdEditText.setError(getResources().getString(R.string.login_pwd_error_detail));
		//			pwdEditText.requestFocus();
		//			return flag;
		//		}
		return true;
	}
	
	//执行授权,获取用户信息
	private void authorize(Platform plat)
	{
		if (plat == null)
		{
			return;
		}
		
		//		if (plat.getName().equals(SinaWeibo.NAME))
		//		{ //以下三行代码，执行后每次都会跳转到微博的登录页面
		//			//		SinaWeibo.ShareParams sp = new SinaWeibo.ShareParams();
		//			plat.removeAccount(true);
		//			ShareSDK.removeCookieOnAuthorize(true);
		//		}
		plat.setPlatformActionListener(this);
		//关闭SSO授权，使用web端授权
		//		plat.SSOSetting(true);
		//		plat.showUser(null);
		
		//使用平台客户端授权
		plat.SSOSetting(false);
		plat.authorize();
	}
	
	public void onComplete(Platform platform, int action, HashMap<String, Object> res)
	{
		if (action == Platform.ACTION_AUTHORIZING) //如果是使用web端授权的话，就应该是  ACTION_USER_INFOR
		{
			Message msg = new Message();
			msg.what = MSG_AUTH_COMPLETE;
			msg.obj = new Object[] {platform.getName(), res};
			handler.sendMessage(msg);
		}
	}
	
	public void onError(Platform platform, int action, Throwable t)
	{
		if (action == Platform.ACTION_AUTHORIZING) //如果是使用web端授权的话，就应该是  ACTION_USER_INFOR
		{
			handler.sendEmptyMessage(MSG_AUTH_ERROR);
		}
		t.printStackTrace();
	}
	
	public void onCancel(Platform platform, int action)
	{
		if (action == Platform.ACTION_AUTHORIZING) //如果是使用web端授权的话，就应该是  ACTION_USER_INFOR
		{
			handler.sendEmptyMessage(MSG_AUTH_CANCEL);
		}
	}
	
	public boolean handleMessage(Message msg)
	{
		switch (msg.what)
		{
			case MSG_AUTH_CANCEL:
				//取消授权
				ToastUtil.getInstant().show(this, R.string.auth_cancel);
				break;
			case MSG_AUTH_ERROR:
				//授权失败
				ToastUtil.getInstant().show(this, R.string.auth_error);
				break;
			case MSG_AUTH_COMPLETE:
				//授权成功
				Object[] objs = (Object[])msg.obj;
				String platformName = (String)objs[0];
				Platform platform = ShareSDK.getPlatform(this, platformName);
				accessToken = platform.getDb().getToken(); // 获取授权token
				openId = platform.getDb().getUserId(); // 获取用户在此平台的ID
				//				String gender = platform.getDb().getUserGender();
				//				String userName = platform.getDb().getUserName();
				sendThirdLoginRequest();
				break;
		}
		return false;
	}
	
	private String accessToken;
	
	private String openId;
	
	private void sendThirdLoginRequest()
	{
		HttpParams httpParams = new HttpParams();
		httpParams.put("openId", openId);
		httpParams.put("accessToken", accessToken);
		HttpUtil.getInstance().post(this, DMConstant.API_Url.THIRD_LOGIN, httpParams, new HttpCallBack()
		{
			@Override
			public void onSuccess(JSONObject result)
			{
				analyThirdLoginData(result);
			}
			
			@Override
			public void onFailure(Throwable t, Context context)
			{
				//				ToastUtil.getInstant().show(CommonThirdLoginActivity.this, "失败");
				super.onFailure(t, context);
			}
			
			@Override
			public void onLoading(Integer integer)
			{
			}
			
			@Override
			public void onStart()
			{
			}
		});
	}
	
	private void analyThirdLoginData(JSONObject result)
	{
		try
		{
			String code = result.getString("code");
			if (DMConstant.ResultCode.SUCCESS.equals(code))
			{//需要将登录密码返回，在设置修改交易密码以及手势密码里面会用到
				// 成功
				saveUserInfo(result, openId, accessToken);
				SharedPreferenceUtils.put(this, "isThirdLogin", true);
			}
			else if ("000002".equals(code))
			{
				JSONObject jsonObject;
				String accessToken = null;
				String openId = null;
				if (result.has("data"))
				{
					jsonObject = result.getJSONObject("data");
					if (jsonObject.has("qqToken"))
					{
						accessToken = jsonObject.getString("qqToken");
					}
					if (jsonObject.has("openId"))
					{
						openId = jsonObject.getString("openId");
					}
				}
				Intent intent = new Intent(this, SupplementInfoActivity.class);
				if (!TextUtils.isEmpty(accessToken))
				{
					intent.putExtra("accessToken", accessToken);
				}
				if (!TextUtils.isEmpty(openId))
				{
					intent.putExtra("openId", openId);
				}
				startActivity(intent);
			}
		}
		catch (JSONException e)
		{
			e.printStackTrace();
		}
	}
}
