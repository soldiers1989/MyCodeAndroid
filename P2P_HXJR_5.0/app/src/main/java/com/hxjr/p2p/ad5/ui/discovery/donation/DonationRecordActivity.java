package com.hxjr.p2p.ad5.ui.discovery.donation;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.hxjr.p2p.ad5.bean.GyRecordList;
import com.hxjr.p2p.ad5.ui.BaseActivity;
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

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

/**
 * 捐赠记录
 * @author  huangkaibo
 * @date 2015年11月13日
 */
public class DonationRecordActivity extends BaseActivity implements OnRefreshListener, OnMoreListener
{
	
	private DMSwipeRefreshLayout dmSwipeRefreshLayout;
	
	private DMListView mListView;
	
	private DonationRecordAdapter adapter;
	
	private int pageNumber = 1;// 数据分页页码，该参数默认为第一页
	
	private String bidId = "0";
	
	private List<GyRecordList> gyRecords = new ArrayList<GyRecordList>(DMConstant.DigitalConstant.TEN);
	
	private View recordHeaderView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.donation_record_list);
		bidId = getIntent().getStringExtra("bidId");
		initView();
	}
	
	@Override
	protected void initView()
	{
		super.initView();
		((TextView)findViewById(R.id.title_text)).setText(R.string.donation_record);
		dmSwipeRefreshLayout = (DMSwipeRefreshLayout)findViewById(R.id.dmSwipeRefreshLayout);
		dmSwipeRefreshLayout.setOnRefreshListener(this);
		mListView = (DMListView)findViewById(R.id.recordListView);
		recordHeaderView = LayoutInflater.from(this).inflate(R.layout.donation_record_list_header, mListView, false);
		mListView.addHeaderView(recordHeaderView);
		mListView.setEmptyText("暂无捐赠记录");
		mListView.setOnMoreListener(this);
		initData();
	}
	
	/**
	 * 初始化数据
	 */
	private void initData()
	{
		if (adapter == null)
		{
			adapter = new DonationRecordAdapter(this);
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
	
	private void getList(final int number)
	{
		HttpParams httpParams = new HttpParams();
		httpParams.put("pageIndex", number);
		httpParams.put("pageSize", DMConstant.DigitalConstant.PAGE_SIZE);
		httpParams.put("bidId", bidId);
		
		HttpUtil.getInstance().post(this, DMConstant.API_Url.GY_LOAN_RECORD, httpParams, new HttpCallBack()
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
						if (isEmpty(dataStr))
						{
							JSONArray dataList = result.getJSONArray("data");
							gyRecords.clear();
							for (int i = 0; i < dataList.length(); i++)
							{
								DMJsonObject data = new DMJsonObject(dataList.getString(i));
								GyRecordList record = new GyRecordList(data);
								gyRecords.add(record);
							}
							if (number == 1)
							{
								pageNumber = 1;
								adapter.clearList();
							}
							adapter.addAll(gyRecords);
							mListView.stopLoading();
							if (number == 1 && gyRecords.size() == 0)
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
							mListView.stopLoading();
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
			
		});
	}
	
	private boolean isEmpty(String dataStr)
	{
		if (dataStr != null && !dataStr.equals(""))
		{
			return true;
		}
		return false;
	}
}
