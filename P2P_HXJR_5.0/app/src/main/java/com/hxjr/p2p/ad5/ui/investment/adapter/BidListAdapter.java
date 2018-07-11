package com.hxjr.p2p.ad5.ui.investment.adapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dm.widgets.RoundProgressBar;
import com.hxjr.p2p.ad5.DMApplication;
import com.hxjr.p2p.ad5.R;
import com.hxjr.p2p.ad5.bean.BidBean;
import com.hxjr.p2p.ad5.utils.FormatUtil;
import com.hxjr.p2p.ad5.utils.UIHelper;

public class BidListAdapter extends BaseAdapter
{
	private List<BidBean> mList;
	
	private Context mContext;
	
	public BidListAdapter(Context context)
	{
		mContext = context;
		mList = new ArrayList<BidBean>(10);
	}
	
	/**
	 * 添加记录
	 * 
	 * @param list
	 */
	public void addAll(List<BidBean> list)
	{
		mList.addAll(list);
		this.notifyDataSetChanged();
	}
	
	/**
	 * 获取记录
	 * 
	 * @param list
	 */
	public List<BidBean> getAll()
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
			convertView = LayoutInflater.from(mContext).inflate(R.layout.bid_list_item, parent, false);
			holder = new ViewHolder();
			holder.bidTypeImg = (ImageView)convertView.findViewById(R.id.bidTypeImg);
			holder.bidTitle = (TextView)convertView.findViewById(R.id.bidTitle);
			holder.status = (TextView)convertView.findViewById(R.id.status);
			holder.yearRate = (TextView)convertView.findViewById(R.id.yearRate);
			holder.giftRate = (TextView)convertView.findViewById(R.id.giftRate);
			holder.cycle = (TextView)convertView.findViewById(R.id.cycle);
			holder.progress = (RoundProgressBar)convertView.findViewById(R.id.progress);
			
