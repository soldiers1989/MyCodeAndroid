package com.hxjr.p2p.ad5.ui.mine;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.dm.http.DMException;
import com.dm.http.HttpCallBack;
import com.dm.http.HttpUtil;
import com.dm.utils.DMJsonObject;
import com.dm.utils.DMLog;
import com.dm.utils.HtmlUtil;
import com.dm.widgets.TouchWebView;
import com.dm.widgets.utils.AlertDialogUtil;
import com.dm.widgets.utils.ToastUtil;
import com.hxjr.p2p.ad5.DMApplication;
import com.hxjr.p2p.ad5.R;
import com.hxjr.p2p.ad5.bean.AccountBean;
import com.hxjr.p2p.ad5.bean.Chargep;
import com.hxjr.p2p.ad5.bean.Fee;
import com.hxjr.p2p.ad5.bean.UserInfo;
import com.hxjr.p2p.ad5.service.ApiUtil;
import com.hxjr.p2p.ad5.utils.DMConstant;
import com.hxjr.p2p.ad5.utils.ErrorUtil;
import com.hxjr.p2p.ad5.utils.HttpParams;
import com.hxjr.p2p.ad5.utils.NetConnectErrorManager;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by TodayFu Lee on 2017/12/20.
 */

public class YhRechargeFragment extends Fragment implements View.OnClickListener {

    private final static int POST_GET_USERINFO = 0x01;
    private final static int POST_GET_ACCOUNTINFO = 0x02;

    private NetConnectErrorManager netConnectErrorManager;
    private AccountBean accountInfo;
    private TextView available;
    private TextView eleNum;
    private EditText recharge_input_money;
    private EditText bankNum;
    private Button verify_btn;
    private View mView;
    private UserInfo userInfo;
    /**
     * 充值手续费
     */
    private Chargep chargep;

    private TouchWebView warm_tips;

    private TextView warm_title;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        return inflater.inflate(R.layout.yh_recharge_layout, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        ApiUtil.getUserInfo(getActivity());
        initUserInfo();
    }

    private void initView() {
        mView = getView();
        chargep = new Chargep(); //给一个初始值，避免报空指针异常
        available = (TextView) mView.findViewById(R.id.available);
        eleNum = (TextView) mView.findViewById(R.id.eleNum);
        recharge_input_money = (EditText) mView.findViewById(R.id.recharge_input_money);
        bankNum = (EditText) mView.findViewById(R.id.bankNum);
        verify_btn = (Button) mView.findViewById(R.id.verify_btn);
        verify_btn.setOnClickListener(this);
        warm_tips = (TouchWebView) mView.findViewById(R.id.warm_tips);
        warm_tips.getSettings().setLoadWithOverviewMode(true);
        warm_tips.getSettings().setJavaScriptEnabled(true);
        warm_tips.setBackgroundColor(getResources().getColor(R.color.main_bg)); // 设置背景色
        warm_tips.setWebViewClient(new MyWebViewClient());
        warm_tips.getSettings().setUserAgentString("Android/1.0");
        warm_title = (TextView) mView.findViewById(R.id.warm_title);
    }

    private void initUserInfo() {
        post(POST_GET_ACCOUNTINFO, new HttpParams(), DMConstant.API_Url.USER_ACCOUNT);

        post(POST_GET_USERINFO, new HttpParams(), DMConstant.API_Url.USER_USERINFO);

        queryFee();
    }

