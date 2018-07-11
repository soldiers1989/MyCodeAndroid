package com.hxjr.p2p.ad5.ui.mine.setting;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dm.http.HttpCallBack;
import com.dm.http.HttpUtil;
import com.dm.utils.DMLog;
import com.dm.utils.StringUtils;
import com.dm.widgets.utils.AlertDialogUtil;
import com.dm.widgets.utils.ToastUtil;
import com.hxjr.p2p.ad5.DMApplication;
import com.hxjr.p2p.ad5.R;
import com.hxjr.p2p.ad5.bean.RegexInfo;
import com.hxjr.p2p.ad5.bean.UserInfo;
import com.hxjr.p2p.ad5.service.ApiUtil;
import com.hxjr.p2p.ad5.service.ApiUtil.OnGetUserInfoCallBack;
import com.hxjr.p2p.ad5.ui.BaseActivity;
import com.hxjr.p2p.ad5.utils.DMConstant;
import com.hxjr.p2p.ad5.utils.ErrorUtil;
import com.hxjr.p2p.ad5.utils.FormatUtil;
import com.hxjr.p2p.ad5.utils.HttpParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 安全信息认证
 *
 * @author God
 * @date 2015年11月25日
 */
public class SecurityInfoActivity extends BaseActivity implements OnClickListener {

    private static final int CERTIFICATION = 0; // 实名认证

    private static final int BIND_EMAIL = 1; // 绑定邮箱

    private static final int BIND_PHONE = 2; // 绑定手机

    private static final int SET_TRADE_PWD = 7; // 设置交易密码

    private static final int GET_EMAIL_CODE = 3; // 获取邮箱验证码

    private static final int GET_PHONE_COND = 4; // 获取手机验证码

    private static final int SAFE_LEVEL_LOW = 1; // 安全等级低

    private static final int SAFE_LEVEL_MID = 2; // 安全等级中

    private static final int SAFE_LEVEL_LESS_HIGH = 3; // 安全等级较高

    private static final int SAFE_LEVEL_HIGH = 4; // 安全等级高

    private static final int SECURITY_CODE_TIME = 60; // 获取验证码时间, 默认为一分钟

    private static final int HANDLE_EMAIL_MESSAGE = 5;

    private static final int HANDLE_PHONE_MESSAGE = 6;

    private static final int CHECK_VERIFYCODE = 10; // 校验验证码

    private int remainEmailTime; // 剩余邮箱验证码时间, 默认为一分钟

    private int remainPhoneTime; // 剩余手机验证码时间, 默认为一分钟

    // 安全等级进度条
    private View safeLvlProgress1;

    private View safeLvlProgress2;

    private View safeLvlProgress3;

    /**
     * 安全等级文字描述
     */
    private TextView saveLvlView;

    /**
     * 实名认证
     */
    private LinearLayout idCardLayout;

    /** 名称 */
    //	private TextView nameView;

    /**
     * 修改名字
     */
    private TextView nameModifyView;

    /**
     * 新名字
     */
    private EditText newNameEditer;

    /**
     * 新身份证号
     */
    private EditText newIDEditer;

    /**
     * 提交实名信息按钮
     */
    private Button nameSubmitBtn;

    /**
     * 绑定邮箱
     */
    private LinearLayout emailLayout;

    /**
     * 绑定邮箱名称
     */
    private TextView emailView;

    /**
     * 绑定 邮箱
     */
    private TextView emailModifyView;

    /**
     * 新邮箱
     */
    private EditText newEmailEditer;

    // /**邮箱验证码*/
    // private EditText emailCheckEditer;

    // /** 获取邮箱验证码按钮*/
    // private Button queryEmailCheckBtn;

    /**
     * 提交邮箱信息按钮
     */
    private Button emailSubmitBtn;

    /**
     * 绑定手机
     */
    private LinearLayout phoneLayout;

    /**
     * 绑定手机名称
     */
    private TextView phoneView;

    private TextView phoneModifyView;

    /**
     * 新手机
     */
    private EditText newPhoneEditer;

    /**
     * 手机验证码
     */
    private EditText phoneCheckEditer;

    /**
     * 获取手机验证码按钮
     */
    private Button queryPhoneCheckBtn;

    /**
     * 提交手机信息按钮
     */
    private Button phoneSubmitBtn;

    /**
     * 交易密码设置
     */
    private LinearLayout tradePwdLayout;

    private TextView tradePwdView;

    private TextView tradePwdModifyView;

    /**
     * 支付密码
     */
    private EditText tradePwdEditer;

    /**
     * 重复支付密码
     */
    private EditText reTradePwdEditer;

    /**
     * 提交支付密码按钮
     */
    private Button tradePwdSubmitBtn;

    /**
     * 是否可以获取手机验证码
     */
    private boolean canGetPhoneCode = true;

    /**
     * 是否可以点击获取手机验证码按钮
     */
    private boolean isPhoneBtnCanClick = false;

    private Timer emailTimer;

    private Timer phoneTimer;

    /**
     * 是否可以取消邮箱验证的timer
     */
    private boolean isEmailTimerCanCancel = true; //

    /**
     * 是否可以取消手机验证的timer
     */
    private boolean isPhoneTimerCanCancel = true;

