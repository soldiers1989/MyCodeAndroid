package com.hxjr.p2p.ad5.utils;

import android.content.Context;
import android.content.Intent;
import android.webkit.JavascriptInterface;

import com.hxjr.p2p.ad5.ui.investment.bid.ImageViewerActivity;

public class JSInterface
{
	private Context mContext;
	
	private StringBuilder dates = new StringBuilder();
	
	public JSInterface(Context context)
	{
		mContext = context;
	}
	
	@JavascriptInterface
	public void showBigImg(String imgUrl)
	{
		if (!FormatUtil.validateUrl(imgUrl))
		{
			return;
		}
		Intent intent = new Intent(mContext, ImageViewerActivity.class);
		intent.putExtra("imgUrl", imgUrl);
		mContext.startActivity(intent);
	}
}
