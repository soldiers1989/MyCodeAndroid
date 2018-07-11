/*
 * 文 件 名:  Deque.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  jiaohongyun
 * 修改时间:  2014年12月23日
 */
package com.dm.img.bitmap.core;

import android.graphics.Bitmap;

import com.dm.img.utils.Utils;

public class BaseMemoryCacheImpl implements IMemoryCache
{
    
    private final LruMemoryCache<String, Bitmap> mMemoryCache;
    
    public BaseMemoryCacheImpl(int size)
    {
        mMemoryCache = new LruMemoryCache<String, Bitmap>(size)
        {
            @Override
            protected int sizeOf(String key, Bitmap bitmap)
            {
                return Utils.getBitmapSize(bitmap);
            }
        };
    }
    
    @Override
    public void put(String key, Bitmap bitmap)
    {
        mMemoryCache.put(key, bitmap);
    }
    
    @Override
    public Bitmap get(String key)
    {
        return mMemoryCache.get(key);
    }
    
    @Override
    public void evictAll()
    {
        mMemoryCache.evictAll();
    }
    
    @Override
    public void remove(String key)
    {
        mMemoryCache.remove(key);
    }
    
}
