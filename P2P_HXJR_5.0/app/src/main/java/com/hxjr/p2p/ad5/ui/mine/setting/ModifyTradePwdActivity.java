package com.hxjr.p2p.ad5.ui.mine.setting;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

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
import com.dm.widgets.utils.AlertDialogUtil;
import com.dm.widgets.utils.AlertDialogUtil.ConfirmListener;
import com.dm.widgets.utils.ToastUtil;

/**
 * 修改交易密码
 * @author  huangkaibo
 * @date 2015年10月30日
 */
public class ModifyTradePwdActivity extends BaseActivity implements OnClickListener
{
	
	private TextView deal_pwd_old;
	
	private TextView deal_pwd_new;
	
	private TextView deal_pwd_re;
	
	private RegexInfo regexInfo;
	
	private Context mContext;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.modify_trade_pwd);
		mContext = this;
		regexInfo = FormatUtil.initRegexInfo(this);
		initView();
	}
	
	/**
	 * 初始化页面元素
	 */
	@Override
	protected void initView()
	{
		super.initView();
		((TextView)findViewById(R.id.title_text)).setText(R.string.pwd_manager_pay);
		deal_pwd_old = (TextView)findViewById(R.id.deal_pwd_old);
		deal_pwd_new = (TextView)findViewById(R.id.deal_pwd_new);
		deal_pwd_re = (TextView)findViewById(R.id.deal_pwd_re);
		findViewById(R.id.modify_submit).setOnClickListener(this);
		findViewById(R.id.forget_pay_old_pwd_tv).setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v)
	{
		if (checkClick(v.getId()))
		{
			switch (v.getId())
			{
				case R.id.modify_submit:
				{
					//提交修改
					modifyTradePwd();
					break;
				}
				case R.id.forget_pay_old_pwd_tv:
				{
					//忘记原交易密码
					Intent intent = new Intent(this, FindTradePwdActivity.class);
					startActivity(intent);
					finish();
					break;
				}
				default:
					break;
			}
		}
		
	}
	
	/**
	 * 修改交易密码
	 */
	private void modifyTradePwd()
	{
		String oldPassword = deal_pwd_old.getText().toString();
		String newPassword = deal_pwd_new.getText().toString();
		String confirmPassword = deal_pwd_re.getText().toString();
		
		if ("".equalsIgnoreCase(oldPassword))
		{
			ToastUtil.getInstant().show(ModifyTradePwdActivity.this, "请输入原交易密码");
			return;
		}
		if (!FormatUtil.matches(regexInfo.getTxPwdRegex(), oldPassword))
		{
			ToastUtil.getInstant().show(this, regexInfo.getTxPwdContent());
			return;
		}
		if ("".equalsIgnoreCase(newPassword))
		{
			ToastUtil.getInstant().show(ModifyTradePwdActivity.this, "请输入新交易密码");
			return;
		}
		if (newPassword.equals(oldPassword))
		{
			ToastUtil.getInstant().show(ModifyTradePwdActivity.this, "新密码不能和原密码一样");
			return;
		}
		if (!FormatUtil.matches(regexInfo.getTxPwdRegex(), newPassword))
		{
			ToastUtil.getInstant().show(this, regexInfo.getTxPwdContent());
			return;
		}
		if (!newPassword.equalsIgnoreCase(confirmPassword))
		{
			ToastUtil.getInstant().show(ModifyTradePwdActivity.this, "新密码要和确认密码一致");
			return;
		}
		
		HttpParams params = new HttpParams();
		//		params.put("pwd", EncryptUtil.getMD5Str(oldPassword));
		//		params.put("onePwd", EncryptUtil.getMD5Str(newPassword));
		//		params.put("twoPwd", EncryptUtil.getMD5Str(confirmPassword));
		params.put("pwd", oldPassword);
		params.put("onePwd", newPassword);
		params.put("twoPwd", confirmPassword);
		HttpUtil.getInstance().post(this, DMConstant.API_Url.USER_UPDATETRANPWD, params, new HttpCallBack()
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
						ToastUtil.getInstant().show(ModifyTradePwdActivity.this, "恭喜您修改密码成功！");
						DMApplication.getInstance().getUserInfo().setWithdrawPsw(true);
						finish();
					}
					else if (ErrorUtil.ErroreCode.ERROR_000044.equals(code))
					{
						// 交易密码错误
						AlertDialogUtil.confirm(mContext, "原交易密码有误，请重试", "找回支付密码", "确定", new ConfirmListener()
						{
							
							@Override
							public void onOkClick()
							{
								startActivity(new Intent(mContext, FindTradePwdActivity.class));
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
			
			@Override
			public void onFailure(Throwable t, Context context)
			{
				ToastUtil.getInstant().show(ModifyTradePwdActivity.this, t.getMessage());
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
