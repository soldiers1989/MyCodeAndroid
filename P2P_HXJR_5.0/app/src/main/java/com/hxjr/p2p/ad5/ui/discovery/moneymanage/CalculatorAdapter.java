package com.hxjr.p2p.ad5.ui.discovery.moneymanage;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hxjr.p2p.ad5.R;
import com.hxjr.p2p.ad5.bean.CalculatorResult;
import com.hxjr.p2p.ad5.utils.DMConstant;

public class CalculatorAdapter extends BaseAdapter
{
	private List<CalculatorResult> mList;
	
	private Context context;
	
	public CalculatorAdapter(Context context)
	{
		this.context = context;
		this.mList = new ArrayList<CalculatorResult>(DMConstant.DigitalConstant.TEN);
	}
	
	/**
	 * 添加记录
	 * @param list
	 */
	public void addAll(List<CalculatorResult> list)
	{
		mList.clear();
		mList.addAll(list);
		this.notifyDataSetChanged();
	}
	
	@Override
	public int getCount()
	{
		return mList.size();
	}
	
	@Override
	public Object getItem(int position)
	{
		return mList.get(position);
	}
	
	@Override
	public long getItemId(int position)
	{
		return position;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		ViewHolder holder = null;
		if (convertView == null)
		{
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.calculator_result_list_item, parent, false);
			holder.periods_tv = (TextView)convertView.findViewById(R.id.periods_tv);
			holder.repayment_principal_and_interest_tv =
				(TextView)convertView.findViewById(R.id.repayment_principal_and_interest_tv);
			holder.repayment_principal_tv = (TextView)convertView.findViewById(R.id.repayment_principal_tv);
			holder.repayment_interest_tv = (TextView)convertView.findViewById(R.id.repayment_interest_tv);
			holder.remain_principal_tv = (TextView)convertView.findViewById(R.id.remain_principal_tv);
			convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder)convertView.getTag();
		}
		
		CalculatorResult calculatorResult = mList.get(position);
		holder.periods_tv.setText(calculatorResult.getPeriods());
		holder.repayment_principal_and_interest_tv.setText(calculatorResult.getRepaymentPrincipalAndInterest());
		holder.repayment_principal_tv.setText(calculatorResult.getRepaymentPrincipal());
		holder.repayment_interest_tv.setText(calculatorResult.getRepaymentInterest());
		holder.remain_principal_tv.setText(calculatorResult.getRemainPrincipal());
		return convertView;
	}
	
	private class ViewHolder
	{
		private TextView periods_tv;
		
		private TextView repayment_principal_and_interest_tv;
		
		private TextView repayment_principal_tv;
		
		private TextView repayment_interest_tv;
		
		private TextView remain_principal_tv;
	}
	
}
