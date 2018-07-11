/*
 * 文 件 名:  Deque.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  jiaohongyun
 * 修改时间:  2014年12月23日
 */
package com.dm.img.bitmap.core;

import java.util.ArrayList;

public class BytesBufferPool
{
    
    public static class BytesBuffer
    {
        public byte[] data;
        
        public int offset;
        
        public int length;
        
        private BytesBuffer(int capacity)
        {
            this.data = new byte[capacity];
        }
    }
    
    private final int mPoolSize;
    
    private final int mBufferSize;
    
    private final ArrayList<BytesBuffer> mList;
    
    public BytesBufferPool(int poolSize, int bufferSize)
    {
        mList = new ArrayList<BytesBuffer>(poolSize);
        mPoolSize = poolSize;
        mBufferSize = bufferSize;
    }
    
    public synchronized BytesBuffer get()
    {
        int n = mList.size();
        return n > 0 ? mList.remove(n - 1) : new BytesBuffer(mBufferSize);
    }
    
    public synchronized void recycle(BytesBuffer buffer)
    {
        if (buffer.data.length != mBufferSize)
            return;
        if (mList.size() < mPoolSize)
        {
            buffer.offset = 0;
            buffer.length = 0;
            mList.add(buffer);
        }
    }
    
    public synchronized void clear()
    {
        mList.clear();
    }
}