			holder.amount = (TextView)convertView.findViewById(R.id.amount);
			holder.bidFlag2 = (ImageView)convertView.findViewById(R.id.bidFlag2);
			holder.bid_dfb_text = (TextView)convertView.findViewById(R.id.bid_dfb_text);
			holder.bid_dfb_line = convertView.findViewById(R.id.bid_dfb_line);
			convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder)convertView.getTag();
		}
		BidBean bean = mList.get(position);
		holder.bidTitle.setText(bean.getBidTitle());
		holder.yearRate.setText(bean.getRate());
		String jlRate = bean.getJlRate();
		holder.giftRate.setText(jlRate);
		if (bean.getIsDay().equals("F"))
		{
			//按月分期
			UIHelper.formatDateTextSize(holder.cycle, bean.getCycle() + "个月");
		}
		else if (bean.getIsDay().equals("S"))
		{
			//按天分期
			UIHelper.formatDateTextSize(holder.cycle, bean.getCycle() + "天");
		}
		if (bean.getStatus().equals("HKZ"))
		{
			//还款中特殊处理
			Double a = Double.valueOf(bean.getAmount());
			Double b = Double.valueOf(bean.getRemainAmount());
			Double c = a - b;
			bean.setAmount(c + "");
			bean.setRemainAmount("0");
		}
		UIHelper.formatMoneyTextSize(holder.amount, FormatUtil.formatMoney(Double.valueOf(bean.getAmount())), 0.5f);
		if (bean.getStatus().equals("DFB") || bean.getStatus().equals("YFB"))
		{
			// 待发布或预发布
			holder.progress.setVisibility(View.VISIBLE);
			holder.progress.setYFB(true);
			holder.progress.setProgress(0);
			holder.bid_dfb_text.setVisibility(View.VISIBLE);
			holder.bid_dfb_line.setVisibility(View.VISIBLE);
			holder.bid_dfb_text.setText(bean.getPublishDate());
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
			long time = 0L;
			try
			{
				Date date = sdf.parse(bean.getPublishDate());
				time = date.getTime() - System.currentTimeMillis() + DMApplication.getInstance().diffTime;
			}
			catch (ParseException e)
			{
				e.printStackTrace();
			}
			if (holder.myCountDownTimer != null)
			{
				holder.myCountDownTimer.cancel();
			}
			holder.myCountDownTimer = new MyCountDownTimer(time, 1000L);
			holder.myCountDownTimer.textView = holder.bid_dfb_text;
			holder.myCountDownTimer.line = holder.bid_dfb_line;
			holder.myCountDownTimer.pos = position;
			holder.myCountDownTimer.start();
		}
		else if ("HKZ".equals(bean.getStatus()) || "YJQ".equals(bean.getStatus()))
		{
			//待付款，还款中，已结清
			holder.progress.setVisibility(View.VISIBLE);
			holder.progress.setProgress(100);
			holder.progress.setYFB(false);
			holder.bid_dfb_text.setVisibility(View.GONE);
			holder.bid_dfb_line.setVisibility(View.GONE);
			holder.bid_dfb_text.setTag(null);
		}
		else if("DFK".equals(bean.getStatus()))
		{
			// 待放款改为进度百分比
			holder.progress.setVisibility(View.VISIBLE);
			holder.progress.setYFB(true);
			holder.progress.setProgress(FormatUtil.getBidProgress(bean.getAmount(), bean.getRemainAmount()));
			holder.bid_dfb_text.setVisibility(View.GONE);
			holder.bid_dfb_line.setVisibility(View.GONE);
			holder.bid_dfb_text.setTag(null);
		}
		else
		{
			// 其它
			holder.progress.setYFB(false);
			holder.progress.setVisibility(View.VISIBLE);
			holder.progress.setProgress(FormatUtil.getBidProgress(bean.getAmount(), bean.getRemainAmount()));
			holder.bid_dfb_text.setVisibility(View.GONE);
			holder.bid_dfb_line.setVisibility(View.GONE);
			holder.bid_dfb_text.setTag(null);
		}
		
		String flag = bean.getFlag();
		holder.bidTypeImg.setImageResource(UIHelper.bidTyeImgs.containsKey(flag) ? UIHelper.bidTyeImgs.get(flag) : -1);
		holder.status.setText(FormatUtil.convertBidStatus(bean.getStatus()));
		if (bean.getIsXsb())
		{
			holder.bidFlag2.setImageResource(R.drawable.pic_xs);
			holder.bidFlag2.setVisibility(View.VISIBLE);
		}
		else if (bean.getIsJlb())
		{
			holder.bidFlag2.setImageResource(R.drawable.pic_jl);
			holder.bidFlag2.setVisibility(View.VISIBLE);
		}
		else
		{
			holder.bidFlag2.setVisibility(View.INVISIBLE);
		}
		return convertView;
	}
	
	private class ViewHolder
	{
		ImageView bidTypeImg;
		
		TextView bidTitle;
		
		TextView status;
		
		TextView yearRate;
		
		TextView giftRate;
		
		TextView cycle;
		
		TextView amount;
		
		RoundProgressBar progress;
		
		ImageView bidFlag2;
		
		TextView bid_dfb_text;
		
		View bid_dfb_line;
		
		MyCountDownTimer myCountDownTimer;
	}
	
	private class MyCountDownTimer extends CountDownTimer
	{
		public TextView textView;
		
		public View line;
		
		public int pos = -1;
		
		public MyCountDownTimer(long millisInFuture, long countDownInterval)
		{
			super(millisInFuture, countDownInterval);
		}
		
		@Override
		public void onTick(long millisUntilFinished)
		{
			if (this.textView != null)
			{
				textView.setText(surplusLongToString(millisUntilFinished));
			}
		}
		
		@Override
		public void onFinish()
		{
			if (this.textView != null)
			{
				textView.setVisibility(View.GONE);
			}
			if (this.line != null)
			{
				line.setVisibility(View.GONE);
			}
			if (this.pos != -1)
			{
				mList.get(pos).setStatus("TBZ");
			}
			
			BidListAdapter.this.notifyDataSetChanged();
			this.cancel();
		}
		
	}
	
	private static final long DAY = 24 * 3600L;
	
	private static final long HOUR = 24L;
	
	private static final long MINUTE = 60L;
	
	private static final long SECOND = 60L;
	
	public String surplusLongToString(long between)
	{
		long temp0 = between / 1000;
		long temp1 = temp0 / 60;
		long temp2 = temp1 / 60;
		long second = temp0 % SECOND;
		long minute = temp1 % MINUTE;
		long hour = temp2 % HOUR;
		long day = temp0 / DAY;
		return "倒计时：" + day + "天" + hour + "小时" + minute + "分" + second + "秒";
	}
}
