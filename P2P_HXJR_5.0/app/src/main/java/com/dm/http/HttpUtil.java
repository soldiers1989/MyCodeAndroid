package com.dm.http;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.AssetManager;
import android.os.Build;

import com.dm.utils.ThreadsPool;
import com.hxjr.p2p.ad5.DMApplication;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

/**
 * Http操作处理类
 *
 * @author jiaohongyun
 */
public class HttpUtil {
    /**
     * 日志用的TAG
     */
    private static final String LOG_TAG = HttpUtil.class.getCanonicalName();

    private static HttpUtil myHttp = null;

    /**
     * 默认请求参数
     */
    private BaseHttpParams defaultParams;


    /**
     * 默认构造函数
     */
    public HttpUtil() {
        super();
    }

    public static HttpUtil getInstance() {
        if (myHttp == null) {
            myHttp = new HttpUtil();
        }
        return myHttp;

    }

    /**
     * @return 返回 defaultParams
     */
    public BaseHttpParams getDefaultParams() {
        return defaultParams;
    }

    /**
     * @param
     */
    private void setDefaultParams(BaseHttpParams defaultParams) {
        this.defaultParams = defaultParams;
    }

    /**
     * 设置默认的请求参数
     */
    public static void initDefaultHttpParams(BaseHttpParams defaultParams) {
        getInstance().setDefaultParams(defaultParams);
    }

    @SuppressLint("NewApi")
    public void post(Context context, String url, BaseHttpParams params, Map<String, File> fileMap, HttpCallBack
            callBack) {
        HttpTask task = new HttpTask(params, callBack, fileMap, context);
        int sdK = Build.VERSION.SDK_INT;
        if (sdK < 11) {
            task.execute(url);
        } else {
            task.executeOnExecutor(ThreadsPool.THREAD_POOL_EXECUTOR, url);
        }
    }

    @SuppressLint("NewApi")
    public void post(Context context, String url, BaseHttpParams params, HttpCallBack callBack) {
        HttpTask task = new HttpTask(params, callBack, context);
        int sdK = Build.VERSION.SDK_INT;
        if (sdK < 11) {
            task.execute(url);
        } else {
            task.executeOnExecutor(ThreadsPool.THREAD_POOL_EXECUTOR, url);
        }
    }

    @SuppressLint("NewApi")
    public void post(Context context, String url, HttpCallBack callBack) {
        post(context, url, null, callBack);
    }

    /**
     * 初始化https
     */
    public static void initSSL1() {
        try {
            MyHostnameVerifier myHostnameVerifier = new MyHostnameVerifier();
            MyX509TrustManager myX509TrustManager = new MyX509TrustManager();
            SSLContext sslContext = null;
            sslContext = SSLContext.getInstance("TLS");
            X509TrustManager[] xtmArray = new X509TrustManager[]{myX509TrustManager};
            sslContext.init(null, xtmArray, new java.security.SecureRandom());
            if (sslContext != null) {
                HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
            }
            HttpsURLConnection.setDefaultHostnameVerifier(myHostnameVerifier);
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化https
     */
    public static void initSSL2(Context context) {
        try {
            //取得SSL的SSLContext实例
            SSLContext sslContext = SSLContext.getInstance(CLIENT_AGREEMENT);
            //取得KeyManagerFactory实例
            KeyManagerFactory keyManager = KeyManagerFactory.getInstance(CLIENT_KEY_MANAGER);
            //取得TrustManagerFactory的X509密钥管理器
            TrustManagerFactory trustManager = TrustManagerFactory.getInstance(CLIENT_TRUST_MANAGER);

            //取得BKS密库实例
            KeyStore keyKeyStore = KeyStore.getInstance(CLIENT_KEY_KEYSTORE);
            KeyStore trustKeyStore = KeyStore.getInstance(CLIENT_TRUST_KEYSTORE);

            //加载证书和私钥,通过读取资源文件的方式读取密钥和信任证书（kclient:密钥;lt_client:信任证书）
            if (mAssetManager == null) {
                mAssetManager = context.getAssets();
            }
            InputStream is = mAssetManager.open("server_trust.keystore");
            //kclient:密钥
            keyKeyStore.load(is, CLIENT_KET_PASSWORD.toCharArray());
            is.reset();
            //lt_client:信任证书
            trustKeyStore.load(is, CLIENT_TRUST_PASSWORD.toCharArray());
            is.close();

            //初始化密钥管理器、信任证书管理器
            keyManager.init(keyKeyStore, CLIENT_KET_PASSWORD.toCharArray());
            trustManager.init(trustKeyStore);

            //初始化SSLContext
            sslContext.init(keyManager.getKeyManagers(), trustManager.getTrustManagers(), null);

            HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(new MyHostnameVerifier());
        } catch (UnrecoverableKeyException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * HttpUrlConnection 方式，支持指定load-der.crt证书验证，此种方式Android官方建议
     *
     * @throws CertificateException
     * @throws IOException
     * @throws KeyStoreException
     * @throws NoSuchAlgorithmException
     * @throws KeyManagementException
     */
    public static void initSSL3(DMApplication context) {
        handleSSLHandshake();
        try {
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            InputStream in = context.getAssets().open("server.crt");
            Certificate ca = cf.generateCertificate(in);

            KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
            keystore.load(null, null);
            keystore.setCertificateEntry("ca", ca);

            String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
            tmf.init(keystore);
            context.trustManager=tmf.getTrustManagers();

            // Create an SSLContext that uses our TrustManager
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, tmf.getTrustManagers(), null);
            HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(new MyHostnameVerifier());
//            URL url = new URL("https://www.todayfu.com/app/bid/publics/tjBidList.htm");
//            HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
//            urlConnection.setSSLSocketFactory(sslContext.getSocketFactory());
//            InputStream input = urlConnection.getInputStream();
//
//            BufferedReader reader = new BufferedReader(new InputStreamReader(input, "UTF-8"));
//            StringBuffer result = new StringBuffer();
//            String line = "";
//            while ((line = reader.readLine()) != null) {
//                result.append(line);
//            }
//            Log.e("TTTT", result.toString());
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 私钥密码
     */
    private static final String CLIENT_KET_PASSWORD = "123456";

    /**
     * 信任证书密码
     */
    private static final String CLIENT_TRUST_PASSWORD = "123456";

    /**
     * 使用协议
     */
    private static final String CLIENT_AGREEMENT = "TLS";

    /**
     * 密钥管理器
     */
    private static final String CLIENT_KEY_MANAGER = "X509";

    /**
     * 信任证书管理器
     */
    private static final String CLIENT_TRUST_MANAGER = "X509";

    /**
     * 密库，这里用的是BouncyCastle密库
     */
    private static final String CLIENT_KEY_KEYSTORE = "BKS";

    /**
     * 密库，这里用的是BouncyCastle密库
     */
    private static final String CLIENT_TRUST_KEYSTORE = "BKS";

    //    private static final String HTTS_URL = "https://192.168.7.39:8443/";

    private static AssetManager mAssetManager = null;

    //要实现x209证书认证
    static class MyX509TrustManager implements X509TrustManager {

        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {

        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {

            return null;
        }

    }

    //要实现主机名验证
    static class MyHostnameVerifier implements HostnameVerifier {

        @Override
        public boolean verify(String hostname, SSLSession session) {

            return true;
        }

    }
    /**
     * Enables https connections
     */
    @SuppressLint("TrulyRandom")
    public static void handleSSLHandshake() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }

                @Override
                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }};

            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String arg0, SSLSession arg1) {
                    return true;
                }
            });
        } catch (Exception ignored) {
        }
    }
}
