/*
 * 文 件 名:  Deque.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  jiaohongyun
 * 修改时间:  2014年12月23日
 */
package com.dm.img.bitmap.core;

import java.lang.ref.SoftReference;
import java.util.HashMap;

import android.graphics.Bitmap;

public class SoftMemoryCacheImpl implements IMemoryCache
{
    
    private final HashMap<String, SoftReference<Bitmap>> mMemoryCache;
    
    public SoftMemoryCacheImpl(int size)
    {
        
        mMemoryCache = new HashMap<String, SoftReference<Bitmap>>();
    }
    
    @Override
    public void put(String key, Bitmap bitmap)
    {
        mMemoryCache.put(key, new SoftReference<Bitmap>(bitmap));
    }
    
    @Override
    public Bitmap get(String key)
    {
        SoftReference<Bitmap> memBitmap = mMemoryCache.get(key);
        if (memBitmap != null)
        {
            return memBitmap.get();
        }
        return null;
    }
    
    @Override
    public void evictAll()
    {
        mMemoryCache.clear();
    }
    
    @Override
    public void remove(String key)
    {
        mMemoryCache.remove(key);
    }
    
}
