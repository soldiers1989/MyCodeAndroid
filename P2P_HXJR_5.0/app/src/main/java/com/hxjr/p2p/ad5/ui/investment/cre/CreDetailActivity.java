package com.hxjr.p2p.ad5.ui.investment.cre;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.dm.http.HttpCallBack;
import com.dm.http.HttpUtil;
import com.dm.utils.DMJsonObject;
import com.dm.utils.DMLog;
import com.dm.utils.DensityUtil;
import com.dm.utils.StringUtils;
import com.dm.widgets.MyScrollView;
import com.dm.widgets.MyScrollView.ScrollListener;
import com.dm.widgets.ScrollViewContainer;
import com.dm.widgets.ScrollViewContainer.OnPullUpScrollViewListener;
import com.hxjr.p2p.ad5.DMApplication;
import com.hxjr.p2p.ad5.R;
import com.hxjr.p2p.ad5.bean.CreDetailBase;
import com.hxjr.p2p.ad5.bean.UserInfo;
import com.hxjr.p2p.ad5.ui.BaseActivity;
import com.hxjr.p2p.ad5.ui.investment.bid.BidInfoFragment;
import com.hxjr.p2p.ad5.ui.investment.bid.InvestRecordFragment;
import com.hxjr.p2p.ad5.ui.investment.bid.ReplayPlanFragment;
import com.hxjr.p2p.ad5.ui.mine.user.LoginActivity;
import com.hxjr.p2p.ad5.ui.tg.UnRegisterTgActivity;
import com.hxjr.p2p.ad5.utils.DMConstant;
import com.hxjr.p2p.ad5.utils.ErrorUtil;
import com.hxjr.p2p.ad5.utils.FormatUtil;
import com.hxjr.p2p.ad5.utils.HttpParams;
import com.hxjr.p2p.ad5.utils.UIHelper;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 债权详情
 * @author  jiaohongyun
 * @date 2015年11月4日
 */
public class CreDetailActivity extends BaseActivity implements OnPullUpScrollViewListener, ScrollListener, OnClickListener
{
	private static final int REQUEST_CODE_LOGIN = 1; //startActivity 登录
	
	private ScrollViewContainer scrollview_container;
	
	private ScrollView top_myscroll;
	
	private MyScrollView myScroll;
	
	private TextView bottom_view;
	
	/**
	 * 页面上方背景图宽高比
	 */
	private static final double WIDTH_D_HEIGHT = 1.596452328159645d;
	
	/**
	 * 屏幕宽度
	 */
	private int screenWidht;
	
	/**
	 * 上方的背景图
	 */
	private ImageView invest_dynamic_iv;
	
	/**
	 * 标的信息按钮
	 */
	private View bidInfoBtn1;
	
	/**
	 * 还款计划按钮
	 */
	private View bidInfoBtn2;
	
	/**
	 * 投资记录按钮
	 */
	private View bidInfoBtn3;
	
	private TextView bidInfoBtnText1;
	
	private TextView bidInfoBtnText2;
	
	private TextView bidInfoBtnText3;
	
	private ImageView bidInfoBtnLine1;
	
	private ImageView bidInfoBtnLine2;
	
	private ImageView bidInfoBtnLine3;
	
	private TextView unlogin_tv;
	
	private TextView after_tv;
	
	private View pull_up_to_load_more_ll;
	
	private String creditorId;
	
	private String bidId;
	
	private String creStatus;
	
