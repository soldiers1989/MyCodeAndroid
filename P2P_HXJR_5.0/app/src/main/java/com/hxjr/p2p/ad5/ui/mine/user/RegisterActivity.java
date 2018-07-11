package com.hxjr.p2p.ad5.ui.mine.user;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.dm.http.DMException;
import com.dm.http.HttpCallBack;
import com.dm.http.HttpUtil;
import com.dm.utils.DMJsonObject;
import com.dm.widgets.utils.AlertDialogUtil;
import com.dm.widgets.utils.AlertDialogUtil.ConfirmListener;
import com.dm.widgets.utils.ToastUtil;
import com.hxjr.p2p.ad5.R;
import com.hxjr.p2p.ad5.ui.BaseActivity;
import com.hxjr.p2p.ad5.utils.AgreementManager;
import com.hxjr.p2p.ad5.utils.DMConstant;
import com.hxjr.p2p.ad5.utils.ErrorUtil;
import com.hxjr.p2p.ad5.utils.FormatUtil;
import com.hxjr.p2p.ad5.utils.HttpParams;

/**
 * 注册
 * @author  jiaohongyun
 * @date 2015年11月10日
 */
public class RegisterActivity extends BaseActivity
{
	private boolean isdowning;
	
	/**
	 * 手机验证码倒计时
	 */
	private CountDownTimer phoneDownTimer;
	
	/**
	 * 手机号码
	 */
	private EditText phoneEdit;
	
	/**
	 * 验证码
	 */
	private EditText codeEdit;
	
	/**
	 * 获取验证码按钮
	 */
	private Button btn;
	
	/**
	 * 协议管理类
	 */
	private AgreementManager agreementManager;
	
	private SharedPreferences sp;
	
	/**
	 * 倒计时所剩时间
	 */
	private long remainTime = 60000L;
	
	/**
	 * 最后停止时间
	 */
	private long lastTime;
	
	private String userId;
	
	private boolean isCilckGetCode = false;
	
