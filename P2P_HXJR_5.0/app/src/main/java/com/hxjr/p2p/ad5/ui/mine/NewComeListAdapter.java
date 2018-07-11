package com.hxjr.p2p.ad5.ui.mine;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

import com.hxjr.p2p.ad5.bean.News;
import com.hxjr.p2p.ad5.utils.DMConstant;
import com.hxjr.p2p.ad5.R;
import com.dm.utils.StringUtils;

/**
 * 媒体报道
 * @author  huangkaibo
 * @date 2015年11月6日
 */
public class NewComeListAdapter extends BaseAdapter
{
	private Context context;
	
	private List<News> newsList;
	
	private ViewHolder viewHoler;
	
	private boolean isShowImage = false;
	
	private String imgUrlPre;
	
	public NewComeListAdapter(Context context, boolean isShowImage, List<News> newsList)
	{
		this.context = context;
		this.isShowImage = isShowImage;
		this.newsList = new ArrayList<News>(DMConstant.DigitalConstant.TEN);
		this.newsList.addAll(newsList);
	}
	
	public NewComeListAdapter(Context context, boolean isShowImage)
	{
		this.context = context;
		this.isShowImage = isShowImage;
		this.newsList = new ArrayList<News>(DMConstant.DigitalConstant.TEN);
	}
	
	public void updateList(List<News> newsInfos)
	{
		if (null != newsInfos && null != newsList)
		{
			newsList.clear();
			newsList.addAll(newsInfos);
			notifyDataSetChanged();
		}
	}
	
	/**
	 * 添加记录
	 * @param list
	 */
	public void addAll(List<News> list)
	{
		newsList.addAll(list);
		this.notifyDataSetChanged();
	}
	
	/**
	 * 清除记录
	 */
	public void clearList()
	{
		newsList.clear();
	}
	
	public void isShowImage(boolean isShowImage)
	{
		this.isShowImage = isShowImage;
	}
	
	@Override
	public int getCount()
	{
		return newsList.size();
	}
	
	@Override
	public Object getItem(int position)
	{
		return newsList.get(position);
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
			convertView = LayoutInflater.from(context).inflate(R.layout.newcome_item, parent, false);
			viewHoler = new ViewHolder();
			viewHoler.newsImg = (ImageView)convertView.findViewById(R.id.news_item_iv);
			viewHoler.newsImg.setScaleType(ScaleType.FIT_XY);
			viewHoler.newsTitle = (TextView)convertView.findViewById(R.id.news_item_title);
			viewHoler.newsSummary = (TextView)convertView.findViewById(R.id.news_item_summary);
			viewHoler.newsSummary.setVisibility(View.GONE);
			convertView.setTag(viewHoler);
		}
		else
		{
			viewHoler = (ViewHolder)convertView.getTag();
		}
		
		News news = newsList.get(position);
		viewHoler.newsTitle.setText(news.getTitle());
		String summary = news.getDesc();
		viewHoler.newsSummary.setText(StringUtils.isEmptyOrNull(summary) ? "暂无摘要" : summary);
		//		
		//		if (isShowImage)
		//		{
		//			viewHoler.newsImg.setVisibility(View.VISIBLE);
		//		}
		//		else
		//		{
		//			viewHoler.newsImg.setVisibility(View.GONE);
		//		}
		return convertView;
	}
	
	class ViewHolder
	{
		private ImageView newsImg;
		
		private TextView newsTitle;
		
		private TextView newsSummary;
	}
}