    /**
     * 是否已经实名认证
     */
    private boolean isCertificated = false;

    private HttpParams httpParams;

    private UserInfo userInfo;

    private SharedPreferences sharedPreferences;

    private Editor editor;

    private String buttonContent; // Button里面的文字内容

    private RegexInfo regexInfo;

    private boolean isCilckGetCode = false;

    /**
     * 是否需要交易密码
     */
    private Boolean isNeedPwd;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case HANDLE_EMAIL_MESSAGE:
                    // queryEmailCheckBtn.setText(buttonContent);
                    // if (isEmailBtnCanClick)
                    // {
                    // queryEmailCheckBtn.setEnabled(true);
                    // }
                    break;

                case HANDLE_PHONE_MESSAGE:
                    queryPhoneCheckBtn.setText(buttonContent);
                    if (isPhoneBtnCanClick) {
                        queryPhoneCheckBtn.setEnabled(true);
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.security_info);
        regexInfo = FormatUtil.initRegexInfo(this);
        initVariable();
        initView();
        initData();
        initListener();
    }

    private void initVariable() {
        sharedPreferences = this.getSharedPreferences("config", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        userInfo = DMApplication.getInstance().getUserInfo();
        isNeedPwd = getIntent().getBooleanExtra("isNeedPwd", true);
    }

    @Override
    protected void initView() {
        super.initView();
        ((TextView) findViewById(R.id.title_text)).setText(R.string.security);
        // 安全等级
        safeLvlProgress1 = findViewById(R.id.account_process1);
        safeLvlProgress2 = findViewById(R.id.account_process2);
        safeLvlProgress3 = findViewById(R.id.account_process3);
        // saveLvlProgressBk = findViewById(R.id.account_proccessbk);
        saveLvlView = (TextView) findViewById(R.id.account_savelvltext);

        // 实名验证
        idCardLayout = (LinearLayout) findViewById(R.id.idcard_verify_layout);
        nameModifyView = (TextView) findViewById(R.id.account_namemodify);
        newNameEditer = (EditText) findViewById(R.id.real_name_edit);
        newIDEditer = (EditText) findViewById(R.id.id_card_edit);
        nameSubmitBtn = (Button) findViewById(R.id.idcardverify_ok_btn);

        // 邮箱验证
        emailView = (TextView) findViewById(R.id.account_bindemail);
        emailLayout = (LinearLayout) findViewById(R.id.email_verify_layout);
        emailModifyView = (TextView) findViewById(R.id.account_emailmodify);
        newEmailEditer = (EditText) findViewById(R.id.email_edit);
        // emailCheckEditer = (EditText)findViewById(R.id.email_ver_edit);
        // queryEmailCheckBtn = (Button)findViewById(R.id.email_ver_get_btn);
        emailSubmitBtn = (Button) findViewById(R.id.email_verify_ok_btn);

        // 手机验证
        phoneView = (TextView) findViewById(R.id.account_bindphone);
        phoneLayout = (LinearLayout) findViewById(R.id.mobile_verify_layout);
        phoneModifyView = (TextView) findViewById(R.id.account_phonemodify);
        newPhoneEditer = (EditText) findViewById(R.id.mobile_edit);
        phoneCheckEditer = (EditText) findViewById(R.id.mobile_ver_edit);
        queryPhoneCheckBtn = (Button) findViewById(R.id.mobile_ver_get_btn);
        phoneSubmitBtn = (Button) findViewById(R.id.mobile_verify_ok_btn);

        // 交易密码验证
        tradePwdLayout = (LinearLayout) findViewById(R.id.tradePwd_layout);
        tradePwdView = (TextView) findViewById(R.id.account_tradePwd_tv);
        tradePwdModifyView = (TextView) findViewById(R.id.account_tradePwd_modify);
        tradePwdEditer = (EditText) findViewById(R.id.tradePwd_edit);
        reTradePwdEditer = (EditText) findViewById(R.id.reTradePwd_edit);
        tradePwdSubmitBtn = (Button) findViewById(R.id.tradePwd_verify_ok_btn);

        if (!isNeedPwd) {
            findViewById(R.id.deal_pwd_ll).setVisibility(View.GONE);
            findViewById(R.id.last_line).setVisibility(View.GONE);
        } else {
            findViewById(R.id.deal_pwd_ll).setVisibility(View.VISIBLE);
            findViewById(R.id.last_line).setVisibility(View.VISIBLE);
        }
    }

    private void initListener() {
        nameModifyView.setOnClickListener(this);
        nameSubmitBtn.setOnClickListener(this);
        emailModifyView.setOnClickListener(this);
        // queryEmailCheckBtn.setOnClickListener(this);
        emailSubmitBtn.setOnClickListener(this);
        phoneModifyView.setOnClickListener(this);
        queryPhoneCheckBtn.setOnClickListener(this);
        phoneSubmitBtn.setOnClickListener(this);
        tradePwdModifyView.setOnClickListener(this);
        tradePwdSubmitBtn.setOnClickListener(this);
    }

    /**
     * 填充数据
     */
    private void initData() {
        setPhoneTimer();
        setUserSafeInfo();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ApiUtil.getUserInfo(this, new OnGetUserInfoCallBack() {

            @Override
            public void onSuccess() {
                // TODO Auto-generated method stub
                initVariable();
                setUserSafeInfo();
            }

            @Override
            public void onFailure() {
                // TODO Auto-generated method stub

            }
        });
    }

    private void setPhoneTimer() {
        // long remainPhoneTimeTemp =
        // sharedPreferences.getLong("remainPhoneTime", -1);
        // long phoneStopTime = sharedPreferences.getLong("phoneStopTime", -1);
        // long currentTime = System.currentTimeMillis();
        //
        // if ((currentTime - phoneStopTime) / 1000 < remainPhoneTimeTemp - 2)
        // {
        // newPhoneEditer.setText(sharedPreferences.getString("phone", "null"));
        // remainPhoneTime = (int)(remainPhoneTimeTemp - (currentTime -
        // phoneStopTime) / 1000);
        // startPhoneTimer(remainPhoneTime);
        // }
    }

    private void setUserSafeInfo() {
        if (null != userInfo) {
            int safeLevel = 0;
            //			String userName = userInfo.getRealName(); // 设置用户名和用户ID
            //			if (!StringUtils.isEmptyOrNull(userName)) {
            if (!StringUtils.isEmptyOrNull(userInfo.getUsrCustId())) // 实名认证需要第三方注册后才可以有进度显示
            {
                safeLevel++;
            }
            safeLevel++;
            //				nameView.setText(userName);
            if (userInfo.getIdcardVerified() && userInfo.getIdcardFrontVerified().equals("TG")
                    && userInfo.getIdcardInverseVerified().equals("TG")) {
                nameModifyView.setText("通过");
                //					nameView.setTextColor(getResources().getColor(R.color.text_black_9));
                nameModifyView.setTextColor(getResources().getColor(R.color.text_black_9));
                //                nameModifyView.setEnabled(false);
                //                nameModifyView.setClickable(false);
            }
            if (userInfo.getIdcardVerified() && userInfo.getIdcardFrontVerified().equals("BTG")) {
                nameModifyView.setText("不通过");
            }
            if (userInfo.getIdcardVerified() && userInfo.getIdcardInverseVerified().equals("BTG")) {
                nameModifyView.setText("不通过");
            }
            if (userInfo.getIdcardVerified() && userInfo.getIdcardInverseVerified().equals("WYZ")) {
                nameModifyView.setText("未验证");
            }
            if (userInfo.getIdcardVerified() && userInfo.getIdcardFrontVerified().equals("WYZ")) {
                nameModifyView.setText("未验证");
            }
            if (userInfo.getIdcardVerified() && userInfo.getIdcardFrontVerified().equals("DSH")
                    && userInfo.getIdcardInverseVerified().equals("DSH")) {
                nameModifyView.setText("待审核");
                //					nameView.setTextColor(getResources().getColor(R.color.text_black_9));
                nameModifyView.setTextColor(getResources().getColor(R.color.text_black_9));
                nameModifyView.setEnabled(false);
                nameModifyView.setClickable(false);
            }
            if (userInfo.getIdcardVerified() && userInfo.getIdcardFrontVerified().equals("TG")
                    && userInfo.getIdcardInverseVerified().equals("DSH")) {
                nameModifyView.setText("待审核");
                //					nameView.setTextColor(getResources().getColor(R.color.text_black_9));
                nameModifyView.setTextColor(getResources().getColor(R.color.text_black_9));
                nameModifyView.setEnabled(false);
                nameModifyView.setClickable(false);
            }
            if (userInfo.getIdcardVerified() && userInfo.getIdcardFrontVerified().equals("DSH")
                    && userInfo.getIdcardInverseVerified().equals("TG")) {
                nameModifyView.setText("待审核");
                //					nameView.setTextColor(getResources().getColor(R.color.text_black_9));
                nameModifyView.setTextColor(getResources().getColor(R.color.text_black_9));
                nameModifyView.setEnabled(false);
                nameModifyView.setClickable(false);
            }
            isCertificated = userInfo.getIdcardVerified();
            //			}
            String email = userInfo.getEmail();// 设置用户邮箱
            if (!StringUtils.isEmptyOrNull(email)) {
                safeLevel++;
                emailView.setText(email);
                emailView.setTextColor(getResources().getColor(R.color.text_black_9));
                emailModifyView.setVisibility(View.GONE);
            }

            String phoneNum = userInfo.getPhone(); // 设置用户电话
            if (!StringUtils.isEmptyOrNull(phoneNum)) {
                safeLevel++;
                phoneView.setText(phoneNum);
                phoneView.setTextColor(getResources().getColor(R.color.text_black_9));
                phoneModifyView.setVisibility(View.GONE);
            }

            if (userInfo.isWithdrawPsw()) // 设置交易密码
            {
                tradePwdView.setText("已设置");
                tradePwdView.setTextColor(getResources().getColor(R.color.text_black_9));
                tradePwdModifyView.setVisibility(View.GONE);
            }
            if (!userInfo.isTg() && userInfo.isWithdrawPsw()) {// 如果不是托管，那么安全等级还需要判断交易密码
                safeLevel++;
            } else if (userInfo.isTg() && !StringUtils.isEmptyOrNull(userInfo.getEmail())
                    && !StringUtils.isEmptyOrNull(userInfo.getIdCard())
                    && !StringUtils.isEmptyOrNull(userInfo.getPhoneNumber())) {// 如果是托管，而且完成了其他三个认证，那么安全等级就是高
                safeLevel = SAFE_LEVEL_HIGH;
            }
            userInfo.setSafeLevel(safeLevel);
            setUserSafeLevel(safeLevel); // 设置用户的安全等级
        }
    }

    /**
     * 设置用户的安全等级
     *
     * @param safeLevel
     */
    private void setUserSafeLevel(int safeLevel) {
        if (safeLevel == SAFE_LEVEL_LOW) // 安全等级低
        {
            saveLvlView.setText(getResources().getString(R.string.account_savelvl_low));
            safeLvlProgress1.setBackgroundResource(R.color.safe_level_color);
            safeLvlProgress2.setBackgroundResource(R.color.safe_level_back);
            safeLvlProgress3.setBackgroundResource(R.color.safe_level_back);
        } else if (safeLevel == SAFE_LEVEL_MID || safeLevel == SAFE_LEVEL_LESS_HIGH) // 安全等级中
        {
            saveLvlView.setText(getResources().getString(R.string.account_savelvl_mid));
            safeLvlProgress1.setBackgroundResource(R.color.safe_level_color);
            safeLvlProgress2.setBackgroundResource(R.color.safe_level_color);
            safeLvlProgress3.setBackgroundResource(R.color.safe_level_back);
        } else if (safeLevel == SAFE_LEVEL_HIGH) // 安全等级较高
        {
            saveLvlView.setText(getResources().getString(R.string.account_savelvl_high));
            safeLvlProgress1.setBackgroundResource(R.color.safe_level_color);
            safeLvlProgress2.setBackgroundResource(R.color.safe_level_color);
            safeLvlProgress3.setBackgroundResource(R.color.safe_level_color);
        } else { // 未知安全等级
            saveLvlView.setText(getResources().getString(R.string.account_savelvl_low));
            safeLvlProgress1.setBackgroundResource(R.color.safe_level_back);
            safeLvlProgress2.setBackgroundResource(R.color.safe_level_back);
            safeLvlProgress3.setBackgroundResource(R.color.safe_level_back);
        }
    }

    @Override
    protected void onDestroy() {
        // if (null != userInfo)
        // {
        // DMApplication.getInstance().setUserInfo(userInfo);
        // }
        if (emailTimer != null && isEmailTimerCanCancel) {
            emailTimer.cancel();
        }
        if (phoneTimer != null && isPhoneTimerCanCancel) {
            phoneTimer.cancel();
        }

        // editor.putLong("remainEmailTime", remainEmailTime);
        // editor.putLong("emailStopTime", System.currentTimeMillis());
        // editor.putString("email",
        // newEmailEditer.getText().toString().trim());
        //
        // editor.putLong("remainPhoneTime", remainPhoneTime);
        // editor.putLong("phoneStopTime", System.currentTimeMillis());
        // editor.putString("phone",
        // newPhoneEditer.getText().toString().trim());
        // editor.commit();
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.account_namemodify: // 实名认证
                showSubVerify(CERTIFICATION);
                break;
            case R.id.account_emailmodify: // 绑定邮箱
                showSubVerify(BIND_EMAIL);
                break;
            case R.id.account_phonemodify: // 绑定手机
                showSubVerify(BIND_PHONE);
                break;
            case R.id.account_tradePwd_modify:// 支付密码
                setTradePwd();
                break;
            case R.id.idcardverify_ok_btn: // 提交实名认证
                verifyIdCard();
                break;
            case R.id.email_verify_ok_btn: // 绑定邮箱
                bindEmail();
                break;
            case R.id.mobile_verify_ok_btn: // 绑定手机
                bindPhone();
                break;
            case R.id.tradePwd_verify_ok_btn:// 交易密码设置\
                //                setTradePwd();
                break;
            case R.id.deal_pwd_ll:// 交易密码修改
                setTradePwd();
                break;
            // case R.id.email_ver_get_btn: // 绑定邮箱获取验证码
            // if (canGetEmailCode) //canGetEmailCode
            // {
            // getEmailVerCode();
            // }
            // break;
            case R.id.mobile_ver_get_btn: // 绑定手机获取验证码
                if (canGetPhoneCode) // canGetPhoneCode
                {
                    getPhoneVerCode();
                }
                break;
            default:
                break;
        }
    }

    /**
     * 绑定手机
     */
    private void bindPhone() {
        if (newPhoneEditer.getText().toString().isEmpty()) {
            // 手机号码为空
            AlertDialogUtil
                    .alert(SecurityInfoActivity.this, getString(R.string.account_phone_none))
                    .setCanceledOnTouchOutside(false);
        } else if (FormatUtil.isMobileNO(newPhoneEditer.getText().toString().trim())) {
            // 手机号码格式正确 // 验证码不能为空
            if (phoneCheckEditer.getText().toString().isEmpty()) {
                AlertDialogUtil.alert(SecurityInfoActivity.this,
                        getString(R.string.account_yzm_none)).setCanceledOnTouchOutside(false);
                return;
            } else if (!isCilckGetCode) {
                AlertDialogUtil.alert(SecurityInfoActivity.this, "您还没获取验证码，请先获取验证码")
                        .setCanceledOnTouchOutside(false);
                return;
            } else {
                // 绑定手机
                httpParams = new HttpParams();
                httpParams.put("phone", newPhoneEditer.getText().toString());
                httpParams.put("verifyCode", phoneCheckEditer.getText().toString());
                httpParams.put("type", "RZ");
                post(CHECK_VERIFYCODE, DMConstant.API_Url.CHECK_VERIFY_CODE);
            }
        } else {
            // 输入手机号码格式不正确
            AlertDialogUtil.alert(SecurityInfoActivity.this,
                    getString(R.string.account_phone_format_erro)).setCanceledOnTouchOutside(false);
        }
    }

    /**
     * 获取手机验证码
     */
    private void getPhoneVerCode() {
        if (newPhoneEditer.getText().toString().isEmpty()) {
            AlertDialogUtil
                    .alert(SecurityInfoActivity.this, getString(R.string.account_phone_none))
                    .setCanceledOnTouchOutside(false);
        } else if (!FormatUtil.isMobileNO(newPhoneEditer.getText().toString().trim())) {
            AlertDialogUtil.alert(SecurityInfoActivity.this,
                    getString(R.string.find_pwd_phone_format_error)).setCanceledOnTouchOutside(false);
        } else {
            // 发送验证码到手机
            sendCodeToPhone();

        }
    }

    /**
     * 绑定邮箱
     */
    private void bindEmail() {
        if (newEmailEditer.getText().toString().isEmpty()) {
            // 邮箱地址为空
            AlertDialogUtil.alert(this, getString(R.string.account_emailver_no_email))
                    .setCanceledOnTouchOutside(false);
        } else if (FormatUtil.validateEmail(newEmailEditer.getText().toString())) {
            // 绑定邮箱
            httpParams = new HttpParams();
            httpParams.put("email", newEmailEditer.getText().toString());
            httpParams.put("type", "RZ");
            post(GET_EMAIL_CODE, DMConstant.API_Url.GET_EMAILCODE);// 发送请求绑定邮箱
            // }
        } else {
            // 输入是邮箱地址格式不正确
            AlertDialogUtil.alert(this, getString(R.string.account_emailver_email_format_erro))
                    .setCanceledOnTouchOutside(false);
        }
    }

    // /**
    // * @param time
    // * @param timeInterval
    // */
    // private void startEmailTimer(final int time)
    // {
    // // queryEmailCheckBtn.setEnabled(false); //在一分钟内按钮不能再次点击
    // isEmailBtnCanClick = false;
    // emailTimer = new Timer();
    // TimerTask task = new TimerTask()
    // {
    // private int tempTime = time;
    //
    // @Override
    // public void run()
    // {
    // if (tempTime > 0)
    // {
    // // buttonContent = tempTime +
    // SecurityInfoActivity.this.getResources().getString(R.string.account_re_yzm);
    // buttonContent =
    // String.format(SecurityInfoActivity.this.getResources().getString(R.string.account_re_yzm),
    // tempTime);
    // }
    // else
    // {
    // buttonContent =
    // SecurityInfoActivity.this.getResources().getString(R.string.account_get_security_code);
    // isEmailBtnCanClick = true;
    // isEmailTimerCanCancel = false;
    // emailTimer.cancel();
    // }
    // tempTime--;
    // remainEmailTime = tempTime;
    // Message message = handler.obtainMessage();
    // message.what = HANDLE_EMAIL_MESSAGE;
    // handler.sendMessage(message);
    // }
    // };
    // emailTimer.schedule(task, 0, 1000);
    // }

    /**
     * 发送验证码到手机
     */
    private void sendCodeToPhone() {
        isCilckGetCode = true;

        httpParams = new HttpParams();
        httpParams.put("type", "RZ");
        httpParams.put("phone", newPhoneEditer.getText().toString());
        post(GET_PHONE_COND, DMConstant.API_Url.GETMOBILECODE);
    }

    private void startPhoneTimer(final int time) {
        queryPhoneCheckBtn.setEnabled(false);
        isPhoneBtnCanClick = false;
        phoneTimer = new Timer();
        TimerTask task = new TimerTask() {
            private int tempTime = time;

            @Override
            public void run() {
                if (tempTime > 0) {
                    // buttonContent = tempTime +
                    // SecurityInfoActivity.this.getResources().getString(R.string.account_re_yzm);
                    buttonContent = String.format(SecurityInfoActivity.this.getResources()
                            .getString(R.string.account_re_yzm), tempTime);
                } else {
                    buttonContent = SecurityInfoActivity.this.getResources().getString(
                            R.string.account_get_security_code);
                    isPhoneBtnCanClick = true;
                    isPhoneTimerCanCancel = false;
                    phoneTimer.cancel();
                }
                tempTime--;
                remainPhoneTime = tempTime;
                Message message = handler.obtainMessage();
                message.what = HANDLE_PHONE_MESSAGE;
                handler.sendMessage(message);
            }
        };
        phoneTimer.schedule(task, 0, 1000);
    }

    /**
     * 实名认证
     */
    private void verifyIdCard() {
        if (verifiedIDPrams()) {
            httpParams = new HttpParams();
            String name = newNameEditer.getText().toString();
            // try
            // {
            // name = URLEncoder.encode(name, "utf-8");
            // }
            // catch (UnsupportedEncodingException e)
            // {
            // e.printStackTrace();
            // }
            httpParams.put("name", name);
            httpParams.put("idCard", newIDEditer.getText().toString());
            post(CERTIFICATION, DMConstant.API_Url.USER_SETUSERINFO);
        }
    }

    /**
     * 设置交易密码
     */
    private void setTradePwd() {
        //        String newPassword = tradePwdEditer.getText().toString();
        //        String confirmPassword = reTradePwdEditer.getText().toString();
        //        if ("".equalsIgnoreCase(newPassword)) {
        //            ToastUtil.getInstant().show(SecurityInfoActivity.this, "密码不能为空");
        //        } else if (!FormatUtil.matches(regexInfo.getTxPwdRegex(), newPassword)) {
        //            ToastUtil.getInstant().show(SecurityInfoActivity.this, regexInfo.getTxPwdContent());
        //        } else if (!newPassword.equalsIgnoreCase(confirmPassword)) {
        //            ToastUtil.getInstant().show(SecurityInfoActivity.this, "新密码要和确认密码一致");
        //        } else {
        //            httpParams = new HttpParams();
        //            httpParams.put("onePwd", newPassword);
        //            httpParams.put("twoPwd", confirmPassword);
        //            post(SET_TRADE_PWD, DMConstant.API_Url.USER_SETWITPASSWORD);
        //        }
        Intent intent = new Intent(SecurityInfoActivity.this, TradePwdActivity
                .class);
        startActivity(intent);
    }

    /**
     * 发送post的方法
     */
    private void post(final int postTag, String url) {
        HttpUtil.getInstance().post(this, url, httpParams, new HttpCallBack() {
            @Override
            public void onSuccess(JSONObject result)// 请求成功 已经是主线程，可以在这里进行UI操作
            {
                switch (postTag) {
                    case CHECK_VERIFYCODE:// 校验验证码
                        doAfterCheckVerifyCode(result);
                        break;
                    case CERTIFICATION: // 实名认证
                        doAfterCertification(result);
                        break;
                    case BIND_PHONE: // 绑定手机
                        doAfterBindPhone(result);
                        break;
                    case SET_TRADE_PWD:// 设置交易密码
                        doAfterSetTradePwd(result);
                        break;
                    case GET_EMAIL_CODE: // 获取邮箱验证码
                        doAfterGetCode(result, postTag);
                        break;
                    case GET_PHONE_COND: // 获取手机验证码
                        doAfterGetCode(result, postTag);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onFailure(Throwable t, Context context) // 请求成功失败
            // 已经是主线程，可以在这里进行UI操作
            {
                super.onFailure(t, context);
            }
        });
    }

    /**
     * 发送请求绑定手机
     *
     * @param result
     */
    private void doAfterCheckVerifyCode(JSONObject result) {
        try {
            String code = result.getString("code");

            if (DMConstant.ResultCode.SUCCESS.equals(code)) {
                httpParams.put("phone", newPhoneEditer.getText().toString());
                post(BIND_PHONE, DMConstant.API_Url.USER_SETUSERPHONE); // 发送请求绑定手机
            } else {
                ErrorUtil.showError(result);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取到邮箱验证码后
     */
    protected void doAfterGetCode(JSONObject result, int postTag) {
        DMLog.i("doAfterGetCode", result.toString());
        try {
            String code = result.getString("code");

            if (DMConstant.ResultCode.SUCCESS.equals(code)) {
                if (postTag == GET_EMAIL_CODE) {
                    String temp = "激活邮件已发送至您的邮箱：" + newEmailEditer.getText().toString()
                            + " 请在24小时内登录邮箱并点击邮件中的链接，完成验证。";
                    showSubVerify(BIND_EMAIL);
                    // 提示用户去打开邮件进行验证
                    AlertDialogUtil.alert(this, temp);
                } else {
                    startPhoneTimer(SECURITY_CODE_TIME);
                    ToastUtil.getInstant().show(this, R.string.find_pwd_send_success);
                }
            } else if (ErrorUtil.ErroreCode.ERROR_000019.equals(code)) {
                ToastUtil.getInstant().show(this, "该手机号码已存在");
            } else {
                ErrorUtil.showError(result);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 实名认证后
     *
     * @param result
     */
    protected void doAfterCertification(JSONObject result) {
        try {
            String code = result.getString("code");
            if (DMConstant.ResultCode.SUCCESS.equals(code)) {
                ToastUtil.getInstant().show(this, "实名认证成功");
                userInfo.setRealName(newNameEditer.getText().toString());
                userInfo.setIdCard(newIDEditer.getText().toString());
                // userInfo.setIdcardVerified(true);
                String name = userInfo.getRealName(); // 掩码显示
                if (name.length() == 2) {
                    name = name.substring(0, 1) + "*";
                } else if (name.length() == 3) {
                    name = name.substring(0, 1) + "*" + name.charAt(name.length() - 1);
                } else if (name.length() == 4) {
                    name = name.substring(0, 1) + "**" + name.charAt(name.length() - 1);
                } else if (name.length() == 5) {
                    name = name.substring(0, 1) + "***" + name.charAt(name.length() - 1);
                }
                //				nameView.setText(name);
                //				nameView.setTextColor(getResources().getColor(R.color.text_black_9));
                nameModifyView.setText(userInfo.getIdCard().substring(0, 3) + "**************");
                nameModifyView.setTextColor(getResources().getColor(R.color.text_black_9));
                //                nameModifyView.setClickable(false);

                // userInfo.setSafeLevel(userInfo.getSafeLevel() + 1);
                if (!StringUtils.isEmptyOrNull(userInfo.getUsrCustId())) // 实名认证需要第三方注册后才可以有进度显示
                {
                    userInfo.setSafeLevel(userInfo.getSafeLevel() + 1);
                } else {
                    userInfo.setSafeLevel(userInfo.getSafeLevel());
                }
                setUserSafeLevel(userInfo.getSafeLevel());
                idCardLayout.setVisibility(View.GONE);
                isCertificated = true;
            } else {
                if (code.equals("000003")) {
                    ToastUtil.getInstant().show(this, "请输入正确的身份证号");
                } else {
                    ErrorUtil.showError(result);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 绑定邮箱后
     *
     * @param result
     */
    protected void doAfterBindEmail(JSONObject result) {
        try {
            String code = result.getString("code");

            if (DMConstant.ResultCode.SUCCESS.equals(code)) {
                ToastUtil.getInstant().show(this, "绑定邮箱成功");
                userInfo.setEmail(newEmailEditer.getText().toString());
                // userInfo.setEmailVerified(true);
                int index = userInfo.getEmail().indexOf("@");
                int length = index - 2;
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < length; i++) {
                    sb.append("*");
                }
                emailView.setText(userInfo.getEmail().substring(0, 2) + sb.toString()
                        + userInfo.getEmail().substring(index));
                emailView.setTextColor(getResources().getColor(R.color.text_black_9));
                emailModifyView.setVisibility(View.GONE);
                emailLayout.setVisibility(View.GONE);
                userInfo.setSafeLevel(userInfo.getSafeLevel() + 1);
                setUserSafeLevel(userInfo.getSafeLevel());
            } else if (code.equals(ErrorUtil.ErroreCode.ERROR_000007)) {
                ToastUtil.getInstant().show(this, "您输入的验证码有误，请重试!");
            } else {
                ErrorUtil.showError(result);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 绑定手机后
     *
     * @param result
     */
    protected void doAfterBindPhone(JSONObject result) {
        DMLog.i("doAfterBindPhone", result.toString());
        try {
            String code = result.getString("code");

            if (DMConstant.ResultCode.SUCCESS.equals(code)) {
                ToastUtil.getInstant().show(this, "绑定手机成功");
                userInfo.setPhone(newPhoneEditer.getText().toString());
                // userInfo.setMobileVerified(true);
                String phone = userInfo.getPhone();
                phoneView.setText(phone.substring(0, 3) + "****"
                        + phone.substring(phone.length() - 4, phone.length()));
                phoneView.setTextColor(getResources().getColor(R.color.text_black_9));
                phoneModifyView.setVisibility(View.GONE);
                phoneLayout.setVisibility(View.GONE);
                userInfo.setSafeLevel(userInfo.getSafeLevel() + 1);
                setUserSafeLevel(userInfo.getSafeLevel());
            } else if (ErrorUtil.ErroreCode.ERROR_000007.equals(code)) {
                ToastUtil.getInstant().show(this, "您输入的验证码有误，请重试!");
            } else {
                ErrorUtil.showError(result);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置支付密码后
     *
     * @param result
     */
    protected void doAfterSetTradePwd(JSONObject result) {
        try {
            String code = result.getString("code");
            if (DMConstant.ResultCode.SUCCESS.equals(code)) {
                ToastUtil.getInstant().show(this, "设置交易密码成功");
                userInfo.setWithdrawPsw(true);
                tradePwdView.setText("已设置");
                tradePwdView.setTextColor(getResources().getColor(R.color.text_black_9));
                tradePwdModifyView.setVisibility(View.GONE);
                tradePwdLayout.setVisibility(View.GONE);
                userInfo.setSafeLevel(userInfo.getSafeLevel() + 1);
                setUserSafeLevel(userInfo.getSafeLevel());
            } else {// 新密码不能和登录密码相同！
                String description = result.getString("description");
                if (null != description && description.contains("不能和登录密码相同")) {
                    ToastUtil.getInstant().show(this, "交易密码不能与登录密码一致");
                } else {
                    ErrorUtil.showError(result);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 实名认证参数校验
     *
     * @return
     */
    private boolean verifiedIDPrams() {
        if (newNameEditer.getText().toString().isEmpty()) {
            AlertDialogUtil.alert(this, getString(R.string.account_namever_no_name))
                    .setCanceledOnTouchOutside(false);
            return false;
        } else if (newIDEditer.getText().toString().isEmpty()) {
            AlertDialogUtil.alert(this, getString(R.string.account_namever_no_id))
                    .setCanceledOnTouchOutside(false);
            return false;
        } else if (newNameEditer.getText().toString().trim().length() < 2
                || newNameEditer.getText().toString().trim().length() > 10) {
            AlertDialogUtil.alert(this, getString(R.string.realname_length_error))
                    .setCanceledOnTouchOutside(false);
            return false;
        } else if (!FormatUtil.validateRealName(newNameEditer.getText().toString())) {
            AlertDialogUtil.alert(this, getString(R.string.realname_format_erro))
                    .setCanceledOnTouchOutside(false);
            return false;
        } else if (newIDEditer.getText().toString().length() != 18
                && newIDEditer.getText().toString().length() != 15) {
            AlertDialogUtil.alert(this, getString(R.string.id_number_length_error))
                    .setCanceledOnTouchOutside(false);
            return false;
        } else {
            return true;
        }
    }

    /**
     * 显示认证设置
     *
     * @param tag
     */
    private void showSubVerify(int tag) {
        switch (tag) {
            case CERTIFICATION:
                DMLog.e(userInfo.toString());
                // 隐藏其它认证设置
                emailLayout.setVisibility(View.GONE);
                phoneLayout.setVisibility(View.GONE);
                tradePwdLayout.setVisibility(View.GONE);
                emailModifyView.setText(R.string.account_set_security);
                phoneModifyView.setText(R.string.account_set_security);
                tradePwdModifyView.setText(R.string.account_set_security);
                // 显示实名认证
                // if (idCardLayout.getVisibility() == View.VISIBLE)
                // {
                // idCardLayout.setVisibility(View.GONE);
                // nameModifyView.setText(R.string.account_set_security);
                // }
                // else
                // {
                // idCardLayout.setVisibility(View.VISIBLE);
                // nameModifyView.setText(R.string.account_unset_security);
                // }
                if (userInfo.getIdcardFrontVerified().equals("BTG")
                        || userInfo.getIdcardFrontVerified().equals("WYZ")
                        || userInfo.getIdcardInverseVerified().equals("BTG")
                        || userInfo.getIdcardInverseVerified().equals("WYZ") || userInfo.getIdcardFrontVerified()
                        .equals("TG")
                        || userInfo.getIdcardInverseVerified().equals("TG")) {
                    Intent intent = new Intent(this, SecurityPhotoActivity.class);
                    startActivity(intent);
                }
                if (userInfo.getIdcardFrontVerified().equals("DSH")
                        && userInfo.getIdcardInverseVerified().equals("DSH")) {
                    ToastUtil.getInstant().show(this, "请等待管理员审核！！！");
                }
                break;
            case BIND_EMAIL:
                // 隐藏其它认证设置
                idCardLayout.setVisibility(View.GONE);
                phoneLayout.setVisibility(View.GONE);
                tradePwdLayout.setVisibility(View.GONE);
                if (!isCertificated) {
                    nameModifyView.setText(R.string.account_set_security);
                }
                phoneModifyView.setText(R.string.account_set_security);
                tradePwdModifyView.setText(R.string.account_set_security);
                // 显示绑定邮箱
                if (emailLayout.getVisibility() == View.VISIBLE) {
                    emailLayout.setVisibility(View.GONE);
                    emailModifyView.setText(R.string.account_set_security);
                } else {
                    emailLayout.setVisibility(View.VISIBLE);
                    emailModifyView.setText(R.string.account_unset_security);
                }
                break;
            case BIND_PHONE:
                // 隐藏其它认证设置
                idCardLayout.setVisibility(View.GONE);
                emailLayout.setVisibility(View.GONE);
                tradePwdLayout.setVisibility(View.GONE);
                if (!isCertificated) {
                    nameModifyView.setText(R.string.account_set_security);
                }
                emailModifyView.setText(R.string.account_set_security);
                tradePwdModifyView.setText(R.string.account_set_security);
                // 显示绑定手机
                if (phoneLayout.getVisibility() == View.VISIBLE) {
                    phoneLayout.setVisibility(View.GONE);
                    phoneModifyView.setText(R.string.account_set_security);
                } else {
                    phoneLayout.setVisibility(View.VISIBLE);
                    phoneModifyView.setText(R.string.account_unset_security);
                }
                break;
            case SET_TRADE_PWD:
                // 隐藏其它认证设置
                idCardLayout.setVisibility(View.GONE);
                emailLayout.setVisibility(View.GONE);
                phoneLayout.setVisibility(View.GONE);
                if (!isCertificated) {
                    nameModifyView.setText(R.string.account_set_security);
                }
                emailModifyView.setText(R.string.account_set_security);
                phoneModifyView.setText(R.string.account_set_security);
                // 显示支付密码
                if (tradePwdLayout.getVisibility() == View.VISIBLE) {
                    tradePwdLayout.setVisibility(View.GONE);
                    tradePwdModifyView.setText(R.string.account_set_security);
                } else {
                    tradePwdLayout.setVisibility(View.VISIBLE);
                    tradePwdModifyView.setText(R.string.account_unset_security);
                }

                break;
            default:
                break;
        }
    }

}
