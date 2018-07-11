package com.dm.widgets.xlistview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import android.widget.TextView;

import com.hxjr.p2p.ad5.R;


/**
 * 下拉刷新，加载更多的ListView
 * @author  tangjian
 * @date 2015-6-2
 */
public class XListView extends ListView implements OnScrollListener
{
	private static final int INIT_POINT_Y = -10000;
	
	private float mLastY = INIT_POINT_Y;
	
	private Scroller mScroller; //用来回滚操作
	
	private OnScrollListener mScrollListener; // 滚动监听
	
	private IXListViewListener mListViewListener;
	
	private XListViewHeader mHeaderView;
	
	private RelativeLayout mHeaderViewContent;
	
	private TextView mHeaderTimeView;
	
	private int mHeaderViewHeight;
	
	private boolean mEnablePullRefresh = true;
	
	private boolean mPullRefreshing = false;
	
	private XListViewFooter mFooterView;
	
	private boolean mEnablePullLoad;
	
	private boolean mPullLoading;
	
	private boolean mIsFooterReady = false;
	
	private int mTotalItemCount;
	
	private int mScrollBack;
	
	private final static int SCROLLBACK_HEADER = 0;
	
	private final static int SCROLLBACK_FOOTER = 1;
	
	private final static int SCROLL_DURATION = 400;
	
	// 当上滑时，超过最后一个Item 50了，就响应加载更多事件
	private final static int PULL_LOAD_MORE_DELTA = 50;
	
	private final static float OFFSET_RADIO = 1.8f; //支持IOS的像拉功能
	
	public XListView(Context context)
	{
		super(context);
		initWithContext(context);
	}
	
