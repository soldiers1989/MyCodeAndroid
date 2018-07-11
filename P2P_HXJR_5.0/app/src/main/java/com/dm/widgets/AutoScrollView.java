package com.dm.widgets;

import java.util.List;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hxjr.p2p.ad5.R;

public class AutoScrollView extends ViewGroup
{
	
	private List<String> noticeDatas;
	
	private List<String> timeDatas;
	
	private int picId;
	
	private Context context;
	
	private boolean isScroll;
	
	private int defaultDuration = 600;
	
	private OnAutoScrollViewClickListener onAutoScrollViewClickListener;
	
	public void setDefaultDuration(int defaultDuration)
	{
		this.defaultDuration = defaultDuration;
	}
	
	@SuppressLint("HandlerLeak") private Handler hanlder = new Handler()
	{
		
		@Override
		public void handleMessage(Message msg)
		{
			if (!isScroll)
				return;
			if (preView == null || nextView == null)
				return;
			ObjectAnimator preAnimator = ObjectAnimator.ofFloat(preView, "translationY", 0, -preView.getMeasuredHeight());
			preAnimator.setDuration(defaultDuration);
			preAnimator.setRepeatCount(0);
			preAnimator.start();
			ObjectAnimator nextAnimator = ObjectAnimator.ofFloat(nextView, "translationY", 0, -nextView.getMeasuredHeight());
			nextAnimator.setDuration(defaultDuration);
			nextAnimator.setRepeatCount(0);
			nextAnimator.setStartDelay((int)(defaultDuration * 0.02));
			nextAnimator.start();
			
			nextAnimator.addListener(new AnimatorListener()
			{
				@Override
				public void onAnimationStart(Animator animator)
				{
				}
				
				@Override
				public void onAnimationRepeat(Animator animator)
				{
				}
				
				@Override
				public void onAnimationEnd(Animator animator)
				{
					startDatas = startDatas + 1;
					if (startDatas > noticeDatas.size() - 2)
					{
						startDatas = 0;
					}
					
					removeAllViews();
					genericTextView((startDatas + 2));
					requestLayout();
					startScroll(3f);
				}
				
				@Override
				public void onAnimationCancel(Animator animator)
				{
				}
			});
			super.handleMessage(msg);
		}
	};
	
	public AutoScrollView(Context context, List<String> noticeDatas, List<String> timeDatas, int picId)
	{
		super(context);
		this.noticeDatas = noticeDatas;
		this.timeDatas = timeDatas;
		this.picId = picId;
		this.context = context;
		this.setFocusable(false);
		this.setClickable(false);
		this.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		init(context);
	}
	
