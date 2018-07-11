package com.dm.utils;

import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 * JSON工具类
 * 
 * @author jiaohongyun
 *
 */
public class DMJsonObject extends JSONObject {

	public DMJsonObject() {
		super();
	}

	public DMJsonObject(JSONObject copyFrom, String[] names)
			throws JSONException {
		super(copyFrom, names);
	}

	public DMJsonObject(JSONTokener readFrom) throws JSONException {
		super(readFrom);
	}

	public DMJsonObject(Map<Object, Object> copyFrom) {
		super(copyFrom);
	}

	public DMJsonObject(String json) throws JSONException {
		super(json);
	}

	@Override
	public Object get(String name) throws JSONException {
		return super.has(name) ? super.get(name) : null;
	}

	@Override
	public boolean getBoolean(String name) throws JSONException {
		return super.has(name) ? super.getBoolean(name) : false;
	}

	public boolean getBoolean(String name, boolean flag) throws JSONException {
		return super.has(name) ? super.getBoolean(name) : flag;
	}

	@Override
	public double getDouble(String name) throws JSONException {
		return super.has(name) ? super.getDouble(name) : 0d;
	}

	@Override
	public int getInt(String name) throws JSONException {
		return super.has(name) ? super.getInt(name) : 0;
	}

	@Override
	public JSONArray getJSONArray(String name) throws JSONException {
		return super.has(name) ? super.getJSONArray(name) : new JSONArray();
	}

	@Override
	public JSONObject getJSONObject(String name) throws JSONException {
		return super.has(name) ? super.getJSONObject(name) : null;
	}

	@Override
	public long getLong(String name) throws JSONException {
		return super.has(name) ? super.getLong(name) : 0L;
	}

	@Override
	public String getString(String name) throws JSONException {
		return super.has(name) ? super.getString(name) : null;
	}

}
