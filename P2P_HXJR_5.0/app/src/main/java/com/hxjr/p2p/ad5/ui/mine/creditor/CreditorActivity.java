package com.hxjr.p2p.ad5.ui.mine.creditor;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.hxjr.p2p.ad5.ui.BaseActivity;
import com.hxjr.p2p.ad5.ui.mine.CommonFragmentAdapter;
import com.hxjr.p2p.ad5.ui.mine.CommonFragmentAdapter.OnExtraPageChangeListener;
import com.hxjr.p2p.ad5.R;

/**
 * 债权转让
 * @author  huangkaibo
 * @date 2015-11-2
 */
public class CreditorActivity extends BaseActivity implements OnClickListener
{
	
	private ViewPager viewPager;
	
	private FrameLayout trunIningBtn;// 转让中
	
	private FrameLayout trunOutBtn; //已转让
	
	private FrameLayout trunInBtn; // 已转入
	
    private TextView turnIningTxt;
	
	private TextView turnInTxt;
	
	private TextView turnOutTxt;
	
	private View turnIningLine;
	
	private View turnInLine;
	
	private View turnOutLine;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.creditor_turn_out_in);
		
		initView();
	}
	
	@Override
	protected void initView()
	{
		super.initView();
		((TextView)findViewById(R.id.title_text)).setText(R.string.investment_zqzr);
		
		viewPager = (ViewPager)findViewById(R.id.creditor_turn_out_in_viewpager);
		trunIningBtn = (FrameLayout)findViewById(R.id.creditor_turn_ining_frame);
		trunInBtn = (FrameLayout)findViewById(R.id.creditor_turn_in_frame);
		trunOutBtn = (FrameLayout)findViewById(R.id.creditor_turn_out_frame);
		
		turnIningTxt = (TextView)findViewById(R.id.creditor_turn_ining_txt);
		turnInTxt = (TextView)findViewById(R.id.creditor_turn_in_txt);
		turnOutTxt = (TextView)findViewById(R.id.creditor_turn_out_txt);
		
		turnIningLine = findViewById(R.id.creditor_turn_ining_line);
		turnInLine = findViewById(R.id.creditor_turn_in_line);
		turnOutLine = findViewById(R.id.creditor_turn_out_line);
		
		trunIningBtn.setOnClickListener(this);
		trunInBtn.setOnClickListener(this);
		trunOutBtn.setOnClickListener(this);
		
		initCreditorViewPager();
	}
	
	/**
	 * 
	 */
	private void initCreditorViewPager()
	{
		List<Fragment> fragments = new ArrayList<Fragment>();
		
		fragments.add(new CreditorTurningFragment()); //转让中
		fragments.add(new CreditorTurnOutFragment()); //已转出
		fragments.add(new CreditorTurnInFragment()); //转入
		
		CommonFragmentAdapter adapter =
			new CommonFragmentAdapter(fragments, this.getSupportFragmentManager(), viewPager);
		adapter.setOnExtraPageChangeListener(new OnExtraPageChangeListener()
		{
			public void onExtraPageSelected(int i)
			{
				switchBtnList(i);
			}
		});
	}
	
	private void switchBtnList(int index)
	{
		switch (index)
		{
			case 0:
				turnIningTxt.setTextColor(getResources().getColor(R.color.main_color));
				turnIningLine.setBackgroundColor(getResources().getColor(R.color.main_color));
				
				turnOutTxt.setTextColor(getResources().getColor(R.color.text_gray));
				turnOutLine.setBackgroundColor(getResources().getColor(R.color.light_gray));
				
				turnInTxt.setTextColor(getResources().getColor(R.color.text_gray));
				turnInLine.setBackgroundColor(getResources().getColor(R.color.light_gray));
				break;
			case 1:
				turnIningTxt.setTextColor(getResources().getColor(R.color.text_gray));
				turnIningLine.setBackgroundColor(getResources().getColor(R.color.light_gray));
				
				turnOutTxt.setTextColor(getResources().getColor(R.color.main_color));
				turnOutLine.setBackgroundColor(getResources().getColor(R.color.main_color));
				
				turnInTxt.setTextColor(getResources().getColor(R.color.text_gray));
				turnInLine.setBackgroundColor(getResources().getColor(R.color.light_gray));
				break;
			case 2:
				turnIningTxt.setTextColor(getResources().getColor(R.color.text_gray));
				turnIningLine.setBackgroundColor(getResources().getColor(R.color.light_gray));
				
				turnOutTxt.setTextColor(getResources().getColor(R.color.text_gray));
				turnOutLine.setBackgroundColor(getResources().getColor(R.color.light_gray));
				
				turnInTxt.setTextColor(getResources().getColor(R.color.main_color));
				turnInLine.setBackgroundColor(getResources().getColor(R.color.main_color));
				
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
			case R.id.creditor_turn_ining_frame:
				viewPager.setCurrentItem(0);
				break;
			case R.id.creditor_turn_out_frame:
				viewPager.setCurrentItem(1);
				break;
			case R.id.creditor_turn_in_frame:
				viewPager.setCurrentItem(2);
				break;
			default:
				break;
		}
		
	}
}
