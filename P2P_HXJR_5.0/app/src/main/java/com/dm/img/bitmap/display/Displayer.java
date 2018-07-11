/*
 * 文 件 名:  Deque.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  jiaohongyun
 * 修改时间:  2014年12月23日
 */
package com.dm.img.bitmap.display;

import android.graphics.Bitmap;
import android.view.View;

import com.dm.img.bitmap.core.BitmapDisplayConfig;

public interface Displayer
{
    
    /**
     * 图片加载完成 回调的函数
     * @param imageView
     * @param bitmap
     * @param config
     */
    public void loadCompletedisplay(View imageView, Bitmap bitmap, BitmapDisplayConfig config);
    
    /**
     * 图片加载失败回调的函数
     * @param imageView
     * @param bitmap
     */
    public void loadFailDisplay(View imageView, Bitmap bitmap);
    
}
