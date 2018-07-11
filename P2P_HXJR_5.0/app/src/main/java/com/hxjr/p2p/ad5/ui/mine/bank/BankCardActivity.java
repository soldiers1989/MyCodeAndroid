package com.hxjr.p2p.ad5.ui.mine.bank;

import android.annotation.SuppressLint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.dm.http.HttpCallBack;
import com.dm.http.HttpUtil;
import com.dm.utils.DMJsonObject;
import com.dm.widgets.utils.ToastUtil;
import com.hxjr.p2p.ad5.DMApplication;
import com.hxjr.p2p.ad5.R;
import com.hxjr.p2p.ad5.bean.Address;
import com.hxjr.p2p.ad5.bean.Bank;
import com.hxjr.p2p.ad5.bean.UserInfo;
import com.hxjr.p2p.ad5.ui.BaseActivity;
import com.hxjr.p2p.ad5.utils.DMConstant;
import com.hxjr.p2p.ad5.utils.ErrorUtil;
import com.hxjr.p2p.ad5.utils.FormatUtil;
import com.hxjr.p2p.ad5.utils.HttpParams;
import com.hxjr.p2p.ad5.utils.UIHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 增加银行卡
 * @author  tangjian
 * @date 2015-6-4
 */
public class BankCardActivity extends BaseActivity implements OnClickListener
{
	private EditText openNameEt; //开户名
	
	private EditText cardNumEt; //卡号
	
	private EditText openSubbranchBank; //开户支行
	
	private TextView selectBankTv; //银行
	
	private TextView bankProvinceTv; //省份
	
	private TextView bankCityTv; //城市
	
	private TextView aeraTv; //地区
	
	private PopupWindow popupWindow;
	
	private Button nextStep;
	
	private List<Bank> bankList;
	
	private List<Address> provinceAreas;
	
	private List<Address> cityAreas;
	
	private List<Address> areas;
	
	/**需要提交的城市id*/
	private int submitCityId;
	
	/**需要提交的银行id*/
	private int submitBankId;
	
	/**
	 * 需要提交的地区ID
	 */
	private int addressId;
	
	private UserInfo userInfo;
	
