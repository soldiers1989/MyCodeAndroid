package com.hxjr.p2p.ad5.ui.investment.bid;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.dm.http.HttpCallBack;
import com.dm.http.HttpUtil;
import com.dm.utils.AppManager;
import com.dm.utils.DMJsonObject;
import com.dm.utils.DMLog;
import com.dm.widgets.utils.AlertDialogUtil;
import com.dm.widgets.utils.AlertDialogUtil.AlertListener;
import com.dm.widgets.utils.AlertDialogUtil.ConfirmListener;
import com.dm.widgets.utils.ToastUtil;
import com.hxjr.p2p.ad5.DMApplication;
import com.hxjr.p2p.ad5.R;
import com.hxjr.p2p.ad5.bean.AccountBean;
import com.hxjr.p2p.ad5.bean.BidDetailBase;
import com.hxjr.p2p.ad5.bean.Fee;
import com.hxjr.p2p.ad5.bean.RewardBean;
import com.hxjr.p2p.ad5.bean.RewardInfo;
import com.hxjr.p2p.ad5.receiver.BidAndCreReceiver;
import com.hxjr.p2p.ad5.service.ApiUtil;
import com.hxjr.p2p.ad5.service.ApiUtil.OnPostCallBack;
import com.hxjr.p2p.ad5.ui.BaseActivity;
import com.hxjr.p2p.ad5.ui.mine.RechargeNewActivity;
import com.hxjr.p2p.ad5.ui.mine.WithdrawWebActivity;
import com.hxjr.p2p.ad5.ui.mine.setting.FindTradePwdActivity;
import com.hxjr.p2p.ad5.ui.mine.user.LoginActivity;
import com.hxjr.p2p.ad5.ui.tg.TgThirdWebActivity;
import com.hxjr.p2p.ad5.utils.AgreementManager;
import com.hxjr.p2p.ad5.utils.DMConstant;
import com.hxjr.p2p.ad5.utils.ErrorUtil;
import com.hxjr.p2p.ad5.utils.FormatUtil;
import com.hxjr.p2p.ad5.utils.HttpParams;
import com.hxjr.p2p.ad5.utils.UIHelper;
import com.hxjr.p2p.ad5.utils.UIHelper.OnDealPwdOkListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 投资支付页面
 *
 * @author jiaohongyun
 * @date 2015年11月2日
 */
public class BuyBidActivity extends BaseActivity {
    private Spinner spinner;

    private String remainAmount;

    private double minBidingAmount = 100d;

    /**
     * 最大可投金额
     */
    private double maxBidingAmount;

    private String bidId;

    private BidDetailBase base;

    private TextView remainAmountText;

    private TextView accountAmount;

    private EditText investAmountEdit;

    private TextView tvw_expected_earning;

    private RewardBean selectedReward = null;

    private LinearLayout lly_reward;

    private View line_reward;

    /**
     * 体验金收益
     */
    private double tyjExpected = -1d;

    /**
     * 奖励列表
     */
    private List<RewardBean> list = new ArrayList<RewardBean>();

    private AccountBean accountInfo;

    private Context mContext;

