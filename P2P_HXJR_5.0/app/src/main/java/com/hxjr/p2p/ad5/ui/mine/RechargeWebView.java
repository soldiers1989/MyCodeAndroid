/*
 * 文 件 名:  RechargeWebView.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  jiaohongyun
 * 修改时间:  2015年1月22日
 */
package com.hxjr.p2p.ad5.ui.mine;

import com.hxjr.p2p.ad5.ui.BaseActivity;
import com.hxjr.p2p.ad5.R;
import com.dm.utils.CookieUtil;
import com.dm.utils.DMLog;
import com.dm.widgets.utils.AlertDialogUtil;
import com.dm.widgets.utils.AlertDialogUtil.AlertListener;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

/**
 * 充值跳转页面（网关模式使用）
 * @author  jiaohongyun
 * @version  [版本号, 2015年1月22日]
 */
@SuppressWarnings("deprecation")
public class RechargeWebView extends BaseActivity
{
    /**
	 * 用于加载托管方页面
	 */
    private WebView webView;
    
    /**
	 * 页面标题
	 */
    protected TextView title;
    
    private ProgressDialog pd;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recharge_netgate);
        initView();
    }
    
    /** 
	 * 初始化页面
	 */
    @SuppressLint("SetJavaScriptEnabled")
	public void initView()
    {
		super.initView();
		((TextView)findViewById(R.id.title_text)).setText(R.string.main_title);
		webView = (WebView)findViewById(R.id.base_web);
		String cookies = CookieUtil.getmCookie(this);
        String url = getIntent().getStringExtra("rechargeUrl");
        
        webView.setWebViewClient(new WebViewClient()
        {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url)
            {
                DMLog.d("tg", "BaseWebViewActivity.url=" + url);
                if (checkFinish(url))
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
                DMLog.d("tg", "BaseWebViewActivity.url=" + url);
                if (pd != null && pd.isShowing())
                {
                    pd.dismiss();
                }
                if (checkFinish(url))
                {
                    finish();
                }
                super.onPageFinished(view, url);
            }
            
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon)
            {
				//				pd = ProgressDialog.show(RechargeWebView.this, "", "加载中...");				
                super.onPageStarted(view, url, favicon);
            }
            
        });
        
        webView.setWebChromeClient(new MyWebChromeClient());
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setUserAgentString("Android/1.0");
		//		ToastUtil.getInstant().show(this, cookies);
		synCookies(this, url, cookies);
		if (url.startsWith("http"))
		{
			webView.loadUrl(url);
		}
		else
		{
			webView.loadDataWithBaseURL(null, url, "text/html", "utf-8", null);
		}
		
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
	 * 是否应该返回
	 * @param url
	 * @return
	 */
    private boolean checkFinish(String url)
    {
		//未调测，有待日后实现
        return false;
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
			AlertDialogUtil.alert(RechargeWebView.this, message, new AlertListener()
			{
				
				@Override
				public void doConfirm()
				{
					result.confirm();
				}
			});
            return true;
        }
        
    }
}
