package com.hxjr.p2p.ad5.ui.investment;

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
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.LinearLayout;

import com.dm.http.DMException;
import com.dm.http.HttpCallBack;
import com.dm.http.HttpUtil;
import com.dm.utils.DMJsonObject;
import com.dm.utils.DMLog;
import com.dm.widgets.DMSwipeRefreshLayout;
import com.hxjr.p2p.ad5.DMApplication;
import com.hxjr.p2p.ad5.R;
import com.hxjr.p2p.ad5.bean.BidBean;
import com.hxjr.p2p.ad5.bean.MfenleiBean;
import com.hxjr.p2p.ad5.service.ApiUtil;
import com.hxjr.p2p.ad5.ui.investment.adapter.BidListExpandAdapter;
import com.hxjr.p2p.ad5.ui.investment.bid.BidDetailActivity;
import com.hxjr.p2p.ad5.ui.mine.user.LoginActivity;
import com.hxjr.p2p.ad5.utils.DMConstant;
import com.hxjr.p2p.ad5.utils.HttpParams;
import com.hxjr.p2p.ad5.utils.NetConnectErrorManager;
import com.hxjr.p2p.ad5.utils.NetConnectErrorManager.NetConnetCallBack;
import com.hxjr.p2p.ad5.utils.TimeUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class BidChangeFragment extends Fragment implements OnRefreshListener {

    private ExpandableListView list;

    private BidListExpandAdapter bidAdapter;

    private List<List<BidBean>> childList;

    private List<MfenleiBean> groupList;

    // 推荐标
    private LinearLayout recommend_bid;

    private MyBroadcastReceiver mbr;

    private DMApplication dmApp;

    private DMSwipeRefreshLayout dmSwipeRefreshLayout;

    private NetConnectErrorManager netConnectErrorManager;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bid_elist, container, false);
        list = (ExpandableListView) view.findViewById(R.id.elist);
        dmSwipeRefreshLayout = (DMSwipeRefreshLayout) view
                .findViewById(R.id.dmSwipeRefreshLayout);
        recommend_bid = (LinearLayout) view.findViewById(R.id.recommend_bids);


        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.myBroadcastReceiver.Inrent2"); // 为BroadcastReceiver指定action，即要监听的消息名字。
        intentFilter.addAction("com.myBroadcastReceiver.Inrent222");
        mbr = new MyBroadcastReceiver();
        getActivity().registerReceiver(mbr, intentFilter);
        ApiUtil.getUserInfo(getContext());
        dmSwipeRefreshLayout.setOnRefreshListener(this);
        list.setGroupIndicator(null);
        //		EventBus.getDefault().register(this);
        getdata();
        dmApp = DMApplication.getInstance();
        setLisenter();
        netConnectErrorManager = new NetConnectErrorManager(view,
                dmSwipeRefreshLayout, new NetConnetCallBack() {
            @Override
            public void onNetErrorRefrensh() {
                onRefresh();
            }
        });
        return view;
    }


    private void setLisenter() {
        list.setOnChildClickListener(new OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                BidBean bidBean = (BidBean) parent.getExpandableListAdapter()
                        .getChild(groupPosition, childPosition);
                if (bidBean != null) {
                    if (dmApp.islogined()) {
                        String bidId = "" + bidBean.getId();
                        DMLog.e("bidbean", bidBean.toString());
                        Intent intent = new Intent(getActivity(),
                                BidDetailActivity.class);
                        intent.putExtra("bidId", bidId);
                        intent.putExtra("bidFlag", bidBean.getFlag());
                        getActivity().startActivity(intent);
                    } else {
                        Intent intent = new Intent(getActivity(),
                                LoginActivity.class);
                        getActivity().startActivity(intent);
                    }
                }
                return false;
            }
        });
        list.setOnGroupClickListener(new OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                getChildData(groupPosition);
                return false;
            }
        });
    }

    private void getChildData(final int position) {
        // 每一类标的列表信息
        HttpParams flBidParams = new HttpParams();// 请求标一类中列表数据
        String flBidUrl = DMConstant.API_Url.BID_PUBLICS_BIDLIST;
        for (int i = 0; i < groupList.size(); i++) {
            flBidParams.put("productId", groupList.get(position).getId());
            HttpUtil.getInstance().post(getContext(), flBidUrl, flBidParams,
                    new HttpCallBack() {
                        @Override
                        public void onSuccess(JSONObject result) {
                            try {
                                JSONArray dataList = result
                                        .getJSONArray("data");
                                childList.get(position).clear();
                                for (int i = 0; i < dataList.length(); i++) {
                                    DMJsonObject record = new DMJsonObject(
                                            dataList.getJSONObject(i)
                                                    .toString());
                                    BidBean bidBean = new BidBean(record);
                                    childList.get(position).add(bidBean);
                                }
                                bidAdapter.notifyDataSetChanged();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        }
    }

    private void getdata() {
        HttpParams flBidParams = new HttpParams();// 请求推荐标数据
        String flBidUrl = DMConstant.API_Url.BID_PUBLIC_FL;
        HttpUtil.getInstance().post(getContext(), flBidUrl, flBidParams,
                new HttpCallBack() {
                    @Override
                    public void onSuccess(JSONObject result) {
                        try {
                            dmSwipeRefreshLayout.setRefreshing(false);
                            JSONArray dataList = result.getJSONArray("data");
                            groupList = new ArrayList<MfenleiBean>();
                            childList = new ArrayList<List<BidBean>>(dataList
                                    .length());
                            DMLog.e("childlist", childList.size() + "");
                            for (int i = 0; i < dataList.length(); i++) {
                                DMJsonObject record = new DMJsonObject(dataList
                                        .getJSONObject(i).toString());
                                MfenleiBean mflBean = new MfenleiBean(record);
                                groupList.add(mflBean);
                                if (i == dataList.length() - 1) {
                                    bidAdapter = new BidListExpandAdapter(
                                            getActivity(), childList, groupList);
                                    list.setAdapter(bidAdapter);
                                }
                                childList.add(new ArrayList<BidBean>());
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Throwable t, Context context) {
                        super.onFailure(t, context);
                        // bidListView.stopLoading();
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
                            netConnectErrorManager.onNetError();
                        }
                    }

                });
    }

    //    @Subscribe(threadMode = ThreadMode.MAIN)
    //    public void onEventMainThread(HMsgEvent event) {
    //		getChildData(event.getMsgCount());
    //		Log.e("OnEventMainThread", event.getMsgCount() + "");
    //		list.expandGroup(event.getMsgCount());
    //		for (int i = 0; i < groupList.size(); i++) {
    //			if (i != event.getMsgCount()) {
    //				list.collapseGroup(i);
    //			}
    //		}
    //	}

    @Override
    public void onRefresh() {
        getdata();
        dmSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onDestroy() {
        getActivity().unregisterReceiver(mbr);
        super.onDestroy();
        //		EventBus.getDefault().unregister(this);
    }

    private class MyBroadcastReceiver extends BroadcastReceiver

    {

        public void onReceive(Context context, Intent intent)

        {
            DMLog.e("onReceive");
            if (intent.getBooleanExtra("isLogin", false)) {
                if (DMApplication.getInstance().getUserInfo().getIsExperienceBid().equals("S")
                        && (!TimeUtils.diffTime(DMApplication.getInstance().getUserInfo()
                        .getRegisterTime()).equals("已经注册超过三天"))) {
                    recommend_bid.setVisibility(View.VISIBLE);
                } else {
                    recommend_bid.setVisibility(View.GONE);
                }
            } else {
                recommend_bid.setVisibility(View.GONE);
            }
            if (-1!= intent.getIntExtra("index",-1)) {
                getChildData(intent.getIntExtra("index", 0));
                list.expandGroup(intent.getIntExtra("index", 0));
                for (int i = 0; i < groupList.size(); i++) {
                    if (i != intent.getIntExtra("index", 0)) {
                        list.collapseGroup(i);
                    }
                }
            }
        }

    }
}
