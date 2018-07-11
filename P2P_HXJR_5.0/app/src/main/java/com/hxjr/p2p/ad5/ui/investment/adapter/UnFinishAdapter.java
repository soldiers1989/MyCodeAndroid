package com.hxjr.p2p.ad5.ui.investment.adapter;

import android.content.Context;
import android.os.CountDownTimer;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dm.http.HttpCallBack;
import com.dm.http.HttpUtil;
import com.dm.utils.DMJsonObject;
import com.dm.utils.DMLog;
import com.hxjr.p2p.ad5.DMApplication;
import com.hxjr.p2p.ad5.R;
import com.hxjr.p2p.ad5.bean.BidBean;
import com.hxjr.p2p.ad5.bean.BidDetailBase;
import com.hxjr.p2p.ad5.utils.DMConstant;
import com.hxjr.p2p.ad5.utils.ErrorUtil;
import com.hxjr.p2p.ad5.utils.FormatUtil;
import com.hxjr.p2p.ad5.utils.HttpParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.hxjr.p2p.ad5.R.id.bid_dfb_text;


public class UnFinishAdapter extends BaseAdapter {

    private List<BidBean> bidBeens;
    private Context context;
//    private MyCountDownTimer myCountDownTimer;

    public UnFinishAdapter(Context context, List<BidBean> bidBeens) {
        this.context = context;
        this.bidBeens = bidBeens;
    }

    @Override
    public int getCount() {
        if (bidBeens.size() == 0) {

            return 0;
        } else {
            return bidBeens.size();
        }
    }

