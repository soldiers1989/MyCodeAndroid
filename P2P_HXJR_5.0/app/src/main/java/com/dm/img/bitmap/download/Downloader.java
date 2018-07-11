/*
 * 文 件 名:  Deque.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  jiaohongyun
 * 修改时间:  2014年12月23日
 */
package com.dm.img.bitmap.download;

public interface Downloader
{
    
    /**
     * 请求网络的inputStream填充outputStream
     * @param urlString
     * @param outputStream
     * @return
     */
    public byte[] download(String urlString);
}
