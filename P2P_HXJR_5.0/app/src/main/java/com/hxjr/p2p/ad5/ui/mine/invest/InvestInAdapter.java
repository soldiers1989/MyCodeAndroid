package com.hxjr.p2p.ad5.ui.mine.invest;

import java.util.ArrayList;
import java.util.List;

import com.hxjr.p2p.ad5.bean.MyInvestIn;
import com.hxjr.p2p.ad5.utils.DMConstant;
import com.hxjr.p2p.ad5.utils.FormatUtil;
import com.hxjr.p2p.ad5.utils.UIHelper;
import com.hxjr.p2p.ad5.R;
import com.dm.widgets.RoundProgressBar;

import android.content.Context;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * 投标中-还款适配列表
 * @author  huangkaibo
 * @date 2015-12-4
 */
public class InvestInAdapter extends BaseAdapter
{
	private LayoutInflater inflater;
	
	private List<MyInvestIn> mList;
	
	public InvestInAdapter(Context context)
	{
		inflater = LayoutInflater.from(context);
		mList = new ArrayList<MyInvestIn>(DMConstant.DigitalConstant.TEN);
	}
	
	public void clearList()
	{
		mList.clear();
	}
	
	public void addAll(List<MyInvestIn> list)
	{
		mList.addAll(list);
		notifyDataSetChanged();
	}
	
	@Override
	public int getCount()
	{
		return mList.size();
	}
	
	@Override
	public MyInvestIn getItem(int position)
	{
		return mList.get(position);
	}
	
	@Override
	public long getItemId(int position)
	{
		return position;
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent)
	{
		ViewHolder holder = null;
		if (null == convertView)
		{
			convertView = inflater.inflate(R.layout.my_invest_in_list_item, parent, false);
			holder = new ViewHolder();
			holder.bidTitle = (TextView)convertView.findViewById(R.id.bidTitle);
			holder.status = (TextView)convertView.findViewById(R.id.status);
			holder.yearRate = (TextView)convertView.findViewById(R.id.yearRate);
			holder.giftRate_tv = (TextView)convertView.findViewById(R.id.giftRate_tv);
			holder.cycle = (TextView)convertView.findViewById(R.id.cycle);
			holder.progress = (RoundProgressBar)convertView.findViewById(R.id.progress);
			holder.amount = (TextView)convertView.findViewById(R.id.amount);
			convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder)convertView.getTag();
		}
		
		MyInvestIn bean = getItem(position);
		holder.bidTitle.setText(bean.getBidName());
		holder.status.setText(bean.getStatus() != null ? FormatUtil.convertBidStatus(bean.getStatus()) : "");
		holder.yearRate.setText(bean.getNhl());
		if (bean.getJxl() == 0.00)
		{
			holder.giftRate_tv.setVisibility(View.GONE);
		}
		else
		{
			holder.giftRate_tv.setVisibility(View.VISIBLE);
			holder.giftRate_tv.setText("+" + FormatUtil.get2String(bean.getJxl()) + "%");
		}
		holder.progress.setProgress((int)bean.getProcess());
		UIHelper.formatMoneyTextSize(holder.amount, FormatUtil.formatMoney(bean.getGmjg()),0.5f);
		holder.progress.setProgress((int)bean.getProcess());
		
		if (bean.isDay())
		{
			//按天分期
			UIHelper.formatDateTextSize(holder.cycle, bean.getJkzq() + "天");
		}
		else
		{
			//按月分期
			UIHelper.formatDateTextSize(holder.cycle, bean.getJkzq() + "个月");
		}
		return convertView;
	}
	
	private class ViewHolder
	{
		TextView bidTitle;
		
		TextView status;
		
		TextView yearRate;
		
		TextView giftRate_tv;
		
		TextView cycle;
		
		TextView amount;
		
		RoundProgressBar progress;
		
		MyCountDownTimer myCountDownTimer;
	}
	
	private class MyCountDownTimer extends CountDownTimer
	{
		public TextView textView;
		
		public View line;
		
		public MyCountDownTimer(long millisInFuture, long countDownInterval)
		{
			super(millisInFuture, countDownInterval);
		}
		
		@Override
		public void onTick(long millisUntilFinished)
		{
			if (this.textView != null)
			{
				long second = millisUntilFinished / 1000;
				textView.setText(surplusLongToString(second));
			}
		}
		
		@Override
		public void onFinish()
		{
			textView.setVisibility(View.GONE);
			line.setVisibility(View.GONE);
			InvestInAdapter.this.notifyDataSetChanged();
			this.cancel();
		}
		
	}
	
	private static final long DAY = 24 * 3600L;
	
	private static final long HOUR = (24 * 3600) / 3600L;
	
	private static final long MINUTE = 3600 / 60L;
	
	private static final long SECOND = 60L;
	
	public String surplusLongToString(long between)
	{
		long day = between / DAY;
		long hour = between % HOUR;
		long minute = between % MINUTE;
		long second = between % SECOND;
		return "倒计时：" + day + "天" + hour + "小时" + minute + "分" + second + "秒";
	}
}
