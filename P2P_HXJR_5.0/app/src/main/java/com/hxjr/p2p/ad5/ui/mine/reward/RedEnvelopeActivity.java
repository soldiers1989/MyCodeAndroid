package com.hxjr.p2p.ad5.ui.mine.reward;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.hxjr.p2p.ad5.bean.RewardInfo;
import com.hxjr.p2p.ad5.ui.BaseActivity;
import com.hxjr.p2p.ad5.ui.MainActivity;
import com.hxjr.p2p.ad5.ui.mine.reward.adapter.RedEnvelopeAdapter;
import com.hxjr.p2p.ad5.utils.DMConstant;
import com.hxjr.p2p.ad5.utils.ErrorUtil;
import com.hxjr.p2p.ad5.utils.HttpParams;
import com.hxjr.p2p.ad5.R;
import com.dm.http.HttpCallBack;
import com.dm.http.HttpUtil;
import com.dm.utils.AppManager;
import com.dm.utils.DMJsonObject;
import com.dm.widgets.DMListView;
import com.dm.widgets.DMListView.OnMoreListener;
import com.dm.widgets.DMSwipeRefreshLayout;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

/**
 * 红包列表
 * @author  huangkaibo
 * @date 2015年12月2日
 */
public class RedEnvelopeActivity extends BaseActivity implements OnRefreshListener, OnMoreListener
{
	/**
	 * 未使用
	 */
	public static final String REDENVELOPE_TYPE_USE_NO = "WSY";
	
	/**
	 * 已使用
	 */
	public static final String REDENVELOPE_TYPE_USED = "YSY";
	
	/**
	 * 已过期
	 */
	public static final String REDENVELOPE_TYPE_OVERDUE = "YGQ";
	
	public static final String REWARD_TYPE = "1";// 奖励类型：1-红包；2-加息券
	
	private DMSwipeRefreshLayout dmSwipeRefreshLayout;
	
	private DMListView mListView;
	
	private RedEnvelopeAdapter adapter;
	
	private int pageNumber = 1;
	
	private List<RewardInfo> mList = new ArrayList<RewardInfo>(DMConstant.DigitalConstant.TEN);
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.red_envelope_list);
		initView();
	}
	
	@Override
	protected void initView()
	{
		super.initView();
		((TextView)findViewById(R.id.title_text)).setText(R.string.my_reward_red);
		dmSwipeRefreshLayout = (DMSwipeRefreshLayout)findViewById(R.id.dmSwipeRefreshLayout);
		dmSwipeRefreshLayout.setOnRefreshListener(this);
		mListView = (DMListView)findViewById(R.id.redListView);
		mListView.setOnMoreListener(this);
		mListView.setEmptyText(R.string.hint_no_red);
		mListView.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				RewardInfo rewardInfo = (RewardInfo)parent.getAdapter().getItem(position);
				if (rewardInfo != null && rewardInfo.getStatus().equals(RedEnvelopeActivity.REDENVELOPE_TYPE_USE_NO))
				{
					//去投资
					MainActivity.index = 1;
					AppManager.getAppManager().finishActivity(MyRewardActivity.class);
					finish();
				}
			}
		});
		initData();
	}
	
	@Override
	public void onRefresh()
	{
		getList(1);
		dmSwipeRefreshLayout.setRefreshing(false);
	}
	
	/**
	 * 初始化数据
	 */
	private void initData()
	{
		if (adapter == null)
		{
			adapter = new RedEnvelopeAdapter(this);
		}
		mListView.setAdapter(adapter);
		pageNumber = 1;
		getList(pageNumber);
	}
	
	private void getList(final int number)
	{
		HttpParams params = new HttpParams();
		params.put("pageIndex", number);
		params.put("pageSize", DMConstant.DigitalConstant.PAGE_SIZE);
		params.put("type", REWARD_TYPE);
		
		HttpUtil.getInstance().post(this, DMConstant.API_Url.MY_REWARD_LIST, params, new HttpCallBack()
		{
			@Override
			public void onSuccess(JSONObject result)//请求成功   已经是主线程，可以在这里进行UI操作
			{
				try
				{
					dmSwipeRefreshLayout.setRefreshing(false);
					mListView.stopLoading();
					String code = result.getString("code");
					if (DMConstant.ResultCode.SUCCESS.equals(code))
					{
						String dataStr = result.getString("data");
						
						if (dataStr != null && !dataStr.equals(""))
						{
							JSONArray dataList = result.getJSONArray("data");
							mList.clear();
							for (int i = 0; i < dataList.length(); i++)
							{
								DMJsonObject data = new DMJsonObject(dataList.getString(i));
								RewardInfo reward = new RewardInfo(data);
								mList.add(reward);
							}
							
							if (number == 1)
							{
								adapter.clearList();
								pageNumber = 1;
							}
							
							adapter.addAll(mList);
							mListView.stopLoading();
							if (number == 1 && mList.size() == 0)
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
					}
				}
				catch (JSONException e)
				{
					e.printStackTrace();
				}
			}
			
			@Override
			public void onFailure(Throwable t, Context context) //请求成功失败   已经是主线程，可以在这里进行UI操作
			{
				super.onFailure(t, context);
				mListView.stopLoading();
				dmSwipeRefreshLayout.setRefreshing(false);
			}
		});
	}
	
	@Override
	public void onMore()
	{
		getList(pageNumber);
	}
	
}
