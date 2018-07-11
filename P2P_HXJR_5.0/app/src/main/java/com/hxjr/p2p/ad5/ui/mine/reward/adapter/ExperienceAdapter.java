package com.hxjr.p2p.ad5.ui.mine.reward.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hxjr.p2p.ad5.DMApplication;
import com.hxjr.p2p.ad5.bean.EXP_STATUS;
import com.hxjr.p2p.ad5.bean.ExperienceInfo;
import com.hxjr.p2p.ad5.utils.AmountUtil;
import com.hxjr.p2p.ad5.utils.DMConstant;
import com.hxjr.p2p.ad5.utils.TimeUtils;
import com.hxjr.p2p.ad5.R;

/**
 * @author  huruidong
 * @date 2015-11-20
 */
public class ExperienceAdapter extends BaseAdapter
{
	private Context context;
	
	private List<ExperienceInfo> mList;
	
	private ViewHolder holder;
	
	public ExperienceAdapter(Context context)
	{
		this.context = context;
		mList = new ArrayList<ExperienceInfo>(DMConstant.DigitalConstant.TEN);
	}
	
	public void updateList(List<ExperienceInfo> list)
	{
		if (null != mList && null != list)
		{
			mList.clear();
			mList.addAll(list);
			notifyDataSetChanged();
		}
	}
	
	/**
	 * 添加记录
	 * @param list
	 */
	public void addAll(List<ExperienceInfo> list)
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
	public ExperienceInfo getItem(int position)
	{
		return mList.get(position);
	}
	
	@Override
	public long getItemId(int position)
	{
		return position;
	}
	
	List<ViewHolder> holders = new ArrayList<ViewHolder>();
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		if (null == convertView)
		{
			convertView = LayoutInflater.from(context).inflate(R.layout.my_reward_experienc_list_item, parent, false);
			holder = new ViewHolder();
			holder.tvw_content = (TextView)convertView.findViewById(R.id.tvw_content);
			holder.tvw_experience_money = (TextView)convertView.findViewById(R.id.tvw_experience_money);
			holder.ivw_status = (ImageView)convertView.findViewById(R.id.ivw_status);
			holder.tvw_use = (TextView)convertView.findViewById(R.id.tvw_use);
			convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder)convertView.getTag();
		}
		
		ExperienceInfo exp = getItem(position);
		double expAmount = Double.valueOf(AmountUtil.getString(exp.getExpAmount()));
		holder.tvw_experience_money.setText((int)(expAmount) + "");
		String status = exp.getStatus();
		//判断是否是未使用，如果是，则重新计算倒计时并显示
		long countTime = 1;
		if (null != exp.getEndDate() && !"".equals(exp.getEndDate()))
		{
			//失效时间
			long timeL = TimeUtils.stringToDate(exp.getEndDate()).getTime();
			//失效剩余时间
			countTime = timeL - System.currentTimeMillis() + DMApplication.getInstance().diffTime;
		}
		if (EXP_STATUS.WSY.getName().equals(status) && countTime > 0)
		{//未使用：显示倒计时
			//停止计时器
			if (holder.timeCount != null)
			{
				holder.timeCount.cancel();
			}
			holder.tvw_use.setText(context.getString(R.string.my_reward_invest_immediate));
			holder.tvw_content.setVisibility(View.VISIBLE);
			holder.ivw_status.setImageResource(R.drawable.icon_wsy);
			holder.timeCount = new TimeCount(countTime, 1000L, position);
			holder.timeCount.content = holder.tvw_content;
			holder.timeCount.pos = position;
			holder.timeCount.start();
		}
		else if (EXP_STATUS.YGQ.getName().equals(status) || (EXP_STATUS.WSY.getName().equals(status) && countTime <= 0))
		{ //已过期：显示截至时间
			//失效时间
			String timeS = exp.getEndDate();
			holder.tvw_use.setVisibility(View.GONE);
			holder.tvw_content.setVisibility(View.VISIBLE);
			holder.ivw_status.setImageResource(R.drawable.icon_ygq);
			holder.tvw_content.setText(context.getString(R.string.experience_time_settle, timeS));
		}
		else if (EXP_STATUS.YTZ.getName().equals(status))
		{ //已结清、已投资：显示使用时间
			holder.ivw_status.setImageResource(R.drawable.icon_ytz);
			holder.tvw_content.setVisibility(View.GONE);
		}
		else if (EXP_STATUS.YJQ.getName().equals(status))
		{ //已结清：显示使用时间
			holder.ivw_status.setImageResource(R.drawable.icon_yjq);
			holder.tvw_content.setVisibility(View.GONE);
		}
		else if (EXP_STATUS.YWT.getName().equals(status))
		{ //使用中：显示使用时间
			holder.ivw_status.setImageResource(R.drawable.icon_syz);
			holder.tvw_content.setVisibility(View.GONE);
		}
		return convertView;
	}
	
	class ViewHolder
	{
		/**
		 * 体验金金额
		 */
		public TextView tvw_experience_money;
		
		/**
		 * 内容（使用时间/倒计时）
		 */
		public TextView tvw_content;
		
		/**
		 * 状态
		 */
		public ImageView ivw_status;
		
		/**
		 * 用途
		 */
		public TextView tvw_use;
		
		/**
		 * 倒计时
		 */
		public TimeCount timeCount;
	}
	
	private long DAY = 24 * 3600L;
	
	private long HOUR = 24L;
	
	private long MINUTE = 60L;
	
	private long SECOND = 60L;
	
	/* 定义一个倒计时的内部类 */
	class TimeCount extends CountDownTimer
	{
		TextView content;
		
		int pos = -1;
		
		public TimeCount(long millisInFuture, long countDownInterval, int pos)
		{
			super(millisInFuture, countDownInterval);//参数依次为总时长,和计时的时间间隔
			this.pos = pos;
		}
		
		@Override
		public void onFinish()
		{//计时完毕时触发
			cancel();
			getItem(pos).setStatus(EXP_STATUS.YGQ.getName());
			notifyDataSetChanged();
		}
		
		@Override
		public void onTick(long millisUntilFinished)
		{//计时过程显示
			//			int day = 0, hour = 0, min = 0, sec = 0;
			//			day = (int)(millisUntilFinished / (1000 * 60 * 60 * 24));
			//			hour = (int)((millisUntilFinished - (day * 1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
			//			min = (int)((millisUntilFinished - (day * 1000 * 60 * 60 * 24) - (hour * 1000 * 60 * 60)) / (1000 * 60));
			//			sec = (int)((millisUntilFinished - (day * 1000 * 60 * 60 * 24) - (hour * 1000 * 60 * 60) - (min * 1000 * 60)) / 1000);
			//			String contentS = context.getString(R.string.experience_time_count, day, hour, min, sec);
			
			long temp0 = millisUntilFinished / 1000;
			long temp1 = temp0 / 60;
			long temp2 = temp1 / 60;
			long second = temp0 % SECOND;
			long minute = temp1 % MINUTE;
			long hour = temp2 % HOUR;
			long day = temp0 / DAY;
			String contentS = context.getString(R.string.experience_time_count, day, hour, minute, second);
			content.setText(contentS);
		}
	}
}
