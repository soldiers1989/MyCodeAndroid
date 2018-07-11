package com.hxjr.p2p.ad5.ui.mine.creditor;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hxjr.p2p.ad5.bean.MyCreditorAssignment;
import com.hxjr.p2p.ad5.utils.DMConstant;
import com.hxjr.p2p.ad5.utils.ErrorUtil;
import com.hxjr.p2p.ad5.utils.HttpParams;
import com.hxjr.p2p.ad5.R;
import com.dm.http.HttpCallBack;
import com.dm.http.HttpUtil;
import com.dm.utils.DMJsonObject;
import com.dm.utils.DMLog;
import com.dm.utils.StringUtils;
import com.dm.widgets.DMListView;
import com.dm.widgets.DMListView.OnMoreListener;
import com.dm.widgets.DMSwipeRefreshLayout;

/**
 * 债权转让-已转出
 * @author  huangkaibo
 * @date 2015年12月7日
 */
public class CreditorTurnOutFragment extends Fragment implements OnRefreshListener, OnMoreListener
{
	protected View mView;
	
	private Context context;
	
	private DMSwipeRefreshLayout dmSwipeRefreshLayout;
	
	private DMListView mListView;
	
	private CreditorTurnOutAdapter adapter;
	
	private int pageNumber = 1;
	
	private List<MyCreditorAssignment> mList = new ArrayList<MyCreditorAssignment>(DMConstant.DigitalConstant.TEN);
	
	private static final String STATUS_TYPE = "yzc";// 转让中-zrz;已转出-yzc;已转入-yzr
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		mView = inflater.inflate(R.layout.my_creditor_list, container, false);
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
		mListView = (DMListView)mView.findViewById(R.id.myCreditorListView);
		mListView.setEmptyText("您当前没有已转出的债权！");
		mListView.setOnMoreListener(this);
	}
	
	private void initData()
	{
		if (adapter == null)
		{
			adapter = new CreditorTurnOutAdapter(context);
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
		httpParams.put("type", STATUS_TYPE);
		
		HttpUtil.getInstance().post(getContext(), DMConstant.API_Url.MY_CREDITOR_ASSIGNMENT_LIST, httpParams, new HttpCallBack()
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
						if (!StringUtils.isEmpty(dataStr))
						{
							JSONArray dataList = result.getJSONArray("data");
							mList.clear();
							for (int i = 0; i < dataList.length(); i++)
							{
								DMJsonObject data = new DMJsonObject(dataList.getString(i));
								MyCreditorAssignment mb = new MyCreditorAssignment(data, true);
								mList.add(mb);
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
