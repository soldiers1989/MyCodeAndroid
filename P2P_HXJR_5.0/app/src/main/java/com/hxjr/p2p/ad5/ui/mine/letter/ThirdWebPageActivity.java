package com.hxjr.p2p.ad5.ui.mine.letter;

import com.hxjr.p2p.ad5.ui.BaseActivity;
import com.hxjr.p2p.ad5.R;
import com.dm.utils.CookieUtil;
import com.dm.utils.DMLog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

public class ThirdWebPageActivity extends BaseActivity
{
	private WebView webView;
	
	private String url;
	
	private String title = "";
	
	private String autoUrl;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.third_web_page);
		url = getIntent().getStringExtra("url");
		title = getIntent().getStringExtra("title");
		initView();
		initData();
	}
	
	@Override
	protected void initView()
	{
		super.initView();
		((TextView)findViewById(R.id.title_text)).setText(title);
		webView = (WebView)findViewById(R.id.webView);
	}
	
	@SuppressLint("SetJavaScriptEnabled")
	private void initData()
	{
		webView.setBackgroundColor(getResources().getColor(R.color.main_bg));
		WebSettings webSettings = webView.getSettings();
		webSettings.setUseWideViewPort(true);//设置此属性，可任意比例缩放
		webSettings.setLoadWithOverviewMode(true);
		webSettings.setJavaScriptEnabled(true); // 设置WebView属性，能够执行Javascript脚本
		webSettings.setBuiltInZoomControls(true);
		webSettings.setSupportZoom(true);//支持缩放 
		webSettings.setUseWideViewPort(true);
		webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
		webSettings.setTextZoom(100);
		webView.setWebViewClient(new WebViewClient()
		{
			private String CookieStr;
			
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url)
			{
				if (CookieStr != null)
				{
					synCookies(ThirdWebPageActivity.this, url, CookieStr);
				}
				view.loadUrl(url); //在当前的webview中跳转到新的url
				return true;
			}
			
			@Override
			public void onPageFinished(WebView view, String url)
			{
				DMLog.d("tg", "BaseWebViewActivity.url=" + url);
				CookieManager cookieManager = CookieManager.getInstance();
				CookieStr = cookieManager.getCookie(url);
				super.onPageFinished(view, url);
			}
			
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon)
			{
				super.onPageStarted(view, url, favicon);
				CookieManager cookieManager = CookieManager.getInstance();
				CookieStr = cookieManager.getCookie(url);
			}
			
		});
		String cookies = CookieUtil.getmCookie(this);
		synCookies(this, url, cookies);
		webView.loadUrl(url);
	}
	
	/** 
	 * 同步一下cookie 
	 */
	@SuppressWarnings("deprecation")
	private void synCookies(Context context, String url, String cookies)
	{
		CookieSyncManager.createInstance(context); //这里和CookieSyncManager.getInstance().sync();可以不要，但为了兼容低版本，还是加上
		CookieManager cookieManager = CookieManager.getInstance();
		cookieManager.setAcceptCookie(true);
		//        cookieManager.removeSessionCookie();//移除  
		cookieManager.setCookie(url, cookies);//cookies是在HttpClient中获得的cookie  
		CookieSyncManager.getInstance().sync();
	}
	
	@Override
	protected void onDestroy()
	{
		// TODO Auto-generated method stub
		//		webView.clearHistory();
		//		webView.clearFormData();
		//		webView.clearCache(true);
		super.onDestroy();
	}
}
