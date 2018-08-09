package com.hxjr.p2p.ad5.ui.mine.bank;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebStorage;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dm.http.HttpCallBack;
import com.dm.http.HttpUtil;
import com.dm.utils.CookieUtil;
import com.dm.utils.DMJsonObject;
import com.dm.utils.DMLog;
import com.dm.widgets.utils.AlertDialogUtil;
import com.dm.widgets.utils.ToastUtil;
import com.hxjr.p2p.ad5.DMApplication;
import com.hxjr.p2p.ad5.R;
import com.hxjr.p2p.ad5.bean.UserInfo;
import com.hxjr.p2p.ad5.ui.BaseActivity;
import com.hxjr.p2p.ad5.ui.mine.setting.TradePwdActivity;
import com.hxjr.p2p.ad5.utils.DMConstant;

import org.json.JSONException;
import org.json.JSONObject;

public class BankCardWebActivity extends BaseActivity {
    private WebView webView;

    private String url;

    private String title;

    private ProgressDialog pd;
    private Intent intent;
    public RelativeLayout rl_pro;
    public ProgressBar pb;
    public TextView handle_tv;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (pd != null && pd.isShowing()) {
                pd.dismiss();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bank_web_card);
         intent = getIntent();
        url = intent.getStringExtra("linkUrl");
        title = intent.getStringExtra("title");
        DMLog.e("url", url);
        initView();
        //如果5秒后还在转圈圈，那么就让取消
        handler.sendEmptyMessageDelayed(0, 3000);
    }

    @SuppressLint({"JavascriptInterface", "SetJavaScriptEnabled"})
    @Override
    protected void initView() {
        super.initView();
        ((TextView) findViewById(R.id.title_text)).setText(title);
        webView = (WebView) findViewById(R.id.home_banner_detail_wb);
        rl_pro=(RelativeLayout) findViewById(R.id.rl_pro);
        pb=(ProgressBar) findViewById(R.id.progressBar);
        handle_tv=(TextView) findViewById(R.id.handle_tv);
        WebSettings webSettings = webView.getSettings();
        webSettings.setUseWideViewPort(true);//设置此属性，可任意比例缩放
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setJavaScriptEnabled(true); // 设置WebView属性，能够执行Javascript脚本
        webSettings.setBuiltInZoomControls(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setDomStorageEnabled(true);//开启DOM缓存，关闭的话H5自身的一些操作是无效的
        webSettings.setSupportZoom(true);//支持缩放
        // 加载需要显示的网页
        webView.addJavascriptInterface(new JavascriptInterface(), "classNameBeExposedInJs");
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        // 设置Web视图
        webView.setWebViewClient(new MyWebViewClient());
        if (url != null) {
            // 加载需要显示的网页
            String cookies = CookieUtil.getmCookie(BankCardWebActivity.this);
            synCookies(BankCardWebActivity.this, url, cookies);
            webView.loadUrl(url);
        } else {
            AlertDialogUtil.alert(BankCardWebActivity.this, "数据加载失败");
        }
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            @SuppressWarnings("deprecation")
            public void onReachedMaxAppCacheSize(long requiredStorage, long quota, WebStorage.QuotaUpdater
                    quotaUpdater) {
                super.onReachedMaxAppCacheSize(requiredStorage, quota, quotaUpdater);
                quotaUpdater.updateQuota(2L * requiredStorage);
            }
        });
    }

    public class JavascriptInterface {
        public void back(String module, int status, String place) {

        }
    }

    // 监听所有点击的链接，如果拦截到我们需要的，就跳转到相对应的页面。
    private class MyWebViewClient extends WebViewClient {
        @Override
        @SuppressLint("SetJavaScriptEnabled")
        public void onPageFinished(WebView webView, String url) {
            super.onPageFinished(webView, url);
            webView.getSettings().setJavaScriptEnabled(true);
            if (pd != null && pd.isShowing()) {
                pd.dismiss();
            }
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            if (null == pd) {
                pd = ProgressDialog.show(BankCardWebActivity.this, "", "加载中...");
            } else if (!pd.isShowing()){
                pd.show();
            }
            DMLog.e("onPageStarted  url", url);
            if (url.equals("view://user/center")) {
                HttpUtil.getInstance().post(BankCardWebActivity.this, DMConstant.API_Url.USER_USERINFO, new
                        HttpCallBack()
                {
                    @Override
                    public void onSuccess(JSONObject result)
                    {
                        try
                        {
                            String code = result.getString("code");
                            if (code.equals(DMConstant.ResultCode.SUCCESS))
                            {
                                DMJsonObject data = new DMJsonObject(result.getString("data"));
                                UserInfo userInfo = new UserInfo(data);
                                DMLog.e("getUserInfo------USERINFO",userInfo.toString());
                                DMApplication.getInstance().setUserInfo(userInfo);
                                if ("".equals(DMApplication.getInstance().getUserInfo().getUsrCustId())){
                                    ToastUtil.getInstant().show(BankCardWebActivity.this,"注册失败，请重试！");
                                    BankCardWebActivity.this.finish();
                                }
                                else{
                                    handler1.sendEmptyMessage(MSG_UPDATE);
//                                    Intent intent = new Intent(BankCardWebActivity.this, TradePwdActivity.class);
//                                    BankCardWebActivity.this.startActivity(intent);
//                                    BankCardWebActivity.this.finish();
                                }
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
                    }

                    @Override
                    public void onStart()
                    {
                        setShowProgress(false);
                    }
                });
//                ApiUtil.getUserInfoWebRegister(BankCardWebActivity.this);
//                if ("".equals(DMApplication.getInstance().getUserInfo().getUsrCustId())){
//                    ToastUtil.getInstant().show(BankCardWebActivity.this,"注册失败，请重试！");
//                    finish();
//                }
//                else{
//                    Intent intent = new Intent(BankCardWebActivity.this, TradePwdActivity.class);
//                    startActivity(intent);
//                }
            }
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed();
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            //			dimeng_title_rl.setVisibility(View.GONE);
            DMLog.e("shouldOverrideUrlLoading url", url);
                view.loadUrl(url);
            return true;
        }
    }

    private int pp =0;
    private Handler handler1 = new Handler(){
        public void handleMessage(android.os.Message msg) {
            if (msg.what == MSG_UPDATE) {
                if (pp<40) {
                    pp++;
                    Log.e("PPPPPPPPPPPPP",""+pp);
                    handler1.sendEmptyMessageDelayed(MSG_UPDATE, 1000);
                    rl_pro.setVisibility(View.VISIBLE);
                    webView.setVisibility(View.GONE);
                    handle_tv.setText(40-pp+"请等待！！！");
                } else {
                    handler1.removeMessages(MSG_UPDATE);
                    pb.setVisibility(View.GONE);
                    Intent intent = new Intent(BankCardWebActivity.this, TradePwdActivity.class);
                    BankCardWebActivity.this.startActivity(intent);
                    BankCardWebActivity.this.finish();
                }
            }
        };
    };

    private static final int MSG_UPDATE = 0x100;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (webView.canGoBack() && event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 同步一下cookie
     */
    private void synCookies(Context context, String url, String cookies) {
        CookieSyncManager.createInstance(context);
        CookieManager cookieManager = CookieManager.getInstance();
        //        cookieManager.setAcceptCookie(true);
        //        CookieManager.getInstance().setAcceptThirdPartyCookies(webView,true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cookieManager.setAcceptThirdPartyCookies(webView, true);
        } else {
            cookieManager.setAcceptCookie(true);
        }
        cookieManager.removeSessionCookie();//移除
        cookieManager.setCookie(url, cookies);//cookies是在HttpClient中获得的cookie
        CookieSyncManager.getInstance().sync();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        webView.clearCache(true);
    }
}