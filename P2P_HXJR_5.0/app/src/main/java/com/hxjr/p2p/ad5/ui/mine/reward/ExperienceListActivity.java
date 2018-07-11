package com.hxjr.p2p.ad5.ui.mine.reward;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.hxjr.p2p.ad5.bean.EXP_STATUS;
import com.hxjr.p2p.ad5.bean.ExperienceInfo;
import com.hxjr.p2p.ad5.ui.BaseActivity;
import com.hxjr.p2p.ad5.ui.MainActivity;
import com.hxjr.p2p.ad5.ui.mine.reward.adapter.ExperienceAdapter;
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
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

/**
 * 加息券
 * @author  huruidong
 * @date 2015年11月20日
 */
public class ExperienceListActivity extends BaseActivity implements OnRefreshListener, OnMoreListener
{
	
	private DMSwipeRefreshLayout dmSwipeRefreshLayout;
	
	private DMListView mListView;
	
	private ExperienceAdapter adapter;
	
	private int pageNumber = 1;
	
	private Context mContext;
	
	private List<ExperienceInfo> mList = new ArrayList<ExperienceInfo>(DMConstant.DigitalConstant.TEN);
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_reward_experienc_list);
		mContext = this;
		initView();
	}
	
	@Override
	protected void initView()
	{
		super.initView();
		((TextView)findViewById(R.id.title_text)).setText(R.string.my_reward_experience);
		
		dmSwipeRefreshLayout = (DMSwipeRefreshLayout)findViewById(R.id.dmSwipeRefreshLayout);
		dmSwipeRefreshLayout.setOnRefreshListener(this);
		mListView = (DMListView)findViewById(R.id.list_experience);
		mListView.setOnMoreListener(this);
		mListView.setEmptyText(R.string.hint_no_experience);
		mListView.setOnItemClickListener(new OnItemClickListener()
		{
			
			@Override
			public void onItemClick(AdapterView<?> parent, View arg1, int position, long arg3)
			{
				ExperienceInfo myExp = (ExperienceInfo)parent.getAdapter().getItem(position);
				if (myExp != null)
				{
					if (myExp.getStatus().equals(EXP_STATUS.WSY.getName())) // 未使用
					{
						//去投资
						MainActivity.index = 1;
						AppManager.getAppManager().finishActivity(MyRewardActivity.class);
						finish();
					}
					else if (myExp.getStatus().equals(EXP_STATUS.YGQ.getName())) // 已过期
					{
						return;
					}
					else
					{
						Intent intent = new Intent(mContext, ExperienceDetailActivity.class);
						intent.putExtra("expId", myExp.getExperienceId());
						intent.putExtra("experience_type", myExp.getStatus());
						startActivity(intent);
					}
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
			adapter = new ExperienceAdapter(this);
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
		
		HttpUtil.getInstance().post(this, DMConstant.API_Url.MY_EXP_LIST, params, new HttpCallBack()
		{
			@Override
			public void onSuccess(JSONObject result)
			{
				try
				{
					dmSwipeRefreshLayout.setRefreshing(false);
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
								ExperienceInfo exp = new ExperienceInfo(data);
								mList.add(exp);
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
