package com.hxjr.p2p.ad5.ui.mine.setting;

import com.hxjr.p2p.ad5.R;
import com.hxjr.p2p.ad5.ui.BaseActivity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class NoticeActivity extends BaseActivity implements OnClickListener  {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notice);
	}
	@Override
	public void onClick(View arg0) {
		this.finish();
	}	
}
