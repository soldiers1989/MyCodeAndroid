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

import com.hxjr.p2p.ad5.bean.News;
import com.hxjr.p2p.ad5.ui.discovery.news.adapter.NewsAdapter;
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
import com.dm.widgets.DMListView;
import com.dm.widgets.DMListView.OnMoreListener;
import com.dm.widgets.DMSwipeRefreshLayout;

/**
 * 媒体报道
 * @author  huangkaibo
 * @date 2015年11月27日
 */
public class MediaReportFragment extends Fragment implements OnRefreshListener, OnMoreListener
{
	
	private View mView;
	
	private Context context;
	
	private DMSwipeRefreshLayout dmSwipeRefreshLayout;
	
	private DMListView mListView;
	
	private NewsAdapter adapter;
	
	private int pageNumber = 1;
	
	List<News> newsList = new ArrayList<News>();
	
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
		mListView.setEmptyText("暂时没有法律法规报道！");
		mListView.setOnMoreListener(this);
		context = getActivity();
		mListView.setOnItemClickListener(new OnItemClickListener()
		{
			
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				News news = (News)parent.getAdapter().getItem(position);
				
				if (news != null)
				{
					Intent intent = new Intent(context, NewsDetailActivity.class);
					intent.putExtra("title", "法律法规");
					intent.putExtra("noticeId", news.getId());
					intent.putExtra("newsType", "FLFG");// 媒休报道
					getActivity().startActivity(intent);
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
		pageNumber = 1;
		dmSwipeRefreshLayout.setRefreshing(false);
		getListData(pageNumber);
	}
	
	/**
	 * 初始化数据
	 */
	private void initData()
	{
		if (adapter == null)
		{
			adapter = new NewsAdapter(getActivity(), false);
		}
		mListView.setAdapter(adapter);
		pageNumber = 1;
		getListData(pageNumber);
	}
	
	private void getListData(final int number)
	{
		HttpParams httpParams = new HttpParams();
		httpParams.put("pageIndex", pageNumber);
		httpParams.put("pageSize", DMConstant.DigitalConstant.PAGE_SIZE);
		httpParams.put("type", "FLFG");// 媒休报道
		
		HttpUtil.getInstance().post(getContext(), DMConstant.API_Url.ARTICLE_LIST, httpParams, new HttpCallBack()
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
					String code = result.getString("code");
					if (DMConstant.ResultCode.SUCCESS.equals(code))
					{
						JSONArray dataList = result.getJSONArray("data");
						newsList.clear();
						for (int i = 0; i < dataList.length(); i++)
						{
							DMJsonObject data = new DMJsonObject(dataList.getString(i));
							News news = new News(data);
							newsList.add(news);
						}
						if (number == 1)
						{
							pageNumber = 1;
							adapter.clearList();
						}
						adapter.addAll(newsList);
						mListView.stopLoading();
						if (number == 1 && newsList.size() == 0)
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
