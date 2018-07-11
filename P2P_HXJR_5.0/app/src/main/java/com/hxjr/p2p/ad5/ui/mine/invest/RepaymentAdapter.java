package com.hxjr.p2p.ad5.ui.mine.invest;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dm.http.HttpCallBack;
import com.dm.http.HttpUtil;
import com.dm.widgets.utils.AlertDialogUtil;
import com.dm.widgets.utils.AlertDialogUtil.AlertListener;
import com.dm.widgets.utils.AlertDialogUtil.ConfirmListener;
import com.dm.widgets.utils.ToastUtil;
import com.hxjr.p2p.ad5.R;
import com.hxjr.p2p.ad5.bean.MyInvestRepaymentIn;
import com.hxjr.p2p.ad5.ui.mine.contract.CheckContractActivity;
import com.hxjr.p2p.ad5.ui.mine.setting.FindTradePwdActivity;
import com.hxjr.p2p.ad5.utils.DMConstant;
import com.hxjr.p2p.ad5.utils.ErrorUtil;
import com.hxjr.p2p.ad5.utils.FormatUtil;
import com.hxjr.p2p.ad5.utils.HttpParams;
import com.hxjr.p2p.ad5.utils.UIHelper;
import com.hxjr.p2p.ad5.utils.UIHelper.OnTurnClickListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 回款中-还款适配列表
 * @author  huangkaibo
 * @date 2015-12-07
 */
public class RepaymentAdapter extends BaseAdapter
{
	private LayoutInflater inflater;
	
	private Context context;
	
	private List<MyInvestRepaymentIn> mList;
	
	private MyInvestRepaymentIn repayment;
	
	private boolean isNeedPwd;// 是否需要交易密码
	
	public RepaymentAdapter(Context context, boolean isNeedPwd)
	{
		this.context = context;
		this.isNeedPwd = isNeedPwd;
		inflater = LayoutInflater.from(context);
		mList = new ArrayList<MyInvestRepaymentIn>(DMConstant.DigitalConstant.TEN);
	}
	
	public void addAll(List<MyInvestRepaymentIn> lists)
	{
		mList.addAll(lists);
		notifyDataSetChanged();
	}
	
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
	public MyInvestRepaymentIn getItem(int position)
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
		ViewHolder holder;
		if (null == convertView)
		{
			convertView = inflater.inflate(R.layout.my_invest_repayment_list_item, parent, false);
			holder = new ViewHolder();
			holder.title_tv = (TextView)convertView.findViewById(R.id.bidTitle);
			holder.yearRate_tv = (TextView)convertView.findViewById(R.id.yearRate_tv);
			holder.giftRate_tv = (TextView)convertView.findViewById(R.id.giftRate_tv);
			holder.loaned_money_tv = (TextView)convertView.findViewById(R.id.loaned_money_tv);
			holder.repayment_amount_tv = (TextView)convertView.findViewById(R.id.repayment_amount_tv);
			holder.periods_tv = (TextView)convertView.findViewById(R.id.periods_tv);
			holder.next_repayment_date_tv = (TextView)convertView.findViewById(R.id.next_repayment_date_tv);
			holder.check_agreement_tv = (TextView)convertView.findViewById(R.id.check_agreement_tv);
			holder.turn_out_tv = (TextView)convertView.findViewById(R.id.turn_out_tv);
			convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder)convertView.getTag();
		}
		
		repayment = getItem(position);
		holder.title_tv.setText(repayment.getBidName());
		holder.yearRate_tv.setText(repayment.getNhl());
		if (repayment.getJxl() == 0.00)
		{
			holder.giftRate_tv.setVisibility(View.GONE);
		}
		else
		{
			holder.giftRate_tv.setVisibility(View.VISIBLE);
			holder.giftRate_tv.setText("+" + FormatUtil.get2String(repayment.getJxl()) + "%");
		}
		
		UIHelper.formatMoneyTextSize(holder.loaned_money_tv, FormatUtil.formatMoney(repayment.getGmjg()), 0.5f);
		UIHelper.formatMoneyTextSize(holder.repayment_amount_tv, FormatUtil.formatMoney(repayment.getDsbx()), 0.5f);
		holder.periods_tv.setText("期数：" + repayment.getSyqs() + "/" + repayment.getHkqs());
		holder.next_repayment_date_tv.setText("下个还款日期：" + repayment.getXghkr());
		holder.turn_out_tv.setTag(position);
		holder.check_agreement_tv.setTag(position);
		
		/**
		 * isTransfered   String类型   S：正在转让中  F：未转让
			主要解决已经转让的债权，但是还未经过审批。此时债权的状态为"待还款"，所以需要增加这个判断来隐藏按钮
		 */
