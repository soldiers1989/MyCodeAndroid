package com.hxjr.p2p.ad5.ui.mine;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.dm.http.DMException;
import com.dm.http.HttpCallBack;
import com.dm.http.HttpUtil;
import com.dm.utils.DMJsonObject;
import com.dm.utils.DMLog;
import com.dm.utils.DensityUtil;
import com.dm.utils.StringUtils;
import com.dm.widgets.BadgeView;
import com.dm.widgets.DMSwipeRefreshLayout;
import com.dm.widgets.utils.AlertDialogUtil;
import com.dm.widgets.utils.AlertDialogUtil.ConfirmListener;
import com.dm.widgets.utils.ToastUtil;
import com.hxjr.p2p.ad5.DMApplication;
import com.hxjr.p2p.ad5.R;
import com.hxjr.p2p.ad5.bean.AccountBean;
import com.hxjr.p2p.ad5.bean.Fee;
import com.hxjr.p2p.ad5.bean.UserInfo;
import com.hxjr.p2p.ad5.service.ApiUtil;
import com.hxjr.p2p.ad5.service.ApiUtil.OnPostCallBack;
import com.hxjr.p2p.ad5.ui.MainActivity;
import com.hxjr.p2p.ad5.ui.mine.bank.BankCardManageActivity;
import com.hxjr.p2p.ad5.ui.mine.creditor.CreditorActivity;
import com.hxjr.p2p.ad5.ui.mine.evaluation.EvaluationActivity;
import com.hxjr.p2p.ad5.ui.mine.invest.MyInvestMentActivity;
import com.hxjr.p2p.ad5.ui.mine.letter.LetterInStationActivity;
import com.hxjr.p2p.ad5.ui.mine.loan.MyLoanActivity;
import com.hxjr.p2p.ad5.ui.mine.reward.MyRewardActivity;
import com.hxjr.p2p.ad5.ui.mine.setting.SecurityInfoActivity;
import com.hxjr.p2p.ad5.ui.mine.setting.SettingActivity;
import com.hxjr.p2p.ad5.ui.mine.setting.TradePwdActivity;
import com.hxjr.p2p.ad5.ui.mine.trade.TradeRecordActivity;
import com.hxjr.p2p.ad5.utils.AmountUtil;
import com.hxjr.p2p.ad5.utils.DMConstant;
import com.hxjr.p2p.ad5.utils.ErrorUtil;
import com.hxjr.p2p.ad5.utils.HttpParams;
import com.hxjr.p2p.ad5.utils.NetConnectErrorManager;
import com.hxjr.p2p.ad5.utils.NetConnectErrorManager.NetConnetCallBack;
import com.hxjr.p2p.ad5.utils.SharedPreferenceUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 我的
 *
 * @author jiaohongyun
 * @date 2015年10月19日
 */
public class MyFragment extends Fragment implements OnRefreshListener, OnClickListener {
    private final static int POST_GET_USERINFO = 0x01;

    private final static int POST_GET_ACCOUNTINFO = 0x02;

    private View mView;

    private ScrollView mScrollview;

    private DMSwipeRefreshLayout mSwipeLayout;

    private LinearLayout assets_ll;// 资产显示或隐藏

    private ImageButton img;

    private boolean isShow; // 判断资产是否显示

    private ImageView iconUser;

    private ImageView iconPhone;

    private ImageView iconEmail;

    private ImageView icon_trad_pwd;

    private UserInfo userInfo;// 用户信息

    private AccountBean accountInfo;// 账户信息

    private TextView accountName; //用户名

    private TextView incomeInInvest;//投资总的收益

    private TextView moneyInInvest; //投资中的金额

    private TextView totalAssetInvest; //投资总资产

    private TextView frezzMoneyInvest; //冻结金额

    private TextView availableBalance; //可用余额

    private TextView loanLiabilities; //借款负债

    private TextView letterInStation;// 站内信息

    private TextView evaluation_result;

    private BadgeView badger;

    private View redText;

    private View letter_station_lv;

    private View account_risk_evaluation_lv;

    /**
     * 是否可以执行动画
     */
    private boolean canRunFlag = true;

    /**
     * 动画区域最终高度
     */
    private int assets_ll_height;

    /**
     * 用于动画展开时最后一次高度变化的比较
     */
    private int maxHeight;

    /**
     * 无网络提示
     */
    private NetConnectErrorManager netConnectErrorManager;

