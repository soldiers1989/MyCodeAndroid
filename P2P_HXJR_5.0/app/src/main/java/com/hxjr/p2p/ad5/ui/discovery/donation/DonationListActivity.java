package com.hxjr.p2p.ad5.ui.discovery.donation;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hxjr.p2p.ad5.bean.GyInfo;
import com.hxjr.p2p.ad5.ui.BaseActivity;
import com.hxjr.p2p.ad5.ui.MainActivity;
import com.hxjr.p2p.ad5.utils.CircleViewsHelper;
import com.hxjr.p2p.ad5.utils.DMConstant;
import com.hxjr.p2p.ad5.utils.ErrorUtil;
import com.hxjr.p2p.ad5.utils.FormatUtil;
import com.hxjr.p2p.ad5.utils.HttpParams;
import com.hxjr.p2p.ad5.utils.NetConnectErrorManager;
import com.hxjr.p2p.ad5.utils.UIHelper;
import com.hxjr.p2p.ad5.utils.NetConnectErrorManager.NetConnetCallBack;
import com.hxjr.p2p.ad5.R;
import com.dm.http.DMException;
import com.dm.http.HttpCallBack;
import com.dm.http.HttpUtil;
import com.dm.utils.DMJsonObject;
import com.dm.utils.DMLog;
import com.dm.widgets.DMListView;
import com.dm.widgets.DMListView.OnMoreListener;
import com.dm.widgets.DMSwipeRefreshLayout;
import com.dm.widgets.bannerviewpager.AdvertisementInfo;
import com.dm.widgets.bannerviewpager.ViewPagerScroller;

/**
 * 公益捐赠
 * @author  huangkaibo
 * @date 2015年11月13日
 */
public class DonationListActivity extends BaseActivity implements OnMoreListener, OnRefreshListener
{
	private DMListView mListView;
	
	private DMSwipeRefreshLayout mSwipeLayout;
	
	private DonationAdapter adapter;
	
	private FrameLayout adv_banner_fl;
	
	private ViewPager bannerViewPager;
	
	private LinearLayout points_ll;
	
	private View homeHeaderView;
	
	private Context context;
	
	private TextView amount_tv;
	
	private TextView num_tv;
	
	private CircleViewsHelper circleViewsHelper;
	
	private LinearLayout count_layout;
	
	private int pageNumber = 1;
	
	private List<AdvertisementInfo> infos = new ArrayList<AdvertisementInfo>();
	
	private List<View> points = new ArrayList<View>(5);
	
	//	private List<GyInfo> mList = new ArrayList<GyInfo>(DMConstant.DigitalConstant.TEN);
	
