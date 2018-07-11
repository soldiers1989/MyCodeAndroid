package com.hxjr.p2p.ad5.ui.discovery.donation;

import org.json.JSONException;
import org.json.JSONObject;

import com.hxjr.p2p.ad5.DMApplication;
import com.hxjr.p2p.ad5.bean.GyInfo;
import com.hxjr.p2p.ad5.bean.UserInfo;
import com.hxjr.p2p.ad5.ui.BaseActivity;
import com.hxjr.p2p.ad5.ui.mine.user.LoginActivity;
import com.hxjr.p2p.ad5.ui.tg.UnRegisterTgActivity;
import com.hxjr.p2p.ad5.utils.DMConstant;
import com.hxjr.p2p.ad5.utils.ErrorUtil;
import com.hxjr.p2p.ad5.utils.FormatUtil;
import com.hxjr.p2p.ad5.utils.HttpParams;
import com.hxjr.p2p.ad5.R;
import com.dm.http.HttpCallBack;
import com.dm.http.HttpUtil;
import com.dm.utils.DMJsonObject;
import com.dm.utils.StringUtils;
import com.dm.widgets.RoundCornerProgressBar;
import com.dm.widgets.TouchWebView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 捐赠详情
 * @author  huangkaibo
 * @date 2015年11月13日
 */
public class DonationDetailActivity extends BaseActivity implements OnClickListener
{
	/**捐助记录   点击处*/
	private LinearLayout donation_record_ll;
	
	/**捐助按钮*/
	private Button donation_bid_btn;
	
	/** 标的名称 */
	private TextView loanName_tv;
	
	/**公益机构*/
	private TextView public_welfare_institutions_tv;
	
	/**公益目标*/
	private TextView donation_target_tv;
	
	/**捐款人数*/
	private TextView donation_people_tv;
	
	/**公益 已筹*/
	private TextView donation_have_raise;
	
	/**最低可捐*/
	private TextView donation_min_tv;
	
	/**开始筹集时间*/
	private TextView donation_start_time_tv;
	
	/**结束筹集时间*/
	private TextView donation_end_time_tv;
	
	/**可捐*/
	private TextView donation_enable_tv;
	
	/**标的进度*/
	private RoundCornerProgressBar bid_progress_pb;
	
	private TouchWebView content_webview;
	
	private GyInfo gyInfo;
	
