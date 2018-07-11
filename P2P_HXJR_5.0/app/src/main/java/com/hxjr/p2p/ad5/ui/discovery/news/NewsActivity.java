package com.hxjr.p2p.ad5.ui.discovery.news;

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
 * 资讯
 * @author  huangkaibo
 * @date 2015年11月6日
 */
public class NewsActivity extends BaseActivity implements OnClickListener
{
	
	private ViewPager newsViewPager;
	
	private FrameLayout mediaReportBtn; //媒体报道
	
	private FrameLayout industryNewBtn; //行业资讯
	
	private FrameLayout financialBtn; //网站公告
	
	private TextView mediaReport;
	
	private TextView industryNews;
	
	private TextView financialSchool;
	
	private View mediaReportLine;
	
	private View industryNewsLine;
	
	private View financialSchoolLine;
	
	private List<Fragment> fragments = new ArrayList<Fragment>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.news_activity);
		initView();
	}
	
	@Override
	protected void initView()
	{
		super.initView();
		((TextView)findViewById(R.id.title_text)).setText(R.string.news);
		
		newsViewPager = (ViewPager)findViewById(R.id.news_view_pager);
		
		mediaReportBtn = (FrameLayout)findViewById(R.id.media_report_frame);
		industryNewBtn = (FrameLayout)findViewById(R.id.industry_new_frame);
		financialBtn = (FrameLayout)findViewById(R.id.financial_frame);
		
		mediaReport = (TextView)findViewById(R.id.media_report_tv);
		industryNews = (TextView)findViewById(R.id.industry_news_tv);
		financialSchool = (TextView)findViewById(R.id.financial_school_tv);
		
		mediaReportLine = findViewById(R.id.media_report_line);
		industryNewsLine = findViewById(R.id.industry_news_line);
		financialSchoolLine = findViewById(R.id.financial_school_line);
		
		mediaReportBtn.setOnClickListener(this);
		industryNewBtn.setOnClickListener(this);
		financialBtn.setOnClickListener(this);
		
		initViewPager();
	}
	
	private void initViewPager()
	{
		fragments.add(new MediaReportFragment());
		fragments.add(new IndustryNewsFragment());
		fragments.add(new WebsiteNoticeFragment());
		
		CommonFragmentAdapter adapter =
			new CommonFragmentAdapter(fragments, this.getSupportFragmentManager(), newsViewPager);
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
				mediaReport.setTextColor(getResources().getColor(R.color.main_color));
				mediaReportLine.setBackgroundColor(getResources().getColor(R.color.main_color));
				
				industryNews.setTextColor(getResources().getColor(R.color.text_gray));
				industryNewsLine.setBackgroundColor(getResources().getColor(R.color.light_gray));
				
				financialSchool.setTextColor(getResources().getColor(R.color.text_gray));
				financialSchoolLine.setBackgroundColor(getResources().getColor(R.color.light_gray));
				
				break;
			case 1:
				mediaReport.setTextColor(getResources().getColor(R.color.text_gray));
				mediaReportLine.setBackgroundColor(getResources().getColor(R.color.light_gray));
				
				industryNews.setTextColor(getResources().getColor(R.color.main_color));
				industryNewsLine.setBackgroundColor(getResources().getColor(R.color.main_color));
				
				financialSchool.setTextColor(getResources().getColor(R.color.text_gray));
				financialSchoolLine.setBackgroundColor(getResources().getColor(R.color.light_gray));
				break;
			case 2:
				mediaReport.setTextColor(getResources().getColor(R.color.text_gray));
				mediaReportLine.setBackgroundColor(getResources().getColor(R.color.light_gray));
				
				industryNews.setTextColor(getResources().getColor(R.color.text_gray));
				industryNewsLine.setBackgroundColor(getResources().getColor(R.color.light_gray));
				
				financialSchool.setTextColor(getResources().getColor(R.color.main_color));
				financialSchoolLine.setBackgroundColor(getResources().getColor(R.color.main_color));
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
			case R.id.media_report_frame: //媒体报道
				newsViewPager.setCurrentItem(0);
				break;
			case R.id.industry_new_frame: //行业资讯
				newsViewPager.setCurrentItem(1);
				break;
			case R.id.financial_frame: //网站公告
				newsViewPager.setCurrentItem(2);
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
