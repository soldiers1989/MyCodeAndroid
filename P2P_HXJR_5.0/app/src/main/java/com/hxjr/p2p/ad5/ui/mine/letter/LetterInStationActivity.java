package com.hxjr.p2p.ad5.ui.mine.letter;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.hxjr.p2p.ad5.bean.Letter;
import com.hxjr.p2p.ad5.bean.UserInfo;
import com.hxjr.p2p.ad5.ui.BaseActivity;
import com.hxjr.p2p.ad5.utils.DMConstant;
import com.hxjr.p2p.ad5.utils.ErrorUtil;
import com.hxjr.p2p.ad5.utils.HttpParams;
import com.hxjr.p2p.ad5.DMApplication;
import com.hxjr.p2p.ad5.R;
import com.dm.http.HttpCallBack;
import com.dm.http.HttpUtil;
import com.dm.utils.DMJsonObject;
import com.dm.utils.DMLog;
import com.dm.utils.StringUtils;
import com.dm.widgets.DMListView;
import com.dm.widgets.DMListView.OnMoreListener;
import com.dm.widgets.DMSwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

/**
 * 站内信
 * @author  huangkaibo
 * @date 2015年11月04日
 */
public class LetterInStationActivity extends BaseActivity implements OnRefreshListener, OnMoreListener
{
	private DMSwipeRefreshLayout dmSwipeRefreshLayout;
	
	private DMListView mListView;
	
	private LetterAdapter adapter;
	
	private int pageNumber = 1;// 数据分页页码，该参数默认为第一页
	
	private List<Letter> mList = new ArrayList<Letter>();
	
	private  DMApplication dmApp;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_letter_in_station);
		initView();
		initData();
	}
	
	@Override
	protected void initView()
	{
		super.initView();
		((TextView)findViewById(R.id.title_text)).setText(R.string.account_letter_in_station);
		dmSwipeRefreshLayout = (DMSwipeRefreshLayout)findViewById(R.id.dmSwipeRefreshLayout);
		dmSwipeRefreshLayout.setOnRefreshListener(this);
		dmApp=DMApplication.getInstance();
		mListView = (DMListView)findViewById(R.id.letterListView);
		mListView.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				Letter letter = (Letter)parent.getAdapter().getItem(position);
				if (letter != null && mList.get(position) != null)
				{
					String status = mList.get(position).getStatus();
					mList.get(position).setLetterShowing(mList.get(position).isLetterShowing() ? false : true);
					mList.get(position).setStatus("YD");
					adapter.updateList(mList);
					
					if (!StringUtils.isEmpty(status) && !status.equals("YD"))
					{
						HttpParams params = new HttpParams();
						params.put("id", letter.getId());
						postData(DMConstant.API_Url.USER_LETTERS_STATUS, params);
					}
				}
			}
		});
		mListView.setOnMoreListener(this);
	}
	
	protected void postData(String url, HttpParams params)
	{
		HttpUtil.getInstance().post(this, url, params, new HttpCallBack()
		{
			@Override
			public void onSuccess(JSONObject result)
			{
				UserInfo userInfo=dmApp.getUserInfo();
				Intent intent=new Intent("com.myBroadcastReceiver.Inrent2");
				intent.putExtra("num", userInfo.getLetterCount()-1);
				sendBroadcast(intent);
				Log.e("leeMsg", userInfo.getLetterCount()+"");
			}
			
			@Override
			public void onFailure(Throwable t, Context context)
			{
				super.onFailure(t, context);
			}
			
			//			@Override
			//			public void onStart()
			//			{
			//				setShowProgress(false);
			//			}
		});
	}
	
	/**
	 * 初始化数据
	 */
	private void initData()
	{
		if (adapter == null)
		{
			adapter = new LetterAdapter(this, mList);
		}
		mListView.setAdapter(adapter);
		pageNumber = 1;
		getList(pageNumber);
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
	
	private void getList(final int postPage)
	{
		HttpParams httpParams = new HttpParams();
		httpParams.put("pageIndex", postPage);
		httpParams.put("pageSize", DMConstant.DigitalConstant.PAGE_SIZE);
		
		HttpUtil.getInstance().post(this, DMConstant.API_Url.USER_LETTERS, httpParams, new HttpCallBack()
		{
			@Override
			public void onSuccess(JSONObject result)
			{
				try
				{
					DMLog.i("getLetterList", result.toString());
					String code = result.getString("code");
					if (DMConstant.ResultCode.SUCCESS.equals(code))
					{
						dmSwipeRefreshLayout.setRefreshing(false);
						int recordCount = result.getInt("count");// 总记录数
						JSONArray dataList = result.getJSONArray("data");
						List<Letter> letterList = new ArrayList<Letter>();
						if (postPage == 1)
						{
							mList.clear();
							pageNumber = 1;
						}
						for (int i = 0; i < dataList.length(); i++)
						{
							DMJsonObject data = new DMJsonObject(dataList.getString(i));
							Letter letter = new Letter(data);
							letterList.add(letter);
						}
						mList.addAll(letterList);
						adapter.updateList(mList);
						mListView.stopLoading();
						if (postPage == 1 && letterList.size() == 0)
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
						// 失败
						ErrorUtil.showError(result);
						mListView.stopLoading();
						mListView.hasMoreDate(false);
					}
					
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
			
			@Override
			public void onStart()
			{
				super.onStart();
				dmSwipeRefreshLayout.setRefreshing(true);
			}
			
		});
	}
	
}
