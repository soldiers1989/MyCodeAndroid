package com.dm.widgets.bannerviewpager;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageView;

import com.hxjr.p2p.ad5.R;
import com.dm.img.DMBitmap;
/**
 * ImageView创建工厂
 */
public class ViewDisplayFactory {

	/**
	 * 获取ImageView视图的同时加载显示url
	 * 
	 * @param text
	 * @return
	 */
	public static ImageView getImageView(Context context, String url, DMBitmap fb) {
		ImageView imageView = (ImageView)LayoutInflater.from(context).inflate(
				R.layout.view_banner, null);
		fb.display(imageView, url);
		return imageView;
	}
}
