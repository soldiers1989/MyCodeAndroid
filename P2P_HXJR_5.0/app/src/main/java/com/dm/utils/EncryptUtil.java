package com.dm.utils;

import java.io.UnsupportedEncodingException;

import android.util.Base64;

/**
 * 加密工具类
 * @author jiaohongyun
 *
 */
public class EncryptUtil
{
	
	public static final String SIGN = "sign";
	
	/**
	 * 
	 * @param data
	 * @param key
	 * @return
	 */
	public static String base64Encrypt(String data, String key)
	{
		if (data == null)
		{
			return "";
		}
		String encode = "";
		try
		{
			encode = new String(Base64.encode(data.getBytes("utf-8"), Base64.NO_WRAP));
		}
		catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
		encode += key;
		byte[] b = encode.getBytes();
		for (int i = 0; i < b.length / 2; i++)
		{
			byte temp = b[i];
			b[i] = b[b.length - 1 - i];
			b[b.length - 1 - i] = temp;
		}
		return new String(b);
		
	}
	
	//	/*
	//	 * MD5加密
	//	 */
	//	public static String getMD5Str(String str)
	//	{
	//		MessageDigest messageDigest = null;
	//		
	//		try
	//		{
	//			messageDigest = MessageDigest.getInstance("MD5");
	//			
	//			messageDigest.reset();
	//			
	//			messageDigest.update(str.getBytes("UTF-8"));
	//		}
	//		catch (NoSuchAlgorithmException e)
	//		{
	//			e.printStackTrace();
	//			//            System.exit(-1);
	//		}
	//		catch (UnsupportedEncodingException e)
	//		{
	//			e.printStackTrace();
	//		}
	//		
	//		byte[] byteArray = messageDigest.digest();
	//		
	//		StringBuffer md5StrBuff = new StringBuffer();
	//		
	//		for (int i = 0; i < byteArray.length; i++)
	//		{
	//			if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
	//				md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
	//			else
	//				md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
	//		}
	//		// 16位加密，从第9位到25位
	//		return md5StrBuff.substring(8, 24).toString().toUpperCase(Locale.getDefault());
	//	}
}
