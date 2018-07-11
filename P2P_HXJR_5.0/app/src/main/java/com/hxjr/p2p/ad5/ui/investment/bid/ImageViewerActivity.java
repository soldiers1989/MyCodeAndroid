package com.hxjr.p2p.ad5.ui.investment.bid;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.dm.universalimageloader.core.ImageLoader;
import com.hxjr.p2p.ad5.DMApplication;
import com.hxjr.p2p.ad5.R;

public class ImageViewerActivity extends Activity
{
	private ImageView imageContent;
    private ImageView closeBtn;
    private String imgUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
        setContentView(R.layout.activity_image_viewer);
        imgUrl = getIntent().getStringExtra("imgUrl");
        initView();
    }

    protected void initView() {
        imageContent = (ImageView) findViewById(R.id.imageContent);
        closeBtn = (ImageView) findViewById(R.id.closeBtn);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                setFinishAnim();
            }
        });
        // 网络获取图片去
        ImageLoader.getInstance().displayImage(imgUrl, imageContent, DMApplication.getInstance().getOptions());
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            setFinishAnim();
        }
        return super.onKeyDown(keyCode, event);
    }

    private void setFinishAnim() {
        overridePendingTransition(R.anim.slide_left_in, R.anim.zoom_out);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }
}
