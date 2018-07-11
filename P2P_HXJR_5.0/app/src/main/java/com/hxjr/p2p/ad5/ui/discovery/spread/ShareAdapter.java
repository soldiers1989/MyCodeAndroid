package com.hxjr.p2p.ad5.ui.discovery.spread;

import com.hxjr.p2p.ad5.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author  tangjian
 * @date 2015-8-20
 */

public class ShareAdapter extends BaseAdapter
{
	
	private static String[] shareNames = new String[] {"微信", "朋友圈", "QQ", "QQ空间"};
	
	private int[] shareIcons = new int[] {R.drawable.weixin, R.drawable.pyq, R.drawable.qqshare, R.drawable.qqkj};
	
	private LayoutInflater inflater;
	
	public ShareAdapter(Context context)
	{
		inflater = LayoutInflater.from(context);
	}
	
	@Override
	public int getCount()
	{
		return shareNames.length;
	}
	
	@Override
	public Object getItem(int position)
	{
		return null;
	}
	
	@Override
	public long getItemId(int position)
	{
		return 0;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		if (convertView == null)
		{
			convertView = inflater.inflate(R.layout.share_item, parent, false);
		}
		ImageView shareIcon = (ImageView)convertView.findViewById(R.id.share_icon);
		TextView shareTitle = (TextView)convertView.findViewById(R.id.share_title);
		shareIcon.setImageResource(shareIcons[position]);
		shareTitle.setText(shareNames[position]);
		
		return convertView;
	}
}
