package com.hxjr.p2p.ad5.ui.home;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.dm.http.DMException;
import com.dm.http.HttpCallBack;
import com.dm.http.HttpUtil;
import com.dm.img.DMBitmap;
import com.dm.utils.DMJsonObject;
import com.dm.utils.DMLog;
import com.dm.widgets.AutoScrollView;
import com.dm.widgets.AutoScrollView.OnAutoScrollViewClickListener;
import com.dm.widgets.CircleBadgeView;
import com.dm.widgets.FanProgressBar;
import com.dm.widgets.RoundProgressBar;
import com.dm.widgets.bannerviewpager.AdvertisementInfo;
import com.dm.widgets.bannerviewpager.CycleViewPager;
import com.dm.widgets.bannerviewpager.CycleViewPager.ImageCycleViewListener;
import com.dm.widgets.bannerviewpager.ViewDisplayFactory;
import com.dm.widgets.refresh.PullRefreshLayout;
import com.dm.widgets.refresh.PullRefreshLayout.OnRefreshListener;
import com.dm.widgets.utils.ToastUtil;
import com.hxjr.p2p.ad5.DMApplication;
import com.hxjr.p2p.ad5.R;
import com.hxjr.p2p.ad5.bean.BidBean;
import com.hxjr.p2p.ad5.bean.HomeNotice;
import com.hxjr.p2p.ad5.bean.MfenleiBean;
import com.hxjr.p2p.ad5.receiver.BidAndCreReceiver;
import com.hxjr.p2p.ad5.receiver.BidAndCreReceiver.OnSuccessListener;
import com.hxjr.p2p.ad5.service.ApiUtil;
import com.hxjr.p2p.ad5.ui.MainActivity;
import com.hxjr.p2p.ad5.ui.OperationActivity;
import com.hxjr.p2p.ad5.ui.discovery.spread.SpreadRewardActivity;
import com.hxjr.p2p.ad5.ui.investment.adapter.FlAdapter;
import com.hxjr.p2p.ad5.ui.investment.adapter.UnFinishAdapter;
import com.hxjr.p2p.ad5.ui.investment.bid.BidDetailActivity;
import com.hxjr.p2p.ad5.ui.mine.letter.LetterInStationActivity;
import com.hxjr.p2p.ad5.ui.mine.user.LoginActivity;
import com.hxjr.p2p.ad5.utils.DMConstant;
import com.hxjr.p2p.ad5.utils.ErrorUtil;
import com.hxjr.p2p.ad5.utils.FormatUtil;
import com.hxjr.p2p.ad5.utils.HttpParams;
import com.hxjr.p2p.ad5.utils.NetConnectErrorManager;
import com.hxjr.p2p.ad5.utils.NetConnectErrorManager.NetConnetCallBack;
import com.hxjr.p2p.ad5.utils.TimeUtils;
import com.hxjr.p2p.ad5.utils.UIHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.hxjr.p2p.ad5.ui.MainActivity.index;

/**
 * 首页
 *
 * @author jiaohongyun
 * @date 2015年10月19日
 */
