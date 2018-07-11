package com.hxjr.p2p.ad5.ui.mine.setting;

import org.json.JSONException;
import org.json.JSONObject;

import com.hxjr.p2p.ad5.DMApplication;
import com.hxjr.p2p.ad5.ui.BaseActivity;
import com.hxjr.p2p.ad5.utils.DMConstant;
import com.hxjr.p2p.ad5.utils.HttpParams;
import com.hxjr.p2p.ad5.R;
import com.dm.http.HttpCallBack;
import com.dm.http.HttpUtil;
import com.dm.utils.AppManager;
import com.dm.utils.DMLog;
import com.dm.widgets.utils.AlertDialogUtil;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * 找回支付密码
 * @author  jiaohongyun
 * @date 2015年11月25日
 */
public class FindTradePwdActivity extends BaseActivity
{
	/**
	 * 手机验证码倒计时
	 */
	private CountDownTimer phoneDownTimer;
	
	/**
	 * 手机号码
	 */
	private TextView phoneText;
	
	/**
	 * 验证码
	 */
	private EditText codeEdit;
	
	/**
	 * 获取验证码按钮
	 */
	private Button btn;
	
	private SharedPreferences sp;
	
	/**
	 * 倒计时所剩时间
	 */
	private long remainTime = 60000L;
	
	/**
	 * 最后停止时间
	 */
	private long lastTime;
	
	private boolean hasGetCode;
	
	private String phoneNumber;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.find_trade_pwd);
		phoneNumber = DMApplication.getInstance().getUserInfo().getPhoneNumber();
		initView();
		initData();
	}
	
	@Override
	protected void initView()
	{
		super.initView();
		((TextView)findViewById(R.id.title_text)).setText(R.string.get_trade_password);
		phoneText = (TextView)findViewById(R.id.find_pwd_phone);
		codeEdit = (EditText)findViewById(R.id.find_pwd_verify_code);
		btn = (Button)findViewById(R.id.get_code_btn);
		phoneText.setText(DMApplication.getInstance().getUserInfo().getPhone());
	}
	
	private void initData()
	{
		//		sp = getSharedPreferences("config", Context.MODE_PRIVATE);
		//		remainTime = sp.getLong("findTradePwdremainTime", 0L);
		//		lastTime = sp.getLong("findTradeLastTime", 0);
		//		long currentTime = System.currentTimeMillis();
		//		long temp = remainTime - (currentTime - lastTime);
		//		if (temp > 0)
		//		{
		//			startDownTimer(temp);
		//		}
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
				remainTime = millisUntilFinished;
				int temp = (int)(millisUntilFinished / 1000);
				btn.setText(getString(R.string.find_pwd_wait_time, temp));
				btn.setEnabled(false);
			}
			
			@Override
			public void onFinish()
			{
				btn.setEnabled(true);
				btn.setText(getString(R.string.get_verify_code));
				AppManager appManager = AppManager.getAppManager();
				Activity activity = appManager.currentActivity();
				if (!(activity instanceof FindTradePwdActivity))
				{
					FindTradePwdActivity.this.finish();
				}
			}
		}.start();
	}
	
	/**
	 * 获取验证码
	 * @param v
	 */
	public void getVerifyCode(View v)
	{
		if (checkClick(v.getId())) //防重复点击	
		{
			// 发送验证码到手机
			HttpParams httpParams = new HttpParams();
			httpParams.put("phone", phoneNumber);
			httpParams.put("type", "ZHZF");//找回密码
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
							// 下发验证码成功
							startDownTimer(60000L);
							AlertDialogUtil.alert(FindTradePwdActivity.this, getString(R.string.find_pwd_send_success))
								.setCanceledOnTouchOutside(false);
							hasGetCode = true;
						}
						else
						{ // 下发验证码失败
							AlertDialogUtil.alert(FindTradePwdActivity.this, result.getString("description"))
								.setCanceledOnTouchOutside(false);
						}
					}
					catch (JSONException e)
					{
						DMLog.e(FindTradePwdActivity.class.getSimpleName(), e.getMessage());
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
	
	/**
	 * 下一步，设置密码
	 * @param v
	 */
	public void next(View v)
	{
		if (checkClick(v.getId())) //防重复点击	
		{
			if (checkNextParams())
			{
				// 检查验证码是正确
				HttpParams httpParams = new HttpParams();
				httpParams.put("phone", phoneNumber);
				httpParams.put("verifyCode", codeEdit.getText().toString());
				httpParams.put("type", "ZHZF");
				
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
								Intent intent = new Intent(FindTradePwdActivity.this, SetTradePwdActivity.class);
								startActivity(intent);
								finish();
							}
							else
							{
								// 验证码错误
								AlertDialogUtil.alert(FindTradePwdActivity.this, result.getString("description"))
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
				});
			}
		}
	}
	
	/**
	 * 检查点击下一步参数
	 * @return
	 */
	private boolean checkNextParams()
	{
		if (phoneText.getText().toString().isEmpty())
		{
			//手机号码为空
			AlertDialogUtil.alert(FindTradePwdActivity.this, getString(R.string.find_pwd_phone_empty))
				.setCanceledOnTouchOutside(false);
			return false;
		}
		if (codeEdit.getText().toString().isEmpty())
		{
			//验证码为空
			AlertDialogUtil.alert(FindTradePwdActivity.this, getString(R.string.find_pwd_code_empty))
				.setCanceledOnTouchOutside(false);
			return false;
		}
		if (!hasGetCode)
		{
			AlertDialogUtil.alert(FindTradePwdActivity.this, "您还没获取验证码，请先获取验证码!").setCanceledOnTouchOutside(false);
			return false;
		}
		return true;
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	protected void onDestroy()
	{
		if (phoneDownTimer != null)
		{
			//			lastTime = System.currentTimeMillis();
			//			sp.edit().putLong("findTradePwdremainTime", remainTime).putLong("findTradeLastTime", lastTime).commit();
			phoneDownTimer.cancel();
		}
		super.onDestroy();
	}
}
