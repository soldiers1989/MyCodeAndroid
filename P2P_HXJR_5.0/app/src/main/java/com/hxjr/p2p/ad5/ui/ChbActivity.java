package com.hxjr.p2p.ad5.ui;


import com.hxjr.p2p.ad5.R;
import com.hxjr.p2p.ad5.ui.mine.bank.BankCardManageActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class ChbActivity extends BaseActivity {

	private  TextView qtz;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_open_hb);
		qtz=(TextView) findViewById(R.id.tv_qtz);
		qtz.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
            	Intent intent=new Intent(ChbActivity.this, BankCardManageActivity.class);
            	startActivity(intent);
            	ChbActivity.this.finish();
			}
		});
	}
}
