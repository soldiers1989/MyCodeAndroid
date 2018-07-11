package com.hxjr.p2p.ad5.ui;

import com.hxjr.p2p.ad5.R;

import android.os.Bundle;
import android.widget.TextView;

public class AboutUsActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about_us);
		((TextView)findViewById(R.id.title_text)).setText(R.string.other_about_us);
		initView();
	}
}