	private String bidId;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.donation_detail);
		Intent intent = getIntent();
		bidId = intent.getStringExtra("bidId");
		initView();
		postData();
		initListener();
	}
	
	@Override
	protected void initView()
	{
		super.initView();
		((TextView)findViewById(R.id.title_text)).setText(R.string.donation_detail);
		donation_record_ll = (LinearLayout)findViewById(R.id.donation_record_ll);
		public_welfare_institutions_tv = (TextView)findViewById(R.id.public_welfare_institutions_tv);
		loanName_tv = (TextView)findViewById(R.id.loanName_tv);
		donation_target_tv = (TextView)findViewById(R.id.donation_target_tv);
		donation_people_tv = (TextView)findViewById(R.id.donation_people_tv);
		donation_have_raise = (TextView)findViewById(R.id.donation_have_raise);
		donation_min_tv = (TextView)findViewById(R.id.donation_min_tv);
		donation_start_time_tv = (TextView)findViewById(R.id.donation_start_time_tv);
		donation_end_time_tv = (TextView)findViewById(R.id.donation_end_time_tv);
		donation_enable_tv = (TextView)findViewById(R.id.donation_enable_tv);
		bid_progress_pb = (RoundCornerProgressBar)findViewById(R.id.bid_progress_pb);
		donation_bid_btn = (Button)findViewById(R.id.donation_bid_btn);
		
		content_webview = (TouchWebView)findViewById(R.id.content_webview);
		//http://61.145.159.156:5100/app/gyLoanBidItem.jsp?bidId=7&color=34B8FC
		String url = DMConstant.Config.PLATA_URL + "gyLoanBidItem.jsp?bidId=" + bidId + "&color=34b8fc";
		content_webview.getSettings().setSupportZoom(false);
		content_webview.getSettings().setUseWideViewPort(true);
		content_webview.getSettings().setJavaScriptEnabled(true);
		content_webview.setWebViewClient(new MyWebViewClient());
		content_webview.getSettings().setUserAgentString("Android/1.0");
		content_webview.loadUrl(url);
	}
	
	/**
	 * 获取公益标详情
	 */
	private void postData()
	{
		HttpParams params = new HttpParams();
		params.put("bidId", bidId);
		
		HttpUtil.getInstance().post(this, DMConstant.API_Url.GY_LOAN_DETAIL, params, new HttpCallBack()
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
						gyInfo = new GyInfo(data);
						setViewData();
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
	
	/**
	 * 填充view数据
	 */
	private void setViewData()
	{
		loanName_tv.setText(gyInfo.getLoanName());
		public_welfare_institutions_tv.setText(getString(R.string.public_welfare_institutions, gyInfo.getOrganisers()));
		donation_target_tv.setText(getString(R.string.donation_detail_target, formatMoney(gyInfo.getLoanAmount())));
		donation_people_tv.setText(getString(R.string.donation_detail_people, gyInfo.getTotalNum()));
		donation_have_raise.setText(formatMoney(gyInfo.getDonationsAmount()));//已筹
		donation_min_tv.setText(getString(R.string.donation_min, formatMoney(gyInfo.getMinAmount())));
		donation_enable_tv.setText(formatMoney(gyInfo.getRemaindAmount()));//可捐
		donation_start_time_tv.setText(getString(R.string.donation_start_time, gyInfo.getLoanStartTime()));
		donation_end_time_tv.setText(getString(R.string.donation_end_time, gyInfo.getLoanEndTime()));
		bid_progress_pb.setProgress((float)gyInfo.getProgress() * 100);// 进度
		if ((int)gyInfo.getProgress() != 1 && !gyInfo.isTimeEnd()) //没有捐赠完和没有到期，才显示捐赠的按钮
		{
			donation_bid_btn.setVisibility(View.VISIBLE);
		}
	}
	
	/**
	 * 格式化金额
	 * @param amount
	 * @return
	 */
	private String formatMoney(String amount)
	{
		return StringUtils.isEmptyOrNull(amount) ? "0.00元" : FormatUtil.formatMoney(Double.valueOf(amount));
	}
	
	/**
	 * 初始化监听器
	 */
	private void initListener()
	{
		donation_record_ll.setOnClickListener(this);
		donation_bid_btn.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.donation_record_ll:// 捐赠记录
			{
				Intent recordIntent =
					new Intent(DonationDetailActivity.this, DonationRecordActivity.class).putExtra("bidId", bidId);
				startActivity(recordIntent);
				break;
			}
			case R.id.donation_bid_btn:// 公益投标
			{
				UserInfo userInfo = DMApplication.getInstance().getUserInfo();
				if (!DMApplication.getInstance().islogined())
				{
					//登录
					Intent intent = new Intent(this, LoginActivity.class);
					startActivity(intent);
					DMApplication.toLoginValue = -1;
				}
				else if (null != userInfo && userInfo.isTg() && StringUtils.isEmptyOrNull(userInfo.getUsrCustId()))
				{//如果是托管，并且还没有注册第三方
					startActivity(new Intent(this, UnRegisterTgActivity.class).putExtra("title", "捐助"));
				}
				else
				{
					Intent bidIntent = new Intent(DonationDetailActivity.this, DonationBidActivity.class);
					bidIntent.putExtra("bidId", bidId);
					bidIntent.putExtra("remainAmount", gyInfo.getRemaindAmount());
					double minAmount = Double.valueOf(gyInfo.getMinAmount());
					bidIntent.putExtra("minBidingAmount", (int)minAmount);
					bidIntent.putExtra("loanAmount", gyInfo.getLoanAmount());
					startActivity(bidIntent);
				}
				break;
			}
			
			default:
				break;
		}
	}
	
	class MyWebViewClient extends WebViewClient
	{
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url)
		{
			//			view.loadUrl(url); //在当前的webview中跳转到新的url
			Uri uri = Uri.parse(url);
			Intent intent = new Intent(Intent.ACTION_VIEW, uri);
			startActivity(intent);
			return true;
		}
		
		@Override
		public void onPageFinished(WebView view, String url)
		{
			super.onPageFinished(view, url);
		}
		
		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon)
		{
			super.onPageStarted(view, url, favicon);
		}
	}
}
