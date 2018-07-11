package com.hxjr.p2p.ad5.ui.mine.invest;

import java.util.ArrayList;
import java.util.List;

import com.hxjr.p2p.ad5.ui.BaseActivity;
import com.hxjr.p2p.ad5.ui.mine.CommonFragmentAdapter;
import com.hxjr.p2p.ad5.ui.mine.CommonFragmentAdapter.OnExtraPageChangeListener;
import com.hxjr.p2p.ad5.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * 我的投资
 * @author  huangkaibo
 * @date 2015-11-2
 */
public class MyInvestMentActivity extends BaseActivity implements OnClickListener
{
	private ViewPager viewPager;
	
	private FrameLayout repaymenBtn; //回款按钮
	
	private FrameLayout bidBtn; //投标按钮
	
	private FrameLayout settledBtn; //已结清按钮
	
	private TextView repaymenTxt;
	
	private TextView bidTxt;
	
	private TextView settledTxt;
	
	private View repaymenLine;
	
	private View bidLine;
	
	private View settledLine;
	
	private List<Fragment> fragments = new ArrayList<Fragment>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_investment);
		initView();
	}
	
	@Override
	protected void initView()
	{
		super.initView();
		((TextView)findViewById(R.id.title_text)).setText(R.string.account_my_bid);
		
		repaymenBtn = (FrameLayout)findViewById(R.id.invest_repayment_frame);
		bidBtn = (FrameLayout)findViewById(R.id.invest_bid_frame);
		settledBtn = (FrameLayout)findViewById(R.id.invest_settled_frame);
		
		repaymenTxt = (TextView)findViewById(R.id.repaymentTxt);
		bidTxt = (TextView)findViewById(R.id.bidTxt);
		settledTxt = (TextView)findViewById(R.id.settledTxt);
		
		repaymenLine = findViewById(R.id.repayment_line);
		bidLine = findViewById(R.id.bid_line);
		settledLine = findViewById(R.id.settled_line);
		
		viewPager = (ViewPager)findViewById(R.id.my_invest_viewpager);
		
		repaymenBtn.setOnClickListener(this);
		bidBtn.setOnClickListener(this);
		settledBtn.setOnClickListener(this);
		
		initInvsetViewPager();
		
	}
	
	private void initInvsetViewPager()
	{
		fragments.add(new RepaymentFragment()); //我的投资-回款列表
		fragments.add(new InvestInFragment()); //我的投资-投标中
		fragments.add(new SettleFragment()); // 我的投资-已结清
		
		CommonFragmentAdapter adapter = new CommonFragmentAdapter(fragments, this.getSupportFragmentManager(), viewPager);
		adapter.setOnExtraPageChangeListener(new OnExtraPageChangeListener()
		{
			public void onExtraPageSelected(int i)
			{
				switchBtnList(i);
			}
		});
		
	}
	
	/**
	 * 切换按钮属性
	 */
	private void switchBtnList(int index)
	{
		switch (index)
		{
			case 0:
				repaymenTxt.setTextColor(getResources().getColor(R.color.main_color));
				repaymenLine.setBackgroundColor(getResources().getColor(R.color.main_color));
				
				bidTxt.setTextColor(getResources().getColor(R.color.text_gray));
				bidLine.setBackgroundColor(getResources().getColor(R.color.light_gray));
				
				settledTxt.setTextColor(getResources().getColor(R.color.text_gray));
				settledLine.setBackgroundColor(getResources().getColor(R.color.light_gray));
				
				break;
			case 1:
				repaymenTxt.setTextColor(getResources().getColor(R.color.text_gray));
				repaymenLine.setBackgroundColor(getResources().getColor(R.color.light_gray));
				
				bidTxt.setTextColor(getResources().getColor(R.color.main_color));
				bidLine.setBackgroundColor(getResources().getColor(R.color.main_color));
				
				settledTxt.setTextColor(getResources().getColor(R.color.text_gray));
				settledLine.setBackgroundColor(getResources().getColor(R.color.light_gray));
				
				break;
			case 2:
				repaymenTxt.setTextColor(getResources().getColor(R.color.text_gray));
				repaymenLine.setBackgroundColor(getResources().getColor(R.color.light_gray));
				
				bidTxt.setTextColor(getResources().getColor(R.color.text_gray));
				bidLine.setBackgroundColor(getResources().getColor(R.color.light_gray));
				
				settledTxt.setTextColor(getResources().getColor(R.color.main_color));
				settledLine.setBackgroundColor(getResources().getColor(R.color.main_color));
				
				break;
			default:
				break;
		}
	}
	
	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.invest_repayment_frame:
				viewPager.setCurrentItem(0);
				break;
			case R.id.invest_bid_frame:
				viewPager.setCurrentItem(1);
				break;
			case R.id.invest_settled_frame:
				viewPager.setCurrentItem(2);
				break;
			default:
				break;
		}
	}
}
