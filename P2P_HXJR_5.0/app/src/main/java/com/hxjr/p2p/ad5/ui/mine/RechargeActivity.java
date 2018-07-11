package com.hxjr.p2p.ad5.ui.mine;

import java.text.DecimalFormat;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.hxjr.p2p.ad5.DMApplication;
import com.hxjr.p2p.ad5.bean.BankCard;
import com.hxjr.p2p.ad5.bean.Chargep;
import com.hxjr.p2p.ad5.bean.Fee;
import com.hxjr.p2p.ad5.bean.UserInfo;
import com.hxjr.p2p.ad5.service.ApiUtil;
import com.hxjr.p2p.ad5.service.ApiUtil.OnPostCallBack;
import com.hxjr.p2p.ad5.ui.BaseActivity;
import com.hxjr.p2p.ad5.ui.mine.bank.BankCardActivity;
import com.hxjr.p2p.ad5.ui.mine.bank.BankCardManageActivity;
import com.hxjr.p2p.ad5.ui.mine.setting.SecurityInfoActivity;
import com.hxjr.p2p.ad5.ui.tg.TgThirdWebActivity;
import com.hxjr.p2p.ad5.utils.DMConstant;
import com.hxjr.p2p.ad5.utils.ErrorUtil;
import com.hxjr.p2p.ad5.utils.FormatUtil;
import com.hxjr.p2p.ad5.utils.HttpParams;
import com.hxjr.p2p.ad5.utils.UIHelper;
import com.hxjr.p2p.ad5.R;
import com.dm.android.pay.DMPayPlugin;
import com.dm.android.pay.DMPayPlugin.PayPluginCallBack;
import com.dm.android.pay.PaymentType;
import com.dm.http.HttpCallBack;
import com.dm.http.HttpUtil;
import com.dm.utils.DMJsonObject;
import com.dm.utils.DMLog;
import com.dm.utils.StringUtils;
import com.dm.widgets.GridLayoutForRadioGroup;
import com.dm.widgets.utils.AlertDialogUtil;
import com.dm.widgets.utils.AlertDialogUtil.ConfirmListener;
import com.dm.widgets.utils.ToastUtil;

/**
 * 充值页面
 * @author  jiaohongyun
 * @date 2015年11月17日
 */
public class RechargeActivity extends BaseActivity implements OnClickListener
{
	/**
	 * 选择银行卡
	 */
	private static final int CHOOSE_BANKCARD = 0X124;
	
	private RadioGroup amountRadioGroup;
	
	private GridLayoutForRadioGroup rechage_sel_mode;
	
	private EditText amountEdit;
	
	private TextView recharge_fee;
	
	private TextView recharge_income;
	
	private Button btnRecharge;
	
	/**
	 * 充值手续费
	 */
	private Chargep chargep;
	
	/**是否需要邮箱认证*/
	private boolean isNeedEmailRZ;
	
	//	private String netGate = "BAOFU";
	
	private PaymentType paymentType;
	
	private UserInfo userInfo;
	
