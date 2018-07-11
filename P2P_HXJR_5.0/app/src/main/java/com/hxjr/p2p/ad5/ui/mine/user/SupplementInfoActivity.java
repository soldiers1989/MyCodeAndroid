package com.hxjr.p2p.ad5.ui.mine.user;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.dm.http.HttpCallBack;
import com.dm.http.HttpUtil;
import com.dm.utils.AppManager;
import com.dm.utils.DMJsonObject;
import com.dm.utils.DMLog;
import com.dm.widgets.utils.AlertDialogUtil;
import com.hxjr.p2p.ad5.R;
import com.hxjr.p2p.ad5.bean.RegexInfo;
import com.hxjr.p2p.ad5.service.ApiUtil;
import com.hxjr.p2p.ad5.ui.BaseActivity;
import com.hxjr.p2p.ad5.ui.mine.evaluation.EvaluationActivity;
import com.hxjr.p2p.ad5.ui.mine.setting.gesture.UnlockGesturePasswordActivity;
import com.hxjr.p2p.ad5.utils.DMConstant;
import com.hxjr.p2p.ad5.utils.FormatUtil;
import com.hxjr.p2p.ad5.utils.HttpParams;
import com.hxjr.p2p.ad5.utils.SharedPreferenceUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 完善资料
 * @author  jiaohongyun
 * @date 2015年11月10日
 */
public class SupplementInfoActivity extends BaseActivity
{
	/**
	 * 用户名
	 */
	private EditText userName;
	
	/**
	 * 密码
	 */
	private EditText pwd;
	
	/**
	 * 再次输入的密码
	 */
	private EditText rePwd;
	
	/**
	 * 邀请码
	 */
	private EditText inviteCode;
	
	/**手机号*/
	private String phone;
	
	/**验证码*/
	private String verifyCode;
	
	private RegexInfo regexInfo;
	
	/**第三方登录时注册使用的accessToken*/
	private String accessToken;
	
