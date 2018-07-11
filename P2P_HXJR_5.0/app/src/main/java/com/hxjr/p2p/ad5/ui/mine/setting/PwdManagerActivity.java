package com.hxjr.p2p.ad5.ui.mine.setting;

import org.json.JSONException;
import org.json.JSONObject;

import com.hxjr.p2p.ad5.DMApplication;
import com.hxjr.p2p.ad5.bean.Fee;
import com.hxjr.p2p.ad5.bean.LockPwd;
import com.hxjr.p2p.ad5.bean.UserInfo;
import com.hxjr.p2p.ad5.service.ApiUtil;
import com.hxjr.p2p.ad5.service.ApiUtil.OnPostCallBack;
import com.hxjr.p2p.ad5.ui.BaseActivity;
import com.hxjr.p2p.ad5.ui.mine.setting.gesture.SetGesturePasswordActivity;
import com.hxjr.p2p.ad5.ui.mine.user.FindPwdActivity;
import com.hxjr.p2p.ad5.utils.DMConstant;
import com.hxjr.p2p.ad5.utils.HttpParams;
import com.hxjr.p2p.ad5.utils.SharedPreferenceUtils;
import com.hxjr.p2p.ad5.R;
import com.dm.db.DbUtils;
import com.dm.db.exception.DbException;
import com.dm.db.sqlite.Selector;
import com.dm.http.DMException;
import com.dm.http.HttpCallBack;
import com.dm.http.HttpUtil;
import com.dm.utils.DMJsonObject;
import com.dm.utils.EncrypUtil;
import com.dm.utils.StringUtils;
import com.dm.utils.ThreadsPool;
import com.dm.widgets.utils.AlertDialogUtil;
import com.dm.widgets.utils.AlertDialogUtil.AlertListener;
import com.dm.widgets.utils.AlertDialogUtil.ConfirmListener;
import com.dm.widgets.utils.AlertDialogUtil.PromptListener;
import com.dm.widgets.utils.ToastUtil;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/***
 * 密码管理
 * @author  tangjian
 * @date 2015年11月23日
 */
public class PwdManagerActivity extends BaseActivity
{
	private static final int RESET_LOCK_PWD = 1; //重置手势密码
	
	private static final int RESET_DEAL_PWD = 2; //重置支付密码
	
	private DbUtils db;
	
	private LockPwd lockPwd;
	
	private UserInfo userInfo;
	
	private Button jiaoYiPwdBtn;
	
	private Button shouShiPwdBtn;
	
	/**是否完成了手机认证**/
	private boolean mobileVerified;
	
	/**是否设置了交易密码**/
	private boolean pwdVerified;
	
