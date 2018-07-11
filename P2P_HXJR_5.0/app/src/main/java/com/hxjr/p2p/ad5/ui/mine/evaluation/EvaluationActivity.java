package com.hxjr.p2p.ad5.ui.mine.evaluation;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.dm.utils.CookieUtil;
import com.dm.widgets.utils.AlertDialogUtil;
import com.dm.widgets.utils.AlertDialogUtil.AlertListener;
import com.hxjr.p2p.ad5.DMApplication;
import com.hxjr.p2p.ad5.R;
import com.hxjr.p2p.ad5.bean.UserInfo;
import com.hxjr.p2p.ad5.ui.BaseActivity;
import com.hxjr.p2p.ad5.ui.mine.setting.gesture.SetGesturePasswordActivity;
import com.hxjr.p2p.ad5.utils.DMConstant;

public class EvaluationActivity extends BaseActivity {

	public final static String CSS_STYLE = "<style>* {font-size:40px;line-height:50px;}p {color:#FFFFFF;}</style>";


	private WebView content;
	private  TextView title;
	private String setGesture="";
	private String password="";
	private String account="";
	private String pwd="";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_evaluation);
		Intent intent = getIntent();
		if (null != intent) {
			// noticeId = intent.getIntExtra("noticeId", -1);
			// titleStr = intent.getStringExtra("title");
			setGesture=intent.getStringExtra("SetGesture");
			password=intent.getStringExtra("password");
			account=intent.getStringExtra("account");
			pwd=intent.getStringExtra("pwd");
		}
		initView();
		postData();
	}

	@SuppressLint({ "SetJavaScriptEnabled", "NewApi" })
	@Override
	protected void initView() {
		super.initView();
		content = (WebView) findViewById(R.id.evaluation);
		title=(TextView) findViewById(R.id.title_text);
		title.setText("风险评估");
		content.setBackgroundColor(getResources().getColor(R.color.main_bg));
		content.addJavascriptInterface(EvaluationActivity.this, "androidImgUtil");
		WebSettings webSettings = content.getSettings();
		webSettings.setUseWideViewPort(true);// 设置此属性，可任意比例缩放
		webSettings.setLoadWithOverviewMode(true);
		webSettings.setJavaScriptEnabled(true); // 设置WebView属性，能够执行Javascript脚本
		webSettings.setBuiltInZoomControls(true);
		webSettings.setSupportZoom(true);// 支持缩放
		content.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url); // 在当前的webview中跳转到新的url
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
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
			}

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
			}
		});
	}

	private void postData() {
		synCookies(this, DMConstant.Config.PLATA_URL, CookieUtil.getmCookie(this));
		content.loadUrl(DMConstant.API_Url.EVALUATION);
	}
	private void synCookies(Context context, String url, String cookies)
	{
		CookieSyncManager.createInstance(context);
		CookieManager cookieManager = CookieManager.getInstance();
		cookieManager.setAcceptCookie(true);
		        cookieManager.removeSessionCookie();//移除  
		cookieManager.setCookie(url, cookies);//cookies是在HttpClient中获得的cookie  
		CookieSyncManager.getInstance().sync();
	}
	@JavascriptInterface
	public void closeView(String result) {
		UserInfo userinfo = DMApplication.getInstance().getUserInfo();
		userinfo.setAssessment(result);
		AlertDialogUtil.alert(this, "评估结果：" + result, new AlertListener() {
			@Override
			public void doConfirm() {
				if (setGesture.equals("")) {
					finish();
				}else{
					//必须设置解锁手势密码
					Intent intent;
					intent = new Intent(EvaluationActivity.this, SetGesturePasswordActivity.class);
					intent.putExtra("from", "login");
					intent.putExtra("account", account);
					intent.putExtra("pwd", pwd);
					intent.putExtra("isRegister", true);
					intent.putExtra("password", password);
					startActivity(intent);
					finish();
				}	
			}
		});
	}
}
