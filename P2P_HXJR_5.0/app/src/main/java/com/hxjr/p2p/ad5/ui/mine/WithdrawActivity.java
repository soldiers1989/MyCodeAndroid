package com.hxjr.p2p.ad5.ui.mine;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.dm.http.HttpCallBack;
import com.dm.http.HttpUtil;
import com.dm.utils.DMJsonObject;
import com.dm.utils.DMLog;
import com.dm.utils.HtmlUtil;
import com.dm.widgets.TouchWebView;
import com.dm.widgets.utils.AlertDialogUtil;
import com.dm.widgets.utils.AlertDialogUtil.AlertListener;
import com.dm.widgets.utils.AlertDialogUtil.ConfirmListener;
import com.dm.widgets.utils.ToastUtil;
import com.hxjr.p2p.ad5.DMApplication;
import com.hxjr.p2p.ad5.R;
import com.hxjr.p2p.ad5.bean.BankCard;
import com.hxjr.p2p.ad5.bean.Fee;
import com.hxjr.p2p.ad5.bean.UserInfo;
import com.hxjr.p2p.ad5.bean.Withdraw;
import com.hxjr.p2p.ad5.bean.Withdrawp;
import com.hxjr.p2p.ad5.bean.YbFee;
import com.hxjr.p2p.ad5.service.ApiUtil;
import com.hxjr.p2p.ad5.service.ApiUtil.OnPostCallBack;
import com.hxjr.p2p.ad5.ui.BaseActivity;
import com.hxjr.p2p.ad5.ui.mine.bank.BankCardManageActivity;
import com.hxjr.p2p.ad5.ui.mine.bank.PayNumActivity;
import com.hxjr.p2p.ad5.ui.mine.setting.FindTradePwdActivity;
import com.hxjr.p2p.ad5.ui.tg.TgThirdWebActivity;
import com.hxjr.p2p.ad5.utils.AmountUtil;
import com.hxjr.p2p.ad5.utils.DMConstant;
import com.hxjr.p2p.ad5.utils.ErrorUtil;
import com.hxjr.p2p.ad5.utils.FormatUtil;
import com.hxjr.p2p.ad5.utils.HttpParams;
import com.hxjr.p2p.ad5.utils.UIHelper;
import com.hxjr.p2p.ad5.utils.UIHelper.OnDealPwdOkListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * 提现
 *
 * @author jiaohongyun
 * @date 2015年11月17日
 */
public class WithdrawActivity extends BaseActivity implements OnClickListener {
    /**
     * 最大添加银行卡数
     */
    private int maxbanks;

    private List<BankCard> cards;

    private TextView withdraw_type;

    private TextView withdraw_type_choice;

    private PopupWindow popupWindow;

    private String routFlag;

    private String routCode;

    private List<Withdraw> withdrawList;

    private List<Withdraw> withdrawChoiceList;

    private BankCard bankCard;

    /**
     * 当前绑定的银行卡数
     */
    private int currentCards;

    private LinearLayout listView;

    /**
     * 提现手续费
     */
    private Withdrawp withdrawp;

    //	private SQFee sqFee;

    private YbFee ybFee;

    /**
     * 实际扣除的手续费
     */
    private double fee;

    /**
     * 提现是否需要交易密码
     */
    private boolean isNeedPsw;

    /***
     * 是否必须完成邮箱认证
     */
    private boolean isNeedEmailRZ;

    /**
     * 可用余额
     */
    private String overAmount;

    /**
     * 用户个人信息
     */
    private UserInfo userInfo;

    /**
     * 提现金额
     */
    private EditText withdraw_amount;

    /**
     * 提现手续费
     */
    private TextView withdraw_fee;

    /**
     * 实际到账金额
     */
    private TextView real_withdraw;

    private Button withdraw_btn_ok;

    private TextView getMoneyDate;

    private TextView userAmount;

    /**
     * 添加银行卡
     */
    private TextView addBankImg;

    /**
     * 未绑定银行卡提示
     */
    private TextView no_card_tip;

    private TextView normal_tv;

    private TextView jiaji_tv;

    //    private HttpParams httpParams;

    private final static int YB_CODE_BIND_CARD = 1;

    private final static int YB_REQUEST_CODE_RECHAGE = 2;

    private boolean isNormal = true;// 是否正常提现，还是加急提现，true是正常，false是加急

    /**
     * 温馨提示语
     */
    private TextView warm_title;

