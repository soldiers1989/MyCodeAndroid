package com.hxjr.p2p.ad5.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

import com.dm.utils.AppManager;
import com.dm.utils.DMLog;
import com.dm.widgets.DMFragmentTabHost;
import com.dm.widgets.utils.AlertDialogUtil;
import com.dm.widgets.utils.AlertDialogUtil.ConfirmListener;
import com.dm.widgets.utils.ToastUtil;
import com.hxjr.p2p.ad5.DMApplication;
import com.hxjr.p2p.ad5.R;
import com.hxjr.p2p.ad5.bean.UpdateInfoBean;
import com.hxjr.p2p.ad5.service.ApiUtil;
import com.hxjr.p2p.ad5.ui.discovery.DiscoveryFragment;
import com.hxjr.p2p.ad5.ui.home.HomeNewFragment;
import com.hxjr.p2p.ad5.ui.investment.InvestFragment;
import com.hxjr.p2p.ad5.ui.mine.MyFragment;
import com.hxjr.p2p.ad5.ui.mine.evaluation.EvaluationActivity;
import com.hxjr.p2p.ad5.ui.mine.user.LoginActivity;
import com.hxjr.p2p.ad5.utils.Method;
import com.hxjr.p2p.ad5.utils.UpdateManager;

public class MainActivity extends BaseActivity {
    private static final String LOG_TAG = MainActivity.class.getCanonicalName();
    public static String isNeedEvaluation = "no";
    // 定义FragmentTabHost对象
    private DMFragmentTabHost mTabHost;

    /**
     * 是否从注册页面跳转过来
     */
    private boolean fromRegister;

    /**
     * @return 返回 mTabHost
     */
    public DMFragmentTabHost getmTabHost() {
        return mTabHost;
    }

    // 定义一个布局
    private LayoutInflater layoutInflater;

    // 定义数组来存放Fragment界面
    @SuppressWarnings("rawtypes")
    private Class[] fragmentArray =
            {HomeNewFragment.class, InvestFragment.class, DiscoveryFragment.class, MyFragment.class};

    /**
     * 底部导航图片数组
     */
    private int[] mImageViewArray =
            {R.drawable.tab_home_btn, R.drawable.tab_investment_btn, R.drawable.tab_discovery_btn, R.drawable
                    .tab_mine_btn};

    /**
     * 底部导航文字数组
     */
    private String[] mTextviewArray = null;

    /**
     * 按返回键时的时间
     */
    private long mExitTime;

    /**
     * 指定当前在哪一页
     */
    public static int index;

    private String preTab;

    @Override
    @SuppressLint("NewApi")
    public void onCreate(Bundle savedInstanceState) {
        DMLog.i(LOG_TAG, "onCreate()");
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        mTextviewArray = getResources().getStringArray(R.array.bottom_navigation_texts);
        index = getIntent().getIntExtra("index", 0);
        initView();
        checkVersion();
        fromRegister = getIntent().getBooleanExtra("fromRegister", false);
    }


