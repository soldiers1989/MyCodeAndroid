package com.hxjr.p2p.ad5.ui.mine.bank;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dm.http.HttpCallBack;
import com.dm.http.HttpUtil;
import com.dm.utils.DMJsonObject;
import com.dm.utils.DMLog;
import com.dm.utils.DensityUtil;
import com.dm.utils.StringUtils;
import com.dm.widgets.swipemenulv.SwipeMenu;
import com.dm.widgets.swipemenulv.SwipeMenuCreator;
import com.dm.widgets.swipemenulv.SwipeMenuItem;
import com.dm.widgets.swipemenulv.SwipeMenuListView;
import com.dm.widgets.swipemenulv.SwipeMenuListView.OnMenuItemClickListener;
import com.dm.widgets.utils.AlertDialogUtil;
import com.dm.widgets.utils.AlertDialogUtil.AlertListener;
import com.dm.widgets.utils.AlertDialogUtil.ConfirmListener;
import com.dm.widgets.utils.ToastUtil;
import com.hxjr.p2p.ad5.DMApplication;
import com.hxjr.p2p.ad5.R;
import com.hxjr.p2p.ad5.bean.BankCard;
import com.hxjr.p2p.ad5.bean.Fee;
import com.hxjr.p2p.ad5.bean.UserInfo;
import com.hxjr.p2p.ad5.service.ApiUtil;
import com.hxjr.p2p.ad5.service.ApiUtil.OnGetUserInfoCallBack;
import com.hxjr.p2p.ad5.service.ApiUtil.OnPostCallBack;
import com.hxjr.p2p.ad5.ui.BaseActivity;
import com.hxjr.p2p.ad5.ui.tg.TgThirdWebActivity;
import com.hxjr.p2p.ad5.utils.DMConstant;
import com.hxjr.p2p.ad5.utils.ErrorUtil;
import com.hxjr.p2p.ad5.utils.HttpParams;
import com.hxjr.p2p.ad5.utils.UIHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author  tangjian
 * @date 2015-6-4
 */
public class BankCardManageActivity extends BaseActivity
{
	private SwipeMenuListView listView;
	
	private BankCardAdapter adapter;
	
	private TextView addBankImg;
	
	private TextView tip_no_card_tv;
	
	private View bank_card_line;
	
	//未注册第三方
	private LinearLayout unregist_third_ll;
	
	private LinearLayout reminder_ll;
	private TextView unregist_third_tv;
	
	private TextView reminder_tv;
	private Button unregist_third_btn;
	
	private boolean isChoose = false;
	
	private HttpParams httpParams;
	
