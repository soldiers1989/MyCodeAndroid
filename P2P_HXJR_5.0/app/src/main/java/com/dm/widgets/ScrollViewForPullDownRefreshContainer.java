package com.dm.widgets;

import java.util.Timer;
import java.util.TimerTask;
import com.hxjr.p2p.ad5.R;
import com.dm.utils.DMLog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

/**
 * 上下滑动翻页
 * @author  sushuo
 * @date 2015年5月28日
 */
public class ScrollViewForPullDownRefreshContainer extends RelativeLayout
{
	//	private static final int DEFAULT_PX_SPACE_HEIGHT = 106;// 106px title height + bottom view height
	//	
	//	private int mTitleHeight = DEFAULT_PX_SPACE_HEIGHT;
	
	/**
	 * 自动上滑
	 */
	public static final int AUTO_UP = 0;
	
	/**
	 * 自动上滑不刷新界面
	 */
	public static final int AUTO_UP_DONT_REFRESH = 3;
	
	/**
	 * 自动下滑
	 */
	public static final int AUTO_DOWN = 1;
	
	/**
	 * 动画完成
	 */
	public static final int DONE = 2;
	
	/**
	 * 动画速度
	 */
	public static final float SPEED = 6.5f;
	
	private boolean isMeasured = false;
	
	/**
	 * 用于计算手滑动的速度
	 */
	private VelocityTracker vt;
	
	private int mViewHeight;
	
	private int mViewWidth;
	
	private View topView;
	
	private View bottomView;
	
	private boolean canPullDown;
	
	private boolean canPullUp;
	
	private int state = DONE;
	
	/**
	 * 记录当前展示的是哪个view，0是topView，1是bottomView
	 */
	private int mCurrentViewIndex = 0;
	
	/**
	 * 手滑动距离，这个是控制布局的主要变量
	 */
	private float mMoveLen;
	
	private MyTimer mTimer;
	
	private float mLastY;
	
	/**
	 * 用于控制是否变动布局的另一个条件，mEvents==0时布局可以拖拽了，mEvents==-1时可以舍弃将要到来的第一个move事件，
	 * 这点是去除多点拖动剧变的关键
	 */
	private int mEvents;
	
	private OnPullUpScrollViewListener listener;
	
	private boolean disabled = true;// 是否被禁止滑动
	
	private Handler handler = new Handler()
	{
		
		@Override
		public void handleMessage(Message msg)
		{
			if (mMoveLen != 0)
			{
				if (state == AUTO_UP)
				{
					mMoveLen -= SPEED;
					if (mMoveLen <= -mViewHeight)
					{
						mMoveLen = -mViewHeight;
						state = DONE;
						mCurrentViewIndex = 1;
						if (listener != null)
						{
							listener.scrollToTop(false);
						}
					}
					
				}
				else if (state == AUTO_UP_DONT_REFRESH)
				{
					mMoveLen -= SPEED;
					if (mMoveLen <= -mViewHeight)
					{
						mMoveLen = -mViewHeight;
						state = DONE;
						mCurrentViewIndex = 1;
						if (listener != null)
						{
							listener.scrollToTop(false);
						}
					}
				}
				else if (state == AUTO_DOWN)
				{
					mMoveLen += SPEED;
					if (mMoveLen >= 0)
					{
						mMoveLen = 0;
						state = DONE;
						mCurrentViewIndex = 0;
						if (listener != null)
						{
							listener.scrollToTop(true);
						}
						
					}
				}
				else
				{
					mTimer.cancel();
				}
				
			}
			requestLayout();
		}
		
	};
	
	public ScrollViewForPullDownRefreshContainer(Context context)
	{
		this(context, null);
	}
	
	public ScrollViewForPullDownRefreshContainer(Context context, AttributeSet attrs)
	{
		this(context, attrs, 0);
	}
	
