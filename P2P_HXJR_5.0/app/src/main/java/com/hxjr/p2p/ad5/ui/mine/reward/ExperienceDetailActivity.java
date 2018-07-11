package com.hxjr.p2p.ad5.ui.mine.reward;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.hxjr.p2p.ad5.bean.EXP_STATUS;
import com.hxjr.p2p.ad5.bean.ExperienceInfo;
import com.hxjr.p2p.ad5.ui.BaseActivity;
import com.hxjr.p2p.ad5.utils.DMConstant;
import com.hxjr.p2p.ad5.utils.ErrorUtil;
import com.hxjr.p2p.ad5.utils.FormatUtil;
import com.hxjr.p2p.ad5.utils.HttpParams;
import com.hxjr.p2p.ad5.R;
import com.dm.http.HttpCallBack;
import com.dm.http.HttpUtil;
import com.dm.utils.DMJsonObject;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 体验金信息
 * @author  huangkaibo
 * @date 2015年11月03日
 */
public class ExperienceDetailActivity extends BaseActivity
{
	/**
	 * 借款标题
	 */
	private TextView tvw_lend_title;
	
	/**
	 * 借款金额
	 */
	private TextView tvw_lend_money;
	
	private LinearLayout lly_lend_money;
	
	private View l_lend_money;
	
	/**
	 * 投资金额
	 */
	private TextView tvw_bid_money;
	
	private LinearLayout lly_bid_money;
	
	private View l_bid_money;
	
	/**
	 * 年华利率
	 */
	private TextView tvw_year_rate;
	
	/**
	 * 待赚收益
	 */
	private TextView tvw_wait_money;
	
	private LinearLayout lly_wait_money;
	
	private View l_wait_money;
	
	/**
	 * 已赚收益
	 */
	private TextView tvw_finish_money;
	
	private LinearLayout lly_finish_money;
	
	private View l_finish_money;
	
	/**
	 * 结清时间
	 */
	private TextView tvw_finish_time;
	
	private LinearLayout lly_finish_time;
	
	private View l_finish_time;
	
	/**
	 * 下个还款日
	 */
	private TextView tvw_next_time;
	
	private LinearLayout lly_next_time;
	
	private View l_next_time;
	
	/**
	 * 体验收益期
	 */
	private TextView tvw_income_time;
	
	private LinearLayout lly_income_time;
	
	private View l_income_time;
	
	/**
	 * 体验金状态类型
	 */
	private String type;
	
	/**
	 * 体验金ID
	 */
	private int expId;
	
	private LinearLayout content_ll;
	
