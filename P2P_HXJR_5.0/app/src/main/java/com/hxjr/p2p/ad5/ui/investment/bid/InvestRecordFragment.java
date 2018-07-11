package com.hxjr.p2p.ad5.ui.investment.bid;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dm.http.HttpCallBack;
import com.dm.http.HttpUtil;
import com.dm.utils.DMJsonObject;
import com.dm.widgets.InnerListView;
import com.hxjr.p2p.ad5.R;
import com.hxjr.p2p.ad5.bean.BidRecord;
import com.hxjr.p2p.ad5.ui.investment.bid.adapter.InvestInfoAdapter;
import com.hxjr.p2p.ad5.utils.DMConstant;
import com.hxjr.p2p.ad5.utils.ErrorUtil;
import com.hxjr.p2p.ad5.utils.HttpParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class InvestRecordFragment extends Fragment
{
	private static InvestRecordFragment instant;
	
	/**
	 * 标的ID
	 */
	private String bidId;
	
	private View mView;
	
	private InnerListView investListView;
	
	private InvestInfoAdapter adapter;
	
	public InvestRecordFragment()
	{
		super();
	}
	
	public static InvestRecordFragment getInstant(Bundle args)
	{
		if (instant == null)
		{
			instant = new InvestRecordFragment();
			instant.setArguments(args);
		}
		return instant;
	}
	
	public static void setNull()
	{
		if (null != instant)
		{
			instant = null;
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		return inflater.inflate(R.layout.bid_invest_info, container, false);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		mView = getView();
		bidId = getArguments().getString("bidId");
		mView = getView();
		investListView = (InnerListView)mView.findViewById(R.id.investListView);
		
		TextView emptyText = (TextView)LayoutInflater.from(getActivity()).inflate(R.layout.list_empty_view,
			(ViewGroup)investListView.getParent(),
			false);
		emptyText.setText("暂时没有相关出借记录");
		((ViewGroup)investListView.getParent()).addView(emptyText);
		investListView.setEmptyView(emptyText);
		
		initInvestRecords();
	}
	
	/**
	 * 
	 */
	private void initInvestRecords()
	{
		HttpParams httpParams = new HttpParams();
		httpParams.put("bidId", bidId);
		HttpUtil.getInstance().post(getContext(), DMConstant.API_Url.BID_RECORDSLIST, httpParams, new HttpCallBack()
		{
			@Override
			public void onSuccess(JSONObject result)
			{
				try
				{
					String code = result.getString("code");
					if (code.equals(DMConstant.ResultCode.SUCCESS))
					{
						JSONArray data = result.getJSONArray("data");
						if (data.length() > 0)
						{
							List<BidRecord> list = new ArrayList<BidRecord>();
							for (int i = 0; i < data.length(); i++)
							{
								JSONObject jsonObject = (JSONObject)data.get(i);
								DMJsonObject mdObject = new DMJsonObject(jsonObject.toString());
								BidRecord hkjhBean = new BidRecord(mdObject);
								list.add(hkjhBean);
							}
							adapter = new InvestInfoAdapter(getActivity());
							investListView.setAdapter(adapter);
							adapter.addRecords(list);
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
				setShowProgress(true);
			}
			
		});
	}
}
