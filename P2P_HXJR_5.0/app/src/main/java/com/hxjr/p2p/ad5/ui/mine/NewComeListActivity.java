package com.hxjr.p2p.ad5.ui.mine;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.hxjr.p2p.ad5.bean.News;
import com.hxjr.p2p.ad5.ui.BaseActivity;
import com.hxjr.p2p.ad5.ui.discovery.news.NewsDetailActivity;
import com.hxjr.p2p.ad5.utils.DMConstant;
import com.hxjr.p2p.ad5.utils.ErrorUtil;
import com.hxjr.p2p.ad5.utils.HttpParams;
import com.hxjr.p2p.ad5.R;
import com.dm.http.HttpCallBack;
import com.dm.http.HttpUtil;
import com.dm.utils.DMJsonObject;
import com.dm.widgets.DMListView;
import com.dm.widgets.DMListView.OnMoreListener;
import com.dm.widgets.DMSwipeRefreshLayout;

/**
 * 新手指引
 * @author jiaohongyun
 *
 */
public class NewComeListActivity extends BaseActivity implements OnRefreshListener, OnMoreListener
{
	private DMSwipeRefreshLayout dmSwipeRefreshLayout;
	
	private DMListView mListView;
	
	private NewComeListAdapter adapter;
	
	private int pageNumber = 1;
	
	List<News> newsList = new ArrayList<News>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_come_list_activity);
		initView();
		initData();
	}
	
	@Override
	protected void initView()
	{
		super.initView();
		((TextView)findViewById(R.id.title_text)).setText(R.string.new_come_help);
		dmSwipeRefreshLayout = (DMSwipeRefreshLayout)findViewById(R.id.dmSwipeRefreshLayout);
		dmSwipeRefreshLayout.setOnRefreshListener(this);
		mListView = (DMListView)findViewById(R.id.newsListView);
		mListView.setOnMoreListener(this);
		mListView.setOnItemClickListener(new OnItemClickListener()
		{
			
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				News news = (News)parent.getAdapter().getItem(position);
				
				if (news != null)
				{
					Intent intent = new Intent(NewComeListActivity.this, NewsDetailActivity.class);
					intent.putExtra("title", NewComeListActivity.this.getResources().getString(R.string.new_come_help));
					intent.putExtra("noticeId", news.getId());
					intent.putExtra("newsType", "XSZY");//新手指引
					startActivity(intent);
				}
			}
		});
		
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
			adapter = new NewComeListAdapter(this, false);
		}
		mListView.setAdapter(adapter);
		pageNumber = 1;
		getListData(pageNumber);
	}
	
	private void getListData(final int number)
	{
		HttpParams httpParams = new HttpParams();
		httpParams.put("pageIndex", number);
		httpParams.put("pageSize", DMConstant.DigitalConstant.PAGE_SIZE);
		httpParams.put("type", "XSZY");// 新手指引
		
		HttpUtil.getInstance().post(this, DMConstant.API_Url.ARTICLE_LIST, httpParams, new HttpCallBack()
		{
			
			@Override
			public void onSuccess(JSONObject result)
			{
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
							adapter.clearList();
							pageNumber = 1;
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
				mListView.hasMoreDate(false);
				dmSwipeRefreshLayout.setRefreshing(false);
			}
			
		});
	}
	
	@Override
	public void onMore()
	{
		getListData(pageNumber);
	}
}
