package com.hxjr.p2p.ad5.ui.investment.adapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dm.widgets.RoundProgressBar;
import com.hxjr.p2p.ad5.DMApplication;
import com.hxjr.p2p.ad5.R;
import com.hxjr.p2p.ad5.bean.BidBean;
import com.hxjr.p2p.ad5.bean.MfenleiBean;
import com.hxjr.p2p.ad5.utils.FormatUtil;
import com.hxjr.p2p.ad5.utils.UIHelper;

public class BidListExpandAdapter extends BaseExpandableListAdapter {

	private List<List<BidBean>> childList;

	private List<MfenleiBean> groupList;

	private Context mContext;

	public BidListExpandAdapter(Context context,

	List<List<BidBean>> childList,

	List<MfenleiBean> groupList) {
		mContext = context;
		// mList = new ArrayList<BidBean>(10);
		this.groupList = groupList;

		this.childList = childList;

	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return childList.get(groupPosition).get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
		View convertView, ViewGroup parent) {
		ViewHolder holder;
			if (convertView == null) {
				convertView = LayoutInflater.from(mContext).inflate(R.layout.bid_list_item, parent,
					false);
				holder = new ViewHolder();
				holder.bidTypeImg = (ImageView) convertView.findViewById(R.id.bidTypeImg);
				holder.bidTitle = (TextView) convertView.findViewById(R.id.bidTitle);
				holder.status = (TextView) convertView.findViewById(R.id.status);
				holder.yearRate = (TextView) convertView.findViewById(R.id.yearRate);
				holder.giftRate = (TextView) convertView.findViewById(R.id.giftRate);
				holder.cycle = (TextView) convertView.findViewById(R.id.cycle);
				holder.progress = (RoundProgressBar) convertView.findViewById(R.id.progress);

				holder.amount = (TextView) convertView.findViewById(R.id.amount);
				holder.bidFlag2 = (ImageView) convertView.findViewById(R.id.bidFlag2);
				holder.bid_dfb_text = (TextView) convertView.findViewById(R.id.bid_dfb_text);
				holder.bid_dfb_line = convertView.findViewById(R.id.bid_dfb_line);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			BidBean bean = childList.get(groupPosition).get(childPosition);
			holder.bidTitle.setText(bean.getBidTitle());
			holder.yearRate.setText(bean.getRate());
			String jlRate = bean.getJlRate();
			holder.giftRate.setText(jlRate);
			if (bean.getIsDay().equals("F")) {
				// 按月分期
				UIHelper.formatDateTextSize(holder.cycle, bean.getCycle() + "个月");
			} else if (bean.getIsDay().equals("S")) {
				// 按天分期
				UIHelper.formatDateTextSize(holder.cycle, bean.getCycle() + "天");
			}
			if (bean.getStatus().equals("HKZ")) {
				// 还款中特殊处理
				Double a = Double.valueOf(bean.getAmount());
				Double b = Double.valueOf(bean.getRemainAmount());
				Double c = a - b;
				bean.setAmount(c + "");
				bean.setRemainAmount("0");
			}
			UIHelper.formatMoneyTextSize(holder.amount,
				FormatUtil.formatMoney(Double.valueOf(bean.getAmount())), 0.5f);
			if (bean.getStatus().equals("DFB") || bean.getStatus().equals("YFB")) {
				// 待发布或预发布
				holder.progress.setVisibility(View.VISIBLE);
				holder.progress.setYFB(true);
				holder.progress.setProgress(0);
				holder.bid_dfb_text.setVisibility(View.VISIBLE);
				holder.bid_dfb_line.setVisibility(View.VISIBLE);
				holder.bid_dfb_text.setText(bean.getPublishDate());
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
				long time = 0L;
				try {
					Date date = sdf.parse(bean.getPublishDate());
					time = date.getTime() - System.currentTimeMillis()
						+ DMApplication.getInstance().diffTime;
				} catch (ParseException e) {
					e.printStackTrace();
				}
				if (holder.myCountDownTimer != null) {
					holder.myCountDownTimer.cancel();
				}
				holder.myCountDownTimer = new MyCountDownTimer(time, 1000L);
				holder.myCountDownTimer.textView = holder.bid_dfb_text;
				holder.myCountDownTimer.line = holder.bid_dfb_line;
				holder.myCountDownTimer.pos = childPosition;
				holder.myCountDownTimer.start();
			} else if ("HKZ".equals(bean.getStatus()) || "YJQ".equals(bean.getStatus())) {
				// 待付款，还款中，已结清
				holder.progress.setVisibility(View.VISIBLE);
				holder.progress.setProgress(100);
				holder.progress.setYFB(false);
				holder.bid_dfb_text.setVisibility(View.GONE);
				holder.bid_dfb_line.setVisibility(View.GONE);
				holder.bid_dfb_text.setTag(null);
			} else if ("DFK".equals(bean.getStatus())) {
				// 待放款改为进度百分比
				holder.progress.setVisibility(View.VISIBLE);
				holder.progress.setYFB(true);
				holder.progress.setProgress(FormatUtil.getBidProgress(bean.getAmount(),
					bean.getRemainAmount()));
				holder.bid_dfb_text.setVisibility(View.GONE);
				holder.bid_dfb_line.setVisibility(View.GONE);
				holder.bid_dfb_text.setTag(null);
			} else {
				// 其它
				holder.progress.setYFB(false);
				holder.progress.setVisibility(View.VISIBLE);
				holder.progress.setProgress(FormatUtil.getBidProgress(bean.getAmount(),
					bean.getRemainAmount()));
				holder.bid_dfb_text.setVisibility(View.GONE);
				holder.bid_dfb_line.setVisibility(View.GONE);
				holder.bid_dfb_text.setTag(null);
			}

			String flag = bean.getFlag();
			holder.bidTypeImg
				.setImageResource(UIHelper.bidTyeImgs.containsKey(flag) ? UIHelper.bidTyeImgs
					.get(flag) : -1);
			holder.status.setText(FormatUtil.convertBidStatus(bean.getStatus()));
			if (bean.getIsXsb()) {
				holder.bidFlag2.setImageResource(R.drawable.pic_xs);
				holder.bidFlag2.setVisibility(View.VISIBLE);
			} else if (bean.getIsJlb()) {
				holder.bidFlag2.setImageResource(R.drawable.pic_jl);
				holder.bidFlag2.setVisibility(View.VISIBLE);
			} else {
				holder.bidFlag2.setVisibility(View.INVISIBLE);
			}
		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		if (childList == null || childList.size() == 0) {
			return 0;
		} else {
			return childList.get(groupPosition).size();
		}
	}

	@Override
	public Object getGroup(int groupPosition) {
		return groupList.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return groupList.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView,
		ViewGroup parent) {
		GroupHolder gh = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.fl_list_item, null);
			gh = new GroupHolder();
			gh.tvTitle=(TextView) convertView.findViewById(R.id.flbidTitle);
			gh.tvRate=(TextView) convertView.findViewById(R.id.flyearRate);
			gh.tvCycle=(TextView) convertView.findViewById(R.id.flcycle);
			gh.tvIco=(TextView) convertView.findViewById(R.id.flbidTypeImg);
			gh.arrows=(ImageView) convertView.findViewById(R.id.progress);
			convertView.setTag(gh);
		} else {
			gh = (GroupHolder) convertView.getTag();
		}
		   gh.tvTitle.setText(groupList.get(groupPosition).getName());
		   gh.tvIco.setText(groupList.get(groupPosition).getIcon());
			gh.tvRate.setText(groupList.get(groupPosition).getMinInterestRates()+"-"+groupList.get(groupPosition).getMaxInterestRates());
			gh.tvCycle.setText(groupList.get(groupPosition).getMinLoanLife()+"-"+groupList.get(groupPosition).getMaxLoanLife());
		if (isExpanded) {
			gh.arrows.setBackgroundResource(R.drawable.icon_flna);
		}else{
			gh.arrows.setBackgroundResource(R.drawable.icon_fln);
		}
		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public boolean isChildSelectable(int arg0, int arg1) {
		return true;
	}

	class GroupHolder {
		TextView bidcheckxinTitle;
		TextView bidcheckxinImg;
		ImageView arrows;
		
		TextView tvIco;
		TextView tvTitle;
		TextView tvRate;
		TextView tvCycle;
	}

	private class ViewHolder {
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

	private class MyCountDownTimer extends CountDownTimer {
		public TextView textView;

		public View line;

		public int pos = -1;

		public MyCountDownTimer(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		@Override
		public void onTick(long millisUntilFinished) {
			if (this.textView != null) {
				textView.setText(surplusLongToString(millisUntilFinished));
			}
		}

		@Override
		public void onFinish() {
			if (this.textView != null) {
				textView.setVisibility(View.GONE);
			}
			if (this.line != null) {
				line.setVisibility(View.GONE);
			}
			if (this.pos != -1) {
				// mList.get(pos).setStatus("TBZ");
			}

			BidListExpandAdapter.this.notifyDataSetChanged();
			this.cancel();
		}

	}

	private static final long DAY = 24 * 3600L;

	private static final long HOUR = 24L;

	private static final long MINUTE = 60L;

	private static final long SECOND = 60L;

	public String surplusLongToString(long between) {
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
