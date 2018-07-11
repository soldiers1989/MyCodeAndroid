package com.hxjr.p2p.ad5.ui.discovery.donation;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hxjr.p2p.ad5.bean.GyInfo;
import com.hxjr.p2p.ad5.utils.AmountUtil;
import com.hxjr.p2p.ad5.utils.DMConstant;
import com.hxjr.p2p.ad5.utils.FormatUtil;
import com.hxjr.p2p.ad5.R;
import com.dm.widgets.RoundProgressBar;

public class DonationAdapter extends BaseAdapter
{
	private List<GyInfo> mList;
	
	private Context context;
	
	private ViewHolder holder;
	
	public DonationAdapter(Context context)
	{
		this.context = context;
		mList = new ArrayList<GyInfo>(DMConstant.DigitalConstant.TEN);
	}
	
	/**
	 * 添加记录
	 * @param list
	 */
	public void addAll(List<GyInfo> list)
	{
		mList.addAll(list);
		this.notifyDataSetChanged();
	}
	
	/**
	 * 清除记录
	 */
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
		if (null == convertView)
		{
			convertView = LayoutInflater.from(context).inflate(R.layout.donation_list_item, parent, false);
			holder = new ViewHolder();
			holder.loanName_tv = (TextView)convertView.findViewById(R.id.loanName_tv);
			holder.loanAmount_tv = (TextView)convertView.findViewById(R.id.loanAmount_tv);
			holder.organisers_tv = (TextView)convertView.findViewById(R.id.organisers_tv);
			holder.progress = (RoundProgressBar)convertView.findViewById(R.id.progress);
			convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder)convertView.getTag();
		}
		
		GyInfo gyInfo = mList.get(position);
		holder.loanName_tv.setText(gyInfo.getLoanName());
		String loanAmount = AmountUtil.getString(gyInfo.getLoanAmount());
		holder.loanAmount_tv.setText(FormatUtil.formatMoney(Double.valueOf(loanAmount)));
		holder.organisers_tv.setText(gyInfo.getOrganisers());
		if (gyInfo.isTimeEnd())// 未捐满已到期
		{
			holder.progress.setYFB(true);
		}
		holder.progress.setProgress((int)(gyInfo.getProgress() * 100));
		return convertView;
	}
	
	class ViewHolder
	{
		TextView loanName_tv;
		
		TextView loanAmount_tv;
		
		TextView organisers_tv;
		
		RoundProgressBar progress;
	}
}
