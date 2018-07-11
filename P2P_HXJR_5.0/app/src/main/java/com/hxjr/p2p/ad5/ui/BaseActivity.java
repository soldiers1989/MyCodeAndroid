package com.hxjr.p2p.ad5.ui;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.WindowManager;

import com.dm.utils.AppManager;
import com.dm.utils.ProgressDialogShowing;
import com.hxjr.p2p.ad5.DMApplication;
import com.hxjr.p2p.ad5.R;
import com.hxjr.p2p.ad5.ui.mine.setting.gesture.UnlockGesturePasswordActivity;
import com.hxjr.p2p.ad5.ui.mine.user.LoginActivity;
import com.hxjr.p2p.ad5.utils.Method;
import com.hxjr.p2p.ad5.utils.SharedPreferenceUtils;

/**
 * 应用程序Activity的基类
 * 
 * @author jiaohongyun
 * @date 2015年5月22日
 */
public class BaseActivity extends FragmentActivity implements ProgressDialogShowing
{
	
	/**
	 * 保存上一次点击时间
	 */
	private SparseArray<Long> lastClickTimes;
	
	boolean isActive = true;// activity是否活动
	
	/**
	 * 是否需要显示手势密码
	 */
	protected boolean mustShowShouShi = true;
	
	/**
	 * 加载进度 （如需修改样式，改show方法就可以了）
	 */
	private Dialog progressDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		 this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		// 添加Activity到堆栈
		AppManager.getAppManager().addActivity(this);
		lastClickTimes = new SparseArray<Long>();
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
		{
			getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
				WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		}
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		View view = findViewById(R.id.main_title);
		View statusBar = null;
		if (view != null)
		{
			statusBar = view.findViewById(R.id.statusBar);
		}
		if (statusBar != null)
		{
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
			{
				statusBar.setVisibility(View.VISIBLE);
			}
			else
			{
				statusBar.setVisibility(View.GONE);
			}
		}
		if (!isActive)
		{
			// app 从后台唤醒，进入前台
			isActive = true;
			long temp = System.currentTimeMillis();
			boolean showUnlock = false;
			if (lastActive != 0 && temp - lastActive > SHOW_UNLOCK_TIME)
			{
				showUnlock = true;
				if (!(Boolean)SharedPreferenceUtils.get(this, SharedPreferenceUtils.KEY_HAS_LOCKED, false))
				{
					startActivity(new Intent(this, LoginActivity.class));
					return;
				}
			}
			else
			{
				//				Activity activity = AppManager.getAppManager().currentActivity();
				if (this instanceof UnlockGesturePasswordActivity)
				{
					showUnlock = true;
				}
			}
			if (DMApplication.getInstance().islogined() && mustShowShouShi && showUnlock)
			{
				//这样判断是为了解决该现象：解锁进入应用后，按home键回到桌面，过5分钟后打开应用，这时出现解锁界面
				//再按home键回到桌面，过5分钟后打开应用，会再次弹出解锁界面。这样就有多个了。
				if (!(AppManager.getAppManager().currentActivity() instanceof UnlockGesturePasswordActivity))
				{
					new Handler()
					{//解决回到桌面5分钟后，再次回到应用，解锁成功后dialog不消失的问题。
						@Override
						public void handleMessage(Message msg)
						{
							dismiss();
							removeMessages(0);
						}
					}.sendEmptyMessageDelayed(0, 2000);
					Intent it = new Intent(this, UnlockGesturePasswordActivity.class);
					it.putExtra("isTemp", true);// UnlockGesturePasswordActivity 是否是临时弹出
					it.putExtra("isCancleable", false);
					startActivity(it);
				}
			}
		}
	}
	
	@Override
	protected void onPause()
	{
		super.onPause();
	}
	
	@Override
	protected void onStop()
	{
		super.onStop();
		if (!Method.isRunningPackName(this) || !Method.isScreenOn(this))
		{
			isActive = false;
			lastActive = System.currentTimeMillis();
		}
	}
	
	@Override
	protected void onDestroy()
	{
		// 结束Activity&从堆栈中移除
		AppManager.getAppManager().finishActivity(this);
		lastClickTimes = null;
		super.onDestroy();
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
	
	/**
	 * 初始化页面元素
	 */
	protected void initView()
	{
		// 设置返回按钮事件
		findViewById(R.id.btn_back).setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				finish();
			}
		});
	}
	
	/**
	 * 上一次退到后台时间
	 */
	private long lastActive = 0L;
	
	/**
	 * 多久显示手势密码
	 */
	private static final long SHOW_UNLOCK_TIME = 300000L;
	
	/**
	 * 检查是否可执行点击操作 防重复点击
	 * 
	 * @return 返回true则可执行
	 */
	public boolean checkClick(int id)
	{
		Long lastTime = lastClickTimes.get(id);
		Long thisTime = System.currentTimeMillis();
		lastClickTimes.put(id, thisTime);
		if (lastTime != null && thisTime - lastTime < 1000)
		{
			// 快速双击，第二次不处理
			return false;
		}
		else
		{
			return true;
		}
	}
	
	@Override
	public void show()
	{
		if (!isShowing())
		{
			//如需修改样式，修改这里
			progressDialog = ProgressDialog.show(this, null, "加载中...", true, false);
		}
		progressDialog.show();
	}
	
	@Override
	public boolean isShowing()
	{
		if (progressDialog == null)
		{
			return false;
		}
		else if (progressDialog.isShowing())
		{
			return true;
		}
		else
		{
			progressDialog = null;
			return false;
		}
	}
	
	@Override
	public void dismiss()
	{
		if (isShowing())
		{
			progressDialog.dismiss();
		}
	}

	@Override
	public void show(String content)
	{
		if (!isShowing())
		{
			//如需修改样式，修改这里
			progressDialog = ProgressDialog.show(this, null, content, true, false);
		}
		progressDialog.show();
		
	}
}
