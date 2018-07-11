package com.hxjr.p2p.ad5.ui.mine.creditor;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.hxjr.p2p.ad5.bean.MyCreditorAssignment;
import com.hxjr.p2p.ad5.utils.DMConstant;
import com.hxjr.p2p.ad5.utils.FormatUtil;
import com.hxjr.p2p.ad5.utils.HttpParams;
import com.hxjr.p2p.ad5.utils.UIHelper;
import com.hxjr.p2p.ad5.R;
import com.dm.http.HttpCallBack;
import com.dm.http.HttpUtil;
import com.dm.widgets.utils.AlertDialogUtil;
import com.dm.widgets.utils.AlertDialogUtil.ConfirmListener;
import com.dm.widgets.utils.ToastUtil;

import android.content.Context;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * 转让中
 * @author  huangkaibo
 * @date 2015-12-4
 */
public class CreditorTurningAdapter extends BaseAdapter
{
	private LayoutInflater inflater;
	
	private Context context;
	
	private List<MyCreditorAssignment> mList;
	
	public CreditorTurningAdapter(Context context)
	{
		this.context = context;
		inflater = LayoutInflater.from(context);
		mList = new ArrayList<MyCreditorAssignment>(DMConstant.DigitalConstant.TEN);
	}
	
	public void clearList()
	{
		mList.clear();
	}
	
	public void addAll(List<MyCreditorAssignment> list)
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
	public MyCreditorAssignment getItem(int position)
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
			convertView = inflater.inflate(R.layout.my_creditor_turngoing_list_item, parent, false);
			holder = new ViewHolder();
			holder.bidTitle = (TextView)convertView.findViewById(R.id.bidTitle);
			holder.yearRate_tv = (TextView)convertView.findViewById(R.id.yearRate_tv);
			holder.loaned_money_tv = (TextView)convertView.findViewById(R.id.loaned_money_tv);
			holder.turn_amount_tv = (TextView)convertView.findViewById(R.id.turn_amount_tv);
			holder.turn_cancel_tv = (TextView)convertView.findViewById(R.id.turn_cancel_tv);
			holder.turn_cancel_tv.setTag(position);
			convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder)convertView.getTag();
		}
		
		final MyCreditorAssignment bean = getItem(position);
		holder.bidTitle.setText(bean.getCreditorId());
		String rate = bean.getRate();
		SpannableString rateSB = new SpannableString(rate);
		rateSB.setSpan(new RelativeSizeSpan(0.5f), rate.length() - 1, rate.length(), SpannableString.SPAN_INCLUSIVE_EXCLUSIVE);
		holder.yearRate_tv.setText(rateSB);
		UIHelper.formatMoneyTextSize(holder.loaned_money_tv, FormatUtil.formatMoney(bean.getCreditorVal()),0.5f);
		UIHelper.formatMoneyTextSize(holder.turn_amount_tv, FormatUtil.formatMoney(bean.getSalePrice()),0.5f);
		holder.turn_cancel_tv.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				showConfirmDialog(v);
			}
		});
		
		return convertView;
	}
	
	private void showConfirmDialog(View v)
	{
		final int position = (Integer)v.getTag();
		final MyCreditorAssignment bean = getItem(position);
		AlertDialogUtil.confirm(context, context.getString(R.string.cancel_creditor_right), new ConfirmListener()
		{
			@Override
			public void onOkClick()
			{
				HttpParams params = new HttpParams();
				params.put("creditorId", bean.getBidId());
				HttpUtil.getInstance().post(context, DMConstant.API_Url.CANCEL_CREDIT, params, new HttpCallBack()
				{
					@Override
					public void onSuccess(JSONObject result)
					{
						try
						{
							String code = result.getString("code");
							if (DMConstant.ResultCode.SUCCESS.equals(code))
							{
								AlertDialogUtil.alert(context, context.getString(R.string.cancel_success));
								mList.remove(position);
								notifyDataSetChanged();
							}
							else
							{
								ToastUtil.getInstant().show(context, result.getString("description"));
							}
						}
						catch (JSONException e)
						{
							e.printStackTrace();
						}
					}
					
					@Override
					public void onFailure(Throwable t, Context context)
					{
						super.onFailure(t, context);
					}
				});
			}
			
			@Override
			public void onCancelClick()
			{
				// TODO Auto-generated method stub
			}
		});
	}
	
	private class ViewHolder
	{
		TextView bidTitle;// 标题
		
		TextView yearRate_tv;// 年化利率
		
		TextView loaned_money_tv;// 债权价值
		
		TextView turn_amount_tv;// 转让金额
		
		TextView turn_cancel_tv;// 取消转让
	}
}
