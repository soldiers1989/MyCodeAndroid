package com.hxjr.p2p.ad5.ui.discovery.rich.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hxjr.p2p.ad5.bean.BidRankInfo;
import com.hxjr.p2p.ad5.utils.DMConstant;
import com.hxjr.p2p.ad5.R;

/**周排行
 * @author  huangkaibo
 * @date 2015-11-5
 */
public class RichListAdapter extends BaseAdapter
{
	private Context context;
	
	private List<BidRankInfo> mList;
	
	private ViewHolder holder;
	
	public RichListAdapter(Context context)
	{
		this.context = context;
		mList = new ArrayList<BidRankInfo>(DMConstant.DigitalConstant.TEN);
	}
	
	/**
	 * 添加记录
	 * @param list
	 */
	public void addAll(List<BidRankInfo> list)
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
			convertView = LayoutInflater.from(context).inflate(R.layout.rich_list_week_list_item, parent, false);
			holder = new ViewHolder();
			holder.ranking_tv = (TextView)convertView.findViewById(R.id.ranking_tv);
			holder.ranking_iv = (ImageView)convertView.findViewById(R.id.ranking_iv);
			holder.user_name_tv = (TextView)convertView.findViewById(R.id.user_name_tv);
			holder.invest_money_tv = (TextView)convertView.findViewById(R.id.invest_money_tv);
			convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder)convertView.getTag();
		}
		
		BidRankInfo br = mList.get(position);
		holder.user_name_tv.setText(br.getUserName());
		holder.invest_money_tv.setText(br.getAmount());
		switch (position)
		{
			case 0:
				holder.ranking_tv.setVisibility(View.GONE);
				holder.ranking_iv.setVisibility(View.VISIBLE);
				holder.ranking_iv.setBackgroundResource(R.drawable.icon_win1);
				break;
			case 1:
				holder.ranking_tv.setVisibility(View.GONE);
				holder.ranking_iv.setVisibility(View.VISIBLE);
				holder.ranking_iv.setBackgroundResource(R.drawable.icon_win2);
				break;
			case 2:
				holder.ranking_tv.setVisibility(View.GONE);
				holder.ranking_iv.setVisibility(View.VISIBLE);
				holder.ranking_iv.setBackgroundResource(R.drawable.icon_win3);
				break;
			
			default:
				holder.ranking_tv.setVisibility(View.VISIBLE);
				holder.ranking_tv.setText(br.getRankId()+"");
				holder.ranking_iv.setVisibility(View.GONE);
				break;
		}
		return convertView;
	}
	
	class ViewHolder
	{
		TextView ranking_tv;
		ImageView ranking_iv;
		TextView user_name_tv;
		TextView invest_money_tv;
	}
}
