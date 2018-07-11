package com.hxjr.p2p.ad5.ui.discovery.spread;

import com.hxjr.p2p.ad5.R;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.PopupWindow;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.Platform.ShareParams;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

/**
 * @author  tangjian
 * @date 2015-8-20
 */

public class SharePopupWindow extends PopupWindow
{
	
	private SpreadRewardActivity activity;
	
	private PlatformActionListener platformActionListener;
	
	private ShareParams shareParams;
	
	public SharePopupWindow(SpreadRewardActivity activity)
	{
		this.activity = activity;
	}
	
	public PlatformActionListener getPlatformActionListener()
	{
		return platformActionListener;
	}
	
	public void setPlatformActionListener(PlatformActionListener platformActionListener)
	{
		this.platformActionListener = platformActionListener;
	}
	
	@SuppressLint("InflateParams")
	public void showShareWindow()
	{
		View view = LayoutInflater.from(activity).inflate(R.layout.share_layout, null);
		GridView gridView = (GridView)view.findViewById(R.id.share_gridview);
		gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));//取消GridView中Item选中时默认的背景色 
		ShareAdapter adapter = new ShareAdapter(activity);
		gridView.setAdapter(adapter);
		
		// 设置SelectPicPopupWindow的View
		this.setContentView(view);
		// 设置SelectPicPopupWindow弹出窗体的宽
		this.setWidth(LayoutParams.MATCH_PARENT);
		// 设置SelectPicPopupWindow弹出窗体的高
		this.setHeight(LayoutParams.WRAP_CONTENT);
		// 设置SelectPicPopupWindow弹出窗体可点击
		this.setFocusable(true);
		// 设置SelectPicPopupWindow弹出窗体动画效果
		this.setAnimationStyle(R.style.AnimBottom);
		// 实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(0xb0000000);
		// 设置SelectPicPopupWindow弹出窗体的背景
		this.setBackgroundDrawable(dw);
		
		gridView.setOnItemClickListener(new ShareItemClickListener(this));
	}
	
	private class ShareItemClickListener implements OnItemClickListener
	{
		private PopupWindow pop;
		
		public ShareItemClickListener(PopupWindow pop)
		{
			this.pop = pop;
		}
		
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id)
		{
			share(position);
			activity.setHasShared(true);
			pop.dismiss();
		}
	}
	
	/**
	 * 分享
	 * @param position
	 */
	private void share(int position)
	{
		ShareSDK.initSDK(activity);
		Platform plat = null;
		plat = ShareSDK.getPlatform(activity, getPlatform(position));
		if (platformActionListener != null)
		{
			plat.setPlatformActionListener(platformActionListener);
		}
		if (plat.getName().equals(QQ.NAME))
		{//如果是QQ分享，则需要设置titleUrl
			shareParams.setTitleUrl(shareParams.getUrl());
		}
		plat.share(shareParams);
	}
	
	/**
	 * 初始化分享参数
	 * 
	 * @param shareModel
	 */
	public void initShareParams(ShareModel shareModel)
	{
		if (shareModel != null)
		{
			ShareParams sp = new ShareParams();
			sp.setShareType(Platform.SHARE_TEXT);
			sp.setShareType(Platform.SHARE_WEBPAGE);
			sp.setTitle(shareModel.getTitle());
			//sp.setTitleUrl("http://baidu.com");如果是QQ分享，则需要设置titleUrl
			sp.setText(shareModel.getText());
			sp.setUrl(shareModel.getUrl());
			//			sp.setImageUrl(shareModel.getImageUrl());
			sp.setImagePath(shareModel.getImageUrl());
			shareParams = sp;
		}
	}
	
	/**
	 * 获取平台
	 * @param position
	 * @return
	 */
	private String getPlatform(int position)
	{
		String platform = "";
		switch (position)
		{
			case 0:
				platform = Wechat.NAME;
				break;
			case 1:
				platform = WechatMoments.NAME;
				break;
			case 2:
				platform = QQ.NAME;
				break;
			case 3:
				platform = QZone.NAME;
				break;
		}
		return platform;
	}
}
