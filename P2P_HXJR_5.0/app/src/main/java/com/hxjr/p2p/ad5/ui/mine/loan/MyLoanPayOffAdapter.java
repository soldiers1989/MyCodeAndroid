package com.hxjr.p2p.ad5.ui.mine.loan;

import java.util.ArrayList;
import java.util.List;

import com.hxjr.p2p.ad5.bean.UserLoanBid;
import com.hxjr.p2p.ad5.utils.FormatUtil;
import com.hxjr.p2p.ad5.R;
import com.dm.utils.StringUtils;

import android.content.Context;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * 我的借款-还款适配列表
 * @author  lihao
 * @date 2015-6-5
 */
public class MyLoanPayOffAdapter extends BaseAdapter
{
	private LayoutInflater inflater;
	
	private List<UserLoanBid> userLoanBids;
	
	public MyLoanPayOffAdapter(Context context, List<UserLoanBid> userLoanBids)
	{
		this.userLoanBids = new ArrayList<UserLoanBid>(5);
		this.userLoanBids.addAll(userLoanBids);
		inflater = LayoutInflater.from(context);
	}
	
	public void setDatas(List<UserLoanBid> userLoanBids)
	{
		this.userLoanBids.clear();
		this.userLoanBids.addAll(userLoanBids);
	}
	
	@Override
	public int getCount()
	{
		return userLoanBids.size();
	}
	
	@Override
	public UserLoanBid getItem(int position)
	{
		return userLoanBids.get(position);
	}
	
	@Override
	public long getItemId(int position)
	{
		return position;
	}
	
	private ViewHolder holder = null;
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent)
	{
		if (null == convertView)
		{
			convertView = inflater.inflate(R.layout.my_loan_list_item, parent, false);
			holder = new ViewHolder();
			convertView.findViewById(R.id.can_operation_ll).setVisibility(View.GONE);
			convertView.findViewById(R.id.last_line).setVisibility(View.GONE);
			convertView.findViewById(R.id.arrow_img).setVisibility(View.GONE);
			convertView.findViewById(R.id.loan_status_tv).setVisibility(View.GONE);
			convertView.findViewById(R.id.item_line_height).setVisibility(View.VISIBLE);
			
			holder.title_tv = (TextView)convertView.findViewById(R.id.bidTitle);
			holder.loan_status_tv = (TextView)convertView.findViewById(R.id.loan_status_tv);
			holder.yearRate_tv = (TextView)convertView.findViewById(R.id.yearRate_tv);
			holder.loaned_money_tv = (TextView)convertView.findViewById(R.id.loaned_money_tv);
			holder.repayment_amount_tv = (TextView)convertView.findViewById(R.id.repayment_amount_tv);
			holder.periods_tv = (TextView)convertView.findViewById(R.id.periods_tv);
			holder.next_repayment_date_tv = (TextView)convertView.findViewById(R.id.next_repayment_date_tv);
			
			convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder)convertView.getTag();
		}
		setDataToView(position);
		return convertView;
	}
	
	private void setDataToView(int position)
	{
		UserLoanBid userLoanBid = userLoanBids.get(position);
		holder.title_tv.setText(userLoanBid.getBidTitle());
		holder.loan_status_tv.setText(userLoanBid.getStatus());
		
		double yearRate = Double.parseDouble(StringUtils.isEmptyOrNull(userLoanBid.getRate()) ? "0.00" : userLoanBid.getRate());
		yearRate = yearRate * 100;
		holder.yearRate_tv.setText(doubleToStr(yearRate + ""));
		
		double totalAmount =
			Double.parseDouble(StringUtils.isEmptyOrNull(userLoanBid.getTotalAmount()) ? "0.00" : userLoanBid.getTotalAmount());
		SpannableString termsSB = null;
		int index = 0;
		String tempAmount = "";
		if (totalAmount >= 10000)
		{
			totalAmount = totalAmount / 10000;
			tempAmount = FormatUtil.formatStr2(totalAmount + "") + "万元";
			index = 2;
		}
		else
		{
			tempAmount = FormatUtil.formatStr2(totalAmount + "") + "元";
			index = 1;
		}
		termsSB = new SpannableString(tempAmount);
		termsSB.setSpan(new RelativeSizeSpan(0.5f),
			tempAmount.length() - index,
			tempAmount.length(),
			SpannableString.SPAN_INCLUSIVE_EXCLUSIVE);
		holder.loaned_money_tv.setText(termsSB);
		
		double currentBackAmount = Double
			.parseDouble(StringUtils.isEmptyOrNull(userLoanBid.getTotalBackAmount()) ? "0.00" : userLoanBid.getTotalBackAmount());
		if (currentBackAmount >= 10000)
		{
			currentBackAmount = currentBackAmount / 10000;
			tempAmount = FormatUtil.formatStr2(currentBackAmount + "") + "万元";
			index = 2;
		}
		else
		{
			tempAmount = FormatUtil.formatStr2(currentBackAmount + "") + "元";
			index = 1;
		}
		termsSB = new SpannableString(tempAmount);
		termsSB.setSpan(new RelativeSizeSpan(0.5f),
			tempAmount.length() - index,
			tempAmount.length(),
			SpannableString.SPAN_INCLUSIVE_EXCLUSIVE);
		holder.repayment_amount_tv.setText(termsSB);
		String isDay = userLoanBid.getIsDay();
		int totalTerm = userLoanBid.getTotalTerm();
		if ("F".equals(isDay))
		{
			holder.periods_tv.setText("期数：" + totalTerm + "个月");
		}
		else
		{
			holder.periods_tv.setText("期数：" + totalTerm + "天");
		}
		holder.next_repayment_date_tv.setText("还清日期：" + userLoanBid.getCleanTime());
	}
	
	private String doubleToStr(String sourceDoubleStr)
	{
		return FormatUtil
			.formatStr2(Double.parseDouble(StringUtils.isEmptyOrNull(sourceDoubleStr) ? "0.00" : sourceDoubleStr) + "");
	}
	
	private class ViewHolder
	{
		private TextView title_tv;//借款标题
		
		private TextView loan_status_tv;//借款的还款状态
		
		private TextView yearRate_tv; //年化利率
		
		private TextView loaned_money_tv; //借款金额
		
		private TextView repayment_amount_tv; //已还款金额
		
		private TextView periods_tv; //还款期限
		
		private TextView next_repayment_date_tv; //下一个还款日期
		
		//		private View bidTypeImage; //标的类型
	}
}
