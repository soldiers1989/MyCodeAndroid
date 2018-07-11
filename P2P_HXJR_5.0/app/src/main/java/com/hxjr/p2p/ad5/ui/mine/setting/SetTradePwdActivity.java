package com.hxjr.p2p.ad5.ui.mine.setting;

import org.json.JSONException;
import org.json.JSONObject;

import com.hxjr.p2p.ad5.DMApplication;
import com.hxjr.p2p.ad5.bean.RegexInfo;
import com.hxjr.p2p.ad5.ui.BaseActivity;
import com.hxjr.p2p.ad5.utils.DMConstant;
import com.hxjr.p2p.ad5.utils.ErrorUtil;
import com.hxjr.p2p.ad5.utils.FormatUtil;
import com.hxjr.p2p.ad5.utils.HttpParams;
import com.hxjr.p2p.ad5.R;
import com.dm.http.HttpCallBack;
import com.dm.http.HttpUtil;
import com.dm.widgets.utils.ToastUtil;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class SetTradePwdActivity extends BaseActivity
{
	private TextView trade_pwd_et;
	
	private TextView confirm_trade_pwd_et;
	
	private RegexInfo regexInfo;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_set_trade_pwd);
		regexInfo = FormatUtil.initRegexInfo(this);
		initView();
	}
	
	@Override
	protected void initView()
	{
		super.initView();
		((TextView)findViewById(R.id.title_text)).setText(R.string.password_deal_title);
		trade_pwd_et = (TextView)findViewById(R.id.trade_pwd_et);
		confirm_trade_pwd_et = (TextView)findViewById(R.id.confirm_trade_pwd_et);
		findViewById(R.id.confirm_btn).setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if (checkDatas())
				{
					setDealPwd();
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
			ToastUtil.getInstant().show(SetTradePwdActivity.this, "请输入交易密码");
			return false;
		}
		if (!FormatUtil.matches(regexInfo.getTxPwdRegex(), newPassword))
		{
			ToastUtil.getInstant().show(this, regexInfo.getTxPwdContent());
			return false;
		}
		if (!newPassword.equalsIgnoreCase(confirmPassword))
		{
			ToastUtil.getInstant().show(SetTradePwdActivity.this, "新密码要和确认密码一致");
			return false;
		}
		return true;
	}
	
	/**
	* 新设置密码
	*/
	private void setDealPwd()
	{
		String newPassword = trade_pwd_et.getText().toString();
		String confirmPassword = confirm_trade_pwd_et.getText().toString();
		HttpParams params = new HttpParams();
		params.put("onePwd", newPassword);
		params.put("twoPwd", confirmPassword);
		
		HttpUtil.getInstance().post(this, DMConstant.API_Url.USER_SETWITPASSWORD, params, new HttpCallBack()
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
						ToastUtil.getInstant().show(SetTradePwdActivity.this, "恭喜您修改密码成功！");
						DMApplication.getInstance().getUserInfo().setWithdrawPsw(true);
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
			
			@Override
			public void onFailure(Throwable t, Context context)
			{
				ToastUtil.getInstant().show(SetTradePwdActivity.this, t.getMessage());
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
