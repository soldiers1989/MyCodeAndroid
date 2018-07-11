package com.hxjr.p2p.ad5.ui.discovery.moneymanage;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.dm.http.HttpCallBack;
import com.dm.http.HttpUtil;
import com.dm.utils.DMLog;
import com.dm.utils.DensityUtil;
import com.dm.widgets.utils.ToastUtil;
import com.hxjr.p2p.ad5.R;
import com.hxjr.p2p.ad5.bean.BidDetailBase;
import com.hxjr.p2p.ad5.ui.BaseActivity;
import com.hxjr.p2p.ad5.ui.MainActivity;
import com.hxjr.p2p.ad5.utils.DMConstant;
import com.hxjr.p2p.ad5.utils.ErrorUtil;
import com.hxjr.p2p.ad5.utils.SharedPreferenceUtils;
import com.hxjr.p2p.ad5.utils.UIHelper;

/**
 * 理财计算器
 * @author  huangkaibo
 * @date 2015年11月9日
 */
public class MoneyManageActivity extends BaseActivity implements OnClickListener
{
	private TextView loan_term_tv;
	
	private TextView loan_method_tv;
	
	private Button gotoCalculator_btn;
	
	private LinearLayout load_method_arrow_ll;
	
	private LinearLayout load_term_arrow_ll;
	
	private EditText loan_money_et;
	
	private EditText year_rate_et;
	
	private PopupWindow popupWindow;
	
	private List<String> mLoanTerms;
	
	private List<String> mPaymentMethods;
	
	private int paymentMethod; //选择的还款方式
	
	private int loanTerm; //选择的借款期限
	
	/**
	 * 用户自动投标设置: 投标金额的倍数
	 */
	private int multAmount = 1;
	
