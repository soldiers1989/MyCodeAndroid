package com.hxjr.p2p.ad5.ui.discovery.rich;

import java.util.ArrayList;
import java.util.List;

import com.hxjr.p2p.ad5.ui.BaseActivity;
import com.hxjr.p2p.ad5.ui.MainActivity;
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
 * 土豪榜
 * @author  huangkaibo
 * @date 2015年11月7日
 */
public class RichListActivity extends BaseActivity implements OnClickListener
{
	
	private ViewPager viewPager;
	
	private FrameLayout weekBtn; //周排行按钮
	
	private FrameLayout monthBtn; //月排行按钮
	
	private FrameLayout yearBtn; //年排行按钮
	
	private TextView weekTxt;
	
	private TextView monthTxt;
	
	private TextView yearTxt;
	
	private View weekLine;
	
	private View monthLine;
	
	private View yearLine;
	
	private List<Fragment> fragments = new ArrayList<Fragment>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rich_list_activity);
		initView();
	}
	
	@Override
	protected void initView()
	{
		super.initView();
		((TextView)findViewById(R.id.title_text)).setText(R.string.discovery_rich_list);
		
		weekBtn = (FrameLayout)findViewById(R.id.week_ranking_frame);
		monthBtn = (FrameLayout)findViewById(R.id.month_ranking_frame);
		yearBtn = (FrameLayout)findViewById(R.id.year_ranking_frame);
		
		weekTxt = (TextView)findViewById(R.id.week_ranking_tv);
		monthTxt = (TextView)findViewById(R.id.month_ranking_tv);
		yearTxt = (TextView)findViewById(R.id.year_ranking_tv);
		
		weekLine = findViewById(R.id.week_ranking_line);
		monthLine = findViewById(R.id.month_ranking_line);
		yearLine = findViewById(R.id.year_ranking_line);
		
		viewPager = (ViewPager)findViewById(R.id.rich_list_pager);
		viewPager.setOffscreenPageLimit(3);
		
		weekBtn.setOnClickListener(this);
		monthBtn.setOnClickListener(this);
		yearBtn.setOnClickListener(this);
		
		initInvsetViewPager();
	}
	
	private void initInvsetViewPager()
	{
		fragments.add(new WeekFragment()); //周排行
		fragments.add(new MonthFragment()); //月排行
		fragments.add(new YearFragment()); ///年排行
		
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
				weekTxt.setTextColor(getResources().getColor(R.color.main_color));
				weekLine.setBackgroundColor(getResources().getColor(R.color.main_color));
				
				monthTxt.setTextColor(getResources().getColor(R.color.text_gray));
				monthLine.setBackgroundColor(getResources().getColor(R.color.light_gray));
				
				yearTxt.setTextColor(getResources().getColor(R.color.text_gray));
				yearLine.setBackgroundColor(getResources().getColor(R.color.light_gray));
				
				break;
			case 1:
				weekTxt.setTextColor(getResources().getColor(R.color.text_gray));
				weekLine.setBackgroundColor(getResources().getColor(R.color.light_gray));
				
				monthTxt.setTextColor(getResources().getColor(R.color.main_color));
				monthLine.setBackgroundColor(getResources().getColor(R.color.main_color));
				
				yearTxt.setTextColor(getResources().getColor(R.color.text_gray));
				yearLine.setBackgroundColor(getResources().getColor(R.color.light_gray));
				
				break;
			case 2:
				weekTxt.setTextColor(getResources().getColor(R.color.text_gray));
				weekLine.setBackgroundColor(getResources().getColor(R.color.light_gray));
				
				monthTxt.setTextColor(getResources().getColor(R.color.text_gray));
				monthLine.setBackgroundColor(getResources().getColor(R.color.light_gray));
				
				yearTxt.setTextColor(getResources().getColor(R.color.main_color));
				yearLine.setBackgroundColor(getResources().getColor(R.color.main_color));
				
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
			case R.id.week_ranking_frame:
				viewPager.setCurrentItem(0);
				break;
			case R.id.month_ranking_frame:
				viewPager.setCurrentItem(1);
				break;
			case R.id.year_ranking_frame:
				viewPager.setCurrentItem(2);
				break;
			default:
				break;
		}
	}
	
	@Override
	protected void onDestroy()
	{
		MainActivity.index = 2;
		super.onDestroy();
	}
}
