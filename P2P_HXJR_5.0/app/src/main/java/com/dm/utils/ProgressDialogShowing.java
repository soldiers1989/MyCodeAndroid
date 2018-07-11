package com.dm.utils;

/**
 * 进度展示接口
 * @author  jiaohongyun
 * @date 2015年8月24日
 */
public interface ProgressDialogShowing
{
	/**
	 * 显示加载中
	 */
	void show();
	
	void show(String content);
	
	/**
	 * 是否正在显示
	 * @return
	 */
	boolean isShowing();
	
	/**
	 * 销毁
	 */
	void dismiss();
}