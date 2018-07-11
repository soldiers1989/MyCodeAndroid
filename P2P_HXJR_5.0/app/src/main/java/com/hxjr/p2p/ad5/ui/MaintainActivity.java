package com.hxjr.p2p.ad5.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dm.utils.DMLog;
import com.hxjr.p2p.ad5.R;
import com.hxjr.p2p.ad5.utils.Method;
import com.hxjr.p2p.ad5.utils.UpdateManager;

public class MaintainActivity extends BaseActivity {
    private static final String LOG_TAG = MaintainActivity.class.getCanonicalName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maintain);
        ((TextView)findViewById(R.id.title_text)).setText(R.string.maintain);
        ImageView imageView=(ImageView) findViewById(R.id.mainrain);
        ImageView backImage=(ImageView) findViewById(R.id.btn_back);
        backImage.setVisibility(View.GONE);
        imageView.setBackground(SplashActivity.updateda);
        checkVersion();
    }
        /**
         * 检查版本
         */
        private void checkVersion() {
            DMLog.e(LOG_TAG, "checkVersion()");
            UpdateManager.getInstance().checkForUpdate(this, Method.getVersion(this), false); //检测更新
        }
}
