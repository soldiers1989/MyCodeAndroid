package com.hxjr.p2p.ad5.ui.mine.creditor;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hxjr.p2p.ad5.R;
import com.hxjr.p2p.ad5.bean.MyCreditorAssignment;
import com.hxjr.p2p.ad5.ui.mine.contract.CheckCreContractActivity;
import com.hxjr.p2p.ad5.utils.DMConstant;
import com.hxjr.p2p.ad5.utils.FormatUtil;
import com.hxjr.p2p.ad5.utils.UIHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * 已转出
 * @author  huangkaibo
 * @date 2015-12-4
 */
public class CreditorTurnOutAdapter extends BaseAdapter
{
	private LayoutInflater inflater;
	
	private Context context;
	
	private List<MyCreditorAssignment> mList;
	
	public CreditorTurnOutAdapter(Context context)
	{
		this.context = context;
		inflater = LayoutInflater.from(context);
		mList = new ArrayList<MyCreditorAssignment>(DMConstant.DigitalConstant.TEN);
	}
	
	public void clearList()
	{
		mList.clear();
	}
	
	public void addAll(List<MyCreditorAssignment> list)
	{
		mList.addAll(list);
		notifyDataSetChanged();
	}
	
	@Override
	public int getCount()
	{
		return mList.size();
	}
	
	@Override
	public MyCreditorAssignment getItem(int position)
	{
		return mList.get(position);
	}
	
	@Override
	public long getItemId(int position)
	{
		return position;
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent)
	{
		ViewHolder holder = null;
		if (null == convertView)
		{
			convertView = inflater.inflate(R.layout.my_creditor_turnout_list_item, parent, false);
			holder = new ViewHolder();
			holder.bidTitle = (TextView)convertView.findViewById(R.id.bidTitle);
			holder.trade_fee_tv = (TextView)convertView.findViewById(R.id.trade_fee_tv);
			holder.turnOutBalance_tv = (TextView)convertView.findViewById(R.id.turnOutBalance_tv);
			holder.creditor_value_tv = (TextView)convertView.findViewById(R.id.creditor_value_tv);
			holder.turn_price_tv = (TextView)convertView.findViewById(R.id.turn_price_tv);
			holder.check_agreement_tv = (TextView)convertView.findViewById(R.id.check_agreement_tv);
			holder.turn_date_tv = (TextView)convertView.findViewById(R.id.turn_date_tv);
			convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder)convertView.getTag();
		}
		
		final MyCreditorAssignment bean = getItem(position);
		
		holder.bidTitle.setText(bean.getCreditorId());
		holder.trade_fee_tv.setText("转让费用：" + FormatUtil.formatMoney(bean.getTransferPrice()));
		UIHelper.formatMoneyTextSize(holder.turnOutBalance_tv, FormatUtil.formatMoney(bean.getTurnOutBalance()), 0.5f);
		UIHelper.formatMoneyTextSize(holder.creditor_value_tv, FormatUtil.formatMoney(bean.getCreditorVal()), 0.5f);
		UIHelper.formatMoneyTextSize(holder.turn_price_tv, FormatUtil.formatMoney(bean.getSalePrice()), 0.5f);
		holder.turn_date_tv.setText("转让日期：" + bean.getBuyingDate());
		
		holder.check_agreement_tv.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent intent = new Intent(context, CheckCreContractActivity.class);
				intent.putExtra("id", bean.getBidId() + "");
				context.startActivity(intent);
			}
		});
		
		return convertView;
	}
	
	private class ViewHolder
	{
		TextView bidTitle;
		
		TextView trade_fee_tv;
		
		TextView turnOutBalance_tv;
		
		TextView creditor_value_tv;
		
		TextView turn_price_tv;
		
		TextView check_agreement_tv;
		
		TextView turn_date_tv;
	}
}
