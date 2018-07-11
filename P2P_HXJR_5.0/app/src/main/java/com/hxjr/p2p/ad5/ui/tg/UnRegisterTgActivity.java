package com.hxjr.p2p.ad5.ui.tg;

import com.hxjr.p2p.ad5.DMApplication;
import com.hxjr.p2p.ad5.bean.Fee;
import com.hxjr.p2p.ad5.service.ApiUtil;
import com.hxjr.p2p.ad5.service.ApiUtil.OnPostCallBack;
import com.hxjr.p2p.ad5.ui.BaseActivity;
import com.hxjr.p2p.ad5.utils.SharedPreferenceUtils;
import com.hxjr.p2p.ad5.utils.UIHelper;
import com.hxjr.p2p.ad5.R;
import com.dm.widgets.TextViewFixTouchConsume;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

/**
 * 没有在第三方托管平台上进行注册
 * @author jiaohongyun
 *
 */
public class UnRegisterTgActivity extends BaseActivity
{
//	private TextView tip_five;
	
	/**
	 * 标题
	 */
	private String title;
	
	/**是否需要交易密码*/
	private boolean isNeedPwd = true;
	
	/***
	 * 是否需要邮箱认证
	 */
	private boolean isNeedEmailRZ = true;
	
	private String serverPhone = "400 -678- 55182";
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.un_register_tg);
		title = getIntent().getStringExtra("title");
		initView();
	}
	
	@Override
	protected void initView()
	{
		super.initView();
		((TextView)findViewById(R.id.title_text)).setText(title);
		((TextView)findViewById(R.id.description_tv)).setText(getString(R.string.third_register_description, title));
//		tip_five = ((TextView)findViewById(R.id.tip_five));
		findViewById(R.id.register_btn).setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if (checkSave())
				{
					startActivity(new Intent(UnRegisterTgActivity.this, TgThirdWebActivity.class));
				}
			}
		});
		
		String phoneNumStr = (String)SharedPreferenceUtils.get(this, "consts", "tel", "");
		if (null != phoneNumStr)
		{
			serverPhone = phoneNumStr;
		}
//		tip_five.setText(Html.fromHtml("5、如果充值金额没有及时到账，请联系客服，<a href='' class='blue'>" + serverPhone + "</a>。"));
//		tip_five.setMovementMethod(TextViewFixTouchConsume.LocalLinkMovementMethod.getInstance());
//		CharSequence text = tip_five.getText();
//		if (text instanceof Spannable)
//		{
//			Spannable sp = (Spannable)tip_five.getText();
//			int end = text.length();
//			URLSpan[] urls = sp.getSpans(0, end, URLSpan.class);
//			SpannableStringBuilder style = new SpannableStringBuilder(text);
//			style.clearSpans(); // should clear old spans      
//			for (URLSpan url : urls)
//			{
//				MyURLSpan myURLSpan = new MyURLSpan(url.getURL());
//				style.setSpan(myURLSpan, sp.getSpanStart(url), sp.getSpanEnd(url), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//			}
//			tip_five.setText(style);
//		}
	}
	
	private class MyURLSpan extends ClickableSpan
	{
		MyURLSpan(String url)
		{
		}
		
		@Override
		public void onClick(View widget)
		{//拨打电话
			String phone = serverPhone.replace("-", "").replace(" ", "").trim();
			Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
			startActivity(intent);
		}
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		queryFee();
		ApiUtil.getUserInfo(this);
	}
	
	protected boolean checkSave()
	{
		if (!UIHelper.hasCompletedSecurityInfo(this, DMApplication.getInstance().getUserInfo(), isNeedEmailRZ, isNeedPwd))
		{
			return false;
		}
		return true;
	}
	
	/**
	 * 查询提现手续费和充值手续费
	 */
	private void queryFee()
	{
		ApiUtil.getFee(this, new OnPostCallBack()
		{
			@Override
			public void onSuccess(Fee fee)
			{
				isNeedPwd = fee.getChargep().isNeedPsd();
				isNeedEmailRZ = fee.getBaseInfo().isNeedEmailRZ();
			}
			
			@Override
			public void onFailure()
			{
				isNeedPwd = true;
				isNeedEmailRZ = true;
			}
		});
	}
	
}
