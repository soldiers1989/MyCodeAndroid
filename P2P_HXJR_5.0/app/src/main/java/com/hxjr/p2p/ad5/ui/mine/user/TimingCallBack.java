package com.hxjr.p2p.ad5.ui.mine.user;

public interface TimingCallBack
{
	/**
	 * 成功请求了Timing接口
	 */
	void doTimingCallBack();
	
	/**
	 * 服务器内部错误
	 */
	void onServerError();
	
	/**
	 * http请求连接错误
	 */
	void onConnectError();
	
	/**
	 * 开始请求
	 */
	void onStart();
}
