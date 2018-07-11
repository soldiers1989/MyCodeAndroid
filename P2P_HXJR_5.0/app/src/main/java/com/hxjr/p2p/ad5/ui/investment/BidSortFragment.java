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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.dm.http.DMException;
import com.dm.http.HttpCallBack;
import com.dm.http.HttpUtil;
import com.dm.utils.DMJsonObject;
import com.dm.widgets.DMListView.OnMoreListener;
import com.dm.widgets.DMSwipeRefreshLayout;
import com.hxjr.p2p.ad5.R;
import com.hxjr.p2p.ad5.bean.BidBean;
import com.hxjr.p2p.ad5.receiver.BidAndCreReceiver;
import com.hxjr.p2p.ad5.receiver.BidAndCreReceiver.OnSuccessListener;
import com.hxjr.p2p.ad5.ui.investment.adapter.BidListAdapter;
import com.hxjr.p2p.ad5.utils.DMConstant;
import com.hxjr.p2p.ad5.utils.ErrorUtil;
import com.hxjr.p2p.ad5.utils.HttpParams;
import com.hxjr.p2p.ad5.utils.NetConnectErrorManager;
import com.hxjr.p2p.ad5.utils.NetConnectErrorManager.NetConnetCallBack;

/**
 * 投资列表
 * 
 * @author jiaohongyun
 * @date 2015年10月21日
 */
