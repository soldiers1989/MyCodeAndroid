package com.hxjr.p2p.ad5.ui.tg;

import java.text.DecimalFormat;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.style.ClickableSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.dm.android.pay.PaymentType;
import com.dm.http.HttpCallBack;
import com.dm.http.HttpUtil;
import com.dm.utils.DMLog;
import com.dm.utils.HtmlUtil;
import com.dm.utils.StringUtils;
import com.dm.widgets.TouchWebView;
import com.dm.widgets.utils.AlertDialogUtil;
import com.dm.widgets.utils.AlertDialogUtil.ConfirmListener;
import com.dm.widgets.utils.ToastUtil;
import com.hxjr.p2p.ad5.DMApplication;
import com.hxjr.p2p.ad5.R;
import com.hxjr.p2p.ad5.bean.Chargep;
import com.hxjr.p2p.ad5.bean.Fee;
import com.hxjr.p2p.ad5.bean.SQFee;
import com.hxjr.p2p.ad5.bean.UserInfo;
import com.hxjr.p2p.ad5.bean.YbFee;
import com.hxjr.p2p.ad5.service.ApiUtil;
import com.hxjr.p2p.ad5.service.ApiUtil.OnPostCallBack;
import com.hxjr.p2p.ad5.ui.BaseActivity;
import com.hxjr.p2p.ad5.ui.mine.setting.SecurityInfoActivity;
import com.hxjr.p2p.ad5.utils.DMConstant;
import com.hxjr.p2p.ad5.utils.ErrorUtil;
import com.hxjr.p2p.ad5.utils.FormatUtil;
import com.hxjr.p2p.ad5.utils.HttpParams;

/**
 * 托管充值页面
 * @author jiaohongyun
 *
 */
public class TgRechargeActivity extends BaseActivity implements OnClickListener
{
	private RadioGroup amountRadioGroup;
	
	private EditText amountEdit;
	
	private TextView recharge_fee;
	
	private TextView recharge_income;
	
	private Button btnRecharge;
	
	private UserInfo userInfo;
	
	/**
	 * 充值手续费
	 */
	private Chargep chargep;
	
	/**
	 * 默认托管方式（双乾）
	 * 需要统一在DMConstant中配置托管方式
	 * 会在onCreate方法中赋值
	 */
	private PaymentType paymentType = PaymentType.SHUANGQIAN;
	
	/**客服电话*/
	private String serverPhone = "400 -678- 55182";
	
	private SQFee sqFee;
	
	private YbFee ybFee;
	
	private TextView warm_title;
	
	private TouchWebView warm_tips;
	