	private final static int YB_CODE_BIND_CARD = 1;
	private String wxts;
	private final static int YB_REQUEST_CODE_RECHAGE = 2;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bank_card_manage_activity);
		initView();
		initListener();
	}
	
	@Override
	protected void initView()
	{
		super.initView();
		
		isChoose = getIntent().getBooleanExtra("isChoose", false);
		
		((TextView)findViewById(R.id.title_text)).setText(isChoose ? R.string.account_bank_card_choose
			: R.string.account_bank_card_manage);
		
		addBankImg = (TextView)findViewById(R.id.btn_right);
		setTitleRightIcon(addBankImg);
		
		tip_no_card_tv = (TextView)findViewById(R.id.tip_no_card_tv);
		reminder_tv = (TextView)findViewById(R.id.reminder_tv);
		bank_card_line = findViewById(R.id.bank_card_line);
		listView = (SwipeMenuListView)findViewById(R.id.bank_card_lv);
		//未注册第三方
		unregist_third_ll = (LinearLayout)findViewById(R.id.unregist_third_ll);
		reminder_ll = (LinearLayout)findViewById(R.id.reminder_ll);
		unregist_third_tv = (TextView)findViewById(R.id.unregist_third_tv);
		unregist_third_btn = (Button)findViewById(R.id.unregist_third_btn);
		
//		initMenuListView();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		switch (requestCode)
		{
			case YB_REQUEST_CODE_RECHAGE:

				break;
			default:
				break;
		}
	}
	
	/**
	 * 设置头部title里面右边TextView的icon图标
	 */
	@SuppressWarnings("deprecation")
	protected void setTitleRightIcon(TextView textView)
	{
		Drawable right = getResources().getDrawable(R.drawable.icon_add);
		right.setBounds(0, 0, right.getMinimumWidth(), right.getMinimumHeight());
		textView.setCompoundDrawables(null, null, right, null);
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		ApiUtil.getUserInfo(this, new OnGetUserInfoCallBack()
		{
			@Override
			public void onSuccess()
			{
				switchShowView();
			}
			
			@Override
			public void onFailure()
			{
				switchShowView();
			}
		});
	}
	
	/***
	 * 选择如何显示界面
	 */
	protected void switchShowView()
	{
		UserInfo userInfo = DMApplication.getInstance().getUserInfo();
		if (null != userInfo && userInfo.isTg() && StringUtils.isEmptyOrNull(userInfo.getUsrCustId()))
		{//如果没有注册第三方
			unregist_third_ll.setVisibility(View.VISIBLE);
			addBankImg.setVisibility(View.GONE);
			reminder_ll.setVisibility(View.GONE);
		}
		else
		{
			queryFee();
			getBankCardDatas();
			unregist_third_ll.setVisibility(View.GONE);
			reminder_ll.setVisibility(View.VISIBLE);
//			if (DMApplication.getInstance().getUserInfo().getIsExperienceBid().equals("F")&&TimeUtils.diffTime(DMApplication.getInstance().getUserInfo().getRegisterTime()).equals("注册未超过6小时")) {
//				HttpParams httpParams = new HttpParams();
//				httpParams.put("isExperienceBid ", "F" );
//				HttpUtil.getInstance().post(DMApplication.getInstance(), DMConstant.API_Url.UPDATE_EXPERENCEBID, httpParams, new HttpCallBack()
//				{
//
//					@Override
//					public void onSuccess(JSONObject result)
//					{
//						Intent intent = new Intent(BankCardManageActivity.this,TextMainActivity.class);
//						startActivity(intent);
//				        ApiUtil.getUserInfo(BankCardManageActivity.this);
//				        BankCardManageActivity.this.finish();
//					}
//
//					@Override
//					public void onStart()
//					{
//						setShowProgress(true);
//					}
//
//					@Override
//					public void onFailure(Throwable t, Context context)
//					{
//					}
//				});
//				}
//			addBankImg.setVisibility(View.VISIBLE);
		}
	}
	
	/**
	 * 提现是否需要交易密码
	 */
	private boolean isNeedPsw;
	
	/***
	 * 是否必须完成邮箱认证
	 */
	private boolean isNeedEmailRZ;
	
	private int maxBankCardNum;
	
	/**
	 * 查询提现手续费和充值手续费
	 */
	private void queryFee()
	{
		ApiUtil.getFee(this, new OnPostCallBack()
		{
			@Override
			public void onSuccess(Fee fee)
			{
				isNeedPsw = fee.getChargep().isNeedPsd();
				isNeedEmailRZ = fee.getBaseInfo().isNeedEmailRZ();
				maxBankCardNum = fee.getBaseInfo().getMaxBankCardNum();
				// 按maxbanks做处理
				//				if (maxBankCardNum > 0)
				//				{
				//					maxbanks = maxBankCardNum;
				//				}
			}
			
			@Override
			public void onFailure()
			{
				isNeedPsw = true;
				isNeedEmailRZ = true;
				maxBankCardNum = 1;
			}
		});
	}
	
	/**
	 * 处理点击事件
	 */
	private void initListener()
	{
		listView.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				BankCard card = (BankCard)parent.getAdapter().getItem(position);
				if (card != null)
				{
					if (isChoose)
					{
						Intent intent = new Intent();
						intent.putExtra("cardId", card.getId() + "");
						setResult(1, intent);
						finish();
					}
					else
					{
						Intent intent = new Intent(BankCardManageActivity.this, BankCardInfoActivity.class);
						intent.putExtra("cardInfo", card);
						BankCardManageActivity.this.startActivity(intent);
					}
				}
			}
		});
		
		addBankImg.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if (checkClick(v.getId())) //防重复点击	
				{
					if ("".equals(DMApplication.getInstance().getUserInfo().getUsrCustId())){
						postPayRegister(DMConstant.API_Url.PAY_USER_REGISTERN,new HttpParams());
					}
					else{
						Intent intent = new Intent(BankCardManageActivity.this, ThirdBankCardActivity.class);
						startActivity(intent);
						finish();
					}

//					if (checkSecurityInfo())
//					{
//						Intent intent = new Intent(BankCardManageActivity.this, BankCardWebActivity.class);
//						startActivity(intent);
////						httpParams = new HttpParams();
////						post(DMConstant.API_Url.PAY_BIND_CARD, httpParams, YB_CODE_BIND_CARD);
//					}
				}
			}
		});
		
		unregist_third_btn.setOnClickListener(new OnClickListener()
		{//跳转到注册第三方界面
			@Override
			public void onClick(View v)
			{
				if ("".equals(DMApplication.getInstance().getUserInfo().getUsrCustId())){
					postPayRegister(DMConstant.API_Url.PAY_USER_REGISTERN,new HttpParams());
				}
				else{
					Intent intent = new Intent(BankCardManageActivity.this, ThirdBankCardActivity.class);
					startActivity(intent);
					finish();
				}
//				UserInfo userInfo = DMApplication.getInstance().getUserInfo();
//				if (UIHelper.hasCompletedSecurityInfo(BankCardManageActivity.this, userInfo, isNeedEmailRZ, isNeedPsw))
//				{
//					startActivity(new Intent(BankCardManageActivity.this, BankCardWebActivity.class));
//				}
			}
		});
	}
	
	
	private void postPayRegister(String url, HttpParams params)
	{
		HttpUtil.getInstance().post(BankCardManageActivity.this, url, params, new HttpCallBack()
		{



			@Override
			public void onSuccess(JSONObject result) {
				try {
					DMLog.e(result.toString());
					String code = result.getString("code");
					if (DMConstant.ResultCode.SUCCESS.equals(code)) {
						// 成功
						DMJsonObject data=new DMJsonObject(result.getString("data"));
						String url=data.getString("url");
						Intent intent=new Intent();
						intent.putExtra("linkUrl",url);
						intent.putExtra("title","第三方注册");
						intent.setClass(BankCardManageActivity.this,BankCardWebActivity.class);
						startActivityForResult(intent,1);
					}else{
						String description=result.getString("description");
						ToastUtil.getInstant().show(BankCardManageActivity.this,description);
					}
					BankCardManageActivity.this.finish();
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	/**
	 * 易宝绑卡
	 * @param result
	 */
	protected void doAfterGetYBBindCard(JSONObject result)
	{
		try
		{
			String code = result.getString("code");
			if (DMConstant.ResultCode.SUCCESS.equals(code))
			{
				JSONObject jsonObject = result.getJSONObject("data");
				if(jsonObject != null)
				{
					Intent it = new Intent(BankCardManageActivity.this, TgThirdWebActivity.class);
					it.putExtra("url", jsonObject.getString("url"));
					it.putExtra("message", "添加银行卡成功！");
					it.putExtra("title", "绑定银行卡");
					startActivityForResult(it, YB_REQUEST_CODE_RECHAGE);
				}
			}
		}
		catch (JSONException e)
		{
			e.printStackTrace();
		}
		
	}
	/**
	 * 检测安全认证信息   和  银行卡最大数量
	 * @return
	 */
	protected boolean checkSecurityInfo()
	{
		UserInfo userInfo = DMApplication.getInstance().getUserInfo();
		if (maxbanks <= currentCards)
		{
			AlertDialogUtil.alert(this, "您绑定的银行卡数量已达到最大值，不能再绑定其他银行卡！", new AlertListener()
			{
				@Override
				public void doConfirm()
				{
					finish();
				}
			});
			return false;
		}
		
		if (null != userInfo && !UIHelper.hasCompletedSecurityInfo(this, userInfo, isNeedEmailRZ, isNeedPsw))
		{//查看是否完成了信息安全认证
			return false;
		}
		return true;
	}
	
	/**
	 * 最大添加银行卡数
	 */
	private int maxbanks;
	
	private List<BankCard> cards;
	
	/**
	 * 当前绑定的银行卡数
	 */
	private int currentCards;
	
	/**
	 * 请求数据
	 */
	private void getBankCardDatas()
	{
		HttpUtil.getInstance().post(this, DMConstant.API_Url.USER_MYBANKLIST, new HttpCallBack()
		{
			@Override
			public void onSuccess(JSONObject result)
			{
				try
				{
					String code = result.getString("code");
					if (DMConstant.ResultCode.SUCCESS.equals(code))
					{	
						addBankImg.setVisibility(View.GONE);
						// 成功
						JSONObject data0 = result.getJSONObject("data");
						maxbanks = data0.getInt("maxbanks");
						JSONArray data = data0.getJSONArray("myBankList");
						cards = new ArrayList<BankCard>();
						currentCards = data.length();
						if(currentCards == 0)
						{
							addBankImg.setVisibility(View.VISIBLE);	
						}
						if (currentCards > 0)	
						{
							addBankImg.setVisibility(View.GONE);	
							for (int i = 0; i < currentCards; i++)
							{
								DMJsonObject dmJsonObject = new DMJsonObject(data.get(i).toString());
								BankCard bankCard = new BankCard(dmJsonObject);
								cards.add(bankCard);
							}
							adapter = new BankCardAdapter(BankCardManageActivity.this, cards);
							listView.setAdapter(adapter);
							bank_card_line.setVisibility(View.VISIBLE);
							listView.setVisibility(View.VISIBLE);
							tip_no_card_tv.setVisibility(View.GONE);
						}
						else
						{
							bank_card_line.setVisibility(View.GONE);
							listView.setVisibility(View.INVISIBLE);
							tip_no_card_tv.setVisibility(View.VISIBLE);
						}
						wxts = data0.getString("wxts");
						reminder_tv.setText(Html.fromHtml(wxts));
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
			
		});
	}
	
	private void initMenuListView()
	{
		//创建一个SwipeMenuCreator供ListView使用
		SwipeMenuCreator creator = new SwipeMenuCreator()
		{
			@Override
			public void create(SwipeMenu menu)
			{
				//				//创建一个侧滑菜单
				//				SwipeMenuItem openItem = new SwipeMenuItem(getApplicationContext());
				//				//给该侧滑菜单设置背景
				//				openItem.setBackground(new ColorDrawable(Color.rgb(0xF9, 0x3F, 0x25)));
				//				//设置宽度
				//				openItem.setWidth(DensityUtil.dip2px(BankCardManageActivity.this, 80));
				//				//设置名称
				//				openItem.setTitle(getResources().getString(R.string.unbind));
				//				//字体大小
				//				openItem.setTitleSize(18);
				//				//字体颜色
				//				openItem.setTitleColor(Color.WHITE);
				//				//加入到侧滑菜单中
				//				menu.addMenuItem(openItem);
				
				//创建一个侧滑菜单
				SwipeMenuItem delItem = new SwipeMenuItem(getApplicationContext());
				//给该侧滑菜单设置背景
				delItem.setBackground(new ColorDrawable(Color.rgb(0xF9, 0x3F, 0x25)));
				//设置宽度
				delItem.setWidth(DensityUtil.dip2px(BankCardManageActivity.this, 80));
				//设置图片
				delItem.setIcon(R.drawable.button_ch);
				//加入到侧滑菜单中
				menu.addMenuItem(delItem);
			}
		};
		
		listView.setMenuCreator(creator);
		
		//侧滑菜单的相应事件
		listView.setOnMenuItemClickListener(new OnMenuItemClickListener()
		{
			@Override
			public boolean onMenuItemClick(final int position, SwipeMenu menu, int index)
			{
				//第二个添加的菜单的响应事件(删除)
				AlertDialogUtil.confirm(BankCardManageActivity.this, "是否确认删除银行卡", new ConfirmListener()
				{
					
					@Override
					public void onOkClick()
					{
						int backCardId = cards.get(position).getId();
						unbindCard(backCardId);
					}
					
					@Override
					public void onCancelClick()
					{
						
					}
				});
				return false;
			}
		});
	}
	
	protected void unbindCard(int backCardId)
	{
		HttpParams params = new HttpParams();
		params.put("id", backCardId);
		HttpUtil.getInstance().post(this, DMConstant.API_Url.PAY_UNBIND_CARD, params, new HttpCallBack()
		{
			@Override
			public void onSuccess(JSONObject result)
			{
//				try
//				{
//					String code = result.getString("code");
//					if (DMConstant.ResultCode.SUCCESS.equals(code))
//					{
//						// 成功
//						//ToastUtil.getInstant().show(BankCardManageActivity.this, R.string.card_delete_success);
//						//getBankCardDatas();
//					}
//					else
//					{
//						ErrorUtil.showError(result);
//					}
//				}
//				catch (JSONException e)
//				{
//					e.printStackTrace();
//				}
				String description;
				try
				{
					description = result.getString("description");
					AlertDialogUtil.alert(BankCardManageActivity.this, description).setCanceledOnTouchOutside(false);
					getBankCardDatas();
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
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
}
