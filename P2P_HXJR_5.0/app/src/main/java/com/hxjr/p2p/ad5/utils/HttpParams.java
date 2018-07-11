package com.hxjr.p2p.ad5.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import com.dm.http.BaseHttpParams;
import com.dm.utils.DMLog;
import com.dm.utils.EncryptUtil;

/**
 * http请求参数
 * @author jiaohongyun
 *
 */
public class HttpParams extends BaseHttpParams
{
	private static final String TAG = HttpParams.class.getCanonicalName();
	
	@Override
	public String toString()
	{
		String data = jsonObject.toString();
		DMLog.d(TAG, data);
		String sign = EncryptUtil.base64Encrypt(data, DMConstant.Config.ENCRYPTKEY);
		String result = "";
		try
		{
			//防止乱码
			result = EncryptUtil.SIGN + "=" + URLEncoder.encode(sign, "UTF-8");
		}
		catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
		return result;
	}
	public String toStrings()
	{
		String data = jsonObject.toString();
		DMLog.d(TAG, data);
		String sign = EncryptUtil.base64Encrypt(data, DMConstant.Config.ENCRYPTKEY);
//		String result = "";
//		try
//		{
//			//防止乱码
////			result = URLEncoder.encode(sign, "UTF-8");
//		}
////		catch (UnsupportedEncodingException e)
//		{
//			e.printStackTrace();
//		}
		return sign;
	}
}