	private BankCard card;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.recharge);
		chargep = new Chargep(); //给一个初始值，避免报空指针异常
		userInfo = DMApplication.getInstance().getUserInfo();
		initView();
		initListener();
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		getBankCard();
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
		paymentType = PaymentType.ALLINPAY;
		rechage_sel_mode = (GridLayoutForRadioGroup)findViewById(R.id.rechage_sel_mode);
		amountEdit.setText(((TextView)findViewById(amountRadioGroup.getCheckedRadioButtonId())).getText().toString());
		amountEdit.setSelection(amountEdit.getText().toString().length());
		recharge_fee = (TextView)findViewById(R.id.recharge_fee);
		recharge_income = (TextView)findViewById(R.id.recharge_income);
		btnRecharge = (Button)findViewById(R.id.btnRecharge);
	}
	
	/**
	 * 获取绑定的银行卡
	 */
	private void getBankCard()
	{
		HttpUtil.getInstance().post(this, DMConstant.API_Url.USER_MYBANKLIST, new HttpCallBack()
		{
			@Override
			public void onSuccess(JSONObject result)
			{
				try
				{
					String code = result.getString("code");
					if (DMConstant.ResultCode.SUCCESS.equals(code))
					{
						// 成功
						JSONObject data0 = result.getJSONObject("data");
						JSONArray data = data0.getJSONArray("myBankList");
						ArrayList<BankCard> cards = new ArrayList<BankCard>();
						if (data.length() > 0)
						{
							DMJsonObject dmJsonObject = new DMJsonObject(data.get(0).toString());
							BankCard bankCard = new BankCard(dmJsonObject);
							cards.add(bankCard);
							cards.get(0).setSelected(true);
							card = cards.get(0);
						}
						else
						{
							card = null;
						}
					}
					else
					{
						card = null;
					}
				}
				catch (JSONException e)
				{
					e.printStackTrace();
				}
			}
			
		});
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
		rechage_sel_mode.setOnCheckedChangeListener(new GridLayoutForRadioGroup.OnCheckedChangeListener()
		{
			@Override
			public void onCheckedChanged(GridLayoutForRadioGroup group, int checkedId)
			{
				switch (checkedId)
				{
				//					case R.id.one:
				//					{
				//						netGate = "BAOFU";
				//						break;
				//					}
				//					case R.id.two:
				//					{
				//						netGate = "HUICHAOGATE";
				//						break;
				//					}
					case R.id.three:
					{
						paymentType = PaymentType.LIANLIANAUTHGATE;
						break;
					}
					case R.id.four:
					{
						//						paymentType = PaymentType.LIANLIANAUTHGATE;
						break;
					}
					case R.id.five:
					{
						paymentType = PaymentType.ALLINPAY;
						break;
					}
					default:
						break;
				}
			}
		});
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (CHOOSE_BANKCARD == requestCode)
		{
			//如果是连连支付，需要先进行选择银行卡页面，获取银行卡
			if (data != null)
			{
				recharge(data.getStringExtra("cardId"));
			}
		}
		else
		{
			DMPayPlugin.onActivityResult(requestCode, resultCode, data, this, new PayPluginCallBack()
			{
				@Override
				public void onSuccess()
				{
					ToastUtil.getInstant().show(RechargeActivity.this, "充值成功");
					finish();
				}
				
				@Override
				public void onFaild(String message)
				{
					ToastUtil.getInstant().show(RechargeActivity.this, "充值失败");
				}
			}, paymentType);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	/**
	 * 充值操作
	 */
	private void recharge(String cardId)
	{
		if (checkPrams())
		{
			final PaymentType mPaymentType = paymentType;
			HttpParams params = new HttpParams();
			if ("".equals(cardId) || null == cardId)
			{
				if (card != null)
				{
					params.put("cardId", card.getId());
				}
			}
			else
			{
				params.put("cardId", cardId);
				
			}
			params.put("paymentInstitution", mPaymentType.name());
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
							String payUrl = result.getJSONObject("data").getString("url");
							switch (mPaymentType)
							{
								case SHUANGQIAN:
								{
									//双乾网关支付
									String url = payUrl;
									Intent intent = new Intent(RechargeActivity.this, TgThirdWebActivity.class);
									intent.putExtra("url", url);
									intent.putExtra("message", "充值成功！");
									intent.putExtra("title", DMConstant.TgWebTitle.RECHARGE);
									startActivity(intent);
									break;
								}
								default:
								{
									doPay(payUrl, mPaymentType);
									break;
								}
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
				}
				
				@Override
				public void onFailure(Throwable t, Context context)
				{
					DMLog.e("ssss");
				}
				
				@Override
				public void onLoading(Integer integer)
				{
					
				}
				
				@Override
				public void onStart()
				{
					
				}
				
			});
		}
		
	}
	
	protected void doPay(String payUrl, final PaymentType mpaymentType)
	{
		HttpUtil.getInstance().post(this, payUrl, new HttpCallBack()
		{
			@Override
			public void onSuccess(JSONObject result)
			{
				try
				{
					String code = result.getString("code");
					if (DMConstant.ResultCode.SUCCESS.equals(code))
					{
						String payData = result.getJSONObject("data").toString();
						DMPayPlugin.startPay(RechargeActivity.this, payData, mpaymentType, new PayPluginCallBack()
						{
							@Override
							public void onSuccess()
							{
								AlertDialogUtil.alert(RechargeActivity.this, "充值成功！");
							}
							
							@Override
							public void onFaild(String message)
							{
								AlertDialogUtil.alert(RechargeActivity.this, message);
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
		});
	}
	
	/***
	 * 获取用户安全信息完成的情况
	 * @param userInfo
	 * @param isNeedEmailRZ
	 * @param isNeedPsw
	 * @return
	 */
	public String getSecurityStr()
	{
		String securityInfo = "您必须先完成";
		if ((StringUtils.isEmptyOrNull(userInfo.getRealName()) && chargep.isNeedNciic()))
		{//为完成实名认证，并且真实名字为空，则表示没有完成实名认证
			securityInfo = securityInfo + "实名认证、";
		}
		if ((StringUtils.isEmptyOrNull(userInfo.getPhone()) && chargep.isNeedPhone()))
		{
			securityInfo = securityInfo + "手机号认证、";
		}
		if ((StringUtils.isEmptyOrNull(userInfo.getEmail()) && chargep.isNeedEmail()))
		{
			securityInfo = securityInfo + "邮箱认证、";
		}
		if ((!userInfo.isWithdrawPsw() && chargep.isNeedPsd()))
		{
			securityInfo = securityInfo + "设置交易密码、";
		}
		securityInfo = securityInfo.substring(0, securityInfo.length() - 1) + "！";
		return securityInfo;
	}
	
	/**
	 * 检查用户信息以及用户输入的一些参数
	 * @return
	 */
	private boolean checkPrams()
	{
		userInfo = DMApplication.getInstance().getUserInfo();
		if (null != userInfo)
		{ //是否需要实名认证, 手机认证, 邮箱认证, 交易密码
			if ((StringUtils.isEmptyOrNull(userInfo.getRealName()) && chargep.isNeedNciic())
				|| (StringUtils.isEmptyOrNull(userInfo.getPhone()) && chargep.isNeedPhone())
				|| (StringUtils.isEmptyOrNull(userInfo.getEmail()) && chargep.isNeedEmail())
				|| (!userInfo.isWithdrawPsw() && chargep.isNeedPsd()))
			{
				String message = getSecurityStr();
				AlertDialogUtil.confirm(this, message, "认证", "确定", new ConfirmListener()
				{
					@Override
					public void onOkClick()
					{
						startActivity(new Intent(RechargeActivity.this, SecurityInfoActivity.class).putExtra("isNeedPwd",
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
		if (rechage_sel_mode.getCheckedRadioButtonId() == -1)
		{
			ToastUtil.getInstant().show(this, "请选择充值方式");
			return false;
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
		if (paymentType == PaymentType.LIANLIANAUTHGATE)
		{
			if (card != null)
			{
				return true;
			}
			else
			{
				AlertDialogUtil.confirm(this, "您未绑定银行卡，请点击确认进行银行卡绑定", new ConfirmListener()
				{
					@Override
					public void onOkClick()
					{
						if (UIHelper.hasCompletedSecurityInfo(RechargeActivity.this, userInfo, isNeedEmailRZ, chargep.isNeedPsd()))
						{
							Intent intent = new Intent(RechargeActivity.this, BankCardActivity.class);
							startActivityForResult(intent, DMConstant.RequestCodes.REQUEST_CODE_SECURITY);
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
					if (paymentType == PaymentType.LIANLIANAUTHGATE)
					{
						Intent intent = new Intent(this, BankCardManageActivity.class);
						intent.putExtra("isChoose", true);
						startActivityForResult(intent, CHOOSE_BANKCARD);
					}
					else
					{
						recharge("");
					}
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
		double fee = amount * chargep.getP();
		fee = fee < 1 ? 1 : fee;
		if (fee > chargep.getpMax())
		{
			fee = chargep.getpMax();
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
				RechargeActivity.this.chargep = fee.getChargep();
				String selectAmountStr =
					((RadioButton)findViewById(amountRadioGroup.getCheckedRadioButtonId())).getText().toString();
				makeFee(Integer.valueOf(selectAmountStr));
				
				isNeedEmailRZ = fee.getBaseInfo().isNeedEmailRZ();
			}
			
			@Override
			public void onFailure()
			{
				
			}
		});
	}
}
