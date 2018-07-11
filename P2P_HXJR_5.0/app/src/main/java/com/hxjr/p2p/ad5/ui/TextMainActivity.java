package com.hxjr.p2p.ad5.ui;

import android.os.Bundle;
import android.widget.Button;

import com.dm.widgets.FanProgressBar;
import com.hxjr.p2p.ad5.R;

public class TextMainActivity extends BaseActivity {

	private Button btn;
	private FanProgressBar fanProgressBar;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_page_home_text);
//		btn=(Button) findViewById(R.id.btn_continue);
//		fanProgressBar=(FanProgressBar) findViewById(R.id.fanProgressBar);
//		fanProgressBar.setProgress(0);
//		btn.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View arg0) {
//            	Intent intent=new Intent(TextMainActivity.this, TextDetilActivity.class);
//            	startActivity(intent);
//            	TextMainActivity.this.finish();
//			}
//		});
	}
}
