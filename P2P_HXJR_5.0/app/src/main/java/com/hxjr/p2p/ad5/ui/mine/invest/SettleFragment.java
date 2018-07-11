package com.hxjr.p2p.ad5.ui.mine.invest;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dm.http.HttpCallBack;
import com.dm.http.HttpUtil;
import com.dm.utils.DMJsonObject;
import com.dm.utils.DMLog;
import com.dm.widgets.DMListView;
import com.dm.widgets.DMListView.OnMoreListener;
import com.dm.widgets.DMSwipeRefreshLayout;
import com.hxjr.p2p.ad5.R;
import com.hxjr.p2p.ad5.bean.MyInvestSettle;
import com.hxjr.p2p.ad5.utils.DMConstant;
import com.hxjr.p2p.ad5.utils.ErrorUtil;
import com.hxjr.p2p.ad5.utils.HttpParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/***
 * 已结清
 * @author  tangjian
 * @date 2015年11月26日
 */
public class SettleFragment extends Fragment implements OnRefreshListener, OnMoreListener
{
	
	private View mView;
	
	private DMSwipeRefreshLayout dmSwipeRefreshLayout;
	
	private DMListView mListView;
	
	private Context context;
	
	private SettleAdapter adapter;
	
	private List<MyInvestSettle> mList = new ArrayList<MyInvestSettle>(DMConstant.DigitalConstant.TEN);
	
	private int pageNumber = 1;
	
	private static final String STATUS_TYPE = "yjq";// 回款中-hkz;投标中-tbz;已结清-yjq
	
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
		initData();
	}
	
	private void initView()
	{
		dmSwipeRefreshLayout = (DMSwipeRefreshLayout)mView.findViewById(R.id.dmSwipeRefreshLayout);
		dmSwipeRefreshLayout.setOnRefreshListener(this);
		mListView = (DMListView)mView.findViewById(R.id.investListView);
		mListView.setEmptyText("您当前没有已结清的出借项目！");
		mListView.setOnMoreListener(this);
	}
	
	private void initData()
	{
		if (adapter == null)
		{
			adapter = new SettleAdapter(context);
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
					DMLog.i("getList", result.toString());
					String code = result.getString("code");
					if (DMConstant.ResultCode.SUCCESS.equals(code))
					{
						String dataStr = result.getString("data");
						DMLog.e(result.getString("data"));
						if (isHasValue(dataStr))
						{
							JSONArray dataList = result.getJSONArray("data");
							mList.clear();
							for (int i = 0; i < dataList.length(); i++)
							{
								DMJsonObject data = new DMJsonObject(dataList.getString(i));
								MyInvestSettle ms = new MyInvestSettle(data);
								mList.add(ms);
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
	
	/**
	 * 判断data字段是否有值
	 * @param dataStr
	 * @return
	 */
	private boolean isHasValue(String dataStr)
	{
		if (dataStr != null && !dataStr.equals(""))
		{
			return true;
		}
		return false;
	}
	
}