	public AutoScrollView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		this.context = context;
		this.setFocusable(false);
		this.setClickable(false);
		this.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		init(context);
	}
	
	public AutoScrollView(Context context, AttributeSet attrs, int defStyleAttr)
	{
		super(context, attrs, defStyleAttr);
		this.context = context;
		this.setFocusable(false);
		this.setClickable(false);
		this.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		init(context);
	}
	
	public void init(Context context)
	{
		if (null == noticeDatas || null == timeDatas)
			return;
		
		if (noticeDatas.size() > 1 && timeDatas.size() > 1)
		{
			noticeDatas.add(noticeDatas.get(0));
			timeDatas.add(timeDatas.get(0));
			genericTextView(2);
		}
		else
		{
			genericTextView(1);
		}
		
	}
	
	private int startDatas = 0;
	
	@SuppressLint({"NewApi", "InflateParams"})
	private void genericTextView(int size)
	{
		if (noticeDatas.size() >= size)
		{
			for (int j = startDatas; j < size; j++)
			{
				final View view = LayoutInflater.from(context).inflate(R.layout.notice, this, false);
				view.setId(j);
				TextView date = (TextView)view.findViewById(R.id.notice_date);
				TextView title = (TextView)view.findViewById(R.id.notice_title);
				ImageView noticePic = (ImageView)view.findViewById(R.id.notice_img);
				date.setText(timeDatas.get(j));
				title.setText(noticeDatas.get(j));
				noticePic.setImageResource(picId);
				LinearLayout.LayoutParams params =
					new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
				addView(view, params);
				view.setOnClickListener(new OnClickListener()
				{
					@Override
					public void onClick(View v)
					{
						onAutoScrollViewClickListener.onAutoScrollViewClick(view.getId());
					}
				});
				
				//				final RelativeLayout rl = new RelativeLayout(context);
				//				rl.setId(j);
				//				
				//				ImageView noticeImg = new ImageView(context);
				//				noticeImg.setBackground(context.getResources().getDrawable(R.drawable.home_notice));
				//				RelativeLayout.LayoutParams layoutParams1 =
				//					new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
				//				layoutParams1.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
				//				layoutParams1.leftMargin = DensityUtil.dip2px(context, 11);
				//				
				//				TextView noticeTv = new TextView(context);
				//				noticeTv.setSingleLine();
				//				noticeTv.setEllipsize(TruncateAt.END);
				//				String notice = noticeDatas.get(j);
				//				if (notice.length() > 14)
				//					notice = notice.substring(0, 14);
				//				noticeTv.setText(notice);
				//				noticeTv.setGravity(Gravity.CENTER);
				//				noticeTv.setTextSize(16);
				//				noticeTv.setTextColor(context.getResources().getColor(R.color.text_black_6));
				//				//			Drawable leftImg = context.getResources().getDrawable(R.drawable.home_notice);
				//				//			leftImg.setBounds(0, 0, leftImg.getMinimumWidth() + 5, leftImg.getMinimumHeight() + 5);
				//				//			noticeTv.setCompoundDrawables(leftImg, null, null, null);
				//				RelativeLayout.LayoutParams layoutParams2 =
				//					new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
				//				layoutParams2.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
				//				layoutParams2.leftMargin = DensityUtil.dip2px(context, 35);
				//				
				//				TextView timeTv = new TextView(context);
				//				timeTv.setSingleLine();
				//				timeTv.setEllipsize(TruncateAt.END);
				//				timeTv.setText(timeDatas.get(j));
				//				timeTv.setGravity(Gravity.CENTER);
				//				timeTv.setTextSize(16);
				//				timeTv.setTextColor(context.getResources().getColor(R.color.text_black_6));
				//				RelativeLayout.LayoutParams layoutParams3 =
				//					new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
				//				layoutParams3.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
				//				layoutParams3.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
				//				layoutParams3.rightMargin = DensityUtil.dip2px(context, 11);
				//				
				//				rl.addView(noticeImg, layoutParams1);
				//				rl.addView(noticeTv, layoutParams2);
				//				rl.addView(timeTv, layoutParams3);
				//				addView(rl);
				//				
				//				rl.setOnClickListener(new OnClickListener()
				//				{
				//					@Override
				//					public void onClick(View v)
				//					{
				//						onAutoScrollViewClickListener.onAutoScrollViewClick(rl.getId());
				//					}
				//				});
			}
		}
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		int childCount = getChildCount();
		int height = 0;
		for (int i = 0; i < childCount; i++)
		{
			//			View view = getChildAt(i);
			//			int childWidhtMeasureSpec = MeasureSpec.makeMeasureSpec(widthMeasureSpec, MeasureSpec.AT_MOST);
			//			int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(heightMeasureSpec, MeasureSpec.AT_MOST);
			//			view.measure(childWidhtMeasureSpec, childHeightMeasureSpec);
			//			height = view.getMeasuredHeight();
			
			View child = getChildAt(i);
			measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0);
		}
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
	
	@Override
	protected void onLayout(boolean arg0, int left, int top, int right, int bottom)
	{
		int t = 0;
		for (int i = 0; i < getChildCount(); i++)
		{
			View view = getChildAt(i);
			view.layout(left, t + top, view.getMeasuredWidth(), t + view.getMeasuredHeight());
			t += view.getMeasuredHeight();
		}
	}
	
	private View preView;
	
	private View nextView;
	
	public void startScroll(float seconds)
	{
		float delayMillis = seconds * 1000;
		int childCount = getChildCount();
		if (childCount > 0)
			preView = getChildAt(0);
		if (childCount > 1)
		{
			nextView = getChildAt(1);
		}
		
		if (preView == null || nextView == null)
			return;
		
		hanlder.sendEmptyMessageDelayed(0, (int)delayMillis);
		isScroll = true;
	}
	
	/**
	 * onDestory的时候调用
	 */
	public void onRelease()
	{
		hanlder.removeCallbacksAndMessages(null);
	}
	
	/**
	 * @param 对onAutoScrollViewClickListener进行赋值
	 */
	public void setOnAutoScrollViewClickListener(OnAutoScrollViewClickListener onAutoScrollViewClickListener)
	{
		this.onAutoScrollViewClickListener = onAutoScrollViewClickListener;
	}
	
	public interface OnAutoScrollViewClickListener
	{
		void onAutoScrollViewClick(int id);
	}
}
