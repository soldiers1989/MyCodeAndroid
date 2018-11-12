package com.hxjr.p2p.ad5.ui.mine;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.dm.android.pay.PaymentType;
import com.dm.http.DMException;
import com.dm.http.HttpCallBack;
import com.dm.http.HttpUtil;
import com.dm.utils.DMJsonObject;
import com.dm.utils.DMLog;
import com.dm.utils.HtmlUtil;
import com.dm.utils.StringUtils;
import com.dm.widgets.TouchWebView;
import com.dm.widgets.utils.AlertDialogUtil;
import com.dm.widgets.utils.ToastUtil;
import com.hxjr.p2p.ad5.DMApplication;
import com.hxjr.p2p.ad5.R;
import com.hxjr.p2p.ad5.bean.AccountBean;
import com.hxjr.p2p.ad5.bean.BankCard;
import com.hxjr.p2p.ad5.bean.Chargep;
import com.hxjr.p2p.ad5.bean.Fee;
import com.hxjr.p2p.ad5.bean.UserInfo;
import com.hxjr.p2p.ad5.service.ApiUtil;
import com.hxjr.p2p.ad5.ui.mine.bank.BankCardManageActivity;
import com.hxjr.p2p.ad5.ui.mine.bank.BankCardWebActivity;
import com.hxjr.p2p.ad5.ui.mine.setting.SecurityInfoActivity;
import com.hxjr.p2p.ad5.ui.mine.setting.TradePwdActivity;
import com.hxjr.p2p.ad5.utils.DMConstant;
import com.hxjr.p2p.ad5.utils.ErrorUtil;
import com.hxjr.p2p.ad5.utils.FormatUtil;
import com.hxjr.p2p.ad5.utils.HttpParams;
import com.hxjr.p2p.ad5.utils.NetConnectErrorManager;
import com.hxjr.p2p.ad5.utils.UIHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

/**
 * Created by TodayFu Lee on 2017/12/20.
 */

public class ZxRechargeFragment extends Fragment implements View.OnClickListener {

    private final static int POST_GET_USERINFO = 0x01;

    private final static int POST_GET_ACCOUNTINFO = 0x02;

    private NetConnectErrorManager netConnectErrorManager;

    private boolean isdowning;

    /**
     * 手机验证码倒计时
     */
    private CountDownTimer phoneDownTimer;

    private EditText amountEdit;

    private TextView available;

    private TextView phoneNum;

    private TextView bankNum;

    private TextView recharge_fee;

    private TextView recharge_income;

    private Button btnRecharge;

    private Button get_code_btn;

    private EditText vercodeEdit;

    private AccountBean accountInfo;

    private String sms_seq;

    /**
     * 倒计时所剩时间
     */
    private long remainTime = 120000L;

    /**
     * 最后停止时间
     */
    private long lastTime;

    private boolean isCilckGetCode = false;

    /**
     * 充值手续费
     */
    private Chargep chargep;

    /**
     * 是否需要邮箱认证
     */
    private boolean isNeedEmailRZ;

    //	private String netGate = "BAOFU";

    private PaymentType paymentType;

    private UserInfo userInfo;

    private BankCard bankCard;

    private TouchWebView warm_tips;

    private TextView warm_title;

    private View mView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        return inflater.inflate(R.layout.recharge_layout, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        chargep = new Chargep(); //给一个初始值，避免报空指针异常
        initView();
        sms_seq = "";
        postData();
        initListener();
    }

    protected void initView() {
        mView = getView();
        amountEdit = (EditText) mView.findViewById(R.id.amountEdit);
        available = (TextView) mView.findViewById(R.id.available);
        vercodeEdit = (EditText) mView.findViewById(R.id.vercodeEdit);
        bankNum = (TextView) mView.findViewById(R.id.bankNum);
        phoneNum = (TextView) mView.findViewById(R.id.phoneNum);
        recharge_fee = (TextView) mView.findViewById(R.id.recharge_fee);
        recharge_income = (TextView) mView.findViewById(R.id.recharge_income);
        get_code_btn = (Button) mView.findViewById(R.id.reget_code_btn);
        btnRecharge = (Button) mView.findViewById(R.id.btnRecharge);
        warm_tips = (TouchWebView) mView.findViewById(R.id.warm_tips);
        warm_tips.getSettings().setLoadWithOverviewMode(true);
        warm_tips.getSettings().setJavaScriptEnabled(true);
        warm_tips.setBackgroundColor(getResources().getColor(R.color.main_bg)); // 设置背景色
        warm_tips.setWebViewClient(new MyWebViewClient());
        warm_tips.getSettings().setUserAgentString("Android/1.0");
        warm_title = (TextView) mView.findViewById(R.id.warm_title);
    }

