package com.hxjr.p2p.ad5.utils;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.TextView;

import com.hxjr.p2p.ad5.ui.BaseActivity;
import com.hxjr.p2p.ad5.ui.mine.user.RegisterProtocolActivity;
import com.hxjr.p2p.ad5.R;
import com.dm.http.DMException;
import com.dm.http.HttpCallBack;
import com.dm.http.HttpUtil;
import com.dm.utils.DMJsonObject;
import com.dm.widgets.utils.ToastUtil;

public class AgreementManager
{
	/**
	 * 注册
	 */
	public static final String TYPE_ZC = "ZC";
	
	/**
	 * 借款
	 */
	public static final String TYPE_JK = "JK";
	
	/**
	 * 公益捐赠
	 */
	public static final String TYPE_GYB = "GYB";
	
	/**
	 * 债权转让
	 */
	public static final String TYPE_ZQ = "ZQ";
	
	private View agreementLayout;
	
	private CheckBox agreementCheckBox;
	
	private TextView agreementButton;
	
	private BaseActivity mActivity;
	
	private String mType;
	
	private String isDanBao = "F";
	
	private String agreementType;
	
	private String agreementName;
	
	public AgreementManager(BaseActivity activity, String type)
	{
		mActivity = activity;
		mType = type;
	}
	
	public AgreementManager(BaseActivity activity, String type, String isDanBao)
	{
		mActivity = activity;
		mType = type;
		this.isDanBao = isDanBao;
	}
	
	public void initView()
	{
		agreementLayout = mActivity.findViewById(R.id.agreementLayout);
		agreementCheckBox = (CheckBox)mActivity.findViewById(R.id.agreementCheckBox);
		agreementButton = (TextView)mActivity.findViewById(R.id.agreementButton);
		agreementButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if (mActivity.checkClick(v.getId()))
				{
					gotoProtocol();
				}
			}
		});
		initData();
	}
	
	/**
	 * 获取是否使用协议及协议名称
	 */
	public void initData()
	{
		HttpParams httpParams = new HttpParams();
		httpParams.put("type", mType);
		httpParams.put("isDB", isDanBao);
		HttpUtil.getInstance().post(mActivity, DMConstant.API_Url.INIT_TERMTYPE, httpParams, new HttpCallBack()
		{
			@Override
			public void onSuccess(JSONObject result)
			{
				try
				{
					String code = result.getString("code");
					
					if (DMConstant.ResultCode.SUCCESS.equals(code))
					{
						// agreementType  协议类型
						// agreementName  协议名称
						String dataStr = result.getString("data");
						if (!"".equals(dataStr))
						{
							DMJsonObject data = new DMJsonObject(result.getString("data").toString());
							agreementName = data.getString("agreementName");
							agreementType = data.getString("agreementType");
							agreementLayout.setVisibility(View.VISIBLE);
							if (TYPE_JK.equals(mType))
							{
								agreementName = "借款协议";
							}
							agreementButton.setText("《" + agreementName + "》");
							
						}
						else
						{
							agreementLayout.setVisibility(View.GONE);
						}
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
			
			@Override
			public void onConnectFailure(DMException dmE, Context context)
			{
				ToastUtil.getInstant().show(mActivity, dmE.getDescription());
			}
		});
		//		if (TYPE_ZC.equals(mType))
		//		{
		//		
		//		}
		//		else if (TYPE_JK.equals(mType))
		//		{
		//		
		//		}
		//		else if (TYPE_GYB.equals(mType))
		//		{
		//		
		//		}
		//		else if (TYPE_ZQ.equals(mType))
		//		{
		//		
		//		}
	}
	
	/**
	 * 是否同意
	 * @return
	 */
	public boolean isChecked()
	{
		return agreementLayout.getVisibility() == View.GONE ? true : agreementCheckBox.isChecked();
	}
	
	/**
	 * 打开注册协议
	 * @param v
	 */
	public void gotoProtocol()
	{
		Intent intent = new Intent(mActivity, RegisterProtocolActivity.class);
		intent.putExtra("agreementType", agreementType);
		intent.putExtra("agreementName", agreementName);
		mActivity.startActivity(intent);
	}
}
