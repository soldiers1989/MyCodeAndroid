package com.hxjr.p2p.ad5.ui.mine.loan;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hxjr.p2p.ad5.DMApplication;
import com.hxjr.p2p.ad5.bean.ForwardRepayInfo;
import com.hxjr.p2p.ad5.bean.RepayInfo;
import com.hxjr.p2p.ad5.bean.UserLoanBid;
import com.hxjr.p2p.ad5.ui.mine.contract.CheckContractActivity;
import com.hxjr.p2p.ad5.ui.tg.TgThirdWebActivity;
import com.hxjr.p2p.ad5.utils.DMConstant;
import com.hxjr.p2p.ad5.utils.FormatUtil;
import com.hxjr.p2p.ad5.utils.HttpParams;
import com.hxjr.p2p.ad5.utils.UIHelper;
import com.hxjr.p2p.ad5.utils.UIHelper.OnOkClickListener;
import com.hxjr.p2p.ad5.R;
import com.dm.http.DMException;
import com.dm.http.HttpCallBack;
import com.dm.http.HttpUtil;
import com.dm.utils.DMJsonObject;
import com.dm.utils.DMLog;
import com.dm.utils.StringUtils;
import com.dm.widgets.utils.AlertDialogUtil;
import com.dm.widgets.utils.AlertDialogUtil.AlertListener;
import com.dm.widgets.utils.ToastUtil;

/**
 * 我的借款-还款适配列表
 *
 * @author lihao
 * @date 2015-6-5
 */
public class MyLoanRepaymentAdapter extends BaseAdapter implements OnClickListener {
    private LayoutInflater inflater;

    private Context context;

    private List<UserLoanBid> userLoanBids;

    private RepaymentSuccessListener repaymentSuccessListener;

    public MyLoanRepaymentAdapter(Context context, List<UserLoanBid> userLoanBids,
                                  RepaymentSuccessListener repaymentSuccessListener) {
        this.context = context;
        this.userLoanBids = new ArrayList<UserLoanBid>(5);
        this.userLoanBids.addAll(userLoanBids);
        inflater = LayoutInflater.from(context);
        this.repaymentSuccessListener = repaymentSuccessListener;
    }

    public void setDatas(List<UserLoanBid> userLoanBids) {
        this.userLoanBids.clear();
        this.userLoanBids.addAll(userLoanBids);
    }

    @Override
    public int getCount() {
        return userLoanBids.size();
    }