public class HomeNewFragment extends Fragment implements OnRefreshListener, OnAutoScrollViewClickListener,
        OnSuccessListener {
    private static final int POST_BANNER = 1;

    private static final int POST_NOTICE = 2;

    private static final int POST_BID_COUNT = 3;

    private static final int POST_RECOMMEND_BID = 4;

    private static final String LOG_TAG = MainActivity.class.getCanonicalName() + "HomeFragment";

    private DMApplication app;

    //	private TextView bidTypeImgs;

    private TextView bidTitle;

    private TextView status;

    private TextView yearRate;

    private TextView cycle;

    private TextView amount;

    private RoundProgressBar progresss;

    private TextView bid_dfb_text;

    private View bid_dfb_line;

    private MyCountDownTimer myCountDownTimer;

    private View mView;

    private ImageView bidTypeImg;// 标的担保类型

    private TextView bid_title_tv;// 借款标题

    private TextView recommend_tv;// 理财方式（指：信用认证标、实地认证标、机构担保标等类型）

    private TextView annual_rate_tv;// 年化利率

    private TextView bid_status_tv;// *
    // 标的状态（SQZ:申请中;DSH:待审核;DFB:待发布;YFB:预发布;TBZ:投标中;DFK:待放款;HKZ:还款中;YJQ:已结清;YLB:*
    // 已流标;YDF:已垫付;YZF:已作废）

    private TextView amount_tv;// 可投金额

    private TextView cycle_tv;// 贷款周期（月）

    private TextView paymentType_tv;// 还款方式（还款方式,DEBX:等额本息;MYFX:每月付息,到期还本;YCFQ:本息到期一次付清;DEBJ:等额本金;）

    private Button go_buy_btn;

    private PullRefreshLayout mSwipeLayout;

    private LinearLayout marqueeLinearLayout;

    private List<String> noticeDatas;

    private List<String> timeDatas;

    private List<HomeNotice> notices = new ArrayList<HomeNotice>(DMConstant.DigitalConstant.TEN);
    ;

    private AutoScrollView autoScrollView;

    private Context context;

    private FanProgressBar fanProgressBar;

    private List<ImageView> views = new ArrayList<ImageView>();

    private List<AdvertisementInfo> adInfos = new ArrayList<AdvertisementInfo>(
            DMConstant.DigitalConstant.TEN);

    private CycleViewPager cycleViewPager;

    private ImageView only_one_banner;

    private DMBitmap dmBitmapLoader;

    private BidAndCreReceiver mBidAndCreReceiver;

    private BidBean tjBid;

    private TextView btn_right;

    private CircleBadgeView badger;

    // 分类
    private LinearLayout flBidLayout;

    // 运营数据
    private LinearLayout invest_operational;

    // 银行存管
    private LinearLayout invest_bank;

    // 关于我们
    private LinearLayout invest_about;

    // 推荐标
    private LinearLayout recommend_bid;

    private ListView fl_list;

    private ListView main_unfinsh_list;

    private List<BidBean> bidBeans;


    private List<MfenleiBean> mFlBean;

    private FlAdapter fadapter;

    private UnFinishAdapter unFinishAdapter;

    private MyBroadcastReceiver mbr;


    private View recommendLayout;

    /**
     * 处理进度动画
     */
    private Handler progressHandler = new Handler();

    private double progress;

    private TextView progressNum;

    private LinearLayout tjBidLayout;

    private LinearLayout countLayout;

    private LinearLayout new_llbid;

    private BidBean bean;

    /**
     * 记录首页推荐标的ID，用于设置进度时的判断
     */
    private int tjbid = -1;

    /**
     * 无网络提示
     */
    private NetConnectErrorManager netConnectErrorManager;
    private DecimalFormat df = new DecimalFormat("0.00");

    /**
     * 处理进度动画用的
     */
    private Runnable progressRunnable = new Runnable() {
        private int delayMillis = 15;

        @Override
        public void run() {
            int temp = 0;
            delayMillis = makeDelayMillis();
            if (null != tjBid && tjbid != tjBid.getId()) {
                temp = -1;
                tjbid = tjBid.getId();
            } else {
                temp = fanProgressBar.getProgress();
            }
            temp++;
            if (temp <= progress) {
                fanProgressBar.setProgress(temp);
                if ((progress + "").equals("0.0")) {
                    progressNum.setText("0");
                } else {
                    progressNum.setText("" + df.format(progress));
                }
                progressHandler.postDelayed(this, delayMillis);
            } else {
                progressHandler.removeCallbacks(this);
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.myBroadcastReceiver.Inrent2"); // 为BroadcastReceiver指定action，即要监听的消息名字。
        mbr = new MyBroadcastReceiver();
        getActivity().registerReceiver(mbr, intentFilter);
        app = DMApplication.getInstance();
        bidBeans = new ArrayList<BidBean>();
        return inflater.inflate(R.layout.main_page_home_text, container, false);
    }

    @Override
    public void onResume() {
        DMLog.i(LOG_TAG, "onResume()");
        super.onResume();

        ApiUtil.getUserInfo(getContext());
        onRefresh();
        View view = mView.findViewById(R.id.main_title);
        View statusBar = null;
        if (view != null) {
            statusBar = view.findViewById(R.id.statusBar);
        }
        if (statusBar != null) {
            if (Build.VERSION.SDK_INT >= 19) {
                statusBar.setVisibility(View.VISIBLE);
            } else {
                statusBar.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        DMLog.i(LOG_TAG, "onResume()");
        context = getActivity();
        initView();
        initDMBitmap();
        initData();
    }

    private void initView() {
        ApiUtil.getUserInfo(getContext());
        mView = getView();
        invest_operational = (LinearLayout) mView.findViewById(R.id.invest_operational);
        invest_operational.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                DMLog.e("invest_operational");
                Intent intent = new Intent();
                intent.setClass(getActivity(), OperationActivity.class);
                startActivity(intent);
            }
        });
        invest_about = (LinearLayout) mView.findViewById(R.id.invest_about);
        invest_about.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                DMLog.e("invest_about");
                Intent intent = new Intent(context, WebViewActivity.class);
                intent.putExtra("title", "关于我们");
                intent.putExtra("linkUrl", "http://www.todayfu.com/xxpl/gsjj.html");
                getActivity().startActivity(intent);
            }
        });
        invest_bank = (LinearLayout) mView.findViewById(R.id.invest_bank);
        invest_bank.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                DMLog.e("invest_bank");
                Intent intent = new Intent(context, WebViewActivity.class);
                intent.putExtra("title", "银行存管");
                intent.putExtra("linkUrl", "http://www.todayfu.com/tzgl/xszy/216.html");
                getActivity().startActivity(intent);
            }
        });
        bidTitle = (TextView) mView.findViewById(R.id.mainbidTitle);
        status = (TextView) mView.findViewById(R.id.mainstatus);
        yearRate = (TextView) mView.findViewById(R.id.mainyearRate);
        cycle = (TextView) mView.findViewById(R.id.maincycle);
        progresss = (RoundProgressBar) mView.findViewById(R.id.mainprogresss);
        amount = (TextView) mView.findViewById(R.id.mainamount);
        bid_dfb_text = (TextView) mView.findViewById(R.id.mainbid_dfb_text);
        bid_dfb_line = mView.findViewById(R.id.mainbid_dfb_line);
        mView.findViewById(R.id.btn_back).setVisibility(View.GONE);
        recommend_bid = (LinearLayout) mView.findViewById(R.id.recommend_bid);
        new_llbid = (LinearLayout) mView.findViewById(R.id.new_llbid);
        if (DMApplication.getInstance().islogined()) {
            if (DMApplication.getInstance().getUserInfo().getIsExperienceBid().equals("S")) {
                recommend_bid.setVisibility(View.VISIBLE);
            }
        }
        recommendLayout = mView.findViewById(R.id.recommendLayout);
        recommendLayout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putCharSequence("index", "0");
                intent.putExtras(bundle);
                startActivity(new Intent(getActivity(), SpreadRewardActivity.class));
            }
        });
        ((TextView) mView.findViewById(R.id.title_text)).setText(R.string.app_name);
        // 站内信
        btn_right = (TextView) mView.findViewById(R.id.btn_right);
        badger = new CircleBadgeView(getActivity(), mView.findViewById(R.id.btn_right));
        badger.setGravity(Gravity.CENTER);
        badger.setTextSize(10);
        setTitleRightIcon(btn_right);
        btn_right.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (app.islogined()) {
                    startActivity(new Intent(getActivity(), LetterInStationActivity.class));
                } else {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }
            }
        });

        // 分类
        flBidLayout = (LinearLayout) mView.findViewById(R.id.flBidLayout);
        fl_list = (ListView) mView.findViewById(R.id.mainfl_list);
        fl_list.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                index = 1;
                MainActivity mainA = (MainActivity) getActivity();
                mainA.getmTabHost().setCurrentTab(index);
                Intent intent = new Intent();
                intent.setAction("com.myBroadcastReceiver.Inrent222");
                DMLog.e(position + "");
                intent.putExtra("index", position);
                getActivity().sendBroadcast(intent);
            }
        });
        main_unfinsh_list = (ListView) mView.findViewById(R.id.main_unfinsh_list);
        main_unfinsh_list.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                if (app.islogined()) {
                    Intent intent = new Intent(getActivity(), BidDetailActivity.class);
                    intent.putExtra("bidId", bidBeans.get(position).getId() + "");
                    intent.putExtra("bidFlag", bidBeans.get(position).getFlag());
                    getActivity().startActivity(intent);
                } else {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    getActivity().startActivity(intent);
                }
            }
        });
        mSwipeLayout = (PullRefreshLayout) mView.findViewById(R.id.id_swipe_ly);
        mSwipeLayout.setOnRefreshListener(this);
        fanProgressBar = (FanProgressBar) mView.findViewById(R.id.fanProgressBar);
        cycleViewPager = (CycleViewPager) mView.findViewById(R.id.banner_viewpager);
        only_one_banner = (ImageView) mView.findViewById(R.id.only_one_banner);

        countLayout = (LinearLayout) mView.findViewById(R.id.countLayout);
        tjBidLayout = (LinearLayout) mView.findViewById(R.id.tjBidLayout);
        bidTypeImg = (ImageView) mView.findViewById(R.id.bidTypeImg);
        bid_title_tv = (TextView) mView.findViewById(R.id.bid_title_tv);
        recommend_tv = (TextView) mView.findViewById(R.id.recommend_tv);
        annual_rate_tv = (TextView) mView.findViewById(R.id.annual_rate_tv);
        bid_status_tv = (TextView) mView.findViewById(R.id.bid_status_tv);
        progressNum = (TextView) mView.findViewById(R.id.progressNum);
        amount_tv = (TextView) mView.findViewById(R.id.amount_tv);
        cycle_tv = (TextView) mView.findViewById(R.id.cycle_tv);
        paymentType_tv = (TextView) mView.findViewById(R.id.paymentType_tv);
        go_buy_btn = (Button) mView.findViewById(R.id.go_buy_btn);
        go_buy_btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tjBid != null) {
                    if (app.islogined()) {
                        Intent intent = new Intent(getActivity(), BidDetailActivity.class);
                        intent.putExtra("index", 0);// 主页面tab位置
                        intent.putExtra("bidId", tjBid.getId() + "");
                        intent.putExtra("bidFlag", tjBid.getFlag());
                        getActivity().startActivity(intent);
                    } else {
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        getActivity().startActivity(intent);
                    }
                }
            }
        });
        // 设置首页推荐标刷新广播
        mBidAndCreReceiver = new BidAndCreReceiver();
        mBidAndCreReceiver.setOnSuccessListener(this);
        getActivity().registerReceiver(mBidAndCreReceiver,
                new IntentFilter(BidAndCreReceiver.BID_SUCCESS_RECEIVER));

        netConnectErrorManager = new NetConnectErrorManager(mView, mSwipeLayout,
                new NetConnetCallBack() {
                    @Override
                    public void onNetErrorRefrensh() {
                        onRefresh();
                    }
                });
    }

    /**
     * 设置头部title里面右边TextView的icon图标
     */
    protected void setTitleRightIcon(TextView textView) {
        Drawable right = getResources().getDrawable(R.drawable.icon_xinfeng);
        right.setBounds(0, 0, right.getMinimumWidth(), right.getMinimumHeight());
        textView.setCompoundDrawables(null, null, right, null);
    }

    private void initData() {
        mSwipeLayout.setVisibility(View.GONE);
        postData();
        mSwipeLayout.setVisibility(View.VISIBLE);
        fanProgressBar.setProgress(0);
        progressHandler.postDelayed(progressRunnable, 15);
    }

    private int makeDelayMillis() {
        int temp = 0;
        if (progress > 80) {
            temp = 10;
        } else if (progress > 60) {
            temp = 15;
        } else if (progress > 40) {
            temp = 20;
        } else if (progress > 20) {
            temp = 25;
        } else if (progress > 10) {
            temp = 50;
        } else if (progress > 5) {
            temp = 100;
        } else {
            temp = 200;
        }
        return temp;
    }

    /**
     * 请求数据
     */
    private void postData() {

        HttpParams bannerParams = new HttpParams();// 请求banner数据
        String bannerUrl = DMConstant.API_Url.ADVS_GET;
        postData(bannerUrl, bannerParams, POST_BANNER);

        HttpParams noticeParams = new HttpParams();// 请求公告数据
        String noticeUrl = DMConstant.API_Url.NOTICE_LIST;
        postData(noticeUrl, noticeParams, POST_NOTICE);

        HttpParams rcmBidParams = new HttpParams();// 请求推荐标数据
        String rcmBidUrl = DMConstant.API_Url.RECOMMEND_BID_LIST;
        postData(rcmBidUrl, rcmBidParams, POST_RECOMMEND_BID);
    }

    private void postData(String url, HttpParams params, final int postType) {
        HttpUtil.getInstance().post(getContext(), url, params, new HttpCallBack() {
            @Override
            public void onSuccess(JSONObject result) {
                if (netConnectErrorManager != null) {
                    netConnectErrorManager.onNetGood();
                }
                try {
                    String code = result.getString("code");

                    if (DMConstant.ResultCode.SUCCESS.equals(code)) {
                        switch (postType) {
                            case POST_BANNER:
                                doAfterGetBanner(result);
                                break;
                            case POST_NOTICE:
                                doAfterGetNotice(result);
                                break;
                            case POST_RECOMMEND_BID:
                                DMLog.i("POST_RECOMMEND_BID post result", result.toString());
                                doAfterGetRecommendBid(result);
                                break;
                        }
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
            }

            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onConnectFailure(DMException dmE, Context context) {
                ToastUtil.getInstant().show(getActivity(), dmE.getDescription());
                if (netConnectErrorManager != null) {
                    netConnectErrorManager.onNetError();
                }
            }

        });
    }

    /**
     * 获取公告
     *
     * @param result
     */
    protected void doAfterGetNotice(JSONObject result) {
        try {
            if (null != notices) {
                notices.clear();
            }
            JSONArray dataList = result.getJSONArray("data");
            for (int i = 0; i < dataList.length(); i++) {
                DMJsonObject data = new DMJsonObject(dataList.getJSONObject(i).toString());
                HomeNotice bean = new HomeNotice(data);
                DMLog.e(bean.toString());
                if (TimeUtils.diffsTime(bean.getTime()))
                    notices.add(bean);
            }
            marqueeLinearLayout = (LinearLayout) mView.findViewById(R.id.marqueeLL);
            if (notices.size() == 0) {
                marqueeLinearLayout.setVisibility(View.GONE);
                return;
            } else {
                marqueeLinearLayout.setVisibility(View.VISIBLE);
            }
            noticeDatas = new ArrayList<String>(DMConstant.DigitalConstant.TEN);
            timeDatas = new ArrayList<String>(DMConstant.DigitalConstant.TEN);
            if (null != notices) {
                for (int i = 0; i < notices.size(); i++) {
                    noticeDatas.add(notices.get(i).getTitle());
                    timeDatas.add(notices.get(i).getTime());
                }
            }
            autoScrollView = new AutoScrollView(context, noticeDatas, timeDatas, R.drawable.icon_1);
            autoScrollView.setOnAutoScrollViewClickListener(HomeNewFragment.this);
            autoScrollView.startScroll(1.5f);
            marqueeLinearLayout.removeAllViews();
            marqueeLinearLayout.addView(autoScrollView);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /**
     * 获取banner广告图
     *
     * @param result
     */
    protected void doAfterGetBanner(JSONObject result) {
        try {
            JSONArray dataList = result.getJSONArray("data");
            adInfos.clear();
            for (int i = 0; i < dataList.length(); i++) {
                JSONObject data = dataList.getJSONObject(i);
                AdvertisementInfo adInfo = new AdvertisementInfo();
                // adInfo.setId(data.getString("id"));
                adInfo.setTitle(data.getString("advTitle"));
                adInfo.setImgUrl(data.getString("advImg"));
                if (data.has("advUrl")) {
                    adInfo.setLinkUrl(data.getString("advUrl"));
                }
                adInfos.add(adInfo);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (adInfos != null && adInfos.size() > 1) {// 多张图片的显示
            only_one_banner.setVisibility(View.GONE);
            cycleViewPager.setVisibility(View.VISIBLE);
            views.clear();
            // 将最后一个ImageView添加进来
            views.add(ViewDisplayFactory.getImageView(context, adInfos.get(adInfos.size() - 1)
                    .getImgUrl(), dmBitmapLoader));
            for (int i = 0; i < adInfos.size(); i++) {
                views.add(ViewDisplayFactory.getImageView(context, adInfos.get(i).getImgUrl(),
                        dmBitmapLoader));
            }
            // 将第一个ImageView添加进来
            views.add(ViewDisplayFactory.getImageView(context, adInfos.get(0).getImgUrl(),
                    dmBitmapLoader));
            // 设置循环，在调用setData方法前调用
            cycleViewPager.setCycle(true);
            // 在加载数据前设置是否循环
            cycleViewPager.setData(views, adInfos, mAdCycleViewListener);
            // 设置轮播
            cycleViewPager.setWheel(true);
            // 设置轮播时间，默认5000ms
            cycleViewPager.setTime(3000);
            // 设置圆点指示图标组居中显示，默认靠右
            // cycleViewPager.setIndicatorCenter();

        } else if (adInfos != null && adInfos.size() == 1) {// 只有一张图片的显示
            only_one_banner.setVisibility(View.VISIBLE);
            cycleViewPager.setVisibility(View.GONE);
            dmBitmapLoader.display(only_one_banner, adInfos.get(0).getImgUrl());
            only_one_banner.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (adInfos.get(0).getLinkUrl() != null
                            && FormatUtil.validateUrl(adInfos.get(0).getLinkUrl()))// 如果没有设置关联URL，则不进行任何操作
                    {
                        Intent intent = new Intent(context, WebViewActivity.class);
                        intent.putExtra("title", adInfos.get(0).getTitle());
                        intent.putExtra("linkUrl", adInfos.get(0).getLinkUrl());
                        getActivity().startActivity(intent);
                    }
                }
            });
        } else {// 一张图片都没有
            only_one_banner.setVisibility(View.VISIBLE);
            only_one_banner.setImageResource(R.drawable.pic_adv_default);
            cycleViewPager.setVisibility(View.GONE);
        }

    }


    /**
     * 获取推荐标
     */
    protected void doAfterGetRecommendBid(JSONObject result) {
        try {
            DMLog.i("doAfterGetRecommendBid", result.toString());
            JSONArray dataList = result.getJSONArray("data");
            if (dataList != null && dataList.length() > 0) {
                tjBidLayout.setVisibility(View.VISIBLE);
                //                DMJsonObject data = new DMJsonObject(dataList.getString(0));// 推荐标有多个，现在只取了第一个
                //                tjBid = new BidBean(data);
                getData();
                //                fillBid();
                flBidLayout.setVisibility(View.GONE);
                // flBidLayout.setVisibility(View.VISIBLE);
            } else {
                tjBidLayout.setVisibility(View.GONE);
                // 没标的时候显示分类
                getClassData();
                flBidLayout.setVisibility(View.VISIBLE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getData() {
        // 获取最近标的信息
        HttpParams httpParams = new HttpParams();
        //        httpParams.put("pageIndex", "1");
        //        httpParams.put("pageSize", 1);
        HttpUtil.getInstance().post(getContext(), DMConstant.API_Url.BID_PUBLICS_BIDLIST,
                httpParams, new HttpCallBack() {
                    @Override
                    public void onSuccess(JSONObject result) {
                        if (netConnectErrorManager != null) {
                            netConnectErrorManager.onNetGood();
                        }
                        try {
                            String code = result.getString("code");
                            if (DMConstant.ResultCode.SUCCESS.equals(code)) {
                                // 成功
                                if (result.isNull("data")) {
                                    // tjBidLayout.setVisibility(View.GONE);
                                } else {
                                    JSONArray dataList = result.getJSONArray("data");
                                    bidBeans.clear();
                                    for (int i = dataList.length() - 1; i >= 0; i--) {
                                        DMJsonObject record1 = new DMJsonObject(
                                                dataList.getJSONObject(i)
                                                        .toString());
                                        BidBean bidBean = new BidBean(record1);
                                        if (Double.parseDouble(bidBean.getRemainAmount()) > 0 && (bidBean.getStatus()
                                                .equals("YFB") || bidBean.getStatus().equals("TBZ")))
                                            bidBeans.add(bidBean);
                                    }
                                    tjBid = bidBeans.get(0);
                                    fillBid();
                                    bidBeans.remove(0);
                                    unFinishAdapter = new UnFinishAdapter(context, bidBeans);
                                    unFinishAdapter.notifyDataSetChanged();
                                    if (tjBid.getStatus().equals("DFB")
                                            || tjBid.getStatus().equals("YFB")) {
                                        // 待发布或预发布
//                                                                                progresss.setVisibility(View.VISIBLE);
//                                                                                progresss.setYFB(true);
//                                                                                progresss.setProgress(0);
//                                                                                bid_dfb_text.setVisibility(View
//                                         .VISIBLE);
//                                                                                bid_dfb_line.setVisibility(View
//                                         .VISIBLE);
                                        go_buy_btn.setText(tjBid.getPublishDate());
                                        SimpleDateFormat sdf = new SimpleDateFormat(
                                                "yyyy-MM-dd HH:mm:ss", Locale.CHINA);
                                        long time = 0L;
                                        try {
                                            Date date = sdf.parse(tjBid.getPublishDate());
                                            time = date.getTime() - System.currentTimeMillis()
                                                    + DMApplication.getInstance().diffTime;
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }
                                        if (myCountDownTimer != null) {
                                            myCountDownTimer.cancel();
                                        }
                                        myCountDownTimer = new MyCountDownTimer(time, 1000L);
                                        myCountDownTimer.textView = go_buy_btn;
                                        //                                        myCountDownTimer.line = bid_dfb_line;
                                        myCountDownTimer.pos = 0;
                                        myCountDownTimer.start();
                                    }
                                }
                            } else {
                                // 失败
                                ErrorUtil.showError(result);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        main_unfinsh_list.setAdapter(unFinishAdapter);
                        setListViewHeightBasedOnChildren(main_unfinsh_list);
                    }

                    @Override
                    public void onFailure(Throwable t, Context context) {
                        super.onFailure(t, context);
                    }

                });

    }

    private void getClassData() {
        HttpParams flBidParams = new HttpParams();// 请求标分组数据
        String flBidUrl = DMConstant.API_Url.BID_PUBLIC_FL;
        HttpUtil.getInstance().post(getContext(), flBidUrl, flBidParams, new HttpCallBack() {

            @Override
            public void onSuccess(JSONObject result) {
                if (netConnectErrorManager != null) {
                    netConnectErrorManager.onNetGood();
                }
                try {
                    JSONArray dataList = result.getJSONArray("data");
                    mFlBean = new ArrayList<MfenleiBean>();
                    for (int i = 0; i < dataList.length(); i++) {
                        DMJsonObject record = new DMJsonObject(dataList.getJSONObject(i).toString());
                        MfenleiBean mflBean = new MfenleiBean(record);
                        mFlBean.add(mflBean);
                    }
                    fadapter = new FlAdapter(context, mFlBean);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                fl_list.setAdapter(fadapter);
                setListViewHeightBasedOnChildren(fl_list);
            }

        });
        // 获取最近标的信息
        HttpParams httpParams = new HttpParams();
        httpParams.put("pageIndex", "1");
        httpParams.put("pageSize", 1);
        HttpUtil.getInstance().post(getContext(), DMConstant.API_Url.BID_PUBLICS_BIDLIST,
                httpParams, new HttpCallBack() {
                    @Override
                    public void onSuccess(JSONObject result) {
                        if (netConnectErrorManager != null) {
                            netConnectErrorManager.onNetGood();
                        }
                        try {
                            String code = result.getString("code");
                            if (DMConstant.ResultCode.SUCCESS.equals(code)) {
                                // 成功
                                if (result.isNull("data")) {
                                    // tjBidLayout.setVisibility(View.GONE);
                                } else {
                                    JSONArray dataList = result.getJSONArray("data");
                                    DMJsonObject record = new DMJsonObject(dataList.getJSONObject(0)
                                            .toString());
                                    bean = new BidBean(record);
                                    new_llbid.setOnClickListener(new OnClickListener() {

                                        @Override
                                        public void onClick(View arg0) {
                                            if (DMApplication.getInstance().islogined()) {
                                                Intent intent = new Intent(getActivity(),
                                                        BidDetailActivity.class);
                                                intent.putExtra("bidId", bean.getId() + "");
                                                intent.putExtra("bidFlag", bean.getFlag());
                                                getActivity().startActivity(intent);
                                            } else {
                                                Intent intent = new Intent(getActivity(),
                                                        LoginActivity.class);
                                                getActivity().startActivity(intent);
                                            }
                                        }
                                    });
                                    DMLog.i("GetDataRecommendBid data", bean.toString());
                                    bidTitle.setText(bean.getBidTitle());
                                    yearRate.setText(bean.getRate());
                                    if (bean.getIsDay().equals("F")) {
                                        // 按月分期
                                        UIHelper.formatDateTextSize(cycle, bean.getCycle() + "个月");
                                    } else if (bean.getIsDay().equals("S")) {
                                        // 按天分期
                                        UIHelper.formatDateTextSize(cycle, bean.getCycle() + "天");
                                    }
                                    if (bean.getStatus().equals("HKZ")) {
                                        // 还款中特殊处理
                                        Double a = Double.valueOf(bean.getAmount());
                                        Double b = Double.valueOf(bean.getRemainAmount());
                                        Double c = a - b;
                                        bean.setAmount(c + "");
                                        bean.setRemainAmount("0");
                                    }
                                    UIHelper.formatMoneyTextSize(amount,
                                            FormatUtil.formatMoney(Double.valueOf(bean.getAmount())), 0.5f);
                                    if (bean.getStatus().equals("DFB")
                                            || bean.getStatus().equals("YFB")) {
                                        // 待发布或预发布
                                        progresss.setVisibility(View.VISIBLE);
                                        progresss.setYFB(true);
                                        progresss.setProgress(0);
                                        bid_dfb_text.setVisibility(View.VISIBLE);
                                        bid_dfb_line.setVisibility(View.VISIBLE);
                                        bid_dfb_text.setText(bean.getPublishDate());
                                        SimpleDateFormat sdf = new SimpleDateFormat(
                                                "yyyy-MM-dd HH:mm:ss", Locale.CHINA);
                                        long time = 0L;
                                        try {
                                            Date date = sdf.parse(bean.getPublishDate());
                                            time = date.getTime() - System.currentTimeMillis()
                                                    + DMApplication.getInstance().diffTime;
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }
                                        if (myCountDownTimer != null) {
                                            myCountDownTimer.cancel();
                                        }
                                        myCountDownTimer = new MyCountDownTimer(time, 1000L);
                                        myCountDownTimer.textView = bid_dfb_text;
                                        myCountDownTimer.line = bid_dfb_line;
                                        myCountDownTimer.pos = 0;
                                        myCountDownTimer.start();
                                    } else if ("HKZ".equals(bean.getStatus())
                                            || "YJQ".equals(bean.getStatus())) {
                                        // 待付款，还款中，已结清
                                        progresss.setVisibility(View.VISIBLE);
                                        progresss.setProgress(100);
                                        progresss.setYFB(false);
                                        bid_dfb_text.setVisibility(View.GONE);
                                        bid_dfb_line.setVisibility(View.GONE);
                                        bid_dfb_text.setTag(null);
                                    } else if ("DFK".equals(bean.getStatus())) {
                                        // 待放款改为进度百分比
                                        progresss.setVisibility(View.VISIBLE);
                                        progresss.setYFB(true);
                                        progresss.setProgress(FormatUtil.getBidProgress(
                                                bean.getAmount(), bean.getRemainAmount()));
                                        bid_dfb_text.setVisibility(View.GONE);
                                        bid_dfb_line.setVisibility(View.GONE);
                                        bid_dfb_text.setTag(null);
                                    } else {
                                        // 其它
                                        progresss.setYFB(false);
                                        progresss.setVisibility(View.VISIBLE);
                                        progresss.setProgress(FormatUtil.getBidProgress(
                                                bean.getAmount(), bean.getRemainAmount()));
                                        bid_dfb_text.setVisibility(View.GONE);
                                        bid_dfb_line.setVisibility(View.GONE);
                                        bid_dfb_text.setTag(null);
                                    }

                                    status.setText(FormatUtil.convertBidStatus(bean.getStatus()));
                                    // if (bean.getIsXsb()) {
                                    // bidFlag2
                                    // .setImageResource(R.drawable.pic_xs);
                                    // bidFlag2
                                    // .setVisibility(View.VISIBLE);
                                    // } else if (bean.getIsJlb()) {
                                    // bidFlag2
                                    // .setImageResource(R.drawable.pic_jl);
                                    // bidFlag2
                                    // .setVisibility(View.VISIBLE);
                                    // } else {
                                    // bidFlag2
                                    // .setVisibility(View.INVISIBLE);
                                    // }
                                    // if (bidBean != null) {
                                    // String bidId = "" + bidBean.getId();
                                    // Intent intent = new Intent(getActivity(),
                                    // BidDetailActivity.class);
                                    // intent.putExtra("bidId", bidId);
                                    // intent.putExtra("bidFlag",
                                    // bidBean.getFlag());
                                    // getActivity().startActivity(intent);
                                    // }
                                }
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
                    }

                });
    }

    public void setListViewHeightBasedOnChildren(ListView listView) {

        ListAdapter listAdapter = listView.getAdapter();

        if (listAdapter == null) {

            return;

        }

        int totalHeight = 0;

        for (int i = 0; i < listAdapter.getCount(); i++) {

            View listItem = listAdapter.getView(i, null, listView);

            listItem.measure(0, 0);

            totalHeight += listItem.getMeasuredHeight();

        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();

        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));

        listView.setLayoutParams(params);

    }

    private void fillBid() {
        tjBidLayout.setVisibility(View.VISIBLE);
        bid_title_tv.setText(tjBid.getBidTitle());
        bidTypeImg.setImageResource(UIHelper.bidTyeImgs.get(tjBid.getFlag()));// 标的担保类型
        if (tjBid.getIsJlb()) {
            recommend_tv.setVisibility(View.VISIBLE);
            recommend_tv.setText("奖励推荐");
        } else if (tjBid.getIsXsb()) {
            recommend_tv.setVisibility(View.VISIBLE);
            recommend_tv.setText("新手推荐");
        } else {
            recommend_tv.setVisibility(View.GONE);
        }
        annual_rate_tv.setText("年化利率:" + tjBid.getRate() + "%");// 年化利率
        amount_tv.setText(FormatUtil.formatMoney(Double.valueOf(tjBid.getAmount())));
        if (tjBid.getIsDay().equals("S")) {
            cycle_tv.setText(tjBid.getCycle() + "天期");// 贷款周期（天）
        } else if (tjBid.getIsDay().equals("F")) {
            cycle_tv.setText(tjBid.getCycle() + "月期");// 贷款周期（月）
        }
        paymentType_tv.setText(FormatUtil.formatPaymentType(tjBid.getPaymentType()));// 还款方式
        bid_status_tv.setText(FormatUtil.convertBidStatus(tjBid.getStatus()));// 标的状态
        progress = FormatUtil.getBidProgress(tjBid.getAmount(), tjBid.getRemainAmount());// 标的进度
        progressHandler.postDelayed(progressRunnable, 15);

        if ("YFB".equals(tjBid.getStatus())) {
            go_buy_btn.setBackgroundResource(R.drawable.btn_main);
            go_buy_btn.setText("预发布");
            //            go_buy_btn.setEnabled(false);
        } else if ("TBZ".equals(tjBid.getStatus())) {
            go_buy_btn.setBackgroundResource(R.drawable.btn_main);
            go_buy_btn.setText("马上抢购");
            go_buy_btn.setEnabled(true);
        } else {
            go_buy_btn.setBackgroundResource(R.drawable.btn_mainyfb);
            go_buy_btn.setText("抢购结束");
            go_buy_btn.setEnabled(false);
        }
    }

    /**
     * 初始化图片加载
     */
    private void initDMBitmap() {
        dmBitmapLoader = DMBitmap.create(getActivity());
        dmBitmapLoader.configLoadingImage(R.drawable.img_loading);
        dmBitmapLoader.configLoadfailImage(R.drawable.img_load_failure);
    }

    /**
     * banner点击事件
     */
    private ImageCycleViewListener mAdCycleViewListener = new ImageCycleViewListener() {

        @Override
        public void onImageClick(AdvertisementInfo info, int position, View imageView) {
            if (cycleViewPager.isCycle()) {
                if (info.getLinkUrl() != null && FormatUtil.validateUrl(info.getLinkUrl()))// 如果没有设置关联URL，则不进行任何操作
                {
                    // position = position - 1;
                    Intent intent = new Intent(context, WebViewActivity.class);
                    intent.putExtra("linkUrl", info.getLinkUrl());
                    intent.putExtra("title", info.getTitle());
                    getActivity().startActivity(intent);
                }
            }
        }
    };

    /**
     * 公告点击事件
     */
    @Override
    public void onAutoScrollViewClick(int position) {
        if (position < notices.size()) {
            Intent intent = new Intent(context, NoticeActivity.class);
            intent.putExtra("title", context.getResources().getString(R.string.home_notice));
            intent.putExtra("noticeId", notices.get(position).getId());
            context.startActivity(intent);
        }
    }

    @Override
    public void onRefresh() {
        mSwipeLayout.setRefreshing(false);
        fanProgressBar.setProgress(0);
        progressHandler.postDelayed(progressRunnable, 15);
        postData();
    }

    @Override
    public void onSuccessToUpdateUi(Intent intent) {
        // 投资成功后刷新首页推荐标数据
        if (intent.getIntExtra("bidId", -1) == tjBid.getId()) {
            HttpParams rcmBidParams = new HttpParams();// 请求推荐标数据
            String rcmBidUrl = DMConstant.API_Url.RECOMMEND_BID_LIST;
            postData(rcmBidUrl, rcmBidParams, POST_RECOMMEND_BID);
        }
    }

    @Override
    public void onDestroy() {
        Log.e("destory", "         000000000    0000000");
        if (null != mBidAndCreReceiver) {
            getActivity().unregisterReceiver(mBidAndCreReceiver);
        }
        getActivity().unregisterReceiver(mbr);
        super.onDestroy();
    }

    // @Subscribe
    // public void onEventMainThread(MsgEvent event) {
    // // badge.setBackgroundResource(R.drawable.circle);
    // badger.setGravity(Gravity.CENTER);
    // // if (AliCloudPushApplication.countNum != 0) {
    // badger.setText("21");
    // badger.show();
    // // } else {
    // // badge.hide();
    // // }
    // }
    private class MyBroadcastReceiver extends BroadcastReceiver

    {

        public void onReceive(Context context, Intent intent)

        {

            app = DMApplication.getInstance();
            if (intent.getBooleanExtra("isLogin", true)) {
                String num = intent.getIntExtra("num", 0) + "";
                if (num.equals("0")) {
                    badger.hide();
                } else {
                    badger.setText(num);
                    badger.show();
                }
                //                newLayout.setVisibility(View.GONE);
                recommendLayout.setVisibility(View.VISIBLE);
                if (DMApplication.getInstance().getUserInfo().getIsExperienceBid().equals("S")
                        && (!TimeUtils.diffTime(
                        DMApplication.getInstance().getUserInfo().getRegisterTime()).equals(
                        "已经注册超过三天"))) {
                    recommend_bid.setVisibility(View.VISIBLE);
                } else {
                    recommend_bid.setVisibility(View.GONE);
                }
            } else {
                //                newLayout.setVisibility(View.VISIBLE);
                recommendLayout.setVisibility(View.GONE);
                recommend_bid.setVisibility(View.GONE);
                badger.hide();
            }
        }

    }

    private class MyCountDownTimer extends CountDownTimer {
        public TextView textView;

        public View line;

        public int pos = -1;

        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            if (this.textView != null) {
                textView.setText(surplusLongToString(millisUntilFinished));
            }
        }

        @Override
        public void onFinish() {
            if (this.textView != null) {
//                textView.setVisibility(View.GONE);
                go_buy_btn.setText("马上抢购");
            }
            if (this.line != null) {
                line.setVisibility(View.GONE);
            }
            if (this.pos != -1) {
                // mList.get(pos).setStatus("TBZ");
            }

            // BidListAdapter.this.notifyDataSetChanged();
            this.cancel();
        }

    }

    private static final long DAY = 24 * 3600L;

    private static final long HOUR = 24L;

    private static final long MINUTE = 60L;

    private static final long SECOND = 60L;

    public String surplusLongToString(long between) {
        long temp0 = between / 1000;
        long temp1 = temp0 / 60;
        long temp2 = temp1 / 60;
        long second = temp0 % SECOND;
        long minute = temp1 % MINUTE;
        long hour = temp2 % HOUR;
        long day = temp0 / DAY;
        return String.format("倒计时  %02d", hour + day * 24) + String.format(":%02d", minute) +
                String.format
                        (":%02d", second);
        //        return "倒计时：" + day + "天" + hour + "小时" + minute + "分" + second + "秒";
        //        Date date1 = new Date(between);
        //        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //        String str1 = sdf1.format(date1);
        //        return str1;
    }

    //    /**
    //     * 获取标的基本信息
    //     */
    //    private void getBaseBidInfo(final String bidId) {
    //        HttpParams params = new HttpParams();
    //        params.put("bidId", bidId);
    //        HttpUtil.getInstance().post(getActivity(), DMConstant.API_Url.BID_BID, params, new HttpCallBack() {
    //            @Override
    //            public void onSuccess(JSONObject result) {
    //                try {
    //                    String code = result.getString("code");
    //                    if (code.equals(DMConstant.ResultCode.SUCCESS)) {
    //                        // 成功
    //                        DMJsonObject data = new DMJsonObject(result.getString("data"));
    //                        BidDetailBase base = new BidDetailBase(data);
    //                        bidDetailBases.add(base);
    //                    } else {
    //                        ErrorUtil.showError(result);
    //                    }
    //                } catch (JSONException e) {
    //                    e.printStackTrace();
    //                }
    //            }
    //        });
    //    }
}
