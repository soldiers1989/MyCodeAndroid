package com.dm.http;

/**
 * @author  jiaohongyun
 * @date 2015年7月15日
 */
public class DMException extends Throwable
{
	/**
	 * 网络连接不可用
	 */
	public static final int NET_CONNECTION_ERROR = -1;
	
	/**
	 * 连接不上服务器
	 */
	public static final int CAN_NOT_CONNECT_TO_SERVER = -2;
	
	/**
	 * 连接超时
	 */
	public static final int CONNECT_TIME_OUT = -3;
	
	private int code;
	
	private String description;
	
	/** 
	 * 构造方法
	 */
	public DMException()
	{
		super();
	}
	
	/** 
	 * 构造方法
	 */
	public DMException(int code)
	{
		super();
		this.code = code;
	}
	
	/** 
	 * 构造方法
	 */
	public DMException(int code, String description)
	{
		super();
		this.code = code;
		this.description = description;
	}
	
	/**
	 * @return 返回 code
	 */
	public int getCode()
	{
		return code;
	}
	
	/**
	 * @param 对code进行赋值
	 */
	public void setCode(int code)
	{
		this.code = code;
	}
	
	/**
	 * @return 返回 description
	 */
	public String getDescription()
	{
		return description;
	}
	
	/**
	 * @param 对description进行赋值
	 */
	public void setDescription(String description)
	{
		this.description = description;
	}
	
}
