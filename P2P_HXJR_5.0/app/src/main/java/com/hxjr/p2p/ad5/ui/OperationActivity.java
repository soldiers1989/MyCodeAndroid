package com.hxjr.p2p.ad5.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.dm.http.DMException;
import com.dm.http.HttpCallBack;
import com.dm.http.HttpUtil;
import com.dm.utils.DMJsonObject;
import com.dm.utils.DMLog;
import com.hxjr.p2p.ad5.R;
import com.hxjr.p2p.ad5.bean.HomeBidCount;
import com.hxjr.p2p.ad5.utils.AmountUtil;
import com.hxjr.p2p.ad5.utils.DMConstant;
import com.hxjr.p2p.ad5.utils.ErrorUtil;
import com.hxjr.p2p.ad5.utils.HttpParams;
import com.hxjr.p2p.ad5.utils.TimeUtils;

import org.json.JSONException;
import org.json.JSONObject;

public class OperationActivity extends BaseActivity {

    private TextView back_text;
    //成交金额
    private TextView ljcj;
    //注册总人数
    private TextView zczrs;
    //平台运营天数
    private TextView total_number;
    //本年成交金额
    private TextView bncj;
    //为用户带来收益
    private TextView total_income;

    private TextView end_time;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operation);
        intView();
        postDate();
    }

    private void intView() {
        end_time = (TextView) findViewById(R.id.end_time);
        back_text = (TextView) findViewById(R.id.back_text);
        end_time.setText("-以上数据截止到"+TimeUtils.getCurrentTimeInString()+"-");
        back_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OperationActivity.this.finish();
            }
        });
        ljcj = (TextView) findViewById(R.id.ljcj);
        zczrs = (TextView) findViewById(R.id.zczrs);
        total_number = (TextView) findViewById(R.id.total_number);
        bncj = (TextView) findViewById(R.id.bncj);
        total_income = (TextView) findViewById(R.id.total_income);
    }

    private void postDate() {
        HttpParams bidCountParams = new HttpParams();// 请求投标统计数据
        String bidCountUrl = DMConstant.API_Url.BID_COUNT;
        postData(bidCountUrl, bidCountParams, 3);
    }

    private void postData(String url, HttpParams params, final int postType) {
        HttpUtil.getInstance().post(this, url, params, new HttpCallBack() {
            @Override
            public void onSuccess(JSONObject result) {
                try {
                    String code = result.getString("code");

                    if (DMConstant.ResultCode.SUCCESS.equals(code)) {
                        switch (postType) {
                            case 3:
                                DMLog.i("POST_BID_COUNT post result", result.toString());
                                doAfterGetBidCount(result);
                                break;

                        }
                    } else {
                        // 失败
                        ErrorUtil.showError(result);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable t, Context context) {
                super.onFailure(t, context);
            }

            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onConnectFailure(DMException dmE, Context context) {
                //                ToastUtil.getInstant().show(this, dmE.getDescription());

            }

        });
    }

    /**
     * 获取投资统计
     *
     * @param result
     */
    protected void doAfterGetBidCount(JSONObject result) {
        try {
            DMJsonObject data = new DMJsonObject(result.getString("data"));
            DMLog.e(data.toString());
            HomeBidCount bs = new HomeBidCount(data);
            ljcj.setText(AmountUtil.DT.format(bs.getTotal() / 10000) + "");
            zczrs.setText(bs.getZczrs() + "");
//            total_number.setText((int) bs.getTotal_number() + "");
            bncj.setText(AmountUtil.DT.format(bs.getBncj() / 10000) + "");
            total_income.setText(AmountUtil.DT.format(bs.getTotal_income() / 10000) + "");
            total_number.setText(TimeUtils.diffsDays());
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
