package com.hxjr.p2p.ad5.ui.mine.setting;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.dm.widgets.utils.AlertDialogUtil;
import com.hxjr.p2p.ad5.R;
import com.hxjr.p2p.ad5.ui.BaseActivity;
import com.hxjr.p2p.ad5.utils.Method;
import com.hxjr.p2p.ad5.utils.SharedPreferenceUtils;
import com.hxjr.p2p.ad5.utils.UpdateManager;

/**
 * 关于APP
 * @author  jiaohongyun
 * @date 2015年5月28日
 */
public class AboutActivity extends BaseActivity
{
	private TextView phoneNum;
	
	private TextView versionNameTV;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);
		initView();
		initData();
	}
	
	/**
	 * 初始化数据,获取客服电话
	 */
	private void initData()
	{
		String phoneNumStr = (String)SharedPreferenceUtils.get(this, "consts", "tel", "");
		phoneNum.setText(phoneNumStr);
	}
	
	/**
	 * 初始化页面元素
	 */
	@Override
	protected void initView()
	{
		super.initView();
		((TextView)findViewById(R.id.title_text)).setText(R.string.setting_about);
		phoneNum = (TextView)findViewById(R.id.phoneNum);
		versionNameTV = (TextView)findViewById(R.id.versionNameTV);
		versionNameTV.setText(Method.getVersion(this));
	}
	
	/**
	 * 检查更新
	 * @param v
	 */
	public void update(View v)
	{
		if (checkClick(v.getId())) //防重复点击	
		{
			/** 检查更新弹窗 */
			AlertDialogUtil.confirm(AboutActivity.this, "确定检查更新？", new AlertDialogUtil.ConfirmListener()
			{
				@Override
				public void onOkClick()
				{
					UpdateManager.getInstance().checkForUpdate(AboutActivity.this, versionNameTV.getText().toString(), true);
				}
				
				@Override
				public void onCancelClick()
				{
				
				}
			});
		}
	}
	
	/**
	 * 拨打客服电话
	 * @param v
	 */
	public void phone(View v)
	{
		if (checkClick(v.getId())) //防重复点击	
		{
			String phone = phoneNum.getText().toString().replace("-", "").replace(" ", "").trim();
			//			//传入服务， parse（）解析号码
			//			Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
			//			//通知activtity处理传入的call服务
			//			startActivity(intent);
			Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
			startActivity(intent);
		}
	}
}
