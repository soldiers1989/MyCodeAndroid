/*
 * 文 件 名:  BaseWebViewActivity.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  jiaohongyun
 * 修改时间:  2015年1月13日
 */
package com.dm.tg.web;

import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.hxjr.p2p.ad5.R;
import com.dm.widgets.utils.AlertDialogUtil;
import com.dm.widgets.utils.AlertDialogUtil.AlertListener;
import com.dm.widgets.utils.AlertDialogUtil.ConfirmListener;
import com.dm.widgets.utils.AlertDialogUtil.PromptListener;
import com.dm.widgets.utils.ToastUtil;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.TextView;

/**
 * 用于加载第三方页面的基类
 * @author  jiaohongyun
 * @version  [版本号, 2015年1月13日]
 */
@SuppressWarnings("deprecation")
public abstract class BaseWebViewActivity extends Activity
{
	/**
	 * 用于加载托管方页面
	 */
	protected WebView webView;
	
	/**
	 * 页面标题
	 */
	protected TextView title;
	
	private ProgressDialog pd;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dmt_base_page);
		initView();
	}
	
	/** 
	 * 初始化页面
	 */
	@SuppressLint("SetJavaScriptEnabled")
	private void initView()
	{
		webView = (WebView)findViewById(R.id.base_web);
		title = (TextView)findViewById(R.id.title_text);
		findViewById(R.id.btn_back).setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				finish();
			}
		});
		findViewById(R.id.btn_right).setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				webView.reload();
			}
		});
		setTiteText();
		
		String cookies = getIntent().getStringExtra("cookies");
		String url = setStartUrl();
		
		webView.setWebViewClient(new WebViewClient()
		{
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url)
			{
				Log.d("tg", "BaseWebViewActivity.url=" + url);
				if (isSuccess(url))
				{
					finish();
				}
				else
				{
					view.loadUrl(url); //在当前的webview中跳转到新的url
				}
				return true;
			}
			
			@Override
			public void onPageFinished(WebView view, String url)
			{
				Log.d("tg", "BaseWebViewActivity.url=" + url);
				if (pd != null && pd.isShowing())
				{
					pd.dismiss();
				}
				if (isSuccess(url))
				{
					finish();
				}
				super.onPageFinished(view, url);
			}
			
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon)
			{
				pd = ProgressDialog.show(BaseWebViewActivity.this, "", "加载中...");
				super.onPageStarted(view, url, favicon);
			}
			
		});
		
		webView.setWebChromeClient(new MyWebChromeClient());
		webView.getSettings().setSupportZoom(true);
		webView.getSettings().setUseWideViewPort(true);
		webView.getSettings().setJavaScriptEnabled(true);
		synCookies(this, url, cookies);
		webView.loadUrl(url);
	}
	
	/** 
	 * 同步一下cookie 
	 */
	private void synCookies(Context context, String url, String cookies)
	{
		CookieSyncManager.createInstance(context);
		CookieManager cookieManager = CookieManager.getInstance();
		cookieManager.setAcceptCookie(true);
		//        cookieManager.removeSessionCookie();//移除  
		cookieManager.setCookie(url, cookies);//cookies是在HttpClient中获得的cookie  
		CookieSyncManager.getInstance().sync();
	}
	
	/** 
	 * 设置标题
	 */
	protected void setTiteText()
	{
		title.setText(R.string.main_title);
	}
	
	/**
	 * 第三方业务页面的URL
	 * 需要子类去实现
	 * @return
	 */
	protected abstract String setStartUrl();
	
	private boolean isSuccess(String url)
	{
		String code = getValByParam(url, "code");
		if ("000000".equals(code))
		{
			finish();
			return true;
		}
		else if ("000004".equals(code))
		{
			String description = getValByParam(url, "description");
			showError(description);
			return true;
		}
		else
		{
			return false;
		}
	}
	
	private void showError(String desc)
	{
		if (pd != null && pd.isShowing())
		{
			pd.dismiss();
		}
		String message = null;
		try
		{
			message = new String(Base64.decode(desc, Base64.DEFAULT), "utf-8");
		}
		catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
		ToastUtil.getInstant().show(this, message);
	}
	
	private String getValByParam(String input, String key)
	{
		String result = null;
		Pattern pattern = Pattern.compile("[\\?\\&]" + key + "=([^\\&]*)(\\&?)");
		Matcher matcher = pattern.matcher(input);
		while (matcher.find())
		{
			String _ins = matcher.group(1);
			if (_ins != null && !_ins.isEmpty())
			{
				result = _ins;
			}
		}
		return result;
	}
	
	class MyWebChromeClient extends WebChromeClient
	{
		
		@Override
		public boolean onJsAlert(WebView view, String url, String message, final JsResult result)
		{
			if (pd != null && pd.isShowing())
			{
				pd.dismiss();
			}
			final AlertDialog dlg = AlertDialogUtil.alert(BaseWebViewActivity.this, message, new AlertListener()
			{
				@Override
				public void doConfirm()
				{
					result.confirm();
				}
			});
			dlg.setCancelable(false);
			dlg.setCanceledOnTouchOutside(false);
			return true;
		}
		
		@Override
		public boolean onJsConfirm(WebView view, String url, String message, final JsResult result)
		{
			if (pd != null && pd.isShowing())
			{
				pd.dismiss();
			}
			final AlertDialog dlg = AlertDialogUtil.confirm(BaseWebViewActivity.this, message, new ConfirmListener()
			{
				@Override
				public void onOkClick()
				{
					result.confirm();
				}
				
				@Override
				public void onCancelClick()
				{
					result.cancel();
				}
			});
			dlg.setCancelable(false);
			dlg.setCanceledOnTouchOutside(false);
			return true;
		}
		
		@Override
		public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, final JsPromptResult result)
		{
			if (pd != null && pd.isShowing())
			{
				pd.dismiss();
			}
			final AlertDialog dlg = AlertDialogUtil.prompt(BaseWebViewActivity.this, message, new PromptListener()
			{
				@Override
				public void onOkClick(EditText text)
				{
					result.confirm(text.getText().toString().trim());
				}
				
				@Override
				public void onCancelClick()
				{
					result.cancel();
				}
			});
			dlg.setCancelable(false);
			dlg.setCanceledOnTouchOutside(false);
			return true;
		}
	}
}
