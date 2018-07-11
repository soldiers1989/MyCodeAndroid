package com.hxjr.p2p.ad5.widgets.calendar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.GridView;

import com.hxjr.p2p.ad5.R;

public class WrapContentHeightViewPager extends LazyViewPager
{
	
	public WrapContentHeightViewPager(Context context)
	{
		super(context);
	}
	
	public WrapContentHeightViewPager(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}
	
	// ViewPager不能高度自适应？height=wrap_content 无效解决办法 http://www.tuicool.com/articles/nmIjym
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		
		int height = 0;
		int titleHeight = 0;
		//下面遍历所有child的高度
		for (int i = 0; i < getChildCount(); i++)
		{
			View child = getChildAt(i);
			child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
			int h = child.getMeasuredHeight();
			GridView titleGridView = (GridView)child.findViewById(R.id.gridview);
			titleHeight = titleGridView.getMeasuredHeight();
			if (h > height) //采用最大的view的高度。
				height = h;
			
			break;
		}
		heightMeasureSpec = MeasureSpec.makeMeasureSpec((height - titleHeight) * 6 + titleHeight + 5, MeasureSpec.EXACTLY);
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
}