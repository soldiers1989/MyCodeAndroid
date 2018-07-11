package com.hxjr.p2p.ad5.ui.investment.bid.adapter;

import java.util.ArrayList;
import java.util.List;

import com.hxjr.p2p.ad5.bean.BidRecord;
import com.hxjr.p2p.ad5.utils.FormatUtil;
import com.hxjr.p2p.ad5.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class InvestInfoAdapter extends BaseAdapter
{
	private List<BidRecord> mList;
	
	private Context mContext;
	
	public InvestInfoAdapter(Context context)
	{
		mContext = context;
		mList = new ArrayList<BidRecord>();
	}
	
	public void addRecords(List<BidRecord> mList)
	{
		this.mList.clear();
		this.mList.addAll(mList);
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
		ViewHolder holder;
		if (convertView == null)
		{
			convertView = LayoutInflater.from(mContext).inflate(R.layout.bid_invest_item, parent, false);
			holder = new ViewHolder();
			holder.name = (TextView)convertView.findViewById(R.id.name);
			holder.amount = (TextView)convertView.findViewById(R.id.amount);
			holder.time = (TextView)convertView.findViewById(R.id.time);
			convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder)convertView.getTag();
		}
		BidRecord bidRecord = mList.get(position);
		holder.name.setText(bidRecord.getAccountName());
		holder.amount.setText(FormatUtil.formatMoney(bidRecord.getBidAmount() == null ? 0.00 : Double.valueOf(bidRecord.getBidAmount())));
		holder.time.setText(bidRecord.getBidTime());
		//		Timestamp timestamp = Timestamp.valueOf(bidRecord.getBidTime());
		//		Date date = new Date(timestamp.getTime());
		//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
		//		String time = sdf.format(date);		
		return convertView;
	}
	
	private class ViewHolder
	{
		TextView name;
		
		TextView amount;
		
		TextView time;
	}
	
}
