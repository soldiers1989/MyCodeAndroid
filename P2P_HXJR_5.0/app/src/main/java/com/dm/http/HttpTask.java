package com.dm.http;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;
import java.util.Random;
import java.util.zip.GZIPInputStream;

import javax.net.ssl.HttpsURLConnection;

import org.json.JSONException;
import org.json.JSONObject;

import com.dm.utils.CookieUtil;
import com.dm.utils.DMLog;
import com.dm.utils.NetworkUtils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;

public class HttpTask extends AsyncTask<String, Integer, JSONObject> {
	/**
	 * 日志用的TAG
	 */
	private static final String LOG_TAG = HttpTask.class.getCanonicalName();

	private static final String LINE_FEED = "\r\n";
	private static String multipartBoundary;
	private static char[] MULTIPART_CHARS = ("-_1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ")
			.toCharArray();

	/**
	 * 接收编码
	 */
	private static final String Accept_Encoding = "gzip";

	/**
	 * 超时时间，默认15秒
	 */
	private static int TIME_OUT = 15 * 1000;

	/**
	 * 是否上传文件
	 */
	private  boolean isPostFile = false;

	/**
	 * 以POST方法发出请求
	 */
	private static final String POST = "POST";

	/**
	 * 以GET方法发出请求
	 */
	@SuppressWarnings("unused")
	private static final String GET = "GET";

	/**
	 * 使用的字符集
	 */
	private String charset = "UTF-8";

	private HttpCallBack callBack;

	private BaseHttpParams params;

	private String targetUrl;

	/**
	 * 0成功 1失败
	 */
	private int resultCode;

	private Throwable throwable;

	private Context mContext;

	private Map<String, File> fileMap;

	public HttpTask(BaseHttpParams params, HttpCallBack callBack, Map<String, File> fileMap,
					Context context) {
		super();
		this.callBack = callBack;
		isPostFile = true;
		this.fileMap = fileMap;
		if (params == null) {
			this.params = HttpUtil.getInstance().getDefaultParams();
		} else {
			this.params = params;
		}
		mContext = context;

	}

	public HttpTask(BaseHttpParams params, HttpCallBack callBack, Context context) {
		super();
		this.callBack = callBack;
		isPostFile = false;
		if (params == null) {
			this.params = HttpUtil.getInstance().getDefaultParams();
		} else {
			this.params = params;
		}
		mContext = context;

	}

	public HttpTask(HttpCallBack callBack, Context context) {
		super();
		this.callBack = callBack;
		isPostFile = false;
		this.params = HttpUtil.getInstance().getDefaultParams();
		mContext = context;
	}

	@Override
	protected JSONObject doInBackground(String... urls) {
		publishProgress(0);
		targetUrl = urls[0];
		JSONObject jsonObject = null;
		if (!NetworkUtils.isNetworkAvailable(mContext)) {
			resultCode = 1;
			throwable = new DMException(DMException.NET_CONNECTION_ERROR, "网络连接不可用");
		} else {
			try {
				if (isPostFile) {
					jsonObject = httpPost(targetUrl, params.toStrings(), fileMap);
				} else {
					if (targetUrl.startsWith("https://")) {
						jsonObject = httpsPost(targetUrl, params.toString());
					} else {
						jsonObject = httpPost(targetUrl, params.toString());
					}
				}
			} catch (JSONException e) {
				resultCode = 1;
				throwable = e;
			}
		}
		return jsonObject;
	}

	@Override
	protected void onCancelled() {
		super.onCancelled();
		throwable = null;
		callBack.onFailure(throwable, mContext);
	}

	@Override
	protected void onPostExecute(JSONObject result) {
		super.onPostExecute(result);
		switch (resultCode) {
			case 0:
				callBack.onSuccess(result);
				break;
			case 1:
				callBack.onFailure(throwable, mContext);
				break;
			default:
				callBack.onSuccess(result);
				break;
		}
		publishProgress(1);
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		callBack.onStart();
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		super.onProgressUpdate(values);
		callBack.onLoading(values[0]);
		// if (callBack.isShowProgress())
		// {
		// if (values[0] == 0)
		// {
		// AppManager.getAppManager().getDialogShowing().show();
		// }
		// else if (values[0] == 1)
		// {
		// AppManager.getAppManager().getDialogShowing().dismiss();
		// }
		// }
	}