	private double minFee = 2; //快捷充值最低手续不能低于2元
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tg_recharge);
		chargep = new Chargep(); //给一个初始值，避免报空指针异常
		sqFee = new SQFee();
		ybFee = new YbFee();
		userInfo = DMApplication.getInstance().getUserInfo();
		paymentType = DMConstant.Config.TG_PAYMENT_TYPE;//统一在DMConstant中配置托管方式
		initView();
		initListener();
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		ApiUtil.getUserInfo(this);
		queryFee();
	}
	
	@Override
	protected void initView()
	{
		super.initView();
		((TextView)findViewById(R.id.title_text)).setText(R.string.recharge);
		amountEdit = (EditText)findViewById(R.id.amountEdit);
		amountRadioGroup = (RadioGroup)findViewById(R.id.rechage_sel_amount);
		amountRadioGroup.check(R.id.amountRadio4);
		amountEdit.setText(((TextView)findViewById(amountRadioGroup.getCheckedRadioButtonId())).getText().toString());
		amountEdit.setSelection(amountEdit.getText().toString().length());
		recharge_fee = (TextView)findViewById(R.id.recharge_fee);
		recharge_income = (TextView)findViewById(R.id.recharge_income);
		btnRecharge = (Button)findViewById(R.id.btnRecharge);
		
		warm_title = (TextView)findViewById(R.id.warm_title);
		warm_tips = (TouchWebView) findViewById(R.id.warm_tips);
		
        warm_tips.getSettings().setLoadWithOverviewMode(true);
        warm_tips.getSettings().setJavaScriptEnabled(true);
        warm_tips.setBackgroundColor(getResources().getColor(R.color.main_bg)); // 设置背景色
        warm_tips.setWebViewClient(new MyWebViewClient());
        warm_tips.getSettings().setUserAgentString("Android/1.0");
		
		/*TextView tip_five = ((TextView)findViewById(R.id.tip_five));
		String phoneNumStr = (String)SharedPreferenceUtils.get(this, "consts", "tel", "");
		if (null != phoneNumStr)
		{
			serverPhone = phoneNumStr;
		}
		tip_five.setText(Html.fromHtml("5、如果充值金额没有及时到账，请联系客服，<a href='' class='blue'>" + serverPhone + "</a>。"));
		tip_five.setMovementMethod(TextViewFixTouchConsume.LocalLinkMovementMethod.getInstance());
		CharSequence text = tip_five.getText();
		if (text instanceof Spannable)
		{
			Spannable sp = (Spannable)tip_five.getText();
			int end = text.length();
			URLSpan[] urls = sp.getSpans(0, end, URLSpan.class);
			SpannableStringBuilder style = new SpannableStringBuilder(text);
			style.clearSpans(); // should clear old spans      
			for (URLSpan url : urls)
			{
				MyURLSpan myURLSpan = new MyURLSpan(url.getURL());
				style.setSpan(myURLSpan, sp.getSpanStart(url), sp.getSpanEnd(url), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
			tip_five.setText(style);
		}*/
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
	
	private void initListener()
	{
		amountRadioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId)
			{
				RadioButton button = (RadioButton)findViewById(checkedId);
				amountEdit.setText(button.getText().toString());
				amountEdit.setSelection(amountEdit.getText().toString().length());
			}
		});
		
		amountEdit.addTextChangedListener(new TextWatcher()
		{
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count)
			{
				if (s.toString().startsWith(".") || s.toString().startsWith("0")) // 不能以“.”或“0”开头 
				{
					amountEdit.setText("");
					return;
				}
				if (!s.toString().equals(""))
				{
					makeFee(Double.parseDouble(s.toString()));
				}
				else
				{
					recharge_fee.setText("0");
					recharge_income.setText("0");
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after)
			{
				
			}
			
			@Override
			public void afterTextChanged(Editable s)
			{
				
			}
		});
		btnRecharge.setOnClickListener(this);
		
	}
	
	/**
	 * 充值操作
	 */
	private void recharge()
	{
		if (checkPrams())
		{
			HttpParams params = new HttpParams();
			params.put("paymentInstitution", paymentType.name());
			params.put("amount", amountEdit.getText().toString());
			HttpUtil.getInstance().post(this, DMConstant.API_Url.PAY_CHARGE, params, new HttpCallBack()
			{
				@Override
				public void onSuccess(JSONObject result)
				{
					try
					{
						String code = result.getString("code");
						if (DMConstant.ResultCode.SUCCESS.equals(code))
						{
							String url = result.getJSONObject("data").getString("url").toString();
							Intent intent = new Intent(TgRechargeActivity.this, TgThirdWebActivity.class);
							intent.putExtra("url", url);
							intent.putExtra("message", "充值成功！");
							intent.putExtra("title", DMConstant.TgWebTitle.RECHARGE);
							startActivity(intent);
						}
						else if (ErrorUtil.ErroreCode.ERROR_000047.equals(code))
						{
							final String url = result.getJSONObject("data").getString("url").toString();
							AlertDialogUtil.confirm(TgRechargeActivity.this,
								result.getString("description"),
								"去授权",
								null,
								new ConfirmListener()
								{
									
									@Override
									public void onOkClick()
									{
										Intent intent = new Intent(TgRechargeActivity.this, TgThirdWebActivity.class);
										intent.putExtra("url", url);
										intent.putExtra("message", "授权成功！");
										intent.putExtra("title", DMConstant.TgWebTitle.SOUQUAN);
										startActivity(intent);
									}
									
									@Override
									public void onCancelClick()
									{
									}
								});
						}
						else
						{
							ErrorUtil.showError(result);
						}
					}
					catch (JSONException e)
					{
						e.printStackTrace();
					}
				}
				
				@Override
				public void onFailure(Throwable t, Context context)
				{
					DMLog.e("ssss");
				}
				
			});
		}
	}
	
	/**
	 * 检查参数
	 * @return
	 */
	private boolean checkPrams()
	{
		userInfo = DMApplication.getInstance().getUserInfo();
		if (null != userInfo)
		{//是否需要实名认证, 手机认证, 邮箱认证, 交易密码
			if ((StringUtils.isEmptyOrNull(userInfo.getRealName()) && chargep.isNeedNciic())
				|| (StringUtils.isEmptyOrNull(userInfo.getPhone()) && chargep.isNeedPhone())
				|| (StringUtils.isEmptyOrNull(userInfo.getEmail()) && chargep.isNeedEmail())
				|| (!userInfo.isWithdrawPsw() && chargep.isNeedPsd()))
			{
				AlertDialogUtil.confirm(this,
					getString(R.string.please_complete_security_info),
					"认证",
					"确定",
					new ConfirmListener()
					{
						@Override
						public void onOkClick()
						{
							startActivity(new Intent(TgRechargeActivity.this, SecurityInfoActivity.class).putExtra("isNeedPwd",
								chargep.isNeedPsd()));
						}
						
						@Override
						public void onCancelClick()
						{
						}
					});
				return false;
			}
		}
		if (amountEdit.getText().toString().isEmpty())
		{
			ToastUtil.getInstant().show(this, "充值金额不能为空");
			return false;
		}
		if (Double.parseDouble(amountEdit.getText().toString()) < chargep.getMin())
		{
			ToastUtil.getInstant().show(this, "充值金额最小为：" + FormatUtil.formatStr2(chargep.getMin() + "") + "元");
			return false;
		}
		return true;
	}
	
	@Override
	public void onClick(View v)
	{
		if (checkClick(v.getId()))
		{
			switch (v.getId())
			{
				case R.id.btnRecharge:
				{ //充值
					recharge();
					break;
				}
				default:
					break;
			}
		}
	}
	
	/**
	 * 计算手续费
	 * @param amount
	 */
	private void makeFee(double amount)
	{
		if (amount < chargep.getMin())
		{
			recharge_fee.setText("0.00");
			recharge_income.setText(FormatUtil.formatStr2(amount + ""));
			return;
		}
		if (amount > chargep.getMax())
		{
			ToastUtil.getInstant().show(this, "充值最大金额为：" + FormatUtil.formatStr2(chargep.getMax() + "") + "元");
			String temp = (int)chargep.getMax() + "";
			amountEdit.setText(temp);
			amountEdit.setSelection(temp.length());
			return;
		}
		double fee = 0.0d;
//		if (DMConstant.Config.TG_PAYMENT_TYPE == PaymentType.SHUANGQIAN)
//		{
//			//双乾快捷支付费率
//			fee = amount * sqFee.getKuaiJieRate();
//		}
//		else
//		{
//			fee = amount * chargep.getP();
//		}
////		fee = fee < 1 ? 1 : fee;
//		if (fee > chargep.getpMax())
//		{
//			fee = chargep.getpMax();
//		}
		if(ybFee.isChargeISPT()){  //是平台
			fee = 0;
		}else{
			fee = amount * ybFee.getChargeRate();	
			fee = fee > minFee ? fee : minFee;
		}
		double result = amount - fee;
		DecimalFormat df = new DecimalFormat("#0.00");
		recharge_fee.setText(df.format(fee));
		recharge_income.setText(df.format(result));
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
				if (null != fee.getChargep())
				{
					chargep = fee.getChargep();
				}
				if (null != fee.getSqFee())
				{
					sqFee = fee.getSqFee();
				}
				if(ybFee != fee.getYbFee()){
					ybFee = fee.getYbFee();
				}
//				String selectAmountStr =
//					((RadioButton)findViewById(amountRadioGroup.getCheckedRadioButtonId())).getText().toString();
				//充值回退时不选中默认选择的金额
				String selectAmountStr = amountEdit.getText().toString();
				makeFee(Integer.valueOf(selectAmountStr));
				
				if (null != chargep) {
					warm_title.setVisibility(View.VISIBLE);
                    HtmlUtil htmlUtil = new HtmlUtil();
                    htmlUtil.getHead()
                            .append("<link rel='stylesheet' href='file:///android_asset/css/tips.css' type='text/css'/>");
                    htmlUtil.getBody().append("<div id='content'>").append(chargep.getCwxts()).append("</div>");
                    warm_tips.loadDataWithBaseURL(null, htmlUtil.CreateHtml(), "text/html", "utf-8", null);
                }else{
                	warm_title.setVisibility(View.GONE);
                	warm_tips.setVisibility(View.GONE);
                }
			}
			
			@Override
			public void onFailure()
			{
				
			}
		});
	}
	
	class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            //			view.loadUrl(url); //在当前的webview中跳转到新的url
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }
    }
}