    @Override
    public UserLoanBid getItem(int position) {
        return userLoanBids.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private ViewHolder holder = null;

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (null == convertView) {
            convertView = inflater.inflate(R.layout.my_loan_list_item, parent, false);
            holder = new ViewHolder();
            holder.title_tv = (TextView) convertView.findViewById(R.id.bidTitle);
            holder.loan_status_tv = (TextView) convertView.findViewById(R.id.loan_status_tv);
            holder.yearRate_tv = (TextView) convertView.findViewById(R.id.yearRate_tv);
            holder.loaned_money_tv = (TextView) convertView.findViewById(R.id.loaned_money_tv);
            holder.repayment_amount_tv = (TextView) convertView.findViewById(R.id.repayment_amount_tv);
            holder.periods_tv = (TextView) convertView.findViewById(R.id.periods_tv);
            holder.next_repayment_date_tv = (TextView) convertView.findViewById(R.id.next_repayment_date_tv);
            holder.advance_repayment_tv = (TextView) convertView.findViewById(R.id.advance_repayment_tv);
            holder.repayment_tv = (TextView) convertView.findViewById(R.id.repayment_tv);
            holder.check_agreement_tv = (TextView) convertView.findViewById(R.id.check_agreement_tv);
            holder.repayment_detail_tv = (TextView) convertView.findViewById(R.id.repayment_detail_tv);

            convertView.setTag(holder);

            holder.check_agreement_tv.setOnClickListener(this);
            holder.advance_repayment_tv.setOnClickListener(this);
            holder.repayment_detail_tv.setOnClickListener(this);
            holder.repayment_tv.setOnClickListener(this);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.check_agreement_tv.setTag(position);
        holder.advance_repayment_tv.setTag(position);
        holder.repayment_detail_tv.setTag(position);
        holder.repayment_tv.setTag(position);
        setDataToView(position);
        return convertView;
    }

    private void setDataToView(int position) {
        UserLoanBid userLoanBid = userLoanBids.get(position);
        holder.title_tv.setText(userLoanBid.getBidTitle());
        holder.loan_status_tv.setText(userLoanBid.getStatus());

        double yearRate = Double.parseDouble(StringUtils.isEmptyOrNull(userLoanBid.getRate()) ? "0.00" : userLoanBid
                .getRate());
        yearRate = yearRate * 100;
        holder.yearRate_tv.setText(doubleToStr(yearRate + ""));

        double totalAmount =
                Double.parseDouble(StringUtils.isEmptyOrNull(userLoanBid.getTotalAmount()) ? "0.00" : userLoanBid
						.getTotalAmount());
        SpannableString termsSB = null;
        int index = 0;
        String tempAmount = "";
        if (totalAmount >= 10000) {
            totalAmount = totalAmount / 10000;
            tempAmount = FormatUtil.formatStr2(totalAmount + "") + "万元";
            index = 2;
        } else {
            tempAmount = FormatUtil.formatStr2(totalAmount + "") + "元";
            index = 1;
        }
        termsSB = new SpannableString(tempAmount);
        termsSB.setSpan(new RelativeSizeSpan(0.5f),
                tempAmount.length() - index,
                tempAmount.length(),
                SpannableString.SPAN_INCLUSIVE_EXCLUSIVE);
        holder.loaned_money_tv.setText(termsSB);

        double currentBackAmount =
                Double.parseDouble(StringUtils.isEmptyOrNull(userLoanBid.getCurBackAmount()) ? "0.00"
                        : userLoanBid.getCurBackAmount());
        if (currentBackAmount >= 10000) {
            currentBackAmount = currentBackAmount / 10000;
            tempAmount = FormatUtil.formatStr2(currentBackAmount + "") + "万元";
            index = 2;
        } else {
            tempAmount = FormatUtil.formatStr2(currentBackAmount + "") + "元";
            index = 1;
        }
        termsSB = new SpannableString(tempAmount);
        termsSB.setSpan(new RelativeSizeSpan(0.5f),
                tempAmount.length() - index,
                tempAmount.length(),
                SpannableString.SPAN_INCLUSIVE_EXCLUSIVE);
        holder.repayment_amount_tv.setText(termsSB);

        int backTerm = userLoanBid.getBackTerm();
        int totalTerm = userLoanBid.getTotalTerm();
        holder.periods_tv.setText("期数：" + backTerm + "/" + totalTerm);
        holder.next_repayment_date_tv.setText("下个还款日期：" + userLoanBid.getBackTime());
    }

    private String doubleToStr(String sourceDoubleStr) {
        return FormatUtil.formatStr2(Double.parseDouble(StringUtils.isEmptyOrNull(sourceDoubleStr) ? "0.00" :
				sourceDoubleStr)
                + "");
    }

    private class ViewHolder {
        private TextView title_tv;//借款标题

        private TextView loan_status_tv;//借款的还款状态

        private TextView yearRate_tv; //年化利率

        private TextView loaned_money_tv; //借款金额

        private TextView repayment_amount_tv; //已还款金额

        private TextView periods_tv; //还款期限

        private TextView next_repayment_date_tv; //下一个还款日期

        private TextView advance_repayment_tv; //提前还款

        private TextView repayment_tv; //还款

        private TextView check_agreement_tv; //查看合同

        private TextView repayment_detail_tv; //还款详情
    }

    @Override
    public void onClick(View v) {
        UserLoanBid userLoanBid = userLoanBids.get((Integer) v.getTag());
        switch (v.getId()) {
            case R.id.advance_repayment_tv:
                if (!checkLoanBidInfo(userLoanBid)) {
                    clickOnAdvanceRepayment(userLoanBid, (Integer) v.getTag());
                }
                break;
            case R.id.repayment_tv:
                clickOnRepayment(userLoanBid, (Integer) v.getTag());
                break;
            case R.id.check_agreement_tv:
                context.startActivity(new Intent(context, CheckContractActivity.class).putExtra("id", userLoanBid
						.getBidId() + "")
                        .putExtra("isGyb", false));
                break;
            case R.id.repayment_detail_tv:
                context.startActivity(new Intent(context, RepaymentDetailActivity.class).putExtra("bidId",
                        userLoanBid.getBidId() + "").putExtra("type", "WDJKHKZ"));
                break;
        }
    }

    /**
     * 检查是否逾期
     *
     * @param userLoanBid
     * @return
     */
    private boolean checkLoanBidInfo(UserLoanBid userLoanBid) {
        RepayInfo repayInfo = userLoanBid.getRepayInfo();
        double arrMoney = repayInfo.getLoanArrMoney();
        if (arrMoney > 0) {
            AlertDialogUtil.alert(context, "你有逾期未还，无法提前还款！");
            return true;
        }
        if (userLoanBid.getStatus().equals("已垫付")) {
            AlertDialogUtil.alert(context, "已垫付的标不可以提前还款！");
            return true;
        }
        return false;
    }

    /***
     * 提前还款
     * @param v
     */
    private void clickOnAdvanceRepayment(final UserLoanBid userLoanBid, final int position) {
        List<String> textListDatas = new ArrayList<String>(3);
        final ForwardRepayInfo forwardRepayInfo = userLoanBid.getForwardRepayInfo();
        if (null != forwardRepayInfo) {
            textListDatas.add("还款总额：" + FormatUtil.formatStr2(forwardRepayInfo.getLoanTotalMoney() + ""));
            textListDatas.add("当期应还本息：" + FormatUtil.formatStr2(forwardRepayInfo.getLoanMustMoney() + ""));
            textListDatas.add("剩余本金：" + FormatUtil.formatStr2(forwardRepayInfo.getSybj() + ""));
            textListDatas.add("违约金：" + FormatUtil.formatStr2(forwardRepayInfo.getLoanPenalAmount() + ""));
            textListDatas.add("提前还款手续费：" + FormatUtil.formatStr2(forwardRepayInfo.getLoanManageAmount() + ""));
            UIHelper.showTextListDialog(context, null, null, textListDatas, new OnOkClickListener() {
                @Override
                public void onOkClick() {
                        HttpParams params = new HttpParams();
                        params.put("loanId", userLoanBid.getBidId());
                        params.put("currentTerm", forwardRepayInfo.getNumber());
                        requestData(DMConstant.API_Url.USER_PRE_REPAYMENT, params, 2, position);
                }

                @Override
                public void onCanceClick() {
                }
            });
        }
    }

    /***
     * 还款
     * @param v
     */
    private void clickOnRepayment(final UserLoanBid userLoanBid, final int position) {
        List<String> textListDatas = new ArrayList<String>(3);
        textListDatas.add("当期应还本息：" + FormatUtil.formatStr2(userLoanBid.getRepayInfo().getLoanMustMoney() + ""));
        textListDatas.add("逾期金额：" + FormatUtil.formatStr2(userLoanBid.getRepayInfo().getLoanArrMoney() + ""));
        UIHelper.showTextListDialog(context, null, null, textListDatas, new OnOkClickListener() {
            @Override
            public void onOkClick() {
                    HttpParams params = new HttpParams();
                    params.put("loanId", userLoanBid.getBidId());
                    params.put("currentTerm", userLoanBid.getRepayInfo().getNumber());
                    requestData(DMConstant.API_Url.USER_PAYMENT, params, 1, position);
            }

            @Override
            public void onCanceClick() {
            }
        });
    }

    /***
     * @param url
     * @param params
     * @param postType  1：还款；2：提前还款
     */
    private void requestData(String url, HttpParams params, final int postType, final int position) {
        HttpUtil.getInstance().post(context, url, params, new HttpCallBack() {
            @Override
            public void onSuccess(JSONObject result) {
                try {
                    DMJsonObject jsonObject = new DMJsonObject(result.toString());
                    String code = jsonObject.getString("code");
                    if (null != code && code.equals("000000")) {
                        if (DMApplication.getInstance().getUserInfo().isTg()) {
                            String url = result.getJSONObject("data").getString("url").toString();
                            Intent intent = new Intent(context, TgThirdWebActivity.class);
                            intent.putExtra("url", url);
                            intent.putExtra("message", postType == 1 ? "还款成功,系统确认中！" : "提前还款成功,系统确认中！");
                            intent.putExtra("title", postType == 1 ? DMConstant.TgWebTitle.HUAN_KUAN
                                    : DMConstant.TgWebTitle.TIQIAN_HUANKUAN);
                            context.startActivity(intent);
                        } else {
                            AlertDialogUtil.alert(context, "恭喜，还款成功！", new AlertListener() {
                                @Override
                                public void doConfirm() {
                                    repaymentSuccessListener.repaymentSuccess(postType, position);
                                }
                            });
                        }
                    } else {
                        ToastUtil.getInstant().show(context, jsonObject.getString("description"));
                    }
                } catch (JSONException e) {
                    DMLog.e(MyLoanRepaymentAdapter.class.getSimpleName(), e.getMessage());
                    ToastUtil.getInstant().show(context, "数据错误");
                }
            }

            @Override
            public void onFailure(Throwable t, Context context) {
                super.onFailure(t, context);
            }

            @Override
            public void onConnectFailure(DMException dmE, Context context) {
                super.onConnectFailure(dmE, context);
                ToastUtil.getInstant().show(context, context.getString(R.string.net_connection_error));
            }
        });
    }

    interface RepaymentSuccessListener {
        public void repaymentSuccess(int type, int position);
    }
}
