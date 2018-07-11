package com.hxjr.p2p.ad5.widgets.calendar;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.hxjr.p2p.ad5.R;

public class TitleGridAdapter extends BaseAdapter
{
	
	int[] titles = new int[] {R.string.Sun, R.string.Mon, R.string.Tue, R.string.Wed, R.string.Thu, R.string.Fri, R.string.Sat};
	
	private Activity activity;
	
	public TitleGridAdapter(Activity a)
	{
		activity = a;
	}
	
	@Override
	public int getCount()
	{
		return titles.length;
	}
	
	@Override
	public Object getItem(int position)
	{
		return titles[position];
	}
	
	@Override
	public long getItemId(int position)
	{
		return position;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		LinearLayout iv = new LinearLayout(activity);
		TextView txtDay = new TextView(activity);
		txtDay.setFocusable(false);
		txtDay.setBackgroundColor(Color.TRANSPARENT);
		iv.setOrientation(LinearLayout.VERTICAL);
		
		txtDay.setGravity(Gravity.CENTER);
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		
		Resources res = activity.getResources();
		txtDay.setTextColor(res.getColor(R.color.white));
		txtDay.setBackgroundColor(res.getColor(R.color.back_grey));
		
		txtDay.setText((Integer)getItem(position));
		iv.addView(txtDay, lp);
		return iv;
	}
}
