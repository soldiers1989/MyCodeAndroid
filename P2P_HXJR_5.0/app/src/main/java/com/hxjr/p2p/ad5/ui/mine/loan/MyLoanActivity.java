package com.hxjr.p2p.ad5.ui.mine.loan;

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
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 我的借款
 * @author  huangkaibo
 * @date 2015-11-17
 */
public class MyLoanActivity extends BaseActivity implements OnClickListener
{
	private ViewPager viewPager; //new add
	
	private FrameLayout loanRepaymentInBtn; //还款中
	
	private FrameLayout loanPayOffBtn; //已还清
	
	private TextView loanRepaymentInTxt;
	
	private TextView loanPayOffTxt;
	
	private View loanRepaymentInLine;
	
	private View loanPayOffLine;
	
	private ImageView backIV; //back button
	
	private List<Fragment> fragments = new ArrayList<Fragment>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_loan);
		initView();
	}
	
	@Override
	protected void initView()
	{
		((TextView)findViewById(R.id.title_text)).setText(R.string.account_my_loan);
		backIV = (ImageView)findViewById(R.id.btn_back);
		
		viewPager = (ViewPager)findViewById(R.id.my_loan_viewpager);
		
		loanRepaymentInBtn = (FrameLayout)findViewById(R.id.my_loan_repayment_in_frame);
		loanPayOffBtn = (FrameLayout)findViewById(R.id.my_loan_pay_off_frame);
		
		loanRepaymentInTxt = (TextView)findViewById(R.id.my_loan_repayment_in_txt);
		loanPayOffTxt = (TextView)findViewById(R.id.my_loan_pay_off_txt);
		
		loanRepaymentInLine = findViewById(R.id.my_loan_repayment_in_line);
		loanPayOffLine = findViewById(R.id.my_loan_pay_off_line);
		
		loanRepaymentInBtn.setOnClickListener(this);
		loanPayOffBtn.setOnClickListener(this);
		backIV.setOnClickListener(this);
		
		initMyLoanViewPager();
	}
	
	private void initMyLoanViewPager()
	{
		viewPager.setOffscreenPageLimit(0);
		fragments.add(new MyLoanRepaymentFragment()); //我的借款-还款中
		fragments.add(new MyLoanPayOffFragment()); //我的借款-已还清
		
		CommonFragmentAdapter adapter = new CommonFragmentAdapter(fragments, this.getSupportFragmentManager(), viewPager);
		adapter.setOnExtraPageChangeListener(new OnExtraPageChangeListener()
		{
			public void onExtraPageSelected(int i)
			{
				switchButtonList(i);
			}
		});
	}
	
	private void switchButtonList(int index)
	{
		switch (index)
		{
			case 0:
				loanRepaymentInTxt.setTextColor(getResources().getColor(R.color.main_color));
				loanRepaymentInLine.setBackgroundColor(getResources().getColor(R.color.main_color));
				
				loanPayOffTxt.setTextColor(getResources().getColor(R.color.text_gray));
				loanPayOffLine.setBackgroundColor(getResources().getColor(R.color.light_gray));
				break;
			case 1:
				loanRepaymentInTxt.setTextColor(getResources().getColor(R.color.text_gray));
				loanRepaymentInLine.setBackgroundColor(getResources().getColor(R.color.light_gray));
				
				loanPayOffTxt.setTextColor(getResources().getColor(R.color.main_color));
				loanPayOffLine.setBackgroundColor(getResources().getColor(R.color.main_color));
				break;
			case 2:
				loanRepaymentInTxt.setTextColor(getResources().getColor(R.color.text_gray));
				loanRepaymentInLine.setBackgroundColor(getResources().getColor(R.color.light_gray));
				
				loanPayOffTxt.setTextColor(getResources().getColor(R.color.text_gray));
				loanPayOffLine.setBackgroundColor(getResources().getColor(R.color.light_gray));
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
			case R.id.my_loan_repayment_in_frame:
				viewPager.setCurrentItem(0);
				break;
			case R.id.my_loan_pay_off_frame:
				viewPager.setCurrentItem(1);
				break;
			case R.id.btn_back:
				finish();
				break;
			default:
				break;
		}
	}
}
