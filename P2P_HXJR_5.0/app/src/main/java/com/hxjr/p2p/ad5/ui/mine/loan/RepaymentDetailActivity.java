package com.hxjr.p2p.ad5.ui.mine.loan;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.hxjr.p2p.ad5.ui.BaseActivity;
import com.hxjr.p2p.ad5.utils.DMConstant;
import com.hxjr.p2p.ad5.utils.ErrorUtil;
import com.hxjr.p2p.ad5.utils.HttpParams;
import com.hxjr.p2p.ad5.R;
import com.dm.http.HttpCallBack;
import com.dm.http.HttpUtil;
import com.dm.utils.DMJsonObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 我的借款-还款详情类
 * @author  lihao
 * @date 2015-6-8
 */
public class RepaymentDetailActivity extends BaseActivity
{
	private ListView mListView;
	
	private RepaymentDetailAdapter adapter;
	
	private View repaymentTitle;
	
	private String bidId;
	
	private String type;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_repayment_datail);
		initView();
		bidId = getIntent().getStringExtra("bidId");
		type = getIntent().getStringExtra("type");
		getDetailDatas(1);
	}
	
	@SuppressLint("InflateParams")
	@Override
	protected void initView()
	{
		super.initView();
		((TextView)findViewById(R.id.title_text)).setText(R.string.loan_repayment_detail);
		mListView = (ListView)findViewById(R.id.investListView);
		repaymentTitle = this.getLayoutInflater().inflate(R.layout.repayment_datail_item, null);
		((TextView)repaymentTitle.findViewById(R.id.periods_tv)).setText("期数");
		((TextView)repaymentTitle.findViewById(R.id.money_tv)).setText("金额");
		((TextView)repaymentTitle.findViewById(R.id.date_tv)).setText("还款日期");
		((TextView)repaymentTitle.findViewById(R.id.status_tv)).setText("状态");
		mListView.addHeaderView(repaymentTitle);
	}
	
	private List<RepaymentDetail> mRepaymentDetails = new ArrayList<RepaymentDetail>();
	
	private void getDetailDatas(int postPage)
	{
		HttpParams params = new HttpParams();
		params.put("bidId", bidId);
		params.put("type", null == type ? "WDJKHKZ" : type);
		params.put("pageSize", 40);
		params.put("pageIndex", 1);
		HttpUtil.getInstance().post(this, DMConstant.API_Url.USER_REPAYINFO, params, new HttpCallBack()
		{
			@Override
			public void onSuccess(JSONObject result)
			{
				try
				{
					String code = result.getString("code");
					if (DMConstant.ResultCode.SUCCESS.equals(code))
					{ // 成功
						List<RepaymentDetail> datas = parseLoadBidDatas(result.getJSONArray("data"));
						if (null != datas && datas.size() > 0)
						{
							mRepaymentDetails.addAll(datas);
						}
						if (null == adapter)
						{
							adapter = new RepaymentDetailAdapter(RepaymentDetailActivity.this, mRepaymentDetails);
							mListView.setAdapter(adapter);
						}
						else
						{
							adapter.setDatas(mRepaymentDetails);
							adapter.notifyDataSetChanged();
						}
					}
					else
					{
						ErrorUtil.showError(result);
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
			
			@Override
			public void onStart()
			{
				super.onStart();
			}
		});
	}
	
	protected List<RepaymentDetail> parseLoadBidDatas(JSONArray jsonArray) throws JSONException
	{
		List<RepaymentDetail> repaymentDetails = new ArrayList<RepaymentDetail>();
		if (jsonArray.length() > 0)
		{
			for (int i = 0; i < jsonArray.length(); i++)
			{
				DMJsonObject item = new DMJsonObject(jsonArray.getJSONObject(i).toString());
				RepaymentDetail repaymentDetail = new RepaymentDetail();
				repaymentDetail.setTerm(item.getString("term"));
				repaymentDetail.setRepayDate(item.getString("repayDate"));
				repaymentDetail.setAmount(item.getString("amount"));
				repaymentDetail.setRealDate(item.getString("realDate"));
				repaymentDetail.setStatus(item.getString("status"));
				repaymentDetails.add(repaymentDetail);
			}
		}
		return repaymentDetails;
	}
	
	/***
	 * 还款详情
	 * @author  tangjian
	 * @date 2015年12月12日
	 */
	class RepaymentDetail
	{
		private String term;
		
		private String repayDate;
		
		private String amount;
		
		private String realDate;
		
		private String status;
		
		public String getTerm()
		{
			return term;
		}
		
		public void setTerm(String term)
		{
			this.term = term;
		}
		
		public String getRepayDate()
		{
			return repayDate;
		}
		
		public void setRepayDate(String repayDate)
		{
			this.repayDate = repayDate;
		}
		
		public String getAmount()
		{
			return amount;
		}
		
		public void setAmount(String amount)
		{
			this.amount = amount;
		}
		
		public String getRealDate()
		{
			return realDate;
		}
		
		public void setRealDate(String realDate)
		{
			this.realDate = realDate;
		}
		
		public String getStatus()
		{
			return status;
		}
		
		public void setStatus(String status)
		{
			this.status = status;
		}
	}
}