public class BidSortFragment extends Fragment implements
	OnRefreshListener, OnMoreListener, OnSuccessListener {
	private View mView;

	private DMSwipeRefreshLayout dmSwipeRefreshLayout;

	private ExpandableListView bidListView;

	private BidListAdapter adapter;

	private BidAndCreReceiver mBidAndCreReceiver;

	private int pageNumber = 1;

	private NetConnectErrorManager netConnectErrorManager;

	@Override
	public View onCreateView(LayoutInflater inflater,
		ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.bid_expandlist, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mView = getView();
		dmSwipeRefreshLayout = (DMSwipeRefreshLayout) mView
			.findViewById(R.id.dmSwipeRefreshLayout);
		dmSwipeRefreshLayout.setOnRefreshListener(this);
		bidListView =  (ExpandableListView) mView
			.findViewById(R.id.bid_expandablelistview);
//		bidListView.setEmptyText("暂无投资项目数据");
		
//		bidListView.setOnItemClickListener(new OnItemClickListener() {
//			@Override
//			public void onItemClick(AdapterView<?> parent, View view,
//				int position, long id) {
//				BidBean bidBean = (BidBean) parent.getAdapter()
//					.getItem(position);
//				if (bidBean != null) {
//					String bidId = "" + id;
//					Intent intent = new Intent(getActivity(),
//						BidDetailActivity.class);
//					intent.putExtra("bidId", bidId);
//					intent.putExtra("bidFlag", bidBean.getFlag());
//					getActivity().startActivity(intent);
//				}
//
//			}
//		});
		
//		bidListView.setOnMoreListener(this);

		netConnectErrorManager = new NetConnectErrorManager(mView,
			dmSwipeRefreshLayout, new NetConnetCallBack() {
				@Override
				public void onNetErrorRefrensh() {
					onRefresh();
				}
			});
		initData();
	}

	@Override
	public void onRefresh() {
		getBidList(1);
		dmSwipeRefreshLayout.setRefreshing(false);
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		if (adapter == null) {
			adapter = new BidListAdapter(getActivity());
		}
		bidListView.setAdapter(adapter);
		getBidList(1);
		mBidAndCreReceiver = new BidAndCreReceiver();
		mBidAndCreReceiver.setOnSuccessListener(this);
		getActivity().registerReceiver(mBidAndCreReceiver,
			new IntentFilter(BidAndCreReceiver.BID_SUCCESS_RECEIVER));
	}

	@Override
	public void onSuccessToUpdateUi(Intent intent) {
		// String bidId = intent.getStringExtra("bidId");
		// String amount = intent.getStringExtra("amount");
		// List<BidBean> bidBeans = new ArrayList<BidBean>(10);
		// bidBeans.addAll(adapter.getAll());
		// if (null != bidBeans && bidBeans.size() > 0)
		// {
		// for (int i = 0; i < bidBeans.size(); i++)
		// {
		// if (bidId != null && bidId.equals(bidBeans.get(i).getId() + ""))
		// {
		// String remainAmount = bidBeans.get(i).getRemainAmount();
		// if (!StringUtils.isEmptyOrNull(amount) &&
		// !StringUtils.isEmptyOrNull(remainAmount))
		// {
		// bidBeans.get(i).setRemainAmount(
		// FormatUtil.formatStr2(Double.parseDouble(remainAmount) -
		// Double.parseDouble(amount) + ""));
		// adapter.clearList();
		// adapter.addAll(bidBeans);
		// }
		// }
		// }
		// }
		getBidList(1);
	}

	@Override
	public void onDestroy() {
		if (null != mBidAndCreReceiver) {
			getActivity().unregisterReceiver(mBidAndCreReceiver);
		}
		super.onDestroy();
	}

	private void getBidList(final int number) {
		HttpParams httpParams = new HttpParams();
		httpParams.put("pageIndex", "" + number);
		httpParams.put("pageSize",
			DMConstant.DigitalConstant.PAGE_SIZE);
		HttpUtil.getInstance().post(getContext(),
			DMConstant.API_Url.BID_PUBLICS_BIDLIST, httpParams,
			new HttpCallBack() {
				@Override
				public void onSuccess(JSONObject result) {
					if (netConnectErrorManager != null) {
						netConnectErrorManager.onNetGood();
					}
					try {
						dmSwipeRefreshLayout.setRefreshing(false);
						String code = result.getString("code");
						if (DMConstant.ResultCode.SUCCESS
							.equals(code)) {
							// 成功
							JSONArray dataList = result
								.getJSONArray("data");
							List<BidBean> bidBeans = new ArrayList<BidBean>();
							for (int i = 0; i < dataList.length(); i++) {
								DMJsonObject record = new DMJsonObject(
									dataList.getJSONObject(i)
										.toString());
								BidBean bidBean = new BidBean(record);
								if (DMConstant.Config.bidIndex) {
									if (bidBean.getFlag().equals("信")) {
										bidBeans.add(bidBean);
										Log.d("BId",
											bidBean.toString());
									}
								} else {
									if (bidBean.getFlag().equals("保")) {
										bidBeans.add(bidBean);
										Log.d("BId",
											bidBean.toString());
									}
								}

							}
							if (number == 1) {
								adapter.clearList();
								pageNumber = 1;
							}
							adapter.addAll(bidBeans);
//							bidListView.stopLoading();
							if (pageNumber == 1
								&& bidBeans.size() == 0) {
								return;
							}

							if (dataList.length() == 0
								|| dataList.length() < DMConstant.DigitalConstant.PAGE_SIZE) {
//								bidListView.hasMoreDate(false);
							} else {
//								bidListView.hasMoreDate(true);
								pageNumber++;
							}
						} else {
							// 失败
							ErrorUtil.showError(result);
//							bidListView.stopLoading();
						}

					} catch (JSONException e) {
						e.printStackTrace();
					}
				}

				@Override
				public void onFailure(Throwable t, Context context) {
					super.onFailure(t, context);
//					bidListView.stopLoading();
					dmSwipeRefreshLayout.setRefreshing(false);
				}

				@Override
				public void onStart() {
					super.onStart();
					dmSwipeRefreshLayout.setRefreshing(true);
				}

				@Override
				public void onConnectFailure(DMException dmE,
					Context context) {
					// TODO Auto-generated method stub
					super.onConnectFailure(dmE, context);
					if (netConnectErrorManager != null) {
						adapter.clearList();
						netConnectErrorManager.onNetError();
					}
				}
			});
	}

	@Override
	public void onMore() {
		getBidList(pageNumber);
	}
}
