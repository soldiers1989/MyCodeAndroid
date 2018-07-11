package com.hxjr.p2p.ad5.ui.mine.setting;

import org.json.JSONException;
import org.json.JSONObject;

import com.hxjr.p2p.ad5.DMApplication;
import com.hxjr.p2p.ad5.bean.RegexInfo;
import com.hxjr.p2p.ad5.ui.BaseActivity;
import com.hxjr.p2p.ad5.ui.mine.user.FindPwdActivity;
import com.hxjr.p2p.ad5.ui.mine.user.LoginActivity;
import com.hxjr.p2p.ad5.utils.DMConstant;
import com.hxjr.p2p.ad5.utils.ErrorUtil;
import com.hxjr.p2p.ad5.utils.FormatUtil;
import com.hxjr.p2p.ad5.utils.HttpParams;
import com.hxjr.p2p.ad5.utils.UIHelper;
import com.hxjr.p2p.ad5.R;
import com.dm.http.HttpCallBack;
import com.dm.http.HttpUtil;
import com.dm.utils.AppManager;
import com.dm.utils.StringUtils;
import com.dm.widgets.utils.AlertDialogUtil;
import com.dm.widgets.utils.AlertDialogUtil.AlertListener;
import com.dm.widgets.utils.AlertDialogUtil.ConfirmListener;
import com.dm.widgets.utils.ToastUtil;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

/**
 * 修改登录密码
 * @author  huangkaibo
 * @date 2015年10月30日
 */
public class ModifyLoginPwdActivity extends BaseActivity
{
	private EditText login_pwd_old;
	
	private EditText login_pwd_new;
	
	private EditText login_pwd_re;
	
	private RegexInfo regexInfo;
	
	private Context mContext;
	
	private boolean isNeedPwd;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.modify_login_pwd);
		regexInfo = FormatUtil.initRegexInfo(this);
		mContext = this;
		isNeedPwd = getIntent().getBooleanExtra("isNeedPwd", true);
		initView();
	}
	
	/**
	 * 初始化页面元素
	 */
	@Override
	protected void initView()
	{
		super.initView();
		((TextView)findViewById(R.id.title_text)).setText(R.string.pwd_manager_login);
		login_pwd_old = (EditText)findViewById(R.id.login_pwd_old);
		login_pwd_new = (EditText)findViewById(R.id.login_pwd_new);
		login_pwd_re = (EditText)findViewById(R.id.login_pwd_re);
		findViewById(R.id.forget_old_pwd_tv).setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if (!StringUtils.isEmptyOrNull(DMApplication.getInstance().getUserInfo().getPhone()))
				{
					startActivity(new Intent(ModifyLoginPwdActivity.this, FindPwdActivity.class).putExtra("from", "setting"));
				}
				else
				{
					AlertDialogUtil.alert(ModifyLoginPwdActivity.this, "您还没绑定手机，不能找回登录密码。请前往绑定手机！", new AlertListener()
					{
						@Override
						public void doConfirm()
						{
							startActivity(new Intent(ModifyLoginPwdActivity.this, SecurityInfoActivity.class)
								.putExtra("isNeedPwd", isNeedPwd));
						}
					});
				}
			}
		});
	}
	
	/**
	 * 确定
	 * @param v
	 */
	public void confirm(View v)
	{
		if (checkClick(v.getId()))
		{
			modifyPwd();
		}
	}
	
	protected void modifyPwd()
	{
		String oldPassword = login_pwd_old.getText().toString();
		String newPassword = login_pwd_new.getText().toString();
		String confirmPassword = login_pwd_re.getText().toString();
		
		if ("".equalsIgnoreCase(oldPassword))
		{
			ToastUtil.getInstant().show(ModifyLoginPwdActivity.this, "原密码不能为空");
			return;
		}
		
		if ("".equalsIgnoreCase(newPassword))
		{
			ToastUtil.getInstant().show(ModifyLoginPwdActivity.this, "请输入新密码");
			return;
		}
		if (!FormatUtil.matches(regexInfo.getNewPasswordRegex(), newPassword))
		{
			//密码6-16位支持数字、下划线和字母，区分大小写！
			//			AlertDialogUtil.alert(SupplementInfoActivity.this, getString(R.string.supplement_error4));
			AlertDialogUtil.alert(ModifyLoginPwdActivity.this, regexInfo.getPasswordRegexContent());
			return;
		}
		if (oldPassword.equals(newPassword))
		{
			ToastUtil.getInstant().show(ModifyLoginPwdActivity.this, "新密码不能和原密码相同！");
			return;
		}
		if ("".equalsIgnoreCase(newPassword))
		{
			ToastUtil.getInstant().show(ModifyLoginPwdActivity.this, "请输入确认密码");
			return;
		}
		if (!newPassword.equals(confirmPassword))
		{
			ToastUtil.getInstant().show(ModifyLoginPwdActivity.this, "新密码要和确认密码一致");
			return;
		}
		HttpParams params = new HttpParams();
		params.put("pwd", oldPassword);
		params.put("onePwd", newPassword);
		params.put("twoPwd", confirmPassword);
		
		HttpUtil.getInstance().post(this, DMConstant.API_Url.USER_UPDATE_LOGIN_PWD, params, new HttpCallBack()
		{
			@Override
			public void onSuccess(JSONObject result)
			{
				resetLoginPwdSuccess(result);
			}
			
			@Override
			public void onFailure(Throwable t, Context context)
			{
				ToastUtil.getInstant().show(ModifyLoginPwdActivity.this, t.getMessage());
			}
		});
	}
	
	protected void resetLoginPwdSuccess(JSONObject result)
	{
		try
		{
			String code = result.getString("code");
			if (DMConstant.ResultCode.SUCCESS.equals(code))
			{
				// 成功
				AlertDialogUtil.alert(this, "密码修改成功，请重新登录！", "马上登录", new AlertListener()
				{
					@Override
					public void doConfirm()
					{
						DMApplication.toLoginValue = 3;
						startActivity(new Intent(ModifyLoginPwdActivity.this, LoginActivity.class));
						UIHelper.doLoginOut(ModifyLoginPwdActivity.this, true);
						AppManager.getAppManager().finishActivity(PwdManagerActivity.class);
						AppManager.getAppManager().finishActivity(SettingActivity.class);
						finish();
					}
				});
			}
			else if (ErrorUtil.ErroreCode.ERROR_000031.equals(code))
			{
				AlertDialogUtil.confirm(mContext, result.getString("description"), "找回密码", null, new ConfirmListener()
				{
					@Override
					public void onOkClick()
					{
						if (!StringUtils.isEmptyOrNull(DMApplication.getInstance().getUserInfo().getPhone()))
						{
							startActivity(
								new Intent(ModifyLoginPwdActivity.this, FindPwdActivity.class).putExtra("from", "setting"));
						}
						else
						{
							AlertDialogUtil.alert(ModifyLoginPwdActivity.this, "您还没绑定手机，不能找回登录密码。请前往绑定手机！", new AlertListener()
							{
								@Override
								public void doConfirm()
								{
									startActivity(new Intent(ModifyLoginPwdActivity.this, SecurityInfoActivity.class)
										.putExtra("isNeedPwd", isNeedPwd));
								}
							});
						}
					}
					
					@Override
					public void onCancelClick()
					{
					}
				});
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
}
