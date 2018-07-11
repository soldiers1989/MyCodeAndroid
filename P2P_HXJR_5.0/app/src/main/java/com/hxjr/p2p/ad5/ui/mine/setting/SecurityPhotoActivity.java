package com.hxjr.p2p.ad5.ui.mine.setting;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.dm.http.HttpCallBack;
import com.dm.http.HttpUtil;
import com.dm.photo.IntentEvent;
import com.dm.photo.utils.CameraPermissionCompat;
import com.dm.utils.DMLog;
import com.dm.utils.StringUtils;
import com.dm.widgets.utils.AlertDialogUtil;
import com.dm.widgets.utils.ToastUtil;
import com.hxjr.p2p.ad5.DMApplication;
import com.hxjr.p2p.ad5.R;
import com.hxjr.p2p.ad5.bean.UserInfo;
import com.hxjr.p2p.ad5.service.ApiUtil;
import com.hxjr.p2p.ad5.service.ApiUtil.OnGetUserInfoCallBack;
import com.hxjr.p2p.ad5.ui.BaseActivity;
import com.hxjr.p2p.ad5.utils.DMConstant;
import com.hxjr.p2p.ad5.utils.ErrorUtil;
import com.hxjr.p2p.ad5.utils.FormatUtil;
import com.hxjr.p2p.ad5.utils.HttpParams;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class SecurityPhotoActivity extends BaseActivity implements OnClickListener {

    private UserInfo userInfo;
    private EditText name_et;
    private EditText card_number_et;
    private ImageView inversePh;
    private ImageView frontPh;
    private File f_inverse;
    private File f_front;
    private Map<String, File> fileMap;
    private TextView title_text;
    private ImageView back_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security_photo);
        EventBus.getDefault().register(this);
        fileMap = new HashMap<String, File>();
        intview();
        if (DMApplication.getInstance().islogined()) {
            intData();
            Intent intent = new Intent(this, NoticeActivity.class);
            startActivity(intent);
        } else {
            this.finish();
        }
    }

    private void intData() {
        ApiUtil.getUserInfo(this);
        userInfo = DMApplication.getInstance().getUserInfo();
        if (null != userInfo && null != userInfo.getName() && null != userInfo.getIdCard()) {
            name_et.setText(userInfo.getName());
            card_number_et.setText(userInfo.getIdCard());
            name_et.setFocusable(false);
            name_et.setFocusableInTouchMode(false);
            card_number_et.setFocusable(false);
            card_number_et.setFocusableInTouchMode(false);
        }
        if (userInfo.getIdcardFrontVerified().equals("DSH")) {
            frontPh.setBackgroundResource(R.drawable.jpg_zhmdshh);
            frontPh.setEnabled(false);
            frontPh.setClickable(false);
        }
        if (userInfo.getIdcardInverseVerified().equals("DSH")) {
            inversePh.setBackgroundResource(R.drawable.jpg_fmdshh);
            inversePh.setEnabled(false);
            inversePh.setClickable(false);
        }
        if (userInfo.getIdcardFrontVerified().equals("TG")) {
            frontPh.setBackgroundResource(R.drawable.jpg_shhytg);
//            frontPh.setEnabled(false);
//            frontPh.setClickable(false);
        }
        if (userInfo.getIdcardInverseVerified().equals("TG")) {
            inversePh.setBackgroundResource(R.drawable.jpg_shhytg);
//            inversePh.setEnabled(false);
//            inversePh.setClickable(false);
        }
    }

    private void intview() {
        back_text = (ImageView) findViewById(R.id.btn_back);
        back_text.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                SecurityPhotoActivity.this.finish();
            }
        });
        name_et = (EditText) findViewById(R.id.name_et);
        title_text = (TextView) findViewById(R.id.title_text);
        title_text.setText("实名认证");
        card_number_et = (EditText) findViewById(R.id.card_number_et);
        inversePh = (ImageView) findViewById(R.id.inversePh);
        frontPh = (ImageView) findViewById(R.id.frontPh);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.inversePh:
                if (true != CameraPermissionCompat.cameraIsCanUse()) {
                    // 申请CAMERA权限
                    AlertDialog alert = AlertDialogUtil.alert(this, "华西金融未获得使用您相机的权限，请前往设置中打开华西金融的相机权限后再进行拍照！");
                    alert.setCancelable(false);
                    alert.setCanceledOnTouchOutside(false);
                } else {
                    Intent intent1 = new Intent(this, TakePhotoNewActivity.class);
                    intent1.putExtra("index", "1");
                    startActivity(intent1);
                }
                break;
            case R.id.frontPh:
                if (true != CameraPermissionCompat.cameraIsCanUse()) {
                    // 申请CAMERA权限
                    AlertDialog alert = AlertDialogUtil.alert(this, "华西金融未获得使用您相机的权限，请前往设置中打开华西金融的相机权限后再进行拍照！");
                    alert.setCancelable(false);
                    alert.setCanceledOnTouchOutside(false);
                } else {
                    Intent intent = new Intent(this, TakePhotoNewActivity.class);
                    intent.putExtra("index", "2");
                    startActivity(intent);
                }
                break;
            case R.id.submit_btn:
                fileMap.clear();
                HttpParams hp = new HttpParams();
                if (!userInfo.getIdcardVerified()) {
                    hp.put("name", name_et.getText().toString());
                    hp.put("idCard", card_number_et.getText().toString());
                    if (verifiedIDPrams()) {
                        if (f_front != null && (userInfo.getIdcardFrontVerified().equals("BTG") || userInfo
                                .getIdcardFrontVerified().equals("WYZ"))) {
                            fileMap.put("idcardFrontVerified", f_front);
                        }
                        if (f_inverse != null && (userInfo.getIdcardInverseVerified().equals("BTG") || userInfo
                                .getIdcardInverseVerified().equals("WYZ"))) {
                            fileMap.put("idcardInverseVerified", f_inverse);
                        }
                        DMLog.e(fileMap.toString());
                        HttpUtil.getInstance().post(this, DMConstant.API_Url.USER_SETUSERINFO2, hp,
                                fileMap, new HttpCallBack() {
                                    @Override
                                    public void onStart() {
                                        SecurityPhotoActivity.this.show();
                                    }

                                    @Override
                                    public void onSuccess(JSONObject result) {
                                        DMLog.e(result.toString());
                                        doAfterCertification(result);
                                        SecurityPhotoActivity.this.dismiss();
                                    }

                                    @Override
                                    public void onFailure(Throwable t, Context context) {
                                        SecurityPhotoActivity.this.dismiss();
                                    }
                                });
                    }
                } else {
                    if (f_front != null
                            && !(userInfo.getIdcardFrontVerified().equals("DSH") || userInfo
                            .getIdcardFrontVerified().equals("TG"))) {
                        fileMap.put("idcardFrontVerified", f_front);
                    }
                    if (f_inverse != null
                            && !(userInfo.getIdcardInverseVerified().equals("DSH") || userInfo
                            .getIdcardInverseVerified().equals("TG"))) {
                        fileMap.put("idcardInverseVerified", f_inverse);
                    }
                    DMLog.e(fileMap.toString());
                    HttpUtil.getInstance().post(this, DMConstant.API_Url.USER_SETUSERINFO2, hp,
                            fileMap, new HttpCallBack() {
                                @Override
                                public void onSuccess(JSONObject result) {
                                    DMLog.e(result.toString());
                                    doAfterCertification(result);
                                }
                            });
                }

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);// 反注册EventBus
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(IntentEvent event) {
        if (event.getIntent().getStringExtra("index").equals("1")) {
            inversePh.setImageURI(event.getIntent().getData());
            f_inverse = new File(event.getIntent().getStringExtra("path"));
            DMLog.e(f_inverse.toString());
        } else {
            frontPh.setImageURI(event.getIntent().getData());
            f_front = new File(event.getIntent().getStringExtra("path"));
            DMLog.e(f_front.toString());
        }
    }

    /**
     * 实名认证参数校验
     *
     * @return
     */
    private boolean verifiedIDPrams() {
        if (name_et.getText().toString().isEmpty()) {
            AlertDialogUtil.alert(this, getString(R.string.account_namever_no_name))
                    .setCanceledOnTouchOutside(false);
            return false;
        } else if (card_number_et.getText().toString().isEmpty()) {
            AlertDialogUtil.alert(this, getString(R.string.account_namever_no_id))
                    .setCanceledOnTouchOutside(false);
            return false;
        } else if (name_et.getText().toString().trim().length() < 2
                || name_et.getText().toString().trim().length() > 10) {
            AlertDialogUtil.alert(this, getString(R.string.realname_length_error))
                    .setCanceledOnTouchOutside(false);
            return false;
        } else if (!FormatUtil.validateRealName(name_et.getText().toString())) {
            AlertDialogUtil.alert(this, getString(R.string.realname_format_erro))
                    .setCanceledOnTouchOutside(false);
            return false;
        } else if (card_number_et.getText().toString().length() != 18
                && card_number_et.getText().toString().length() != 15) {
            AlertDialogUtil.alert(this, getString(R.string.id_number_length_error))
                    .setCanceledOnTouchOutside(false);
            return false;
        } else {
            return true;
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
                ToastUtil.getInstant().show(this, "图片上传成功!");
                userInfo.setRealName(name_et.getText().toString());
                userInfo.setIdCard(card_number_et.getText().toString());
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
                name_et.setText(name);
                name_et.setTextColor(getResources().getColor(R.color.text_black_9));
                card_number_et.setText(userInfo.getIdCard().substring(0, 3) + "**************");
                if (!StringUtils.isEmptyOrNull(userInfo.getUsrCustId())) // 实名认证需要第三方注册后才可以有进度显示
                {
                    userInfo.setSafeLevel(userInfo.getSafeLevel() + 1);
                } else {
                    userInfo.setSafeLevel(userInfo.getSafeLevel());
                }
                ApiUtil.getUserInfo(this, new OnGetUserInfoCallBack() {

                    @Override
                    public void onSuccess() {
                        userInfo = DMApplication.getInstance().getUserInfo();
                        if (userInfo.getIdcardFrontVerified().equals("BTG")
                                || userInfo.getIdcardFrontVerified().equals("WYZ")
                                || userInfo.getIdcardInverseVerified().equals("BTG")
                                || userInfo.getIdcardInverseVerified().equals("WYZ")) {
                            ToastUtil.getInstant().show(SecurityPhotoActivity.this, "请上传身份证照片完成认证");
                        }
                    }

                    @Override
                    public void onFailure() {
                    }
                });
                finish();
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
}
