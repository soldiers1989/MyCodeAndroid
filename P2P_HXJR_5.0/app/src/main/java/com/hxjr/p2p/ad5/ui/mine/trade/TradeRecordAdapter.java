package com.hxjr.p2p.ad5.ui.mine.trade;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hxjr.p2p.ad5.bean.TradeRecord;
import com.hxjr.p2p.ad5.utils.DMConstant;
import com.hxjr.p2p.ad5.utils.FormatUtil;
import com.hxjr.p2p.ad5.R;

/**
 * 交易记录适配列表
 * @author  huangkaibo
 * @date 2015-11-2
 */
public class TradeRecordAdapter extends BaseAdapter
{
	private List<TradeRecord> listDatas;
	
	private Context context;
	
	public TradeRecordAdapter(Context context)
	{
		this.context = context;
		listDatas = new ArrayList<TradeRecord>(DMConstant.DigitalConstant.TEN);
	}
	
	public TradeRecordAdapter(List<TradeRecord> listDatas, Context context)
	{
		this.listDatas = listDatas;
		this.context = context;
	}
	
	public void addAll(List<TradeRecord> tradeRecoreds)
	{
		listDatas.addAll(tradeRecoreds);
		this.notifyDataSetChanged();
	}
	
	public void removeAll()
	{
		listDatas.clear();
	}
	
	@Override
	public int getCount()
	{
		return listDatas.size();
	}
	
	@Override
	public TradeRecord getItem(int position)
	{
		return listDatas.get(position);
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
		if (null == convertView)
		{
			convertView = LayoutInflater.from(context).inflate(R.layout.trade_record_list_item, parent, false);
			holder = new ViewHolder();
			holder.tranTypeTxt = (TextView)convertView.findViewById(R.id.recharge_txt);
			holder.remainAmountTxt = (TextView)convertView.findViewById(R.id.balance_txt);
			holder.tranTimeTxt = (TextView)convertView.findViewById(R.id.recharge_time_txt);
			holder.revAmountTxt = (TextView)convertView.findViewById(R.id.trade_record_bid_txt);
			convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder)convertView.getTag();
		}
		
		TradeRecord tr = getItem(position);
		holder.tranTypeTxt.setText(tr.getTranType());
		holder.remainAmountTxt.setText(FormatUtil.get2String(tr.getRevAmount()));
		holder.tranTimeTxt.setText(tr.getTranTime());
		holder.revAmountTxt.setText(FormatUtil.get2String(tr.getInAmount()));
		
		//		double AmountIn = Double.valueOf((tr.getAmountIn().toString()));
		//		double AmountOut = Double.valueOf((tr.getAmountOut().toString()));
		
		if ((tr.getInAmount() - tr.getExpAmount()) >= 0)
		{
			holder.revAmountTxt.setText("+" + FormatUtil.get2String(tr.getInAmount()));
			holder.revAmountTxt.setTextColor(context.getResources().getColor(R.color.main_color));
		}
		else
		{
			holder.revAmountTxt.setText("-" + FormatUtil.get2String(tr.getExpAmount()));
			holder.revAmountTxt.setTextColor(context.getResources().getColor(R.color.red));
		}
		
		return convertView;
	}
	
	private class ViewHolder
	{
		TextView tranTypeTxt;
		
		TextView tranTimeTxt;
		
		TextView remainAmountTxt;
		
		TextView revAmountTxt;
		
	}
	
}
