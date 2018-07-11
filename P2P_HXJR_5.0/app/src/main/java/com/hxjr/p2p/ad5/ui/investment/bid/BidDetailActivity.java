package com.hxjr.p2p.ad5.ui.investment.bid;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.dm.widgets.utils.AlertDialogUtil;
import com.dm.widgets.utils.AlertDialogUtil.AlertListener;
import com.hxjr.p2p.ad5.DMApplication;
import com.hxjr.p2p.ad5.R;
import com.hxjr.p2p.ad5.bean.BidDetailBase;
import com.hxjr.p2p.ad5.bean.UserInfo;
import com.hxjr.p2p.ad5.service.ApiUtil;
import com.hxjr.p2p.ad5.ui.BaseActivity;
import com.hxjr.p2p.ad5.ui.discovery.moneymanage.MoneyManageActivity;
import com.hxjr.p2p.ad5.ui.mine.bank.BankCardManageActivity;
import com.hxjr.p2p.ad5.ui.mine.setting.SecurityPhotoActivity;
import com.hxjr.p2p.ad5.ui.mine.user.LoginActivity;
import com.hxjr.p2p.ad5.utils.DMConstant;
import com.hxjr.p2p.ad5.utils.ErrorUtil;
import com.hxjr.p2p.ad5.utils.FormatUtil;
import com.hxjr.p2p.ad5.utils.HttpParams;
import com.hxjr.p2p.ad5.utils.TimeUtils;
import com.hxjr.p2p.ad5.utils.UIHelper;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 投资详情
 * @author  jiaohongyun
 * @date 2015年11月2日
 */
public class BidDetailActivity extends BaseActivity implements OnPullUpScrollViewListener, ScrollListener, OnClickListener
{
	private ScrollViewContainer scrollview_container;
	
	private ScrollView top_myscroll;
	
	private MyScrollView myScroll;

	private LinearLayout bottom_view;

	private TextView bottom_lc;

	private TextView bottom_tz;

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
	 * 相关文件
	 */
	private View bidInfoBtn2;
	
	/**
	 * 还款计划按钮
	 */
	private View bidInfoBtn3;
	
	/**
	 * 投资记录按钮
	 */
	private View bidInfoBtn4;
	
	private TextView bidInfoBtnText1;
	
	private TextView bidInfoBtnText2;
	
	private TextView bidInfoBtnText3;
	
	private TextView bidInfoBtnText4;
	
	private ImageView bidInfoBtnLine1;
	
	private ImageView bidInfoBtnLine2;
	
	private ImageView bidInfoBtnLine3;
	
	private ImageView bidInfoBtnLine4;
	
	private TextView unlogin_tv;
	
	private TextView after_tv;
	
	private BidDetailBase base;
	
	private TextView bidYearRate;
	
	private TextView giftRate;
	
	private TextView bidTerms;
	
	private TextView bidAmount;
	
	private TextView bidStatus;
	
	private TextView bid_name;
	
	private TextView bidHavenAmount;
	
	private TextView bid_replay_mode;
	
	private TextView bid_protect_mode;
	
	private TextView bid_publish_time;
	
	private TextView bid_remain_time;
	
	private TextView bid_dbjg_name;

	private TextView bid_qxir;
	
	private TextView tvw_jlb_or_xsb;
	
	/**
	 * 标的ID
	 */
	private String bidId;
	
