package com.hxjr.p2p.ad5.ui.mine.creditor;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dm.utils.StringUtils;
import com.hxjr.p2p.ad5.R;
import com.hxjr.p2p.ad5.bean.MyCreditorAssignment;
import com.hxjr.p2p.ad5.ui.mine.contract.CheckCreContractActivity;
import com.hxjr.p2p.ad5.utils.DMConstant;
import com.hxjr.p2p.ad5.utils.FormatUtil;
import com.hxjr.p2p.ad5.utils.UIHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * 已转入
 * @author  huangkaibo
 * @date 2015-12-4
 */
public class CreditorTurnInAdapter extends BaseAdapter
{
	private LayoutInflater inflater;
	
	private Context context;
	
	private List<MyCreditorAssignment> mList;
	
	public CreditorTurnInAdapter(Context context)
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
			convertView = inflater.inflate(R.layout.my_creditor_turnin_list_item, parent, false);
			holder = new ViewHolder();
			holder.yearRate_tv = (TextView)convertView.findViewById(R.id.yearRate_tv);
			holder.periods_tv = (TextView)convertView.findViewById(R.id.periods_tv);
			holder.turnInBalance_tv = (TextView)convertView.findViewById(R.id.turnInBalance_tv);
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
		holder.yearRate_tv
			.setText(StringUtils.isEmptyOrNull(bean.getRate()) ? "0.00" : FormatUtil.get2String(Double.valueOf(bean.getRate()) * 100));
		holder.periods_tv.setText(bean.getSubTerm() + "/" + bean.getTotalTerm());
		UIHelper.formatMoneyTextSize(holder.turnInBalance_tv, FormatUtil.formatMoney(bean.getTurnInBalance()), 0.5f);
		holder.creditor_value_tv.setText("债权价值：" + FormatUtil.formatMoney(bean.getCreditorVal()));
		holder.turn_price_tv.setText("转让价格：" + FormatUtil.formatMoney(bean.getSalePrice()));
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
		TextView yearRate_tv;
		
		TextView periods_tv;
		
		TextView turnInBalance_tv;
		
		TextView creditor_value_tv;
		
		TextView turn_price_tv;
		
		TextView check_agreement_tv;
		
		TextView turn_date_tv;
		
	}
}
