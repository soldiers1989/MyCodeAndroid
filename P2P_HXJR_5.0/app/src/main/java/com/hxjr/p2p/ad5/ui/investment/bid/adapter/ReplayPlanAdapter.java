package com.hxjr.p2p.ad5.ui.investment.bid.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hxjr.p2p.ad5.bean.HkjhBean;
import com.hxjr.p2p.ad5.R;

public class ReplayPlanAdapter extends BaseAdapter
{
	private List<HkjhBean> mList;
	
	private Context mContext;
	
	public ReplayPlanAdapter(Context context, List<HkjhBean> list)
	{
		mContext = context;
		mList = list;
	}
	
	@Override
	public int getCount()
	{
		return mList.size();
	}
	
	@Override
	public HkjhBean getItem(int position)
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
			convertView = LayoutInflater.from(mContext).inflate(R.layout.replay_plan_item, parent, false);
			holder = new ViewHolder();
			holder.tvw_time = (TextView)convertView.findViewById(R.id.tvw_time);
			holder.tvw_status = (TextView)convertView.findViewById(R.id.tvw_status);
			holder.tvw_type = (TextView)convertView.findViewById(R.id.tvw_type);
			holder.tvw_time_real = (TextView)convertView.findViewById(R.id.tvw_time_real);
			convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder)convertView.getTag();
		}
		
		holder.tvw_time.setText(getItem(position).getRepayDate());
		holder.tvw_status.setText(getItem(position).getStatus());
		holder.tvw_type.setText(getItem(position).getRepayType() + "/" + getItem(position).getAmount());
		holder.tvw_time_real.setText(getItem(position).getRealDate());
		
		return convertView;
	}
	
	private class ViewHolder
	{
		TextView tvw_time;
		
		TextView tvw_status;
		
		TextView tvw_type;
		
		TextView tvw_time_real;
	}
	
}
