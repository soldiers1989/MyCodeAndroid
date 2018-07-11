package com.hxjr.p2p.ad5;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import com.alibaba.sdk.android.push.CloudPushService;
import com.alibaba.sdk.android.push.CommonCallback;
import com.alibaba.sdk.android.push.noonesdk.PushServiceFactory;
import com.dm.db.DbUtils;
import com.dm.http.HttpCallBack;
import com.dm.http.HttpUtil;
import com.dm.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.dm.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.dm.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.dm.universalimageloader.core.DisplayImageOptions;
import com.dm.universalimageloader.core.ImageLoader;
import com.dm.universalimageloader.core.ImageLoaderConfiguration;
import com.dm.universalimageloader.core.assist.QueueProcessingType;
import com.dm.universalimageloader.core.download.BaseImageDownloader;
import com.dm.universalimageloader.utils.StorageUtils;
import com.dm.utils.CookieUtil;
import com.dm.utils.DMJsonObject;
import com.dm.utils.DMLog;
import com.dm.utils.EncrypUtil;
import com.dm.utils.MD5Util;
import com.dm.utils.PRNGFixes;
import com.hxjr.p2p.ad5.bean.LockPwd;
import com.hxjr.p2p.ad5.bean.RegexInfo;
import com.hxjr.p2p.ad5.bean.UserInfo;
import com.hxjr.p2p.ad5.service.RegisterInfoService;
import com.hxjr.p2p.ad5.ui.mine.user.TimingCallBack;
import com.hxjr.p2p.ad5.utils.DMConstant;
import com.hxjr.p2p.ad5.utils.ErrorUtil;
import com.hxjr.p2p.ad5.utils.HttpParams;
import com.hxjr.p2p.ad5.utils.SharedPreferenceUtils;
import com.hxjr.p2p.ad5.utils.UIHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import javax.net.ssl.TrustManager;

public class DMApplication extends Application {
    private static final String LOG_FLAG = DMApplication.class.getCanonicalName();

    /**
     * 应用对象
     */
    private static DMApplication dmApp;

    /**
     * 用来判断是从哪个界面跳转到LoginActivity
     * 3: 表示MainActivity点击我的进入
     * 2: 表示从发现界面点击推广有奖
     * 其他表示默认值  -1
     */
    public static int toLoginValue = -1;

    /**
     * 用户信息
     */
    private UserInfo userInfo;

    /**
     * 是否自动登录
     */
    public boolean autoLogin;

    /**
     * 客户端与服务端时间差
     */
    public long diffTime;

    /**
     * 是否为第一次启动应用
     */
    public boolean isFirstRun = true;

    /**
     * 是否刚刚更新
     */
    public boolean isUpdate = false;

    public TrustManager[] trustManager;

    private DbUtils db;

    private RegexInfo regexInfo;

    private DisplayImageOptions options;

    public DisplayImageOptions getOptions() {
        return options;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        DMLog.i(LOG_FLAG, "onCreate()");
        dmApp = this;
        initSysConfig();
        readSharedPreferences();
        initImageLoader();
        //主要用于更新登录状态和更新客户端和服务器端时间差
        Intent intent = new Intent();
        intent.setAction(DMConstant.BroadcastActions.USER_SESSION_UPDATE_COOKIE);
        sendBroadcast(intent);
        startService(new Intent(dmApp, RegisterInfoService.class));//从服务端获取用户名和密码规则
        initBidFlagImgs();
        // 初始化阿里云推送
        initCloudChannel(this);
        //收集错误日志
//        CrashHandler.getInstance().init(getApplicationContext());
    }

