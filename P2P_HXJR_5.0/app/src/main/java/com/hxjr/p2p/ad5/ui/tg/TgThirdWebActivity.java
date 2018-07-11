package com.hxjr.p2p.ad5.ui.tg;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.dm.http.HttpCallBack;
import com.dm.http.HttpUtil;
import com.dm.utils.AppManager;
import com.dm.utils.CookieUtil;
import com.dm.utils.DMLog;
import com.dm.widgets.utils.AlertDialogUtil;
import com.dm.widgets.utils.AlertDialogUtil.AlertListener;
import com.dm.widgets.utils.AlertDialogUtil.ConfirmListener;
import com.dm.widgets.utils.ToastUtil;
import com.hxjr.p2p.ad5.R;
import com.hxjr.p2p.ad5.receiver.BidAndCreReceiver;
import com.hxjr.p2p.ad5.ui.BaseActivity;
import com.hxjr.p2p.ad5.ui.MainActivity;
import com.hxjr.p2p.ad5.ui.discovery.donation.DonationBidActivity;
import com.hxjr.p2p.ad5.ui.discovery.donation.DonationDetailActivity;
import com.hxjr.p2p.ad5.ui.investment.bid.BidDetailActivity;
import com.hxjr.p2p.ad5.ui.investment.bid.BuyBidActivity;
import com.hxjr.p2p.ad5.ui.investment.cre.BuyCreActivity;
import com.hxjr.p2p.ad5.ui.investment.cre.CreDetailActivity;
import com.hxjr.p2p.ad5.ui.mine.WithdrawActivity;
import com.hxjr.p2p.ad5.utils.DMConstant;
import com.hxjr.p2p.ad5.utils.ErrorUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 注册第三方托管帐户
 * 或者其它第三方页面
 *
 * @author jiaohongyun
 */
public class TgThirdWebActivity extends BaseActivity {
    /**
     * 用于加载托管方页面
     */
    private WebView webView;

    private ProgressDialog pd;

    /**
     * 头部标题
     */
    private String title;

    /**
     * 将要跳转的页面
     */
    private String url;

    /**
     * 关闭页面前的提示
     */
    private String message;

