package com.hxjr.p2p.ad5.ui;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.dm.utils.DMLog;
import com.hxjr.p2p.ad5.R;

public class RedPacketActivity extends BaseActivity{

    private FrameLayout flContainer;
    private LinearLayout llyFront;
    private LinearLayout llyBack;
    private boolean isShowFront = true;
//    /**
//     * 按返回键时的时间
//     */
//    private long mExitTime;

    private AnimatorSet mFrontAnimator;
    private AnimatorSet mBackAnimator;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DMLog.e("RedPacketActivity", "onCreate");
		setContentView(R.layout.hb_layout);
        initsView();
        initAnimator();
        setAnimatorListener();
        setCameraDistance();
	}

    @Override
    protected void onPause() {
        DMLog.e("RedPacketActivity", "onPause");
        super.onPause();
    }

    @Override
    protected void onStop() {
        DMLog.e("RedPacketActivity", "onStop");
        super.onStop();
    }

    /**
     * 初始化View
     */
    private void initsView() {

        flContainer = (FrameLayout)findViewById(R.id.fl_container);
        llyFront = (LinearLayout) findViewById(R.id.lly_front);
        llyBack = (LinearLayout) findViewById(R.id.lly_back);
    }
    /**
     * 1.初始化动画
     */
    private void initAnimator() {
        mFrontAnimator = (AnimatorSet) AnimatorInflater.loadAnimator(this,
                R.animator.anim_in);
        mBackAnimator = (AnimatorSet) AnimatorInflater.loadAnimator(this,
                R.animator.anim_out);
    }

    /**
     * 2.设置视角间距，防止旋转时超出边界区域
     */

    private void setCameraDistance() {
        int distance = 6000;
        float scale = getResources().getDisplayMetrics().density * distance;
        llyFront.setCameraDistance(scale);
        llyBack.setCameraDistance(scale);
    }
    /**
     * 3.设置动画监听事件
     */
    private void setAnimatorListener() {

        mFrontAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                flContainer.setClickable(false);
            }
            @Override
            public void onAnimationEnd(Animator animation) {
            	Intent intent=new Intent(RedPacketActivity.this, ChbActivity.class);
            	startActivity(intent);
            	RedPacketActivity.this.finish();
            }
        });

        mBackAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                flContainer.setClickable(false);
            }
        });
    }

    @Override
    protected void onDestroy() {
    	DMLog.e("RedPacketActivity", "onDestroy");
    	super.onDestroy();
    }
    
    /**
     * 4.开启动画
     * @param view
     */
    public void startAnimation(View view) {

        //显示正面
        if(!isShowFront) {
            mFrontAnimator.setTarget(llyFront);
            mBackAnimator.setTarget(llyBack);
            mFrontAnimator.start();
            mBackAnimator.start();
            isShowFront = true;
        } else {
            mFrontAnimator.setTarget(llyBack);
            mBackAnimator.setTarget(llyFront);
            mFrontAnimator.start();
            mBackAnimator.start();
            isShowFront = false;
        }
    }
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        //按两次返回退出应用程序
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            if ((System.currentTimeMillis() - mExitTime) > 2000) {
//                ToastUtil.getInstant().show(this, R.string.app_exit);
//                mExitTime = System.currentTimeMillis();
//            } else {
//                AppManager.getAppManager().AppExit(this);
//            }
//            return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }
}