	private TextView no_data_tv;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.experience_detail);
		expId = getIntent().getIntExtra("expId", 0);
		type = getIntent().getStringExtra("experience_type");
		initView();
		initData();
	}
	
	@Override
	protected void initView()
	{
		super.initView();
		((TextView)findViewById(R.id.title_text)).setText(R.string.experience_bid_detail);//设置标题
		tvw_lend_title = (TextView)findViewById(R.id.tvw_lend_title);
		content_ll = (LinearLayout)findViewById(R.id.content_ll);
		no_data_tv = (TextView)findViewById(R.id.no_data_tv);
		
		tvw_lend_money = (TextView)findViewById(R.id.tvw_lend_money);
		lly_lend_money = (LinearLayout)findViewById(R.id.lly_lend_money);
		l_lend_money = findViewById(R.id.l_lend_money);
		
		tvw_bid_money = (TextView)findViewById(R.id.tvw_bid_money);
		lly_bid_money = (LinearLayout)findViewById(R.id.lly_bid_money);
		l_bid_money = findViewById(R.id.l_bid_money);
		
		tvw_year_rate = (TextView)findViewById(R.id.tvw_year_rate);
		
		tvw_income_time = (TextView)findViewById(R.id.tvw_income_time);
		lly_income_time = (LinearLayout)findViewById(R.id.lly_income_time);
		l_income_time = findViewById(R.id.l_income_time);
		
		tvw_wait_money = (TextView)findViewById(R.id.tvw_wait_money);
		lly_wait_money = (LinearLayout)findViewById(R.id.lly_wait_money);
		l_wait_money = findViewById(R.id.l_wait_money);
		
		tvw_finish_money = (TextView)findViewById(R.id.tvw_finish_money);
		lly_finish_money = (LinearLayout)findViewById(R.id.lly_finish_money);
		l_finish_money = findViewById(R.id.l_finish_money);
		
		tvw_finish_time = (TextView)findViewById(R.id.tvw_finish_time);
		lly_finish_time = (LinearLayout)findViewById(R.id.lly_finish_time);
		l_finish_time = findViewById(R.id.l_finish_time);
		
		tvw_next_time = (TextView)findViewById(R.id.tvw_next_time);
		lly_next_time = (LinearLayout)findViewById(R.id.lly_next_time);
		l_next_time = findViewById(R.id.l_next_time);
	}
	
	private void initData()
	{
		getExpDetail();
	}
	
	/**
	 * 获取体验金详情
	 */
	private void getExpDetail()
	{
		HttpParams params = new HttpParams();
		params.put("expId", expId);
		params.put("status", type);
		
		HttpUtil.getInstance().post(this, DMConstant.API_Url.MY_EXP_DETAIL, params, new HttpCallBack()
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
						if (dataStr != null && !dataStr.equals(""))
						{
							JSONArray dataList = result.getJSONArray("data");
							DMJsonObject data = new DMJsonObject(dataList.getString(0));
							ExperienceInfo exp = new ExperienceInfo(data);
							setIsShowContent(true);
							setShowByType(exp);
						}
						else
						{
							setIsShowContent(false);
						}
					}
					else
					{
						ErrorUtil.showError(result);
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
			}
			
		});
	}
	
	/**
	 * 设置是否显示内容
	 * @param flag
	 */
	private void setIsShowContent(boolean flag)
	{
		if (flag)
		{
			content_ll.setVisibility(View.VISIBLE);
			no_data_tv.setVisibility(View.GONE);
		}
		else
		{
			content_ll.setVisibility(View.GONE);
			no_data_tv.setVisibility(View.VISIBLE);
		}
	}
	
	/**
	 * 根据不同状态显示体验金详情
	 * @param exp
	 */
	private void setShowByType(ExperienceInfo exp)
	{
		if (EXP_STATUS.YWT.getName().equals(type)) // 使用中
		{
			lly_lend_money.setVisibility(View.GONE);
			l_lend_money.setVisibility(View.VISIBLE);
			lly_bid_money.setVisibility(View.VISIBLE);
			l_bid_money.setVisibility(View.VISIBLE);
			lly_income_time.setVisibility(View.VISIBLE);
			l_income_time.setVisibility(View.VISIBLE);
			lly_wait_money.setVisibility(View.GONE);
			l_wait_money.setVisibility(View.GONE);
			lly_finish_money.setVisibility(View.GONE);
			l_finish_money.setVisibility(View.GONE);
			lly_finish_time.setVisibility(View.GONE);
			l_finish_time.setVisibility(View.GONE);
			lly_next_time.setVisibility(View.GONE);
			l_next_time.setVisibility(View.GONE);
			
			tvw_lend_title.setText(exp.getBidTitile());// 借款标题
			//			tvw_lend_money.setText(getString(R.string.experience_money_unit, exp.getDueInAmount()));// 借款金额
			tvw_bid_money.setText(getString(R.string.experience_money_unit, exp.getExpAmount()));// 投资金额
			tvw_year_rate.setText(FormatUtil.getDMPercent(Double.valueOf(exp.getRate())));// 年化利率
			tvw_income_time.setText(exp.getMonths() + "个月");// 体验收益期
		}
		else if (EXP_STATUS.YJQ.getName().equals(type)) // 已结清
		{
			lly_lend_money.setVisibility(View.VISIBLE);
			l_lend_money.setVisibility(View.VISIBLE);
			lly_bid_money.setVisibility(View.GONE);
			l_bid_money.setVisibility(View.GONE);
			lly_income_time.setVisibility(View.GONE);
			l_income_time.setVisibility(View.GONE);
			lly_wait_money.setVisibility(View.GONE);
			l_wait_money.setVisibility(View.GONE);
			lly_finish_money.setVisibility(View.VISIBLE);
			l_finish_money.setVisibility(View.VISIBLE);
			lly_finish_time.setVisibility(View.VISIBLE);
			l_finish_time.setVisibility(View.VISIBLE);
			lly_next_time.setVisibility(View.GONE);
			l_next_time.setVisibility(View.GONE);
			
			tvw_lend_title.setText(exp.getBidTitile());// 借款标题
			tvw_bid_money.setText(getString(R.string.experience_money_unit, exp.getExpAmount()));// 投资金额
			tvw_year_rate.setText(FormatUtil.getDMPercent(Double.valueOf(exp.getRate())));// 年化利率
			tvw_finish_money.setText(getString(R.string.experience_money_unit, exp.getReceivedAmount()));// 已赚金额
			tvw_finish_time.setText(exp.getReceivedDate());// 结清时间
		}
		else if (EXP_STATUS.YTZ.getName().equals(type)) // 已投资
		{
			lly_lend_money.setVisibility(View.GONE);
			l_lend_money.setVisibility(View.GONE);
			
			lly_bid_money.setVisibility(View.VISIBLE);
			l_bid_money.setVisibility(View.VISIBLE);
			
			lly_income_time.setVisibility(View.GONE);
			l_income_time.setVisibility(View.GONE);
			
			lly_wait_money.setVisibility(View.VISIBLE);
			l_wait_money.setVisibility(View.VISIBLE);
			
			lly_finish_money.setVisibility(View.VISIBLE);
			l_finish_money.setVisibility(View.VISIBLE);
			
			lly_finish_time.setVisibility(View.GONE);
			l_finish_time.setVisibility(View.GONE);
			
			lly_next_time.setVisibility(View.VISIBLE);
			l_next_time.setVisibility(View.VISIBLE);
			
			tvw_lend_title.setText(exp.getBidTitile());// 借款标题
			tvw_bid_money.setText(getString(R.string.experience_money_unit, exp.getExpAmount()));// 投资金额
			tvw_year_rate.setText(FormatUtil.getDMPercent(Double.valueOf(exp.getRate())));// 年化利率
			tvw_wait_money.setText(getString(R.string.experience_money_unit, exp.getDueInAmount()));// 待赚金额
			tvw_finish_money.setText(getString(R.string.experience_money_unit, exp.getReceivedAmount()));// 已赚金额
			if (exp.getNextRepaymentDate().contains(" "))
			{
				tvw_next_time.setText(exp.getNextRepaymentDate().substring(0, exp.getNextRepaymentDate().indexOf(" ")));// 下一还款日
			}
			else
			{
				tvw_next_time.setText(exp.getNextRepaymentDate());
			}
		}
	}
}
