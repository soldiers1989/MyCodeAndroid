package com.hxjr.p2p.ad5.ui.mine.reward.adapter;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hxjr.p2p.ad5.bean.RewardInfo;
import com.hxjr.p2p.ad5.ui.mine.reward.RedEnvelopeActivity;
import com.hxjr.p2p.ad5.utils.AmountUtil;
import com.hxjr.p2p.ad5.utils.DMConstant;
import com.hxjr.p2p.ad5.utils.TimeUtils;
import com.hxjr.p2p.ad5.R;

/**
 * @author  huangkaibo
 * @date 2015-11-5
 */
public class RedEnvelopeAdapter extends BaseAdapter
{
	private Context context;
	
	private List<RewardInfo> mList;
	
	private ViewHolder holder;
	
	public RedEnvelopeAdapter(Context context)
	{
		this.context = context;
		mList = new ArrayList<RewardInfo>(DMConstant.DigitalConstant.TEN);
	}
	
	public void updateList(List<RewardInfo> list)
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
	public void addAll(List<RewardInfo> list)
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
	public RewardInfo getItem(int position)
	{
		return mList.get(position);
	}
	
	@Override
	public long getItemId(int position)
	{
		return position;
	}
	
	List<ViewHolder> holders = new ArrayList<ViewHolder>();
	
	@SuppressLint("StringFormatMatches")
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		if (null == convertView)
		{
			convertView = LayoutInflater.from(context).inflate(R.layout.red_envelope_list_item, parent, false);
			holder = new ViewHolder();
			holder.ivw_status = (ImageView)convertView.findViewById(R.id.ivw_status);
			holder.lly_hb_detail = (LinearLayout)convertView.findViewById(R.id.lly_hb_detail);
			holder.tvw_bid_req = (TextView)convertView.findViewById(R.id.tvw_bid_req);
			holder.tvw_red_money = (TextView)convertView.findViewById(R.id.tvw_red_money);
			holder.tvw_source = (TextView)convertView.findViewById(R.id.tvw_source);
			holder.tvw_time = (TextView)convertView.findViewById(R.id.tvw_time);
			convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder)convertView.getTag();
		}
		
		RewardInfo redInfo = mList.get(position);
		double rule = Double.valueOf(redInfo.getInvestUseRule());
		if ((int)rule <= 0)
		{
			holder.tvw_bid_req.setText(context.getString(R.string.use_requirement_no));
		}
		else
		{
			holder.tvw_bid_req.setText(context.getString(R.string.use_requirement, (int)rule));
		}
		double amount = Double.valueOf(AmountUtil.getString(redInfo.getAmount()));
		holder.tvw_red_money.setText((int)amount + "");
		holder.tvw_source.setText(getItem(position).getType());
		holder.tvw_time.setText(context.getString(R.string.overdue_time, redInfo.getTimeOut(), TimeUtils.DATE_FORMAT_DATE));
		
		String status = redInfo.getStatus();// 使用状态： WSY-未使用，YSY-已使用，YGQ-已过期
		if (RedEnvelopeActivity.REDENVELOPE_TYPE_USE_NO.equals(status))
		{
			holder.ivw_status.setBackgroundResource(R.drawable.icon_wsy);
			holder.lly_hb_detail.setVisibility(View.VISIBLE);
		}
		else
		{
			holder.lly_hb_detail.setVisibility(View.GONE);
			if (RedEnvelopeActivity.REDENVELOPE_TYPE_OVERDUE.equals(status))
			{
				holder.ivw_status.setBackgroundResource(R.drawable.icon_ygq);
			}
			else
			{
				holder.ivw_status.setBackgroundResource(R.drawable.icon_ysy);
			}
		}
		
		return convertView;
	}
	
	class ViewHolder
	{
		/**
		 * 红包金额
		 */
		TextView tvw_red_money;
		
		/**
		 * 使用条件
		 */
		TextView tvw_bid_req;
		
		/**
		 * 来源
		 */
		TextView tvw_source;
		
		/**
		 * 详情按钮
		 */
		LinearLayout lly_hb_detail;
		
		/**
		 * 过期时间
		 */
		TextView tvw_time;
		
		/**
		 * 状态
		 */
		ImageView ivw_status;
	}
}
