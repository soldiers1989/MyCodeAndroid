package com.hxjr.p2p.ad5.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.hxjr.p2p.ad5.R;

public class BuyTextActivity extends BaseActivity {

	private TextView tv_money;
	private TextView tv_btn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.buy_bid_text);
		tv_money = (TextView) findViewById(R.id.investAmountEdits);
		tv_btn = (TextView) findViewById(R.id.tv_btn);
		tv_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (tv_money.getText().toString().equals("10000")) {
					Intent intent =new Intent(BuyTextActivity.this,MainActivity.class);
					startActivity(intent);
					BuyTextActivity.this.finish();
				} else {
					tv_money.setText("10000");
//					AppManager.getAppManager().finishActivity(RedPacketActivity.class);
				}
			}
		});
	}

}
