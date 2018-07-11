package com.hxjr.p2p.ad5.ui.mine.bank;

import com.hxjr.p2p.ad5.DMApplication;
import com.hxjr.p2p.ad5.bean.BankCard;
import com.hxjr.p2p.ad5.bean.UserInfo;
import com.hxjr.p2p.ad5.ui.BaseActivity;
import com.hxjr.p2p.ad5.R;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

/**
 * @author  tangjian
 * @date 2015-8-17
 */
public class BankCardInfoActivity extends BaseActivity
{
	private TextView openName;
	
	private TextView cardNum;
	
	private TextView bank;
	
	private BankCard cardInfo;
	
	private UserInfo userInfo;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bank_card_info_activity);
		initView();
		initData();
	}
	
	/**
	 * 填充银行卡信息
	 */
	private void initData()
	{
		Intent intent = getIntent();
		cardInfo = (BankCard)intent.getSerializableExtra("cardInfo");
		userInfo = DMApplication.getInstance().getUserInfo();
		openName.setText(userInfo.getRealName()); //开户名默认是用户真实姓名
		cardNum.setText(cardInfo.getBankNumber());
		bank.setText(cardInfo.getBankname());
	}
	
	@Override
	protected void initView()
	{
		super.initView();
		((TextView)findViewById(R.id.title_text)).setText(R.string.my_bank_card_info);
		openName = (TextView)findViewById(R.id.open_name_tv);
		cardNum = (TextView)findViewById(R.id.card_number_tv);
		bank = (TextView)findViewById(R.id.bank_tv);
	}
}
