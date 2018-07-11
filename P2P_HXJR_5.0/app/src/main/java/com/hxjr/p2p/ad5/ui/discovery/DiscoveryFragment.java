package com.hxjr.p2p.ad5.ui.discovery;

import com.hxjr.p2p.ad5.DMApplication;
import com.hxjr.p2p.ad5.ui.discovery.donation.DonationListActivity;
import com.hxjr.p2p.ad5.ui.discovery.moneymanage.MoneyManageActivity;
import com.hxjr.p2p.ad5.ui.discovery.news.NewsActivity;
import com.hxjr.p2p.ad5.ui.discovery.rich.RichListActivity;
import com.hxjr.p2p.ad5.ui.discovery.spread.SpreadRewardActivity;
import com.hxjr.p2p.ad5.ui.mine.user.LoginActivity;
import com.hxjr.p2p.ad5.R;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * 发现
 * @author  jiaohongyun
 * @date 2015年10月19日
 */
public class DiscoveryFragment extends Fragment implements OnClickListener
{
	private View mView;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		return inflater.inflate(R.layout.main_page_discovery, container, false);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		mView = getView();
		initView();
	}
	
	@Override
	public void onResume()
	{
		super.onResume();
		View view = mView.findViewById(R.id.main_title);
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
	}
	
	private void initView()
	{
		mView.findViewById(R.id.btn_back).setVisibility(View.GONE);
		((TextView)mView.findViewById(R.id.title_text)).setText(R.string.page_title_discovery);
		
		mView.findViewById(R.id.news_tv).setOnClickListener(this);
		mView.findViewById(R.id.rich_list_tv).setOnClickListener(this);
		mView.findViewById(R.id.manage_money_calculator_tv).setOnClickListener(this);
		mView.findViewById(R.id.commonweal_bid_tv).setOnClickListener(this);
		mView.findViewById(R.id.spread_reward_tv).setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.news_tv:
				// 媒体报道
				startActivity(new Intent(getActivity(), NewsActivity.class));
				break;
			case R.id.rich_list_tv:
				// 土豪榜
				startActivity(new Intent(getActivity(), RichListActivity.class));
				break;
			case R.id.manage_money_calculator_tv:
				// 理财计算器
				startActivity(new Intent(getActivity(), MoneyManageActivity.class));
				break;
			case R.id.commonweal_bid_tv:
				// 公益捐赠
				startActivity(new Intent(getActivity(), DonationListActivity.class));
				break;
			case R.id.spread_reward_tv:
				// 推广有奖
				if (DMApplication.getInstance().islogined())
				{
					startActivity(new Intent(getActivity(), SpreadRewardActivity.class));
				}
				else
				{
					DMApplication.toLoginValue = 2;
					startActivity(new Intent(getActivity(), LoginActivity.class));
				}
				break;
				
			default:
				break;
		}
	}
	
}
