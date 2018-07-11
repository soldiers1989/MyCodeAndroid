package com.hxjr.p2p.ad5.ui.investment;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.dm.http.DMException;
import com.dm.http.HttpCallBack;
import com.dm.http.HttpUtil;
import com.dm.utils.DMJsonObject;
import com.dm.widgets.DMListView;
import com.dm.widgets.DMListView.OnMoreListener;
import com.dm.widgets.DMSwipeRefreshLayout;
import com.hxjr.p2p.ad5.R;
import com.hxjr.p2p.ad5.bean.Creditor;
import com.hxjr.p2p.ad5.receiver.BidAndCreReceiver;
import com.hxjr.p2p.ad5.receiver.BidAndCreReceiver.OnSuccessListener;
import com.hxjr.p2p.ad5.ui.investment.adapter.CreListAdapter;
import com.hxjr.p2p.ad5.ui.investment.cre.CreDetailActivity;
import com.hxjr.p2p.ad5.utils.DMConstant;
import com.hxjr.p2p.ad5.utils.ErrorUtil;
import com.hxjr.p2p.ad5.utils.HttpParams;
import com.hxjr.p2p.ad5.utils.NetConnectErrorManager;
import com.hxjr.p2p.ad5.utils.NetConnectErrorManager.NetConnetCallBack;

/**
 * 债权转让
 * @author  jiaohongyun
 * @date 2015年10月21日
 */
public class CreFragment extends Fragment implements OnRefreshListener, OnMoreListener, OnSuccessListener
{
	private View mView;
	
	private DMSwipeRefreshLayout dmSwipeRefreshLayout;
	
	private DMListView creListView;
	
	private CreListAdapter adapter;
	
	private BidAndCreReceiver mBidAndCreReceiver;
	
	private int pageNumber = 1;
	
	private NetConnectErrorManager netConnectErrorManager;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		return inflater.inflate(R.layout.creditor_list, container, false);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		mView = getView();
		dmSwipeRefreshLayout = (DMSwipeRefreshLayout)mView.findViewById(R.id.dmSwipeRefreshLayout);
		dmSwipeRefreshLayout.setOnRefreshListener(this);
		creListView = (DMListView)mView.findViewById(R.id.creListView);
		creListView.setEmptyText("暂无债权转让数据");
		creListView.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				Creditor creditor = (Creditor)parent.getAdapter().getItem(position);
				if (creditor != null)
				{
					Intent intent = new Intent(getActivity(), CreDetailActivity.class);
					intent.putExtra("bidId", "" + creditor.getBidId());
					intent.putExtra("creId", "" + creditor.getId());
					intent.putExtra("bidFlag", creditor.getFlag());
					intent.putExtra("creStatus", creditor.getStatus());
					startActivity(intent);
				}
			}
		});
		creListView.setOnMoreListener(this);
		netConnectErrorManager = new NetConnectErrorManager(mView, dmSwipeRefreshLayout, new NetConnetCallBack()
		{
			@Override
			public void onNetErrorRefrensh()
			{
				onRefresh();
			}
		});
		initData();
	}
	
	@Override
	public void onRefresh()
	{
		getCreList(1);
		dmSwipeRefreshLayout.setRefreshing(false);
	}
	
	/**
	 * 初始化数据
	 */
	private void initData()
	{
		if (adapter == null)
		{
			adapter = new CreListAdapter(getActivity());
		}
		creListView.setAdapter(adapter);
		getCreList(1);
		mBidAndCreReceiver = new BidAndCreReceiver();
		mBidAndCreReceiver.setOnSuccessListener(this);
		getActivity().registerReceiver(mBidAndCreReceiver, new IntentFilter(BidAndCreReceiver.CRE_SUCCESS_RECEIVER));
	}
	
	@Override
	public void onSuccessToUpdateUi(Intent intent)
	{
		//		String creId = intent.getStringExtra("creId");
		//		List<Creditor> creditors = new ArrayList<Creditor>(10);
		//		creditors.addAll(adapter.getAll());
		//		if (null != creditors && creditors.size() > 0)
		//		{
		//			for (int i = 0; i < creditors.size(); i++)
		//			{
		//				if (creId != null && creId.equals(creditors.get(i).getId() + ""))
		//				{
		//					//					creditors.remove(i);//购买债券成功后，改债券将不存在
		//					adapter.clearList();
		//					adapter.addAll(creditors);
		//				}
		//			}
		//		}
		getCreList(1);
	}
	
	@Override
	public void onDestroy()
	{
		if (null != mBidAndCreReceiver)
		{
			getActivity().unregisterReceiver(mBidAndCreReceiver);
		}
		super.onDestroy();
	}
	
	private void getCreList(final int number)
	{
		HttpParams httpParams = new HttpParams();
		httpParams.put("pageIndex", "" + number);
		httpParams.put("pageSize", DMConstant.DigitalConstant.PAGE_SIZE);
		HttpUtil.getInstance().post(getContext(), DMConstant.API_Url.CREDITOR_CREDITORLIST, httpParams, new HttpCallBack()
		{
			@Override
			public void onSuccess(JSONObject result)
			{
				if (netConnectErrorManager != null)
				{
					netConnectErrorManager.onNetGood();
				}
				try
				{
					dmSwipeRefreshLayout.setRefreshing(false);
					String code = result.getString("code");
					if (DMConstant.ResultCode.SUCCESS.equals(code))
					{
						// 成功
						JSONArray dataList = result.getJSONArray("data");
						List<Creditor> creBeans = new ArrayList<Creditor>();
						for (int i = 0; i < dataList.length(); i++)
						{
							DMJsonObject record = new DMJsonObject(dataList.getJSONObject(i).toString());
							Creditor bidBean = new Creditor(record);
							creBeans.add(bidBean);
						}
						if (number == 1)
						{
							adapter.clearList();
							pageNumber = 1;
						}
						adapter.addAll(creBeans);
						creListView.stopLoading();
						
						if (pageNumber == 1 && creBeans.size() == 0)
						{
							return;
						}
						if (dataList.length() == 0 || dataList.length() < DMConstant.DigitalConstant.PAGE_SIZE)
						{
							creListView.hasMoreDate(false);
						}
						else
						{
							creListView.hasMoreDate(true);
							pageNumber++;
						}
					}
					else
					{
						// 失败
						ErrorUtil.showError(result);
						creListView.stopLoading();
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
				creListView.stopLoading();
				dmSwipeRefreshLayout.setRefreshing(false);
			}
			
			@Override
			public void onStart()
			{
				super.onStart();
				dmSwipeRefreshLayout.setRefreshing(true);
			}
			
			@Override
			public void onConnectFailure(DMException dmE, Context context)
			{
				// TODO Auto-generated method stub
				super.onConnectFailure(dmE, context);
				if (netConnectErrorManager != null)
				{
					adapter.clearList();
					netConnectErrorManager.onNetError();
				}
			}
		});
	}
	
	@Override
	public void onMore()
	{
		getCreList(pageNumber);
	}
}