    @Override
    protected void onNewIntent(Intent intent) {
        DMLog.i(LOG_TAG, "onNewIntent()");
        super.onNewIntent(intent);
        setIntent(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        DMLog.e(LOG_TAG, "onResume()");
        DMLog.i(LOG_TAG, index + "");
////        if (DMApplication.getInstance().isUpdate) {
//            Intent intent = new Intent(this, UpdateNoticeActivity.class);
//            startActivity(intent);
////        }
        ApiUtil.getUserInfo(this);
        mTabHost.setCurrentTab(index);
        if (fromRegister) {
            showGoShoushiPWD();
            fromRegister = false;
        }
        if (isNeedEvaluation.equals("yes")) {
            if (DMApplication.getInstance().islogined()) {
                if (null == DMApplication.getInstance().getUserInfo().getAssessment()) {
                    AlertDialogUtil.confirm(MainActivity.this, "您未进行风险评估，请您进行评估！",
                            new ConfirmListener() {

                                @Override
                                public void onOkClick() {
                                    Intent intent = new Intent(MainActivity.this,
                                            EvaluationActivity.class);
                                    intent.putExtra("SetGesture", "");
                                    startActivity(intent);
                                    isNeedEvaluation = "no";
                                }

                                @Override
                                public void onCancelClick() {
                                    isNeedEvaluation = "no";
                                }
                            });
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        //		dismiss();
        super.onDestroy();
        // 结束Activity&从堆栈中移除
        AppManager.getAppManager().finishActivity(this);
    }

    /**
     * 初始化组件
     */
    protected void initView() {
        DMLog.i(LOG_TAG, "initView()");
        // 实例化布局对象
        layoutInflater = LayoutInflater.from(this);

        // 实例化TabHost对象，得到TabHost
        mTabHost = (DMFragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);

        // 获得运行时的android sdk 版本
        int sysVersion = VERSION.SDK_INT;
        if (sysVersion > 10) {
            // android sdk 11 以上才需要设置setShowDividers
            mTabHost.getTabWidget().setShowDividers(LinearLayout.SHOW_DIVIDER_NONE);
        }
        mTabHost.getTabWidget().setBackgroundColor(Color.WHITE);
        int count = fragmentArray.length;
        for (int i = 0; i < count; i++) {
            // 为每一个Tab按钮设置图标、文字和内容
            TabSpec tabSpec = mTabHost.newTabSpec(i + "").setIndicator(getTabItemView(i));
            //			TabSpec tabSpec = mTabHost.newTabSpec(i + "").setIndicator(getTabItemView(i)).setContent(
            //				new Intent(this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

            // 将Tab按钮添加进Tab选项卡中
            mTabHost.addTab(tabSpec, fragmentArray[i], null);
            // 设置Tab按钮的背景
            mTabHost.getTabWidget().getChildAt(i).setBackgroundResource(R.drawable.tab_bar_bg);
        }
        // getLayout();

        mTabHost.setOnTabChangedListener(new OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                if (tabId.equals("3")) {
                    //我的帐户页面要判断是否登录过
                    if (!DMApplication.getInstance().islogined()) {
                        index = 0;
                        DMApplication.toLoginValue = 3;
                        mTabHost.setCurrentTabByTag(preTab);
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        intent.putExtra("isfromMine", true);
                        startActivity(intent);
                    } else {
                        index = mTabHost.getCurrentTab();
                        preTab = tabId;
                        //						showSecurityAlert();
                    }
                } else {
                    preTab = tabId;
                    index = mTabHost.getCurrentTab();
                }
            }

        });

        mTabHost.setCurrentTab(index);

    }

    //
    //	private void showSecurityAlert()
    //	{
    //		UserInfo userInfo = DMApplication.getInstance().getUserInfo();
    //		if (null != userInfo && MainActivity.index == 3)
    //		{
    //			boolean hasShowSecurityDialog = (Boolean)SharedPreferenceUtils.get(this, "hasShowSecurityDialog",
    // false);
    //			//如果还没有弹出完成安全信息认证的提示框，并且还没有完成安全信息认证，则弹出提示框
    //			if (!hasShowSecurityDialog && (StringUtils.isEmptyOrNull(userInfo.getRealName())
    //				|| StringUtils.isEmptyOrNull(userInfo.getPhone()) || StringUtils.isEmptyOrNull(userInfo.getEmail
    // ())))
    //			{
    //				SharedPreferenceUtils.put(this, "hasShowSecurityDialog", true);
    //				AlertDialogUtil.confirm(this, "注册成功，为了您的资金安全保障，请完善安全信息！", "马上完善", "先逛逛", new ConfirmListener()
    //				{
    //					@Override
    //					public void onOkClick()
    //					{
    //						startActivity(new Intent(MainActivity.this, SecurityInfoActivity.class));
    //					}
    //
    //					@Override
    //					public void onCancelClick()
    //					{
    //					}
    //				});
    //			}
    //		}
    //	}

    /**
     * 给Tab按钮设置图标和文字
     */
    private View getTabItemView(int index) {
        DMLog.i(LOG_TAG, "getTabItemView()-->index=" + index);

        View view = layoutInflater.inflate(R.layout.tab_item_view, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.imageview);
        imageView.setImageResource(mImageViewArray[index]);

        TextView textView = (TextView) view.findViewById(R.id.textview);
        textView.setText(mTextviewArray[index]);
        return view;
    }

    /**
     * 弹出退出对话框
     */
    private void exitAlert() {
        DMLog.i(LOG_TAG, "exitAlert()");
    }

    /**
     * 回到手机桌面
     *
     * @param context
     * @see [类、类#方法、类#成员]
     */
    public static void goDesk(final Context context) {
        DMLog.i(LOG_TAG, "goDesk()");
        Intent i = new Intent(Intent.ACTION_MAIN);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addCategory(Intent.CATEGORY_HOME);
        context.startActivity(i);
    }

    private UpdateInfoBean updateInfoBean;

    /**
     * 检查版本
     */
    private void checkVersion() {
        DMLog.e(LOG_TAG, "checkVersion()");
        UpdateManager.getInstance().checkForUpdate(this, Method.getVersion(this), false); //检测更新
    }

    /**
     * 提醒用户设置手势密码
     */
    private void showGoShoushiPWD() {
        //		DMComfirmDialogConfig config = DMConfirmDialog.getConfigInstance();
        //		config.setTitle(getString(R.string.set_hand_title));
        //		config.setContent(getString(R.string.set_hand_centent));
        //		config.setOkStr(getString(R.string.set_hand_btn_ok));
        //		config.setCancelStr(getString(R.string.set_hand_cancle));
        //		DMConfirmDialog.show(this, config, new DMComfirmDialogListener()
        //		{
        //
        //			@Override
        //			public void onComfirmOk(AlertDialog dlg)
        //			{
        //				//跳转到设置手势密码页面
        //				Intent intent = new Intent(MainActivity.this, ShouShiPwdActivity.class);
        //				startActivity(intent);
        //				dlg.cancel();
        //			}
        //
        //			@Override
        //			public void onComfirmCancel()
        //			{
        //
        //			}
        //		});
    }

    //    public UpdateInfoBean getUpdateInfoBean() {
    //        return updateInfoBean;
    //    }

    //	/**
    //	 * 加载进度
    //	 */
    //	private ProgressDialog progressDialog;

    //	@Override
    //	public void show()
    //	{
    //		if (!isShowing())
    //		{
    //			progressDialog = ProgressDialog.show(this, null, "加载中...", true, false);
    //		}
    //	}
    //
    //	@Override
    //	public boolean isShowing()
    //	{
    //		if (progressDialog == null)
    //		{
    //			return false;
    //		}
    //		else if (progressDialog.isShowing())
    //		{
    //			return true;
    //		}
    //		else
    //		{
    //			return false;
    //		}
    //	}
    //
    //	@Override
    //	public void dismiss()
    //	{
    //		if (isShowing())
    //		{
    //			progressDialog.dismiss();
    //		}
    //	}

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //按两次返回退出应用程序
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                ToastUtil.getInstant().show(this, R.string.app_exit);
                mExitTime = System.currentTimeMillis();
            } else {
                AppManager.getAppManager().AppExit(this);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
