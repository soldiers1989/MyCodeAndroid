package com.hxjr.p2p.ad5.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.hxjr.p2p.ad5.DMApplication;
import com.hxjr.p2p.ad5.R;
import com.hxjr.p2p.ad5.utils.SharedPreferenceUtils;

public class UpdateNoticeActivity extends BaseActivity implements View.OnClickListener{
    private static final String TAG = "UpdateNoticeActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_notice);
    }
    @Override
    public void onClick(View arg0) {
        this.finish();
        SharedPreferenceUtils.put(this,SharedPreferenceUtils.KEY_IS_UPDATE,false);
        DMApplication.getInstance().isUpdate=false;
        Log.e(TAG, "onClick: "+DMApplication.getInstance().isUpdate );
    }
}
