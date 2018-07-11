package com.dm.utils;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import android.util.Base64;

/**
 * 加密工具类
 * @author  jiaohongyun
 * @date 2015年6月5日
 */
public class EncrypUtil
{
	private static final String IV_STRING = "8532164579245368";
	
	/**
	 * 加密
	 * @param cleartext
	 * @param dataPassword
	 * @return
	 */
	public static String encrypt(String cleartext, String dataPassword)
	{
		byte[] encryptedData = null;
		try
		{
			IvParameterSpec zeroIv = new IvParameterSpec(IV_STRING.getBytes("utf-8"));
			SecretKeySpec key = new SecretKeySpec(dataPassword.getBytes("utf-8"), "AES");
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, key, zeroIv);
			encryptedData = cipher.doFinal(cleartext.getBytes("utf-8"));
		}
		catch (InvalidKeyException e)
		{
			e.printStackTrace();
		}
		catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
		catch (NoSuchAlgorithmException e)
		{
			e.printStackTrace();
		}
		catch (NoSuchPaddingException e)
		{
			e.printStackTrace();
		}
		catch (InvalidAlgorithmParameterException e)
		{
			e.printStackTrace();
		}
		catch (IllegalBlockSizeException e)
		{
			e.printStackTrace();
		}
		catch (BadPaddingException e)
		{
			e.printStackTrace();
		}
		
		return Base64.encodeToString(encryptedData, Base64.NO_WRAP);
	}
	
	/**
	 * 解密
	 * @param dataPassword
	 * @param encrypted
	 * @return
	 */
	public static String decrypt(String dataPassword, String encrypted)
	{
		byte[] decryptedData = null;
		String res = null;
		try
		{
			byte[] byteMi = Base64.decode(encrypted, Base64.NO_WRAP);
			IvParameterSpec zeroIv = new IvParameterSpec(IV_STRING.getBytes("utf-8"));
			SecretKeySpec key = new SecretKeySpec(dataPassword.getBytes(), "AES");
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, key, zeroIv);
			decryptedData = cipher.doFinal(byteMi);
			res = new String(decryptedData, "utf-8");
		}
		catch (InvalidKeyException e)
		{
			e.printStackTrace();
		}
		catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
		catch (NoSuchAlgorithmException e)
		{
			e.printStackTrace();
		}
		catch (NoSuchPaddingException e)
		{
			e.printStackTrace();
		}
		catch (InvalidAlgorithmParameterException e)
		{
			e.printStackTrace();
		}
		catch (IllegalBlockSizeException e)
		{
			e.printStackTrace();
		}
		catch (BadPaddingException e)
		{
			e.printStackTrace();
		}
		
		return res;
	}
	
}
