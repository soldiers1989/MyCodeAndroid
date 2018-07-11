package com.hxjr.p2p.ad5.ui.investment.cre;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.dm.http.HttpCallBack;
import com.dm.http.HttpUtil;
import com.dm.utils.DMJsonObject;
import com.dm.widgets.utils.AlertDialogUtil;
import com.dm.widgets.utils.AlertDialogUtil.ConfirmListener;
import com.dm.widgets.utils.ToastUtil;
import com.hxjr.p2p.ad5.DMApplication;
import com.hxjr.p2p.ad5.R;
import com.hxjr.p2p.ad5.bean.AccountBean;
import com.hxjr.p2p.ad5.bean.CreDetailBase;
import com.hxjr.p2p.ad5.bean.Fee;
import com.hxjr.p2p.ad5.service.ApiUtil;
import com.hxjr.p2p.ad5.service.ApiUtil.OnPostCallBack;
import com.hxjr.p2p.ad5.ui.BaseActivity;
import com.hxjr.p2p.ad5.ui.mine.RechargeNewActivity;
import com.hxjr.p2p.ad5.ui.mine.WithdrawWebActivity;
import com.hxjr.p2p.ad5.ui.mine.setting.FindTradePwdActivity;
import com.hxjr.p2p.ad5.ui.mine.user.LoginActivity;
import com.hxjr.p2p.ad5.utils.AgreementManager;
import com.hxjr.p2p.ad5.utils.DMConstant;
import com.hxjr.p2p.ad5.utils.ErrorUtil;
import com.hxjr.p2p.ad5.utils.FormatUtil;
import com.hxjr.p2p.ad5.utils.HttpParams;
import com.hxjr.p2p.ad5.utils.UIHelper;
import com.hxjr.p2p.ad5.utils.UIHelper.OnDealPwdOkListener;

import org.json.JSONException;
import org.json.JSONObject;

public class BuyCreActivity extends BaseActivity {
    private TextView turn_ammount_tv;

    /**
     * 账户余额
     */
    private TextView account_available_tv;

    /**
     * 购买金额
     */
    private TextView buy_ammount_tv;

    /**
     * 预期收益
     */
    private TextView expect_income_tv;

    private Button pay_immediately_btn;

    private CreDetailBase base;

    private String creditorId;

    private AccountBean accountInfo;

    private Context mContext;

