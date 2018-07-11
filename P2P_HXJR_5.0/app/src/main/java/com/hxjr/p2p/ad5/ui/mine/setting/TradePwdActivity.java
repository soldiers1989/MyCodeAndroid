package com.hxjr.p2p.ad5.ui.mine.setting;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.dm.http.DMException;
import com.dm.http.HttpCallBack;
import com.dm.http.HttpUtil;
import com.dm.utils.DMJsonObject;
import com.dm.widgets.utils.AlertDialogUtil;
import com.dm.widgets.utils.ToastUtil;
import com.hxjr.p2p.ad5.R;
import com.hxjr.p2p.ad5.ui.BaseActivity;
import com.hxjr.p2p.ad5.ui.mine.WithdrawWebActivity;
import com.hxjr.p2p.ad5.utils.DMConstant;
import com.hxjr.p2p.ad5.utils.FormatUtil;
import com.hxjr.p2p.ad5.utils.HttpParams;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * 交易密码验证
 *
 * @author jiaohongyun
 * @date 2017年06月22日
 */
public class TradePwdActivity extends BaseActivity {
    private boolean isdowning;

    /**
     * 手机验证码倒计时
     */
    private CountDownTimer phoneDownTimer;


    /**
     * 验证码
     */
    private EditText codeEdit;

    /**
     * 获取验证码按钮
     */
    private Button btn;

    /**
     * 倒计时所剩时间
     */
    private long remainTime = 60000L;

    /**
     * 最后停止时间
     */
    private long lastTime;

    private String userName;

    private EditText userName_et;

    private EditText idCard_et;

    private EditText phoneNum_et;

    private String idCard;

    private boolean isCilckGetCode = false;

    private String phoneNum;

