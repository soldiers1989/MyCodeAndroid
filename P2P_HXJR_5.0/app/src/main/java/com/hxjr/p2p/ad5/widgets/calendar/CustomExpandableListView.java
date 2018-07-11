package com.hxjr.p2p.ad5.widgets.calendar;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ExpandableListView;

/**
 * 解决ScrollView嵌套ExpandableListView导致界面高度错误
 * 
 * @author  sushuo
 * @date 2015年9月21日
 */
public class CustomExpandableListView extends ExpandableListView
{
	
	public CustomExpandableListView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		// TODO Auto-generated constructor stub  
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		// TODO Auto-generated method stub  
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
		
		super.onMeasure(widthMeasureSpec, expandSpec);
	}
}
