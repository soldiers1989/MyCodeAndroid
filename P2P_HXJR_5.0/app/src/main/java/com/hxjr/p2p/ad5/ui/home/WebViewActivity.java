package com.hxjr.p2p.ad5.ui.home;

import com.hxjr.p2p.ad5.ui.BaseActivity;
import com.hxjr.p2p.ad5.utils.FormatUtil;
import com.hxjr.p2p.ad5.R;
import com.dm.widgets.utils.AlertDialogUtil;
import com.dm.widgets.utils.ToastUtil;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebStorage;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

/**
 * @author  huangkaibo
 * @date 2015-11-18
 */
public class WebViewActivity extends BaseActivity
{
	private WebView webView;
	
	private String url;
	
	private String title;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_banner_detail);
		Intent intent = getIntent();
		url = intent.getStringExtra("linkUrl");
		title = intent.getStringExtra("title");
		initView();
	}
	
	@SuppressLint({"JavascriptInterface", "SetJavaScriptEnabled"})
	@Override
	protected void initView()
	{
		super.initView();
		((TextView)findViewById(R.id.title_text)).setText(title);
		webView = (WebView)findViewById(R.id.home_banner_detail_wb);
		webView.clearCache(true);
		WebSettings webSettings = webView.getSettings();
		webSettings.setUseWideViewPort(true);//设置此属性，可任意比例缩放
		webSettings.setLoadWithOverviewMode(true);
		webSettings.setJavaScriptEnabled(true); // 设置WebView属性，能够执行Javascript脚本
		webSettings.setBuiltInZoomControls(true);
		webSettings.setSupportZoom(true);//支持缩放 
		// 加载需要显示的网页
		webView.addJavascriptInterface(new JavascriptInterface(), "classNameBeExposedInJs");
		webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
		if (url != null && FormatUtil.validateUrl(url))
		{
			// 加载需要显示的网页
			webView.loadUrl(url);
		}
		else
		{
			AlertDialogUtil.alert(WebViewActivity.this, "数据加载失败");
		}
		// 设置Web视图
		webView.setWebViewClient(new MyWebViewClient());
		
		webView.setWebChromeClient(new WebChromeClient()
		{
			@Override
			@SuppressWarnings("deprecation")
			public void onReachedMaxAppCacheSize(long requiredStorage, long quota, WebStorage.QuotaUpdater quotaUpdater)
			{
				super.onReachedMaxAppCacheSize(requiredStorage, quota, quotaUpdater);
				quotaUpdater.updateQuota(2L * requiredStorage);
			}
		});
	}
	
	public class JavascriptInterface
	{
		public void back(String module, int status, String place)
		{
			
		}
	}
	
	// 监听所有点击的链接，如果拦截到我们需要的，就跳转到相对应的页面。
	private class MyWebViewClient extends WebViewClient
	{
		@Override
		@SuppressLint("SetJavaScriptEnabled")
		public void onPageFinished(WebView webView, String url)
		{
			super.onPageFinished(webView, url);
			webView.getSettings().setJavaScriptEnabled(true);
		}
		
		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon)
		{
			super.onPageStarted(view, url, favicon);
		}
		
		@Override
		public void onReceivedError(WebView view, int errorCode, String description, String failingUrl)
		{
			ToastUtil.getInstant().show(WebViewActivity.this, "加载失败");
		}
		
		@Override
		public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error)
		{
			handler.proceed();
		}
		
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url)
		{
			//			dimeng_title_rl.setVisibility(View.GONE);
			view.loadUrl(url);
			return true;
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (webView.canGoBack() && event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0)
		{
			webView.goBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