    /**
     * 是否需要交易密码
     */
    private boolean isNeedPwd = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.main_page_mine, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        initListeners();
        userInfo = DMApplication.getInstance().getUserInfo();
        initUserInfo();

    }

    @Override
    public void onResume() {
        super.onResume();
        View view = mView.findViewById(R.id.main_title);
        View statusBar = null;
        if (view != null) {
            statusBar = view.findViewById(R.id.statusBar);
        }
        if (statusBar != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                statusBar.setVisibility(View.VISIBLE);
            } else {
                statusBar.setVisibility(View.GONE);
            }
        }
        if (DMApplication.getInstance().islogined()) {
            postData();
            mScrollview.scrollTo(0, 0);
        }


        userInfo = DMApplication.getInstance().getUserInfo();
        initUserInfo();
    }

    private void synchronizationBalance() {
        HttpParams params = new HttpParams();
        HttpUtil.getInstance().post(getActivity(), DMConstant.API_Url.SYNCHRONIZATIONBALANCE, params, new
                HttpCallBack() {
                    @Override
                    public void onSuccess(JSONObject result) {
                        try {
                            String code = result.getString("code");
                            if (DMConstant.ResultCode.SUCCESS.equals(code)) {
                                DMLog.e(result.toString());
                            } else {
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Throwable t, Context context) {
                        //				super.onFailure(t, context);
                    }
                });
    }

    private void showSecurityAlert() {
        if (null != userInfo && MainActivity.index == 3) {
            boolean hasShowSecurityDialog = (Boolean) SharedPreferenceUtils.get(getActivity(),
                    "hasShowSecurityDialog", false);
            //如果还没有弹出完成安全信息认证的提示框，并且还没有完成安全信息认证，则弹出提示框
            if (!hasShowSecurityDialog && (StringUtils.isEmptyOrNull(userInfo.getRealName())
                    || StringUtils.isEmptyOrNull(userInfo.getPhone()) || StringUtils.isEmptyOrNull(userInfo.getEmail
                    ()))) {
                SharedPreferenceUtils.put(getActivity(), "hasShowSecurityDialog", true);
                AlertDialogUtil.confirm(getActivity(), "注册成功，为了您的资金安全保障，请完善安全信息！", "马上完善", "先逛逛", new ConfirmListener
                        () {
                    @Override
                    public void onOkClick() {
                        startActivity(new Intent(getActivity(), SecurityInfoActivity.class).putExtra("isNeedPwd",
                                isNeedPwd));
                    }

                    @Override
                    public void onCancelClick() {
                    }
                });
            }
        }
    }

    private void initView() {
        mView = getView();
        mView.findViewById(R.id.btn_back).setVisibility(View.GONE);
        ((TextView) mView.findViewById(R.id.title_text)).setText(R.string.page_title_mine);
        redText = mView.findViewById(R.id.red_text);
        mScrollview = (ScrollView) mView.findViewById(R.id.my_fragment_scrollview);
        mSwipeLayout = (DMSwipeRefreshLayout) mView.findViewById(R.id.id_swipe_ly);
        mSwipeLayout.setOnRefreshListener(this);
        assets_ll = (LinearLayout) mView.findViewById(R.id.assets_ll);
        img = (ImageButton) mView.findViewById(R.id.img);
        accountName = (TextView) mView.findViewById(R.id.account_name);
        letterInStation = (TextView) mView.findViewById(R.id.account_letter_in_station_tv);
        letter_station_lv = mView.findViewById(R.id.account_letter_in_station_lv);
        account_risk_evaluation_lv = mView.findViewById(R.id.account_risk_evaluation_lv);
        evaluation_result = (TextView) mView.findViewById(R.id.evaluation_result);
        iconUser = (ImageView) mView.findViewById(R.id.icon_user);
        iconPhone = (ImageView) mView.findViewById(R.id.icon_phone);
        iconEmail = (ImageView) mView.findViewById(R.id.icon_email);
        icon_trad_pwd = (ImageView) mView.findViewById(R.id.icon_trad_pwd);
        incomeInInvest = (TextView) mView.findViewById(R.id.income_in_invest);
        moneyInInvest = (TextView) mView.findViewById(R.id.money_in_invest);
        totalAssetInvest = (TextView) mView.findViewById(R.id.total_asset_in_invest);
        frezzMoneyInvest = (TextView) mView.findViewById(R.id.freeze_money_in_invest);
        availableBalance = (TextView) mView.findViewById(R.id.available_balance_in_invest);
        loanLiabilities = (TextView) mView.findViewById(R.id.loan_liabilities_in_invest);
        isShow = false;
        assets_ll_height = DensityUtil.dip2px(getActivity(), 125);
        maxHeight = assets_ll_height - 10;
        badger = new BadgeView(getActivity(), mView.findViewById(R.id.red_text));
    }

    /**
     * 给视图填充账号信息
     */
    private void initAccountInfo() {
        if (accountInfo != null) {
            incomeInInvest.setText(AmountUtil.numKbPointFormat(accountInfo.getEarnAmount())); // 投资总收益
            moneyInInvest.setText(AmountUtil.numKbPointFormat(Double.valueOf(accountInfo.getInvestAmount())));// 投资中金额
            //			totalAssetInvest.setText(AmountUtil.numKbPointFormat(Double.valueOf(accountInfo.getTotalAmount
            // ()))); // 总资产
            totalAssetInvest.setText(AmountUtil.numKbPointFormat(Double.valueOf(accountInfo.getMerelyAmount()))); // 净资产
            frezzMoneyInvest.setText(AmountUtil.numKbPointFormat(Double.valueOf(accountInfo.getFreezeAmount()))); //
            // 冻结金额
            availableBalance.setText(AmountUtil.numKbPointFormat(Double.valueOf(accountInfo.getOverAmount()))); // 可用余额
            loanLiabilities.setText(AmountUtil.numKbPointFormat(Double.valueOf(accountInfo.getLoanAmount()))); // 借款负债
        }
    }

    /**
     * 给视图填充用户信息
     */
    private void initUserInfo() {
        badger.setGravity(Gravity.CENTER);
        badger.setTextSize(10);
        badger.setBadgePosition(BadgeView.POSITION_CENTER);
        badger.setTextColor(Color.WHITE);
        badger.setBadgeBackgroundColor(Color.RED);
        if (userInfo != null) {
            accountName.setText("你好，" + userInfo.getAccountName() + "!");
            if (userInfo.getLetterCount() > 0) //根据userInfo里面letterCount的值设置letter图标
            {
                //				setLetterIcon(getResources().getDrawable(R.drawable.icon_user11));
                badger.setText(userInfo.getLetterCount() + "");
                badger.show();
            } else {
                //				setLetterIcon(getResources().getDrawable(R.drawable.icon_user11));
                badger.hide();
            }
            if (!StringUtils.isEmptyOrNull(userInfo.getRealName())
                    && (DMApplication.getInstance().getUserInfo().isTg() ? (null != userInfo.getUsrCustId() && !""
                    .equals(userInfo.getUsrCustId()))
                    : true)) {
                iconUser.setImageResource(R.drawable.icon_user01);
            } else {
                iconUser.setImageResource(R.drawable.icon_user01_);
            }
            if (!StringUtils.isEmptyOrNull(userInfo.getPhone())) {
                iconPhone.setImageResource(R.drawable.icon_user02);
            } else {
                iconPhone.setImageResource(R.drawable.icon_user02_);
            }
            if (!StringUtils.isEmptyOrNull(userInfo.getEmail())) {
                iconEmail.setImageResource(R.drawable.icon_user03);
            } else {
                iconEmail.setImageResource(R.drawable.icon_user03_);
            }
            if (!(null == userInfo.getAssessment())) {
                evaluation_result.setText(userInfo.getAssessment());
                evaluation_result.setTextColor(Color.parseColor("#ff666666"));
            } else {
                evaluation_result.setText("未评估");
                evaluation_result.setTextColor(Color.RED);
            }

            //			if (!isNeedPwd)
            //			{//如果不需要交易密码，那么不显示交易密码的图标
            //				icon_trad_pwd.setVisibility(View.GONE);
            //			}
            //			else
            //			{
            //				icon_trad_pwd.setVisibility(View.VISIBLE);
            //				if (userInfo.isWithdrawPsw())
            //				{
            //					icon_trad_pwd.setImageResource(R.drawable.icon_user04);
            //				}
            //				else
            //				{
            //					icon_trad_pwd.setImageResource(R.drawable.icon_user04_);
            //				}
            //			}
            if (null != userInfo.getUsrCustId() && !("").equals(userInfo.getUsrCustId())) {
                icon_trad_pwd.setVisibility(View.VISIBLE);
                icon_trad_pwd.setImageResource(R.drawable.icon_user04);
                isNeedPwd = true;
            } else {
                icon_trad_pwd.setVisibility(View.GONE);
                isNeedPwd = false;
            }
        }
    }

    /**
     * 获取账户与用户信息
     */
    private void postData() {
        post(POST_GET_ACCOUNTINFO, new HttpParams(), DMConstant.API_Url.USER_ACCOUNT);

        post(POST_GET_USERINFO, new HttpParams(), DMConstant.API_Url.USER_USERINFO);
    }

    private void post(final int postTag, HttpParams params, String url) {
        HttpUtil.getInstance().post(getContext(), url, params, new HttpCallBack() {
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
     * 查询提现手续费和充值手续费
     */
    private void queryFee() {
        ApiUtil.getFee(getActivity(), new OnPostCallBack() {
            @Override
            public void onSuccess(Fee fee) {
                isNeedPwd = fee.getChargep().isNeedPsd();
                initUserInfo();
            }

            @Override
            public void onFailure() {
                isNeedPwd = true;
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
            initAccountInfo();
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
            DMApplication.getInstance().setUserInfo(userInfo);
            initUserInfo();
            synchronizationBalance();
        } catch (JSONException e) {
            DMLog.e(MyFragment.class.getSimpleName(), e.getMessage());
        }
        showSecurityAlert();
    }

    /**
     * 根据userInfo里面letterCount的值设置letter图标
     */
    protected void setLetterIcon(Drawable left) {
        Drawable right = getResources().getDrawable(R.drawable.icon_jt3);
        left.setBounds(0, 0, left.getMinimumWidth(), left.getMinimumHeight());
        right.setBounds(0, 0, right.getMinimumWidth(), right.getMinimumHeight());
        ((TextView) redText).setCompoundDrawables(left, null, right, null);
    }

    private void initListeners() {
        mView.findViewById(R.id.setting).setOnClickListener(this);
        mView.findViewById(R.id.account_newcomer).setOnClickListener(this);
        mView.findViewById(R.id.account_letter_in_station_tv).setOnClickListener(this);
        mView.findViewById(R.id.account_letter_in_station_lv).setOnClickListener(this);
        mView.findViewById(R.id.account_risk_evaluation_lv).setOnClickListener(this);
        mView.findViewById(R.id.my_bid_txt).setOnClickListener(this);
        mView.findViewById(R.id.creditor_txt).setOnClickListener(this);
        mView.findViewById(R.id.my_reward).setOnClickListener(this);
        mView.findViewById(R.id.trade_record_txt).setOnClickListener(this);
        mView.findViewById(R.id.account_bank_card_tv).setOnClickListener(this);
        mView.findViewById(R.id.repayment).setOnClickListener(this);
        mView.findViewById(R.id.my_recharge_btn).setOnClickListener(this);
        mView.findViewById(R.id.my_loan_txt).setOnClickListener(this);
        mView.findViewById(R.id.withdraw).setOnClickListener(this);
        mView.findViewById(R.id.securityInfo).setOnClickListener(this);
        // 资产显示或隐藏
        img.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isShow && canRunFlag) {
                    //					assets_ll.setVisibility(View.GONE);
                    if (goneHandler == null) {
                        goneHandler = new Handler();
                    }
                    goneHandler.post(goneRun);
                    //					img.setImageResource(R.drawable.icon_m_jt2);
                    isShow = false;
                } else if (canRunFlag) {
                    assets_ll.setVisibility(View.VISIBLE);
                    if (visibleHandler == null) {
                        visibleHandler = new Handler();
                    }
                    visibleHandler.post(visibleRun);
                    //					img.setImageResource(R.drawable.icon_m_jt1);
                    isShow = true;
                }

            }
        });

        netConnectErrorManager = new NetConnectErrorManager(mView, mSwipeLayout, new NetConnetCallBack() {
            @Override
            public void onNetErrorRefrensh() {
                onRefresh();
            }
        });
    }

    private Handler visibleHandler = null;

    private Runnable visibleRun = new Runnable() {
        private LayoutParams layoutParams;

        @Override
        public void run() {
            layoutParams = assets_ll.getLayoutParams();
            if (layoutParams.height <= assets_ll_height) {
                canRunFlag = false;
                if (layoutParams.height > maxHeight) {
                    layoutParams.height = assets_ll_height;
                    assets_ll.setLayoutParams(layoutParams);
                    img.setImageResource(R.drawable.icon_m_jt1);
                    canRunFlag = true;
                } else {
                    assets_ll.setLayoutParams(layoutParams);
                    visibleHandler.postDelayed(this, 10);
                    layoutParams.height = layoutParams.height + 10;
                }
            } else {
                canRunFlag = true;
            }
        }
    };

    private Handler goneHandler = null;

    private Runnable goneRun = new Runnable() {
        private LayoutParams layoutParams;

        @Override
        public void run() {
            layoutParams = assets_ll.getLayoutParams();
            if (layoutParams.height >= 0) {
                canRunFlag = false;
                if (layoutParams.height < 10) {
                    layoutParams.height = 0;
                    assets_ll.setLayoutParams(layoutParams);
                    canRunFlag = true;
                    img.setImageResource(R.drawable.icon_m_jt2);
                } else {
                    assets_ll.setLayoutParams(layoutParams);
                    visibleHandler.postDelayed(this, 10);
                    layoutParams.height = layoutParams.height - 10;
                }
            } else {
                canRunFlag = true;
            }
        }
    };

    @Override
    public void onRefresh() {
        postData();
        mSwipeLayout.setRefreshing(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.securityInfo: {
                // 账户安全
                startActivity(new Intent(getActivity(), SecurityInfoActivity.class).putExtra("isNeedPwd", isNeedPwd));
                break;
            }
            case R.id.my_reward: {
                // 我的奖励
                startActivity(new Intent(getActivity(), MyRewardActivity.class));
                break;
            }
            case R.id.my_bid_txt: {
                // 我的投资
                startActivity(new Intent(getActivity(), MyInvestMentActivity.class));
                break;
            }
            case R.id.creditor_txt: {
                // 债权转让
                startActivity(new Intent(getActivity(), CreditorActivity.class));
                break;
            }
            case R.id.trade_record_txt: {
                //交易记录
                startActivity(new Intent(getActivity(), TradeRecordActivity.class));
                break;
            }
            case R.id.account_bank_card_tv: { // 银行卡管理
                startActivity(new Intent(getActivity(), BankCardManageActivity.class));
                break;
            }
            case R.id.account_letter_in_station_tv: { // 站内信
                startActivity(new Intent(getActivity(), LetterInStationActivity.class));
                break;
            }
            case R.id.account_letter_in_station_lv: { // 站内信
                startActivity(new Intent(getActivity(), LetterInStationActivity.class));
                break;
            }
            case R.id.account_risk_evaluation_lv: { // 风险评估
                Intent intent = new Intent(getActivity(), EvaluationActivity.class);
                intent.putExtra("SetGesture", "");
                startActivity(intent);
                break;
            }
            case R.id.account_newcomer: {
                // 新手入门
                startActivity(new Intent(getActivity(), NewComeListActivity.class));
                break;
            }
            case R.id.setting: { // 设置
                startActivity(new Intent(getActivity(), SettingActivity.class).putExtra("isNeedPwd", isNeedPwd));
                break;
            }
            case R.id.repayment: { // 回款日历
                startActivity(new Intent(getActivity(), RepaymentCalendarActivity.class));
                break;
            }
            case R.id.my_recharge_btn: {
                //				if (DMApplication.getInstance().getUserInfo().isTg())
                //				{
                //					//托管
                //					if (TextUtils.isEmpty(DMApplication.getInstance().getUserInfo().getUsrCustId()))
                //					{
                //						//未注册第三方托管帐号
                //						startActivity(new Intent(getActivity(), UnRegisterTgActivity.class).putExtra
                // ("title", "充值"));
                //					}
                //					else
                //					{
                //						//已经注册了第三方托管帐号
                //						startActivity(new Intent(getActivity(), TgRechargeActivity.class));
                //					}
                //				}
                //				else
                //				{
                // 充值
                    startActivity(new Intent(getActivity(), RechargeNewsActivity.class));
                //				}
                break;
            }
            case R.id.withdraw: {
                //				if (DMApplication.getInstance().getUserInfo().isTg())
                //				{
                //托管
                //                if (TextUtils.isEmpty(DMApplication.getInstance().getUserInfo().getUsrCustId())) {
                //未注册第三方托管帐号
                //						startActivity(new Intent(getActivity(), UnRegisterTgActivity.class)
                // .putExtra("title", "提现"));
                //                } else {
                //已经注册了第三方托管帐号
                Intent intent = new Intent(getActivity(), WithdrawActivity.class);
                intent.putExtra("overAmount", availableBalance.getText().toString());
                if (userInfo.isWithdrawPsw()) {
                    startActivity(intent);
                } else {
                    AlertDialogUtil.alert(getActivity(), "您未设置交易密码，请设置交易密码", "确认", new AlertDialogUtil
                            .AlertListener() {
                        @Override
                        public void doConfirm() {
                            startActivity(new Intent(getActivity(), TradePwdActivity.class));
                        }
                    });
                }
                //                }
                //				}
                //				else
                //				{
                // 提现
                //					Intent intent = new Intent(getActivity(), WithdrawActivity.class);
                //					intent.putExtra("overAmount", availableBalance.getText().toString());
                //					startActivity(intent);
                //				}
                break;
            }
            case R.id.my_loan_txt: {
                //我的借款
                startActivity(new Intent(getActivity(), MyLoanActivity.class));
                break;
            }
            default:
                break;
        }
    }

}