	private View xianLayout;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bank_card_activity);
		initView();
		initData();
		initListener();
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		getShengData();
		getBankList();
	}
	
	/**
	 * 查找银行列表
	 */
	private void getBankList()
	{
		HttpParams params = new HttpParams();
		HttpUtil.getInstance().post(this, DMConstant.API_Url.USER_BANKLIST, params, new HttpCallBack()
		{
			@Override
			public void onSuccess(JSONObject result)
			{
				try
				{
					String code = result.getString("code");
					if (DMConstant.ResultCode.SUCCESS.equals(code))
					{
						// 成功
						JSONArray dataList = result.getJSONArray("data");
						if (bankList == null)
						{
							bankList = new ArrayList<Bank>();
						}
						bankList.clear();
						for (int i = 0; i < dataList.length(); i++)
						{
							DMJsonObject dmJsonObject = new DMJsonObject(dataList.get(i).toString());
							Bank bank = new Bank(dmJsonObject);
							bankList.add(bank);
						}
					}
				}
				catch (JSONException e)
				{
					e.printStackTrace();
				}
			}
			
		});
	}
	
	@Override
	protected void initView()
	{
		//设置返回按钮事件
		((ImageView)findViewById(R.id.btn_back)).setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				finish();
			}
		});
		((TextView)findViewById(R.id.title_text)).setText(R.string.add_bank_card);
		
		openNameEt = (EditText)findViewById(R.id.open_name_et);
		openNameEt.setEnabled(false);
		cardNumEt = (EditText)findViewById(R.id.card_number_et);
		openSubbranchBank = (EditText)findViewById(R.id.open_subbranch_bank_et);
		selectBankTv = (TextView)findViewById(R.id.select_bank_tv);
		bankProvinceTv = (TextView)findViewById(R.id.select_province_tv);
		bankCityTv = (TextView)findViewById(R.id.select_city_tv);
		aeraTv = (TextView)findViewById(R.id.select_area_tv);
		nextStep = (Button)findViewById(R.id.next_step_btn);
		xianLayout = findViewById(R.id.xianLayout);
		if (DMConstant.Config.SHOW_XIAN)
		{
			xianLayout.setVisibility(View.VISIBLE);
		}
		else
		{
			xianLayout.setVisibility(View.GONE);
		}
	}
	
	private void initData()
	{
		provinceAreas = new ArrayList<Address>(DMConstant.DigitalConstant.TEN);
		cityAreas = new ArrayList<Address>(DMConstant.DigitalConstant.TEN);
		areas = new ArrayList<Address>(DMConstant.DigitalConstant.TEN);
		userInfo = DMApplication.getInstance().getUserInfo();
		openNameEt.setText(null == userInfo ? "" : userInfo.getName()); //开户名默认是用户真实姓名
	}
	
	private void initListener()
	{
		nextStep.setOnClickListener(this);
		selectBankTv.setOnClickListener(this);
		bankProvinceTv.setOnClickListener(this);
		bankCityTv.setOnClickListener(this);
		aeraTv.setOnClickListener(this);
		
		openSubbranchBank.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.next_step_btn:
				submitBankInfo();
				break;
			case R.id.select_bank_tv:
			{
				UIHelper.hideSrfAndRun(this, new Runnable()
				{
					@Override
					public void run()
					{
						showBankPopup(bankList, selectBankTv);
					}
				});
				break;
			}
			case R.id.select_province_tv:
			{
				//省
				UIHelper.hideSrfAndRun(this, new Runnable()
				{
					@Override
					public void run()
					{
						showAreaPopup(0, provinceAreas, bankProvinceTv);
					}
				});
				break;
			}
			case R.id.select_city_tv:
			{
				//市
				UIHelper.hideSrfAndRun(this, new Runnable()
				{
					@Override
					public void run()
					{
						showAreaPopup(1, cityAreas, bankCityTv);
					}
				});
				break;
			}
			case R.id.select_area_tv:
			{
				//县
				UIHelper.hideSrfAndRun(this, new Runnable()
				{
					@Override
					public void run()
					{
						showAreaPopup(2, areas, aeraTv);
					}
				});
			}
				break;
		}
	}
	
	/**
	 * 点击下一步，提交信息，完成添加银行卡
	 */
	private void submitBankInfo()
	{
		if (!checkParams())
		{
			return;
		}
		HttpParams params = new HttpParams();
		params.put("banknumber", cardNumEt.getText().toString().trim());
		params.put("subbranch", openSubbranchBank.getText().toString().trim());
		if (!DMConstant.Config.SHOW_XIAN)
		{
			addressId = submitCityId;
		}
		params.put("xian", "" + addressId);
		params.put("bankId", "" + submitBankId);
		params.put("name", DMApplication.getInstance().getUserInfo().getName());
		HttpUtil.getInstance().post(this, DMConstant.API_Url.ADD_BANK_CARD, params, new HttpCallBack()
		{
			@Override
			public void onSuccess(JSONObject result)
			{
				try
				{
					String code = result.getString("code");
					if (DMConstant.ResultCode.SUCCESS.equals(code))
					{
						// 成功
						ToastUtil.getInstant().show(BankCardActivity.this, R.string.add_card_success);
						finish();
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
	
	/**
	 * 检测输入的内容
	 */
	private boolean checkParams()
	{
		if (TextUtils.isEmpty(cardNumEt.getText().toString().trim()))
		{
			ToastUtil.getInstant().show(this, "请输入银行卡卡号");
			return false;
		}
		if (!FormatUtil.checkBankCard(cardNumEt.getText().toString().trim()))
		{
			ToastUtil.getInstant().show(this, "银行卡错误");
			return false;
		}
		if (selectBankTv.getText().toString().trim().equals("选择银行"))
		{
			ToastUtil.getInstant().show(this, "请选择银行");
			return false;
		}
		if (bankProvinceTv.getText().toString().trim().equals("省份"))
		{
			ToastUtil.getInstant().show(this, "请选择省份");
			return false;
		}
		if (bankCityTv.getText().toString().trim().equals("所在地"))
		{
			ToastUtil.getInstant().show(this, "请选择城市");
			return false;
		}
		if (DMConstant.Config.SHOW_XIAN)
		{
			if (aeraTv.getText().toString().trim().contains("所在区"))
			{
				ToastUtil.getInstant().show(this, "请选择地区");
				return false;
			}
		}
		if (openSubbranchBank.getText().toString().trim().equals(""))
		{
			ToastUtil.getInstant().show(this, "请输入开户支行");
			return false;
		}
		return true;
	}
	
	/**
	 * 显示银行卡popupWindow
	 */
	@SuppressLint("InflateParams")
	@SuppressWarnings("deprecation")
	private void showBankPopup(final List<Bank> bandCards, final TextView selectBankTv)
	{
		View contentView = LayoutInflater.from(BankCardActivity.this).inflate(R.layout.popup_window, null);
		
		int height = getPopupWindowHeight(selectBankTv, bandCards.size()); //获取悬浮窗的高度
		
		popupWindow = new PopupWindow(contentView, selectBankTv.getWidth(), height, true);
		popupWindow.setBackgroundDrawable(new BitmapDrawable()); //加该行代码后，点击界面其他位置popupWindow会消失
		//				popupWindow.setFocusable(true);
		ListView listView = (ListView)contentView.findViewById(R.id.popup_window_listview);
		listView.setAdapter(new BankBaseAdapter(bandCards));
		popupWindow.showAsDropDown(selectBankTv);
		listView.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view, final int position, long id)
			{
				popupWindow.dismiss();
				Bank bank = (Bank)parent.getAdapter().getItem(position);
				if (bank != null)
				{
					selectBankTv.setText(bank.getBankName());
					submitBankId = bank.getId();
				}
			}
		});
	}
	
	/**
	 * 显示地区popupWindow
	 * @param type 地区级别0省1市2地区
	 * @param datas
	 * @param textView
	 */
	@SuppressLint("InflateParams")
	@SuppressWarnings("deprecation")
	protected void showAreaPopup(final int type, final List<Address> datas, final TextView textView)
	{
		View contentView = LayoutInflater.from(BankCardActivity.this).inflate(R.layout.popup_window, null);
		
		int height = getPopupWindowHeight(textView, datas.size()); //获取悬浮窗的高度
		
		popupWindow = new PopupWindow(contentView, textView.getWidth(), height, true);
		popupWindow.setBackgroundDrawable(new BitmapDrawable()); //加该行代码后，点击界面其他位置popupWindow会消失
		//				popupWindow.setFocusable(true);
		ListView listView = (ListView)contentView.findViewById(R.id.popup_window_listview);
		listView.setAdapter(new AreaBaseAdapter(datas, type));
		popupWindow.showAsDropDown(textView);
		listView.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view, final int position, long id)
			{
				popupWindow.dismiss();
				switch (type)
				{
					case 0:
					{
						bankCityTv.setText("所在地");
						aeraTv.setText("所在区(县)");
						getCityData(datas.get(position).getId());
						textView.setText(datas.get(position).getSheng());
						break;
					}
					case 1:
					{
						aeraTv.setText("所在区(县)");
						submitCityId = datas.get(position).getId();
						textView.setText(datas.get(position).getShi());
						getXianData(datas.get(position).getId());
						break;
					}
					case 2:
					{
						textView.setText(datas.get(position).getXian());
						addressId = datas.get(position).getId();
						break;
					}
					default:
						break;
				}
			}
		});
	}
	
	/**
	 * 获取省
	 */
	private void getShengData()
	{
		HttpParams params = new HttpParams();
		params.put("type", "SHENG");
		HttpUtil.getInstance().post(this, DMConstant.API_Url.SEARCH_ADDRESS, params, new HttpCallBack()
		{
			
			@Override
			public void onSuccess(JSONObject result)
			{
				try
				{
					String code = result.getString("code");
					if (DMConstant.ResultCode.SUCCESS.equals(code))
					{
						// 成功
						JSONArray dataList = result.getJSONArray("data");
						provinceAreas.clear();
						for (int i = 0; i < dataList.length(); i++)
						{
							DMJsonObject record = new DMJsonObject(dataList.getJSONObject(i).toString());
							Address address = new Address(record);
							provinceAreas.add(address);
						}
						
					}
				}
				catch (JSONException e)
				{
					e.printStackTrace();
				}
			}
			
		});
	}
	
	/**
	 * 获取县地区
	 * @param id
	 */
	private void getXianData(int id)
	{
		if (!DMConstant.Config.SHOW_XIAN)
		{
			return;
		}
		HttpParams params = new HttpParams();
		params.put("type", "XIAN");
		params.put("cityId", id);
		HttpUtil.getInstance().post(this, DMConstant.API_Url.SEARCH_ADDRESS, params, new HttpCallBack()
		{
			@Override
			public void onSuccess(JSONObject result)
			{
				try
				{
					String code = result.getString("code");
					if (DMConstant.ResultCode.SUCCESS.equals(code))
					{
						// 成功
						JSONArray dataList = result.getJSONArray("data");
						areas.clear();
						for (int i = 0; i < dataList.length(); i++)
						{
							DMJsonObject record = new DMJsonObject(dataList.getJSONObject(i).toString());
							Address address = new Address(record);
							areas.add(address);
						}
					}
				}
				catch (JSONException e)
				{
					e.printStackTrace();
				}
			}
		});
	}
	
	/**
	 * 获取市
	 * @param id
	 */
	private void getCityData(int id)
	{
		HttpParams params = new HttpParams();
		params.put("type", "SHI");
		params.put("provinceId", id);
		HttpUtil.getInstance().post(this, DMConstant.API_Url.SEARCH_ADDRESS, params, new HttpCallBack()
		{
			
			@Override
			public void onSuccess(JSONObject result)
			{
				try
				{
					String code = result.getString("code");
					if (DMConstant.ResultCode.SUCCESS.equals(code))
					{
						// 成功
						JSONArray dataList = result.getJSONArray("data");
						cityAreas.clear();
						for (int i = 0; i < dataList.length(); i++)
						{
							DMJsonObject record = new DMJsonObject(dataList.getJSONObject(i).toString());
							Address address = new Address(record);
							cityAreas.add(address);
						}
						
					}
				}
				catch (JSONException e)
				{
					e.printStackTrace();
				}
			}
			
		});
	}
	
	/**
	 *  设置popupWindow的高度
	 */
	private int getPopupWindowHeight(View parentView, int size)
	{
		int[] position = new int[2];
		parentView.getLocationInWindow(position);
		DisplayMetrics displayMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		int screenHeight = displayMetrics.heightPixels; //屏高
		int height = 0;
		if (size * parentView.getHeight() < 400)
		{
			height = size * parentView.getHeight();
		}
		else
		{
			height = screenHeight - position[1] - parentView.getHeight() - 10;
		}
		return height;
	}
	
	/**地区ListView的适配器*/
	public class AreaBaseAdapter extends BaseAdapter
	{
		private List<Address> datas;
		
		private int type;
		
		public AreaBaseAdapter(List<Address> datas, int type)
		{
			this.datas = datas;
			this.type = type;
		}
		
		@SuppressLint({"ViewHolder", "InflateParams"})
		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			convertView = LayoutInflater.from(BankCardActivity.this).inflate(R.layout.popup_window_lv_item, null);
			TextView tv = (TextView)convertView.findViewById(R.id.item_tv);
			switch (type)
			{
				case 0:
				{
					tv.setText(datas.get(position).getSheng());
					break;
				}
				case 1:
				{
					tv.setText(datas.get(position).getShi());
					break;
				}
				case 2:
				{
					tv.setText(datas.get(position).getXian());
					break;
				}
				default:
					break;
			}
			
			return convertView;
		}
		
		@Override
		public long getItemId(int position)
		{
			return position;
		}
		
		@Override
		public Object getItem(int position)
		{
			return datas.get(position);
		}
		
		@Override
		public int getCount()
		{
			return datas.size();
		}
	}
	
	/**银行卡ListView的适配器*/
	public class BankBaseAdapter extends BaseAdapter
	{
		private List<Bank> datas;
		
		public BankBaseAdapter(List<Bank> datas)
		{
			this.datas = datas;
		}
		
		@SuppressLint({"ViewHolder", "InflateParams"})
		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			convertView = LayoutInflater.from(BankCardActivity.this).inflate(R.layout.popup_window_lv_item, null);
			TextView tv = (TextView)convertView.findViewById(R.id.item_tv);
			tv.setText(datas.get(position).getBankName());
			return convertView;
		}
		
		@Override
		public long getItemId(int position)
		{
			return position;
		}
		
		@Override
		public Object getItem(int position)
		{
			return datas.get(position);
		}
		
		@Override
		public int getCount()
		{
			return datas.size();
		}
	}
	
	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		if (null != popupWindow && popupWindow.isShowing())
		{
			popupWindow.dismiss();
		}
	}
}