	private String phoneNum;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
		sp = getSharedPreferences("config", Context.MODE_PRIVATE);
		initView();
		initData();
	}
	
	@Override
	protected void initView()
	{
		super.initView();
		((TextView)findViewById(R.id.title_text)).setText(R.string.register);
		
		phoneEdit = (EditText)findViewById(R.id.register_phone);
		phoneNum = getIntent().getStringExtra("phoneNum");
		if (null != phoneNum)
		{
			phoneEdit.setText(phoneNum);
			phoneEdit.setSelection(phoneNum.length());
		}
		codeEdit = (EditText)findViewById(R.id.register_verify_code);
		btn = (Button)findViewById(R.id.get_code_btn);
		agreementManager = new AgreementManager(this, AgreementManager.TYPE_ZC);
		agreementManager.initView();
	}
	
	/**
	 * 进入下一步
	 * @param v
	 */
	public void next(View v)
	{
		if (checkClick(v.getId())) //防重复点击	
		{
			if (checkVerifyCodeParams(true, true))
			{
				// 判断验证码是否正确
				HttpParams httpParams = new HttpParams();
				httpParams.put("phone", phoneEdit.getText().toString());
				httpParams.put("verifyCode", codeEdit.getText().toString());
				httpParams.put("userId", userId);
				httpParams.put("type", "RZ");
				
				HttpUtil.getInstance().post(this, DMConstant.API_Url.CHECK_VERIFY_CODE, httpParams, new HttpCallBack()
				{
					@Override
					public void onSuccess(JSONObject result)
					{
						try
						{
							String code = result.getString("code");
							
							if (DMConstant.ResultCode.SUCCESS.equals(code))
							{
								//跳转到补充页面
								Intent intent = new Intent(RegisterActivity.this, SupplementInfoActivity.class);
								intent.putExtra("phone", phoneEdit.getText().toString());
								intent.putExtra("verifyCode", codeEdit.getText().toString());
								startActivity(intent);
								finish();
							}
							else if (ErrorUtil.ErroreCode.ERROR_000019.equals(code))
							{
								AlertDialogUtil.confirm(RegisterActivity.this,
									result.getString("description"),
									"登录",
									"确定",
									new ConfirmListener()
								{
									
									@Override
									public void onOkClick()
									{
										Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
										intent.putExtra("phoneNumber", phoneEdit.getText().toString());
										startActivity(intent);
										finish();
									}
									
									@Override
									public void onCancelClick()
									{
									}
								});
							}
							else
							{
								// 验证码错误
								AlertDialogUtil.alert(RegisterActivity.this, result.getString("description"))
									.setCanceledOnTouchOutside(false);
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
						super.onFailure(t, context);
					}
					
					@Override
					public void onConnectFailure(DMException dmE, Context context)
					{
						ToastUtil.getInstant().show(RegisterActivity.this, dmE.getDescription());
					}
				});
			}
		}
	}
	
	/**
	 * 返回登录
	 */
	public void toLogin(View v)
	{
		finish();
	}
	
	/**
	 * 获手机取验证码
	 * @param v
	 */
	public void getVerifyCode(View v)
	{
		if (checkClick(v.getId())) //防重复点击	
		{
			if (checkVerifyCodeParams(false, false))
			{
				isCilckGetCode = true;
				// 发送验证码到手机
				HttpParams httpParams = new HttpParams();
				httpParams.put("phone", phoneEdit.getText().toString());
				httpParams.put("type", "RZ");//注册
				//				httpParams.put("type", "ZHZF");//找回密码
				HttpUtil.getInstance().post(this, DMConstant.API_Url.GETMOBILECODE, httpParams, new HttpCallBack()
				{
					@Override
					public void onSuccess(JSONObject result)
					{
						try
						{
							String code = result.getString("code");
							
							if (DMConstant.ResultCode.SUCCESS.equals(code))
							{
								startDownTimer(60000L);
								// 下发验证码成功
								AlertDialogUtil.alert(RegisterActivity.this, getString(R.string.find_pwd_send_success))
									.setCanceledOnTouchOutside(false);
								if (!"".equals(result.getString("data")))
								{
									DMJsonObject data = new DMJsonObject(result.getString("data"));
									userId = data.getString("userId");
								}
								else
								{
									userId = phoneEdit.getText().toString();
								}
							}
							else if (ErrorUtil.ErroreCode.ERROR_000019.equals(code))
							{
								AlertDialogUtil.confirm(RegisterActivity.this,
									result.getString("description"),
									"登录",
									"确定",
									new ConfirmListener()
								{
									
									@Override
									public void onOkClick()
									{
										Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
										intent.putExtra("phoneNumber", phoneEdit.getText().toString());
										startActivity(intent);
										finish();
									}
									
									@Override
									public void onCancelClick()
									{
									}
								});
							}
							else
							{
								//下发验证码失败
								AlertDialogUtil.alert(RegisterActivity.this, result.getString("description"))
									.setCanceledOnTouchOutside(false);
							}
						}
						catch (JSONException e)
						{
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					
				});
			}
		}
	}
	
	/**
	 * 检查获取验证码参数
	 * @return
	 */
	private boolean checkVerifyCodeParams(boolean checkSecurityCode, boolean isCheckAgreed)
	{
		if (phoneEdit.getText().toString().isEmpty())
		{
			//手机号码为空
			AlertDialogUtil.alert(RegisterActivity.this, getString(R.string.find_pwd_phone_empty))
				.setCanceledOnTouchOutside(false);
			return false;
		}
		
		if (!FormatUtil.isMobileNO(phoneEdit.getText().toString().trim()))
		{
			//手机号码格式不正确
			AlertDialogUtil.alert(RegisterActivity.this, getString(R.string.find_pwd_phone_format_error))
				.setCanceledOnTouchOutside(false);
			return false;
		}
		
		if (checkSecurityCode)
		{
			if (!isCilckGetCode)
			{
				AlertDialogUtil.alert(RegisterActivity.this, "您还没获取验证码，请先获取验证码").setCanceledOnTouchOutside(false);
				return false;
			}
			if (codeEdit.getText().toString().trim().length() == 0)
			{
				AlertDialogUtil.alert(RegisterActivity.this, getString(R.string.find_pwd_code_empty))
					.setCanceledOnTouchOutside(false);
				return false;
			}
		}
		if (isCheckAgreed)
		{
			if (agreementManager != null && !agreementManager.isChecked())
			{
				AlertDialogUtil.alert(RegisterActivity.this, "请先阅读注册协议并勾选！").setCanceledOnTouchOutside(false);
				return false;
			}
		}
		return true;
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		if (agreementManager != null)
		{
			agreementManager.initData();
		}
	}
	
	@Override
	protected void onDestroy()
	{
		if (phoneDownTimer != null)
		{
			lastTime = System.currentTimeMillis();
			sp.edit().putLong("registerRemainTime", remainTime).putLong("registerLastTime", lastTime).commit();
			
			phoneDownTimer.cancel();
		}
		super.onDestroy();
	}
	
	private void initData()
	{
		remainTime = sp.getLong("registerRemainTime", 0L);
		lastTime = sp.getLong("registerLastTime", 0);
		long currentTime = System.currentTimeMillis();
		long temp = remainTime - (currentTime - lastTime);
		if (temp > 0)
		{
			startDownTimer(temp);
		}
	}
	
	/**
	 * 开始倒计时
	 * @param btn
	 */
	private void startDownTimer(long time)
	{
		phoneDownTimer = new CountDownTimer(time, 1000)
		{
			
			@Override
			public void onTick(long millisUntilFinished)
			{
				isdowning = true;
				int temp = (int)(millisUntilFinished / 1000);
				btn.setText(getString(R.string.find_pwd_wait_time, temp));
				btn.setEnabled(false);
			}
			
			@Override
			public void onFinish()
			{
				isdowning = false;
				btn.setEnabled(true);
				btn.setText(getString(R.string.get_verify_code));
			}
		}.start();
	}
	
}
