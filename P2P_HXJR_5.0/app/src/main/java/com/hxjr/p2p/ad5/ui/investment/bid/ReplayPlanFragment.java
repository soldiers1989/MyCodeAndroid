package com.hxjr.p2p.ad5.ui.investment.bid;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.hxjr.p2p.ad5.bean.HkjhBean;
import com.hxjr.p2p.ad5.ui.investment.bid.adapter.ReplayPlanAdapter;
import com.hxjr.p2p.ad5.utils.DMConstant;
import com.hxjr.p2p.ad5.utils.ErrorUtil;
import com.hxjr.p2p.ad5.utils.HttpParams;
import com.hxjr.p2p.ad5.R;
import com.dm.http.HttpCallBack;
import com.dm.http.HttpUtil;
import com.dm.utils.DMJsonObject;
import com.dm.widgets.InnerListView;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ReplayPlanFragment extends Fragment
{
	private static ReplayPlanFragment instant;
	
	/**
	 * 标的ID
	 */
	private String bidId;
	
	private View mView;
	
	private InnerListView listView;
	
	private List<HkjhBean> replayList;
	
	private ReplayPlanAdapter adapter;
	
	public ReplayPlanFragment()
	{
		super();
	}
	
	public static ReplayPlanFragment getInstant(Bundle args)
	{
		if (instant == null)
		{
			instant = new ReplayPlanFragment();
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
		return inflater.inflate(R.layout.bid_replay_info, container, false);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		bidId = getArguments().getString("bidId");
		mView = getView();
		listView = (InnerListView)mView.findViewById(R.id.replayListView);
		
		TextView emptyText = (TextView)LayoutInflater.from(getActivity()).inflate(R.layout.list_empty_view,
			(ViewGroup)listView.getParent(),
			false);
		emptyText.setText("暂时没有相关还款计划");
		((ViewGroup)listView.getParent()).addView(emptyText);
		listView.setEmptyView(emptyText);
		
		initHuanKuan();
	}
	
	private void initHuanKuan()
	{
		HttpParams httpParams = new HttpParams();
		httpParams.put("bidId", bidId);
		HttpUtil.getInstance().post(getContext(), DMConstant.API_Url.BID_REPAYLIST, httpParams, new HttpCallBack()
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
							replayList = new ArrayList<HkjhBean>();
							for (int i = 0; i < data.length(); i++)
							{
								JSONObject jsonObject = (JSONObject)data.get(i);
								DMJsonObject mdObject = new DMJsonObject(jsonObject.toString());
								HkjhBean hkjhBean = new HkjhBean(mdObject);
								replayList.add(hkjhBean);
							}
							
							adapter = new ReplayPlanAdapter(getActivity(), replayList);
							listView.setAdapter(adapter);
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
				setShowProgress(true);
			}
			
		});
	}
}
