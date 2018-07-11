/*
 * 文 件 名:  Deque.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  jiaohongyun
 * 修改时间:  2014年12月23日
 */
package com.dm.img.bitmap.core;

import android.graphics.Bitmap;
import android.view.animation.Animation;

public class BitmapDisplayConfig
{
    
    private int bitmapWidth;
    
    private int bitmapHeight;
    
    private Animation animation;
    
    private int animationType;
    
    private Bitmap loadingBitmap;
    
    private Bitmap loadfailBitmap;
    
    public int getBitmapWidth()
    {
        return bitmapWidth;
    }
    
    public void setBitmapWidth(int bitmapWidth)
    {
        this.bitmapWidth = bitmapWidth;
    }
    
    public int getBitmapHeight()
    {
        return bitmapHeight;
    }
    
    public void setBitmapHeight(int bitmapHeight)
    {
        this.bitmapHeight = bitmapHeight;
    }
    
    public Animation getAnimation()
    {
        return animation;
    }
    
    public void setAnimation(Animation animation)
    {
        this.animation = animation;
    }
    
    public int getAnimationType()
    {
        return animationType;
    }
    
    public void setAnimationType(int animationType)
    {
        this.animationType = animationType;
    }
    
    public Bitmap getLoadingBitmap()
    {
        return loadingBitmap;
    }
    
    public void setLoadingBitmap(Bitmap loadingBitmap)
    {
        this.loadingBitmap = loadingBitmap;
    }
    
    public Bitmap getLoadfailBitmap()
    {
        return loadfailBitmap;
    }
    
    public void setLoadfailBitmap(Bitmap loadfailBitmap)
    {
        this.loadfailBitmap = loadfailBitmap;
    }
    
    public class AnimationType
    {
        public static final int userDefined = 0;
        
        public static final int fadeIn = 1;
    }
    
}