	/**是否需要交易密码*/
	private boolean isNeedPwd = true;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pwd_manager);
		userInfo = DMApplication.getInstance().getUserInfo();
		initView();
		getDbInfo();
	}
	
	@Override
	protected void initView()
	{
		super.initView();
		((TextView)findViewById(R.id.title_text)).setText(R.string.pwd_manager_title);
		jiaoYiPwdBtn = ((Button)findViewById(R.id.jiaoYiPwdBtn));
		shouShiPwdBtn = ((Button)findViewById(R.id.shouShiPwdBtn));
		if (!getIntent().getBooleanExtra("isNeedPwd", true))
		{//如果不需要交易密码
			findViewById(R.id.last_line).setVisibility(View.GONE);
			jiaoYiPwdBtn.setVisibility(View.GONE);
		}
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		getAccountInfo();
		ApiUtil.getFee(this, new OnPostCallBack()
		{
			@Override
			public void onSuccess(Fee fee)
			{
				isNeedPwd = fee.getChargep().isNeedPsd();
			}
			
			@Override
			public void onFailure()
			{
			}
		});
	}
	
	/**
	 * 从数据库查找当前登录用户信息
	 */
	private void getDbInfo()
	{
		ThreadsPool.executeOnExecutor(new Runnable()
		{
			@Override
			public void run()
			{
				try
				{
					db = DbUtils.create(PwdManagerActivity.this);
					//将手势密码保存到数据库
					db.createTableIfNotExist(LockPwd.class);
					if (null != userInfo)
					{
						lockPwd = db.findFirst(Selector.from(LockPwd.class).where("userId", "=", userInfo.getId()));
						handler.sendEmptyMessage(null == lockPwd ? 0 : 1);
					}
					else
					{
						handler.sendEmptyMessage(1);
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
			}
		});
	}
	
	Handler handler = new Handler()
	{
		
		@Override
		public void handleMessage(Message msg)
		{
			switch (msg.what)
			{
				case 0:
					//没有设置手势密码
					shouShiPwdBtn.setText(R.string.pwd_manager_shoushi);
					break;
				case 1:
					//设置手势密码
					shouShiPwdBtn.setText(R.string.pwd_shousi_setting_m);
					break;
					
				default:
					break;
			}
		}
		
	};
	
	/**
	 * 请求用户账号信息
	 */
	private void getAccountInfo()
	{
		HttpUtil.getInstance().post(PwdManagerActivity.this, DMConstant.API_Url.USER_USERINFO, new HttpCallBack()
		{
			@Override
			public void onSuccess(JSONObject result)
			{
				try
				{
					String code = result.getString("code");
					if (code.equals(DMConstant.ResultCode.SUCCESS))
					{
						DMJsonObject data = new DMJsonObject(result.getString("data"));
						UserInfo temp = new UserInfo(data);
						if (null != temp)
						{
							mobileVerified = StringUtils.isEmptyOrNull(temp.getPhone()) ? false : true;
							pwdVerified = temp.isWithdrawPsw();
							setData(pwdVerified);
						}
					}
					else
					{
						mobileVerified = StringUtils.isEmptyOrNull(userInfo.getPhone()) ? false : true;
						pwdVerified = userInfo.isWithdrawPsw();
						setData(pwdVerified);
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
				mobileVerified = StringUtils.isEmptyOrNull(userInfo.getPhone()) ? false : true;
				pwdVerified = userInfo.isWithdrawPsw();
				setData(pwdVerified);
			}
			
			@Override
			public void onStart()
			{
				setShowProgress(false);
			}
		});
	}
	
	protected void setData(boolean isWithdrawPsw)
	{
		if (isWithdrawPsw)
		{
			//设置过交易密码
			jiaoYiPwdBtn.setText(R.string.pwd_manager_pay);
		}
		else
		{
			jiaoYiPwdBtn.setText(R.string.pwd_manager_trade);
		}
	}
	
	/**
	 * 跳转到修改登录密码
	 * @param v
	 */
	public void modifyLoginPwd(View v)
	{
		if (checkClick(v.getId())) //防重复点击	
		{
			Intent intent = new Intent(this, ModifyLoginPwdActivity.class).putExtra("isNeedPwd", isNeedPwd);
			startActivity(intent);
		}
	}
	
	/**
	 * 跳转重置手势密码
	 * @param v
	 */
	public void reSetShouShi(View v)
	{
		if (checkClick(v.getId())) //防重复点击	
		{
			showLoginPwd(RESET_LOCK_PWD);
		}
	}
	
	/**
	 * 弹框提示输入登陆密码
	 * @param resetType
	 */
	private void showLoginPwd(final int resetType)
	{
		AlertDialogUtil.promptOne(this, "请输入登录密码", null, new PromptListener()
		{
			@Override
			public void onOkClick(EditText text)
			{
				int length = text.getText().toString().length();
				String content = text.getText().toString();
				String loginPwd = "";
				if (lockPwd != null)
				{
					loginPwd = EncrypUtil.decrypt(DMConstant.StringConstant.ENCRYP_SEND, lockPwd.getLoginPwd());
				}
				else
				{
					loginPwd = (String)SharedPreferenceUtils.get(PwdManagerActivity.this, "loginTemp", "password", "");
				}
				if (length == 0)
				{
					ToastUtil.getInstant().show(PwdManagerActivity.this, "请输入登录密码");
					return;
				}
				if (length < 6 || length > 18)
				{
					ToastUtil.getInstant().show(PwdManagerActivity.this, "登录密码长度不对");
					return;
				}
				//				if (!content.equals(loginPwd))
				//				{
				//					showErrorPwd(resetType);
				//					return;
				//				}
				checkLoginPwd(content, resetType);
			}
			
			@Override
			public void onCancelClick()
			{
			}
		});
	}
	
	protected void checkLoginPwd(final String content, final int resetType)
	{
		HttpParams params = new HttpParams();
		params.put("password", content);
		HttpUtil.getInstance().post(this, DMConstant.API_Url.CHECK_LOGIN_PWD, params, new HttpCallBack()
		{
			@Override
			public void onSuccess(JSONObject result)
			{
				try
				{
					String code = result.getString("code");
					if (null != code && DMConstant.ResultCode.SUCCESS.equals(code))
					{
						Intent intent = null;
						switch (resetType)
						{
							case RESET_LOCK_PWD:
								intent = new Intent(PwdManagerActivity.this, SetGesturePasswordActivity.class);
								intent.putExtra("password", content);
								intent.putExtra("account", lockPwd.getAccount());
								intent.putExtra("isResetGesturePwd", true);
								startActivity(intent);
								break;
								
							case RESET_DEAL_PWD:
								intent = new Intent(PwdManagerActivity.this, ModifyTradePwdActivity.class);
								startActivity(intent);
								break;
						}
					}
					else
					{//{"data":"","description":"登录密码错误!","code":"000003"}
						if (null != code && "000003".equals(code))
						{
							showErrorPwd(resetType);
						}
						else
						{
							ToastUtil.getInstant().show(PwdManagerActivity.this, result.getString("description"));
						}
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
				ToastUtil.getInstant().show(PwdManagerActivity.this, dmE.getDescription());
			}
		});
	}
	
	/**
	 * 弹出提示登录密码错误
	 * @param resetType
	 */
	protected void showErrorPwd(final int resetType)
	{
		AlertDialogUtil.confirm(this, "登录密码不正确，请重试！", "找回密码", "确定", new ConfirmListener()
		{
			@Override
			public void onOkClick()
			{
				if (mobileVerified)
				{
					Intent intent = new Intent(PwdManagerActivity.this, FindPwdActivity.class);
					intent.putExtra("from", "setting");
					startActivity(intent);
				}
				else
				{
					AlertDialogUtil.alert(PwdManagerActivity.this, "您还没绑定手机，不能找回登录密码。请前往绑定手机！", new AlertListener()
					{
						@Override
						public void doConfirm()
						{
							startActivity(
								new Intent(PwdManagerActivity.this, SecurityInfoActivity.class).putExtra("isNeedPwd", isNeedPwd));
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
	
	/**
	 * 跳转重置交易密码
	 * @param v
	 */
	public void reSetTradePwd(View v)
	{
		if (checkClick(v.getId())) //防重复点击	
		{
			if (pwdVerified)
			{
				showLoginPwd(RESET_DEAL_PWD);
			}
			else
			{
				startActivity(new Intent(PwdManagerActivity.this, SetTradePwdActivity.class));
			}
		}
	}
}
