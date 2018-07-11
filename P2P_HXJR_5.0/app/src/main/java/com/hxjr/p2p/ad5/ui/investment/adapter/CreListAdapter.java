package com.hxjr.p2p.ad5.ui.investment.adapter;

import java.util.ArrayList;
import java.util.List;

import com.hxjr.p2p.ad5.bean.Creditor;
import com.hxjr.p2p.ad5.utils.FormatUtil;
import com.hxjr.p2p.ad5.utils.UIHelper;
import com.hxjr.p2p.ad5.R;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/***
 * 投资-->债权转让
 * @author  tangjian
 * @date 2015年12月8日
 */
public class CreListAdapter extends BaseAdapter
{
	private List<Creditor> mList;
	
	private Context mContext;
	
	public CreListAdapter(Context context)
	{
		mContext = context;
		mList = new ArrayList<Creditor>(10);
	}
	
	/**
	 * 添加记录
	 * @param list
	 */
	public void addAll(List<Creditor> list)
	{
		mList.addAll(list);
		this.notifyDataSetChanged();
	}
	
	/**
	 * 获取记录
	 * @param list
	 */
	public List<Creditor> getAll()
	{
		return mList;
	}
	
	/**
	 * 清除记录
	 */
	public void clearList()
	{
		mList.clear();
	}
	
	/**
	 * 清除记录
	 * @param isRefreshView 是否刷新视图
	 */
	public void clearList(boolean isRefreshView)
	{
		mList.clear();
		if (isRefreshView)
		{
			this.notifyDataSetChanged();
		}
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
		return mList.get(position).getId();
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		ViewHolder holder;
		if (convertView == null)
		{
			convertView = LayoutInflater.from(mContext).inflate(R.layout.cre_list_item, parent, false);
			holder = new ViewHolder();
			holder.creName = (TextView)convertView.findViewById(R.id.creName);
			holder.creYearRate = (TextView)convertView.findViewById(R.id.creYearRate);
			holder.creJlRate = (TextView)convertView.findViewById(R.id.creJlRate);
			holder.creLimit = (TextView)convertView.findViewById(R.id.creLimit);
			holder.creAmount = (TextView)convertView.findViewById(R.id.creAmount);
			convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder)convertView.getTag();
		}
		Creditor creditor = mList.get(position);
		
		//title
		holder.creName.setText(creditor.getCreditorTitle());
		@SuppressWarnings("deprecation")
		Drawable drawable = mContext.getResources().getDrawable(UIHelper.bidTyeImgs.get(creditor.getFlag()));
		drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
		holder.creName.setCompoundDrawables(drawable, null, null, null);
		
		//年利率
		String yearRateS = creditor.getRate();
		if (!yearRateS.contains("%"))
		{
			yearRateS = yearRateS + "%";
		}
		SpannableString yearRateSBS = new SpannableString(yearRateS);
		yearRateSBS.setSpan(new RelativeSizeSpan(0.5f),
			yearRateS.length() - 1,
			yearRateS.length(),
			SpannableString.SPAN_INCLUSIVE_EXCLUSIVE);
		holder.creYearRate.setText(yearRateSBS);
		holder.creJlRate.setText(creditor.getJlRate());
		String terms = "";
		SpannableString termsSB = null;
		
		//剩余期限
		if ("S".equals(creditor.getIsDay()))
		{
			//天标
			terms = creditor.getDays().trim() + "天";
			termsSB = new SpannableString(terms);
			termsSB.setSpan(new RelativeSizeSpan(0.5f),
				terms.length() - 1,
				terms.length(),
				SpannableString.SPAN_INCLUSIVE_EXCLUSIVE);
			holder.creLimit.setText(termsSB);
		}
		else
		{
			terms = creditor.getRemainCycle() + "个月";
			termsSB = new SpannableString(terms);
			termsSB.setSpan(new RelativeSizeSpan(0.5f),
				terms.length() - 2,
				terms.length(),
				SpannableString.SPAN_INCLUSIVE_EXCLUSIVE);
			holder.creLimit.setText(termsSB);
		}
		
		//转让金额
		String lendAmount = "";
		SpannableString lendAmountSB = null;
		lendAmount = FormatUtil.formatMoney(Double.valueOf(creditor.getSalePrice()));
		int index = -1;
		if (lendAmount.endsWith("万元") || lendAmount.endsWith("百万"))
		{
			index = 2;
		}
		else if (lendAmount.endsWith("元") || lendAmount.endsWith("亿"))
		{
			index = 1;
		}
		lendAmountSB = new SpannableString(lendAmount);
		lendAmountSB.setSpan(new RelativeSizeSpan(0.5f),
			lendAmount.length() - index,
			lendAmount.length(),
			SpannableString.SPAN_INCLUSIVE_EXCLUSIVE);
		holder.creAmount.setText(lendAmountSB);
		return convertView;
	}
	
	private class ViewHolder
	{
		TextView creName;
		
		TextView creYearRate;
		
		TextView creLimit;
		
		TextView creAmount;
		
		TextView creJlRate;
	}
}