	private NetConnectErrorManager netConnectErrorManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.donation_list);
		this.context = this;
		initView();
		initListener();
	}
	
	@Override
	protected void initView()
	{
		super.initView();
		((TextView)findViewById(R.id.title_text)).setText(R.string.discovery_commonweal_bid);
		
		mSwipeLayout = (DMSwipeRefreshLayout)findViewById(R.id.dmSwipeRefreshLayout);
		mSwipeLayout.setOnRefreshListener(this);
		mListView = (DMListView)findViewById(R.id.donationListView);
		mListView.setOnMoreListener(this);
		homeHeaderView = LayoutInflater.from(context).inflate(R.layout.home_header_item, mListView, false);
		mListView.addHeaderView(homeHeaderView);
		count_layout = (LinearLayout)homeHeaderView.findViewById(R.id.count_layout);
		adv_banner_fl = (FrameLayout)homeHeaderView.findViewById(R.id.adv_banner_fl);
		bannerViewPager = (ViewPager)homeHeaderView.findViewById(R.id.donation_banner);
		points_ll = (LinearLayout)homeHeaderView.findViewById(R.id.ponint_ll);
		amount_tv = (TextView)homeHeaderView.findViewById(R.id.amount_tv);
		num_tv = (TextView)homeHeaderView.findViewById(R.id.num_tv);
		netConnectErrorManager =
			new NetConnectErrorManager(this.getWindow().getDecorView(), mSwipeLayout, new NetConnetCallBack()
			{
				@Override
				public void onNetErrorRefrensh()
				{
					onRefresh();
				}
			});
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		postData();
	}
	
	/**
	 * 初始化数据
	 */
	private void postData()
	{
		getGyLoanInfo();
		if (adapter == null)
		{
			adapter = new DonationAdapter(this);
		}
		mListView.setAdapter(adapter);
		pageNumber = 1;
		getGyLoanInfoList(pageNumber);
	}
	
	private void initListener()
	{
		mListView.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				GyInfo gyInfo = (GyInfo)parent.getAdapter().getItem(position);
				if (gyInfo != null)
				{
					Intent intent = new Intent(DonationListActivity.this, DonationDetailActivity.class);
					intent.putExtra("bidId", gyInfo.getLoadId() + "");
					startActivity(intent);
				}
			}
		});
		
		bannerViewPager.addOnPageChangeListener(new OnPageChangeListener()
		{
			@Override
			public void onPageSelected(int arg0)
			{
				if (infos.size() > 1 && null != circleViewsHelper)
				{
					circleViewsHelper.stopTimer();
					circleViewsHelper.startTimer();
					for (int i = 0; i < infos.size(); i++)
					{
						try
						{
							if (bannerViewPager.getCurrentItem() % infos.size() == i)
							{
								//将当前图片对应的圆点设置为白色
								points_ll.getChildAt(i).setBackgroundResource(R.drawable.home_banner_point_on);
							}
							else
							{
								//将其他图片对应的圆点设置为相应的颜色
								points_ll.getChildAt(i).setBackgroundResource(R.drawable.home_banner_point_off);
							}
						}
						catch (Exception e)
						{
							DMLog.d(e.getMessage());
						}
					}
				}
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2)
			{
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0)
			{
			}
		});
	}
	
	/**
	 * 获取公益标信息(广告、统计信息)
	 */
	private void getGyLoanInfo()
	{
		HttpUtil.getInstance().post(this, DMConstant.API_Url.GY_LOAN_COUNT, new HttpCallBack()
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
						Double amount = data.getDouble("donationsAmount");
						count_layout.setVisibility(View.VISIBLE);
						String amountStr = FormatUtil.formatMoney(amount);
						UIHelper.formatMoneyTextSize(amount_tv, amountStr, 0.7f);
						String num = data.getInt("donationsNum") + "笔";
						SpannableString numSB = new SpannableString(num);
						numSB.setSpan(new RelativeSizeSpan(0.7f),
							num.length() - 1,
							num.length(),
							SpannableString.SPAN_INCLUSIVE_EXCLUSIVE);
						num_tv.setText(numSB);
						JSONArray dataList = data.getJSONArray("advs");
						infos.clear();
						if (dataList != null && dataList.length() > 0)
						{
							for (int i = 0; i < dataList.length(); i++)
							{
								JSONObject obj = dataList.getJSONObject(i);
								AdvertisementInfo info = new AdvertisementInfo();
								info.setTitle(obj.getString("advTitle"));
								info.setImgUrl(obj.getString("advImg"));
								if (obj.has("advUrl"))
								{
									info.setLinkUrl(obj.getString("advUrl"));
								}
								infos.add(info);
							}
							initBanner();
						}
						else
						{
							adv_banner_fl.setVisibility(View.GONE);
						}
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
				//				super.onFailure(t, context);
			}
			
		});
	}
	
	/**
	 * 获取公益标列表
	 */
	private void getGyLoanInfoList(final int postPage)
	{
		HttpParams params = new HttpParams();
		params.put("pageIndex", postPage);
		params.put("pageSize", DMConstant.DigitalConstant.PAGE_SIZE);
		HttpUtil.getInstance().post(this, DMConstant.API_Url.GY_LOAD_LIST, params, new HttpCallBack()
		{
			@Override
			public void onSuccess(JSONObject result)
			{
				if (netConnectErrorManager != null)
				{
					netConnectErrorManager.onNetGood();
				}
				try
				{
					String code = result.getString("code");
					if (DMConstant.ResultCode.SUCCESS.equals(code))
					{
						JSONArray dataList = result.getJSONArray("data");
						//						mList.clear();
						List<GyInfo> mList = new ArrayList<GyInfo>();
						for (int i = 0; i < dataList.length(); i++)
						{
							DMJsonObject data = new DMJsonObject(dataList.getString(i));
							GyInfo gyInfo = new GyInfo(data);
							mList.add(gyInfo);
						}
						
						if (postPage == 1)
						{//第一次或者刷新都会进来
							pageNumber = 1;
							adapter.clearList();
						}
						adapter.addAll(mList);
						mListView.stopLoading();
						
						if (dataList.length() == 0 || dataList.length() < DMConstant.DigitalConstant.PAGE_SIZE)
						{
							mListView.hasMoreDate(false);
						}
						else
						{
							mListView.hasMoreDate(true);
							pageNumber++;
						}
					}
					else
					{
						ErrorUtil.showError(result);
						mListView.stopLoading();
						mListView.hasMoreDate(false);
					}
					mSwipeLayout.setRefreshing(false);
				}
				catch (JSONException e)
				{
					e.printStackTrace();
				}
			}
			
			@Override
			public void onFailure(Throwable t, Context context)
			{
				super.onFailure(t, context);
				mListView.stopLoading();
				mSwipeLayout.setRefreshing(false);
			}
			
			@Override
			public void onConnectFailure(DMException dmE, Context context)
			{
				// TODO Auto-generated method stub
				super.onConnectFailure(dmE, context);
				if (netConnectErrorManager != null)
				{
					netConnectErrorManager.onNetError();
				}
			}
		});
	}
	
	@Override
	public void onRefresh()
	{
		getGyLoanInfo();
		getGyLoanInfoList(1);
		mSwipeLayout.setRefreshing(false);
	}
	
	@Override
	public void onMore()
	{
		getGyLoanInfoList(pageNumber);
	}
	
	/**
	 * 初始化banner
	 */
	private void initBanner()
	{
		adv_banner_fl.setVisibility(View.VISIBLE);
		bannerViewPager.setAdapter(new DonationBannerAdapter(infos, this));
		ViewPagerScroller scroller = new ViewPagerScroller(this);
		scroller.setScrollDuration(1000);
		scroller.initViewPagerScroll(bannerViewPager); //这个是设置切换过渡时间为1000毫秒
		initPointViews();
	}
	
	private void initPointViews()
	{
		if (infos.size() > 1)
		{
			LinearLayout.LayoutParams pointLayoutParams = new LinearLayout.LayoutParams(15, 15);
			pointLayoutParams.leftMargin = 10;
			points.clear();
			points_ll.removeAllViews();
			for (int i = 0; i < infos.size(); i++)
			{
				View pointView = new View(this);
				if (i == 0)
				{
					pointView.setBackgroundResource(R.drawable.home_banner_point_on);
				}
				else
				{
					pointView.setBackgroundResource(R.drawable.home_banner_point_off);
				}
				pointView.setLayoutParams(pointLayoutParams);
				pointView.setFocusable(true);
				points.add(pointView);
				points_ll.addView(pointView);
			}
			circleViewsHelper = new CircleViewsHelper(bannerViewPager);
			circleViewsHelper.startTimer();
		}
	}
	
	@Override
	protected void onStop()
	{
		super.onStop();
		if (null != circleViewsHelper)
		{
			circleViewsHelper.stopTimer();
			circleViewsHelper = null;
		}
	}
	
	@Override
	protected void onDestroy()
	{
		MainActivity.index = 2;
		super.onDestroy();
	}
	
}
