package com.hxjr.p2p.ad5.ui.discovery.news.adapter;

import java.util.ArrayList;
import java.util.List;

import com.hxjr.p2p.ad5.bean.HomeNotice;
import com.hxjr.p2p.ad5.utils.DMConstant;
import com.hxjr.p2p.ad5.R;
import com.dm.utils.StringUtils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

/**
 * 网站公告适配器
 * @author jiaohongyun
 *
 */
public class NoticeAdapter extends BaseAdapter
{
	private Context context;
	
	private List<HomeNotice> newsList;
	
	private ViewHolder viewHoler;
	
	private boolean isShowImage = false;
	
	private String imgUrlPre;
	
	public NoticeAdapter(Context context, boolean isShowImage, List<HomeNotice> newsList)
	{
		this.context = context;
		this.isShowImage = isShowImage;
		this.newsList = new ArrayList<HomeNotice>(DMConstant.DigitalConstant.TEN);
		this.newsList.addAll(newsList);
	}
	
	public NoticeAdapter(Context context, boolean isShowImage)
	{
		this.context = context;
		this.isShowImage = isShowImage;
		this.newsList = new ArrayList<HomeNotice>(DMConstant.DigitalConstant.TEN);
	}
	
	public void updateList(List<HomeNotice> newsInfos)
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
	public void addAll(List<HomeNotice> list)
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
			convertView = LayoutInflater.from(context).inflate(R.layout.news_item_img, parent, false);
			viewHoler = new ViewHolder();
			viewHoler.newsImg = (ImageView)convertView.findViewById(R.id.news_item_iv);
			viewHoler.newsImg.setScaleType(ScaleType.FIT_XY);
			viewHoler.newsTitle = (TextView)convertView.findViewById(R.id.news_item_title);
			viewHoler.newsSummary = (TextView)convertView.findViewById(R.id.news_item_summary);
			convertView.setTag(viewHoler);
		}
		else
		{
			viewHoler = (ViewHolder)convertView.getTag();
		}
		
		HomeNotice news = newsList.get(position);
		viewHoler.newsTitle.setText(news.getTitle());
		String summary = news.getTitle();
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
