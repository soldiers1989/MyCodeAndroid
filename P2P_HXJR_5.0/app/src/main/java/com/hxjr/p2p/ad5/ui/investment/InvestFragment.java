package com.hxjr.p2p.ad5.ui.investment;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dm.utils.DMLog;
import com.hxjr.p2p.ad5.R;

/**
 * 我要投资页面
 *
 * @author jiaohongyun
 * @date 2015年10月17日
 */
public class InvestFragment extends Fragment {
	private static final String LOG_TAG = InvestFragment.class.getCanonicalName();

	private View mView;

	/**
	 * 项目列表按钮
	 */
	private TextView proBtn;

	/**
	 * 债权转让按钮
	 */
	private TextView creBtn;

	private View tab_btns;

	private ViewPager viewPager;

	private MyPagerAdapter myAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//		EventBus.getDefault().register(this);
		DMLog.i(LOG_TAG, "onCreateView()");
		return inflater.inflate(R.layout.investment, container, false);
	}

	@Override
	public void onResume() {
		super.onResume();
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
		initView();
	}

	/**
	 * 初始化页面
	 */
	private void initView() {
		mView = getView();
		mView.findViewById(R.id.btn_back).setVisibility(View.GONE);
		// mView.findViewById(R.id.btn_right).setVisibility(View.GONE);
		((TextView) mView.findViewById(R.id.title_text)).setText(R.string.page_title_investment);
		proBtn = (TextView) mView.findViewById(R.id.project_list_btn);
		creBtn = (TextView) mView.findViewById(R.id.creditor_list_btn);
		tab_btns = mView.findViewById(R.id.tab_btns);
		proBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				viewPager.setCurrentItem(0);
			}
		});

		creBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				viewPager.setCurrentItem(1);
			}
		});

		myAdapter = new MyPagerAdapter(getActivity().getSupportFragmentManager());
		viewPager = (ViewPager) mView.findViewById(R.id.invest_viewpager);
		viewPager.setAdapter(myAdapter);
		// 初始化当前显示的view
		viewPager.setCurrentItem(0);
		viewPager.addOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int index) {
				if (index == 0) {
					// 选中项目列表
					tab_btns.setBackgroundResource(R.drawable.pic_tab1);
					proBtn.setTextColor(getResources().getColor(R.color.main_color));
					creBtn.setTextColor(getResources().getColor(R.color.white));
				} else if (index == 1) {
					// 选中债权转让
					tab_btns.setBackgroundResource(R.drawable.pic_tab2);
					proBtn.setTextColor(getResources().getColor(R.color.white));
					creBtn.setTextColor(getResources().getColor(R.color.main_color));
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});
	}

//	@Subscribe
//	public void onEventMainThread(HMsgEvent event) {
//		viewPager.setCurrentItem(0);
//	}

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
						fm = new BidChangeFragment(); // 投资列表
						array.put(0, fm);
					}
					break;
				case 1:
					if (fm == null) {
						fm = new CreFragment(); // 债权转让列表
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
