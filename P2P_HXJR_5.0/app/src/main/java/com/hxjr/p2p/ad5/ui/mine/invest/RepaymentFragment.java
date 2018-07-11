package com.hxjr.p2p.ad5.ui.mine.invest;

import android.content.BroadcastReceiver;
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
import com.dm.utils.StringUtils;
import com.dm.widgets.DMListView;
import com.dm.widgets.DMListView.OnMoreListener;
import com.dm.widgets.DMSwipeRefreshLayout;
import com.hxjr.p2p.ad5.R;
import com.hxjr.p2p.ad5.bean.Fee;
import com.hxjr.p2p.ad5.bean.MyInvestRepaymentIn;
import com.hxjr.p2p.ad5.service.ApiUtil;
import com.hxjr.p2p.ad5.service.ApiUtil.OnPostCallBack;
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
 * 回款中
 * @author  huangkaibo
 * @date 2015年11月26日
 */
public class RepaymentFragment extends Fragment implements OnRefreshListener, OnMoreListener
{
	private View mView;
	
	private DMSwipeRefreshLayout dmSwipeRefreshLayout;
	
	private DMListView mListView;
	
	private Context context;
	
	private RepaymentAdapter adapter;
	
	private List<MyInvestRepaymentIn> mList = new ArrayList<MyInvestRepaymentIn>(DMConstant.DigitalConstant.TEN);
	
	private int pageNumber = 1;
	
	private static final String STATUS_TYPE = "hkz";// 回款中-hkz;投标中-tbz;已结清-yjq
	
	private boolean isNeedPwd;// 是否需要交易密码
	
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
		register();
		queryFee();
	}
	
	private void initView()
	{
		dmSwipeRefreshLayout = (DMSwipeRefreshLayout)mView.findViewById(R.id.dmSwipeRefreshLayout);
		dmSwipeRefreshLayout.setOnRefreshListener(this);
		mListView = (DMListView)mView.findViewById(R.id.investListView);
		mListView.setEmptyText("您当前没有回款中的出借项目！");
		mListView.setOnMoreListener(this);
		mListView.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				MyInvestRepaymentIn bidBean = (MyInvestRepaymentIn)parent.getAdapter().getItem(position);
				if (bidBean != null)
				{
					Intent intent = new Intent(getActivity(), BidDetailActivity.class);
					intent.putExtra("bidId", bidBean.getBidId() + "");
					getActivity().startActivity(intent);
				}
			}
		});
	}
	
	private void initData()
	{
		if (adapter == null)
		{
			adapter = new RepaymentAdapter(context, isNeedPwd);
		}
		mListView.setAdapter(adapter);
		pageNumber = 1;
		getList(pageNumber);
	}
	
	@Override
	public void onMore()
	{
		getList(pageNumber);
	}
	
	@Override
	public void onRefresh()
	{
		getList(1);
		dmSwipeRefreshLayout.setRefreshing(false);
	}
	
	private void getList(final int number)
	{
		HttpParams params = new HttpParams();
		params.put("pageIndex", pageNumber);
		params.put("pageSize", DMConstant.DigitalConstant.PAGE_SIZE);
		params.put("type", STATUS_TYPE);
		
		HttpUtil.getInstance().post(getContext(), DMConstant.API_Url.MY_INVESTMENT_LIST, params, new HttpCallBack()
		{
			
			@Override
			public void onSuccess(JSONObject result)
			{
				try
				{
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
								MyInvestRepaymentIn mr = new MyInvestRepaymentIn(data);
								mList.add(mr);
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
					}
					dmSwipeRefreshLayout.setRefreshing(false);
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
		});
	}
	
	private void register()
	{
		IntentFilter filter = new IntentFilter();
		filter.addAction(DMConstant.BroadcastActions.MY_INVESTMENT_REPAYMENT);
		getActivity().registerReceiver(receiver, filter);
	}
	
	private BroadcastReceiver receiver = new BroadcastReceiver()
	{
		@Override
		public void onReceive(Context context, Intent intent)
		{
			onRefresh();
		}
	};
	
	@Override
	public void onDestroy()
	{
		// TODO Auto-generated method stub
		super.onDestroy();
		getActivity().unregisterReceiver(receiver);
	}
	
	/**
	 * 查询提现手续费和充值手续费
	 */
	private void queryFee()
	{
		ApiUtil.getFee(context, new OnPostCallBack()
		{
			@Override
			public void onSuccess(Fee fee)
			{
				isNeedPwd = fee.getChargep().isNeedPsd();
				initData();
			}
			
			@Override
			public void onFailure()
			{
				isNeedPwd = true;
				initData();
			}
		});
	}
	
}
