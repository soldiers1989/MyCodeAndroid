/*
 * 文 件 名:  BidProgres.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  zhoulantao
 * 修改时间:  2015年11月25日
 */
package com.hxjr.p2p.ad5.bean;

import org.json.JSONException;

import com.dm.utils.DMJsonObject;

/**
 * 投标进度
 * 
 * @author  zhoulantao
 * @version  [版本号, 2015年11月25日]
 */
public class BidProgress
{
    /**
     * 标题时间
     */
    private String titleTime;
    
    /**
     * 简介
     */
    private String introduction;
    
    /**
     * 更多
     */
    private String moreUrl;
    
    public BidProgress()
	{
	}
    
    public BidProgress(DMJsonObject data)
	{
    	try
		{
    		titleTime = data.getString("titleTime");
    		introduction = data.getString("introduction");
    		moreUrl = data.getString("moreUrl");
		}
		catch (JSONException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
	}

	public String getTitleTime()
    {
        return titleTime;
    }
    
    public void setTitleTime(String titleTime)
    {
        this.titleTime = titleTime;
    }
    
    public String getIntroduction()
    {
        return introduction;
    }
    
    public void setIntroduction(String introduction)
    {
        this.introduction = introduction;
    }
    
    public String getMoreUrl()
    {
        return moreUrl;
    }
    
    public void setMoreUrl(String moreUrl)
    {
        this.moreUrl = moreUrl;
    }
    
}