    /**
     * 获取账户与用户信息
     */
    private void postData() {
        post(POST_GET_ACCOUNTINFO, new HttpParams(), DMConstant.API_Url.USER_ACCOUNT);

        post(POST_GET_USERINFO, new HttpParams(), DMConstant.API_Url.USER_USERINFO);

        getBankCardDatas();
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
                                queryFee();
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
     * 请求数据
     */
    private void getBankCardDatas() {
        HttpUtil.getInstance().post(getActivity(), DMConstant.API_Url.USER_MYBANKLIST, new HttpCallBack() {
            @Override
            public void onSuccess(JSONObject result) {
                try {
                    String code = result.getString("code");
                    if (DMConstant.ResultCode.SUCCESS.equals(code)) {
                        // 成功
                        JSONObject data0 = result.getJSONObject("data");
                        JSONArray data = data0.getJSONArray("myBankList");
                        int currentCards = data.length();
                        if (currentCards == 0) {
                            //未注册第三方操作
                            AlertDialogUtil.confirm(getActivity(), "您未绑定银行卡，请点击确认进行银行卡绑定", new
                                    AlertDialogUtil
                                            .ConfirmListener() {

                                        @Override
                                        public void onOkClick() {
                                            if (UIHelper.hasCompletedSecurityInfo(getActivity(), userInfo,
                                                    isNeedEmailRZ,
                                                    chargep.isNeedPsd())) {
                                                Intent intent = new Intent(getActivity(),
                                                        BankCardManageActivity.class);
                                                startActivityForResult(intent, DMConstant.RequestCodes
                                                        .REQUEST_CODE_SECURITY);
                                            }
                                        }

                                        @Override
                                        public void onCancelClick() {

                                        }
                                    });
                        }
                        if (currentCards > 0) {
                            DMJsonObject dmJsonObject = new DMJsonObject(data.get(0).toString());
                            bankCard = new BankCard(dmJsonObject);
                            bankNum.setText(bankCard.getBankNumber());
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

    private void initListener() {
        amountEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().startsWith(".") || s.toString().startsWith("0")) // 不能以“.”或“0”开头
                {
                    amountEdit.setText("");
                    return;
                }
                if (!s.toString().equals("")) {
                    makeFee(Double.parseDouble(s.toString()));
                } else {
                    recharge_fee.setText("0");
                    recharge_income.setText("0");
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        btnRecharge.setOnClickListener(this);
        get_code_btn.setOnClickListener(this);

    }

    /**
     * 检查用户信息以及用户输入的一些参数
     *
     * @return
     */
    private boolean checkPrams() {
        userInfo = DMApplication.getInstance().getUserInfo();
        if (null != userInfo) { //是否需要实名认证, 手机认证, 邮箱认证, 交易密码
            if ((StringUtils.isEmptyOrNull(userInfo.getRealName()) && chargep.isNeedNciic())
                    || (StringUtils.isEmptyOrNull(userInfo.getPhone()) && chargep.isNeedPhone())
                    || (StringUtils.isEmptyOrNull(userInfo.getEmail()) && chargep.isNeedEmail())
                    || (!userInfo.isWithdrawPsw() && chargep.isNeedPsd())) {
                String message = getSecurityStr();
                AlertDialogUtil.confirm(getActivity(), message, "认证", "确定", new AlertDialogUtil.ConfirmListener() {

                    @Override
                    public void onOkClick() {
                        startActivity(new Intent(getActivity(), SecurityInfoActivity.class).putExtra
                                ("isNeedPwd",
                                        chargep.isNeedPsd()));
                    }

                    @Override
                    public void onCancelClick() {
                    }
                });
                return false;
            }
            if (userInfo.isWithdrawPsw()) {
            } else {
                AlertDialogUtil.alert(getActivity(), "您未设置交易密码，请设置交易密码", "确认", new AlertDialogUtil
                        .AlertListener() {
                    @Override
                    public void doConfirm() {
                        startActivity(new Intent(getActivity(), TradePwdActivity.class));
                    }
                });
            }
        }
        if (amountEdit.getText().toString().isEmpty()) {
            ToastUtil.getInstant().show(getActivity(), "充值金额不能为空");
            return false;
        }
        if (vercodeEdit.getText().toString().isEmpty()) {
            ToastUtil.getInstant().show(getActivity(), "请输入验证码！");
            return false;
        }
        if (sms_seq.equals("")) {
            ToastUtil.getInstant().show(getActivity(), "请重新获取验证码！");
            return false;
        }
        if (amountEdit.getText().toString().isEmpty()) {
            ToastUtil.getInstant().show(getActivity(), "请输入充值金额！");
            return false;
        }
        if (Double.parseDouble(amountEdit.getText().toString()) < chargep.getMin()) {
            ToastUtil.getInstant().show(getActivity(), "充值金额最小为：" + FormatUtil.formatStr2(chargep.getMin() + "") + "元");
            return false;
        }
        if (null == userInfo.getUsrCustId()) {
            AlertDialogUtil.confirm(getActivity(), "您未绑定银行卡，请点击确认进行银行卡绑定", new AlertDialogUtil.ConfirmListener() {

                @Override
                public void onOkClick() {
                    if (UIHelper.hasCompletedSecurityInfo(getActivity(), userInfo, isNeedEmailRZ,
                            chargep.isNeedPsd())) {
                        Intent intent = new Intent(getActivity(), BankCardManageActivity.class);
                        startActivityForResult(intent, DMConstant.RequestCodes.REQUEST_CODE_SECURITY);
                    }
                }

                @Override
                public void onCancelClick() {

                }
            });
            return false;
        }
        if (null == bankCard) {
            AlertDialogUtil.confirm(getActivity(), "您未绑定银行卡，请点击确认进行银行卡绑定", new AlertDialogUtil.ConfirmListener() {

                @Override
                public void onOkClick() {
                    if (UIHelper.hasCompletedSecurityInfo(getActivity(), userInfo, isNeedEmailRZ,
                            chargep.isNeedPsd())) {
                        Intent intent = new Intent(getActivity(), BankCardManageActivity.class);
                        startActivityForResult(intent, DMConstant.RequestCodes.REQUEST_CODE_SECURITY);
                    }
                }

                @Override
                public void onCancelClick() {

                }
            });
            return false;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnRecharge: { //充值
                recharge("");
                break;
            }
            case R.id.reget_code_btn: {
                DMLog.e("SDFJSLKDLFKJSLDKFDDSDFS");
                getVerifyCode();
                break;
            }
            default:
                break;
        }
    }

    /***
     * 获取用户安全信息完成的情况
     * @return
     */
    public String getSecurityStr() {
        String securityInfo = "您必须先完成";
        if ((StringUtils.isEmptyOrNull(userInfo.getRealName()) && chargep.isNeedNciic()))
        {//为完成实名认证，并且真实名字为空，则表示没有完成实名认证
            securityInfo = securityInfo + "实名认证、";
        }
        if ((StringUtils.isEmptyOrNull(userInfo.getPhone()) && chargep.isNeedPhone())) {
            securityInfo = securityInfo + "手机号认证、";
        }
        if ((StringUtils.isEmptyOrNull(userInfo.getEmail()) && chargep.isNeedEmail())) {
            securityInfo = securityInfo + "邮箱认证、";
        }
        if ((!userInfo.isWithdrawPsw() && chargep.isNeedPsd())) {
            securityInfo = securityInfo + "设置交易密码、";
        }
        securityInfo = securityInfo.substring(0, securityInfo.length() - 1) + "！";
        return securityInfo;
    }

    /**
     * 充值操作
     */
    private void recharge(String cardId) {
        if (checkPrams()) {
            //            final PaymentType mPaymentType = paymentType;
            HttpParams params = new HttpParams();
            params.put("sms_seq", sms_seq);
            params.put("sendCode", vercodeEdit.getText().toString());
            params.put("amount", amountEdit.getText().toString());
            btnRecharge.setClickable(false);
            vercodeEdit.setText("");
            HttpUtil.getInstance().post(getActivity(), DMConstant.API_Url.PAY_CHARGE, params, new HttpCallBack() {
                @Override
                public void onSuccess(JSONObject result) {
                    try {
                        DMLog.e(result.toString());
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
                            btnRecharge.setClickable(true);
                        } else {
                            ErrorUtil.showError(result);
                            btnRecharge.setClickable(true);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Throwable t, Context context) {

                    ToastUtil.getInstant().show(getActivity(), "充值失败请稍后尝试！");
                }

                @Override
                public void onLoading(Integer integer) {

                }

                @Override
                public void onStart() {
                    //                    (BaseActivity)getActivity().show("充值中，请稍候...");
                }

            });
        }

    }

    /**
     * 计算手续费
     *
     * @param amount
     */
    private void makeFee(double amount) {
        if (amount < chargep.getMin()) {
            recharge_fee.setText("0.00");
            recharge_income.setText(FormatUtil.formatStr2(amount + ""));
            return;
        }
        if (amount > chargep.getMax()) {
            ToastUtil.getInstant().show(getActivity(), "充值最大金额为：" + FormatUtil.formatStr2(chargep.getMax() + "") + "元");
            String temp = (int) chargep.getMax() + "";
            amountEdit.setText(temp);
            amountEdit.setSelection(temp.length());
            return;
        }
        double fee = amount * chargep.getP();
        DMLog.e(chargep.getP() + "");
        DMLog.e(chargep.getpMax() + "");
        fee = fee < 1 ? fee : 1;
        if (fee > chargep.getpMax()) {
            fee = chargep.getpMax();
        }
        double result = amount - fee;
        DecimalFormat df = new DecimalFormat("#0.00");
        recharge_fee.setText(df.format(fee));
        recharge_income.setText(df.format(result));
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
                    HtmlUtil htmlUtil = new HtmlUtil();
                    htmlUtil.getHead()
                            .append("<link rel='stylesheet' href='file:///android_asset/css/tips.css' " +
                                    "type='text/css'/>");
                    htmlUtil.getBody().append("<div id='content'>").append(chargep.getCwxts()).append("</div>");
                    warm_tips.loadDataWithBaseURL(null, htmlUtil.CreateHtml(), "text/html",
                            "utf-8", null);
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

    /**
     * 开始倒计时
     *
     * @param time
     */
    private void startDownTimer(long time) {
        phoneDownTimer = new CountDownTimer(time, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                isdowning = true;
                int temp = (int) (millisUntilFinished / 1000);
                if (isAdded()) {
                    get_code_btn.setText(getString(R.string.find_pwd_wait_time, temp));
                    get_code_btn.setEnabled(false);
                }
            }

            @Override
            public void onFinish() {
                isdowning = false;
                if (isAdded()) {
                    get_code_btn.setEnabled(true);
                    get_code_btn.setText(getString(R.string.get_verify_code));
                }
            }
        }.start();
    }

    /**
     * 获手机取验证码
     *
     * @param
     */
    public void getVerifyCode() {
        if (null != bankCard) {
            isCilckGetCode = true;
            // 发送验证码到手机
            HttpParams httpParams = new HttpParams();
            HttpUtil.getInstance().post(getActivity(), DMConstant.API_Url.RECHARGEVERIFYCODE, httpParams, new
                    HttpCallBack
                            () {
                        @Override
                        public void onSuccess(JSONObject result) {
                            try {
                                String code = result.getString("code");

                                if (DMConstant.ResultCode.SUCCESS.equals(code)) {
                                    startDownTimer(120000L);
                                    // 下发验证码成功
                                    AlertDialogUtil.alert(getActivity(), getString(R.string
                                            .find_pwd_send_success))
                                            .setCanceledOnTouchOutside(false);
                                    if (!"".equals(result.getString("data"))) {
                                        DMJsonObject data = new DMJsonObject(result.getString("data"));
                                        sms_seq = data.getString("sms_seq");
                                    } else {
                                        //									userId = phoneNum;
                                    }
                                } else {
                                    //下发验证码失败
                                    AlertDialogUtil.alert(getActivity(), result.getString("description"))
                                            .setCanceledOnTouchOutside(false);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                    });
        } else {
            AlertDialogUtil.confirm(getActivity(), "您未绑定银行卡，请点击确认进行银行卡绑定", new
                    AlertDialogUtil
                            .ConfirmListener() {

                        @Override
                        public void onOkClick() {
                            if (UIHelper.hasCompletedSecurityInfo(getActivity(), userInfo,
                                    isNeedEmailRZ,
                                    chargep.isNeedPsd())) {
                                Intent intent = new Intent(getActivity(),
                                        BankCardWebActivity.class);
                                startActivityForResult(intent, DMConstant.RequestCodes
                                        .REQUEST_CODE_SECURITY);
                            }
                        }

                        @Override
                        public void onCancelClick() {

                        }
                    });
        }
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
            phoneNum.setText(userInfo.getPhoneNumber());
            //            bankNum.setText(userInfo.getUsrCustId());
            DMApplication.getInstance().setUserInfo(userInfo);
        } catch (JSONException e) {
            DMLog.e(MyFragment.class.getSimpleName(), e.getMessage());
        }
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
