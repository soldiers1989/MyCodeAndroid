package com.hxjr.p2p.ad5.ui.mine.contract;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.dm.http.HttpCallBack;
import com.dm.http.HttpUtil;
import com.dm.utils.DMJsonObject;
import com.dm.utils.DMLog;
import com.hxjr.p2p.ad5.R;
import com.hxjr.p2p.ad5.ui.BaseActivity;
import com.hxjr.p2p.ad5.utils.DMConstant;
import com.hxjr.p2p.ad5.utils.ErrorUtil;
import com.hxjr.p2p.ad5.utils.HttpParams;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 我的债权转让-查看合同
 * @author  huangkaibo
 * @date 2016年01月06日
 */
public class CheckCreContractActivity extends BaseActivity
{
	private WebView webView;
	
	private String bidId; //标的ID

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.check_contract);
		initView();
	}
	
	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void initView()
	{
		super.initView();
		((TextView)findViewById(R.id.title_text)).setText(R.string.invest_check_context);
		
		webView = (WebView)findViewById(R.id.creditor_webview);
		webView.setBackgroundColor(getResources().getColor(R.color.main_bg));
		WebSettings webSettings = webView.getSettings();
		webSettings.setUseWideViewPort(true);//设置此属性，可任意比例缩放
		webSettings.setDomStorageEnabled(true);//对H5支持
		webSettings.setJavaScriptEnabled(true); // 设置WebView属性，能够执行Javascript脚本
		webSettings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
		webSettings.setLoadsImagesAutomatically(true); //支持自动加载图片
		webSettings.setJavaScriptEnabled(true);
		webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
		webSettings.setDomStorageEnabled(true);
		webSettings.setDatabaseEnabled(true);
		webSettings.setAppCacheEnabled(true);
		webSettings.setAllowFileAccess(true);
		webSettings.setSavePassword(true);
		webSettings.setBuiltInZoomControls(true);
		webSettings.setLoadWithOverviewMode(true);
		webSettings.setBuiltInZoomControls(true);
		// 设置Web视图
		webView.setWebViewClient(new MyWebViewClient());
		bidId = getIntent().getStringExtra("id");
		getCheckLoanContractData();
	}
	// 监听所有点击的链接，如果拦截到我们需要的，就跳转到相对应的页面。
	private class MyWebViewClient extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
		}
	}
	private void getCheckLoanContractData()
	{
		HttpParams params = new HttpParams();
		params.put("id", bidId);
//		params.put("isGyb", isGyb ? "true" : "false");
		HttpUtil.getInstance().post(this, DMConstant.API_Url.CHECK_CREDITOR_AGREEMENT, params, new HttpCallBack()
		{
			
			@Override
			public void onSuccess(JSONObject result)
			{
				try
				{
					DMLog.i("getCheckLoanContractData", result.toString());
					String code = result.getString("code");
					if (DMConstant.ResultCode.SUCCESS.equals(code))
					{
						DMJsonObject data = new DMJsonObject(result.getString("data"));
						if (null != data.getString("url")) {
							webView.loadUrl(data.getString("url"));
						} else {
							webView.loadDataWithBaseURL(null, data.getString("agreementView"), "text/html", "UTF-8",
									null);
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
				super.onFailure(t, context);
			}
			
		});
	}
	
}
