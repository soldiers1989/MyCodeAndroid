package com.hxjr.p2p.ad5.ui.mine.setting.gesture;

import java.util.List;

import com.hxjr.p2p.ad5.DMApplication;
import com.hxjr.p2p.ad5.bean.LockPwd;
import com.hxjr.p2p.ad5.ui.BaseActivity;
import com.hxjr.p2p.ad5.ui.MainActivity;
import com.hxjr.p2p.ad5.ui.mine.user.LoginActivity;
import com.hxjr.p2p.ad5.utils.DMConstant;
import com.hxjr.p2p.ad5.utils.SharedPreferenceUtils;
import com.hxjr.p2p.ad5.R;
import com.dm.db.DbUtils;
import com.dm.db.exception.DbException;
import com.dm.db.sqlite.Selector;
import com.dm.utils.AppManager;
import com.dm.utils.DMLog;
import com.dm.utils.EncrypUtil;
import com.dm.widgets.LockPatternView;
import com.dm.widgets.LockPatternView.Cell;
import com.dm.widgets.utils.LockPatternUtils;
import com.dm.widgets.utils.ToastUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 解锁手势密码
 * @author  jiaohongyun
 * @date 2015年8月18日
 */
public class UnlockGesturePasswordActivity extends BaseActivity implements OnClickListener
{
	private LockPatternView mLockPatternView;
	
	private int mFailedPatternAttemptsSinceLastTimeout = 0;
	
	private CountDownTimer mCountdownTimer = null;
	
	private Handler mHandler = new Handler();
	
	private TextView mHeadTextView;
	
	private Button forgetView;
	
	private Button otherLogin;
	
	//    private Animation mShakeAnim;
	
	private Toast mToast;
	
	/**
	 * 解锁密码
	 */
	private LockPwd lockPwd;
	
	private String lockPwdStr = "";
	
	//	/**
	//	 * 是否需要重新设置手势密码
	//	 */
	//	private boolean isSetting;
	
	private boolean isTemp = false;
	
	/**
	 * 剩余重新输入密码时间
	 */
	private long remainTime = 0;
	
	private boolean isCancleable;
	