	public ScrollViewForPullDownRefreshContainer(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		init();
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ScrollViewContainer, defStyle, 0);
		//		mTitleHeight = a.getDimensionPixelSize(R.styleable.ScrollViewContainer_space_height, DEFAULT_PX_SPACE_HEIGHT);
		a.recycle();
	}
	
	private void init()
	{
		mTimer = new MyTimer(handler);
	}
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev)
	{
		if (!disabled)
		{
			switch (ev.getActionMasked())
			{
				case MotionEvent.ACTION_DOWN:
					if (vt == null)
						vt = VelocityTracker.obtain();
					else
						vt.clear();
					mLastY = ev.getY();
					vt.addMovement(ev);
					mEvents = 0;
					break;
				case MotionEvent.ACTION_POINTER_DOWN:
				case MotionEvent.ACTION_POINTER_UP:
					// 多一只手指按下或抬起时舍弃将要到来的第一个事件move，防止多点拖拽的bug
					mEvents = -1;
					break;
				case MotionEvent.ACTION_MOVE:
					
					vt.addMovement(ev);
					if (canPullUp && mCurrentViewIndex == 0 && mEvents == 0)
					{
						mMoveLen += (ev.getY() - mLastY);
						// 防止上下越界
						if (mMoveLen > 0)
						{
							mMoveLen = 0;
							mCurrentViewIndex = 0;
						}
						else if (mMoveLen < -mViewHeight)
						{
							mMoveLen = -mViewHeight;
							mCurrentViewIndex = 1;
							
						}
						if (mMoveLen < -8)
						{
							// 防止事件冲突
							ev.setAction(MotionEvent.ACTION_CANCEL);
						}
					}
					else if (canPullDown && mCurrentViewIndex == 1 && mEvents == 0)
					{
						mMoveLen += (ev.getY() - mLastY);
						// 防止上下越界
						if (mMoveLen < -mViewHeight)
						{
							mMoveLen = -mViewHeight;
							mCurrentViewIndex = 1;
						}
						else if (mMoveLen > 0)
						{
							mMoveLen = 0;
							mCurrentViewIndex = 0;
						}
						if (mMoveLen > 8 - mViewHeight)
						{
							// 防止事件冲突
							ev.setAction(MotionEvent.ACTION_CANCEL);
						}
					}
					else
					{
						mEvents++;
					}
					mLastY = ev.getY();
					requestLayout();
					break;
				case MotionEvent.ACTION_UP:
					mLastY = ev.getY();
					vt.addMovement(ev);
					vt.computeCurrentVelocity(700);
					// 获取Y方向的速度
					float mYV = vt.getYVelocity();
					if (mMoveLen == 0 || mMoveLen == -mViewHeight)
						break;
					if (Math.abs(mYV) < 500)
					{
						// 速度小于一定值的时候当作静止释放，这时候两个View往哪移动取决于滑动的距离
						if (mMoveLen <= -mViewHeight / 2)
						{
							state = AUTO_UP_DONT_REFRESH;
						}
						else if (mMoveLen > -mViewHeight / 2)
						{
							state = AUTO_DOWN;
						}
					}
					else
					{
						// 抬起手指时速度方向决定两个View往哪移动
						if (mYV < 0)
						{
							state = AUTO_UP;
						}
						else
						{
							state = AUTO_DOWN;
						}
					}
					mTimer.schedule(2);
					try
					{
						vt.recycle();
						vt = null;
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}
					
					break;
					
			}
		}
		// TODO Auto-generated method stub
		return super.dispatchTouchEvent(ev);
	}
	
	@SuppressLint("DrawAllocation")
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b)
	{
		topView.layout(0, (int)mMoveLen, mViewWidth, topView.getMeasuredHeight() + (int)mMoveLen);
		// 判断bottomview到达顶部
		if ((mViewHeight + (int)mMoveLen) == 0)
		{
			handler.postDelayed(new Runnable()
			{
				
				@Override
				public void run()
				{
					// TODO Auto-generated method stub  
					if (listener != null)
					{
						listener.toUpRefresh();
					}
				}
			}, 100);
		}
		bottomView.layout(0,
			mViewHeight + (int)mMoveLen,
			mViewWidth,
			mViewHeight + (int)mMoveLen + bottomView.getMeasuredHeight());
	}
	
	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		if (!isMeasured)
		{
			isMeasured = true;
			//			mViewHeight = getMeasuredHeight();
			mViewHeight = getMeasuredHeight();//- mTitleHeight;// 去除title height
			mViewWidth = getMeasuredWidth();
			topView = getChildAt(0);
			bottomView = getChildAt(1);
			
			bottomView.setOnTouchListener(bottomViewTouchListener);
			topView.setOnTouchListener(topViewTouchListener);
		}
	}
	
	public void setTitleHeight(int titleHeight)
	{
		//		mTitleHeight = titleHeight;
		isMeasured = false;
		requestLayout();
		invalidate();
	}
	
	/**
	 * 设置是否被禁止滑动
	 * 
	 * @param enable
	 */
	public void setDisabled(boolean disabled)
	{
		this.disabled = disabled;
	}
	
	private OnTouchListener topViewTouchListener = new OnTouchListener()
	{
		
		@Override
		public boolean onTouch(View v, MotionEvent event)
		{
			ScrollView sv = (ScrollView)v;
			if (sv.getScrollY() == (sv.getChildAt(0).getMeasuredHeight() - sv.getMeasuredHeight()) && mCurrentViewIndex == 0)
				canPullUp = true;
			else
				canPullUp = false;
			return false;
		}
	};
	
	private OnTouchListener bottomViewTouchListener = new OnTouchListener()
	{
		
		@Override
		public boolean onTouch(View v, MotionEvent event)
		{
			ScrollView sv = (ScrollView)v;
			if (sv.getScrollY() == 0 && mCurrentViewIndex == 1)
				canPullDown = true;
			else
				canPullDown = false;
			return false;
		}
	};
	
	class MyTimer
	{
		private Handler handler;
		
		private Timer timer;
		
		private MyTask mTask;
		
		public MyTimer(Handler handler)
		{
			this.handler = handler;
			timer = new Timer();
		}
		
		public void schedule(long period)
		{
			if (mTask != null)
			{
				mTask.cancel();
				mTask = null;
			}
			mTask = new MyTask(handler);
			timer.schedule(mTask, 0, period);
		}
		
		public void cancel()
		{
			if (mTask != null)
			{
				mTask.cancel();
				mTask = null;
			}
		}
		
		class MyTask extends TimerTask
		{
			private Handler handler;
			
			public MyTask(Handler handler)
			{
				this.handler = handler;
			}
			
			@Override
			public void run()
			{
				handler.obtainMessage().sendToTarget();
			}
			
		}
	}
	
	public void setOnPullUpScrollViewListener(OnPullUpScrollViewListener listener)
	{
		this.listener = listener;
	}
	
	public interface OnPullUpScrollViewListener
	{
		
		public void toUpRefresh();
		
		public void scrollToTop(boolean canRefresh);
		
	}
	
}
