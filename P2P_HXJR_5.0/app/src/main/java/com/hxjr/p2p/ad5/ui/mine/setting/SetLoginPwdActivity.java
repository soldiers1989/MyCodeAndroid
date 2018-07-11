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
import com.dm.widgets.utils.AlertDialogUtil;
import com.dm.widgets.utils.AlertDialogUtil.AlertListener;
import com.dm.widgets.utils.ToastUtil;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class SetLoginPwdActivity extends BaseActivity
{
	private TextView trade_pwd_et;
	
	private TextView confirm_trade_pwd_et;
	
	private String phone;
	
	private String from;
	
	private RegexInfo regexInfo;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_set_login_pwd);
		regexInfo = FormatUtil.initRegexInfo(this);
		phone = getIntent().getStringExtra("phone");
		from = getIntent().getStringExtra("from");
		initView();
	}
	
	@Override
	protected void initView()
	{
		super.initView();
		((TextView)findViewById(R.id.title_text)).setText(R.string.password_set_login_title);
		trade_pwd_et = (TextView)findViewById(R.id.trade_pwd_et);
		confirm_trade_pwd_et = (TextView)findViewById(R.id.confirm_trade_pwd_et);
		findViewById(R.id.confirm_btn).setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if (checkDatas())
				{
					setLoginPwd();
				}
			}
		});
	}
	
	protected boolean checkDatas()
	{
		String newPassword = trade_pwd_et.getText().toString();
		String confirmPassword = confirm_trade_pwd_et.getText().toString();
		if ("".equalsIgnoreCase(newPassword))
		{
			ToastUtil.getInstant().show(SetLoginPwdActivity.this, "密码不能为空");
			return false;
		}
		//		if (!FormatUtil.validateLoginPwd(newPassword))
		//		{
		//			//密码6-16位支持数字、下划线和字母，区分大小写！
		//			ToastUtil.getInstant().show(SetLoginPwdActivity.this, R.string.supplement_error4);
		//			return false;
		//		}
		if (!FormatUtil.matches(regexInfo.getNewPasswordRegex(), newPassword)) //!FormatUtil.validateLoginPwd(pwd.getText().toString())
		{
			//密码6-18位支持数字、下划线和字母，区分大小写！
			AlertDialogUtil.alert(SetLoginPwdActivity.this, regexInfo.getPasswordRegexContent());
			return false;
		}
		if (!newPassword.equalsIgnoreCase(confirmPassword))
		{
			ToastUtil.getInstant().show(SetLoginPwdActivity.this, "新密码要和确认密码一致");
			return false;
		}
		return true;
	}
	
	/**
	* 新设置密码
	*/
	private void setLoginPwd()
	{
		HttpParams params = new HttpParams();
		params.put("password", trade_pwd_et.getText().toString());
		params.put("rePassword", confirm_trade_pwd_et.getText().toString());
		params.put("phone", phone);
		HttpUtil.getInstance().post(this, DMConstant.API_Url.RESET_LOGIN_PWD, params, new HttpCallBack()
		{
			@Override
			public void onSuccess(JSONObject result)
			{
				try
				{
					String code = result.getString("code");
					if (DMConstant.ResultCode.SUCCESS.equals(code))
					{
						// 成功
						if (null != from && from.equals("setting"))
						{
							AlertDialogUtil.alert(SetLoginPwdActivity.this, "密码修改成功，请重新登录！", "马上登录", new AlertListener()
							{
								@Override
								public void doConfirm()
								{
									DMApplication.toLoginValue = 3;
									startActivity(new Intent(SetLoginPwdActivity.this, LoginActivity.class));
									UIHelper.doLoginOut(SetLoginPwdActivity.this, true);
									AppManager.getAppManager().finishActivity(FindPwdActivity.class);
									AppManager.getAppManager().finishActivity(ModifyLoginPwdActivity.class);
									AppManager.getAppManager().finishActivity(PwdManagerActivity.class);
									AppManager.getAppManager().finishActivity(SettingActivity.class);
									finish();
								}
							});
						}
						else
						{
							ToastUtil.getInstant().show(SetLoginPwdActivity.this, "恭喜您修改密码成功！");
							finish();
						}
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
			
			@Override
			public void onFailure(Throwable t, Context context)
			{
				ToastUtil.getInstant().show(SetLoginPwdActivity.this, t.getMessage());
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
}
