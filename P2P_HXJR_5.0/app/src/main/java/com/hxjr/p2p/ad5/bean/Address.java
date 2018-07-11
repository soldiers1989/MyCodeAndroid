package com.hxjr.p2p.ad5.bean;

import java.io.Serializable;

import org.json.JSONException;

import com.dm.utils.DMJsonObject;

/**
 * 开户所在地
 * @author  jiaohongyun
 * @version  [版本号, 2015年1月23日]
 */
public class Address implements Serializable
{
    /**
	 * 注释内容
	 */
    private static final long serialVersionUID = -2214051347740209104L;
    
    /**
	 * 编号
	 */
    private int id;
    
    /**
	 * 县
	 */
    private String xian;
    
    /**
	 * 市
	 */
    private String shi;
    
    /**
	 * 省
	 */
    private String sheng;
    
    public Address(DMJsonObject data)
    {
        try
        {
            id = data.getInt("id");
            xian = data.getString("xian");
            shi = data.getString("shi");
            sheng = data.getString("sheng");
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }
    
    public int getId()
    {
        return id;
    }
    
    public void setId(int id)
    {
        this.id = id;
    }
    
    public String getXian()
    {
        return xian;
    }
    
    public void setXian(String xian)
    {
        this.xian = xian;
    }
    
    public String getShi()
    {
        return shi;
    }
    
    public void setShi(String shi)
    {
        this.shi = shi;
    }
    
    public String getSheng()
    {
        return sheng;
    }
    
    public void setSheng(String sheng)
    {
        this.sheng = sheng;
    }
    
    @Override
    public String toString()
    {
        return sheng + shi + xian;
    }
    
}
