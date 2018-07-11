package com.dm.http;

import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * http请求参数
 * @author jiaohongyun
 *
 */
public abstract class BaseHttpParams
{
	private static final String TAG = BaseHttpParams.class.getCanonicalName();
	
	protected JSONObject jsonObject;
	
	protected BaseHttpParams()
	{
		init();
	}
	
	protected BaseHttpParams(Map<String, Object> source)
	{
		init();
		
		for (Map.Entry<String, Object> entry : source.entrySet())
		{
			put(entry.getKey(), entry.getValue());
		}
	}
	
	protected BaseHttpParams(String key, Object value)
	{
		init();
		put(key, value);
	}
	
	protected BaseHttpParams(Object... keysAndValues)
	{
		init();
		int len = keysAndValues.length;
		if (len % 2 != 0)
			throw new IllegalArgumentException("Supplied arguments must be even");
		for (int i = 0; i < len; i += 2)
		{
			String key = String.valueOf(keysAndValues[i]);
			String val = String.valueOf(keysAndValues[i + 1]);
			put(key, val);
		}
	}
	
	private void init()
	{
		jsonObject = new JSONObject();
	}
	
	public void put(String key, Object value)
	{
		if (key != null && value != null)
		{
			try
			{
				jsonObject.put(key, value);
			}
			catch (JSONException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	public void remove(String key)
	{
		jsonObject.remove(key);
	}
	
	public abstract String toString();
	public abstract String toStrings();
}