    /**
     * 协议管理类
     */
    private AgreementManager agreementManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buy_bid);
        mContext = this;
        bidId = getIntent().getStringExtra("bidId");
        base = (BidDetailBase) getIntent().getSerializableExtra("base");
        //		remainAmount = getIntent().getStringExtra("remainAmount");
        //		minBidingAmount = getIntent().getIntExtra("minBidingAmount", 100);
        //		bidId = getIntent().getStringExtra("bidId");
        remainAmount = base.getRemainAmount();
        minBidingAmount = base.getMinBidingAmount();
        maxBidingAmount = base.getMaxBidingAmount();
        initView();
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
     * 是否需要交易密码
     */
    private boolean isNeedPsw;

    /***
     * 是否必须完成邮箱认证
     */
    private boolean isNeedEmailRZ;

    /**
     * 查询是否需要交易密码
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

    /**
     * 查询账户信息
     */
    private void getAccountInfo() {
        HttpUtil.getInstance().post(this, DMConstant.API_Url.USER_ACCOUNT, new HttpCallBack() {
            @Override
            public void onSuccess(JSONObject result) {
                try {
                    String code = result.getString("code");
                    if (code.equals(DMConstant.ResultCode.SUCCESS)) {
                        DMJsonObject data = new DMJsonObject(result.getString("data"));
                        accountInfo = new AccountBean(data);
                        String terms = getString(R.string.bid_buy_user_amount, accountInfo.getOverAmount());
                        accountAmount.setText(UIHelper.makeSpannableStr(mContext,
                                terms,
                                terms.length() - 1 - (accountInfo.getOverAmount() + "").length(),
                                terms.length() - 1));
                        double totalAmount = Double.valueOf(accountInfo.getOverAmount());
                        double remainAmount = Double.valueOf(BuyBidActivity.this.remainAmount);
                        //剩余磕头金额小于最小可投金额
                        if (remainAmount < minBidingAmount) {
                            //可投金额为0，剩余金额不足
                            minBidingAmount = 0;
                            maxBidingAmount = 0;
                        } else if (remainAmount < maxBidingAmount) {
                            //剩余可投金额小于每次最大可投金额
                            int bb = (int) remainAmount;
                            int ketouJiner = bb - bb % 100;
                            ketouJiner = bb;
                            maxBidingAmount = ketouJiner;
                        }
                        investAmountEdit.setHint("金额范围  " + FormatUtil.formatStr2(minBidingAmount + "") + "~"
                                + FormatUtil.formatStr2(maxBidingAmount + ""));
                    } else {
                        ErrorUtil.showError(result);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                getRewards();
            }

            ;

            @Override
            public void onFailure(Throwable t, Context context) {
                super.onFailure(t, context);
                getRewards();
            }
        });
    }

    /**
     * 获取未使用奖励
     */
    private void getRewards() {
        //如果是新手标或者奖励标，则不显示奖励
        if (base.getIsJlb() || base.getIsXsb()) {
            return;
        }
        HttpUtil.getInstance().post(this, DMConstant.API_Url.UNUSE_REWARD_LIST, new HttpCallBack() {
            @Override
            public void onSuccess(JSONObject result) {
                try {
                    DMLog.i("getList", result.toString());
                    String code = result.getString("code");
                    if (DMConstant.ResultCode.SUCCESS.equals(code)) {
                        String dataStr = result.getString("data");
                        JSONObject dataObj = new JSONObject(dataStr);
                        list.clear();
                        if (dataStr != null && !dataStr.equals("")) {
                            //体验金数据
                            String experAmonut = dataObj.getString("experAmonut");
                            if (null != experAmonut && !"".equals(experAmonut) && !"0.00".equals(experAmonut)) {
                                RewardBean bean = new RewardBean();
                                bean.setTitle(experAmonut + "元体验金");
                                bean.setType("experience");
                                list.add(bean);
                            }
                            //红包数据
                            if (dataStr.contains("redPkgList")) {
                                JSONArray hbList = dataObj.getJSONArray("redPkgList");
                                for (int i = 0; i < hbList.length(); i++) {
                                    DMJsonObject datahb = new DMJsonObject(hbList.getString(i));
                                    RewardInfo reward = new RewardInfo(datahb);
                                    RewardBean bean = new RewardBean();
                                    bean.setId(reward.getId());
                                    bean.setAmount(reward.getAmount());
                                    bean.setRule(reward.getInvestUseRule());
                                    if (null == reward.getInvestUseRule() || "".equals(reward.getInvestUseRule())
                                            || Double.parseDouble(reward.getInvestUseRule()) <= 0) {
                                        bean.setTitle(reward.getAmount() + "元红包（无使用条件）");
                                    } else {
                                        bean.setTitle(reward.getAmount() + "元红包（出借满" + reward.getInvestUseRule() +
                                                "元可用）");
                                    }
                                    bean.setType("hb");
                                    list.add(bean);
                                }
                            }
                            //加息券数据
                            if (dataStr.contains("jxqList")) {
                                JSONArray jxqList = dataObj.getJSONArray("jxqList");
                                for (int i = 0; i < jxqList.length(); i++) {
                                    DMJsonObject datahb = new DMJsonObject(jxqList.getString(i));
                                    RewardInfo reward = new RewardInfo(datahb);
                                    RewardBean bean = new RewardBean();
                                    bean.setId(reward.getId());
                                    bean.setAmount(reward.getAmount());
                                    bean.setRule(reward.getInvestUseRule());
                                    if (null == reward.getInvestUseRule() || "".equals(reward.getInvestUseRule())
                                            || Double.parseDouble(reward.getInvestUseRule()) <= 0) {
                                        bean.setTitle(reward.getAmount() + "%加息券（无使用条件）");
                                    } else {
                                        bean.setTitle(reward.getAmount() + "%加息券（出借满" + reward.getInvestUseRule() +
                                                "元可用）");
                                    }
                                    bean.setType("jxq");
                                    list.add(bean);
                                }
                            }
                        } else {
                        }
                    } else {
                        ErrorUtil.showError(result);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                initSpinner();
            }

            @Override
            public void onFailure(Throwable t, Context context) //请求成功失败   已经是主线程，可以在这里进行UI操作
            {
                super.onFailure(t, context);
                initSpinner();
            }
        });
    }

    private void initSpinner() {
        List<String> l = new ArrayList<String>();
        l.add("不使用");
        if (list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                l.add(list.get(i).getTitle());
            }
            lly_reward.setVisibility(View.VISIBLE);
            line_reward.setVisibility(View.VISIBLE);
        } else {
            //如果没有奖励，则隐藏奖励框
            lly_reward.setVisibility(View.GONE);
            line_reward.setVisibility(View.GONE);
            return;
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_item_text) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                //                Log.w(TAG_CRY, "It is getView ing!");

                View v = super.getView(position, convertView, parent);
                //                ((CheckedTextView)v).setText("lallalalal");
                if (position == 0 ) {
                    ((CheckedTextView) v.findViewById(R.id.spinner_item_text)).setText("请点击使用奖励");
                    //                                                        ((CheckedTextView)v.findViewById(R.id
                    // .spinner_item_text))
                    // .setHint("请点击是用奖励"); //"Hint to be displayed"
                }
                return v;
            }

            @Override
            public int getCount() {
                return super.getCount();
            }

        };
        adapter.addAll(l);
        adapter.setDropDownViewResource(R.layout.spinner_text);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                if (arg2 - 1 >= 0) {
                    selectedReward = list.get(arg2 - 1);
                    //					if ("experience".equals(selectedReward.getType()) || "jxq".equals
                    // (selectedReward.getType()))
                    //					{
                    //					}
                } else {
                    selectedReward = null;
                }
                calcExpectedEarning(base.getPaymentType());
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
    }

    @Override
    protected void initView() {
        super.initView();
        ((TextView) findViewById(R.id.title_text)).setText(R.string.page_title_investment);
        lly_reward = (LinearLayout) findViewById(R.id.lly_reward);
        line_reward = findViewById(R.id.line_reward);
        spinner = (Spinner) findViewById(R.id.spinner);
        remainAmountText = (TextView) findViewById(R.id.remainAmountText);
        remainAmountText.setText(remainAmount);
        accountAmount = (TextView) findViewById(R.id.accountAmount);
        accountAmount.setText(getString(R.string.bid_buy_user_amount, "0.00"));
        tvw_expected_earning = (TextView) findViewById(R.id.tvw_expected_earning);
        investAmountEdit = (EditText) findViewById(R.id.investAmountEdit);
        investAmountEdit.setHint("金额范围  " + minBidingAmount + "~" + remainAmount);
        investAmountEdit.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                //只能输入两个小数位
                String textValue = arg0.toString().trim();
                if (textValue.contains(".") && textValue.substring(textValue.indexOf(".")).length() > 3) {
                    investAmountEdit.setText(textValue.subSequence(0, textValue.length() - 1));
                    investAmountEdit.setSelection(investAmountEdit.getText().toString().length());
                    return;
                }

                if (null != arg0 && !"".equals(arg0.toString()) && Double.parseDouble(arg0.toString()) >
                        maxBidingAmount) {
                    investAmountEdit.setText(FormatUtil.formatStr2(maxBidingAmount + ""));
                    investAmountEdit.setSelection(FormatUtil.formatStr2(maxBidingAmount + "").length());
                } else {
                    calcExpectedEarning(base.getPaymentType());
                }
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
            }
        });

        if (base.getIsJlb() || base.getIsXsb()) {
            lly_reward.setVisibility(View.GONE);
            line_reward.setVisibility(View.GONE);
        }

        agreementManager = new AgreementManager(this, AgreementManager.TYPE_JK, base.getIsDanBao());
        agreementManager.initView();
    }

    /**
     * 计算预期收益
     * <p>
     * 根据用户输入的购买金额和使用的奖励，即时显示预期收益值
     * 1、等额本息
     * 设贷款额为a，月利率为i，年利率为I，还款月数为n，每月还款额为b，还款利息总和为Y   Y＝n×a×i×（1＋i）^n÷〔（1＋i）^n－1〕－a      i=I/12
     * 2、一次还本付息（自然月）
     * 设贷款额为a，月利率为i，年利率为I
     */
    private void calcExpectedEarning(String type) {
        //如果没有填写投资金额 或者选择的不是体验金，则设置收益为0
        if ("".equals(investAmountEdit.getText().toString()))
        //			&& (null == selectedReward ? true : !"experience".equals(selectedReward.getType()))
        {
            tvw_expected_earning.setText(FormatUtil.formatMoney(0d));
            return;
        }
        //		Double I =
        //			"+null".equals(base.getJlRate()) || "".equals(base.getJlRate()) ? Double.parseDouble(base
        // .getRateValue())
        //				: Double.parseDouble(base.getRateValue())
        //					+ (Double.parseDouble(base.getJlRate().replace("+", "").replace("%", ""))) / 100;
        Double I = Double.parseDouble(base.getRateValue());
        //判断是否使用加息券
        if (null != selectedReward && "jxq".equals(selectedReward.getType())) {
            I = I + Double.parseDouble(selectedReward.getAmount()) / 100;
        }

        Double i = 0d;
        //判断是否按天计算,计算利率
        if (base.getIsDay().equals("F")) {
            i = I / 12;
        } else if (base.getIsDay().equals("S")) {
            i = I / 360;
        }
        Double Y = 0d;
        Double a =
                Double.parseDouble("".equals(investAmountEdit.getText().toString()) ? "0" : investAmountEdit.getText
                        ().toString());
        int n = base.getCycle();

        if ("等额本息".equals(type)) {
            Y = n * FormatUtil.getRound(a * i * Math.pow((i + 1), n) / (Math.pow(i + 1, n) - 1)) - a;
        }
        if ("等额本金".equals(type)) {
            Y = ((a / n + a * i) + a / n * (1 + i)) / 2 * n - a;
        }
        if ("本息到期一次付清".equals(type)) {
            Y = a * n * i;
        }
        if ("每月付息,到期还本".equals(type)) {
            Y = a * n * i;
        }

        //判断是否使用体验金
        if (null != selectedReward && "experience".equals(selectedReward.getType())) {
            getTyjExpectedEarn(Y);
        } else {
            tvw_expected_earning.setText(FormatUtil.formatMoney(FormatUtil.getRound(Y)));
        }

    }

    /**
     * 获取体验金收益
     */
    private void getTyjExpectedEarn(final double Y) {
        //由于进入此界面后体验金不会改变，所以可以缓存体验金预期收益
        if (-1d == tyjExpected) {
            HttpParams params = new HttpParams();
            params.put("tyjAmount", selectedReward.getAmount());
            params.put("biaoId", bidId);
            HttpUtil.getInstance().post(this, DMConstant.API_Url.EXPERIENCE_EXPECTED, params, new HttpCallBack() {
                @Override
                public void onSuccess(JSONObject result) {
                    try {
                        String code = result.getString("code");
                        if (code.equals(DMConstant.ResultCode.SUCCESS)) {
                            tyjExpected = result.getDouble("data");
                            tvw_expected_earning.setText(FormatUtil.formatMoney(Y + tyjExpected));
                        } else {
                            ErrorUtil.showError(result);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                ;

                @Override
                public void onFailure(Throwable t, Context context) {
                    super.onFailure(t, context);
                }
            });
        } else {
            tvw_expected_earning.setText(FormatUtil.formatMoney(Y + tyjExpected));
        }
    }

    /**
     * 投资支付（按钮事件）
     *
     * @param view
     */
    public void buyBid(View view) {
        if (checkClick(view.getId())) {
            if (checkParams()) {
                if (isNeedPsw) {
                    UIHelper.showPayPwdEditDialog(this, null, new OnDealPwdOkListener() {
                        @Override
                        public void onDealPwdOk(String dealPassword) {
                            confirmBuy(dealPassword);
                        }
                    });
                } else {
                    confirmBuy(null);
                }
            }
        }
    }

    /**
     * 检查参数
     *
     * @return
     */
    private boolean checkParams() {
        if (null == accountInfo) {
            ToastUtil.getInstant().show(this, "请检查用户信息");
            return false;
        }
        if (!UIHelper.hasCompletedSecurityInfo(this, DMApplication.getInstance().getUserInfo(), isNeedEmailRZ,
                isNeedPsw)) {//查看是否完成了信息安全认证
            return false;
        }
        if (null == investAmountEdit.getText().toString() || "".equals(investAmountEdit.getText().toString())) {
            ToastUtil.getInstant().show(this, "请输入出借金额");
            return false;
        }
        if (Double.parseDouble(investAmountEdit.getText().toString()) < minBidingAmount) {
            ToastUtil.getInstant().show(this, "出借金额不能少于" + FormatUtil.formatStr2(minBidingAmount + "") + "元");
            //			UIHelper.showViewShake(mContext, investAmountEdit);
            investAmountEdit.setText(FormatUtil.formatStr2(minBidingAmount + ""));
            return false;
        }
        if (Double.parseDouble(accountInfo.getOverAmount()) < Double.parseDouble(investAmountEdit.getText().toString
                ())) {
            if (null == DMApplication.getInstance().getUserInfo()) {
                startActivity(new Intent(mContext, LoginActivity.class));
                return false;
            }
            AlertDialogUtil.confirm(this, getString(R.string.available_ammount_not_enough), "充值", "确定", new
                    ConfirmListener() {
                        @Override
                        public void onOkClick() {

                            startActivity(new Intent(BuyBidActivity.this,
                                    RechargeNewActivity.class));
                        }

                        @Override
                        public void onCancelClick() {
                        }
                    });
            return false;
        }
        if (null != selectedReward && ("hb".equals(selectedReward.getType()) || "jxq".equals(selectedReward.getType()
        ))) {
            if (Double.parseDouble(investAmountEdit.getText().toString()) < Double.parseDouble(selectedReward.getRule
                    ())) {
                ToastUtil.getInstant().show(this, "没有达到奖励使用规则");
                return false;
            }
        }
        if (agreementManager != null && !agreementManager.isChecked()) {
            AlertDialogUtil.alert(this, "请先阅读借款协议并勾选！").setCanceledOnTouchOutside(false);
            return false;
        }
        return true;
    }

    /**
     * 确认并提交购买
     *
     * @param text
     */
    private void confirmBuy(String password) {
        HttpParams httpParams = new HttpParams();
        httpParams.put("bidId", bidId);
        httpParams.put("amount", investAmountEdit.getText().toString());
        httpParams.put("tranPwd", null != password ? password : "");
        //		httpParams.put("tranPwd", EncryptUtil.getMD5Str(password));
        if (selectedReward != null) {
            //使用奖励的情况
            httpParams.put("userReward", "true");//获取是使用奖励   有值则为使用(PC接口限制的。。。)
            httpParams.put("myRewardType", selectedReward.getType());//experience:体验金 |hb:红包|jxq:加息券
            if ("hb".equals(selectedReward.getType())) {
                httpParams.put("hbRule", selectedReward.getId());//红包使用规则(ID)
            } else if ("jxq".equals(selectedReward.getType())) {
                httpParams.put("jxqRule", selectedReward.getId());//加息券使用规则(ID)
            }
        }

        HttpUtil.getInstance().post(this, DMConstant.API_Url.BID_BUYBID, httpParams, new HttpCallBack() {
            @Override
            public void onSuccess(JSONObject result) {
                try {
                    String code = result.getString("code");
                    if (code.equals(DMConstant.ResultCode.SUCCESS)) {
                        if (DMApplication.getInstance().getUserInfo().isTg()
                                ) {
                            String url = result.getJSONObject("data").getString("url").toString();
                            Intent intent = new Intent(mContext, WithdrawWebActivity.class);
                            intent.putExtra("linkUrl", url);
                            intent.putExtra("message", "投标成功！");
                            intent.putExtra("title", DMConstant.TgWebTitle.BUY_BID);
                            //                            startActivity(intent);
                            startActivityForResult(intent, 001);
                        } else {
                            AlertDialogUtil.alert(mContext, mContext.getString(R.string.bid_buy_ok), new
                                    AlertListener() {
                                        @Override
                                        public void doConfirm() {
                                            Intent intent = new Intent(BidAndCreReceiver.BID_SUCCESS_RECEIVER);
                                            intent.putExtra("bidId", Integer.parseInt(bidId));
                                            intent.putExtra("amount", investAmountEdit.getText().toString());
                                            sendBroadcast(intent);
                                            // 关闭投标详情页面
                                            AppManager.getAppManager().getActivity(BidDetailActivity.class).finish();
                                            // 关闭购买页面
                                            finish();
                                        }
                                    });
                        }
                    } else if (code.equals(ErrorUtil.ErroreCode.ERROR_000044)) {
                        String description = result.getString("description");
                        if (null != description && description.contains("交易密码")) {
                            showDealPwdError();
                        } else {
                            AlertDialogUtil.alert(mContext, FormatUtil.Html2Text(description));
                        }
                    } else if (DMApplication.getInstance().getUserInfo().isTg() && ErrorUtil.ErroreCode
                            .ERROR_000047.equals(code)) {
                        final String url = result.getJSONObject("data").getString("url").toString();
                        AlertDialogUtil.confirm(BuyBidActivity.this,
                                result.getString("description"),
                                "去授权",
                                null,
                                new ConfirmListener() {

                                    @Override
                                    public void onOkClick() {
                                        Intent intent = new Intent(BuyBidActivity.this, TgThirdWebActivity.class);
                                        intent.putExtra("url", url);
                                        intent.putExtra("message", "授权成功！");
                                        intent.putExtra("title", DMConstant.TgWebTitle.SOUQUAN);
                                        startActivity(intent);
                                    }

                                    @Override
                                    public void onCancelClick() {
                                    }
                                });
                    } else {
                        ErrorUtil.showError(result);
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

    /***
     * 提示交易密码错误
     */
    protected void showDealPwdError() {
        AlertDialogUtil.confirm(mContext, getString(R.string.deal_pwd_err), null, "找回交易密码", new ConfirmListener() {
            @Override
            public void onOkClick() {
            }

            @Override
            public void onCancelClick() {
                startActivity(new Intent(mContext, FindTradePwdActivity.class));
            }
        });
    }

    //投标回调

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == 1111) {
            DMLog.e(requestCode + "requestCode===========resultCode" + resultCode);
            AlertDialogUtil.alert(mContext, mContext.getString(R.string.bid_buy_ok), new
                    AlertListener() {
                        @Override
                        public void doConfirm() {
                            Intent intent = new Intent(BidAndCreReceiver.BID_SUCCESS_RECEIVER);
                            intent.putExtra("bidId", Integer.parseInt(bidId));
                            intent.putExtra("amount", investAmountEdit.getText().toString());
                            sendBroadcast(intent);
                            // 关闭投标详情页面
                            AppManager.getAppManager().getActivity(BidDetailActivity.class).finish();
                            // 关闭购买页面
                            finish();
                        }
                    });
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