    private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tradepwd);
        initView();
        initData();
    }

    @Override
    protected void initView() {
        super.initView();
        title = (TextView) findViewById(R.id.title_text);
        title.setText("交易密码身份验证");
        phoneNum_et = (EditText) findViewById(R.id.phoneNum_et);
        codeEdit = (EditText) findViewById(R.id.verify_code);
        userName_et = (EditText) findViewById(R.id.user_name_et);
        idCard_et = (EditText) findViewById(R.id.idcard_num_et);
        btn = (Button) findViewById(R.id.get_code_btn);
    }

    /**
     * 进入下一步
     *
     * @param v
     */
    public void next(View v) {
        if (checkClick(v.getId())) //防重复点击
        {
            userName = userName_et.getText().toString();
            idCard = idCard_et.getText().toString();
            phoneNum = phoneNum_et.getText().toString();
            if (checkVerifyCodeParams(true)) {
                // 判断验证码是否正确
                HttpParams httpParams = new HttpParams();
                httpParams.put("phone", phoneNum);
                httpParams.put("verifyCode", codeEdit.getText().toString());
                httpParams.put("type", "ZHZF");
                HttpUtil.getInstance().post(this, DMConstant.API_Url.CHECK_VERIFY_CODE, httpParams, new HttpCallBack() {
                    @Override
                    public void onSuccess(JSONObject result) {
                        try {
                            String code = result.getString("code");

                            if (DMConstant.ResultCode.SUCCESS.equals(code)) {
                                //跳转到补充页面
                                //								Intent intent = new Intent(TradePwdActivity.this,
                                // SupplementInfoActivity.class);
                                //								intent.putExtra("phone", phoneEdit.getText().toString
                                // ());
                                //								intent.putExtra("verifyCode", codeEdit.getText()
                                // .toString());
                                //								startActivity(intent);
                                //								finish();
                                HttpParams httpParams = new HttpParams();
                                httpParams.put("phone", phoneNum);
                                httpParams.put("name", userName);
                                httpParams.put("idCard", idCard);

                                HttpUtil.getInstance().post(TradePwdActivity.this, DMConstant.API_Url
                                                .SETPWD, httpParams,
                                        new HttpCallBack() {
                                            @Override
                                            public void onSuccess(JSONObject result) {
                                                try {

                                                    String code = result.getString("code");
                                                    if (DMConstant.ResultCode.SUCCESS.equals(code)) {
                                                        JSONObject resNew = new JSONObject(result.getString("data")
                                                                .toString());
                                                        String url = resNew.getString("url");
                                                        if (url != null) {
                                                            Intent intent = new Intent(TradePwdActivity.this,
                                                                    WithdrawWebActivity.class);
                                                            intent.putExtra("linkUrl", url);
                                                            intent.putExtra("title", "设置交易密码");
                                                            startActivity(intent);
                                                            TradePwdActivity.this.finish();
                                                        }
                                                    } else {
                                                        AlertDialogUtil.alert(TradePwdActivity.this, result.getString
                                                                ("description"))
                                                                .setCanceledOnTouchOutside(false);
                                                        codeEdit.setText("");
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


                            } else {
                                // 验证码错误
                                AlertDialogUtil.alert(TradePwdActivity.this, result.getString("description"))
                                        .setCanceledOnTouchOutside(false);
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
                    public void onConnectFailure(DMException dmE, Context context) {
                        ToastUtil.getInstant().show(TradePwdActivity.this, dmE.getDescription());
                    }
                });
            }
        }
    }

    /**
     * 获手机取验证码
     *
     * @param v
     */
    public void getVerifyCode(View v) {
        if (checkClick(v.getId())) //防重复点击
        {
            phoneNum = phoneNum_et.getText().toString();
            if (checkVerifyCodeParams(false)) {
                isCilckGetCode = true;
                // 发送验证码到手机
                HttpParams httpParams = new HttpParams();
                httpParams.put("phone", phoneNum);
                httpParams.put("type", "ZHZF");//注册
                //				httpParams.put("type", "ZHZF");//找回密码
                HttpUtil.getInstance().post(this, DMConstant.API_Url.GETMOBILECODE, httpParams, new HttpCallBack() {
                    @Override
                    public void onSuccess(JSONObject result) {
                        try {
                            String code = result.getString("code");

                            if (DMConstant.ResultCode.SUCCESS.equals(code)) {
                                startDownTimer(60000L);
                                // 下发验证码成功
                                AlertDialogUtil.alert(TradePwdActivity.this, getString(R.string.find_pwd_send_success))
                                        .setCanceledOnTouchOutside(false);
                                if (!"".equals(result.getString("data"))) {
                                    DMJsonObject data = new DMJsonObject(result.getString("data"));
                                    //									userId = data.getString("userId");
                                } else {
                                    //									userId = phoneNum;
                                }
                            } else {
                                //下发验证码失败
                                AlertDialogUtil.alert(TradePwdActivity.this, result.getString("description"))
                                        .setCanceledOnTouchOutside(false);
                            }
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }

                });
            }
        }
    }

    /**
     * 检查获取验证码参数
     *
     * @return
     */
    private boolean checkVerifyCodeParams(boolean checkSecurityCode) {
        if (phoneNum.isEmpty()) {
            //手机号码为空
            AlertDialogUtil.alert(TradePwdActivity.this, getString(R.string.find_pwd_phone_empty))
                    .setCanceledOnTouchOutside(false);
            return false;
        }

        if (!FormatUtil.isMobileNO(phoneNum)) {
            //手机号码格式不正确
            AlertDialogUtil.alert(TradePwdActivity.this, getString(R.string.find_pwd_phone_format_error))
                    .setCanceledOnTouchOutside(false);
            return false;
        }

        if (checkSecurityCode) {
            if (!isCilckGetCode) {
                AlertDialogUtil.alert(TradePwdActivity.this, "您还没获取验证码，请先获取验证码").setCanceledOnTouchOutside(false);
                return false;
            }
            if (codeEdit.getText().toString().trim().length() == 0) {
                AlertDialogUtil.alert(TradePwdActivity.this, getString(R.string.find_pwd_code_empty))
                        .setCanceledOnTouchOutside(false);
                return false;
            }
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        if (phoneDownTimer != null) {
            lastTime = System.currentTimeMillis();
            phoneDownTimer.cancel();
        }
        super.onDestroy();
    }

    private void initData() {
        long currentTime = System.currentTimeMillis();
        long temp = remainTime - (currentTime - lastTime);
        if (temp > 0) {
            startDownTimer(temp);
        }
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
                btn.setText(getString(R.string.find_pwd_wait_time, temp));
                btn.setEnabled(false);
            }

            @Override
            public void onFinish() {
                isdowning = false;
                btn.setEnabled(true);
                btn.setText(getString(R.string.get_verify_code));
            }
        }.start();
    }

}