	private JSONObject httpPost(String url, String data, Map<String, File> fileMap)
			throws JSONException {
		DMLog.d(LOG_TAG, "httpPost()-->url :" + url);
		DMLog.d(LOG_TAG, "httpPost()-->data :" + data);
		String jsonStr = "";
		System.setProperty("http.keepAlive", "false");
		multipartBoundary = _generateMultipartBoundary();
		HttpURLConnection connection = null;
		DataOutputStream dataOutStream = null;
		try {
			connection = _openPostConnection(url);
			String sCookie = CookieUtil.getmCookie(mContext);
			if (sCookie != null && sCookie.length() > 0) {
				connection.setRequestProperty("Cookie", sCookie);
			}
			dataOutStream = new DataOutputStream(connection.getOutputStream());

			// 添加Post请求参数
			_doAddFormFields(dataOutStream, data);
			// 向HTTP请求添加上传文件部分
			_doAddFilePart(dataOutStream, fileMap);

			dataOutStream.writeBytes(LINE_FEED);
			dataOutStream.writeBytes("--" + multipartBoundary+"--" );
			dataOutStream.writeBytes(LINE_FEED);
			dataOutStream.close();
			int code = connection.getResponseCode();
			if (code == HttpURLConnection.HTTP_OK) {
				InputStream in = connection.getInputStream();
				BufferedReader rd = null;

				String contentEncoding = connection.getContentEncoding();
				if (contentEncoding != null && contentEncoding.equalsIgnoreCase("gzip")) {

					rd = new BufferedReader(
							new InputStreamReader(new GZIPInputStream(in), charset), 8192);
				} else {

					rd = new BufferedReader(new InputStreamReader(in, charset), 8192); // 对应的字符编码转换
				}
				String tempLine = rd.readLine();
				StringBuffer temp = new StringBuffer();
				while (tempLine != null) {
					temp.append(tempLine);
					tempLine = rd.readLine();
				}
				jsonStr = temp.toString();
				// Get the cookie
				String cookie = connection.getHeaderField("set-cookie");
				if (cookie != null && cookie.length() > 0) {
					CookieUtil.setmCookie(cookie, mContext);
				}
				rd.close();
				in.close();
			} else {
				DMLog.w(LOG_TAG, "Http Response Code:" + code);
				resultCode = 1;
				throwable = new DMException(code);
			}
		} catch (MalformedURLException e) {
			resultCode = 1;
			throwable = e;
			DMLog.e(LOG_TAG, e.getMessage());
		} catch (IOException e) {
			if (e instanceof SocketTimeoutException) {
				resultCode = 1;
				throwable = new DMException(DMException.CONNECT_TIME_OUT, "网络连接超时");
				DMLog.e(LOG_TAG, "SocketTimeoutException service failed !");
				return new JSONObject();
			} else if (e instanceof ConnectException) {
				resultCode = 1;
				throwable = new DMException(DMException.CAN_NOT_CONNECT_TO_SERVER, "服务器异常");
				DMLog.e(LOG_TAG, "ConnectException service failed !");
				return new JSONObject();
			} else {
				resultCode = 1;
				throwable = e;
				DMLog.e(LOG_TAG, "connect service failed !");
			}
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
		DMLog.d(LOG_TAG, "httpPost()-->result:" + jsonStr + url);
		resultCode = 0;
		return new JSONObject(jsonStr);
	}

	/**
	 * Http Post请求方法
	 *
	 * @param url
	 * @return
	 * @throws JSONException
	 * @see [类、类#方法、类#成员]
	 */
	@SuppressWarnings("deprecation")
	private JSONObject httpPost(String url, String data) throws JSONException {
		DMLog.d(LOG_TAG, "httpPost()-->urlurl:" + url);
		DMLog.d(LOG_TAG, "httpPost()-->data:" + data);
		String jsonStr = "";
		System.setProperty("http.keepAlive", "false");
		HttpURLConnection connection = null;
		try {
			String proxyHost = android.net.Proxy.getDefaultHost();
			if (proxyHost != null) {
				java.net.Proxy proxy = new java.net.Proxy(java.net.Proxy.Type.HTTP,
						new InetSocketAddress(android.net.Proxy.getDefaultHost(),
								android.net.Proxy.getDefaultPort()));
				connection = (HttpURLConnection) new URL(url).openConnection(proxy);
			} else {
				connection = (HttpURLConnection) new URL(url).openConnection();
			}

			connection.setDoInput(true);// 设置可以读取返回的数据
			connection.setDoOutput(true); // 设置可以提交数据
			connection.setChunkedStreamingMode(0);
			connection.setRequestProperty("Connection", "close");

			connection.setUseCaches(false);// 不用cache
			connection.setDefaultUseCaches(false);
			// 设置超时时间
			connection.setConnectTimeout(TIME_OUT);
			connection.setReadTimeout(TIME_OUT);

			connection.setRequestMethod(POST);
			connection.setRequestProperty("User-Agent", "Android/1.0");
			connection.setRequestProperty("Accept-Encoding", Accept_Encoding);
			// connection.setRequestProperty("Content-Type",
			// "application/json;charset=UTF-8");
			String sCookie = CookieUtil.getmCookie(mContext);
			if (sCookie != null && sCookie.length() > 0) {
				connection.setRequestProperty("Cookie", sCookie);
			}
			connection.getOutputStream().write(data.getBytes());
			connection.getOutputStream().flush();
			connection.getOutputStream().close();

			int code = connection.getResponseCode();
			if (code == HttpURLConnection.HTTP_OK) {
				InputStream in = connection.getInputStream();
				BufferedReader rd = null;

				String contentEncoding = connection.getContentEncoding();
				if (contentEncoding != null && contentEncoding.equalsIgnoreCase("gzip")) {

					rd = new BufferedReader(
							new InputStreamReader(new GZIPInputStream(in), charset), 8192);
				} else {

					rd = new BufferedReader(new InputStreamReader(in, charset), 8192); // 对应的字符编码转换
				}
				String tempLine = rd.readLine();
				StringBuffer temp = new StringBuffer();
				while (tempLine != null) {
					temp.append(tempLine);
					tempLine = rd.readLine();
				}
				jsonStr = temp.toString();
				// Get the cookie
				String cookie = connection.getHeaderField("set-cookie");
				if (cookie != null && cookie.length() > 0) {
					CookieUtil.setmCookie(cookie, mContext);
				}
				rd.close();
				in.close();
			} else {
				DMLog.w(LOG_TAG, "Http Response Code:" + code);
				resultCode = 1;
				throwable = new DMException(code);
			}
		} catch (MalformedURLException e) {
			resultCode = 1;
			throwable = e;
			DMLog.e(LOG_TAG, e.getMessage());
		} catch (IOException e) {
			if (e instanceof SocketTimeoutException) {
				resultCode = 1;
				throwable = new DMException(DMException.CONNECT_TIME_OUT, "网络连接超时");
				DMLog.e(LOG_TAG, "SocketTimeoutException service failed !");
				return new JSONObject();
			} else if (e instanceof ConnectException) {
				resultCode = 1;
				throwable = new DMException(DMException.CAN_NOT_CONNECT_TO_SERVER, "服务器异常");
				DMLog.e(LOG_TAG, "ConnectException service failed !");
				return new JSONObject();
			} else {
				resultCode = 1;
				throwable = e;
				DMLog.e(LOG_TAG, "connect service failed !");
			}
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
		DMLog.d(LOG_TAG, "httpPost()-->result:" + jsonStr + url);
		resultCode = 0;
		return new JSONObject(jsonStr);
	}

	/**
	 * Http Post请求方法
	 *
	 * @param url
	 * @return
	 * @throws JSONException
	 * @see [类、类#方法、类#成员]
	 */
	@SuppressLint("TrulyRandom")
	@SuppressWarnings("deprecation")
	private JSONObject httpsPost(String url, String data) throws JSONException {
		DMLog.i(LOG_TAG, "httpsPost()-->urlurl:" + url);
		DMLog.i(LOG_TAG, "httpsPost()-->data:" + data);
		String jsonStr = "";
		HttpsURLConnection connection = null;

		try {
			String proxyHost = android.net.Proxy.getDefaultHost();
			if (proxyHost != null) {
				java.net.Proxy proxy = new java.net.Proxy(java.net.Proxy.Type.HTTP,
						new InetSocketAddress(android.net.Proxy.getDefaultHost(),
								android.net.Proxy.getDefaultPort()));
				connection = (HttpsURLConnection) new URL(url).openConnection(proxy);
			} else {
				connection = (HttpsURLConnection) new URL(url).openConnection();
			}

			connection.setDoInput(true);// 设置可以读取返回的数据
			connection.setDoOutput(true); // 设置可以提交数据

			connection.setRequestProperty("Connection", "keep-alive");
			connection.setUseCaches(true);// 不用cache
			// 设置超时时间
			connection.setConnectTimeout(30000);
			connection.setReadTimeout(30000);

			connection.setRequestMethod("POST");
			connection.setRequestProperty("User-Agent", "Android/1.0");
			// connection.setRequestProperty("Accept-Encoding", "gzip");
			// connection.setRequestProperty("Content-Type",
			// "application/json;charset=UTF-8");
			String sCookie = CookieUtil.getmCookie(mContext);
			if (sCookie != null && sCookie.length() > 0) {
				connection.setRequestProperty("Cookie", sCookie);
			}
			connection.getOutputStream().write(data.getBytes());
			connection.getOutputStream().flush();
			connection.getOutputStream().close();

			int code = connection.getResponseCode();
			if (code == HttpURLConnection.HTTP_OK) {
				InputStream in = connection.getInputStream();
				BufferedReader rd = null;

				String contentEncoding = connection.getContentEncoding();
				if (contentEncoding != null && contentEncoding.equalsIgnoreCase("gzip")) {

					rd = new BufferedReader(
							new InputStreamReader(new GZIPInputStream(in), "utf-8"), 8192);
				} else {

					rd = new BufferedReader(new InputStreamReader(in, "utf-8"), 8192); // 对应的字符编码转换
				}
				String tempLine = rd.readLine();
				StringBuffer temp = new StringBuffer();
				while (tempLine != null) {
					temp.append(tempLine);
					tempLine = rd.readLine();
				}
				jsonStr = temp.toString();
				// Get the cookie
				String cookie = connection.getHeaderField("set-cookie");
				if (cookie != null && cookie.length() > 0) {
					CookieUtil.setmCookie(cookie, mContext);
				}
				rd.close();
				in.close();
				DMLog.e(LOG_TAG, "ok");
			} else {
				resultCode = 1;
				DMLog.e(LOG_TAG, "resultCode:" + resultCode);
				throwable = new DMException(code);
			}
		} catch (MalformedURLException e) {
			resultCode = 1;
			throwable = e;
			DMLog.e(LOG_TAG, e.getMessage());
		} catch (IOException e) {
			if (e instanceof SocketTimeoutException) {
				resultCode = 1;
				throwable = new DMException(DMException.CONNECT_TIME_OUT, "连接超时");
				DMLog.e(LOG_TAG, "connect service failed !");
			} else if (e instanceof ConnectException) {
				resultCode = 1;
				throwable = new DMException(DMException.CAN_NOT_CONNECT_TO_SERVER, "服务器异常");
				DMLog.e(LOG_TAG, "connect service failed !");
			} else {
				resultCode = 1;
				throwable = e;
				DMLog.e(LOG_TAG, "connect service failed !");
			}
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}

		return new JSONObject(jsonStr);
	}

	private static HttpURLConnection _openPostConnection(String purl) throws IOException {
		URL url = new URL(purl);
		String proxyHost = android.net.Proxy.getDefaultHost();
		HttpURLConnection connection = null;
		if (proxyHost != null) {
			java.net.Proxy proxy = new java.net.Proxy(java.net.Proxy.Type.HTTP,
					new InetSocketAddress(android.net.Proxy.getDefaultHost(),
							android.net.Proxy.getDefaultPort()));
			connection = (HttpURLConnection) url.openConnection(proxy);
		} else {
			connection = (HttpURLConnection) url.openConnection();
		}
		connection.setDoInput(true);
		connection.setUseCaches(false);
		connection.setDoOutput(true);
		connection.setConnectTimeout(TIME_OUT);
		connection.setReadTimeout(TIME_OUT);
		connection.setRequestMethod("POST");
		connection.setRequestProperty("Content-Type", "multipart/form-data; boundary="
				+ multipartBoundary);
		connection.setRequestProperty("User-Agent", "Android Client Agent");
		connection.setRequestProperty("Accept-Encoding", Accept_Encoding);

		return connection;
	}

	/**
	 * 生成HTTP协议中的边界字符串
	 *
	 * @return 边界字符串
	 */
	private static String _generateMultipartBoundary() {
		Random rand = new Random();
		char[] chars = new char[rand.nextInt(9) + 12]; // 随机长度(12 - 20个字符)
		for (int i = 0; i < chars.length; i++) {
			chars[i] = MULTIPART_CHARS[rand.nextInt(MULTIPART_CHARS.length)];
		}
		return "===AndroidFormBoundary" + new String(chars);
	}

	private static void _doAddFormFields(DataOutputStream oStream, String data) throws IOException {
		oStream.writeBytes("--" + multipartBoundary);
		oStream.writeBytes(LINE_FEED);

		oStream.writeBytes("Content-Disposition: form-data; name=\"" + "sign" + "\"");
		oStream.writeBytes(LINE_FEED);

		oStream.writeBytes(LINE_FEED);
		oStream.writeBytes(data);
		oStream.writeBytes(LINE_FEED);
	}

	/**
	 * 向HTTP请求添加上传文件部分
	 *
	 * @param oStream
	 *            由HTTPURLConnection获取的输出流
	 * @param fileMap
	 *            文件Map, key为文件域名, value为要上传的文件
	 */
	private static void _doAddFilePart(DataOutputStream oStream, Map<String, File> fileMap)
			throws IOException {
		if (fileMap == null || fileMap.isEmpty())
			return;

		for (Map.Entry<String, File> fileEntry : fileMap.entrySet()) {
			String fileName = fileEntry.getValue().getName();

			oStream.writeBytes("--" + multipartBoundary);
			oStream.writeBytes(LINE_FEED);

			oStream.writeBytes("Content-Disposition: form-data; name=\"" + fileEntry.getKey()
					+ "\"; filename=\"" + fileName + "\"");
			oStream.writeBytes(LINE_FEED);

			oStream.writeBytes("Content-Type: " + URLConnection.guessContentTypeFromName(fileName));
			oStream.writeBytes(LINE_FEED);
			oStream.writeBytes(LINE_FEED);

			InputStream iStream = null;
			try {
				iStream = new FileInputStream(fileEntry.getValue());
				byte[] buffer = new byte[4096];
				int bytesRead;
				while ((bytesRead = iStream.read(buffer)) != -1) {
					oStream.write(buffer, 0, bytesRead);
				}

				iStream.close();
				oStream.writeBytes(LINE_FEED);
				oStream.flush();
			} catch (IOException ignored) {
			} finally {
				try {
					if (iStream != null)
						iStream.close();
				} catch (Exception ignored) {
				}
			}
		}
	}
}
