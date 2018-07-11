package com.hxjr.p2p.ad5.ui.discovery.donation;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.dm.http.HttpCallBack;
import com.dm.http.HttpUtil;
import com.dm.utils.AppManager;
import com.dm.utils.DMJsonObject;
import com.dm.utils.StringUtils;
import com.dm.widgets.utils.AlertDialogUtil;
import com.dm.widgets.utils.AlertDialogUtil.AlertListener;
import com.dm.widgets.utils.AlertDialogUtil.ConfirmListener;
import com.dm.widgets.utils.ToastUtil;
import com.hxjr.p2p.ad5.DMApplication;
import com.hxjr.p2p.ad5.R;
import com.hxjr.p2p.ad5.bean.AccountBean;
import com.hxjr.p2p.ad5.bean.Fee;
import com.hxjr.p2p.ad5.service.ApiUtil;
import com.hxjr.p2p.ad5.service.ApiUtil.OnPostCallBack;
import com.hxjr.p2p.ad5.ui.BaseActivity;
import com.hxjr.p2p.ad5.ui.mine.RechargeActivity;
import com.hxjr.p2p.ad5.ui.mine.setting.FindTradePwdActivity;
import com.hxjr.p2p.ad5.ui.tg.TgRechargeActivity;
import com.hxjr.p2p.ad5.ui.tg.TgThirdWebActivity;
import com.hxjr.p2p.ad5.utils.AgreementManager;
import com.hxjr.p2p.ad5.utils.DMConstant;
import com.hxjr.p2p.ad5.utils.ErrorUtil;
import com.hxjr.p2p.ad5.utils.FormatUtil;
import com.hxjr.p2p.ad5.utils.HttpParams;
import com.hxjr.p2p.ad5.utils.UIHelper;
import com.hxjr.p2p.ad5.utils.UIHelper.OnDealPwdOkListener;

/**
 * 捐助投标
 * @author  huangkaibo
 * @date 2015年11月18日
 */
public class DonationBidActivity extends BaseActivity implements OnClickListener
{
	
	private Button donationBuyBid;
	
	private TextView maxAmount_tv;
	
	private TextView accountAmount_tv;
	
	private EditText donationAmountEdit;
	
	/**
	 * 最小可投金额
	 */
	private int minBidingAmount = 100;
	
	/**
	 * 剩余可投金额
	 */
	private String remainAmount;
	
	private String loanAmount;
	
	private String bidId;
	
