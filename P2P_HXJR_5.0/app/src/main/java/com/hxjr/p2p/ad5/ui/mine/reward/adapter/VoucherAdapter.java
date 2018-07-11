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
import android.widget.TextView;

import com.hxjr.p2p.ad5.bean.RewardInfo;
import com.hxjr.p2p.ad5.ui.mine.reward.VoucherActivity;
import com.hxjr.p2p.ad5.utils.DMConstant;
import com.hxjr.p2p.ad5.utils.FormatUtil;
import com.hxjr.p2p.ad5.utils.TimeUtils;
import com.hxjr.p2p.ad5.R;

/**
 * @author  huangkaibo
 * @date 2015-11-5
 */
public class VoucherAdapter extends BaseAdapter
{
	private Context context;
	
	private List<RewardInfo> mList;
	
	private ViewHolder holder;
	
	public VoucherAdapter(Context context)
	{
		this.context = context;
		mList = new ArrayList<RewardInfo>(DMConstant.DigitalConstant.TEN);
	}
	
	public void updateList(List<RewardInfo> letters)
	{
		if (null != mList && null != letters)
		{
			mList.clear();
			mList.addAll(letters);
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
			convertView = LayoutInflater.from(context).inflate(R.layout.voucher_list_item, parent, false);
			holder = new ViewHolder();
			holder.ivw_status = (ImageView)convertView.findViewById(R.id.ivw_status);
			holder.tvw_form = (TextView)convertView.findViewById(R.id.tvw_form);
			holder.tvw_requirement = (TextView)convertView.findViewById(R.id.tvw_requirement);
			holder.tvw_time = (TextView)convertView.findViewById(R.id.tvw_time);
			holder.tvw_voucher_rate = (TextView)convertView.findViewById(R.id.tvw_voucher_rate);
			holder.tvw_detail = (TextView)convertView.findViewById(R.id.tvw_detail);
			holder.ivw_detail = (ImageView)convertView.findViewById(R.id.ivw_detail);
			convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder)convertView.getTag();
		}
		
		RewardInfo voucher = mList.get(position);
		String amount = FormatUtil.formatStr2(Double.valueOf(voucher.getAmount()) + "");
		holder.tvw_voucher_rate.setText(amount + "%");
		double rule = Double.valueOf(voucher.getInvestUseRule());
		if ((int)rule <= 0)
		{
			holder.tvw_requirement.setText(context.getString(R.string.use_requirement_no));
		}
		else
		{
			holder.tvw_requirement.setText(context.getString(R.string.use_requirement, (int)rule));
		}
		holder.tvw_form.setText(voucher.getType());
		if (VoucherActivity.VOUCHER_TYPE_USE_NO.equals(voucher.getStatus()))
		{
			holder.ivw_status.setBackgroundResource(R.drawable.icon_wsy);
			holder.tvw_time.setVisibility(View.VISIBLE);
			holder.tvw_time.setText(context.getString(R.string.overdue_time, voucher.getTimeOut(), TimeUtils.DATE_FORMAT_DATE));
			holder.ivw_detail.setVisibility(View.VISIBLE);
			holder.tvw_detail.setVisibility(View.VISIBLE);
		}
		else
		{
			holder.ivw_detail.setVisibility(View.GONE);
			holder.tvw_detail.setVisibility(View.GONE);
			holder.tvw_time.setVisibility(View.VISIBLE);
			holder.tvw_time.setText(context.getString(R.string.overdue_time, voucher.getTimeOut(), TimeUtils.DATE_FORMAT_DATE));
			if (VoucherActivity.VOUCHER_TYPE_SETTLE.equals(voucher.getStatus()))
			{
				holder.ivw_status.setBackgroundResource(R.drawable.icon_yjq);
			}
			else if (VoucherActivity.VOUCHER_TYPE_OVERDUE.equals(voucher.getStatus()))
			{
				holder.ivw_status.setBackgroundResource(R.drawable.icon_ygq);
			}
			else if (VoucherActivity.VOUCHER_TYPE_USED.equals(voucher.getStatus()))
			{
				holder.ivw_status.setBackgroundResource(R.drawable.icon_ysy);
			}
		}
		
		return convertView;
	}
	
	class ViewHolder
	{
		/**
		 * 利率
		 */
		private TextView tvw_voucher_rate;
		
		/**
		 * 使用条件
		 */
		private TextView tvw_requirement;
		
		/**
		 * 来源
		 */
		private TextView tvw_form;
		
		/**
		 * 状态
		 */
		private ImageView ivw_status;
		
		/**
		 * 过期时间 
		 */
		private TextView tvw_time;
		
		/**
		 * 详情
		 */
		private TextView tvw_detail;
		
		private ImageView ivw_detail;
	}
}
