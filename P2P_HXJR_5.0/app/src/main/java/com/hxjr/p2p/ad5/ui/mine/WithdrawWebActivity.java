package com.hxjr.p2p.ad5.ui.mine;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.TextView;

import com.dm.utils.CookieUtil;
import com.dm.utils.DMLog;
import com.dm.widgets.utils.AlertDialogUtil;
import com.dm.widgets.utils.ToastUtil;
import com.hxjr.p2p.ad5.R;
import com.hxjr.p2p.ad5.ui.BaseActivity;
import com.hxjr.p2p.ad5.ui.mine.setting.TradePwdActivity;

public class WithdrawWebActivity extends BaseActivity {
    private WebView webView;

    private String url;

    private String title;

    private ProgressDialog pd;
    private Intent intent;

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
        setContentView(R.layout.home_banner_detail);
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
            String cookies = CookieUtil.getmCookie(WithdrawWebActivity.this);
            synCookies(WithdrawWebActivity.this, url, cookies);
            webView.loadUrl(url);
        } else {
            AlertDialogUtil.alert(WithdrawWebActivity.this, "数据加载失败");
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
                pd = ProgressDialog.show(WithdrawWebActivity.this, "", "加载中...");
            } else if (!pd.isShowing()){
                pd.show();
            }
            DMLog.e("onPageStarted  url", url);
            if (url.equals("view://user/setPassword")) {
                Intent intent = new Intent(WithdrawWebActivity.this, TradePwdActivity.class);
                startActivity(intent);
            } else if (url.equals("status://setpassword/finish")) {
                ToastUtil.getInstant().show(WithdrawWebActivity.this, "设置成功");
                WithdrawWebActivity.this.finish();
            } else if (url.equals("status://setpassword/fail")) {
                ToastUtil.getInstant().show(WithdrawWebActivity.this, "设置密码失败，请重新尝试！");
                WithdrawWebActivity.this.finish();
            } else if (url.equals("status://withdraw/finish")) {
                ToastUtil.getInstant().show(WithdrawWebActivity.this, "提现成功！");
                WithdrawWebActivity.this.finish();
            } else if (url.equals("status://bid/finish")) {
//                ToastUtil.getInstant().show(WithdrawWebActivity.this, "投标成功！");
                setResult(1111, intent);
                WithdrawWebActivity.this.finish();
            } else if (url.equals("status://bid/fail")) {
                ToastUtil.getInstant().show(WithdrawWebActivity.this, "投标失败，请重新尝试！");
                setResult(0001, intent);
                WithdrawWebActivity.this.finish();
            } else if (url.equals("status://debt/finish")) {
                ToastUtil.getInstant().show(WithdrawWebActivity.this, "购买债权成功！");
                WithdrawWebActivity.this.finish();
            } else if (url.equals("status://debt/fail")) {
                ToastUtil.getInstant().show(WithdrawWebActivity.this, "购买债权失败！请重新购买！");
                WithdrawWebActivity.this.finish();
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
                if (url.equals("view://user/setPassword")) {
                    Intent intent = new Intent(WithdrawWebActivity.this, TradePwdActivity.class);
                    startActivity(intent);
                } else if (url.equals("status://setpassword/finish")) {
                    ToastUtil.getInstant().show(WithdrawWebActivity.this, "设置成功");
                    WithdrawWebActivity.this.finish();
                } else if (url.equals("status://setpassword/fail")) {
                    ToastUtil.getInstant().show(WithdrawWebActivity.this, "设置密码失败，请重新尝试！");
                    WithdrawWebActivity.this.finish();
                } else if (url.equals("status://withdraw/finish")) {
                    WithdrawWebActivity.this.finish();
                } else if (url.equals("status://bid/finish")) {
                    WithdrawWebActivity.this.finish();
                } else if (url.equals("status://bid/fail")) {
                    WithdrawWebActivity.this.finish();
                } else if (url.equals("status://debt/finish")) {
                    WithdrawWebActivity.this.finish();
                } else if (url.equals("status://debt/fail")) {
                    WithdrawWebActivity.this.finish();
                } else {
                    view.loadUrl(url);
                }
            return true;
        }
    }

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