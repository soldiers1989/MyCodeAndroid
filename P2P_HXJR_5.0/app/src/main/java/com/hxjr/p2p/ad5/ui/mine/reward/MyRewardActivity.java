package com.hxjr.p2p.ad5.ui.mine.reward;

import org.json.JSONException;
import org.json.JSONObject;

import com.hxjr.p2p.ad5.ui.BaseActivity;
import com.hxjr.p2p.ad5.utils.DMConstant;
import com.hxjr.p2p.ad5.utils.ErrorUtil;
import com.hxjr.p2p.ad5.R;
import com.dm.http.HttpCallBack;
import com.dm.http.HttpUtil;
import com.dm.utils.DMJsonObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

/**
 * 我的奖励详情
 * @author  huangkaibo
 * @date 2015年12月3日
 */
public class MyRewardActivity extends BaseActivity implements OnClickListener
{
	private TextView experience_money_tv;
	
	private TextView red_num_tv;
	
	private TextView voucher_num_tv;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_reward);
		initView();
		initData();
	}
	
	@Override
	protected void initView()
	{
		super.initView();
		((TextView)findViewById(R.id.title_text)).setText(R.string.account_my_reward);
		findViewById(R.id.experience_layout).setOnClickListener(this);
		findViewById(R.id.red_layout).setOnClickListener(this);
		findViewById(R.id.voucher_layout).setOnClickListener(this);
		experience_money_tv = (TextView)findViewById(R.id.experience_money_tv);
		red_num_tv = (TextView)findViewById(R.id.red_num_tv);
		voucher_num_tv = (TextView)findViewById(R.id.voucher_num_tv);
	}
	
	private void initData()
	{
		getMyRewardInfo();
	}
	
	private void getMyRewardInfo()
	{
		HttpUtil.getInstance().post(this, DMConstant.API_Url.MY_REWARD_INFO, new HttpCallBack()
		{
			@Override
			public void onSuccess(JSONObject result)
			{
				try
				{
					String code = result.getString("code");
					if (DMConstant.ResultCode.SUCCESS.equals(code))
					{
						DMJsonObject data = new DMJsonObject(result.getString("data"));
						experience_money_tv.setText(data.getString("experAmonut"));
						red_num_tv.setText(data.getInt("totalHbAmount") + "");
						voucher_num_tv.setText(data.getInt("totalJxqAmount") + "");
					}
					else
					{
						ErrorUtil.showError(result);
					}
				}
				catch (JSONException e)
				{
					// TODO Auto-generated catch block
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
	
	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.experience_layout:
				//体验金
				Intent expIntent = new Intent(this, ExperienceListActivity.class);
				startActivity(expIntent);
				break;
			case R.id.red_layout:
				//红包
				Intent redIntent = new Intent(this, RedEnvelopeActivity.class);
				startActivity(redIntent);
				break;
			case R.id.voucher_layout:
				//加息券
				Intent voucherIntent = new Intent(this, VoucherActivity.class);
				startActivity(voucherIntent);
				break;
				
			default:
				break;
		}
	}
	
}
