package com.hxjr.p2p.ad5.ui.mine.loan;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.dm.http.HttpCallBack;
import com.dm.http.HttpUtil;
import com.dm.utils.DMJsonObject;
import com.dm.widgets.DMListView;
import com.dm.widgets.DMListView.OnMoreListener;
import com.hxjr.p2p.ad5.R;
import com.hxjr.p2p.ad5.bean.ForwardRepayInfo;
import com.hxjr.p2p.ad5.bean.RepayInfo;
import com.hxjr.p2p.ad5.bean.UserLoanBid;
import com.hxjr.p2p.ad5.receiver.BidAndCreReceiver;
import com.hxjr.p2p.ad5.receiver.BidAndCreReceiver.OnSuccessListener;
import com.hxjr.p2p.ad5.ui.investment.bid.BidDetailActivity;
import com.hxjr.p2p.ad5.ui.mine.loan.MyLoanRepaymentAdapter.RepaymentSuccessListener;
import com.hxjr.p2p.ad5.utils.DMConstant;
import com.hxjr.p2p.ad5.utils.ErrorUtil;
import com.hxjr.p2p.ad5.utils.HttpParams;

/**
 * 我的借款-还款列类
 * @author  lihao
 * @date 2015-6-5
 */
public class MyLoanRepaymentFragment extends Fragment implements RepaymentSuccessListener, OnSuccessListener
{
	private SwipeRefreshLayout mSwipeRefreshLayout;
	
	private DMListView mListView;
	
	private MyLoanRepaymentAdapter adapter;
	
	private List<UserLoanBid> userLoanBids = new ArrayList<UserLoanBid>(5);
	
	private BidAndCreReceiver mBidAndCreReceiver;
	
	private boolean isOnRefresh = true;
	
	private boolean isOnLoadMore = false;
	
	private int pageNumber = 1;
	
	private int pageSize = 10;
	
	private Timer timer = new Timer();  
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		return inflater.inflate(R.layout.invest_list, container, false);
	}

	@Override
	public void onResume() {
		super.onResume();
		getData(pageNumber);
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
		mListView.setEmptyText("您当前没有还款中的借款！");
		mListView.setAdapter(new MyLoanRepaymentAdapter(getActivity(), userLoanBids, this));
	}
	
	private void initListener()
	{
		
		mBidAndCreReceiver = new BidAndCreReceiver();
		mBidAndCreReceiver.setOnSuccessListener(this);
		getActivity().registerReceiver(mBidAndCreReceiver, new IntentFilter(BidAndCreReceiver.LOAN_SUCCESS_RECEIVER));
		
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
		params.put("bidStatus", "1"); //1，还款中；2，已还清
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
						if (isOnRefresh)
						{
							pageNumber = 1;
						}
						if (isOnLoadMore)
						{
							pageNumber += 1;	
						}
						userLoanBids.clear();
						if (null != datas && datas.size() > 0)
						{
							userLoanBids.addAll(datas);
						}
						if (null == adapter)
						{
							adapter = new MyLoanRepaymentAdapter(getActivity(), userLoanBids, MyLoanRepaymentFragment.this);
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
				JSONObject repayInfoItem = item.getJSONObject("repayInfo");
				if (null != repayInfoItem)
				{
					RepayInfo repayInfo = new RepayInfo();
					repayInfo.setOverdueManage(repayInfoItem.getDouble("overdueManage"));
					repayInfo.setAccountAmount(repayInfoItem.getDouble("accountAmount"));
					repayInfo.setLoanArrMoney(repayInfoItem.getDouble("loanArrMoney"));
					repayInfo.setLoanManageAmount(repayInfoItem.getDouble("loanManageAmount"));
					repayInfo.setLoanMustMoney(repayInfoItem.getDouble("loanMustMoney"));
					repayInfo.setNumber(repayInfoItem.getInt("number"));
					repayInfo.setLoanTotalMoney(repayInfoItem.getDouble("loanTotalMoney"));
					repayInfo.setYhbj(repayInfoItem.getDouble("yhbj"));
					repayInfo.setLoanID(repayInfoItem.getInt("loanID"));
					repayInfo.setOverdueInterest(repayInfoItem.getDouble("overdueInterest"));
					userLoanBid.setRepayInfo(repayInfo);
				}
				
				if (item.has("forwardRepayInfo"))
				{
					JSONObject forwardRepayInfoItem = item.getJSONObject("forwardRepayInfo");
					if (null != forwardRepayInfoItem)
					{
						ForwardRepayInfo forwardRepayInfo = new ForwardRepayInfo();
						forwardRepayInfo.setAccountAmount(forwardRepayInfoItem.getDouble("accountAmount"));
						forwardRepayInfo.setSylx(forwardRepayInfoItem.getDouble("sylx"));
						forwardRepayInfo.setLoanManageAmount(forwardRepayInfoItem.getDouble("loanManageAmount"));
						forwardRepayInfo.setLoanMustMoney(forwardRepayInfoItem.getDouble("loanMustMoney"));
						forwardRepayInfo.setNumber(forwardRepayInfoItem.getInt("number"));
						forwardRepayInfo.setLoanTotalMoney(forwardRepayInfoItem.getDouble("loanTotalMoney"));
						forwardRepayInfo.setSybj(forwardRepayInfoItem.getDouble("sybj"));
						forwardRepayInfo.setLoanID(forwardRepayInfoItem.getInt("loanID"));
						forwardRepayInfo.setLoanPenalAmount(forwardRepayInfoItem.getDouble("loanPenalAmount"));
						userLoanBid.setForwardRepayInfo(forwardRepayInfo);
					}
				}
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
	
	@Override
	public void repaymentSuccess(int type, int position)
	{
		//		if (null != userLoanBids && userLoanBids.size() > 0)
		//		{
		//			userLoanBids.remove(position);
		//			adapter.notifyDataSetChanged();
		//		}
		isOnRefresh = true;
		getData(1);
	}
	
	@Override
	public void onSuccessToUpdateUi(Intent intent)
	{
		isOnRefresh = true;
		mSwipeRefreshLayout.setRefreshing(true);
		TimerTask task = new TimerTask()
		{
			
			@Override
			public void run()
			{
				getData(1);
			}
		};
		timer.schedule(task, 5000);
		
	}
}