//		if (getItem(position).isCanTrans())
//		{
//			/**
//			 * isCanTrans     boolean类型  false: 不可转让  true：可转让
//			债权必须持有一段时间之后才能被用户转让，在这段时间内债权不允许被转让
//			 */
//			holder.turn_out_tv.setVisibility("F".equals(getItem(position).isTransfered()) ? View.VISIBLE : View.INVISIBLE);
//		}
//		else
//		{
//			holder.turn_out_tv.setVisibility(View.INVISIBLE);
//		}
		holder.turn_out_tv.setVisibility(View.VISIBLE);
		holder.turn_out_tv.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				final int position = (Integer)v.getTag();
				final MyInvestRepaymentIn repayment = getItem(position);
				String[] textArr = new String[2];
				textArr[0] = FormatUtil.formatMoney(repayment.getSourceZqPrice());
				textArr[1] = FormatUtil.get2String(repayment.getTransferRate() * 100) + "%";
				UIHelper.showTurnDialog(context, "转让", "取消", textArr, isNeedPwd, new OnTurnClickListener()
				{
					@Override
					public void onOkClick(String salePrice, String tradPwd)
					{
						HttpParams params = new HttpParams();
						params.put("salePrice", salePrice);
						params.put("tranPwd", null != tradPwd ? tradPwd : "");
						params.put("creditorId", repayment.getZqId() + "");
						params.put("bidAmount", repayment.getSourceZqPrice() + "");
						HttpUtil.getInstance().post(context, DMConstant.API_Url.TRANSFER_CREDIT, params, new HttpCallBack()
						{
							@Override
							public void onSuccess(JSONObject result)
							{
								try
								{
									String code = result.getString("code");
									if (DMConstant.ResultCode.SUCCESS.equals(code))
									{
										AlertDialogUtil.alert(context,
											context.getString(R.string.creditor_transfer_success),
											new AlertListener()
											{
												@Override
												public void doConfirm()
												{
													Intent intent =
														new Intent(DMConstant.BroadcastActions.MY_INVESTMENT_REPAYMENT);// 发送广播刷新列表
													context.sendBroadcast(intent);
												}
											}).setCanceledOnTouchOutside(false);
										//	mList.remove(position);
										//	notifyDataSetChanged();
									}
									else if (code.equals(ErrorUtil.ErroreCode.ERROR_000044))
									{
										String description = result.getString("description");
										if (null != description && description.contains("交易密码"))
										{
											showDealPwdError();
										}
										else
										{
											AlertDialogUtil.alert(context, FormatUtil.Html2Text(description));
										}
									}
									else
									{
										ToastUtil.getInstant().show(context, result.getString("description"));
									}
								}
								catch (JSONException e)
								{
									// TODO Auto-generated catch block
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
				});
			}
		});
		
		holder.check_agreement_tv.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				MyInvestRepaymentIn repayment = getItem((Integer)v.getTag());
				Intent intent = new Intent(context, CheckContractActivity.class);
				intent.putExtra("id", repayment.getBidId() + "");
				intent.putExtra("creditId" ,repayment.getZqId()+"");
				context.startActivity(intent);
			}
		});
		return convertView;
	}
	
	private class ViewHolder
	{
		private TextView title_tv;//借款标题
		
		private TextView yearRate_tv; //年化利率
		
		private TextView giftRate_tv; // 加息利率
		
		private TextView loaned_money_tv; //借款金额
		
		private TextView repayment_amount_tv; //已还款金额
		
		private TextView periods_tv; //还款期限
		
		private TextView next_repayment_date_tv; //下一个还款日期
		
		private TextView check_agreement_tv; //查看合同
		
		private TextView turn_out_tv; //转让
	}
	
	/***
	 * 提示交易密码错误
	 */
	protected void showDealPwdError()
	{
		AlertDialogUtil.confirm(context, context.getString(R.string.deal_pwd_err), null, "找回交易密码", new ConfirmListener()
		{
			@Override
			public void onOkClick()
			{
			}
			
			@Override
			public void onCancelClick()
			{
				context.startActivity(new Intent(context, FindTradePwdActivity.class));
			}
		});
	}
}