	/**
	 * 用户自动投标设置: 最低投标金额
	 */
	private int minAmount = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.manage_money_calculator);
		multAmount = (Integer)SharedPreferenceUtils.get(this, "CaculatorConfig", "multAmount", 1);
		minAmount = (Integer)SharedPreferenceUtils.get(this, "CaculatorConfig", "minAmount", 0);
		initView();
		initData();
	}
	
	private void initData()
	{
		mLoanTerms = new ArrayList<String>(DMConstant.DigitalConstant.TEN);
		for (int i = 1; i < 37; i++)
		{
			mLoanTerms.add(i + "个月");
		}
		
		mPaymentMethods = new ArrayList<String>(4);
		mPaymentMethods.add("等额本息");
		mPaymentMethods.add("每月付息，到期还本");
		mPaymentMethods.add("本息到期一次付清");
		mPaymentMethods.add("等额本金");
		initCaculatorConfig(false);
	}
	
	/**
	 * 查询用户自动投标设置
	 */
	private void initCaculatorConfig(final boolean flag)
	{
		HttpUtil.getInstance().post(this, DMConstant.API_Url.AUTO_BID_CFG, new HttpCallBack()
		{
			@Override
			public void onSuccess(JSONObject result)
			{
				try
				{
					String code = result.getString("code");
					if (DMConstant.ResultCode.SUCCESS.equals(code))
					{
						JSONObject data = result.getJSONObject("data");
						multAmount = data.getInt("multAmount");
						minAmount = data.getInt("minAmount");
						SharedPreferenceUtils.put(MoneyManageActivity.this, "CaculatorConfig", "multAmount", multAmount);
						SharedPreferenceUtils.put(MoneyManageActivity.this, "CaculatorConfig", "minAmount", minAmount);
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
	
	@Override
	protected void initView()
	{
		super.initView();
		((TextView)findViewById(R.id.title_text)).setText(R.string.discovery_manage_money_calculator);
		loan_term_tv = (TextView)findViewById(R.id.loan_term_tv);
		loan_method_tv = (TextView)findViewById(R.id.loan_method_tv);
		load_method_arrow_ll = (LinearLayout)findViewById(R.id.load_method_arrow_ll);
		load_term_arrow_ll = (LinearLayout)findViewById(R.id.load_term_arrow_ll);
		loan_money_et = (EditText)findViewById(R.id.loan_money_et);
		year_rate_et = (EditText)findViewById(R.id.year_rate_et);
		gotoCalculator_btn = (Button)findViewById(R.id.gotoCalculator_btn);
		
		Intent intent=getIntent();
		Bundle bundle=intent.getExtras();
		if (bundle!=null) {
			BidDetailBase base=(BidDetailBase) bundle.getSerializable("base");
			year_rate_et.setText(base.getRate().replace("%", ""));
			loan_term_tv.setText(base.getCycle()+"个月");
			loanTerm = Integer.parseInt(loan_term_tv.getText().toString().replace("个月", ""));
			loan_method_tv.setText(base.getPaymentType());
			if (loan_method_tv.getText().equals("等额本息")) {
				paymentMethod=1;
			}else if (loan_method_tv.getText().equals("每月付息,到期还本")) {
				paymentMethod=2;
			}
			else if (loan_method_tv.getText().equals("本息到期一次付清")) {
				paymentMethod=3;
			}else if (loan_method_tv.getText().equals("等额本金")) {
				paymentMethod=4;
			}
		}
		
		load_method_arrow_ll.setOnClickListener(this);
		load_term_arrow_ll.setOnClickListener(this);
		gotoCalculator_btn.setOnClickListener(this);
		
		
		
		loan_money_et.addTextChangedListener(new TextWatcher()
		{
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count)
			{
				String LendMoneyTv =loan_money_et.getText().toString().trim();
				if ((LendMoneyTv.startsWith("0") && LendMoneyTv.indexOf(".") != 1)
					|| LendMoneyTv.startsWith(".")) //开头不能是0或者.
				{
					loan_money_et.setText("");
//					ToastUtil.getInstant().show(MoneyManageActivity.this, "金额必须大于:1");
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
		year_rate_et.addTextChangedListener(new TextWatcher()
		{
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count)
			{
				String temp = s.toString();
				if (temp.startsWith("."))
				{
					year_rate_et.setText("");
					return;
				}
				
				if ((temp.startsWith("0") && temp.indexOf(".") != 1)
					|| temp.startsWith(".")) //开头不能是0或者.
				{
					year_rate_et.setText("");
					ToastUtil.getInstant().show(MoneyManageActivity.this, "年利率小于最小范围值:1");
				}
				if (temp.contains(".") && temp.substring(temp.indexOf(".")).length() > 3)
				{
					year_rate_et.setText(temp.substring(0, temp.indexOf(".") + 3));
					year_rate_et.setSelection(year_rate_et.getText().toString().length());
					return;
				}
//				if (!temp.equals(""))
//				{
//					double yearRate = Double.parseDouble(temp);
//					if (yearRate > 24)
//					{
//						year_rate_et.setText(temp.substring(0, temp.length() - 1));
//						year_rate_et.setSelection(temp.length() - 1);
//						ToastUtil.getInstant().show(MoneyManageActivity.this, "年利率最大值为24%");
//						return;
//					}
//				}
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
	}
	
	/**
	 * 计算
	 * @param v
	 */
	public void gotoCalculator()
	{
		if (checkParasm())
		{
			Intent intent = new Intent(MoneyManageActivity.this, CalculatorResultActivity.class);
			intent.putExtra("loan_money", loan_money_et.getText().toString().trim());
			intent.putExtra("year_rate", year_rate_et.getText().toString().trim());
			intent.putExtra("loan_term", loan_term_tv.getText().toString().trim());
			intent.putExtra("loan_method", loan_method_tv.getText().toString().trim());
			intent.putExtra("paymentMethod", paymentMethod);//还款方式
			intent.putExtra("loanTerm", loanTerm);//借款期限
			startActivity(intent);
		}
	}
	
	private boolean checkParasm()
	{
		if (multAmount == 1 && minAmount == 0)
		{
			//查询			
			initCaculatorConfig(true);
			return false;
		}
		if (loan_money_et.getText().toString().trim().length() == 0)
		{
			ToastUtil.getInstant().show(this, "请输入借款金额");
			return false;
		}
		int loanMoney = Integer.valueOf(loan_money_et.getText().toString().trim());
		if (loanMoney % multAmount != 0)
		{
			ToastUtil.getInstant().show(this, "出借金额必须是" + multAmount + "的倍数");
			return false;
		}
		if (loanMoney < minAmount)
		{
			ToastUtil.getInstant().show(this, "出借金额小于最小范围值:" + minAmount);
			return false;
		}
		if (year_rate_et.getText().toString().trim().length() == 0)
		{
			ToastUtil.getInstant().show(this, "请输入年利率");
			return false;
		}
		if (loan_term_tv.getText().toString().trim().length() == 0)
		{
			ToastUtil.getInstant().show(this, "请选择借款期限");
			return false;
		}
		if (loan_method_tv.getText().toString().trim().length() == 0)
		{
			ToastUtil.getInstant().show(this, "请选择还款方式");
			return false;
		}
		double rate = Double.valueOf(year_rate_et.getText().toString().trim());
		if (rate < 1)
		{
			year_rate_et.selectAll();
			year_rate_et.requestFocus();
			ToastUtil.getInstant().show(this, "年利率小于最小范围值:1");
			return false;
		}
		if (rate > 24)
		{
			year_rate_et.selectAll();
			year_rate_et.requestFocus();
			ToastUtil.getInstant().show(this, "年利率超出最大范围值:24");
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
				case R.id.load_method_arrow_ll:
					UIHelper.hideSrfAndRun(this, new Runnable()
					{
						@Override
						public void run()
						{
							showPopupWindow(mPaymentMethods, false);
						}
					});
					break;
				case R.id.load_term_arrow_ll:
					UIHelper.hideSrfAndRun(this, new Runnable()
					{
						@Override
						public void run()
						{
							showPopupWindow(mLoanTerms, true);
						}
					});
					break;
				case R.id.gotoCalculator_btn:
					gotoCalculator();
					break;
			}
		}
	}
	
	@SuppressLint("InflateParams")
	@SuppressWarnings("deprecation")
	private void showPopupWindow(List<String> datas, final boolean isLoanTerm)
	{
		View contentView = LayoutInflater.from(MoneyManageActivity.this).inflate(R.layout.popup_window, null);
		popupWindow = new PopupWindow(this);
		popupWindow.setBackgroundDrawable(new BitmapDrawable()); //加该行代码后，点击界面其他位置popupWindow会消失
		//				popupWindow.setFocusable(true);
		ListView listView = (ListView)contentView.findViewById(R.id.popup_window_listview);
		listView.setAdapter(new MyAdapter(datas));
		
		popupWindow.setContentView(contentView);
		// 设置SelectPicPopupWindow弹出窗体的宽
		popupWindow.setWidth(LayoutParams.MATCH_PARENT);
		// 设置SelectPicPopupWindow弹出窗体的高
		int height = datas.size() >= 5 ? 5 * DensityUtil.dip2px(this, 50) : datas.size() * DensityUtil.dip2px(this, 50); //获取悬浮窗的高度
		popupWindow.setHeight(height);
		// 设置SelectPicPopupWindow弹出窗体可点击
		popupWindow.setFocusable(true);
		// 设置SelectPicPopupWindow弹出窗体动画效果
		popupWindow.setAnimationStyle(R.style.AnimBottom);
		// 显示窗口 (设置layout在PopupWindow中显示的位置)
		popupWindow.showAtLocation(this.findViewById(R.id.root_ll), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
		listView.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view, final int position, long id)
			{
				popupWindow.dismiss();
				String temp = ((String)parent.getAdapter().getItem(position));
				if (isLoanTerm)
				{
					loanTerm = Integer.parseInt(temp.replace("个月", ""));
					loan_term_tv.setText(temp);
				}
				else
				{
					paymentMethod = position + 1;
					loan_method_tv.setText(temp);
				}
			}
		});
	}
	
	class MyAdapter extends BaseAdapter
	{
		private List<String> myDatas;
		
		public MyAdapter(List<String> datas)
		{
			myDatas = datas;
		}
		
		@Override
		public int getCount()
		{
			return myDatas.size();
		}
		
		@Override
		public String getItem(int position)
		{
			return myDatas.get(position);
		}
		
		@Override
		public long getItemId(int position)
		{
			return position;
		}
		
		@SuppressLint({"ViewHolder", "InflateParams"})
		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			convertView = LayoutInflater.from(MoneyManageActivity.this).inflate(R.layout.popup_window_lv_item, null);
			TextView tv = (TextView)convertView.findViewById(R.id.item_tv);
			tv.setText(myDatas.get(position));
			return convertView;
		}
	}
	
	@Override
	protected void onDestroy()
	{
		MainActivity.index = 2;
		super.onDestroy();
	}}
