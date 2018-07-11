package com.hxjr.p2p.ad5.ui.discovery.donation;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hxjr.p2p.ad5.bean.GyRecordList;
import com.hxjr.p2p.ad5.utils.DMConstant;
import com.hxjr.p2p.ad5.R;
/**
 * 捐赠记录
 * @author  huangkaibo
 * @date 2015年12月1日
 */
public class DonationRecordAdapter extends BaseAdapter
{
	private Context context;
	
	private List<GyRecordList> mList;
	
	private ViewHolder holder;
	
	public DonationRecordAdapter(Context context)
	{
		this.context = context;
		this.mList = new ArrayList<GyRecordList>(DMConstant.DigitalConstant.TEN);
	}
	
	/**
	 * 添加记录
	 * @param list
	 */
	public void addAll(List<GyRecordList> list)
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
			convertView = LayoutInflater.from(context).inflate(R.layout.donation_record_list_item, parent, false);
			holder = new ViewHolder();
			holder.userName_tv = (TextView)convertView.findViewById(R.id.userName_tv);
			holder.investAmount_tv = (TextView)convertView.findViewById(R.id.investAmount_tv);
			holder.investTime_tv = (TextView)convertView.findViewById(R.id.investTime_tv);
			convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder)convertView.getTag();
		}
		
		GyRecordList gyRecord = mList.get(position);
		
		holder.userName_tv.setText(gyRecord.getUserName());
		
		holder.investAmount_tv.setText(gyRecord.getLoanAmount());
		
		holder.investTime_tv.setText(gyRecord.getLocanTime());
		
		return convertView;
	}
	
	class ViewHolder
	{
		TextView userName_tv;
		
		TextView investAmount_tv;
		
		TextView investTime_tv;
		
	}
	
}
