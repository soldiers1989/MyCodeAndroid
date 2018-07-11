package com.hxjr.p2p.ad5.ui.mine.user;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.json.JSONException;
import org.json.JSONObject;

import com.hxjr.p2p.ad5.ui.BaseActivity;
import com.hxjr.p2p.ad5.utils.DMConstant;
import com.hxjr.p2p.ad5.utils.HttpParams;
import com.hxjr.p2p.ad5.R;
import com.dm.http.DMException;
import com.dm.http.HttpCallBack;
import com.dm.http.HttpUtil;
import com.dm.utils.DMJsonObject;
import com.dm.widgets.utils.ToastUtil;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

/**
 * 注册协议
 * @author  jiaohongyun
 * @date 2015年6月18日
 */
public class RegisterProtocolActivity extends BaseActivity
{
	private WebView webView;
	
	private String agreementType;
	
	private String agreementName;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		super.mustShowShouShi = false;
		setContentView(R.layout.register_protocol);
		agreementType = getIntent().getStringExtra("agreementType");
		agreementName = getIntent().getStringExtra("agreementName");
		initView();
		//		initData();
	}
	
	/**
	 * 初始化
	 */
	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void initView()
	{
		super.initView();
		((TextView)findViewById(R.id.title_text)).setText(agreementName);
		webView = (WebView)findViewById(R.id.protocol_webView);
		webView.setBackgroundColor(getResources().getColor(R.color.main_bg));
		WebSettings webSettings = webView.getSettings();
		webSettings.setUseWideViewPort(true);//设置此属性，可任意比例缩放
		webSettings.setLoadWithOverviewMode(true);
		webSettings.setJavaScriptEnabled(true); // 设置WebView属性，能够执行Javascript脚本
		webSettings.setBuiltInZoomControls(true);
		webSettings.setSupportZoom(true);//支持缩放 
		webSettings.setTextZoom(200);
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		initData();
	}
	
	private void initData()
	{
		HttpParams httpParams = new HttpParams();
		httpParams.put("type", agreementType);
		try
		{
			httpParams.put("name", URLEncoder.encode(agreementName, "utf-8"));
		}
		catch (UnsupportedEncodingException e)
		{
			httpParams.put("name", agreementName);
			e.printStackTrace();
		}
		
		HttpUtil.getInstance().post(this, DMConstant.API_Url.REGISTER_AGREEMENT, httpParams, new HttpCallBack()
		{
			@Override
			public void onSuccess(JSONObject result)
			{
				try
				{
					String code = result.getString("code");
					
					if (DMConstant.ResultCode.SUCCESS.equals(code))
					{
						DMJsonObject data = new DMJsonObject(result.getString("data").toString());
						if (data != null)
						{
							String content = data.getString("content");
							webView.loadDataWithBaseURL(null, content, "text/html", "utf-8", null);
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
				ToastUtil.getInstant().show(RegisterProtocolActivity.this, dmE.getDescription());
			}
		});
	}
}
