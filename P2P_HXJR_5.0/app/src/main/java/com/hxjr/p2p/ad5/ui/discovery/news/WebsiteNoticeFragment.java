package com.hxjr.p2p.ad5.ui.discovery.news;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.hxjr.p2p.ad5.bean.HomeNotice;
import com.hxjr.p2p.ad5.ui.discovery.news.adapter.NoticeAdapter;
import com.hxjr.p2p.ad5.ui.home.NoticeActivity;
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

/**
 * 网站公告
 * @author  huangkaibo
 * @date 2015年11月27日
 */
public class WebsiteNoticeFragment extends Fragment implements OnRefreshListener, OnMoreListener
{
	
	private View mView;
	
	private DMSwipeRefreshLayout dmSwipeRefreshLayout;
	
	private DMListView mListView;
	
	private NoticeAdapter adapter;
	
	private int pageNumber = 1;
	
	List<HomeNotice> newsList = new ArrayList<HomeNotice>();
	
	private NetConnectErrorManager netConnectErrorManager;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		mView = inflater.inflate(R.layout.media_list, container, false);
		return mView;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		dmSwipeRefreshLayout = (DMSwipeRefreshLayout)mView.findViewById(R.id.dmSwipeRefreshLayout);
		dmSwipeRefreshLayout.setOnRefreshListener(this);
		mListView = (DMListView)mView.findViewById(R.id.newsListView);
		mListView.setEmptyText("暂时没有网站公告！");
		mListView.setOnMoreListener(this);
		mListView.setOnItemClickListener(new OnItemClickListener()
		{
			
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				HomeNotice news = (HomeNotice)parent.getAdapter().getItem(position);
				
				if (news != null)
				{
					//公告详情
					Intent intent = new Intent(getContext(), NoticeActivity.class);
					intent.putExtra("title", "网站公告");
					intent.putExtra("noticeId", news.getId());
					startActivity(intent);
				}
			}
		});
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
		dmSwipeRefreshLayout.setRefreshing(false);
		getListData(1);
	}
	
	/**
	 * 初始化数据
	 */
	private void initData()
	{
		if (adapter == null)
		{
			adapter = new NoticeAdapter(getActivity(), false);
		}
		mListView.setAdapter(adapter);
		pageNumber = 1;
		getListData(pageNumber);
	}
	
	private void getListData(final int postPage)
	{
		HttpParams httpParams = new HttpParams();
		httpParams.put("pageIndex", postPage);
		httpParams.put("pageSize", DMConstant.DigitalConstant.PAGE_SIZE);
		
		HttpUtil.getInstance().post(getContext(), DMConstant.API_Url.NOTICE_LIST, httpParams, new HttpCallBack()
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
					DMLog.i("getMediaList", result.toString());
					String code = result.getString("code");
					if (DMConstant.ResultCode.SUCCESS.equals(code))
					{
						JSONArray dataList = result.getJSONArray("data");
						newsList.clear();
						for (int i = 0; i < dataList.length(); i++)
						{
							DMJsonObject data = new DMJsonObject(dataList.getJSONObject(i).toString());
							HomeNotice bean = new HomeNotice(data);
							newsList.add(bean);
						}
						if (postPage == 1)
						{//第一次或者刷新都会进来
							pageNumber = 1;
							adapter.clearList();
						}
						adapter.addAll(newsList);
						mListView.stopLoading();
						if (postPage == 1 && newsList.size() == 0)
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
						mListView.hasMoreDate(false);
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
	
	@Override
	public void onMore()
	{
		getListData(pageNumber);
	}
}