    @Override
    public Object getItem(int position) {

        return bidBeens.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView,
                        ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.unfinsh_list_item, parent, false);
            holder = new ViewHolder();
            holder.bidFlag = (TextView) convertView.findViewById(R.id.bidFlag);
            holder.bidTitle = (TextView) convertView.findViewById(R.id.bidTitle);
            holder.yearRate = (TextView) convertView.findViewById(R.id.yearRate);
            holder.tvCycle = (TextView) convertView.findViewById(R.id.tvCycle);
            holder.progress_tv = (TextView) convertView.findViewById(R.id.progress_tv);
            holder.paymentType = (TextView) convertView.findViewById(R.id.paymentType);
            holder.amount = (TextView) convertView.findViewById(R.id.amount);
            holder.guarantee = (TextView) convertView.findViewById(R.id.guarantee);
            holder.progressBar = (ProgressBar) convertView.findViewById(R.id.progressBar);
            holder.bid_dfb_text=(TextView) convertView.findViewById(bid_dfb_text);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.bidFlag.setText(bidBeens.get(position).getFlag());
        holder.bidTitle.setText(bidBeens.get(position).getBidTitle());
        String yearRateS = bidBeens.get(position).getRate();
        SpannableString yearRateSB = new SpannableString(yearRateS);
        yearRateSB.setSpan(new RelativeSizeSpan(0.5f),
                yearRateS.length() - 1,
                yearRateS.length(),
                SpannableString.SPAN_INCLUSIVE_EXCLUSIVE);
        holder.yearRate.setText(bidBeens.get(position).getRate());
        holder.tvCycle.setText(bidBeens.get(position).getCycle() + "");
        holder.paymentType.setText(FormatUtil.formatPaymentType(bidBeens.get(position).getPaymentType()));
        //借款金额
//        String lendAmount = "";
//        SpannableString lendAmountSB = null;
        //默认显示借款金额
//        lendAmount = ;
//        if (lendAmount.endsWith("万元") || lendAmount.endsWith("百万")) {
//            lendAmountSB = new SpannableString(lendAmount);
//            lendAmountSB.setSpan(new RelativeSizeSpan(0.5f),
//                    lendAmount.length() - 2,
//                    lendAmount.length(),
//                    SpannableString.SPAN_INCLUSIVE_EXCLUSIVE);
//        }
//        else if (lendAmount.endsWith("元") || lendAmount.endsWith("亿")) {
//            lendAmountSB = new SpannableString(lendAmount);
//            lendAmountSB.setSpan(new RelativeSizeSpan(0.5f),
//                    lendAmount.length() - 1,
//                    lendAmount.length(),
//                    SpannableString.SPAN_INCLUSIVE_EXCLUSIVE);
//        }
        if (bidBeens.get(position).getStatus().equals("DFB")
                || bidBeens.get(position).getStatus().equals("YFB")) {
            // 待发布或预发布
            holder.bid_dfb_text.setVisibility(View.VISIBLE);
            holder.bid_dfb_text.setText(bidBeens.get(position).getPublishDate());
            SimpleDateFormat sdf = new SimpleDateFormat(
                    "yyyy-MM-dd HH:mm:ss", Locale.CHINA);
            long time = 0L;
            try {
                Date date = sdf.parse(bidBeens.get(position).getPublishDate());
                time = date.getTime() - System.currentTimeMillis()
                        + DMApplication.getInstance().diffTime;
            } catch (ParseException e) {
                e.printStackTrace();
            }
            MyCountDownTimer myCountDownTimer=null;
            if (myCountDownTimer != null) {
                myCountDownTimer.cancel();
            }
            myCountDownTimer = new MyCountDownTimer(time, 1000L);
            myCountDownTimer.textView = holder.bid_dfb_text;
            myCountDownTimer.pos = 0;
            myCountDownTimer.start();
        }
        holder.amount.setText(FormatUtil.formatMoney2(Double.valueOf(bidBeens.get(position).getAmount())));
        //        holder.guarantee.setText(bidBeens.get(position).getGuarantee());
        getBaseBidInfo(bidBeens.get(position).getId() + "", holder.guarantee);
        holder.progress_tv.setText(FormatUtil.getBidProgress(
                bidBeens.get(position).getAmount(), bidBeens.get(position).getRemainAmount()) + "%");
        holder.progressBar.setProgress((int) FormatUtil.getBidProgress(
                bidBeens.get(position).getAmount(), bidBeens.get(position).getRemainAmount()));
        return convertView;
    }

    class ViewHolder {
        TextView bidFlag;
        TextView bidTitle;
        TextView yearRate;
        TextView tvCycle;
        TextView progress_tv;
        TextView paymentType;
        TextView amount;
        TextView guarantee;
        ProgressBar progressBar;
        TextView bid_dfb_text;
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
//                go_buy_btn.setText("马上抢购");
            }
            if (this.line != null) {
                line.setVisibility(View.GONE);
            }
            if (this.pos != -1) {
                // mList.get(pos).setStatus("TBZ");
            }

            // BidListAdapter.this.notifyDataSetChanged();
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
        return String.format("倒计时  %02d", hour + day * 24) + String.format(":%02d", minute) +
                String.format
                        (":%02d", second);
        //        return "倒计时：" + day + "天" + hour + "小时" + minute + "分" + second + "秒";
        //        Date date1 = new Date(between);
        //        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //        String str1 = sdf1.format(date1);
        //        return str1;
    }

    /**
     * 获取标的基本信息
     */
    private void getBaseBidInfo(String bidId, final TextView textView) {
        HttpParams params = new HttpParams();
        params.put("bidId", bidId);
        HttpUtil.getInstance().post(context, DMConstant.API_Url.BID_BID, params, new HttpCallBack() {
            @Override
            public void onSuccess(JSONObject result) {
                try {
                    String code = result.getString("code");
                    if (code.equals(DMConstant.ResultCode.SUCCESS)) {
                        // 成功
                        DMJsonObject data = new DMJsonObject(result.getString("data"));
                        BidDetailBase base = new BidDetailBase(data);
                        DMLog.e(base.getGuarantee());
                        if (base.getGuarantee().equals("")) {
                            textView.setText("华西世纪财富管理有限公司");
                        } else {
                            textView.setText(base.getGuarantee());
                        }
                    } else {
                        ErrorUtil.showError(result);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
