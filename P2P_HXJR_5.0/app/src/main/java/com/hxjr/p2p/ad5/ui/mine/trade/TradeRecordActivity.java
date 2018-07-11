package com.hxjr.p2p.ad5.ui.mine.trade;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.hxjr.p2p.ad5.bean.TradeRecord;
import com.hxjr.p2p.ad5.ui.BaseActivity;
import com.hxjr.p2p.ad5.utils.DMConstant;
import com.hxjr.p2p.ad5.utils.ErrorUtil;
import com.hxjr.p2p.ad5.utils.HttpParams;
import com.hxjr.p2p.ad5.R;
import com.dm.http.HttpCallBack;
import com.dm.http.HttpUtil;
import com.dm.utils.DMJsonObject;
import com.dm.utils.DMLog;
import com.dm.widgets.DMListView;
import com.dm.widgets.DMListView.OnMoreListener;
import com.dm.widgets.DMSwipeRefreshLayout;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.widget.TextView;

/**
 * 交易记录
 * @author  huangkaibo
 * @date 2015-11-2
 */
public class TradeRecordActivity extends BaseActivity implements OnRefreshListener, OnMoreListener
{
	private DMSwipeRefreshLayout dmSwipeRefreshLayout;
	
	private DMListView mListView;
	
	private TradeRecordAdapter adapter;
	
	private int pageNumber = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.trade_record);
		initView();
	}
	
	@Override
	protected void initView()
	{
		super.initView();
		((TextView)findViewById(R.id.title_text)).setText(R.string.trade_record);//设置标题
		dmSwipeRefreshLayout = (DMSwipeRefreshLayout)findViewById(R.id.dmSwipeRefreshLayout);
		dmSwipeRefreshLayout.setOnRefreshListener(this);
		mListView = (DMListView)findViewById(R.id.tradeListView);
		mListView.setEmptyText("您当前没有交易记录！");
		mListView.setOnMoreListener(this);
		initData();
	}
	
	private void initData()
	{
		if (adapter == null)
		{
			adapter = new TradeRecordAdapter(this);
		}
		mListView.setAdapter(adapter);
		pageNumber = 1;
		getTradeList(pageNumber);
	}
	
	@Override
	public void onMore()
	{
		getTradeList(pageNumber);
	}
	
	@Override
	public void onRefresh()
	{
		getTradeList(1);
		dmSwipeRefreshLayout.setRefreshing(false);
	}
	
	private void getTradeList(final int number)
	{
		HttpParams params = new HttpParams();
		params.put("pageIndex", number);
		params.put("pageSize", DMConstant.DigitalConstant.PAGE_SIZE);
		
		HttpUtil.getInstance().post(this, DMConstant.API_Url.USER_TRANLOGLIST, params, new HttpCallBack()
		{
			@Override
			public void onSuccess(JSONObject result)
			{
				try
				{
					DMLog.i("getTradeList", result.toString());
					String code = result.getString("code");
					if (DMConstant.ResultCode.SUCCESS.equals(code))
					{
						JSONArray dataList = result.getJSONArray("data");
						List<TradeRecord> tradeList = new ArrayList<TradeRecord>();
						for (int i = 0; i < dataList.length(); i++)
						{
							DMJsonObject data = new DMJsonObject(dataList.getString(i));
							TradeRecord trecord = new TradeRecord(data);
							tradeList.add(trecord);
						}
						if (number == 1)
						{
							adapter.removeAll();
							pageNumber = 1;
						}
						adapter.addAll(tradeList);
						mListView.stopLoading();
						if (number == 1 && tradeList.size() == 0)
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
}
