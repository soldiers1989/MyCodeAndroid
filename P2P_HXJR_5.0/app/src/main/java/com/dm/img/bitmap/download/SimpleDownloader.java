/*
 * 文 件 名:  Deque.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  jiaohongyun
 * 修改时间:  2014年12月23日
 */
package com.dm.img.bitmap.download;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Locale;

import android.util.Log;

/**
 * 根据 图片url地址下载图片 可以是本地和网络
 * @author  jiaohongyun
 * @version  [版本号, 2014年12月23日]
 */
public class SimpleDownloader implements Downloader
{
	
	private static final String TAG = SimpleDownloader.class.getSimpleName();
	
	private static final int IO_BUFFER_SIZE = 8 * 1024; // 8k
	
	@Override
	public byte[] download(String urlString)
	{
		if (urlString == null)
			return null;
			
		if (urlString.trim().toLowerCase(Locale.getDefault()).startsWith("http"))
		{
			return getFromHttp(urlString);
		}
		else if (urlString.trim().toLowerCase(Locale.getDefault()).startsWith("file:"))
		{
			try
			{
				File f = new File(new URI(urlString));
				if (f.exists() && f.canRead())
				{
					return getFromFile(f);
				}
			}
			catch (URISyntaxException e)
			{
				Log.e(TAG, "Error in read from file - " + urlString + " : " + e);
			}
		}
		else
		{
			File f = new File(urlString);
			if (f.exists() && f.canRead())
			{
				return getFromFile(f);
			}
		}
		
		return null;
	}
	
	private byte[] getFromFile(File file)
	{
		if (file == null)
			return null;
			
		FileInputStream fis = null;
		try
		{
			fis = new FileInputStream(file);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			int len = 0;
			byte[] buffer = new byte[1024];
			while ((len = fis.read(buffer)) != -1)
			{
				baos.write(buffer, 0, len);
			}
			return baos.toByteArray();
		}
		catch (Exception e)
		{
			Log.e(TAG, "Error in read from file - " + file + " : " + e);
		}
		finally
		{
			if (fis != null)
			{
				try
				{
					fis.close();
					fis = null;
				}
				catch (IOException e)
				{
					// do nothing
				}
			}
		}
		
		return null;
	}
	
	protected static final int MAX_REDIRECT_COUNT = 5;
	
	private HttpURLConnection createConnection(String downloadUrl)
	{
		HttpURLConnection urlConnection = null;
		URL url;
		try
		{
			url = new URL(downloadUrl);
			urlConnection = (HttpURLConnection)url.openConnection();
		}
		catch (MalformedURLException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		urlConnection.setInstanceFollowRedirects(true);
		return urlConnection;
	}
	
	private byte[] getFromHttp(String urlString)
	{
		HttpURLConnection urlConnection = null;
		BufferedOutputStream out = null;
		FlushedInputStream in = null;
		
		try
		{
			urlConnection = createConnection(urlString);
			int redirectCount = 0;
			while (urlConnection.getResponseCode() / 100 == 3 && redirectCount < MAX_REDIRECT_COUNT)
			{
				urlConnection = createConnection(urlConnection.getHeaderField("Location"));
				redirectCount++;
			}
			int connCode = urlConnection.getResponseCode();
			if (200 == connCode)
			{
				in = new FlushedInputStream(new BufferedInputStream(urlConnection.getInputStream(), IO_BUFFER_SIZE));
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				int b;
				while ((b = in.read()) != -1)
				{
					baos.write(b);
				}
				//Get the cookie 使用图片验证码的时候要用的
				//            String cookie = urlConnection.getHeaderField("set-cookie");
				//            if (cookie != null && cookie.length() > 0)
				//            {
				//                CookieUtil.setmCookie(cookie,);
				//            }
				return baos.toByteArray();
			}
			
		}
		catch (final IOException e)
		{
			Log.e(TAG, "Error in downloadBitmap - " + urlString + " : " + e);
		}
		finally
		{
			if (urlConnection != null)
			{
				urlConnection.disconnect();
			}
			try
			{
				if (out != null)
				{
					out.close();
				}
				if (in != null)
				{
					in.close();
				}
			}
			catch (final IOException e)
			{
			}
		}
		return null;
	}
	//    
	//	private byte[] getFromHttps(String urlString)
	//	{
	//		HttpsURLConnection urlConnection = null;
	//		BufferedOutputStream out = null;
	//		FlushedInputStream in = null;
	//		
	//		try
	//		{
	//			final URL url = new URL(urlString);
	//			urlConnection = (HttpsURLConnection)url.openConnection();
	//			int connCode = urlConnection.getResponseCode();
	//			if (200 == connCode)
	//			{
	//				in = new FlushedInputStream(new BufferedInputStream(urlConnection.getInputStream(), IO_BUFFER_SIZE));
	//				ByteArrayOutputStream baos = new ByteArrayOutputStream();
	//				int b;
	//				while ((b = in.read()) != -1)
	//				{
	//					baos.write(b);
	//				}
	//				//Get the cookie 使用图片验证码的时候要用的
	//				//            String cookie = urlConnection.getHeaderField("set-cookie");
	//				//            if (cookie != null && cookie.length() > 0)
	//				//            {
	//				//                CookieUtil.setmCookie(cookie,);
	//				//            }
	//				return baos.toByteArray();
	//			}
	//			else
	//			{
	//				return null;
	//			}
	//			
	//		}
	//		catch (final IOException e)
	//		{
	//			Log.e(TAG, "Error in downloadBitmap - " + urlString + " : " + e);
	//			return null;
	//		}
	//		finally
	//		{
	//			if (urlConnection != null)
	//			{
	//				urlConnection.disconnect();
	//			}
	//			try
	//			{
	//				if (out != null)
	//				{
	//					out.close();
	//				}
	//				if (in != null)
	//				{
	//					in.close();
	//				}
	//			}
	//			catch (final IOException e)
	//			{
	//			}
	//		}
	//	}
	
	public class FlushedInputStream extends FilterInputStream
	{
		public FlushedInputStream(InputStream inputStream)
		{
			super(inputStream);
		}
		
		@Override
		public long skip(long n) throws IOException
		{
			long totalBytesSkipped = 0L;
			while (totalBytesSkipped < n)
			{
				long bytesSkipped = in.skip(n - totalBytesSkipped);
				if (bytesSkipped == 0L)
				{
					int by_te = read();
					if (by_te < 0)
					{
						break; // we reached EOF
					}
					else
					{
						bytesSkipped = 1; // we read one byte
					}
				}
				totalBytesSkipped += bytesSkipped;
			}
			return totalBytesSkipped;
		}
	}
}