	private void showToast(CharSequence message)
	{
		if (null == mToast)
		{
			mToast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
			mToast.setGravity(Gravity.CENTER, 0, 0);
		}
		else
		{
			mToast.setText(message);
		}
		mToast.show();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0)
		{
			if (remainTime > 0)
			{
				//计时器倒计时中，回到桌面，不关闭应用
				Intent i = new Intent(Intent.ACTION_MAIN);
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				i.addCategory(Intent.CATEGORY_HOME);
				startActivity(i);
			}
			else
			{
				//关闭应用
				if (isCancleable)
				{
					finish();
					AppManager.getAppManager().finishActivity(UnlockGesturePasswordActivity.class);
				}
				else
				{
					Intent i = new Intent(Intent.ACTION_MAIN);
					i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					i.addCategory(Intent.CATEGORY_HOME);
					startActivity(i);
				}
			}
		}
		return false;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gesturepassword_unlock);
		mLockPatternView = (LockPatternView)this.findViewById(R.id.gesturepwd_unlock_lockview);
		mLockPatternView.setOnPatternListener(mChooseNewLockPatternListener);
		mLockPatternView.setTactileFeedbackEnabled(true);
		mHeadTextView = (TextView)findViewById(R.id.gesturepwd_unlock_text);
		//        mShakeAnim = AnimationUtils.loadAnimation(this, R.anim.shake_x);
		otherLogin = (Button)findViewById(R.id.gesturepwd_unlock_other_user);
		forgetView = (Button)findViewById(R.id.gesturepwd_unlock_forget);
		forgetView.setClickable(true);
		otherLogin.setClickable(true);
		forgetView.setOnClickListener(this);
		otherLogin.setOnClickListener(this);
		//		
		lockPwd = (LockPwd)getIntent().getParcelableExtra("lockPwd");
		DbUtils db = DbUtils.create(this);
		//将手势密码保存到数据库
		try
		{
			db.createTableIfNotExist(LockPwd.class);
			int userId = (Integer)SharedPreferenceUtils.get(this, SharedPreferenceUtils.KEY_USER_ID, -1);
			lockPwd = db.findFirst(Selector.from(LockPwd.class).where("userId", "=", userId));
		}
		catch (DbException e)
		{
			e.printStackTrace();
		}
		finally
		{
			db.close();
		}
		//设置加密
		if (null != lockPwd && null != lockPwd.getPwd())
		{
			lockPwdStr = EncrypUtil.decrypt(DMConstant.StringConstant.ENCRYP_SEND, lockPwd.getPwd());
		}
		findViewById(R.id.btn_back).setVisibility(View.GONE);
		((TextView)findViewById(R.id.title_text)).setText(getString(R.string.title_unlock_gesture_password));
		isCancleable = getIntent().getBooleanExtra("isCancleable", false);
		if (TextUtils.isEmpty(lockPwdStr))
		{
			Activity main = AppManager.getAppManager().getActivity(MainActivity.class);
			if (main == null)
			{
				Intent intent = new Intent(this, MainActivity.class);
				startActivity(intent);
			}
			finish();
		}
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		if (mCountdownTimer != null)
		{
			mCountdownTimer.onTick(0);
		}
	}
	
	/** {@inheritDoc} */
	
	@Override
	protected void onPause()
	{
		super.onPause();
	}
	
	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		if (mCountdownTimer != null)
		{
			mCountdownTimer.cancel();
		}
		AppManager.getAppManager().finishActivity(UnlockGesturePasswordActivity.class);
	}
	
	private Runnable mClearPatternRunnable = new Runnable()
	{
		@Override
		public void run()
		{
			mLockPatternView.clearPattern();
		}
	};
	
	protected LockPatternView.OnPatternListener mChooseNewLockPatternListener = new LockPatternView.OnPatternListener()
	{
		
		@Override
		public void onPatternStart()
		{
			mLockPatternView.removeCallbacks(mClearPatternRunnable);
			patternInProgress();
		}
		
		@Override
		public void onPatternCleared()
		{
			mLockPatternView.removeCallbacks(mClearPatternRunnable);
		}
		
		@Override
		public void onPatternDetected(List<Cell> pattern)
		{
			if (pattern == null)
			{
				return;
			}
			String pwd = LockPatternUtils.patternToString(pattern);
			if (pwd.equals(lockPwdStr))
			{
				ToastUtil.getInstant().show(UnlockGesturePasswordActivity.this, "解锁成功");
				mLockPatternView.setDisplayMode(LockPatternView.DisplayMode.Correct);
				
				// UnlockGesturePasswordActivity 是否是临时弹出，如果应用从后台进入前台，弹出此页面，值为true
				isTemp = getIntent().getBooleanExtra("isTemp", false);
				// 跳转到主界面                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                
				DMLog.e("isTemp", isTemp + "");
				if (!isTemp)
				{
					Intent intent = new Intent(UnlockGesturePasswordActivity.this, MainActivity.class);
					startActivity(intent);
				}
				else
				{
					//走到这里，表示应用是从后台切换到前台（即点击了home键，应用变为后台运行）
				}
				finish();
				DMApplication.getInstance().autoLogin(true, lockPwd, null);
			}
			else
			{
				mLockPatternView.setDisplayMode(LockPatternView.DisplayMode.Wrong);
				if (pattern.size() >= LockPatternUtils.MIN_PATTERN_REGISTER_FAIL)
				{
					mFailedPatternAttemptsSinceLastTimeout++;
					int retry = LockPatternUtils.FAILED_ATTEMPTS_BEFORE_TIMEOUT - mFailedPatternAttemptsSinceLastTimeout;
					if (retry >= 0)
					{
						if (retry == 0)
						{
							showToast("您已5次输错密码，请30秒后再试");
							mLockPatternView.setEnabled(false);
						}
						mHeadTextView.setText("密码错误，还可以再输入" + retry + "次");
					}
					
				}
				else
				{
					showToast("输入长度不够，请重试");
				}
				
				if (mFailedPatternAttemptsSinceLastTimeout >= LockPatternUtils.FAILED_ATTEMPTS_BEFORE_TIMEOUT)
				{
					mHandler.postDelayed(attemptLockout, 2000);
				}
				else
				{
					mLockPatternView.postDelayed(mClearPatternRunnable, 2000);
				}
			}
		}
		
		@Override
		public void onPatternCellAdded(List<Cell> pattern)
		{
		
		}
		
		private void patternInProgress()
		{
		}
	};
	
	Runnable attemptLockout = new Runnable()
	{
		
		@Override
		public void run()
		{
			mLockPatternView.clearPattern();
			mLockPatternView.setEnabled(false);
			//            AppLog.d("create count");
			if (mCountdownTimer != null)
			{
				mCountdownTimer.cancel();
			}
			mCountdownTimer = new CountDownTimer(LockPatternUtils.FAILED_ATTEMPT_TIMEOUT_MS + 1, 1000)
			{
				
				@Override
				public void onTick(long millisUntilFinished)
				{
					remainTime = millisUntilFinished;
					int secondsRemaining = (int)(millisUntilFinished / 1000) - 1;
					if (secondsRemaining > 0)
					{
						mHeadTextView.setText(secondsRemaining + " 秒后重试");
					}
					else
					{
						mHeadTextView.setText("请绘制手势密码");
						//						mHeadTextView.setTextColor(Color.WHITE);
					}
					
				}
				
				@Override
				public void onFinish()
				{
					mLockPatternView.setEnabled(true);
					mFailedPatternAttemptsSinceLastTimeout = 0;
					
					if (visible)
					{
						//页面在前台时处理
					}
					else
					{
						//页面不在前台，关闭应用                            
						finish();
					}
				}
			}.start();
		}
	};
	
	@Override
	public void onClick(View view)
	{
		if (R.id.gesturepwd_unlock_forget == view.getId())
		{
			//忘记密码，进入登录页面
			Intent intent = new Intent(this, LoginActivity.class);
			intent.putExtra("forgetLockPwd", true);
			startActivityForResult(intent, 1);
			DMApplication.toLoginValue = -1;
		}
		else if (R.id.gesturepwd_unlock_other_user == view.getId())
		{
			//使用其它号码，进入登录页面
			Intent intent = new Intent(this, LoginActivity.class);
			intent.putExtra("useOtherUser", true);
			startActivityForResult(intent, 1);
			DMApplication.toLoginValue = -1;
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK)
		{
			//			MainActivity.index = 0;
			finish();
		}
	}
	
	/**
	 * 页面是否在前台
	 */
	private boolean visible = true;
	
	@Override
	protected void onRestart()
	{
		super.onRestart();
		visible = true;
	}
	
	@Override
	protected void onStop()
	{
		super.onStop();
		visible = false;
	}
}
