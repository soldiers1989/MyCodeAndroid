package com.hxjr.p2p.ad5.ui.mine.invest;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hxjr.p2p.ad5.R;
import com.hxjr.p2p.ad5.bean.MyInvestSettle;
import com.hxjr.p2p.ad5.ui.mine.contract.CheckContractActivity;
import com.hxjr.p2p.ad5.utils.DMConstant;
import com.hxjr.p2p.ad5.utils.FormatUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的投资-已结清适配列表
 * @author  huangkaibo
 * @date 2015-12-07
 */
public class SettleAdapter extends BaseAdapter
{
	private LayoutInflater inflater;
	
	private List<MyInvestSettle> mList;
	
	private Context context;
	
	public SettleAdapter(Context context)
	{
		this.context = context;
		inflater = LayoutInflater.from(context);
		mList = new ArrayList<MyInvestSettle>(DMConstant.DigitalConstant.TEN);
	}
	
	public void addAll(List<MyInvestSettle> lists)
	{
		mList.addAll(lists);
		notifyDataSetChanged();
	}
	
	public void clearList()
	{
		mList.clear();
	}
	
	@Override
	public int getCount()
	{
		return mList.size();
	}
	
	@Override
	public MyInvestSettle getItem(int position)
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
		ViewHolder holder;
		if (null == convertView)
		{
			convertView = inflater.inflate(R.layout.my_invest_settle_list_item, parent, false);
			holder = new ViewHolder();
			holder.check_contract_tv = (TextView)convertView.findViewById(R.id.settle_check_contract);
			holder.settle_time_txt = (TextView)convertView.findViewById(R.id.settle_time_txt);
			holder.invest_money_value_tv = (TextView)convertView.findViewById(R.id.settle_money_value_txt);
			holder.annual_rate_value_tv = (TextView)convertView.findViewById(R.id.settle_annual_rate_value_txt);
			holder.giftRate_tv = (TextView)convertView.findViewById(R.id.giftRate_tv);
			holder.back_money_value_tv = (TextView)convertView.findViewById(R.id.settle_back_money_value_txt);
			holder.make_money_value_tv = (TextView)convertView.findViewById(R.id.settle_make_money_value_txt);
			holder.creditor_rights_id_tv = (TextView)convertView.findViewById(R.id.creditor_rights_id_tv);
			convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder)convertView.getTag();
		}
		
		MyInvestSettle settle = mList.get(position);
		holder.settle_time_txt.setText(settle.getJqsj());
		holder.invest_money_value_tv.setText(FormatUtil.formatMoney(settle.getGmjg()));
		holder.annual_rate_value_tv.setText(settle.getNhl());
		if (settle.getJxl() == 0.00)
		{
			holder.giftRate_tv.setVisibility(View.GONE);
		}
		else
		{
			holder.giftRate_tv.setVisibility(View.VISIBLE);
			holder.giftRate_tv.setText("+" + FormatUtil.get2String(settle.getJxl()) + "%");
		}
		holder.back_money_value_tv.setText(settle.getStatus());// 结清方式
		holder.make_money_value_tv.setText(FormatUtil.formatMoney(settle.getYzje()));
		holder.creditor_rights_id_tv.setText(settle.getCreditorId());
		
		holder.check_contract_tv.setTag(settle);
		holder.check_contract_tv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				MyInvestSettle repayment =(MyInvestSettle)v.getTag();
				Intent intent = new Intent(context, CheckContractActivity.class);
				intent.putExtra("id", repayment.getBidId() + "");
				intent.putExtra("creditId" ,repayment.getZqId()+"");
				context.startActivity(intent);
			}
		});
		return convertView;
	}
	
	private class ViewHolder
	{
		TextView check_contract_tv;
		
		TextView settle_time_txt;
		
		TextView invest_money_value_tv;//投资金额值
		
		TextView annual_rate_value_tv; //年化利率值
		
		TextView giftRate_tv;// 加息利率
		
		TextView back_money_value_tv; // 结清方式
		
		TextView make_money_value_tv; //已赚金额值
		
		TextView creditor_rights_id_tv;// 债权ID
		
	}
	
//	@Override
//	public void onClick(View v)
//	{
//		switch (v.getId())
//		{
//			case R.id.settle_check_contract:
//				int settleId = (Integer)v.getTag();
//				Intent intent = new Intent(context, CheckContractActivity.class);
//				intent.putExtra("id", settleId + "");
//				context.startActivity(intent);
//				break;
//			default:
//				break;
//		}
//
//	}
	
}
