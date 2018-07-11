package com.hxjr.p2p.ad5.ui;

import com.dm.utils.DMLog;
import com.hxjr.p2p.ad5.R;
import com.hxjr.p2p.ad5.ui.mine.setting.gesture.UnlockGesturePasswordActivity;
import com.hxjr.p2p.ad5.utils.DMConstant;
import com.hxjr.p2p.ad5.utils.SharedPreferenceUtils;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

import static com.hxjr.p2p.ad5.R.string.update;

public class AdvertiseActivity extends BaseActivity {

    // 声明控件对象
    private TextView textView;
    private int count = 2;
    private Animation animation;
    private boolean isJump = false;
    private RelativeLayout we_fl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        we_fl = (RelativeLayout) findViewById(R.id.we_fl);
        we_fl.setBackground(SplashActivity.myda);
        textView = (TextView) findViewById(R.id.textView); // 初始化控件对象
        animation = AnimationUtils.loadAnimation(this, R.anim.animation_text);
        handler.sendEmptyMessageDelayed(0, 1000);
    }

    private void getCount() {
        count--;
        textView.setText(count + "");
        animation.reset();
        textView.startAnimation(animation);
    }

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            boolean hasLocked = (Boolean) SharedPreferenceUtils.get(AdvertiseActivity.this,
                    SharedPreferenceUtils.KEY_HAS_LOCKED, false);
            boolean isFirstRun = (Boolean) SharedPreferenceUtils.get(AdvertiseActivity.this,
                    SharedPreferenceUtils.KEY_IS_FIRST_RUN, true);
            if (msg.what == 0) {
                if (count != 0) {
                    if (isJump) {
                        if (SplashActivity.updateda == null) {
                            if (isFirstRun && DMConstant.Config.HAS_WELCOME) {
                                // 第一次安装使用,并且需要显示欢迎、指引页
                                Intent intent = new Intent(AdvertiseActivity.this,
                                        WelcomeActivity.class);
                                startActivity(intent);
                                finish();
                            } else if (hasLocked) {
                                // 跳到解锁页面
                                Intent intent = new Intent(AdvertiseActivity.this,
                                        UnlockGesturePasswordActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                // 跳转到首页
                                Intent intent = new Intent(AdvertiseActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        } else {
                            //服务器维护中
                            Intent intent = new Intent(AdvertiseActivity.this, MaintainActivity.class);
                            startActivity(intent);
                            finish();
                        }

                    } else {
                        getCount();
                        handler.sendEmptyMessageDelayed(0, 1000);
                    }
                } else {
                    handler.sendEmptyMessage(1);
                }
            } else if (msg.what == 1) {
                if (SplashActivity.updateda == null) {
                    if (isFirstRun && DMConstant.Config.HAS_WELCOME) {
                        // 第一次安装使用,并且需要显示欢迎、指引页
                        Intent intent = new Intent(AdvertiseActivity.this, WelcomeActivity.class);
                        startActivity(intent);
                        finish();
                    } else if (hasLocked) {
                        // 跳到解锁页面
                        Intent intent = new Intent(AdvertiseActivity.this,
                                UnlockGesturePasswordActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        // 跳转到首页
                        Intent intent = new Intent(AdvertiseActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } else {
                    //服务器维护中
                    Intent intent = new Intent(AdvertiseActivity.this, MaintainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }

        ;
    };

    public void onJump(View view) {
        isJump = true;
    }
}
