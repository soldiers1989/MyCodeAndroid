package com.hxjr.p2p.ad5.ui.mine.loan;

import java.util.ArrayList;
import java.util.List;

import com.hxjr.p2p.ad5.ui.mine.loan.RepaymentDetailActivity.RepaymentDetail;
import com.hxjr.p2p.ad5.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * 我的借款-还款详情适配列
 * @author  tangjian
 * @date 2015年11月25日
 */
public class RepaymentDetailAdapter extends BaseAdapter
{
	private Context context;
	
	private List<RepaymentDetail> datas;
	
	public RepaymentDetailAdapter(Context context, List<RepaymentDetail> datas)
	{
		this.context = context;
		this.datas = new ArrayList<RepaymentDetail>();
		this.datas.addAll(datas);
	}
	
	public void setDatas(List<RepaymentDetail> datas)
	{
		this.datas.clear();
		this.datas.addAll(datas);
	}
	
	@Override
	public int getCount()
	{
		return datas.size();
	}
	
	@Override
	public Object getItem(int position)
	{
		return datas.get(position);
	}
	
	@Override
	public long getItemId(int position)
	{
		return position;
	}
	
	@SuppressLint("SimpleDateFormat")
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		ViewHolder holder = null;
		if (null == convertView)
		{
			convertView = LayoutInflater.from(context).inflate(R.layout.repayment_datail_item, parent, false);
			holder = new ViewHolder();
			holder.periods_tv = (TextView)convertView.findViewById(R.id.periods_tv);
			holder.money_tv = (TextView)convertView.findViewById(R.id.money_tv);
			holder.date_tv = (TextView)convertView.findViewById(R.id.date_tv);
			holder.status_tv = (TextView)convertView.findViewById(R.id.status_tv);
			convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder)convertView.getTag();
		}
		RepaymentDetail repaymentDetail = datas.get(position);
		holder.periods_tv.setText(repaymentDetail.getTerm());
		holder.money_tv.setText(repaymentDetail.getAmount());
		holder.date_tv.setText(repaymentDetail.getRepayDate());
		holder.status_tv.setText(repaymentDetail.getStatus());
		return convertView;
	}
	
	private class ViewHolder
	{
		private TextView periods_tv;
		
		private TextView money_tv;
		
		private TextView date_tv;
		
		private TextView status_tv;
	}
}
