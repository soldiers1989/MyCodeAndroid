package com.hxjr.p2p.ad5.ui.discovery.spread;

import java.util.List;

import com.hxjr.p2p.ad5.bean.SpreadInfo;
import com.hxjr.p2p.ad5.bean.SpreadInfo.SpreadEntity;
import com.hxjr.p2p.ad5.ui.BaseActivity;
import com.hxjr.p2p.ad5.utils.FormatUtil;
import com.hxjr.p2p.ad5.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 我的推广
 * @author  huangkaibo
 * @date 2015年11月9日
 */
public class MySpreadActivity extends BaseActivity
{
	private TextView total_spread_number_tv;//推广客户总数
	
	private TextView total_reward_tv; //持续奖励总计
	
	private TextView effective_spread_reward_tv; //有效推广奖励
	
	private TextView total_spread_reward_tv; //推广奖励总计
	
	private LinearLayout invited_users_ll; //有邀请的好友时显示
	
	private TextView no_invited_users_tv; //没有邀请的好友时显示
	
	private SpreadInfo spreadInfo; //邀请信息，保存了邀请码，邀请地址和推广客户总数等数据
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_spread);
		Intent intent = getIntent();
		if (null != intent)
		{
			spreadInfo = (SpreadInfo)intent.getSerializableExtra("spreadInfo");
			if (null == spreadInfo)
			{
				spreadInfo = new SpreadInfo();
			}
		}
		initView();
		postData();
	}
	
	@Override
	protected void initView()
	{
		super.initView();
		((TextView)findViewById(R.id.title_text)).setText(R.string.discovery_my_spread);
		total_spread_number_tv = (TextView)findViewById(R.id.total_spread_number_tv);
		total_reward_tv = (TextView)findViewById(R.id.total_reward_tv);
		effective_spread_reward_tv = (TextView)findViewById(R.id.effective_spread_reward_tv);
		total_spread_reward_tv = (TextView)findViewById(R.id.total_spread_reward_tv);
		invited_users_ll = (LinearLayout)findViewById(R.id.invited_users_ll);
		no_invited_users_tv = (TextView)findViewById(R.id.no_invited_users_tv);
		
		total_spread_number_tv.setText(spreadInfo.getYqCount() + "");
		total_reward_tv.setText(FormatUtil.formatStr2(spreadInfo.getRewardCxTotal() + ""));
		effective_spread_reward_tv.setText(FormatUtil.formatStr2(spreadInfo.getRewardYxTotal() + ""));
		total_spread_reward_tv.setText(FormatUtil.formatStr2(spreadInfo.getRewardTotal() + ""));
	}
	
	private void postData()
	{
		List<SpreadEntity> spreadEntitys = spreadInfo.getSpreadEntity();
		if (null != spreadEntitys && spreadEntitys.size() > 0)
		{
			invited_users_ll.setVisibility(View.VISIBLE);
			no_invited_users_tv.setVisibility(View.GONE);
			for (SpreadEntity spreadEntity : spreadEntitys)
			{
				View inflate = LayoutInflater.from(this).inflate(R.layout.my_spread_item, invited_users_ll, false);
				TextView name_of_friend_tv = (TextView)inflate.findViewById(R.id.name_of_friend_tv);
				TextView spread_date_tv = (TextView)inflate.findViewById(R.id.spread_date_tv);
				name_of_friend_tv.setText(spreadEntity.getUserName());
				spread_date_tv.setText(spreadEntity.getRegisterTime());
				invited_users_ll.addView(inflate);
			}
		}
		else
		{
			no_invited_users_tv.setVisibility(View.VISIBLE);
			invited_users_ll.setVisibility(View.GONE);
		}
	}
	
	/**
	 * 马上去邀请
	 * @param v
	 */
	public void goInvite(View v)
	{
		if (checkClick(v.getId()))
		{
			finish();
		}
	}
}