	public XListView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		initWithContext(context);
	}
	
	public XListView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		initWithContext(context);
	}
	
	private void initWithContext(Context context)
	{
		mScroller = new Scroller(context, new DecelerateInterpolator());
		super.setOnScrollListener(this);
		
		// 初始化头
		mHeaderView = new XListViewHeader(context);
		mHeaderViewContent = (RelativeLayout)mHeaderView.findViewById(R.id.xlistview_header_content);
		mHeaderTimeView = (TextView)mHeaderView.findViewById(R.id.xlistview_header_time);
		addHeaderView(mHeaderView);
		
		// 初始化尾
		mFooterView = new XListViewFooter(context);
		
		// 初始化头部高度
		mHeaderView.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener()
		{
			@SuppressWarnings("deprecation")
			@Override
			public void onGlobalLayout()
			{
				mHeaderViewHeight = mHeaderViewContent.getHeight();
				getViewTreeObserver().removeGlobalOnLayoutListener(this);
			}
		});
	}
	
	@Override
	public void setAdapter(ListAdapter adapter)
	{
		// 保证底部只被添加一次
		if (mIsFooterReady == false)
		{
			mIsFooterReady = true;
			addFooterView(mFooterView);
		}
		super.setAdapter(adapter);
	}
	
	public void setPullRefreshEnable(boolean enable)
	{
		mEnablePullRefresh = enable;
		if (!mEnablePullRefresh)
		{
			mHeaderViewContent.setVisibility(View.INVISIBLE);
		}
		else
		{
			mHeaderViewContent.setVisibility(View.VISIBLE);
		}
	}
	
	public void setPullLoadEnable(boolean enable)
	{
		mEnablePullLoad = enable;
		if (!mEnablePullLoad)
		{
			mFooterView.hide();
			mFooterView.setOnClickListener(null);
			setFooterDividersEnabled(false);
		}
		else
		{
			mPullLoading = false;
			mFooterView.show();
			mFooterView.setState(XListViewFooter.STATE_NORMAL);
			setFooterDividersEnabled(true);
			mFooterView.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					startLoadMore();
				}
			});
		}
	}
	
	public void stopRefresh()
	{
		if (mPullRefreshing == true)
		{
			mPullRefreshing = false;
			resetHeaderHeight();
		}
	}
	
	public void stopLoadMore()
	{
		if (mPullLoading == true)
		{
			mPullLoading = false;
			mFooterView.setState(XListViewFooter.STATE_NORMAL);
		}
	}
	
	public void setRefreshTime(String time)
	{
		mHeaderTimeView.setText(time);
	}
	
	private void invokeOnScrolling()
	{
		if (mScrollListener instanceof OnXScrollListener)
		{
			OnXScrollListener l = (OnXScrollListener)mScrollListener;
			l.onXScrolling(this);
		}
	}
	
	private void updateHeaderHeight(float delta)
	{
		mHeaderView.setVisiableHeight((int)delta + mHeaderView.getVisiableHeight());
		if (mEnablePullRefresh && !mPullRefreshing)
		{
			if (mHeaderView.getVisiableHeight() > mHeaderViewHeight)
			{
				mHeaderView.setState(XListViewHeader.STATE_READY);
			}
			else
			{
				mHeaderView.setState(XListViewHeader.STATE_NORMAL);
			}
		}
		setSelection(0);
	}
	
	private void resetHeaderHeight()
	{
		int height = mHeaderView.getVisiableHeight();
		if (height == 0)
			return;
		if (mPullRefreshing && height <= mHeaderViewHeight)
		{
			return;
		}
		int finalHeight = 0;
		if (mPullRefreshing && height > mHeaderViewHeight)
		{
			finalHeight = mHeaderViewHeight;
		}
		mScrollBack = SCROLLBACK_HEADER;
		mScroller.startScroll(0, height, 0, finalHeight - height, SCROLL_DURATION);
		invalidate();
	}
	
	private void updateFooterHeight(float delta)
	{
		int height = mFooterView.getBottomMargin() + (int)delta;
		if (mEnablePullLoad && !mPullLoading)
		{
			if (height > PULL_LOAD_MORE_DELTA)
			{
				mFooterView.setState(XListViewFooter.STATE_READY);
			}
			else
			{
				mFooterView.setState(XListViewFooter.STATE_NORMAL);
			}
		}
		mFooterView.setBottomMargin(height);
	}
	
	private void resetFooterHeight()
	{
		int bottomMargin = mFooterView.getBottomMargin();
		if (bottomMargin > 0)
		{
			mScrollBack = SCROLLBACK_FOOTER;
			mScroller.startScroll(0, bottomMargin, 0, -bottomMargin, SCROLL_DURATION);
			invalidate();
		}
	}
	
	private void startLoadMore()
	{
		mPullLoading = true;
		mFooterView.setState(XListViewFooter.STATE_LOADING);
		if (mListViewListener != null)
		{
			mListViewListener.onLoadMore();
		}
	}
	
	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent ev)
	{
		if (mLastY == INIT_POINT_Y)
		{
			mLastY = ev.getRawY();
		}
		
		switch (ev.getAction())
		{
			case MotionEvent.ACTION_DOWN:
				mLastY = ev.getRawY();
				break;
			case MotionEvent.ACTION_MOVE:
				final float deltaY = ev.getRawY() - mLastY;
				mLastY = ev.getRawY();
				if (getFirstVisiblePosition() == 0 && (mHeaderView.getVisiableHeight() > 0 || deltaY > 0))
				{
					updateHeaderHeight(deltaY / OFFSET_RADIO);
					invokeOnScrolling();
				}
				else if (getLastVisiblePosition() == mTotalItemCount - 1 && (mFooterView.getBottomMargin() > 0 || deltaY < 0))
				{
					updateFooterHeight(-deltaY / OFFSET_RADIO);
				}
				break;
			default:
				mLastY = INIT_POINT_Y;
				if (getFirstVisiblePosition() == 0)
				{
					if (mEnablePullRefresh && mHeaderView.getVisiableHeight() > mHeaderViewHeight)
					{
						mPullRefreshing = true;
						mHeaderView.setState(XListViewHeader.STATE_REFRESHING);
						if (mListViewListener != null)
						{
							mListViewListener.onRefresh();
						}
					}
					resetHeaderHeight();
				}
				else if (getLastVisiblePosition() == mTotalItemCount - 1)
				{
					if (mEnablePullLoad && mFooterView.getBottomMargin() > PULL_LOAD_MORE_DELTA && !mPullLoading)
					{
						startLoadMore();
					}
					resetFooterHeight();
				}
				break;
		}
		return super.onTouchEvent(ev);
	}
	
	@Override
	public void computeScroll()
	{
		if (mScroller.computeScrollOffset())
		{
			if (mScrollBack == SCROLLBACK_HEADER)
			{
				mHeaderView.setVisiableHeight(mScroller.getCurrY());
			}
			else
			{
				mFooterView.setBottomMargin(mScroller.getCurrY());
			}
			postInvalidate();
			invokeOnScrolling();
		}
		super.computeScroll();
	}
	
	@Override
	public void setOnScrollListener(OnScrollListener l)
	{
		mScrollListener = l;
	}
	
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState)
	{
		if (mScrollListener != null)
		{
			mScrollListener.onScrollStateChanged(view, scrollState);
		}
	}
	
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
	{
		mTotalItemCount = totalItemCount;
		if (mScrollListener != null)
		{
			mScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
		}
	}
	
	public void setXListViewListener(IXListViewListener l)
	{
		mListViewListener = l;
	}
	
	public interface OnXScrollListener extends OnScrollListener
	{
		public void onXScrolling(View view);
	}
	
	/**
	 * 需要user自己实现
	 * @author  tangjian
	 * @date 2015-6-2
	 */
	public interface IXListViewListener
	{
		public void onRefresh();
		
		public void onLoadMore();
	}
	
	public void setHasNoMoreData()
	{
		mFooterView.setHintView(R.string.pushmsg_center_no_more_msg);
		mFooterView.show();
		mFooterView.setOnClickListener(null);
		setFooterDividersEnabled(true);
	}
}
