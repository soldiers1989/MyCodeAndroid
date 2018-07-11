package com.hxjr.p2p.ad5.ui.discovery.moneymanage;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.hxjr.p2p.ad5.bean.CalculatorResult;
import com.hxjr.p2p.ad5.ui.BaseActivity;
import com.hxjr.p2p.ad5.utils.FormatUtil;
import com.hxjr.p2p.ad5.R;

/**
 * 计算结果
 * @author  huangkaibo
 * @date 2015年11月18日
 */
public class CalculatorResultActivity extends BaseActivity
{
	
	/**等额本息**/
	private final static int PAYMENT_METHOD_ONE = 1;
	
	/**每月付息，到期还本**/
	private final static int PAYMENT_METHOD_TWO = 2;
	
	/**本息到期一次付清**/
	private final static int PAYMENT_METHOD_THREE = 3;
	
	/**等额本金**/
	private final static int PAYMENT_METHOD_FOUR = 4;
	
	private ListView mListView; //这里不需要有加载更多和刷新
	
	private TextView load_money_tv;
	
	private TextView your_interest_tv;
	
	private TextView get_principal_and_interest_tv;
	
	private CalculatorAdapter adapter;
	
	private List<CalculatorResult> mCalculatorResults;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.calculator_result_list);
		initView();
	}
	
	@Override
	protected void initView()
	{
		super.initView();
		((TextView)findViewById(R.id.title_text)).setText(R.string.calculator_result);
		load_money_tv = (TextView)findViewById(R.id.load_money_tv);
		your_interest_tv = (TextView)findViewById(R.id.your_interest_tv);
		get_principal_and_interest_tv = (TextView)findViewById(R.id.get_principal_and_interest_tv);
		mListView = (ListView)findViewById(R.id.resultListView);
		initData();
	}
	
	private String loan_money; //出借金额
	
	private String year_rate; //年利率
	
	private String loan_term; //借款期限文本
	
	private String loan_method; //还款方式文本
	
	private int paymentMethod; //还款方式
	
	private int loanTerm; //借款期限
	
	/**
	 * 初始化数据
	 */
	private void initData()
	{
		Intent intent = getIntent();
		loan_money = intent.getStringExtra("loan_money");
		year_rate = intent.getStringExtra("year_rate");
		loan_term = intent.getStringExtra("loan_term");
		loan_method = intent.getStringExtra("loan_method");
		paymentMethod = intent.getIntExtra("paymentMethod", -1);
		loanTerm = intent.getIntExtra("loanTerm", -1);
		get_principal_and_interest_tv.setText(getString(R.string.shou_hui_ben_xi, loanTerm));
		load_money_tv.setText(getString(R.string.your_interest_str, FormatUtil.formatStr2(Double.parseDouble(loan_money) + "")));
		getCalculatorList();
	}
	
	private void getCalculatorList()
	{
		switch (paymentMethod)
		{
			case PAYMENT_METHOD_ONE: //等额本息
				doMethodOne();
				break;
			case PAYMENT_METHOD_TWO: //每月付息，到期还本
				doMethodTwo();
				break;
			case PAYMENT_METHOD_THREE://本息到期一次付清
				doMethodThree();
				break;
			case PAYMENT_METHOD_FOUR://等额本金
				doMethodFour();
				break;
			default:
				
				break;
		}
	}
	
	/**等额本金**/
	private void doMethodFour()
	{
		mCalculatorResults = new ArrayList<CalculatorResult>(loanTerm);
		double totalMoney = Double.parseDouble(loan_money);
		double monthRate = Double.parseDouble(year_rate) / 100 / 12;
		
		double monthPrincipal = totalMoney / loanTerm; //每月应偿还的本金
		double monthInterest = 0.00; //月供利息
		double hasRepaymentTotal = 0.00; //已还总额
		double totalInterest = 0.00; //利息总额
		for (int i = 1; i < loanTerm + 1; i++)
		{
			CalculatorResult calculatorResult = new CalculatorResult();
			calculatorResult.setPeriods("第" + i + "期");
			//每月应还利息 = 剩余本金（本金-已还本金）* 月利率
			monthInterest = (totalMoney - hasRepaymentTotal) * monthRate;
			hasRepaymentTotal = hasRepaymentTotal + monthPrincipal;
			totalInterest = totalInterest + monthInterest;
			calculatorResult.setRepaymentPrincipalAndInterest(FormatUtil.formatStr2((monthPrincipal + monthInterest) + ""));
			calculatorResult.setRepaymentInterest(FormatUtil.formatStr2(monthInterest + ""));
			
			calculatorResult.setRepaymentPrincipal(FormatUtil.formatStr2(monthPrincipal + ""));
			calculatorResult.setRemainPrincipal(FormatUtil.formatStr2(totalMoney - hasRepaymentTotal + ""));
			mCalculatorResults.add(calculatorResult);
		}
		your_interest_tv.setText(getString(R.string.load_money_str, FormatUtil.formatStr2(totalInterest + "")));
		adapter = new CalculatorAdapter(this);
		adapter.addAll(mCalculatorResults);
		mListView.setAdapter(adapter);
	}
	
	/**本息到期一次付清**/
	private void doMethodThree()
	{
		mCalculatorResults = new ArrayList<CalculatorResult>(loanTerm);
		double totalMoney = Double.parseDouble(loan_money);
		double monthRate = Double.parseDouble(year_rate) / 100 / 12;
		double interest = totalMoney * monthRate; //每月应偿还的利息
		double totalInterest = interest * loanTerm;
		your_interest_tv.setText(getString(R.string.load_money_str, FormatUtil.formatStr2(totalInterest + "")));
		for (int i = 1; i < loanTerm + 1; i++)
		{
			CalculatorResult calculatorResult = new CalculatorResult();
			calculatorResult.setPeriods("第" + i + "期");
			calculatorResult.setRepaymentPrincipalAndInterest(FormatUtil.formatStr2(i == loanTerm ? (totalInterest + totalMoney)
				+ "" : "0.00"));
			calculatorResult.setRepaymentInterest(FormatUtil.formatStr2(i == loanTerm ? totalInterest + "" : "0.00"));
			calculatorResult.setRepaymentPrincipal(FormatUtil.formatStr2(i == loanTerm ? totalMoney + "" : "0.00"));
			calculatorResult.setRemainPrincipal(FormatUtil.formatStr2(i == loanTerm ? "0.00" : totalMoney + ""));
			mCalculatorResults.add(calculatorResult);
		}
		adapter = new CalculatorAdapter(this);
		adapter.addAll(mCalculatorResults);
		mListView.setAdapter(adapter);
	}
	
	/**等额本息**/
	private void doMethodOne()
	{
		mCalculatorResults = new ArrayList<CalculatorResult>(loanTerm);
		double totalMoney = Double.parseDouble(loan_money);
		double monthRate = Double.parseDouble(year_rate) / 100 / 12;
		
		//月供本息（p）=借款总额 * power（1+∑月利率,借款期数）*月利率/ (power（1+月利率,借款期数）-1)
		double temp = Math.pow((1 + monthRate), loanTerm);
		//每月所需还的本息
		double interestAndMonthPrincipal = totalMoney * (monthRate * temp / (temp - 1));
		double totalInterest = 0.00; //总共利息
		double interest = 0.00; //月供利息
		double monthPrincipal = 0.00; //每月应偿还的本金
		double hasRepaymentTotal = 0.00; //已还本金之和
		for (int i = 1; i < loanTerm + 1; i++)
		{
			CalculatorResult calculatorResult = new CalculatorResult();
			calculatorResult.setPeriods("第" + i + "期");
			calculatorResult.setRepaymentPrincipalAndInterest(FormatUtil.formatStr2((interestAndMonthPrincipal) + ""));
			
			//月供利息=（本金-已还本金之和）*月利率
			interest = (totalMoney - hasRepaymentTotal) * monthRate;
			calculatorResult.setRepaymentInterest(FormatUtil.formatStr2(interest + ""));
			
			totalInterest = totalInterest + interest;
			//月供本金=月供本息-月供利息
			monthPrincipal = interestAndMonthPrincipal - interest;
			hasRepaymentTotal = hasRepaymentTotal + monthPrincipal;
			calculatorResult.setRepaymentPrincipal(FormatUtil.formatStr2(monthPrincipal + ""));
			//剩余本金 = totalMoney - hasRepaymentTotal
			calculatorResult.setRemainPrincipal(FormatUtil.formatStr2(totalMoney - hasRepaymentTotal + ""));
			mCalculatorResults.add(calculatorResult);
		}
		
		your_interest_tv.setText(getString(R.string.load_money_str, FormatUtil.formatStr2(totalInterest + "")));
		adapter = new CalculatorAdapter(this);
		adapter.addAll(mCalculatorResults);
		mListView.setAdapter(adapter);
	}
	
	/**
	 * 每月付息，到期还本
	 */
	private void doMethodTwo()
	{
		mCalculatorResults = new ArrayList<CalculatorResult>(loanTerm);
		double totalMoney = Double.parseDouble(loan_money);
		double monthRate = Double.parseDouble(year_rate) / 100 / 12;
		double interest = totalMoney * monthRate; //每月应偿还的利息
		double totalInterest = interest * loanTerm;
		your_interest_tv.setText(getString(R.string.load_money_str, FormatUtil.formatStr2(totalInterest + "")));
		//		double monthPrincipal = totalMoney / loanTerm; //每月应偿还的本金
		for (int i = 1; i < loanTerm + 1; i++)
		{
			CalculatorResult calculatorResult = new CalculatorResult();
			calculatorResult.setPeriods("第" + i + "期");
			//			calculatorResult.setRepaymentPrincipalAndInterest(FormatUtil.formatStr2((interest + monthPrincipal) + ""));
			//			calculatorResult.setRepaymentInterest(FormatUtil.formatStr2(interest + ""));
			//			calculatorResult.setRepaymentPrincipal(FormatUtil.formatStr2(monthPrincipal + ""));
			//			double remainPrincipal = totalMoney - monthPrincipal * i;
			//			calculatorResult.setRemainPrincipal(FormatUtil.formatStr2(remainPrincipal + ""));
			if (i != loanTerm)
			{
				calculatorResult.setRepaymentPrincipalAndInterest(FormatUtil.formatStr2((interest) + ""));
				calculatorResult.setRepaymentInterest(FormatUtil.formatStr2(interest + ""));
				calculatorResult.setRepaymentPrincipal(FormatUtil.formatStr2("0.00"));
				calculatorResult.setRemainPrincipal(FormatUtil.formatStr2(totalMoney + ""));
			}
			else
			{
				calculatorResult.setRepaymentPrincipalAndInterest(FormatUtil.formatStr2((interest + totalMoney) + ""));
				calculatorResult.setRepaymentInterest(FormatUtil.formatStr2(interest + ""));
				calculatorResult.setRepaymentPrincipal(FormatUtil.formatStr2(totalMoney + ""));
				calculatorResult.setRemainPrincipal(FormatUtil.formatStr2("0.00"));
			}
			mCalculatorResults.add(calculatorResult);
		}
		
		adapter = new CalculatorAdapter(this);
		adapter.addAll(mCalculatorResults);
		mListView.setAdapter(adapter);
	}
	
	/**
	 * 重新计算
	 * @param v
	 */
	public void recalculate(View v)
	{
		finish();
	}
}