	/**第三方登录时注册使用的openId*/
	private String openId;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.supplement_info);
		regexInfo = FormatUtil.initRegexInfo(this);
		phone = getIntent().getStringExtra("phone");
		verifyCode = getIntent().getStringExtra("verifyCode");
		accessToken = getIntent().getStringExtra("accessToken");
		openId = getIntent().getStringExtra("openId");
		initView();
	}
	
	@Override
	protected void initView()
	{
		super.initView();
		((TextView)findViewById(R.id.title_text)).setText(R.string.supplement_info);
		
		userName = (EditText)findViewById(R.id.userName);
		pwd = (EditText)findViewById(R.id.pwd);
		rePwd = (EditText)findViewById(R.id.rePwd);
		inviteCode = (EditText)findViewById(R.id.inviteCode);
	}
	
	/**用户名**/
	private String account;
	
	/**登录密码**/
	private String userPwd;
	
	/**
	 * 提交资料
	 * @param v
	 */
	public void submit(View v)
	{
		if (checkClick(v.getId())) //防重复点击	
		{
			if (checkParams())
			{
				account = userName.getText().toString();
				userPwd = pwd.getText().toString();
				HttpParams httpParams = new HttpParams();
				httpParams.put("phone", phone);
				httpParams.put("verifyCode", verifyCode);
				httpParams.put("type", "LC");
				httpParams.put("accountName", userName.getText().toString());
				httpParams.put("password", pwd.getText().toString());
				httpParams.put("newPassword", pwd.getText().toString());
				httpParams.put("userRole","INVESTOR");
				
				if (null != accessToken)
				{
					httpParams.put("accessToken", accessToken);
					httpParams.put("openId", openId);
					account = openId;
					userPwd = accessToken;
				}
				if (!inviteCode.getText().toString().isEmpty())
				{
					httpParams.put("code", inviteCode.getText().toString());
				}
				
				HttpUtil.getInstance().post(this, DMConstant.API_Url.USER_REGISTER, httpParams, new HttpCallBack()
				{
					@Override
					public void onSuccess(JSONObject result)
					{
						try
						{
							String code = result.getString("code");
							
							if (DMConstant.ResultCode.SUCCESS.equals(code))
							{
								if (!TextUtils.isEmpty(accessToken))
								{//如果accessToken不为空，则表示使用的第三方登录
									SharedPreferenceUtils.put(SupplementInfoActivity.this, "isThirdLogin", true);
								}
								else
								{
									SharedPreferenceUtils.put(SupplementInfoActivity.this, "isThirdLogin", false);
								}
								DMJsonObject data = new DMJsonObject(result.getString("data"));
								if (data != null)
								{
									DMLog.e(data.toString());
//									UserInfo userInfo = new UserInfo(data);
//									DMLog.e(userInfo.toString());
//									AccountBean accountBean=new AccountBean(data);
//									DMLog.e(accountBean.toString());
									ApiUtil.getUserInfo(SupplementInfoActivity.this);
//									UserInfo userInfo=new UserInfo();
//									userInfo.setId(data.getInt("id"));
//									userInfo.setSafeLevel( data.getInt("safeLevel"));
//									userInfo.setAccountName(data.getString("userName"));
//									DMLog.e(userInfo.toString());
//									DMApplication.getInstance().setUserInfo(userInfo);
//									DMLog.e(userInfo.toString());
									
									//注册成功，表示还没有在我的界面弹出完善安全信息认证的提示
									SharedPreferenceUtils.put(SupplementInfoActivity.this, "hasShowSecurityDialog", false);
									Intent intent = null;
									
									//必须设置解锁手势密码
//									intent = new Intent(SupplementInfoActivity.this, SetGesturePasswordActivity.class);
//									intent.putExtra("from", "login");
//									intent.putExtra("account", account);
//									intent.putExtra("pwd", userPwd);
//									intent.putExtra("isRegister", true);
//									intent.putExtra("password", userPwd);
//									startActivity(intent);
									//进入风险评估
									intent = new Intent(SupplementInfoActivity.this, EvaluationActivity.class);
									intent.putExtra("SetGesture", "SetGesture");
									intent.putExtra("password", userPwd);
									intent.putExtra("account", account);
									intent.putExtra("pwd", userPwd);
									startActivity(intent);
									Activity register = AppManager.getAppManager().getActivity(RegisterActivity.class);
									if (null != register)
									{ //有可能是从第三方过来，所以没有前面验证码一步
										AppManager.getAppManager().finishActivity(RegisterActivity.class);
									}
									Activity unlock = AppManager.getAppManager().getActivity(UnlockGesturePasswordActivity.class);
									if (null != unlock)
									{
										AppManager.getAppManager().finishActivity(UnlockGesturePasswordActivity.class);
									}
									AppManager.getAppManager().finishActivity(LoginActivity.class);
									finish();
								}
							}
							//							else if (ErrorUtil.ErroreCode.ERROR_000044.equals(code))
							//							{
							//								AlertDialogUtil.alert(SupplementInfoActivity.this,
							//									result.getString("description").replace("/业务员工号", ""));
							//							}
							else
							{
								// 注册失败
								AlertDialogUtil.alert(SupplementInfoActivity.this, result.getString("description"));
							}
						}
						catch (JSONException e)
						{
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					
					@Override
					public void onFailure(Throwable t, Context context)
					{
						super.onFailure(t, context);
					}
				});
			}
		}
	}
	
	/**
	 * @return
	 */
	private boolean checkParams()
	{
		if (userName.getText().toString().isEmpty())
		{
			//用户名不能为空！
			AlertDialogUtil.alert(SupplementInfoActivity.this, getString(R.string.supplement_error1));
			return false;
		}
		
		//		if (!FormatUtil.validateUserName(userName.getText().toString()))
		if (!FormatUtil.matches(regexInfo.getNewUserNameRegex(), userName.getText().toString()))
		{
			//用户名6-18个字符，可使用字母、数字、下划线,需以字母开头
			//			AlertDialogUtil.alert(SupplementInfoActivity.this, getString(R.string.supplement_error2));
			AlertDialogUtil.alert(SupplementInfoActivity.this, "用户名" + regexInfo.getUserNameRegexContent().replace("用户名", ""));
			return false;
		}
		if (pwd.getText().toString().isEmpty())
		{
			//密码不能为空！
			AlertDialogUtil.alert(SupplementInfoActivity.this, getString(R.string.supplement_error3));
			return false;
		}
		if (!FormatUtil.matches(regexInfo.getNewPasswordRegex(), pwd.getText().toString())) //!FormatUtil.validateLoginPwd(pwd.getText().toString())
		{
			//密码6-16位支持数字、下划线和字母，区分大小写！
			//			AlertDialogUtil.alert(SupplementInfoActivity.this, getString(R.string.supplement_error4));
			AlertDialogUtil.alert(SupplementInfoActivity.this, "密码" + regexInfo.getPasswordRegexContent().replace("密码", ""));
			return false;
		}
		if (rePwd.getText().toString().isEmpty())
		{
			AlertDialogUtil.alert(this, getString(R.string.repwd_is_null));
			return false;
		}
		if (!pwd.getText().toString().equals(rePwd.getText().toString()))
		{
			//两次输入的密码不一样
			AlertDialogUtil.alert(SupplementInfoActivity.this, getString(R.string.supplement_error5));
			return false;
		}
		if (pwd.getText().toString().equals(rePwd.getText().toString()))
		{
			//不能输入带空格字符
			if (pwd.getText().toString().contains(" ") || rePwd.getText().toString().contains(" "))
			{
				AlertDialogUtil.alert(SupplementInfoActivity.this, getString(R.string.supplement_pwd_format_error));
				return false;
			}
		}
		return true;
	}
	
}
