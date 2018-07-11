package com.hxjr.p2p.ad5.ui.mine;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.View;
import android.widget.TextView;

import com.dm.widgets.utils.AlertDialogUtil;
import com.hxjr.p2p.ad5.R;
import com.hxjr.p2p.ad5.ui.BaseActivity;

public class RechargeNewsActivity extends BaseActivity {
    /**
     * 在线充值按钮
     */
    private TextView zxczBtn;

    /**
     * 银行转账按钮
     */
    private TextView yhzzBtn;

    private View tab_btns;

    private ViewPager viewPager;

    private MyPagerAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge_news);
        initView();
    }

    /**
     * 初始化页面
     */
    protected void initView() {
        super.initView();
        yhzzBtn = (TextView) findViewById(R.id.yhzz_list_btn);
        zxczBtn = (TextView) findViewById(R.id.zxcz_list_btn);
        viewPager = (ViewPager) findViewById(R.id.recharge_viewpager);
        zxczBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(0);
            }
        });

        yhzzBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(1);
            }
        });
        tab_btns = findViewById(R.id.tab_btns);
        myAdapter = new MyPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(myAdapter);
        // 初始化当前显示的view
        viewPager.setCurrentItem(0);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int index) {
                if (index == 0) {
                    // 选中项目列表
                    tab_btns.setBackgroundResource(R.drawable.pic_tab1);
                    zxczBtn.setTextColor(getResources().getColor(R.color.main_color));
                    yhzzBtn.setTextColor(getResources().getColor(R.color.white));
                } else if (index == 1) {
                    // 选中债权转让
                    tab_btns.setBackgroundResource(R.drawable.pic_tab2);
                    zxczBtn.setTextColor(getResources().getColor(R.color.white));
                    yhzzBtn.setTextColor(getResources().getColor(R.color.main_color));
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });
        AlertDialogUtil.alert(this, "1.推荐使用银行转账充值." + "\n" + "2.若使用银行转账，请先使用网银向您的电子账号转账，转账成功后再进行核账操作。", "确认", new
                AlertDialogUtil
                        .AlertListener() {
                    @Override
                    public void doConfirm() {

                    }
                });
    }

    private class MyPagerAdapter extends FragmentPagerAdapter {
        SparseArray<Fragment> array = new SparseArray<Fragment>(2);

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int index) {
            Fragment fm = array.get(index);
            switch (index) {
                case 0:
                    if (fm == null) {
                        fm = new YhRechargeFragment(); // 银行转账
                        array.put(0, fm);
                    }
                    break;
                case 1:
                    if (fm == null) {
                        fm = new ZxRechargeFragment(); // 在线充值
                        array.put(1, fm);
                    }
                    break;
                default:
                    break;
            }
            return fm;
        }

        @Override
        public int getCount() {
            return 2;
        }
    }
}