    /**
     * 后台配置的提示信息
     */
    private TouchWebView warm_tips;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.withdraw);
        overAmount = getIntent().getStringExtra("overAmount");
        withdrawp = new Withdrawp();//给一个初始值，避免报空指针异常
        //		sqFee = new SQFee();
        ybFee = new YbFee();
        userInfo = DMApplication.getInstance().getUserInfo();
        initView();
        initListener();
    }

    @Override
    protected void initView() {
        super.initView();
        ((TextView) findViewById(R.id.title_text)).setText(R.string.withdraw);
        addBankImg = (TextView) findViewById(R.id.btn_right);
        addBankImg.setVisibility(View.VISIBLE);
        setTitleRightIcon(addBankImg);
        listView = (LinearLayout) findViewById(R.id.bank_listView);
        no_card_tip = (TextView) findViewById(R.id.no_card_tip);
        withdraw_btn_ok = (Button) findViewById(R.id.withdraw_btn_ok);
        withdraw_fee = (TextView) findViewById(R.id.withdraw_fee);
        withdraw_fee.setText(R.string.withdraw_fee_init);
        real_withdraw = (TextView) findViewById(R.id.real_withdraw);
        getMoneyDate = (TextView) findViewById(R.id.getMoneyDate);
        userAmount = (TextView) findViewById(R.id.userAmount);
        normal_tv = (TextView) findViewById(R.id.normal_tv);
        jiaji_tv = (TextView) findViewById(R.id.jiaji_tv);

        warm_title = (TextView) findViewById(R.id.warm_title);
        warm_tips = (TouchWebView) findViewById(R.id.warm_tips);

        //		normal_tv.setOnClickListener(this);
        //		jiaji_tv.setOnClickListener(this);
        withdraw_btn_ok.setOnClickListener(this);
        withdraw_amount = (EditText) findViewById(R.id.withdraw_amount);
        //		withdraw_amount.setFilters(new InputFilter[] {new InputFilter.LengthFilter(maxLength)});
        real_withdrawStr = getString(R.string.real_getamount, "0.00");
        String nowStr = getToday();
        getMoneyDateStr = getString(R.string.may_income_time, nowStr);
        real_withdraw.setText(makeSpannableStr(real_withdrawStr, 10, real_withdrawStr.length()));
        getMoneyDate.setText(makeSpannableStr(getMoneyDateStr, 7, getMoneyDateStr.length() - 18));
        userAmount.setText(overAmount);

        warm_tips.getSettings().setLoadWithOverviewMode(true);
        warm_tips.getSettings().setJavaScriptEnabled(true);
        warm_tips.setBackgroundColor(getResources().getColor(R.color.main_bg)); // 设置背景色
        warm_tips.setWebViewClient(new MyWebViewClient());
        warm_tips.getSettings().setUserAgentString("Android/1.0");
        withdraw_type = (TextView) findViewById(R.id.withdraw_type);
        withdraw_type_choice = (TextView) findViewById(R.id.withdraw_type_choice);
        withdraw_type_choice.setOnClickListener(this);
        withdraw_type.setOnClickListener(this);
        withdrawList = new ArrayList<Withdraw>();
        withdrawChoiceList = new ArrayList<Withdraw>();
        Withdraw wd = new Withdraw();
        wd.setName("指定资金通道");
        wd.setType("Y");
        withdrawList.add(wd);
        Withdraw wd1 = new Withdraw();
        wd1.setName("ESB选择资金通道");
        wd1.setType("N");
        withdrawList.add(wd1);
        Withdraw wdC = new Withdraw();
        wdC.setName("河北银行大额通道");
        wdC.setType("G9");
        withdrawChoiceList.add(wdC);
        DMLog.e(withdrawList.toString());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case YB_REQUEST_CODE_RECHAGE:

                break;
            default:
                break;
        }
    }

    /**
     * 设置头部title里面右边TextView的icon图标
     */
    protected void setTitleRightIcon(TextView textView) {
        Drawable right = getResources().getDrawable(R.drawable.icon_add);
        right.setBounds(0, 0, right.getMinimumWidth(), right.getMinimumHeight());
        textView.setCompoundDrawables(null, null, right, null);
    }

    private void initListener() {
        addBankImg.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkClick(v.getId())) //防重复点击
                {
                    if (checkSecurityInfo()) {
                        Intent intent = new Intent(WithdrawActivity.this, BankCardManageActivity.class);
                        startActivityForResult(intent, DMConstant.RequestCodes.REQUEST_CODE_SECURITY);
                        //						httpParams = new HttpParams();
                        //						post(DMConstant.API_Url.PAY_BIND_CARD, httpParams, YB_CODE_BIND_CARD);
                    }
                }
            }
        });

        withdraw_amount.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                /**
                 * 余额为0时，输入内容强制显示为0.00
                 * return 语句是防止进入死循环
                 */
                if (0 == Double.valueOf(overAmount.replace(",", "").trim())) {
                    if (s.toString().equals("0.00")) {
                        return;
                    }
                    withdraw_amount.setText("0.00");
                } else {
                    checkAndSetText(s.toString());
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

//    private void post(String url, HttpParams params, final int postType) {
//        HttpUtil.getInstance().post(WithdrawActivity.this, url, params, new HttpCallBack() {
//
//            @Override
//            public void onSuccess(JSONObject result) {
//                switch (postType) {
//                    case YB_CODE_BIND_CARD:
//                        doAfterGetYBBindCard(result);
//                        break;
//                    default:
//                        break;
//                }
//            }
//        });
//    }

//    /**
//     * 易宝绑卡
//     *
//     * @param result
//     */
//    protected void doAfterGetYBBindCard(JSONObject result) {
//        try {
//            String code = result.getString("code");
//            if (DMConstant.ResultCode.SUCCESS.equals(code)) {
//                JSONObject jsonObject = result.getJSONObject("data");
//                if (jsonObject != null) {
//                    Intent it = new Intent(WithdrawActivity.this, TgThirdWebActivity.class);
//                    it.putExtra("url", jsonObject.getString("url"));
//                    it.putExtra("title", "绑定银行卡");
//                    startActivityForResult(it, YB_REQUEST_CODE_RECHAGE);
//                }
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//    }

    /***
     * 检查输入的内容，并给手续费和到账金额设置值
     * @param textValue
     */
    protected void checkAndSetText(String textValue) {
        if (textValue.isEmpty()) {
            //			withdraw_feeStr = getString(R.string.withdraw_fee, "0.00");
            setFeeText("0.00");
            real_withdrawStr = getString(R.string.real_getamount, "0.00");
            //			withdraw_fee.setText(makeSpannableStr(withdraw_feeStr, 8, withdraw_feeStr.length() - 14));
            real_withdraw.setText(makeSpannableStr(real_withdrawStr, 10, real_withdrawStr.length()));
            return;
        }
        if (textValue.startsWith(".") || textValue.startsWith("0")) {//不能以.或者0开头
            withdraw_amount.setText("");
            withdraw_amount.setSelection(withdraw_amount.getText().toString().length());
            return;
        }
        if (textValue.endsWith(".")) { //如果末尾是小数点，则不做计算
            return;
        }
        //只能输入两个小数位
        if (textValue.contains(".") && textValue.substring(textValue.indexOf(".")).length() > 3) {
            withdraw_amount.setText(textValue.subSequence(0, textValue.length() - 1));
            withdraw_amount.setSelection(withdraw_amount.getText().toString().length());
            return;
        }
        double amount = Double.valueOf(textValue.replace(",", "").trim());
        double overAmount1 = Double.valueOf(overAmount.replace(",", "").trim());
        if (amount > overAmount1) {
            withdraw_amount.setText(AmountUtil.getString(overAmount));
            withdraw_amount.setSelection(withdraw_amount.getText().toString().length());
            return;
        }
        if (amount < withdrawp.getMin()) {
            return;
        }
        if (amount > withdrawp.getMax()) {
            //			withdraw_amount.setText(withdrawp.getMax() + "");
            withdraw_amount.setSelection(withdraw_amount.getText().toString().length());
            if (null != withdrawp) {
                //				ToastUtil.getInstant().show(WithdrawActivity.this,
                //					"最大可提现金额为" + FormatUtil.formatStr2(withdrawp.getMax() + "") + "元");
            }
            return;
        }

        real_withdraw.setText(makeSpannableStr(real_withdrawStr, 10, real_withdrawStr.length()));

        fee = 0d;//扣费
        String withdraw_feeStr = "";
        double sjAmount = 0;
        if (!ybFee.isWithdarwISPT()) { //非平台
            if (withdrawp.isTxkfType()) {
                if (!isNormal) //加急
                {
                    if ((amount * ybFee.getUrgentRate() + 2) < ybFee.getWithdarwCosts()) {
                        setFeeText(FormatUtil.formatStr2(ybFee.getWithdarwCosts() + ""));
                        withdraw_feeStr = getString(R.string.real_getamount, FormatUtil.formatStr2(amount - ybFee
                                .getWithdarwCosts() + ""));
                        sjAmount = amount - ybFee.getWithdarwCosts();
                    } else {
                        setFeeText(FormatUtil.formatStr2(amount * ybFee.getUrgentRate() + 2 + ""));
                        withdraw_feeStr = getString(R.string.real_getamount, FormatUtil.formatStr2(amount - amount *
                                ybFee.getUrgentRate() + 2 + ""));
                        sjAmount = amount - amount * ybFee.getUrgentRate() + 2;
                    }
                } else {
                    setFeeText(FormatUtil.formatStr2(ybFee.getWithdarwCosts() + ""));
                    withdraw_feeStr = getString(R.string.real_getamount, FormatUtil.formatStr2(amount - ybFee
                            .getWithdarwCosts() + ""));
                    sjAmount = amount - ybFee.getWithdarwCosts();
                }
                if (sjAmount > 0) {
                    real_withdraw.setText(makeSpannableStr(withdraw_feeStr, 10, withdraw_feeStr.length()));
                } else {
                    real_withdrawStr = getString(R.string.real_getamount, "0.00");
                }
            } else  //外扣
            {
                //				setFeeText(FormatUtil.formatStr2(ybFee.getWithdarwCosts() + ""));
                //				withdraw_feeStr = getString(R.string.real_getamount, FormatUtil.formatStr2(amount +
                // ""));
                //				sjAmount = amount;
                if (!isNormal) //加急
                {
                    if ((amount * ybFee.getUrgentRate() + 2) < ybFee.getWithdarwCosts()) {
                        setFeeText(FormatUtil.formatStr2(ybFee.getWithdarwCosts() + ""));
                        //						withdraw_feeStr = getString(R.string.real_getamount, FormatUtil
                        // .formatStr2(amount - ybFee.getWithdarwCosts() + ""));
                        //						sjAmount = amount - ybFee.getWithdarwCosts();
                    } else {
                        setFeeText(FormatUtil.formatStr2(amount * ybFee.getUrgentRate() + 2 + ""));
                        //						withdraw_feeStr = getString(R.string.real_getamount, FormatUtil
                        // .formatStr2(amount - amount * ybFee.getUrgentRate() + 2 + ""));
                        //						sjAmount = amount - amount * ybFee.getUrgentRate() + 2;
                    }
                } else {
                    setFeeText(FormatUtil.formatStr2(ybFee.getWithdarwCosts() + ""));
                    withdraw_feeStr = getString(R.string.real_getamount, FormatUtil.formatStr2(amount - ybFee
                            .getWithdarwCosts() + ""));
                    //					sjAmount = amount-ybFee.getWithdarwCosts();
                }
                withdraw_feeStr = getString(R.string.real_getamount, FormatUtil.formatStr2(amount + ""));
                real_withdraw.setText(makeSpannableStr(withdraw_feeStr, 10, withdraw_feeStr.length()));
            }
        } else  //平台
        {
            //			if (!isNormal) //加急
            //			{
            //				if ((amount * ybFee.getUrgentRate() + 2) < ybFee.getWithdarwCosts())
            //				{
            //					setFeeText(FormatUtil.formatStr2(ybFee.getWithdarwCosts() + ""));
            //				}
            //				else
            //				{
            //					setFeeText(FormatUtil.formatStr2(amount * ybFee.getUrgentRate() + 2 + ""));
            //				}
            //			}
            //			else
            //			{
            //				setFeeText(FormatUtil.formatStr2(ybFee.getWithdarwCosts() + ""));
            //			}
            //			withdraw_feeStr = getString(R.string.real_getamount, amount + "");
            //			sjAmount = amount;
            //			if(sjAmount > 0)
            //			{
            //				real_withdraw.setText(makeSpannableStr(withdraw_feeStr, 10, withdraw_feeStr.length()));
            //			}else{
            //				real_withdrawStr = getString(R.string.real_getamount, "0.00");
            //			}
            withdraw_feeStr = getString(R.string.withdraw_fee_pt, "0.00");
            withdraw_fee.setText(makeSpannableStr(withdraw_feeStr, 8, withdraw_feeStr.length() - 15));
            real_withdrawStr = getString(R.string.real_getamount, FormatUtil.formatStr2(amount + ""));
            real_withdraw.setText(makeSpannableStr(real_withdrawStr, 10, real_withdrawStr.length()));
        }
    }

    /**
     * 获取服务器端当前日期
     *
     * @return
     */
    private String getToday() {
        Date date = new Date();
        long time = date.getTime() + DMApplication.getInstance().diffTime;
        Date now = new Date(time);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        String nowStr = sdf.format(now);
        return nowStr;
    }

    //	private String withdraw_feeStr;

    private String real_withdrawStr;

    private String getMoneyDateStr;

    public SpannableString makeSpannableStr(String str, int a, int b) {
        SpannableString spanStr = new SpannableString(str);
        spanStr.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.orange)),
                a,
                b,
                SpannableString.SPAN_INCLUSIVE_EXCLUSIVE);
        if (b != str.length()) {
            spanStr.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.back_grey)),
                    b,
                    str.length(),
                    SpannableString.SPAN_INCLUSIVE_EXCLUSIVE);
        }
        return spanStr;
    }

    @Override
    protected void onResume() {
        super.onResume();
        DMLog.e("onResume", "onResume");
        ApiUtil.getUserInfo(this);
        queryFee();
        getBankCardDatas();
    }

    /**
     * 查询提现手续费和充值手续费
     */
    private void queryFee() {
        ApiUtil.getFee(this, new OnPostCallBack() {
            @Override
            public void onSuccess(Fee fee) {
                WithdrawActivity.this.withdrawp = fee.getWithdrawp();
                //				if (null != fee.getSqFee())
                //				{
                //					sqFee = fee.getSqFee();
                //				}
                String withdraw_feeStr;
                if (null != fee.getYbFee()) {
                    ybFee = fee.getYbFee();
                }
                if (ybFee.isWithdarwISPT()) {
                    withdraw_feeStr = getString(R.string.withdraw_fee_pt, "0.00");
                    withdraw_fee.setText(makeSpannableStr(withdraw_feeStr, 8, withdraw_feeStr.length() - 15));
                }
                //				else{
                //					withdraw_feeStr = getString(R.string.withdraw_fee_amount, "0.00");
                //					withdraw_fee.setText(makeSpannableStr(withdraw_feeStr, 8, withdraw_feeStr.length()
                // - 15));
                //				}

                isNeedPsw = fee.getChargep().isNeedPsd();
                isNeedEmailRZ = fee.getBaseInfo().isNeedEmailRZ();
                setFeeText("0.00");

                if (null != withdrawp.getTwxts() && !"".equals(withdrawp.getTwxts())) {
                    warm_title.setVisibility(View.VISIBLE);
                    HtmlUtil htmlUtil = new HtmlUtil();
                    htmlUtil.getHead()
                            .append("<link rel='stylesheet' href='file:///android_asset/css/tips.css' " +
                                    "type='text/css'/>");
                    htmlUtil.getBody().append("<div id='content'>").append(withdrawp.getTwxts()).append("</div>");
                    warm_tips.loadDataWithBaseURL(null, htmlUtil.CreateHtml(), "text/html", "utf-8", null);
                } else {
                    warm_title.setVisibility(View.GONE);
                    warm_tips.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure() {
                isNeedPsw = true;
                isNeedEmailRZ = true;
            }
        });
    }

    /**
     * 请求数据
     */
    private void getBankCardDatas() {
        HttpUtil.getInstance().post(this, DMConstant.API_Url.USER_MYBANKLIST, new HttpCallBack() {
            @Override
            public void onSuccess(JSONObject result) {
                try {
                    String code = result.getString("code");
                    if (DMConstant.ResultCode.SUCCESS.equals(code)) {
                        addBankImg.setVisibility(View.GONE);
                        // 成功
                        JSONObject data0 = result.getJSONObject("data");
                        maxbanks = data0.getInt("maxbanks");
                        JSONArray data = data0.getJSONArray("myBankList");
                        cards = new ArrayList<BankCard>();
                        currentCards = data.length();
                        if (currentCards == 0) {
                            addBankImg.setVisibility(View.VISIBLE);
                        }
                        if (currentCards > 0) {
                            addBankImg.setVisibility(View.GONE);
                            for (int i = 0; i < currentCards; i++) {
                                DMJsonObject dmJsonObject = new DMJsonObject(data.get(i).toString());
                                BankCard bankCard = new BankCard(dmJsonObject);
                                cards.add(bankCard);
                            }
                            cards.get(0).setSelected(true);
                            listView.setVisibility(View.VISIBLE);
                            no_card_tip.setVisibility(View.GONE);
                            addCardViews();
                        } else {
                            listView.setVisibility(View.GONE);
                            no_card_tip.setVisibility(View.VISIBLE);
                        }
                    } else {//如果失败，则将最大银行卡数量设置为大于0
                        maxbanks = 1;
                        listView.removeAllViews();
                        ErrorUtil.showError(result);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable t, Context context) {//如果失败，则将最大银行卡数量设置为大于0
                maxbanks = 1;
            }

        });
    }

    /**
     * 添加绑定过的银行卡视图
     */
    protected void addCardViews() {
        if (bankCardViews == null) {
            bankCardViews = new ArrayList<View>();
        }
        listView.removeAllViews();
        bankCardViews.clear();
        int size = cards.size();
        for (int i = 0; i < size; i++) {
            View view = LayoutInflater.from(this).inflate(R.layout.withdraw_bank_card_item, listView, false);
            View line = LayoutInflater.from(this).inflate(R.layout.withdraw_bank_card_item_line, listView, false);
            listView.addView(view);
            bankCardViews.add(view);
            if (i != size - 1) {
                listView.addView(line);
            }
            initMyCardInfo(view, i);
        }
    }

    /**
     * 检测安全认证信息   和  银行卡最大数量
     *
     * @return
     */
    protected boolean checkSecurityInfo() {
        if (maxbanks <= currentCards) {
            AlertDialogUtil.alert(this, "您绑定的银行卡数量已达到最大值，不能再绑定其他银行卡！", new AlertListener() {
                @Override
                public void doConfirm() {
                    finish();
                }
            });
            return false;
        }
        if (!UIHelper.hasCompletedSecurityInfo(this, DMApplication.getInstance().getUserInfo(), isNeedEmailRZ,
                isNeedPsw)) {//查看是否完成了信息安全认证
            return false;
        }
        return true;
    }

    /**
     * 设置绑定的银行卡信息
     *
     * @param view
     * @param i
     */
    private void initMyCardInfo(View view, int i) {
         bankCard = cards.get(i);
        ImageView icon = (ImageView) view.findViewById(R.id.select_icon);
        TextView bankName = (TextView) view.findViewById(R.id.bank_name);
        TextView cardTailNum = (TextView) view.findViewById(R.id.bank_card_tail_num);

        if (bankCard.isSelected()) {
            icon.setImageResource(R.drawable.icon_xk1);
            bankName.setTextColor(this.getResources().getColor(R.color.orange));
            cardTailNum.setTextColor(this.getResources().getColor(R.color.text_black_6));
        } else {
            icon.setImageResource(R.drawable.icon_xk01);
            bankName.setTextColor(this.getResources().getColor(R.color.text_black_9));
            cardTailNum.setTextColor(this.getResources().getColor(R.color.text_black_9));
        }
        bankName.setText(bankCard.getBankname());
        cardTailNum.setText(bankCard.getWeiHao());
        view.setTag(i);
        view.setOnClickListener(this);
        DMLog.e(bankCard.toString());
        if (null==bankCard.getBankUnionCode()||bankCard.getBankUnionCode().equals("")){
            AlertDialogUtil.confirm(WithdrawActivity.this,"请输入银联号码",new AlertDialogUtil.ConfirmListener(){
                @Override
                public void onOkClick() {
                    Intent intent=new Intent();
                    intent.setClass(WithdrawActivity.this, PayNumActivity.class);
                    startActivity(intent);
                }

                @Override
                public void onCancelClick() {
                    ToastUtil.getInstant().show(WithdrawActivity.this,"银联号未更新！");
                    finish();
                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.withdraw_type: {
                UIHelper.hideSrfAndRun(this, new Runnable() {
                    @Override
                    public void run() {
                        showWithDrawTypePopup(withdrawList, withdraw_type);
                    }
                });
                break;
            }
            case R.id.withdraw_type_choice: {
                UIHelper.hideSrfAndRun(this, new Runnable() {
                    @Override
                    public void run() {
                        showWithDrawChoicePopup(withdrawChoiceList, withdraw_type_choice);
                    }
                });
                break;
            }
            case R.id.withdrawBankCard: {
                changeBankSelect(v);
                break;
            }
            case R.id.withdraw_btn_ok: {
                if (checkClick(R.id.withdraw_btn_ok)) {
                    confirmWithdraw();
                }
                break;
            }
            case R.id.normal_tv: {
                normal_tv.setTextColor(getResources().getColor(R.color.main_color));
                jiaji_tv.setTextColor(getResources().getColor(R.color.text_gray));
                isNormal = true;
                checkAndSetText(withdraw_amount.getText().toString());
                break;
            }
            case R.id.jiaji_tv: {
                normal_tv.setTextColor(getResources().getColor(R.color.text_gray));
                jiaji_tv.setTextColor(getResources().getColor(R.color.main_color));
                isNormal = false;
                checkAndSetText(withdraw_amount.getText().toString());
                break;
            }
            default:
                break;
        }
    }

    /**
     * 提现按钮被点击事件
     */
    private void confirmWithdraw() {
        if (checkParams()) {
            String withdrawAmountStr = withdraw_amount.getText().toString();
            double amount = Double.valueOf(withdrawAmountStr);
            //withdrawp.getMin()
            if (amount > withdrawp.getMax()) {
                AlertDialogUtil.alert(WithdrawActivity.this,
                        "提现金额范围不正确，您输入的提现金额为" + amount + "元，" + "允许提现的金额范围为" + FormatUtil.formatStr2(withdrawp.getMin
                                () + "") + "-"
                                + FormatUtil.formatStr2(withdrawp.getMax() + "") + "元").setCanceledOnTouchOutside
                        (false);
            } else {
                if (isNeedPsw) //需要交易密码
                {
                    showTradPwd();
                } else { //不需要交易密码
                    doWithdrawPost(null);
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
        if (cards == null || cards.size() < 1) {
            AlertDialogUtil.alert(WithdrawActivity.this, "请先添加银行卡");
            return false;
        }
        if (withdraw_amount.getText().toString().isEmpty()) {
            ToastUtil.getInstant().show(WithdrawActivity.this, R.string.withdraw_amount_empty);
            return false;
        }
        if (routFlag == null) {
            ToastUtil.getInstant().show(WithdrawActivity.this, "请选择指定资金通道");
            return false;
        }
        if (routFlag == "Y" && routCode == null) {
            ToastUtil.getInstant().show(WithdrawActivity.this, "请选择资金通道");
            return false;
        }
        double amount = Double.valueOf(overAmount.replace(",", "").trim());
        double inputAmount = Double.valueOf(withdraw_amount.getText().toString());

        //		if ((amount * ybFee.getUrgentRate() + 2) < ybFee.getWithdarwCosts())
        //		{
        //			if (inputAmount <= amount - ybFee.getWithdarwCosts())
        //			{
        //				ToastUtil.getInstant().show(WithdrawActivity.this, "提现金额应大于提现手续费金额");
        //				return false;
        //			}
        //		}
        //		else
        //		{
        //			if (inputAmount <= (amount - amount * ybFee.getUrgentRate() + 2))
        //			{
        //				ToastUtil.getInstant().show(WithdrawActivity.this, "提现金额应大于提现手续费金额");
        //				return false;
        //			}
        //		}
        if (null != withdrawp && inputAmount < withdrawp.getMin()) {
            ToastUtil.getInstant().show(WithdrawActivity.this, "最小提现金额为" + FormatUtil.formatStr2(withdrawp.getMin() +
                    "") + "元");
            return false;
        }
        if (!ybFee.isWithdarwISPT()) {
            if (inputAmount <= ybFee.getWithdarwCosts() && withdrawp.isTxkfType()) {
                ToastUtil.getInstant().show(WithdrawActivity.this, "提现金额应大于提现手续费金额");
                return false;
            }
        }

        if (inputAmount > amount) {
            ToastUtil.getInstant().show(WithdrawActivity.this, R.string.withdraw_amount_not_enought);
            return false;
        }
        if (isNeedPsw && !DMApplication.getInstance().getUserInfo().isWithdrawPsw()) {
            ToastUtil.getInstant().show(WithdrawActivity.this, R.string.withdraw_not_set_pwd);
            return false;
        }
        return true;
    }

    /**
     * 弹框提示输入交易密码
     */
    private void showTradPwd() {
        UIHelper.showPayPwdEditDialog(this, "确定", new OnDealPwdOkListener() {
            @Override
            public void onDealPwdOk(String dealPassword) {
                doWithdrawPost(dealPassword);
            }
        });
    }

    /***
     * 发出提现的请求
     * @param dealPassword
     */
    protected void doWithdrawPost(String dealPassword) {
        String id = cards.get(selectedBankCardIndex).getId() + "";
        String amount = withdraw_amount.getText().toString().trim();
        HttpParams params = new HttpParams();
        params.put("cardId", id);
        params.put("amount", amount);
        params.put("routFlag", routFlag);
        if (routFlag == "Y")
            params.put("routCode", routCode);
        if (null != dealPassword) {
            params.put("withdrawPsd", dealPassword);
        }
        HttpUtil.getInstance().post(WithdrawActivity.this, DMConstant.API_Url.PAY_WITHDRAW, params, new HttpCallBack() {
            @Override
            public void onSuccess(JSONObject result) {
                try {
                    String code = result.getString("code");
                    if (DMConstant.ResultCode.SUCCESS.equals(code)) {

                        String url = result.getString("data").toString();
                        DMLog.e(url);
                        Intent intent = new Intent(WithdrawActivity.this, WithdrawWebActivity.class);
                        intent.putExtra("linkUrl", url);
                        intent.putExtra("message", "提现申请成功！");
                        intent.putExtra("title", DMConstant.TgWebTitle.WITHDRAW);
                        startActivity(intent);
                        WithdrawActivity.this.finish();
                    } else if (code.equals(ErrorUtil.ErroreCode.ERROR_000044)) {
                        String description = result.getString("description");
                        if (null != description && description.contains("交易密码")) {
                            showDealPwdError();
                        } else {
                            AlertDialogUtil.alert(WithdrawActivity.this, FormatUtil.Html2Text(description));
                        }
                    } else if (DMApplication.getInstance().getUserInfo().isTg() && ErrorUtil.ErroreCode
                            .ERROR_000047.equals(code)) {
                        final String url = result.getJSONObject("data").getString("url").toString();
                        AlertDialogUtil.confirm(WithdrawActivity.this,
                                result.getString("description"),
                                "去授权",
                                null,
                                new ConfirmListener() {

                                    @Override
                                    public void onOkClick() {
                                        Intent intent = new Intent(WithdrawActivity.this, TgThirdWebActivity.class);
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

            }
        });
    }

    /***
     * 提示交易密码错误
     */
    protected void showDealPwdError() {
        AlertDialogUtil.confirm(WithdrawActivity.this, getString(R.string.deal_pwd_err), null, "找回交易密码", new
                ConfirmListener() {
                    @Override
                    public void onOkClick() {
                    }

                    @Override
                    public void onCancelClick() {
                        startActivity(new Intent(WithdrawActivity.this, FindTradePwdActivity.class));
                    }
                });
    }

    /**
     * 被选中的银行卡位置
     */
    private int selectedBankCardIndex;

    private List<View> bankCardViews;

    private void changeBankSelect(View v) {
        int j = (Integer) v.getTag();
        int length = cards.size();
        for (int i = 0; i < length; i++) {
            BankCard temp = cards.get(i);
            if (i == j) {
                temp.setSelected(true);
                selectedBankCardIndex = i;
            } else {
                temp.setSelected(false);
            }
            View view = bankCardViews.get(i);
            initMyCardInfo(view, i);
        }
    }

    private void setFeeText(String feeAmount) {
        String withdraw_feeStr = "";
        if (ybFee.isWithdarwISPT()) {  //平台
            withdraw_feeStr = getString(R.string.withdraw_fee_pt, feeAmount);
            withdraw_fee.setText(makeSpannableStr(withdraw_feeStr, 8, withdraw_feeStr.length() - 15));
        } else {
            if (WithdrawActivity.this.withdrawp.isTxkfType()) { //内扣
                withdraw_feeStr = getString(R.string.withdraw_fee, feeAmount);
                withdraw_fee.setText(makeSpannableStr(withdraw_feeStr, 8, withdraw_feeStr.length() - 15));
            } else { //外扣
                withdraw_feeStr = getString(R.string.withdraw_fee_w, feeAmount);
                withdraw_fee.setText(makeSpannableStr(withdraw_feeStr, 8, withdraw_feeStr.length() - 15));
            }
        }

    }

    /**
     * 显示是否指定提现通道popupWindow
     */
    @SuppressLint("InflateParams")
    @SuppressWarnings("deprecation")
    private void showWithDrawTypePopup(final List<Withdraw> withDwraw, final TextView selectBankTv) {
        View contentView = LayoutInflater.from(WithdrawActivity.this).inflate(R.layout.popup_window, null);

        int height = getPopupWindowHeight(selectBankTv, withDwraw.size()); //获取悬浮窗的高度

        popupWindow = new PopupWindow(contentView, selectBankTv.getWidth(), height, true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable()); //加该行代码后，点击界面其他位置popupWindow会消失
        //				popupWindow.setFocusable(true);
        ListView listView = (ListView) contentView.findViewById(R.id.popup_window_listview);
        listView.setAdapter(new WithDrawBaseAdapter(withDwraw));
        popupWindow.showAsDropDown(selectBankTv);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                popupWindow.dismiss();
                Withdraw withdraw = (Withdraw) parent.getAdapter().getItem(position);
                if (withdraw != null) {
                    withdraw_type.setText(withdraw.getName());
                    routFlag = withdraw.getType();
                    if (routFlag == "Y") {
                        withdraw_type_choice.setVisibility(View.VISIBLE);
                    } else {
                        withdraw_type_choice.setVisibility(View.GONE);
                        routCode = null;
                    }
                }
            }
        });
    }

    /**
     * 显示选择通道popupWindow
     */
    @SuppressLint("InflateParams")
    @SuppressWarnings("deprecation")
    private void showWithDrawChoicePopup(final List<Withdraw> withdrawChoiceList, final TextView selectBankTv) {
        View contentView = LayoutInflater.from(WithdrawActivity.this).inflate(R.layout.popup_window, null);

        int height = getPopupWindowHeight(selectBankTv, withdrawChoiceList.size()); //获取悬浮窗的高度

        popupWindow = new PopupWindow(contentView, selectBankTv.getWidth(), height, true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable()); //加该行代码后，点击界面其他位置popupWindow会消失
        //				popupWindow.setFocusable(true);
        ListView listView = (ListView) contentView.findViewById(R.id.popup_window_listview);
        listView.setAdapter(new WithDrawBaseAdapter(withdrawChoiceList));
        popupWindow.showAsDropDown(selectBankTv);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                popupWindow.dismiss();
                Withdraw withdraw = (Withdraw) parent.getAdapter().getItem(position);
                if (withdraw != null) {
                    withdraw_type_choice.setText(withdraw.getName());
                    routCode = withdraw.getType();
                }
            }
        });
    }

    /**
     * 银行卡ListView的适配器
     */
    public class WithDrawBaseAdapter extends BaseAdapter {
        private List<Withdraw> datas;

        public WithDrawBaseAdapter(List<Withdraw> datas) {
            this.datas = datas;
        }

        @SuppressLint({"ViewHolder", "InflateParams"})
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = LayoutInflater.from(WithdrawActivity.this).inflate(R.layout.popup_window_lv_item, null);
            TextView tv = (TextView) convertView.findViewById(R.id.item_tv);
            tv.setText(datas.get(position).getName());
            return convertView;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public Object getItem(int position) {
            return datas.get(position);
        }

        @Override
        public int getCount() {
            return datas.size();
        }
    }

    /**
     * 设置popupWindow的高度
     */
    private int getPopupWindowHeight(View parentView, int size) {
        int[] position = new int[2];
        parentView.getLocationInWindow(position);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int screenHeight = displayMetrics.heightPixels; //屏高
        int height = 0;
        if (size * parentView.getHeight() < 400) {
            height = size * parentView.getHeight();
        } else {
            height = screenHeight - position[1] - parentView.getHeight() - 10;
        }
        return height;
    }

    class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            //			view.loadUrl(url); //在当前的webview中跳转到新的url
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
