package com.hxjr.p2p.ad5.ui.mine.setting;

import com.hxjr.p2p.ad5.DMApplication;
import com.hxjr.p2p.ad5.ui.BaseActivity;
import com.hxjr.p2p.ad5.ui.mine.user.LoginActivity;
import com.hxjr.p2p.ad5.utils.UIHelper;
import com.hxjr.p2p.ad5.R;
import com.dm.widgets.utils.AlertDialogUtil;
import com.dm.widgets.utils.AlertDialogUtil.ConfirmListener;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

/**
 * 设置页面
 * @author  huangkaibo
 * @date 2015年10月28日
 */
public class SettingActivity extends BaseActivity
{
	private boolean isNeedPwd;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting);
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
		((TextView)findViewById(R.id.title_text)).setText(R.string.setting_title);
	}
	
	/**
	 * 跳转到安全信息
	 * @param v
	 */
	public void gotoSecurity(View v)
	{
		if (checkClick(v.getId())) //防重复点击	
		{
			Intent intent = new Intent(this, SecurityInfoActivity.class).putExtra("isNeedPwd", isNeedPwd);
			startActivity(intent);
		}
	}
	
	/**
	 * 跳转到密码管理
	 * @param v
	 */
	public void gotoPwdManager(View v)
	{
		if (checkClick(v.getId())) //防重复点击	
		{
			Intent intent = new Intent(this, PwdManagerActivity.class).putExtra("isNeedPwd", isNeedPwd);
			startActivity(intent);
		}
	}
	
	/**
	 * 跳转到关于
	 * @param v
	 */
	public void gotoAbout(View v)
	{
		if (checkClick(v.getId())) //防重复点击	
		{
			Intent intent = new Intent(this, AboutActivity.class);
			startActivity(intent);
		}
	}
	
	/**
	 * 退出登录
	 * @param v
	 */
	public void loginOut(View v)
	{
		if (checkClick(v.getId())) //防重复点击	
		{
			AlertDialogUtil.confirm(this, getString(R.string.do_you_want_login_out), new ConfirmListener()
			{
				@Override
				public void onOkClick()
				{
					DMApplication.toLoginValue = 3;
					startActivity(new Intent(SettingActivity.this, LoginActivity.class).putExtra("isfromMine", true));
					UIHelper.doLoginOut(SettingActivity.this, false);
				}
				
				@Override
				public void onCancelClick()
				{
				
				}
			});
			
		}
	}
}
