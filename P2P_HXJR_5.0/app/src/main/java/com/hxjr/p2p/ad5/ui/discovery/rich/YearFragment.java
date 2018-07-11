package com.hxjr.p2p.ad5.ui.discovery.rich;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.hxjr.p2p.ad5.bean.BidRankInfo;
import com.hxjr.p2p.ad5.ui.discovery.rich.adapter.RichListAdapter;
import com.hxjr.p2p.ad5.utils.DMConstant;
import com.hxjr.p2p.ad5.utils.ErrorUtil;
import com.hxjr.p2p.ad5.utils.HttpParams;
import com.hxjr.p2p.ad5.utils.NetConnectErrorManager;
import com.hxjr.p2p.ad5.utils.NetConnectErrorManager.NetConnetCallBack;
import com.hxjr.p2p.ad5.R;
import com.dm.http.DMException;
import com.dm.http.HttpCallBack;
import com.dm.http.HttpUtil;
import com.dm.utils.DMJsonObject;
import com.dm.utils.DMLog;
import com.dm.widgets.DMListView;
import com.dm.widgets.DMListView.OnMoreListener;
import com.dm.widgets.DMSwipeRefreshLayout;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 年排行
 * @author  huangkaibo
 * @date 2015年11月9日
 */
public class YearFragment extends Fragment implements OnRefreshListener, OnMoreListener
{
	private View mView;
	
	private Context context;
	
	private DMSwipeRefreshLayout dmSwipeRefreshLayout;
	
	private DMListView mListView;
	
	private RichListAdapter adapter;
	
	private int pageNumber = 1;
	
	private static final String RANK_TYPE = "3";// 1-周排行，2-月排行，3-年排行
	
	private NetConnectErrorManager netConnectErrorManager;
	
	private View richHeaderView;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		mView = inflater.inflate(R.layout.rich_list_week_list, container, false);
		return mView;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		context = getActivity();
		initView();
	}
	
	private void initView()
	{
		dmSwipeRefreshLayout = (DMSwipeRefreshLayout)mView.findViewById(R.id.dmSwipeRefreshLayout);
		dmSwipeRefreshLayout.setOnRefreshListener(this);
		mListView = (DMListView)mView.findViewById(R.id.weekListView);
		richHeaderView = LayoutInflater.from(context).inflate(R.layout.rich_list_header, mListView, false);
		mListView.addHeaderView(richHeaderView);
		mListView.setEmptyText(R.string.year_ranking_no_data);
		mListView.setOnMoreListener(this);
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
		getList(1);
		dmSwipeRefreshLayout.setRefreshing(false);
	}
	
	@Override
	public void onMore()
	{
		getList(pageNumber);
	}
	
	/**
	 * 初始化数据
	 */
	private void initData()
	{
		if (adapter == null)
		{
			adapter = new RichListAdapter(context);
		}
		mListView.setAdapter(adapter);
		pageNumber = 1;
		getList(pageNumber);
	}
	
	private void getList(final int number)
	{
		
		HttpParams params = new HttpParams();
		params.put("type", RANK_TYPE);
		params.put("pageIndex", pageNumber);
		params.put("pageSize", DMConstant.DigitalConstant.PAGE_SIZE);
		
		HttpUtil.getInstance().post(getContext(), DMConstant.API_Url.USER_BID_RANK, params, new HttpCallBack()
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
					DMLog.i("getRichList", result.toString());
					String code = result.getString("code");
					if (DMConstant.ResultCode.SUCCESS.equals(code))
					{
						JSONArray dataList = result.getJSONArray("data");
						List<BidRankInfo> rList = new ArrayList<BidRankInfo>();
						for (int i = 0; i < dataList.length(); i++)
						{
							DMJsonObject data = new DMJsonObject(dataList.getString(i));
							BidRankInfo r = new BidRankInfo(data);
							rList.add(r);
						}
						if (number == 1)
						{
							adapter.clearList();
							pageNumber = 1;
						}
						adapter.addAll(rList);
						mListView.stopLoading();
						if (number == 1 && rList.size() == 0)
						{
							return;
						}
						
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
						ErrorUtil.showError(result);
						mListView.stopLoading();
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
				mListView.stopLoading();
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
					netConnectErrorManager.onNetError();
				}
			}
		});
		
	}
}
