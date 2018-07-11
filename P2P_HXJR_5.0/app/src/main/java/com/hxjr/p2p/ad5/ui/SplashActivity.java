package com.hxjr.p2p.ad5.ui;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import com.dm.http.DMException;
import com.dm.http.HttpCallBack;
import com.dm.http.HttpUtil;
import com.dm.utils.AppManager;
import com.dm.utils.DMLog;
import com.hxjr.p2p.ad5.R;
import com.hxjr.p2p.ad5.utils.DMConstant;
import com.hxjr.p2p.ad5.utils.ErrorUtil;
import com.hxjr.p2p.ad5.utils.HttpParams;
import com.hxjr.p2p.ad5.utils.NetConnectErrorManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 应用启动闪屏
 *
 * @author jiaohongyun
 */
public class SplashActivity extends BaseActivity {
    private static final String LOG_FLAG = SplashActivity.class.getCanonicalName();
    /**
     * 无网络提示
     */
    private NetConnectErrorManager netConnectErrorManager;
    private String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DMLog.i(LOG_FLAG, "onCreate()");
        // 点击安装包进行安装，安装结束后不点击完成，而是点击打开应用，应用启动后，再回到桌面，从桌面点击应用图标会造成反复重启应用的现象。
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            this.finish();
            return;
        }
        // 防止第三方跳转时出现双实例
        Activity aty = AppManager.getAppManager().getActivity(MainActivity.class);
        if (aty != null && !aty.isFinishing()) {
            finish();
            return;
        }
        final View view = View.inflate(this, R.layout.splash, null);
        setContentView(view);
        // 渐变展示启动屏
        AlphaAnimation aa = new AlphaAnimation(1.0f, 1.0f);
        aa.setDuration(3000);
        view.startAnimation(aa);
        aa.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationEnd(Animation arg0) {
                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(SplashActivity.this, permissions[0]) != PackageManager
                            .PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(SplashActivity.this, permissions, 210);
                    } else {
                        redirectTo();
                    }
                } else {
                    redirectTo();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationStart(Animation animation) {
            }
        });
    }

    // 用户权限 申请 的回调方法
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[]
            grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 210) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    finish();
                } else {
                    redirectTo();
                }
            }
        }
    }

    String urlImage;

    /**
     * 跳转页面
     */
    protected void redirectTo() {
        HttpParams imgParams = new HttpParams();// 请求推荐标数据
        String imgUrl = DMConstant.API_Url.START_IMG;
        postData(imgUrl, imgParams, 1);
    }

    private void postData(String url, HttpParams params, final int postType) {
        HttpUtil.getInstance().post(this, url, params, new HttpCallBack() {
            @Override
            public void onSuccess(JSONObject result) {
                if (netConnectErrorManager != null) {
                    netConnectErrorManager.onNetGood();
                }
                try {
                    String code = result.getString("code");
                    if (DMConstant.ResultCode.SUCCESS.equals(code)) {
                        JSONObject data = new JSONObject(result.getString("data"));
                        urlImage = data.getString("url");
//                        String updateInfo = data.getString("updateInfo");
//                        JSONObject jsonObject = new JSONObject(updateInfo);
//                        String isUpdate = jsonObject.getString("isUpdate");
//                        String url = jsonObject.getString("url");
//                        if (isUpdate.equals("0")) {
//                            ImagesTask task = new ImagesTask();
//                            task.execute(url);
//                        } else {
                            updateda=null;
                            getDrawable(urlImage);
//                        }
                    } else {
                        // 失败
                        ErrorUtil.showError(result);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable t, Context context) {
                super.onFailure(t, context);
                if (netConnectErrorManager != null) {
                    netConnectErrorManager.onNetError();
                }
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
            }

            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onConnectFailure(DMException dmE, Context context) {
            }

        });
    }

    static Drawable myda = null;
    static Drawable updateda = null;

    private void getDrawable(String urlpath) {
        ImageTask task = new ImageTask();
        task.execute(urlpath);
        DMLog.e(urlpath);
    }

    class ImageTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... urlpath) {
            try {
                URL url = new URL(urlpath[0]);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.connect();
                InputStream is = conn.getInputStream();
                myda = Drawable.createFromStream(is, "background");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Intent intent = new Intent(SplashActivity.this,
                    AdvertiseActivity.class);
            startActivity(intent);
            finish();
        }
    }

    class ImagesTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... urlpath) {
            try {
                URL url = new URL(urlpath[0]);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.connect();
                InputStream is = conn.getInputStream();
                updateda = Drawable.createFromStream(is, "background");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            getDrawable(urlImage);
        }
    }
}