    private void initImageLoader() {
        String filePath =
                Environment.getExternalStorageDirectory() + "/Android/data/" + getApplicationContext().getPackageName
                        () +
                        "/cache/";
        File cacheDir = StorageUtils.getOwnCacheDirectory(getApplicationContext(), filePath);// 获取到缓存的目录地址
        Log.d("cacheDir", cacheDir.getPath());
        // 创建配置ImageLoader(所有的选项都是可选的,只使用那些你真的想定制)，这个可以设定在APPLACATION里面，设置为全局的配置参数
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                // .memoryCacheExtraOptions(480, 800) // max width, max
                // height，即保存的每个缓存文件的最大长宽
                // .discCacheExtraOptions(480, 800, CompressFormat.JPEG,
                // 75, null) // Can slow ImageLoader, use it carefully
                // (Better don't use it)设置缓存的详细信息，最好不要设置这个
                .threadPoolSize(3)
                // 线程池内加载的数量
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new WeakMemoryCache())
                // .memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024
                // * 1024)) // You can pass your own memory cache
                // implementation你可以通过自己的内存缓存实现
                .memoryCacheSize(2 * 1024 * 1024)
                // /.discCacheSize(50 * 1024 * 1024)
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                // 将保存的时候的URI名称用MD5
                // 加密
                // .discCacheFileNameGenerator(new
                // HashCodeFileNameGenerator())//将保存的时候的URI名称用HASHCODE加密
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                // .discCacheFileCount(100) //缓存的File数量
                .discCache(new UnlimitedDiskCache(cacheDir))
                // 自定义缓存路径
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
                .imageDownloader(new BaseImageDownloader(getApplicationContext(), 5 * 1000, 30 * 1000))
                // connectTimeout (5 s),
                // readTimeout(30)// 超时时间
                .writeDebugLogs()
                // Remove for release app
                .build();
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);// 全局初始化此配置

        options = new DisplayImageOptions.Builder().cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                //                .showImageOnLoading(R.drawable.image_loading)
                //                .showStubImage(R.drawable.image_loading)          // 设置图片下载期间显示的图片
                //                .showImageForEmptyUri(R.drawable.load_error)  // 设置图片Uri为空或是错误的时候显示的图片
                //                .showImageOnFail(R.drawable.load_error)// 设置图片加载或解码过程中发生错误显示的图片
                .build();
    }

    /**
     * 初始化标的标识可选图片
     */
    private void initBidFlagImgs() {
        UIHelper.initBidTyepImgs();
    }

    /**
     * 初始化系统配置
     */
    private void initSysConfig() {
        //是否打印日志
        DMLog.setLogAtt(DMConstant.Config.IS_LOG);
        HttpUtil.initDefaultHttpParams(new HttpParams());//设置默认http/https请求参数
        switch (DMConstant.Config.API_MODE) {
            case 0: {
                break;
            }
            case 1: {
                try {
                    PRNGFixes.apply();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                HttpUtil.initSSL1();
                break;
            }
            case 2: {
                try {
                    PRNGFixes.apply();
                } catch (Exception e) {
                    e.printStackTrace();
                }
//                HttpUtil.initSSL1();
                HttpUtil.initSSL3(dmApp);
                break;
            }
            default:
                break;
        }
    }

    /**
     * 读取保存的共享信息
     */
    private void readSharedPreferences() {
        String cookies = (String) SharedPreferenceUtils.get(dmApp, SharedPreferenceUtils.KEY_COOKIE, "");
        //防止覆盖安装时出现cookie为空，但却自动登录而导致崩溃的问题；
        if ("".equals(cookies)) {
            SharedPreferenceUtils.put(dmApp, SharedPreferenceUtils.KEY_AUTO_LOGIN, false);
            return;
        }
        autoLogin = (Boolean) SharedPreferenceUtils.get(dmApp, SharedPreferenceUtils.KEY_AUTO_LOGIN, false);
        isFirstRun = (Boolean) SharedPreferenceUtils.get(dmApp, SharedPreferenceUtils.KEY_IS_FIRST_RUN, true);
        isUpdate = (Boolean) SharedPreferenceUtils.get(dmApp, SharedPreferenceUtils.KEY_IS_UPDATE, false);
        if (autoLogin) {
            CookieUtil.setmCookie(cookies, DMApplication.this);
            autoLogin(false, null, null);
        }
    }

    /**
     * 更新timing并自动登录
     *
     * @param flag    是否显示加载进度
     * @param lockPwd
     */
    public void autoLogin(final boolean flag, final LockPwd lockPwd, final TimingCallBack callBack) {
        DMLog.i(LOG_FLAG, "autoLogin()");
        boolean isThirdLogin = (Boolean) SharedPreferenceUtils.get(this, "isThirdLogin", false);
        if (isThirdLogin) {
            if (null != lockPwd) {
                thirdLogin(lockPwd);
            }
        } else {
            getTiming(flag, lockPwd, callBack);
        }
    }

    private void getTiming(final boolean flag, final LockPwd lockPwd, final TimingCallBack callBack) {
        String url = DMConstant.API_Url.SYS_KEEPSESSION;
        HttpParams params = new HttpParams();
        params.put("verType", 1 + "");
        HttpUtil.getInstance().post(dmApp, url, params, new HttpCallBack() {
            @Override
            public void onSuccess(JSONObject result) {
                try {
                    String code = result.getString("code");
                    long cTime = System.currentTimeMillis();
                    if (code.equals(DMConstant.ResultCode.SUCCESS)) {
                        JSONObject data = result.getJSONObject("data");
                        long sTime = data.getLong("time");
                        diffTime = cTime - sTime;
                        String time = data.getString("time");
                        if (lockPwd != null) {
                            login(lockPwd, time);
                        }
                        if (callBack != null) {
                            callBack.doTimingCallBack();
                        }
                        String serviceTel = data.getString("tel");
                        String kfQQ = data.getString("kfQQ");
                        SharedPreferenceUtils.put(dmApp, "consts", "tel", serviceTel);
                        SharedPreferenceUtils.put(dmApp, "consts", "kfQQ", kfQQ);
                    } else {
                        ErrorUtil.showError(result);
                        if (callBack != null) {
                            callBack.onServerError();
                        }
                    }
                } catch (JSONException e) {
                    DMLog.e(LOG_FLAG, e.getMessage());
                    if (callBack != null) {
                        callBack.onServerError();
                    }
                }
            }

            @Override
            public void onStart() {
                this.setShowProgress(flag);
                if (callBack != null) {
                    callBack.onStart();
                }
            }

            @Override
            public void onFailure(Throwable t, Context context) {
                DMLog.e(LOG_FLAG, "autoLogin()-->onFailure:" + t.getMessage());
                if (callBack != null) {
                    callBack.onConnectError();
                }
            }
        });
    }

    /**
     * 登录
     *
     * @param time
     */
    public void login(final LockPwd lockPwd, String time) {
        DMLog.i(LOG_FLAG, "login()");
        final String url = DMConstant.API_Url.USER_LOGIN;
        String password = EncrypUtil.decrypt(DMConstant.StringConstant.ENCRYP_SEND, lockPwd.getLoginPwd());
        String flag = MD5Util.getFlag(lockPwd.getAccount(), password, time);
        HttpParams httpParams = new HttpParams();
        httpParams.put("flag", flag);
        httpParams.put("accountName", lockPwd.getAccount());
        httpParams.put("time", time);
        httpParams.put("password", password);
        HttpUtil.getInstance().post(dmApp, url, httpParams, new HttpCallBack() {

            @Override
            public void onSuccess(JSONObject result) {
                saveUserInfo(result, lockPwd);
            }

            @Override
            public void onStart() {
                setShowProgress(true);
            }

            @Override
            public void onFailure(Throwable t, Context context) {
                DMLog.e(LOG_FLAG, "login()-->onFailure:" + t.getMessage());
            }
        });
    }

    public void thirdLogin(final LockPwd lockPwd) {
        DMLog.i(LOG_FLAG, "analyThirdLoginData()");
        final String url = DMConstant.API_Url.THIRD_LOGIN;
        String accessToken = EncrypUtil.decrypt(DMConstant.StringConstant.ENCRYP_SEND, lockPwd.getLoginPwd());
        HttpParams httpParams = new HttpParams();
        httpParams.put("openId", lockPwd.getAccount());
        httpParams.put("accessToken", accessToken);
        HttpUtil.getInstance().post(dmApp, url, httpParams, new HttpCallBack() {
            @Override
            public void onSuccess(JSONObject result) {
                saveUserInfo(result, lockPwd);
            }

            @Override
            public void onStart() {
                setShowProgress(true);
            }

            @Override
            public void onLoading(Integer integer) {
            }

            @Override
            public void onFailure(Throwable t, Context context) {
                DMLog.e(LOG_FLAG, "login()-->onFailure:" + t.getMessage());
            }
        });
    }

    /**
     * 解析并保存用户对象
     *
     * @param result
     */
    private void saveUserInfo(JSONObject result, final LockPwd lockPwd) {
        DMLog.i(LOG_FLAG, "saveUserInfo()");
        try {
            String code = result.getString("code");
            if (code.equals(DMConstant.ResultCode.SUCCESS)) {
                // 成功
                DMJsonObject data = new DMJsonObject(result.getString("data"));
                final UserInfo userInfo = new UserInfo(data);
                setUserInfo(userInfo);
                SharedPreferenceUtils.put(dmApp, SharedPreferenceUtils.KEY_AUTO_LOGIN, true);
                SharedPreferenceUtils.put(dmApp, SharedPreferenceUtils.KEY_COOKIE, CookieUtil.getmCookie
                        (DMApplication.this));
            } else {
                //				ErrorUtil.showError(result);
                DMLog.w(LOG_FLAG, "autoLogin Failure:" + result);
            }
        } catch (JSONException e) {
            DMLog.e(LOG_FLAG, e.getMessage());
        }
        Intent intent = new Intent(DMConstant.BroadcastActions.USER_SESSION_LOGIN);
        sendBroadcast(intent);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        DMLog.i(LOG_FLAG, "onTerminate()");
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        DMLog.i(LOG_FLAG, "onLowMemory()");
    }

    public static DMApplication getInstance() {
        return dmApp;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
        int userId = -1;
        Intent intent = new Intent("com.myBroadcastReceiver.Inrent2");
        if (userInfo != null) {
            intent.putExtra("num", userInfo.getLetterCount());
            sendBroadcast(intent);
            userId = userInfo.getId();
            CloudPushService pushService = PushServiceFactory.getCloudPushService();
            pushService.bindAccount(userInfo.getAccountName(), new CommonCallback() {

                @Override
                public void onSuccess(String arg0) {

                }

                @Override
                public void onFailed(String arg0, String arg1) {

                }
            });
        } else {
            intent.putExtra("isLogin", false);
            sendBroadcast(intent);
        }
        SharedPreferenceUtils.put(this, SharedPreferenceUtils.KEY_USER_ID, userId);
    }

    /**
     * 是否已经登录
     *
     * @return
     */
    public boolean islogined() {
        return userInfo != null ? true : false;
        // return true;
    }

    public RegexInfo getRegexInfo() {
        return regexInfo;
    }

    public void setRegexInfo(RegexInfo regexInfo) {
        this.regexInfo = regexInfo;
    }


    /**
     * 初始化云推送通道
     *
     * @param applicationContext
     */
    // final CloudPushService pushService = AlibabaSDK
    // .getService(CloudPushService.class);
    private void initCloudChannel(Context applicationContext) {
        PushServiceFactory.init(applicationContext);
        CloudPushService pushService = PushServiceFactory.getCloudPushService();
        Log.e(LOG_FLAG + "111111111", pushService.getDeviceId() + "");
        pushService.register(applicationContext, new CommonCallback() {
            @Override
            public void onSuccess(String response) {
                Log.e(LOG_FLAG + "000000000", "init cloudchannel success");
            }

            @Override
            public void onFailed(String errorCode, String errorMessage) {
                Log.e(LOG_FLAG + "000000000", "init cloudchannel failed -- errorcode:" + errorCode
                        + " -- errorMessage:" + errorMessage);
            }
        });
    }
}
