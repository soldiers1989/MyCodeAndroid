package com.hxjr.p2p.ad5.ui.mine.loan;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.hxjr.p2p.ad5.bean.UserLoanBid;
import com.hxjr.p2p.ad5.ui.investment.bid.BidDetailActivity;
import com.hxjr.p2p.ad5.utils.DMConstant;
import com.hxjr.p2p.ad5.utils.ErrorUtil;
import com.hxjr.p2p.ad5.utils.HttpParams;
import com.hxjr.p2p.ad5.R;
import com.dm.http.HttpCallBack;
import com.dm.http.HttpUtil;
import com.dm.utils.DMJsonObject;
import com.dm.widgets.DMListView;
import com.dm.widgets.DMListView.OnMoreListener;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * 我的借款-已还清类
 * @author  huangkaibo
 * @date 2015-11-17
 */
public class MyLoanPayOffFragment extends Fragment
{
	private SwipeRefreshLayout mSwipeRefreshLayout;
	
	private DMListView mListView;
	
	private MyLoanPayOffAdapter adapter;
	
	private boolean isOnRefresh = true;
	
	private boolean isOnLoadMore = false;
	
	private List<UserLoanBid> userLoanBids = new ArrayList<UserLoanBid>(5);
	
	private int pageNumber = 1;
	
	private int pageSize = 10;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		return inflater.inflate(R.layout.invest_list, container, false);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		initView();
		initListener();
		getData(pageNumber);
	}
	
	/**
	 * 初始化控件
	 */
	private void initView()
	{
		mSwipeRefreshLayout = (SwipeRefreshLayout)getView().findViewById(R.id.dmSwipeRefreshLayout);
		mListView = (DMListView)getView().findViewById(R.id.investListView);
		mListView.setEmptyText("您当前没有已还清的借款！");
		mListView.setAdapter(new MyLoanPayOffAdapter(getActivity(), userLoanBids));
	}
	
	private void initListener()
	{
		mSwipeRefreshLayout.setOnRefreshListener(new OnRefreshListener()
		{
			@Override
			public void onRefresh()
			{
				isOnRefresh = true;
				mSwipeRefreshLayout.setRefreshing(true);
				getData(1);
			}
		});
		
		mListView.setOnMoreListener(new OnMoreListener()
		{
			@Override
			public void onMore()
			{
				isOnLoadMore = true;
				getData(pageNumber + 1);
			}
		});
		mListView.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				startActivity(new Intent(getActivity(), BidDetailActivity.class).putExtra("bidId",
					((UserLoanBid)parent.getAdapter().getItem(position)).getBidId() + ""));
			}
		});
	}
	
	private void getData(int postPage)
	{
		HttpParams params = new HttpParams();
		params.put("bidStatus", "2"); //1，还款中；2，已还清
		params.put("pageIndex", postPage);
		params.put("pageSize", pageSize);
		HttpUtil.getInstance().post(getContext(), DMConstant.API_Url.USER_MYLOANLIST, params, new HttpCallBack()
		{
			@Override
			public void onSuccess(JSONObject result)
			{
				try
				{
					String code = result.getString("code");
					if (DMConstant.ResultCode.SUCCESS.equals(code))
					{ // 成功
						List<UserLoanBid> datas = parseLoadBidDatas(result.getJSONArray("data"));
						if (null != datas && datas.size() > 0)
						{
							if (isOnRefresh)
							{
								pageNumber = 1;
								userLoanBids.clear();
							}
							if (isOnLoadMore)
							{
								pageNumber += 1;
							}
							userLoanBids.addAll(datas);
						}
						if (null == adapter)
						{
							adapter = new MyLoanPayOffAdapter(getActivity(), userLoanBids);
							mListView.setAdapter(adapter);
						}
						else
						{
							adapter.setDatas(userLoanBids);
							adapter.notifyDataSetChanged();
						}
					}
					else
					{
						ErrorUtil.showError(result);
					}
					stopRefreshAndLoadMore();
				}
				catch (JSONException e)
				{
					e.printStackTrace();
					stopRefreshAndLoadMore();
				}
			}
			
			@Override
			public void onFailure(Throwable t, Context context)
			{
				super.onFailure(t, context);
				stopRefreshAndLoadMore();
			}
			
			@Override
			public void onStart()
			{
				super.onStart();
			}
		});
	}
	
	/***
	* 解析接口返回的我的借款列表数据
	* @param jsonObject
	* @throws JSONException 
	*/
	protected List<UserLoanBid> parseLoadBidDatas(JSONArray datas) throws JSONException
	{
		List<UserLoanBid> userLoanBids = new ArrayList<UserLoanBid>(5);
		if (datas.length() > 0)
		{
			for (int i = 0; i < datas.length(); i++)
			{
				DMJsonObject item = new DMJsonObject(datas.getJSONObject(i).toString());
				UserLoanBid userLoanBid = new UserLoanBid();
				userLoanBid.setTotalTerm(item.getInt("totalTerm"));
				userLoanBid.setStatus(item.getString("status"));
				userLoanBid.setCurBackAmount(item.getString("curBackAmount"));
				userLoanBid.setIsDay(item.getString("isDay"));
				userLoanBid.setCleanTime(item.getString("cleanTime"));
				userLoanBid.setBackTime(item.getString("backTime"));
				userLoanBid.setBackTerm(item.getInt("backTerm"));
				userLoanBid.setTotalBackAmount(item.getString("totalBackAmount"));
				userLoanBid.setRate(item.getString("rate"));
				userLoanBid.setTheterm(item.getInt("theterm"));
				userLoanBid.setTotalAmount(item.getString("totalAmount"));
				userLoanBid.setBidId(item.getInt("bidId"));
				userLoanBid.setBidTitle(item.getString("bidTitle"));
				userLoanBids.add(userLoanBid);
			}
		}
		return userLoanBids;
	}
	
	/**
	 * 停止刷新或加载更多
	 */
	protected void stopRefreshAndLoadMore()
	{
		if (isOnRefresh)
		{
			isOnRefresh = false;
			mSwipeRefreshLayout.setRefreshing(false);
			if (userLoanBids.size() == 0)
			{
				mListView.stopLoading();
				return;
			}
		}
		if (isOnLoadMore)
		{
			isOnLoadMore = false;
		}
		if (userLoanBids.size() == 0 || userLoanBids.size() < pageSize * pageNumber)
		{
			mListView.hasMoreDate(false);
		}
		else
		{
			mListView.hasMoreDate(true);
		}
		mListView.stopLoading();
	}
}