	private MyCountDownTimer timer;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bid_detail);
		bidId = getIntent().getExtras().getString("bidId");
		initView();
		getBaseBidInfo();
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		getBaseBidInfo();
		if (DMApplication.getInstance().islogined())
		{
			unlogin_tv.setVisibility(View.GONE);
			after_tv.setVisibility(View.GONE);
			ApiUtil.getUserInfo(this);
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
	
	@Override
	protected void initView()
	{
		super.initView();
		((TextView)findViewById(R.id.title_text)).setText(R.string.investment_detail);
		scrollview_container = (ScrollViewContainer)findViewById(R.id.scrollview_container);
		scrollview_container.setOnPullUpScrollViewListener(this);
		
		top_myscroll = (ScrollView)findViewById(R.id.top_myscroll);
		top_myscroll.setVerticalScrollBarEnabled(false);
		myScroll = (MyScrollView)findViewById(R.id.myscroll);
		myScroll.setVerticalScrollBarEnabled(false);
		myScroll.setScrollListener(this);

		bottom_view = (LinearLayout) findViewById(R.id.bottom_view);
		bottom_lc = (TextView) findViewById(R.id.bottom_lc);
		bottom_tz = (TextView) findViewById(R.id.bottom_tz);
		// bottom_view.setOnClickListener(this);
		bottom_lc.setOnClickListener(this);
		bottom_tz.setOnClickListener(this);

		invest_dynamic_iv = (ImageView) findViewById(R.id.invest_dynamic_iv);
		screenWidht = DensityUtil.getScreenWidth(this);
		LayoutParams layoutParams = invest_dynamic_iv.getLayoutParams();
		layoutParams.width = screenWidht;
		layoutParams.height = (int)(screenWidht / WIDTH_D_HEIGHT);
		invest_dynamic_iv.setLayoutParams(layoutParams);
		
		bidInfoBtn1 = findViewById(R.id.bidInfoBtn1);
		bidInfoBtn2 = findViewById(R.id.bidInfoBtn2);
		bidInfoBtn3 = findViewById(R.id.bidInfoBtn3);
		bidInfoBtn4 = findViewById(R.id.bidInfoBtn4);
		bidInfoBtn1.setOnClickListener(this);
		bidInfoBtn2.setOnClickListener(this);
		bidInfoBtn3.setOnClickListener(this);
		bidInfoBtn4.setOnClickListener(this);
		bidInfoBtnText1 = (TextView)findViewById(R.id.bidInfoBtnText1);
		bidInfoBtnText2 = (TextView)findViewById(R.id.bidInfoBtnText2);
		bidInfoBtnText3 = (TextView)findViewById(R.id.bidInfoBtnText3);
		bidInfoBtnText4 = (TextView)findViewById(R.id.bidInfoBtnText4);
		bidInfoBtnLine1 = (ImageView)findViewById(R.id.bidInfoBtnLine1);
		bidInfoBtnLine2 = (ImageView)findViewById(R.id.bidInfoBtnLine2);
		bidInfoBtnLine3 = (ImageView)findViewById(R.id.bidInfoBtnLine3);
		bidInfoBtnLine4 = (ImageView)findViewById(R.id.bidInfoBtnLine4);
		
		bidYearRate = (TextView)findViewById(R.id.bidYearRate);
		giftRate = (TextView)findViewById(R.id.giftRate);
		bidTerms = (TextView)findViewById(R.id.bidTerms);
		bidAmount = (TextView)findViewById(R.id.bidAmount);
		bidStatus = (TextView)findViewById(R.id.bidStatus);
		bid_name = (TextView)findViewById(R.id.bid_name);
		bidHavenAmount = (TextView)findViewById(R.id.bidHavenAmount);
		bid_replay_mode = (TextView)findViewById(R.id.bid_replay_mode);
		bid_protect_mode = (TextView)findViewById(R.id.bid_protect_mode);
		bid_publish_time = (TextView)findViewById(R.id.bid_publish_time);
		bid_remain_time = (TextView)findViewById(R.id.bid_remain_time);
		bid_dbjg_name = (TextView)findViewById(R.id.bid_dbjg_name);
		bid_qxir=(TextView)findViewById(R.id.bid_qxir);
		tvw_jlb_or_xsb = (TextView)findViewById(R.id.tvw_jlb_or_xsb);
		
		unlogin_tv = (TextView)findViewById(R.id.unlogin_tv);
		after_tv = (TextView)findViewById(R.id.after_tv);
		unlogin_tv.setOnClickListener(this);
	}

	/**
	 * 获取标的基本信息
	 */
	private void getBaseBidInfo()
	{
		HttpParams params = new HttpParams();
		params.put("bidId", bidId);
		HttpUtil.getInstance().post(this, DMConstant.API_Url.BID_BID, params, new HttpCallBack()
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
						DMLog.e(data.toString());
						base = new BidDetailBase(data);
						//年化利率
						String yearRateS = base.getRate();
						SpannableString yearRateSB = new SpannableString(yearRateS);
						yearRateSB.setSpan(new RelativeSizeSpan(0.5f),
							yearRateS.length() - 1,
							yearRateS.length(),
							SpannableString.SPAN_INCLUSIVE_EXCLUSIVE);
						bidYearRate.setText(yearRateSB);
						giftRate.setText(base.getJlRate());
						//投资期限
						String terms = "";
						SpannableString termsSB = null;
						if (base.getIsDay().equals("F"))
						{
							terms = base.getCycle() + "个月";
							termsSB = new SpannableString(terms);
							termsSB.setSpan(new RelativeSizeSpan(0.5f),
								terms.length() - 2,
								terms.length(),
								SpannableString.SPAN_INCLUSIVE_EXCLUSIVE);
						}
						else if (base.getIsDay().equals("S"))
						{
							terms = base.getCycle() + "天";
							termsSB = new SpannableString(terms);
							termsSB.setSpan(new RelativeSizeSpan(0.5f),
								terms.length() - 1,
								terms.length(),
								SpannableString.SPAN_INCLUSIVE_EXCLUSIVE);
						}
						bidTerms.setText(termsSB);
						//借款金额
						String lendAmount = "";
						SpannableString lendAmountSB = null;
						//默认显示借款金额
						lendAmount = FormatUtil.formatMoney(Double.valueOf(base.getAmount()));
						if (lendAmount.endsWith("万元") || lendAmount.endsWith("百万"))
						{
							lendAmountSB = new SpannableString(lendAmount);
							lendAmountSB.setSpan(new RelativeSizeSpan(0.5f),
								lendAmount.length() - 2,
								lendAmount.length(),
								SpannableString.SPAN_INCLUSIVE_EXCLUSIVE);
						}
						else if (lendAmount.endsWith("元") || lendAmount.endsWith("亿"))
						{
							lendAmountSB = new SpannableString(lendAmount);
							lendAmountSB.setSpan(new RelativeSizeSpan(0.5f),
								lendAmount.length() - 1,
								lendAmount.length(),
								SpannableString.SPAN_INCLUSIVE_EXCLUSIVE);
						}
						bidAmount.setText(lendAmountSB);
						//标的状态
						bidStatus.setText(FormatUtil.convertBidStatus(base.getStatus()));
						//借款标题
						bid_name.setText(base.getBidTitle());
						if (null != base.getFlag() && !"".equals(base.getFlag()) && !"null".equals(base.getFlag()))
						{
							@SuppressWarnings("deprecation")
							Drawable drawable =
								BidDetailActivity.this.getResources().getDrawable(UIHelper.bidTyeImgs.get(base.getFlag()));
							drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
							bid_name.setCompoundDrawables(drawable, null, null, null);
						}
						if (base.getIsJlb())
						{
							tvw_jlb_or_xsb.setText("奖励");
							tvw_jlb_or_xsb.setTextColor(getResources().getColor(R.color.orange));
						}
						else if (base.getIsXsb())
						{
							tvw_jlb_or_xsb.setText("新手");
							tvw_jlb_or_xsb.setTextColor(getResources().getColor(R.color.main_color));
						}
						else
						{
							tvw_jlb_or_xsb.setVisibility(View.GONE);
						}
						
						//已投金额
						String alrAmount = base.getAlrAmount() + "元";
						SpannableString alrAmountSB = new SpannableString(alrAmount);
						alrAmountSB.setSpan(new RelativeSizeSpan(0.5f),
							alrAmount.length() - 1,
							alrAmount.length(),
							SpannableString.SPAN_INCLUSIVE_EXCLUSIVE);
						bidHavenAmount.setText(alrAmountSB);
						changeTab(0);
						Bundle bundle = new Bundle();
						bundle.putString("bidId", bidId);
						bundle.putString("isDanBao", base.getIsDanBao());
						switchFragment(BidInfoFragment.getInstant(bundle));
						if ("TBZ".equals(base.getStatus()))
						{
							//投标中
							bottom_view.setVisibility(View.VISIBLE);
						}
						else
						{
							//非投标中
							bottom_view.setVisibility(View.GONE);
						}
						fillData(base.getStatus(), base.getFlag(),base.isXgwj());
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
		});
	}
	
	private void fillData(String status, String type,boolean isXgwj)
	{
		DMLog.e(base.toString());
		//起息日
		bid_qxir.setVisibility(View.VISIBLE);
		if (base.getJxType()==0){
		bid_qxir.setText("起息日期：标的放款后当日计息");
	}else {
		bid_qxir.setText("起息日期：标的放款后次日计息");
	}
		//还款方式
		bid_replay_mode.setVisibility(View.VISIBLE);
		bid_replay_mode.setText(getString(R.string.replay_mode, base.getPaymentType()));
		//发布时间
		String publishTime = getString(R.string.publish_time, base.getPublishTime());
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
			if ("TBZ".equals(status))
			{
				//投标中
				//剩余时间
				bid_remain_time.setVisibility(View.VISIBLE);
				bid_remain_time.setText(getString(R.string.remain_time, getRemainTime(base.getEndTime())));
				if (!getRemainTime(base.getEndTime()).contains("天"))
				{
					if (timer == null)
					{
						timer = new MyCountDownTimer(TimeUtils.string2LongTime2(base.getEndTime()) - System.currentTimeMillis()
							+ DMApplication.getInstance().diffTime, 1000l);
					}
					timer.start();
				}
			}
			else
			{
				//非投标中
				//剩余时间
				bid_remain_time.setVisibility(View.GONE);
			}
		}
		if (type.contains("保"))
		{
			//保障方式
			bid_protect_mode.setVisibility(View.VISIBLE);
			bid_protect_mode.setText(getString(R.string.protect_mode, base.getGuaSch()));
			//担保机构
			bid_dbjg_name.setVisibility(View.VISIBLE);
			bid_dbjg_name.setText(getString(R.string.dbjg_name, base.getDbjg()));
			if ("TBZ".equals(status))
			{
				//投标中
				//剩余时间
				bid_remain_time.setVisibility(View.VISIBLE);
				bid_remain_time.setText(getString(R.string.remain_time, getRemainTime(base.getEndTime())));
				if (!getRemainTime(base.getEndTime()).contains("天"))
				{
					if (timer == null)
					{
						timer = new MyCountDownTimer(TimeUtils.string2LongTime2(base.getEndTime()) - System.currentTimeMillis()
							+ DMApplication.getInstance().diffTime, 1000l);
					}
					timer.start();
				}
			}
			else
			{
				//非投标中
				//剩余时间
				bid_remain_time.setVisibility(View.GONE);
			}
		}
		
		if ("YFB".equals(status) || "TBZ".equals(status) || "DFK".equals(status))
		{
			bidInfoBtn3.setVisibility(View.GONE);
		}
		else
		{
			bidInfoBtn3.setVisibility(View.VISIBLE);
		}
		bidInfoBtn2.setVisibility(isXgwj ? View.VISIBLE : View.GONE);
	}
	
	private class MyCountDownTimer extends CountDownTimer
	{
		public MyCountDownTimer(long millisInFuture, long countDownInterval)
		{
			super(millisInFuture, countDownInterval);
		}
		
		@Override
		public void onTick(long millisUntilFinished)
		{
			if (bid_remain_time != null)
			{
				bid_remain_time.setText(getString(R.string.remain_time, getRemainTime(base.getEndTime())));
			}
		}
		
		@Override
		public void onFinish()
		{
			bid_remain_time.setVisibility(View.GONE);
			// bottom_view.setVisibility(View.GONE);
			this.cancel();
		}
	}
	
	private String getRemainTime(String endTime)
	{
		String rTime = TimeUtils.formatDuring(
			TimeUtils.string2LongTime2(endTime) - System.currentTimeMillis() + DMApplication.getInstance().diffTime);
		if (rTime.contains("天"))
		{
			return rTime.substring(0, rTime.indexOf("分") + 1);
		}
		return rTime;
	}
	
	@Override
	public void onRefresh()
	{
	
	}
	
	@Override
	public void toUpRefresh()
	{
	
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
	public void onClick(View v) {
		if (checkClick(v.getId())) {
			switch (v.getId()) {
			case R.id.bottom_tz: {
				UserInfo userInfo = DMApplication.getInstance().getUserInfo();
				Intent intent;
				if (!DMApplication.getInstance().islogined()) {
					// 未登录
					DMApplication.toLoginValue = -1;
					intent = new Intent(this, LoginActivity.class);
					startActivity(intent);
				} else if (null != userInfo && userInfo.isTg()
					&& StringUtils.isEmptyOrNull(userInfo.getUsrCustId())) {// 如果是托管，并且还没有注册第三方
					intent = new Intent(this, BankCardManageActivity.class);
					startActivity(intent);
				} else { // 支付
					if (null != userInfo && userInfo.getIdcardFrontVerified().equals("TG")
						&& userInfo.getIdcardInverseVerified().equals("TG")) {
						intent = new Intent(this, BuyBidActivity.class);
						intent.putExtra("bidId", bidId);
						intent.putExtra("base", base);
						startActivity(intent);
					} else if (null != userInfo && (userInfo.getIdcardFrontVerified().equals("DSH")
						&& userInfo.getIdcardInverseVerified().equals("DSH"))) {
						AlertDialogUtil.alert(BidDetailActivity.this, "身份待审核，请您等审核后操作！！！", "确定",
							new AlertListener() {
								@Override
								public void doConfirm() {
								}
							});
					}else if (null != userInfo && (userInfo.getIdcardFrontVerified().equals("TG")
							&& userInfo.getIdcardInverseVerified().equals("DSH"))) {
						AlertDialogUtil.alert(BidDetailActivity.this, "身份待审核，请您等审核后操作！！！", "确定",
								new AlertListener() {
									@Override
									public void doConfirm() {
									}
								});
					}else if (null != userInfo && (userInfo.getIdcardFrontVerified().equals("DSH")
							&& userInfo.getIdcardInverseVerified().equals("TG"))) {
						AlertDialogUtil.alert(BidDetailActivity.this, "身份待审核，请您等审核后操作！！！", "确定",
								new AlertListener() {
									@Override
									public void doConfirm() {
									}
								});
					} else if (null != userInfo
						&& (!userInfo.getIdcardFrontVerified().equals("TG") || !userInfo
							.getIdcardInverseVerified().equals("TG"))
						&& (!userInfo.getIdcardFrontVerified().equals("DSH") || !userInfo
							.getIdcardInverseVerified().equals("DSH"))) {
						AlertDialogUtil.alert(BidDetailActivity.this, "请您立即进行身份信息验证！！！", "马上验证",
							new AlertListener() {

								@Override
								public void doConfirm() {
									Intent intent = new Intent(BidDetailActivity.this,
										SecurityPhotoActivity.class);
									startActivity(intent);
								}
							});
					}
				}
				break;
			}
			case R.id.bottom_lc: {
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				bundle.putSerializable("base", base);
				intent.putExtras(bundle);
				intent.setClass(this, MoneyManageActivity.class);
				startActivity(intent);
			}
			case R.id.bidInfoBtn1: {
				changeTab(0);
				Bundle bundle = new Bundle();
				bundle.putString("bidId", bidId);
				bundle.putString("isDanBao", base.getIsDanBao());
				switchFragment(BidInfoFragment.getInstant(bundle));
				break;
			}
			case R.id.bidInfoBtn2: {
				changeTab(1);
				Bundle bundle = new Bundle();
				bundle.putString("bidId", bidId);
				switchFragment(RelatedDocumentFragment.getInstant(bundle));
				break;
			}
			case R.id.bidInfoBtn3: {
				changeTab(2);
				Bundle bundle = new Bundle();
				bundle.putString("bidId", bidId);
				switchFragment(ReplayPlanFragment.getInstant(bundle));
				break;
			}
			case R.id.bidInfoBtn4: {
				changeTab(3);
				Bundle bundle = new Bundle();
				bundle.putString("bidId", bidId);
				switchFragment(InvestRecordFragment.getInstant(bundle));
				break;
			}
			case R.id.unlogin_tv: {
				// 登录
				Intent intent = new Intent(this, LoginActivity.class);
				startActivity(intent);
				break;
			}
			default:
				break;
			}
		}
		
	}
	
	/**
	 * 修改TAB标签状态
	 * 
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
				bidInfoBtnText4.setTextColor(getResources().getColor(R.color.text_black_8));
				if (Build.VERSION.SDK_INT < 21)
				{
					bidInfoBtnLine1.setImageDrawable(getResources().getDrawable(R.drawable.btn_bottom_blue_line));
					bidInfoBtnLine2.setImageDrawable(getResources().getDrawable(R.drawable.btn_bottom_cc_line));
					bidInfoBtnLine3.setImageDrawable(getResources().getDrawable(R.drawable.btn_bottom_cc_line));
					bidInfoBtnLine4.setImageDrawable(getResources().getDrawable(R.drawable.btn_bottom_cc_line));
				}
				else
				{
					bidInfoBtnLine1.setImageDrawable(getResources().getDrawable(R.drawable.btn_bottom_blue_line));
					bidInfoBtnLine2.setImageDrawable(getResources().getDrawable(R.drawable.btn_bottom_cc_line));
					bidInfoBtnLine3.setImageDrawable(getResources().getDrawable(R.drawable.btn_bottom_cc_line));
					bidInfoBtnLine4.setImageDrawable(getResources().getDrawable(R.drawable.btn_bottom_cc_line));
				}
				break;
			}
			case 1:
			{
				bidInfoBtnText2.setTextColor(getResources().getColor(R.color.main_color));
				bidInfoBtnText1.setTextColor(getResources().getColor(R.color.text_black_8));
				bidInfoBtnText3.setTextColor(getResources().getColor(R.color.text_black_8));
				bidInfoBtnText4.setTextColor(getResources().getColor(R.color.text_black_8));
				if (Build.VERSION.SDK_INT < 21)
				{
					bidInfoBtnLine2.setImageDrawable(getResources().getDrawable(R.drawable.btn_bottom_blue_line));
					bidInfoBtnLine1.setImageDrawable(getResources().getDrawable(R.drawable.btn_bottom_cc_line));
					bidInfoBtnLine3.setImageDrawable(getResources().getDrawable(R.drawable.btn_bottom_cc_line));
					bidInfoBtnLine4.setImageDrawable(getResources().getDrawable(R.drawable.btn_bottom_cc_line));
				}
				else
				{
					bidInfoBtnLine2.setImageDrawable(getResources().getDrawable(R.drawable.btn_bottom_blue_line));
					bidInfoBtnLine1.setImageDrawable(getResources().getDrawable(R.drawable.btn_bottom_cc_line));
					bidInfoBtnLine3.setImageDrawable(getResources().getDrawable(R.drawable.btn_bottom_cc_line));
					bidInfoBtnLine4.setImageDrawable(getResources().getDrawable(R.drawable.btn_bottom_cc_line));
				}
				break;
			}
			case 2:
			{
				bidInfoBtnText3.setTextColor(getResources().getColor(R.color.main_color));
				bidInfoBtnText2.setTextColor(getResources().getColor(R.color.text_black_8));
				bidInfoBtnText1.setTextColor(getResources().getColor(R.color.text_black_8));
				bidInfoBtnText4.setTextColor(getResources().getColor(R.color.text_black_8));
				if (Build.VERSION.SDK_INT < 21)
				{
					bidInfoBtnLine3.setImageDrawable(getResources().getDrawable(R.drawable.btn_bottom_blue_line));
					bidInfoBtnLine2.setImageDrawable(getResources().getDrawable(R.drawable.btn_bottom_cc_line));
					bidInfoBtnLine1.setImageDrawable(getResources().getDrawable(R.drawable.btn_bottom_cc_line));
					bidInfoBtnLine4.setImageDrawable(getResources().getDrawable(R.drawable.btn_bottom_cc_line));
				}
				else
				{
					bidInfoBtnLine3.setImageDrawable(getResources().getDrawable(R.drawable.btn_bottom_blue_line));
					bidInfoBtnLine2.setImageDrawable(getResources().getDrawable(R.drawable.btn_bottom_cc_line));
					bidInfoBtnLine1.setImageDrawable(getResources().getDrawable(R.drawable.btn_bottom_cc_line));
					bidInfoBtnLine4.setImageDrawable(getResources().getDrawable(R.drawable.btn_bottom_cc_line));
				}
				break;
			}
			case 3:
			{
				bidInfoBtnText4.setTextColor(getResources().getColor(R.color.main_color));
				bidInfoBtnText3.setTextColor(getResources().getColor(R.color.text_black_8));
				bidInfoBtnText2.setTextColor(getResources().getColor(R.color.text_black_8));
				bidInfoBtnText1.setTextColor(getResources().getColor(R.color.text_black_8));
				if (Build.VERSION.SDK_INT < 21)
				{
					bidInfoBtnLine4.setImageDrawable(getResources().getDrawable(R.drawable.btn_bottom_blue_line));
					bidInfoBtnLine3.setImageDrawable(getResources().getDrawable(R.drawable.btn_bottom_cc_line));
					bidInfoBtnLine2.setImageDrawable(getResources().getDrawable(R.drawable.btn_bottom_cc_line));
					bidInfoBtnLine1.setImageDrawable(getResources().getDrawable(R.drawable.btn_bottom_cc_line));
				}
				else
				{
					bidInfoBtnLine4.setImageDrawable(getResources().getDrawable(R.drawable.btn_bottom_blue_line));
					bidInfoBtnLine3.setImageDrawable(getResources().getDrawable(R.drawable.btn_bottom_cc_line));
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
	 * 
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
		RelatedDocumentFragment.setNull();
		InvestRecordFragment.setNull();
		ReplayPlanFragment.setNull();
		if (null != timer)
		{
			timer.cancel();
		}
		super.onDestroy();
	}
}
