package com.hxjr.p2p.ad5.ui.investment.bid;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.dm.widgets.TouchWebView;
import com.hxjr.p2p.ad5.R;
import com.hxjr.p2p.ad5.utils.DMConstant;
import com.hxjr.p2p.ad5.utils.JSInterface;

public class RelatedDocumentFragment extends Fragment
{
	private static RelatedDocumentFragment instant;
	
	/**
	 * 标的ID
	 */
	private String bidId;
	
	private View mView;
	
	private TouchWebView bidInfoWeb;
	
	public RelatedDocumentFragment()
	{
		super();
	}
	
	public static RelatedDocumentFragment getInstant(Bundle args)
	{
		if (instant == null)
		{
			instant = new RelatedDocumentFragment();
			instant.setArguments(args);
		}
		return instant;
	}
	
	public static void setNull()
	{
		if (null != instant)
		{
			instant = null;
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState)
	{
		return inflater.inflate(R.layout.related_document_info, container,false);
	}
	
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		bidId = getArguments().getString("bidId");
		mView = getView();
		initView();
	}
	
	private void initView()
	{
		bidInfoWeb = (TouchWebView)mView.findViewById(R.id.bidInfoWeb);
		String url = DMConstant.Config.PLATA_URL + "xgwj.jsp?bidId=" + bidId;
		bidInfoWeb.getSettings().setSupportZoom(false);
		bidInfoWeb.getSettings().setUseWideViewPort(true);
		bidInfoWeb.getSettings().setJavaScriptEnabled(true);
		bidInfoWeb.setWebViewClient(new MyWebViewClient());
		bidInfoWeb.getSettings().setUserAgentString("Android/1.0");

		// android 5.0以上默认不支持Mixed Content
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			bidInfoWeb.getSettings().setMixedContentMode(
					WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE);
		}

		JSInterface jsInterface = new JSInterface(getContext());
		bidInfoWeb.addJavascriptInterface(jsInterface, "androidImgUtil");
		
		bidInfoWeb.loadUrl(url);
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
		public void onReceivedSslError(WebView view,
									   SslErrorHandler handler, SslError error) {
			// TODO Auto-generated method stub
			// handler.cancel();// Android默认的处理方式
			handler.proceed();// 接受所有网站的证书
			// handleMessage(Message msg);// 进行其他处理
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