	/**
	 * 协议管理类
	 */
	private AgreementManager agreementManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.donation_bid);
		initView();
		initData();
	}
	
	@Override
	protected void initView()
	{
		super.initView();
		((TextView)findViewById(R.id.title_text)).setText(R.string.donation_bid);
		
		donationBuyBid = (Button)findViewById(R.id.donationBuyBid);
		donationBuyBid.setOnClickListener(this);
		maxAmount_tv = (TextView)findViewById(R.id.maxAmount_tv);
		accountAmount_tv = (TextView)findViewById(R.id.accountAmount_tv);
		donationAmountEdit = (EditText)findViewById(R.id.donationAmountEdit);
		donationAmountEdit.addTextChangedListener(new TextWatcher()
		{
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count)
			{
				if (s.toString().startsWith("."))
				{
					donationAmountEdit.setText("");
					return;
				}
				String temp = s.toString(); //保证只有两位小数
				if (temp.contains(".") && temp.substring(temp.indexOf(".")).length() > 3)
				{
					donationAmountEdit.setText(temp.substring(0, temp.indexOf(".") + 3));
					donationAmountEdit.setSelection(donationAmountEdit.getText().toString().length());
					return;
				}
				if (!temp.equals("") && temp.lastIndexOf(".") != temp.length() - 1
					&& (!TextUtils.isEmpty(remainAmount) && Double.parseDouble(temp) > Double.parseDouble(remainAmount)))
				{//输入框中不为空，以及最后一个不是小数点; 输入的内容大于剩余可投金额时
					donationAmountEdit.setText(FormatUtil.formatStr2(remainAmount));
					donationAmountEdit.setSelection(donationAmountEdit.getText().toString().length());
					ToastUtil.getInstant().show(DonationBidActivity.this, "剩余可捐助金额为" + FormatUtil.formatStr2(remainAmount) + "元");
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
		agreementManager = new AgreementManager(this, AgreementManager.TYPE_GYB);
		agreementManager.initView();
	}
	
	private void initData()
	{
		remainAmount = getIntent().getStringExtra("remainAmount");
		minBidingAmount = getIntent().getIntExtra("minBidingAmount", 100);
		//		loanAmount = getIntent().getStringExtra("loanAmount");
		maxAmount_tv.setText(this.getString(R.string.donation_max, remainAmount));
		bidId = getIntent().getStringExtra("bidId");
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		queryFee();
		getAccountInfo();
		ApiUtil.getUserInfo(this);
		if (agreementManager != null)
		{
			agreementManager.initData();
		}
	}
	
	/**
	 * 提现是否需要交易密码
	 */
	private boolean isNeedPsw;
	
	/***
	 * 是否必须完成邮箱认证
	 */
	private boolean isNeedEmailRZ;
	
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
				isNeedPsw = fee.getChargep().isNeedPsd();
				isNeedEmailRZ = fee.getBaseInfo().isNeedEmailRZ();
			}
			
			@Override
			public void onFailure()
			{
				isNeedPsw = true;
				isNeedEmailRZ = true;
			}
		});
	}
	
	/***
	 * 个人账户信息
	 */
	private AccountBean accountInfo;
	
	/**
	 * 查询账户信息
	 */
	private void getAccountInfo()
	{
		HttpUtil.getInstance().post(this, DMConstant.API_Url.USER_ACCOUNT, new HttpCallBack()
		{
			@Override
			public void onSuccess(JSONObject result)
			{
				try
				{
					String code = result.getString("code");
					if (code.equals(DMConstant.ResultCode.SUCCESS))
					{
						DMJsonObject data = new DMJsonObject(result.getString("data"));
						accountInfo = new AccountBean(data);
						
						//设置账户余额
						String terms =
							getString(R.string.bid_buy_user_amount, accountInfo.getOverAmount().replace(",", "").trim());
						accountAmount_tv.setText(UIHelper.makeSpannableStr(DonationBidActivity.this, terms, terms.length() - 1
							- accountInfo.getOverAmount().length(), terms.length() - 1));
						
						double remainAmount = Double.valueOf(DonationBidActivity.this.remainAmount);
						if (remainAmount < minBidingAmount)
						{//剩余可投金额小于最小可投金额
							donationAmountEdit.setHint("金额范围  " + 0 + "~" + FormatUtil.formatStr2(minBidingAmount + ""));
						}
						else
						{
							donationAmountEdit.setHint("金额范围  " + FormatUtil.formatStr2(minBidingAmount + "") + "~"
								+ FormatUtil.formatStr2(remainAmount + ""));
						}
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
			};
			
			@Override
			public void onFailure(Throwable t, Context context)
			{
				super.onFailure(t, context);
			}
		});
	}
	
	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.donationBuyBid:
			{
				if (checkClick(v.getId()) && checkParams())
				{
					if (isNeedPsw)
					{ //需要交易密码
						UIHelper.showPayPwdEditDialog(this, "支付", new OnDealPwdOkListener()
						{
							@Override
							public void onDealPwdOk(String dealPassword)
							{
								confirmBuy(dealPassword);
							}
						});
					}
					else
					{ //不需要交易密码
						confirmBuy(null);
					}
				}
				break;
			}
			default:
				break;
		}
		
	}
	
	/**
	 * 确认并提交购买
	 * @param text 
	 */
	private void confirmBuy(String pwd)
	{
		HttpParams httpParams = new HttpParams();
		httpParams.put("loanId", bidId);
		httpParams.put("amount", donationAmountEdit.getText().toString());
		httpParams.put("tranPwd", null != pwd ? pwd : "");
		
		HttpUtil.getInstance().post(this, DMConstant.API_Url.GY_LOAN_BID, httpParams, new HttpCallBack()
		{
			@Override
			public void onSuccess(JSONObject result)
			{
				try
				{
					String code = result.getString("code");
					if (code.equals(DMConstant.ResultCode.SUCCESS))
					{
						if (DMApplication.getInstance().getUserInfo().isTg()
							)
						{
							String url = result.getJSONObject("data").getString("url").toString();
							Intent intent = new Intent(DonationBidActivity.this, TgThirdWebActivity.class);
							intent.putExtra("url", url);
							intent.putExtra("message", "捐赠成功！");
							intent.putExtra("title", DMConstant.TgWebTitle.DONATION_BID);
							startActivity(intent);
						}
						else
						{
							AlertDialogUtil.alert(DonationBidActivity.this, "恭喜您，捐赠成功！", new AlertListener()
							{
								@Override
								public void doConfirm()
								{
									AppManager.getAppManager().finishActivity(DonationDetailActivity.class);
									finish();
								}
							}).setCanceledOnTouchOutside(false);
						}
						
					}
					else if (code.equals(ErrorUtil.ErroreCode.ERROR_000044))
					{
						String description = result.getString("description");
						if (null != description && description.contains("交易密码"))
						{
							showDealPwdError();
						}
						else
						{
							AlertDialogUtil.alert(DonationBidActivity.this, FormatUtil.Html2Text(description));
						}
					}
					else if (DMApplication.getInstance().getUserInfo().isTg() && ErrorUtil.ErroreCode.ERROR_000047.equals(code))
					{
						final String url = result.getJSONObject("data").getString("url").toString();
						AlertDialogUtil.confirm(DonationBidActivity.this,
							result.getString("description"),
							"去授权",
							null,
							new ConfirmListener()
							{
								
								@Override
								public void onOkClick()
								{
									Intent intent = new Intent(DonationBidActivity.this, TgThirdWebActivity.class);
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
				super.onFailure(t, context);
			}
		});
	}
	
	/**
	 * 检测安全信息认证以及参数
	 * 
	 * @return
	 */
	private boolean checkParams()
	{
		// 安全信息认证完成
		if (!UIHelper.hasCompletedSecurityInfo(this, DMApplication.getInstance().getUserInfo(), isNeedEmailRZ, isNeedPsw))
		{
			return false;
		}
		
		String donationAmount = donationAmountEdit.getText().toString();
		if ("".equals(donationAmount))
		{
			ToastUtil.getInstant().show(this, "请输入捐助金额");
			return false;
		}
		if (Double.parseDouble(donationAmount) < minBidingAmount)
		{
			ToastUtil.getInstant().show(this, "最小捐助金额为" + FormatUtil.formatStr2(minBidingAmount + "") + "元");
			donationAmountEdit.setText(FormatUtil.formatStr2(minBidingAmount + ""));
			return false;
		}
		if (null != accountInfo)
		{//余额不足
			String temp = accountInfo.getOverAmount().replace(",", "").trim();
			if (!StringUtils.isEmptyOrNull(temp) && Double.parseDouble(donationAmount) > Double.parseDouble(temp))
			{
				AlertDialogUtil.confirm(this, getString(R.string.available_ammount_not_enough), "充值", "确定", new ConfirmListener()
				{
					@Override
					public void onOkClick()
					{
						if (DMApplication.getInstance().getUserInfo().isTg())
						{
							startActivity(new Intent(DonationBidActivity.this, TgRechargeActivity.class));
						}
						else
						{
							startActivity(new Intent(DonationBidActivity.this, RechargeActivity.class));
						}
					}
					
					@Override
					public void onCancelClick()
					{
					}
					
				});
				return false;
			}
		}
		if (agreementManager != null && !agreementManager.isChecked())
		{
			AlertDialogUtil.alert(DonationBidActivity.this, "请先阅读公益捐助协议并勾选！").setCanceledOnTouchOutside(false);
			return false;
		}
		return true;
	}
	
	/***
	 * 提示交易密码错误
	 */
	protected void showDealPwdError()
	{
		AlertDialogUtil.confirm(DonationBidActivity.this, getString(R.string.deal_pwd_err), null, "找回交易密码", new ConfirmListener()
		{
			@Override
			public void onOkClick()
			{
			}
			
			@Override
			public void onCancelClick()
			{
				startActivity(new Intent(DonationBidActivity.this, FindTradePwdActivity.class));
			}
		});
	}
}