	private String bidFlag;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.creditor_detail);
		bidId = getIntent().getExtras().getString("bidId");
		// creId
		creditorId = getIntent().getExtras().getString("creId");
		bidFlag = getIntent().getExtras().getString("bidFlag");
		creStatus = getIntent().getExtras().getString("creStatus");
		initView();
		initData();
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		if (DMApplication.getInstance().islogined())
		{
			unlogin_tv.setVisibility(View.GONE);
			after_tv.setVisibility(View.GONE);
		}
		else
		{
			unlogin_tv.setVisibility(View.VISIBLE);
			after_tv.setVisibility(View.VISIBLE);
		}
		if (DMApplication.getInstance().islogined())
		{
			scrollview_container.setDisabled(false);
		}
		else
		{
			scrollview_container.setDisabled(true);
		}
	}
	
	private CreDetailBase base;
	
	private TextView bidYearRate;
	
	private TextView giftRate;
	
	private TextView bidTerms;
	
	private TextView bidAmount;
	
	private TextView bidStatus;
	
	private TextView bid_name;
	
	/**
	 * 还款方式
	 */
	private TextView bid_replay_mode;
	
	/**
	 * 保障方式
	 */
	private TextView bid_protect_mode;
	
	/**
	 * 发布时间
	 */
	private TextView bid_publish_time;
	
	/**
	 * 担保机构
	 */
	private TextView bid_dbjg_name;
	
	/**
	 * 待收本息
	 */
	private TextView bid_dsbx_amount;
	
	/**
	 * 剩余期数
	 */
	private TextView bid_syqs_time;
	
	@Override
	protected void initView()
	{
		super.initView();
		((TextView)findViewById(R.id.title_text)).setText(R.string.cre_page_title);
		scrollview_container = (ScrollViewContainer)findViewById(R.id.scrollview_container);
		scrollview_container.setOnPullUpScrollViewListener(this);
		
		top_myscroll = (ScrollView)findViewById(R.id.top_myscroll);
		top_myscroll.setVerticalScrollBarEnabled(false);
		myScroll = (MyScrollView)findViewById(R.id.myscroll);
		myScroll.setVerticalScrollBarEnabled(false);
		myScroll.setScrollListener(this);
		
		bottom_view = (TextView)findViewById(R.id.bottom_view);
		bottom_view.setOnClickListener(this);
		
		invest_dynamic_iv = (ImageView)findViewById(R.id.invest_dynamic_iv);
		screenWidht = DensityUtil.getScreenWidth(this);
		LayoutParams layoutParams = invest_dynamic_iv.getLayoutParams();
		layoutParams.width = screenWidht;
		layoutParams.height = (int)(screenWidht / WIDTH_D_HEIGHT);
		invest_dynamic_iv.setLayoutParams(layoutParams);
		
		bidInfoBtn1 = findViewById(R.id.bidInfoBtn1);
		bidInfoBtn2 = findViewById(R.id.bidInfoBtn2);
		bidInfoBtn3 = findViewById(R.id.bidInfoBtn3);
		bidInfoBtn1.setOnClickListener(this);
		bidInfoBtn2.setOnClickListener(this);
		bidInfoBtn3.setOnClickListener(this);
		bidInfoBtnText1 = (TextView)findViewById(R.id.bidInfoBtnText1);
		bidInfoBtnText2 = (TextView)findViewById(R.id.bidInfoBtnText2);
		bidInfoBtnText3 = (TextView)findViewById(R.id.bidInfoBtnText3);
		bidInfoBtnLine1 = (ImageView)findViewById(R.id.bidInfoBtnLine1);
		bidInfoBtnLine2 = (ImageView)findViewById(R.id.bidInfoBtnLine2);
		bidInfoBtnLine3 = (ImageView)findViewById(R.id.bidInfoBtnLine3);
		
		bidYearRate = (TextView)findViewById(R.id.bidYearRate);
		giftRate = (TextView)findViewById(R.id.giftRate);
		bidTerms = (TextView)findViewById(R.id.bidTerms);
		bidAmount = (TextView)findViewById(R.id.bidAmount);
		bidStatus = (TextView)findViewById(R.id.bidStatus);
		bid_name = (TextView)findViewById(R.id.bid_name);
		
		bid_dsbx_amount = (TextView)findViewById(R.id.bid_dsbx_amount);
		bid_syqs_time = (TextView)findViewById(R.id.bid_syqs_time);
		bid_replay_mode = (TextView)findViewById(R.id.bid_replay_mode);
		bid_protect_mode = (TextView)findViewById(R.id.bid_protect_mode);
		bid_publish_time = (TextView)findViewById(R.id.bid_publish_time);
		bid_dbjg_name = (TextView)findViewById(R.id.bid_dbjg_name);
		
		pull_up_to_load_more_ll = findViewById(R.id.pull_up_to_load_more_ll);
		unlogin_tv = (TextView)findViewById(R.id.unlogin_tv);
		after_tv = (TextView)findViewById(R.id.after_tv);
		unlogin_tv.setOnClickListener(this);
	}
	
	private void initData()
	{
		initCreditor();
	}
	
	private void initCreditor()
	{
		HttpParams httpParams = new HttpParams();
		httpParams.put("creditorId", creditorId);//creditorId
		httpParams.put("bidId", bidId);
		HttpUtil.getInstance().post(this, DMConstant.API_Url.CREDITOR_CREDITOR, httpParams, new HttpCallBack()
		{
			@Override
			public void onSuccess(JSONObject result)
			{
				try
				{
					String code = result.getString("code");
					if (code.equals(DMConstant.ResultCode.SUCCESS))
					{
						// 成功
						DMJsonObject data = new DMJsonObject(result.getString("data"));
						base = new CreDetailBase(data);
						//年化利率
						String yearRateS = base.getRate();
						SpannableString yearRateSB = new SpannableString(yearRateS);
						yearRateSB.setSpan(new RelativeSizeSpan(0.5f),
							yearRateS.length() - 1,
							yearRateS.length(),
							SpannableString.SPAN_INCLUSIVE_EXCLUSIVE);
						bidYearRate.setText(yearRateSB);
						giftRate.setText(base.getJlRate());
						//投资剩余期数
						String terms = "";
						SpannableString termsSB = null;
						int index = -1;
						if (base.getIsDay().equals("F"))
						{
							terms = base.getDays() + "个月";
							index = 2;
						}
						else if (base.getIsDay().equals("S"))
						{
							terms = base.getCycle() + "天";
							index = 1;
						}
						termsSB = new SpannableString(terms);
						termsSB.setSpan(new RelativeSizeSpan(0.7f),
							terms.length() - index,
							terms.length(),
							SpannableString.SPAN_INCLUSIVE_EXCLUSIVE);
						bidTerms.setText(termsSB);
						
						//转让金额
						String lendAmount = "";
						SpannableString lendAmountSB = null;
						lendAmount = FormatUtil.formatMoney(Double.valueOf(base.getSalePrice()));
						if (lendAmount.endsWith("万元") || lendAmount.endsWith("百万"))
						{
							index = 2;
						}
						else if (lendAmount.endsWith("元") || lendAmount.endsWith("亿"))
						{
							index = 1;
						}
						lendAmountSB = new SpannableString(lendAmount);
						lendAmountSB.setSpan(new RelativeSizeSpan(0.7f),
							lendAmount.length() - index,
							lendAmount.length(),
							SpannableString.SPAN_INCLUSIVE_EXCLUSIVE);
						bidAmount.setText(lendAmountSB);
						//标的状态
						bidStatus.setText(FormatUtil.convertBidStatus(base.getStatus()));
						//根据表的状态，设置购买按钮
						bottom_view.setVisibility(View.VISIBLE);
						if ("ZRZ".equals(creStatus))
						{
							bottom_view.setText("马上购买");
							bottom_view.setBackgroundColor(getResources().getColor(R.color.main_color));
							bottom_view.setEnabled(true);
						}
						else
						{
							bottom_view.setText("已结束");
							bottom_view.setBackgroundColor(getResources().getColor(R.color.back_grey));
							bottom_view.setEnabled(false);
						}
						//借款标题
						bid_name.setText(base.getBidTitle());
						@SuppressWarnings("deprecation")
						Drawable drawable = getResources().getDrawable(UIHelper.bidTyeImgs.get(bidFlag));
						drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
						bid_name.setCompoundDrawables(drawable, null, null, null);
						if (DMApplication.getInstance().islogined())
						{
							changeTab(0);
							Bundle bundle = new Bundle();
							bundle.putString("bidId", bidId);
							bundle.putString("isDanBao", base.getIsDanBao());
							switchFragment(BidInfoFragment.getInstant(bundle));
						}
						fillData(base.getStatus(), bidFlag);
					}
					else
					{
						ErrorUtil.showError(result);
					}
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
			}
		});
	}
	
	private void fillData(String status, String type)
	{
		//待收本息
		if (null == base.getDhjeAmount() || "null".equals(base.getDhjeAmount()))
		{
			bid_dsbx_amount.setVisibility(View.GONE);
		}
		else
		{
			bid_dsbx_amount.setText(getString(R.string.dsbx_name, FormatUtil.formatStr2(base.getDhjeAmount().replace(",", ""))));
		}
		//剩余期数
		bid_syqs_time.setText(getString(R.string.syqs_name, base.getDays()));
		//还款方式
		bid_replay_mode.setVisibility(View.VISIBLE);
		bid_replay_mode.setText(getString(R.string.replay_mode, base.getPaymentType()));
		//发布时间
		String publishTime = getString(R.string.publish_time, base.getPublishTime());
		publishTime = publishTime.substring(0, publishTime.indexOf(" "));
		//		SpannableString publishTimeSB = new SpannableString(publishTime);
		//		publishTimeSB.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.orange)),
		//			5,
		//			publishTime.length(),
		//			SpannableString.SPAN_INCLUSIVE_EXCLUSIVE);
		bid_publish_time.setVisibility(View.VISIBLE);
		bid_publish_time.setText(publishTime);
		if (null == type || "".equals(type) || "null".equals(type))
		{
			return;
		}
		if (type.contains("信") || type.contains("抵") || type.contains("实"))
		{
			//保障方式
			bid_protect_mode.setVisibility(View.GONE);
			//担保机构
			bid_dbjg_name.setVisibility(View.GONE);
		}
		if (type.contains("保"))
		{
			//保障方式
			bid_protect_mode.setVisibility(View.VISIBLE);
			bid_protect_mode.setText(getString(R.string.protect_mode, base.getGuaSch()));
			//担保机构
			bid_dbjg_name.setVisibility(View.VISIBLE);
			bid_dbjg_name.setText(getString(R.string.dbjg_name, base.getGuarantee()));
		}
	}
	
	@Override
	public void onRefresh()
	{
		//		if (basicInfoFragment == null)
		//		{
		//			setTabSelection(0);
		//		}
		DMLog.d("hyjiao", "onRefresh()");
	}
	
	@Override
	public void toUpRefresh()
	{
		DMLog.d("hyjiao", "toUpRefresh()");
	}
	
	@Override
	public void scrollChanged(int x, int y)
	{
		if (y > 0)
		{
			//			l1.setVisibility(View.VISIBLE);
		}
		else
		{
			//			l1.setVisibility(View.GONE);
		}
	}
	
	@Override
	public void onClick(View v)
	{
		if (checkClick(v.getId()))
		{
			switch (v.getId())
			{
				case R.id.bottom_view:
				{
					UserInfo userInfo = DMApplication.getInstance().getUserInfo();
					if (!DMApplication.getInstance().islogined())
					{
						//未登录
						DMApplication.toLoginValue = -1;
						Intent intent = new Intent(this, LoginActivity.class);
						startActivityForResult(intent, REQUEST_CODE_LOGIN);
					}
					else if (null != userInfo && userInfo.isTg() && StringUtils.isEmptyOrNull(userInfo.getUsrCustId()))
					{//如果是托管，并且还没有注册第三方
						startActivity(new Intent(this, UnRegisterTgActivity.class).putExtra("title", "购买债权"));
					}
					else
					{ //支付
						Intent intent = new Intent(this, BuyCreActivity.class);
						intent.putExtra("base", base);
						intent.putExtra("creditorId", creditorId);
						startActivity(intent);
					}
					break;
				}
				case R.id.bidInfoBtn1:
				{
					changeTab(0);
					Bundle bundle = new Bundle();
					bundle.putString("bidId", bidId);
					bundle.putString("isDanBao", base.getIsDanBao());
					switchFragment(BidInfoFragment.getInstant(bundle));
					break;
				}
				case R.id.bidInfoBtn2:
				{
					changeTab(1);
					Bundle bundle = new Bundle();
					bundle.putString("bidId", bidId);
					switchFragment(ReplayPlanFragment.getInstant(bundle));
					break;
				}
				case R.id.bidInfoBtn3:
				{
					changeTab(2);
					Bundle bundle = new Bundle();
					bundle.putString("bidId", bidId);
					switchFragment(InvestRecordFragment.getInstant(bundle));
					break;
				}
				case R.id.unlogin_tv:
				{
					//登录
					Intent intent = new Intent(this, LoginActivity.class);
					startActivityForResult(intent, REQUEST_CODE_LOGIN);
					break;
				}
				default:
					break;
			}
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent)
	{
		super.onActivityResult(requestCode, resultCode, intent);
		if (requestCode == REQUEST_CODE_LOGIN)
		{
			if (DMApplication.getInstance().islogined())
			{
				changeTab(0);
				Bundle bundle = new Bundle();
				bundle.putString("bidId", bidId);
				bundle.putString("isDanBao", base.getIsDanBao());
				switchFragment(BidInfoFragment.getInstant(bundle));
			}
		}
	}
	
	/**
	 * 修改TAB标签状态
	 * @param index
	 */
	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	private void changeTab(int index)
	{
		switch (index)
		{
			case 0:
			{
				bidInfoBtnText1.setTextColor(getResources().getColor(R.color.main_color));
				bidInfoBtnText2.setTextColor(getResources().getColor(R.color.text_black_8));
				bidInfoBtnText3.setTextColor(getResources().getColor(R.color.text_black_8));
				
				if (Build.VERSION.SDK_INT < 21)
				{
					bidInfoBtnLine1.setImageDrawable(getResources().getDrawable(R.drawable.btn_bottom_blue_line));
					bidInfoBtnLine2.setImageDrawable(getResources().getDrawable(R.drawable.btn_bottom_cc_line));
					bidInfoBtnLine3.setImageDrawable(getResources().getDrawable(R.drawable.btn_bottom_cc_line));
				}
				else
				{
					bidInfoBtnLine1.setImageDrawable(getResources().getDrawable(R.drawable.btn_bottom_blue_line));
					bidInfoBtnLine2.setImageDrawable(getResources().getDrawable(R.drawable.btn_bottom_cc_line));
					bidInfoBtnLine3.setImageDrawable(getResources().getDrawable(R.drawable.btn_bottom_cc_line));
				}
				break;
			}
			case 1:
			{
				bidInfoBtnText2.setTextColor(getResources().getColor(R.color.main_color));
				bidInfoBtnText1.setTextColor(getResources().getColor(R.color.text_black_8));
				bidInfoBtnText3.setTextColor(getResources().getColor(R.color.text_black_8));
				if (Build.VERSION.SDK_INT < 21)
				{
					bidInfoBtnLine2.setImageDrawable(getResources().getDrawable(R.drawable.btn_bottom_blue_line));
					bidInfoBtnLine1.setImageDrawable(getResources().getDrawable(R.drawable.btn_bottom_cc_line));
					bidInfoBtnLine3.setImageDrawable(getResources().getDrawable(R.drawable.btn_bottom_cc_line));
				}
				else
				{
					bidInfoBtnLine2.setImageDrawable(getResources().getDrawable(R.drawable.btn_bottom_blue_line));
					bidInfoBtnLine1.setImageDrawable(getResources().getDrawable(R.drawable.btn_bottom_cc_line));
					bidInfoBtnLine3.setImageDrawable(getResources().getDrawable(R.drawable.btn_bottom_cc_line));
				}
				break;
			}
			case 2:
			{
				bidInfoBtnText3.setTextColor(getResources().getColor(R.color.main_color));
				bidInfoBtnText2.setTextColor(getResources().getColor(R.color.text_black_8));
				bidInfoBtnText1.setTextColor(getResources().getColor(R.color.text_black_8));
				if (Build.VERSION.SDK_INT < 21)
				{
					bidInfoBtnLine3.setImageDrawable(getResources().getDrawable(R.drawable.btn_bottom_blue_line));
					bidInfoBtnLine2.setImageDrawable(getResources().getDrawable(R.drawable.btn_bottom_cc_line));
					bidInfoBtnLine1.setImageDrawable(getResources().getDrawable(R.drawable.btn_bottom_cc_line));
				}
				else
				{
					bidInfoBtnLine3.setImageDrawable(getResources().getDrawable(R.drawable.btn_bottom_blue_line));
					bidInfoBtnLine2.setImageDrawable(getResources().getDrawable(R.drawable.btn_bottom_cc_line));
					bidInfoBtnLine1.setImageDrawable(getResources().getDrawable(R.drawable.btn_bottom_cc_line));
				}
				break;
			}
			default:
				break;
		}
	}
	
	/**
	 * 切换Fragment
	 * @param fragment
	 */
	private void switchFragment(Fragment fragment)
	{
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.replace(R.id.bidInfoContent, fragment);
		ft.commit();
	}
	
	@Override
	protected void onDestroy()
	{
		BidInfoFragment.setNull();
		InvestRecordFragment.setNull();
		ReplayPlanFragment.setNull();
		super.onDestroy();
	}
}
