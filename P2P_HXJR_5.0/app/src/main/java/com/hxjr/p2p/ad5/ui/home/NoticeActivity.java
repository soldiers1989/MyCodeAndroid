package com.hxjr.p2p.ad5.ui.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.webkit.SslErrorHandler;
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
 * 公告详情
 *
 * @author huangkaibo
 * @date 2015-11-19
 */
public class NoticeActivity extends BaseActivity {
    public final static String CSS_STYLE = "<style>* {font-size:40px;line-height:50px;}p {color:#FFFFFF;}</style>";

    private int noticeId;

    private TextView title;

    private String titleStr = "";

    private TextView time;

    private WebView content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notice_detail_activity);
        Intent intent = getIntent();
        if (null != intent) {
            noticeId = intent.getIntExtra("noticeId", -1);
            titleStr = intent.getStringExtra("title");
        }
        initView();
        postData();
    }

    @SuppressLint({"SetJavaScriptEnabled", "NewApi"})
    @Override
    protected void initView() {
        super.initView();
        ((TextView) findViewById(R.id.title_text)).setText(titleStr);
        title = (TextView) findViewById(R.id.notice_title);
        time = (TextView) findViewById(R.id.notice_time);
        content = (WebView) findViewById(R.id.notice_content);
        content.setBackgroundColor(getResources().getColor(R.color.main_bg));
        WebSettings webSettings = content.getSettings();
        webSettings.setUseWideViewPort(true);//设置此属性，可任意比例缩放
        webSettings.setLoadWithOverviewMode(true);
        // android 5.0以上默认不支持Mixed Content
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE);
        }
        webSettings.setJavaScriptEnabled(true); // 设置WebView属性，能够执行Javascript脚本
        webSettings.setBuiltInZoomControls(true);
        webSettings.setSupportZoom(true);//支持缩放
        webSettings.setTextZoom(300); //设置字体默认缩放大小
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
        HttpParams params = new HttpParams();
        params.put("id", noticeId);
        HttpUtil.getInstance().post(this, DMConstant.API_Url.NOTICE_ITEM, params,
                new HttpCallBack() {
                    @Override
                    public void onSuccess(JSONObject result) {
                        try {
                            String code = result.getString("code");

                            if (DMConstant.ResultCode.SUCCESS.equals(code)) {
                                DMJsonObject dataObj = new DMJsonObject(result.getString("data"));

                                title.setText(dataObj.getString("title"));
                                time.setText("发布时间 : " +
                                        dataObj.getString("releaseTime"));//publishDate
                                DMLog.e(dataObj.getString("content"));
                                content.loadDataWithBaseURL(DMConstant.Config.PLATA_URL, dataObj.getString("content")
                                        , "text/html", "utf-8", null);
                            } else {
                                ErrorUtil.showError(result);
                            }
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Throwable t, Context context) {
                        super.onFailure(t, context);
                    }
                });
    }
}
