package com.hxjr.p2p.ad5.ui.mine.invest;

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

import com.dm.http.HttpCallBack;
import com.dm.http.HttpUtil;
import com.dm.utils.DMJsonObject;
import com.dm.utils.DMLog;
import com.dm.utils.StringUtils;
import com.dm.widgets.DMListView;
import com.dm.widgets.DMListView.OnMoreListener;
import com.dm.widgets.DMSwipeRefreshLayout;
import com.hxjr.p2p.ad5.R;
import com.hxjr.p2p.ad5.bean.MyInvestIn;
import com.hxjr.p2p.ad5.receiver.BidAndCreReceiver;
import com.hxjr.p2p.ad5.receiver.BidAndCreReceiver.OnSuccessListener;
import com.hxjr.p2p.ad5.ui.investment.bid.BidDetailActivity;
import com.hxjr.p2p.ad5.utils.DMConstant;
import com.hxjr.p2p.ad5.utils.ErrorUtil;
import com.hxjr.p2p.ad5.utils.HttpParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 投标中
 * @author  huangkaibo
 * @date 2015年11月26日
 */
public class InvestInFragment extends Fragment implements OnRefreshListener, OnMoreListener, OnSuccessListener
{
	protected View mView;
	
	private Context context;
	
	private DMSwipeRefreshLayout dmSwipeRefreshLayout;
	
	private DMListView mListView;
	
	private InvestInAdapter adapter;
	
	private BidAndCreReceiver mBidAndCreReceiver;
	
	private int pageNumber = 1;
	
	private List<MyInvestIn> mList = new ArrayList<MyInvestIn>(DMConstant.DigitalConstant.TEN);
	
	private static final String STATUS_TYPE = "tbz";// 回款中-hkz;投标中-tbz;已结清-yjq
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		mView = inflater.inflate(R.layout.invest_list, container, false);
		return mView;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		context = getActivity();
		initView();
		initData();
	}
	
	private void initView()
	{
		dmSwipeRefreshLayout = (DMSwipeRefreshLayout)mView.findViewById(R.id.dmSwipeRefreshLayout);
		dmSwipeRefreshLayout.setOnRefreshListener(this);
		mListView = (DMListView)mView.findViewById(R.id.investListView);
		mListView.setEmptyText("您当前没有投标中的出借项目！");
		mListView.setOnMoreListener(this);
		mListView.setOnItemClickListener(new OnItemClickListener()
		{
			
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				MyInvestIn bidBean = (MyInvestIn)parent.getAdapter().getItem(position);
				if (bidBean != null)
				{
					Intent intent = new Intent(getActivity(), BidDetailActivity.class);
					intent.putExtra("bidId", bidBean.getBidId() + "");
					//					intent.putExtra("bidFlag", bidBean.getStatus());
					getActivity().startActivity(intent);
				}
			}
		});
	}
	
	private void initData()
	{
		if (adapter == null)
		{
			adapter = new InvestInAdapter(context);
		}
		mListView.setAdapter(adapter);
		pageNumber = 1;
		getList(pageNumber);
		mBidAndCreReceiver = new BidAndCreReceiver();
		mBidAndCreReceiver.setOnSuccessListener(this);
		getActivity().registerReceiver(mBidAndCreReceiver, new IntentFilter(BidAndCreReceiver.BID_SUCCESS_RECEIVER));
	}
	
	@Override
	public void onRefresh()
	{
		getList(1);
		dmSwipeRefreshLayout.setRefreshing(false);
	}
	
	@Override
	public void onMore()
	{
		getList(pageNumber);
	}
	
	private void getList(final int number)
	{
		
		HttpParams httpParams = new HttpParams();
		httpParams.put("pageIndex", number);
		httpParams.put("pageSize", DMConstant.DigitalConstant.PAGE_SIZE);
		httpParams.put("type", STATUS_TYPE);
		
		HttpUtil.getInstance().post(getContext(), DMConstant.API_Url.MY_INVESTMENT_LIST, httpParams, new HttpCallBack()
		{
			@Override
			public void onSuccess(JSONObject result)
			{
				try
				{
					DMLog.i("getList", result.toString());
					String code = result.getString("code");
					if (DMConstant.ResultCode.SUCCESS.equals(code))
					{
						String dataStr = result.getString("data");
						if (!StringUtils.isEmpty(dataStr))
						{
							JSONArray dataList = result.getJSONArray("data");
							mList.clear();
							for (int i = 0; i < dataList.length(); i++)
							{
								DMJsonObject data = new DMJsonObject(dataList.getString(i));
								MyInvestIn myInvestIn = new MyInvestIn(data);
								mList.add(myInvestIn);
							}
							if (number == 1)
							{
								adapter.clearList();
								pageNumber = 1;
							}
							adapter.addAll(mList);
							mListView.stopLoading();
							
							if (dataList.length() == 0 || dataList.length() < DMConstant.DigitalConstant.PAGE_SIZE)
							{
								mListView.hasMoreDate(false);
							}
							else
							{
								mListView.hasMoreDate(true);
								pageNumber++;
							}
						}
						else
						{
							if (number == 1)
							{
								adapter.clearList();
								pageNumber = 1;
								mListView.setAdapter(adapter);
							}
							mListView.stopLoading();
						}
					}
					else
					{
						// 失败
						ErrorUtil.showError(result);
						mListView.stopLoading();
						mListView.hasMoreDate(false);
					}
					dmSwipeRefreshLayout.setRefreshing(false);
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
				mListView.stopLoading();
				dmSwipeRefreshLayout.setRefreshing(false);
			}
		});
	}

	@Override
	public void onSuccessToUpdateUi(Intent intent)
	{
		getList(1);
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
}
