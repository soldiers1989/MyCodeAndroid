package com.dm.widgets;

import com.hxjr.p2p.ad5.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 滑动自动刷新，底部提示加载更多的listView
 * 
 * @author jiaohongyun
 *
 */
public class DMListView extends ListView
{
	
	private OnMoreListener onMoreListener;
	
	private View mFooterView;
	
	private Context mContext;
	
	private TextView mFooterTextView;
	
	private View mFooterProgressBar;
	
	/**
	 * 是否滑动时加载更多
	 */
	private boolean flag = true;
	
	/**
	 * 是否可以点击更多
	 */
	private boolean clickFlag = true;
	
	/***
	 * 是否还有更多
	 */
	private boolean isHasMore = false;
	
	/**
	 * 0 更多  1没有更多  2网络不给力
	 */
	private int mWhat;
	
	private int scrollState;
	
	/**
	 * 没有数据时显示
	 */
	private TextView emptyView;
	
	public DMListView(Context context, AttributeSet attrs, int defStyleAttr)
	{
		super(context, attrs, defStyleAttr);
		mContext = context;
		init();
		TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.DMListView);
		String emptyString = mTypedArray.getString(R.styleable.DMListView_emptyText);
		if (emptyString != null && !emptyString.isEmpty())
		{
			//设置没有数据时显示的内容
			this.setEmptyText(emptyString);
		}
		mTypedArray.recycle();
	}
	
	public DMListView(Context context, AttributeSet attrs)
	{
		this(context, attrs, 0);
	}
	
	public DMListView(Context context)
	{
		this(context, null);
	}
	
	@SuppressLint("InflateParams")
	private void init()
	{
		/**
		 * 自定义脚部文件
		 */
		mFooterView = LayoutInflater.from(mContext).inflate(R.layout.pulldown_footer, null);
		mFooterTextView = (TextView)mFooterView.findViewById(R.id.pulldown_footer_text);
		mFooterProgressBar = mFooterView.findViewById(R.id.footer_loading_progress_bar);
		mFooterView.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if (clickFlag)
				{
					flag = true;
					mWhat = 0;
					mFooterTextView.setVisibility(View.GONE);
					DMListView.this.startLoading();
					onMoreListener.onMore();
				}
			}
		});
		this.addFooterView(mFooterView, null, false);
		mFooterView.setVisibility(View.GONE);
		initScrollListener();
		//没有数据时
		emptyView = (TextView)LayoutInflater.from(mContext).inflate(R.layout.list_empty_view, this, false);
		//		emptyView.setVisibility(View.GONE);
	}
	
	private void initScrollListener()
	{
		this.setOnScrollListener(new OnScrollListener()
		{
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState)
			{
				DMListView.this.scrollState = scrollState;
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
			{
				if (DMListView.this.scrollState != OnScrollListener.SCROLL_STATE_IDLE
					&& firstVisibleItem + visibleItemCount == totalItemCount && flag && isHasMore)
				{
					if (onMoreListener != null)
					{
						flag = false;
						mWhat = 0;
						mFooterTextView.setVisibility(View.GONE);
						DMListView.this.startLoading();
						onMoreListener.onMore();
					}
				}
			}
		});
	}
	
	public void setOnMoreListener(OnMoreListener onMoreListener)
	{
		this.onMoreListener = onMoreListener;
	}
	
	/**
	 * 是否还有更多的数据
	 */
	public void hasMoreDate(boolean hasMoreData)
	{
		if (hasMoreData) //有更多数据
		{
			flag = true;
			clickFlag = true;
			isHasMore = true;
			mFooterView.setVisibility(View.VISIBLE);
			mFooterProgressBar.setVisibility(View.GONE);
			mFooterTextView.setText("加载更多");
			mWhat = 0;
		}
		else
		{
			flag = false;
			clickFlag = false;
			isHasMore = false;
			mFooterView.setVisibility(View.VISIBLE);
			mFooterProgressBar.setVisibility(View.GONE);
			mFooterTextView.setText("暂无更多数据");
			mWhat = 1;
		}
	}
	
	/**
	 * 网络连接失败
	 */
	public void netNotReday()
	{
		flag = false;
		mFooterProgressBar.setVisibility(View.GONE);
		mWhat = 2;
	}
	
	private boolean hasEmptyView = false;
	
	/**
	 * 停止加载
	 */
	public void stopLoading()
	{
		mFooterProgressBar.setVisibility(View.GONE);
		mFooterTextView.setVisibility(View.VISIBLE);
		if (this.getAdapter().isEmpty())
		{
			//显示没有数据
			if (!hasEmptyView)
			{
				this.addFooterView(emptyView, null, false);
				hasEmptyView = true;
			}
		}
		else
		{
			//删除没有数据的显示
			this.removeFooterView(emptyView);
			hasEmptyView = false;
		}
		if (this.getAdapter().isEmpty())
		{//解决  暂无更多数据  和  您当前还没有......同时显示的问题
			mFooterTextView.setVisibility(View.GONE);
		}
		switch (mWhat)
		{
			case 0:
				mFooterView.setClickable(true);
				mFooterView.setEnabled(true);
				mFooterTextView.setText("加载更多");
				break;
			case 1:
				//				this.removeFooterView(mFooterView);
				mFooterView.setClickable(false);
				mFooterView.setEnabled(false);
				mFooterTextView.setText("暂无更多数据");
				break;
			case 2:
				mFooterView.setClickable(true);
				mFooterView.setEnabled(true);
				mFooterTextView.setText("网络不给力，请检查您的网络设置！");
				break;
			default:
				break;
		}
	}
	
	/**
	 * 开始加载
	 */
	public void startLoading()
	{
		mFooterProgressBar.setVisibility(View.VISIBLE);
	}
	
	/**
	 * 设置没有数据时的文字
	 * @param emptyText
	 */
	public void setEmptyText(String emptyText)
	{
		emptyView.setText(emptyText);
	}
	
	/**
	 * 设置没有数据时的文字
	 * @param resid
	 */
	public void setEmptyText(int resid)
	{
		emptyView.setText(resid);
	}
	
	/**
	 * 刷新事件接口
	 */
	public interface OnMoreListener
	{
		void onMore();
	}
}
