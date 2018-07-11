package com.hxjr.p2p.ad5.ui;

import com.hxjr.p2p.ad5.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class TextDetilActivity extends Activity {

	private TextView btn;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bid_detail_text);
		btn=(TextView) findViewById(R.id.tv_btn);
		btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
            	Intent intent=new Intent(TextDetilActivity.this, BuyTextActivity.class);
            	startActivity(intent);
            	TextDetilActivity.this.finish();
			}
		} );
	}

}
