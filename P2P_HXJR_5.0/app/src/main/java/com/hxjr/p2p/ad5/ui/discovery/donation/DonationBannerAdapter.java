package com.hxjr.p2p.ad5.ui.discovery.donation;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.hxjr.p2p.ad5.ui.home.WebViewActivity;
import com.hxjr.p2p.ad5.utils.DMConstant;
import com.hxjr.p2p.ad5.utils.FormatUtil;
import com.hxjr.p2p.ad5.R;
import com.dm.img.DMBitmap;
import com.dm.widgets.bannerviewpager.AdvertisementInfo;

public class DonationBannerAdapter extends PagerAdapter
{
	// 界面列表
	private List<AdvertisementInfo> infos = new ArrayList<AdvertisementInfo>(DMConstant.DigitalConstant.TEN);
	
	private Context mContext;
	
	private DMBitmap dmBitmapLoader;
	
	public DonationBannerAdapter(List<AdvertisementInfo> banners, Context context)
	{
		mContext = context;
		this.infos.clear();
		this.infos.addAll(banners);
		//初始化加载图片的类
		dmBitmapLoader = DMBitmap.create(mContext);
		dmBitmapLoader.configLoadingImage(R.drawable.img_loading);
		dmBitmapLoader.configLoadfailImage(R.drawable.img_load_failure);
	}
	
	public void updateBanners(List<AdvertisementInfo> views)
	{
		if (null != views && null != this.infos)
		{
			this.infos.clear();
			this.infos.addAll(views);
			notifyDataSetChanged();
		}
	}
	
	/**
	 * 获得当前界面数
	 */
	@Override
	public int getCount()
	{
		return infos != null ? Integer.MAX_VALUE : 0;
	}
	
	/**
	 * 初始化position位置的界面
	 */
	@Override
	public Object instantiateItem(ViewGroup container, int position)
	{
		int currentInt = position % infos.size();
		final AdvertisementInfo info = infos.get(currentInt);
		ImageView imageView = new ImageView(mContext);
		imageView.setScaleType(ImageView.ScaleType.FIT_XY);
		container.addView(imageView);
		dmBitmapLoader.display(imageView, info.getImgUrl());
		imageView.setTag(currentInt);
		// 网络获取图片去
		imageView.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				int index = (Integer)v.getTag();
				AdvertisementInfo info = infos.get(index);
				if (info != null && info.getLinkUrl() != null && FormatUtil.validateUrl(info.getLinkUrl()))//如果没有设置关联URL，则不进行任何操作
				{
					Intent intent = new Intent(mContext, WebViewActivity.class);
					intent.putExtra("linkUrl", info.getLinkUrl());
					intent.putExtra("title", info.getTitle());
					mContext.startActivity(intent);
				}
			}
		});
		return imageView;
	}
	
	/**
	 * 判断是否由对象生成界面
	 */
	@Override
	public boolean isViewFromObject(View view, Object arg1)
	{
		return view == arg1;
	}
	
	/**
	 * 销毁position位置的界面
	 */
	@Override
	public void destroyItem(ViewGroup container, int position, Object object)
	{
		container.removeView((View)object);
	}
}