    private boolean markerFalg = false;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (pd != null && pd.isShowing()) {
                pd.dismiss();
            }
        }

        ;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tg_register);

        url = getIntent().getStringExtra("url");
        DMLog.e(url);
        message = getIntent().getStringExtra("message");
        title = getIntent().getStringExtra("title");
        initView();
    }

    /**
     * 初始化页面
     */
    @SuppressLint("SetJavaScriptEnabled")
    public void initView() {
        super.initView();
        if (TextUtils.isEmpty(title)) {
            ((TextView) findViewById(R.id.title_text)).setText("注册第三方托管账户");
        } else {
            ((TextView) findViewById(R.id.title_text)).setText(title);
        }

        webView = (WebView) findViewById(R.id.base_web);
        webView.setWebViewClient(new MyWebViewClient());
        webView.setWebChromeClient(new MyWebChromeClient());
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);  //提高渲染的优先级
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN); //支持内容重新布局
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setDomStorageEnabled(true);

        webView.getSettings().setUseWideViewPort(true);//设置此属性，可任意比例缩放
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setJavaScriptEnabled(true); // 设置WebView属性，能够执行Javascript脚本
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setAppCacheEnabled(true);
        webView.getSettings().setDatabaseEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);//开启DOM缓存，关闭的话H5自身的一些操作是无效的


        //		webView.getSettings().setUserAgentString("Android/1.0");
        if (!TextUtils.isEmpty(url)) {
            String cookies = CookieUtil.getmCookie(TgThirdWebActivity.this);
            synCookies(TgThirdWebActivity.this, url, cookies);
            webView.loadUrl(url);
        } else {
            getUrl();
        }

        //如果5秒后还在转圈圈，那么就让取消
        handler.sendEmptyMessageDelayed(0, 3000);
    }

    class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            //			if(url.endsWith("?")){
            //				url = url.substring(0, url.length()-1);
            //			}
            Log.e("OverrideUrlLoading", url);
            //            Random random = new Random();
            //            int temp = random.nextInt(100000000);
            //            if (url.indexOf("?") != -1) {
            //                url = url + "&dmRandom=" + temp;
            //            } else {
            //                url = url + "?dmRandom=" + temp;
            //            }
            //
            //            if (isSuccess(url)) {
            //                Log.e("isSuccess", url);
            //                markerFalg = true;
            //                if (pd != null && pd.isShowing()) {
            //                    pd.dismiss();
            //                }
            //                doFinish(message);
            //            } else {
            //                Log.e("OverrideUrlLoading", url);
            //                view.loadUrl(url); //在当前的webview中跳转到新的url
            //            }
            if (url.equals(DMConstant.API_Url.HKCG)) {
//                view.loadUrl(url); //在当前的webview中跳转到新的url
                doFinish(message);
            } else if (url.equals(DMConstant.API_Url.HKSB)) {
//                view.loadUrl(url); //在当前的webview中跳转到新的url
                doFinish("还款失败，请重试！");
            }else{
                view.loadUrl(url);
            }
            //            else {
            //                Random random = new Random();
            //                int temp = random.nextInt(100000000);
            //                if (url.indexOf("?") != -1) {
            //                    url = url + "&dmRandom=" + temp;
            //                } else {
            //                    url = url + "?dmRandom=" + temp;
            //                }
            //
            //                if (isSuccess(url)) {
            //                    Log.e("isSuccess", url);
            //                    markerFalg = true;
            //                    if (pd != null && pd.isShowing()) {
            //                        pd.dismiss();
            //                    }
            //                    doFinish(message);
            //                } else {
            //                    Log.e("OverrideUrlLoading", url);
            //                    view.loadUrl(url); //在当前的webview中跳转到新的url
            //                }
            //            }

            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            if (isSuccess(url) && !markerFalg) {
                if (pd != null && pd.isShowing()) {
                    pd.dismiss();
                }
                doFinish(message);
            }

            super.onPageFinished(view, url);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            if (null == pd) {
                pd = ProgressDialog.show(TgThirdWebActivity.this, "", "加载中...");
            }
            super.onPageStarted(view, url, favicon);
        }
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

    /**
     * 获取第三方注册地址
     */
    private void getUrl() {
        HttpUtil.getInstance().post(this, DMConstant.API_Url.PAYUSERREGISTER, new HttpCallBack() {
            @Override
            public void onSuccess(JSONObject result) {
                try {
                    String code = result.getString("code");
                    if (code.equals(DMConstant.ResultCode.SUCCESS)) {
                        String url = result.getString("data");
                        if (!TextUtils.isEmpty(url)) {
                            String cookies = CookieUtil.getmCookie(TgThirdWebActivity.this);
                            synCookies(TgThirdWebActivity.this, url, cookies);
                            webView.loadUrl(url);
                        } else {
                            ToastUtil.getInstant().show(TgThirdWebActivity.this, "未知异常");
                        }
                    } else {
                        ErrorUtil.showError(result);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void showError(String desc) {
        if (pd != null && pd.isShowing()) {
            pd.dismiss();
        }
        String message = title + "失败";
        try {
            message = new String(Base64.decode(desc, Base64.DEFAULT), "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }

        AlertDialogUtil.alert(TgThirdWebActivity.this, message, new AlertListener() {
            @Override
            public void doConfirm() {
                finish();
            }
        });
    }

    private boolean isSuccess(String url) {
        String code = getValByParam(url, "code");
        if ("000000".equals(code)) {
            if (DMConstant.TgWebTitle.BUY_BID.equals(title))  //投标
            {
                AppManager.getAppManager().finishActivity(BuyBidActivity.class);
                AppManager.getAppManager().finishActivity(BidDetailActivity.class);
                //发广播通知投标成功
                Intent intent = new Intent(BidAndCreReceiver.BID_SUCCESS_RECEIVER);
                sendBroadcast(intent);
            } else if (DMConstant.TgWebTitle.BUY_CRE.equals(title))  //债权购买
            {
                AppManager.getAppManager().finishActivity(BuyCreActivity.class);
                AppManager.getAppManager().finishActivity(CreDetailActivity.class);
                //发广播通知转让成功
                Intent intent = new Intent(BidAndCreReceiver.CRE_SUCCESS_RECEIVER);
                sendBroadcast(intent);
            } else if (DMConstant.TgWebTitle.HUAN_KUAN.equals(title) || DMConstant.TgWebTitle.TIQIAN_HUANKUAN.equals
                    (title)) { //还款或提前还款
                //发广播通知转让成功
                Intent intent = new Intent(BidAndCreReceiver.LOAN_SUCCESS_RECEIVER);
                sendBroadcast(intent);
            } else if (DMConstant.TgWebTitle.DONATION_BID.equals(title))  //捐赠
            {
                AppManager.getAppManager().finishActivity(DonationBidActivity.class);
                AppManager.getAppManager().finishActivity(DonationDetailActivity.class);
            } else if (DMConstant.TgWebTitle.WITHDRAW.equals(title))  //提现
            {
                AppManager.getAppManager().finishActivity(WithdrawActivity.class);
            } else if (DMConstant.TgWebTitle.RECHARGE.equals(title))  //充值
            {
                AppManager.getAppManager().finishActivity(TgRechargeActivity.class);
            } else if (DMConstant.TgWebTitle.BIND_CARD.equals(title)) {
                setResult(RESULT_OK); //托管易宝绑卡
                ToastUtil.getInstant().show(this, "绑卡成功");
            }
            return true;
        } else if ("000004".equals(code)) {
            String description = getValByParam(url, "description");
            try {
                description = URLDecoder.decode(description, "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            showError(description);
            return false;
        } else {
            //			Log.e("code", code);
            return false;
        }
    }

    private String getValByParam(String input, String key) {
        String result = null;
        Pattern pattern = Pattern.compile("[\\?\\&]" + key + "=([^\\&]*)\\&?");
        Matcher matcher = pattern.matcher(input);
        while (matcher.find()) {
            String _ins = matcher.group(1);
            if (_ins != null && !_ins.isEmpty()) {
                result = _ins;
            }
        }
        return result;
    }

    private void doFinish(String message) {
        if (pd != null && pd.isShowing()) {
            pd.dismiss();
        }
        if (TextUtils.isEmpty(message)) {
            message = "托管帐号注册成功！";
        }
        if (message.equals("投标成功！") || message.equals("转让成功！")) {
            MainActivity.isNeedEvaluation = "yes";
        }
        AlertDialogUtil.alert(TgThirdWebActivity.this, message, new AlertListener() {
            @Override
            public void doConfirm() {
                //不清楚具体的上一个页面，所以把可能的上次页面一个一个都关掉
                AppManager.getAppManager().finishActivity(UnRegisterTgActivity.class);
                AppManager.getAppManager().finishActivity(TgRechargeActivity.class);
                finish();
            }
        });
    }


    class MyWebChromeClient extends WebChromeClient {

        @Override
        public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
            AlertDialogUtil.alert(TgThirdWebActivity.this, message, new AlertListener() {
                @Override
                public void doConfirm() {
                    result.confirm();
                }
            }).setCancelable(false);
            return true;
        }

        @Override
        public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {
            AlertDialogUtil.confirm(TgThirdWebActivity.this, message, new ConfirmListener() {

                @Override
                public void onOkClick() {
                    result.confirm();
                }

                @Override
                public void onCancelClick() {
                    result.cancel();
                }
            });
            return true;
        }
    }
}