    /**
     * 协议管理类
     */
    private AgreementManager agreementManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buy_cre);
        mContext = this;
        initView();
        initData();
    }

    @Override
    protected void initView() {
        super.initView();
        ((TextView) findViewById(R.id.title_text)).setText(R.string.creditor_buy);
        turn_ammount_tv = (TextView) findViewById(R.id.turn_ammount_tv);
        account_available_tv = (TextView) findViewById(R.id.account_available_tv);
        buy_ammount_tv = (TextView) findViewById(R.id.buy_ammount_tv);
        expect_income_tv = (TextView) findViewById(R.id.expect_income_tv);
        pay_immediately_btn = (Button) findViewById(R.id.pay_immediately_btn);
        pay_immediately_btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                payImmediate();
            }
        });
    }

    private void initData() {
        base = (CreDetailBase) getIntent().getSerializableExtra("base");
        creditorId = getIntent().getStringExtra("creditorId");
        String creditorVal = base.getCreditorVal();
        String salePrice = base.getSalePrice();
        String expectIncome = FormatUtil.formatStr2(Double.parseDouble(creditorVal) - Double.parseDouble(salePrice) +
                "");
        turn_ammount_tv.setText(salePrice);

        String terms = getString(R.string.buy_ammount_amount, salePrice);
        buy_ammount_tv.setText(UIHelper.makeSpannableStr(mContext,
                terms,
                terms.length() - 1 - (salePrice + "").length(),
                terms.length() - 1));

        terms = getString(R.string.expect_income_amount, expectIncome);
        expect_income_tv.setText(UIHelper.makeSpannableStr(mContext,
                terms,
                terms.length() - 1 - expectIncome.length(),
                terms.length() - 1));
        agreementManager = new AgreementManager(this, AgreementManager.TYPE_ZQ, base.getIsDanBao());
        agreementManager.initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getAccountInfo();
        ApiUtil.getUserInfo(this);
        queryFee();
        if (agreementManager != null) {
            agreementManager.initData();
        }
    }

    /**
     * 请求用户账号信息
     */
    private void getAccountInfo() {
        HttpUtil.getInstance().post(BuyCreActivity.this, DMConstant.API_Url.USER_ACCOUNT, new HttpCallBack() {
            @Override
            public void onSuccess(JSONObject result) {
                try {
                    String code = result.getString("code");
                    if (code.equals(DMConstant.ResultCode.SUCCESS)) {
                        DMJsonObject data = new DMJsonObject(result.getString("data"));
                        accountInfo = new AccountBean(data);
                        String terms =
                                getString(R.string.bid_buy_user_amount, FormatUtil.formatStr2(accountInfo
										.getOverAmount()));
                        account_available_tv.setText(UIHelper.makeSpannableStr(mContext, terms, terms.length() - 1
                                - accountInfo.getOverAmount().length(), terms.length() - 1));
                    } else {
                        ToastUtil.getInstant().show(BuyCreActivity.this, result.getString("description"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable t, Context context) {
                super.onFailure(t, context);
            }

            @Override
            public void onStart() {
                super.onStart();
            }
        });
    }

    /**
     * 购买债券是否需要交易密码
     */
    private boolean isNeedPsw;

    /***
     * 是否必须完成邮箱认证
     */
    private boolean isNeedEmailRZ;

    /**
     * 查询是否需要设置交易密码
     */
    private void queryFee() {
        ApiUtil.getFee(this, new OnPostCallBack() {
            @Override
            public void onSuccess(Fee fee) {
                isNeedPsw = fee.getChargep().isNeedPsd();
                isNeedEmailRZ = fee.getBaseInfo().isNeedEmailRZ();
            }

            @Override
            public void onFailure() {
                isNeedPsw = true;
                isNeedEmailRZ = true;
            }
        });
    }

    /***
     * 马上支付
     */
    protected void payImmediate() {
        if (checkInfo()) {
            if (isNeedPsw) {
                UIHelper.showPayPwdEditDialog(this, null, new OnDealPwdOkListener() {
                    @Override
                    public void onDealPwdOk(String dealPassword) {
                        pay(dealPassword);
                    }
                });
            } else {//如果不需要交易密码，那么就不用弹框输入交易密码
                pay(null);
            }
        }
    }

    private void pay(String dealPassword) {
        HttpParams params = new HttpParams();
        if (!TextUtils.isEmpty(dealPassword)) {
            params.put("tranPwd", dealPassword);
        }
        params.put("creditorId", creditorId);
        HttpUtil.getInstance().post(BuyCreActivity.this,
                DMConstant.API_Url.CREDITOR_BUYCREDITOR,
                params,
                new HttpCallBack() {
                    @Override
                    public void onSuccess(JSONObject result) {
                        try {
                            String code = result.getString("code");
                            if (code.equals(DMConstant.ResultCode.SUCCESS)) {
                                //							if (DMApplication.getInstance().getUserInfo().isTg()
                                //								)
                                //							{
                                String url = result.getJSONObject("data").getString("url").toString();
                                Intent intent = new Intent(mContext, WithdrawWebActivity.class);
                                intent.putExtra("linkUrl", url);
                                intent.putExtra("message", "债权购买成功！");
                                intent.putExtra("title", DMConstant.TgWebTitle.BUY_CRE);
                                startActivity(intent);
                                BuyCreActivity.this.finish();
                                //							}
                                //							else
                                //							{
                                //								AlertDialogUtil.alert(mContext, "购买成功", new
								// AlertListener()
                                //								{
                                //									@Override
                                //									public void doConfirm()
                                //									{
                                //										sendBroadcast(new Intent(BidAndCreReceiver
								// .CRE_SUCCESS_RECEIVER).putExtra("creId",
                                //											creditorId));
                                //										AppManager.getAppManager().finishActivity
								// (CreDetailActivity.class);
                                //										finish();
                                //									}
                                //								});
                                //							}
                            } else if (code.equals(ErrorUtil.ErroreCode.ERROR_000044)) {
                                String description = result.getString("description");
                                if (null != description && description.contains("交易密码")) {
                                    dealPwdError();
                                } else {
                                    AlertDialogUtil.alert(mContext, FormatUtil.Html2Text(description));
                                }
                            } else if (DMApplication.getInstance().getUserInfo().isTg()
                                    && ErrorUtil.ErroreCode.ERROR_000047.equals(code)) {
                                //							final String url = result.getJSONObject("data").getString
								// ("url").toString();
                                //							AlertDialogUtil.confirm(BuyCreActivity.this,
                                //								result.getString("description"),
                                //								"去授权",
                                //								null,
                                //								new ConfirmListener()
                                //								{
                                //
                                //									@Override
                                //									public void onOkClick()
                                //									{
                                //										Intent intent = new Intent(BuyCreActivity.this, TgThirdWebActivity.class);
                                //										intent.putExtra("url", url);
                                //										intent.putExtra("message", "授权成功！");
                                //										intent.putExtra("title", DMConstant.TgWebTitle.SOUQUAN);
                                //										startActivity(intent);
                                //									}
                                //
                                //									@Override
                                //									public void onCancelClick()
                                //									{
                                //									}
                                //								});
                            } else {
                                //									if (code.equals("000001"))
                                //									{
                                //										dealPwdError();
                                //									}
                                ToastUtil.getInstant().show(BuyCreActivity.this, result.getString("description"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Throwable t, Context context) {
                        super.onFailure(t, context);
                    }
                });
    }

    /**
     * 交易密码错误
     */
    protected void dealPwdError() {
        //弹出找回交易密码提示框
        AlertDialogUtil.confirm(this, getString(R.string.deal_pwd_err), "找回交易密码", "确定", new ConfirmListener() {
            @Override
            public void onOkClick() {
                startActivity(new Intent(BuyCreActivity.this, FindTradePwdActivity.class));
            }

            @Override
            public void onCancelClick() {
            }
        });
    }

    /***
     * 检测个人信息是否已完善
     * @return
     */
    private boolean checkInfo() {
        if (null == accountInfo) {
            ToastUtil.getInstant().show(this, "请检查用户信息");
            return false;
        }
        if (!UIHelper.hasCompletedSecurityInfo(this, DMApplication.getInstance().getUserInfo(), isNeedEmailRZ, isNeedPsw)) {//查看是否完成了信息安全认证
            return false;
        }
        if (Double.parseDouble(accountInfo.getOverAmount()) < Double.parseDouble(base.getSalePrice())) {
            if (null == DMApplication.getInstance().getUserInfo()) {
                startActivity(new Intent(mContext, LoginActivity.class));
                return false;
            }
            AlertDialogUtil.confirm(this, getString(R.string.available_ammount_not_enough), "充值", "确定", new ConfirmListener() {
                @Override
                public void onOkClick() {
                    startActivity(new Intent(BuyCreActivity.this, RechargeNewActivity.class));
                }

                @Override
                public void onCancelClick() {
                }
            });
            return false;
        }
        if (agreementManager != null && !agreementManager.isChecked()) {
            AlertDialogUtil.alert(BuyCreActivity.this, "请先阅读债权转让协议并勾选！").setCanceledOnTouchOutside(false);
            return false;
        }
        return true;
    }
}
