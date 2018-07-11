package com.hxjr.p2p.ad5.utils;

import com.hxjr.p2p.ad5.R;

import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;

/**
 * 无网络时的界面管理
 * @author jiaohongyun
 *
 */
public class NetConnectErrorManager
{
	private View noNetworkLayout;
	
	private View mView;
	
	private View mainView;
	
	private View net_error_content;
	
	private NetConnetCallBack mCallBack;
	
	//	public NetConnectErrorManager(View parent, NetConnetCallBack callBack)
	//	{
	//		mView = parent;
	//		mCallBack = callBack;
	//		initView();
	//	}
	
	public NetConnectErrorManager(View parent, View mainView, NetConnetCallBack callBack)
	{
		mView = parent;
		mCallBack = callBack;
		this.mainView = mainView;
		initView();
	}
	
	private void initView()
	{
		noNetworkLayout = mView.findViewById(R.id.noNetworkLayout);
		noNetworkLayout.setVisibility(View.GONE);
		net_error_content = mView.findViewById(R.id.net_error_content);
		noNetworkLayout.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				net_error_content.setVisibility(View.GONE);
				new Handler().postDelayed(new Runnable()
				{
					@Override
					public void run()
					{
						net_error_content.setVisibility(View.VISIBLE);
					}
				}, 200);
				mCallBack.onNetErrorRefrensh();
			}
		});
	}
	
	/**
	 * 网络正常
	 */
	public void onNetGood()
	{
		if (noNetworkLayout != null)
		{
			noNetworkLayout.setVisibility(View.GONE);
			if (mainView != null)
			{
				mainView.setVisibility(View.VISIBLE);
			}
		}
	}
	
	public void onNetError()
	{
		if (noNetworkLayout != null)
		{
			if (mainView != null)
			{
				mainView.setVisibility(View.GONE);
			}
			noNetworkLayout.setVisibility(View.VISIBLE);
		}
	}
	public interface NetConnetCallBack
	{
		/**
		 * 刷新数据
		 */
		public void onNetErrorRefrensh();
	}
}