    private void post(final int postTag, HttpParams params, String url) {
        HttpUtil.getInstance().post(getActivity(), url, params, new HttpCallBack() {
            @Override
            public void onSuccess(JSONObject result) {
                if (netConnectErrorManager != null) {
                    netConnectErrorManager.onNetGood();
                }
                try {
                    String code = result.getString("code");
                    if (code.equals(DMConstant.ResultCode.SUCCESS)) {
                        switch (postTag) {
                            case POST_GET_ACCOUNTINFO:
                                doAfterGetAccountInfo(result);
                                break;
                            case POST_GET_USERINFO:
                                doAfterGetUserInfo(result);
                                break;
                            default:
                                break;
                        }
                    } else {
                        ErrorUtil.showError(result);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable t, Context context) {
                if (postTag == POST_GET_USERINFO) {
                    super.onFailure(t, context);
                }
            }

            @Override
            public void onStart() {
                if (postTag == POST_GET_USERINFO) {
                    setShowProgress(true);
                } else {
                    setShowProgress(false);
                }
            }

            @Override
            public void onConnectFailure(DMException dmE, Context context) {
                if (postTag == POST_GET_USERINFO) {
                    ToastUtil.getInstant().show(getActivity(), dmE.getDescription());
                    if (netConnectErrorManager != null) {
                        netConnectErrorManager.onNetError();
                    }
                }
            }
        });
    }

    /**
     * 获取账户信息
     *
     * @param result
     */
    private void doAfterGetAccountInfo(JSONObject result) {
        try {
            DMLog.i("doAfterGetAccountInfo", result.toString());
            DMJsonObject data = new DMJsonObject(result.getString("data"));
            accountInfo = new AccountBean(data);
            available.setText(accountInfo.getOverAmount());
        } catch (JSONException e) {
            DMLog.e(MyFragment.class.getSimpleName(), e.getMessage());
        }
    }

    /**
     * 获取用户信息
     *
     * @param result
     */
    private void doAfterGetUserInfo(JSONObject result) {
        try {
            DMLog.i("doAfterGetUserInfo", result.toString());
            DMJsonObject data = new DMJsonObject(result.getString("data"));
            userInfo = new UserInfo(data);
            //            available.setText(accountInfo.getOverAmount());
            //            bankNum.setText(bankCard.getBankNumber());
            //            phoneNum.setText(userInfo.getPhoneNumber());
            //            bankNum.setText(userInfo.getUsrCustId());
            if (userInfo.getUsrCustId() != null)
                eleNum.setText(userInfo.getUsrCustId());
            DMApplication.getInstance().setUserInfo(userInfo);

        } catch (JSONException e) {
            DMLog.e(MyFragment.class.getSimpleName(), e.getMessage());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.verify_btn: { //核对转账记录
                verity();
                break;
            }
            default:
                break;
        }
    }

    private void verity() {
        HttpParams params = new HttpParams();
        params.put("amunt", recharge_input_money.getText().toString().trim());
        params.put("cardNum", bankNum.getText().toString().trim());
        HttpUtil.getInstance().post(getActivity(), DMConstant.API_Url.CHARGE_VERIFY, params, new HttpCallBack() {
            @Override
            public void onSuccess(JSONObject result) {
                DMLog.e(result.toString());
                try {
                    String code = result.getString("code");
                    if (DMConstant.ResultCode.SUCCESS.equals(code)) {
                        AlertDialogUtil.alert(getActivity(), "充值成功！", "确认", new
                                AlertDialogUtil
                                        .AlertListener() {
                                    @Override
                                    public void doConfirm() {
                                        getActivity().finish();
                                    }
                                });
                    } else {
                        AlertDialogUtil.alert(getActivity(), result.getString("description"), "确认", new
                                AlertDialogUtil
                                        .AlertListener() {
                                    @Override
                                    public void doConfirm() {
                                        //                                        getActivity().finish();
                                    }
                                });
                    }
                } catch (Exception e) {
                    ToastUtil.getInstant().show(getActivity(), e.toString());
                }
            }
        });
    }

    /**
     * 查询提现手续费和充值手续费
     */
    private void queryFee() {
        ApiUtil.getFee(getActivity(), new ApiUtil.OnPostCallBack() {
            @Override
            public void onSuccess(Fee fee) {
                if (null != fee.getChargep()) {
                    chargep = fee.getChargep();
                }

                if (null != chargep) {
                    warm_title.setVisibility(View.VISIBLE);
                    HtmlUtil htmlUtil = new HtmlUtil();
                    htmlUtil.getHead()
                            .append("<link rel='stylesheet' href='file:///android_asset/css/tips.css' " +
                                    "type='text/css'/>");
                    htmlUtil.getBody().append("<div id='content'>").append(chargep.getCwxts()).append("</div>");
                    warm_tips.loadDataWithBaseURL(null, htmlUtil.CreateHtml(), "text/html", "utf-8", null);
                } else {
                    warm_title.setVisibility(View.GONE);
                    warm_tips.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure() {

            }
        });
    }

    class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            //			view.loadUrl(url); //在当前的webview中跳转到新的url
            DMLog.e(url);
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }
    }
}
