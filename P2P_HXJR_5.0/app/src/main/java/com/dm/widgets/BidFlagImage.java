package com.dm.widgets;

import com.hxjr.p2p.ad5.R;
import com.dm.utils.DensityUtil;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

public class BidFlagImage extends View
{
	
	public BidFlagImage(Context context)
	{
		this(context, null);
	}
	
	public BidFlagImage(Context context, AttributeSet attrs)
	{
		this(context, attrs, 0);
	}
	
	public BidFlagImage(Context context, AttributeSet attrs, int defStyleAttr)
	{
		super(context, attrs, defStyleAttr);
		paint = new Paint();
		TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.RoundProgressBar);
		
		// 获取自定义属性和默认值
		roundColor = mTypedArray.getColor(R.styleable.BidFlagImage_b_f_roundColor, Color.parseColor("#ff34b8fc"));
		textColor = mTypedArray.getColor(R.styleable.BidFlagImage_b_f_textColor, Color.parseColor("#ff34b8fc"));
		int defaultSize = DensityUtil.sp2px(context, 14);
		textSize = mTypedArray.getDimension(R.styleable.BidFlagImage_b_f_textSize, defaultSize);
		mText = mTypedArray.getString(R.styleable.BidFlagImage_b_f_mText);
		if (mText == null)
		{
			mText = "信";
		}
		roundWidth = mTypedArray.getDimension(R.styleable.BidFlagImage_b_f_roundWidth, 1);
		mTypedArray.recycle();
	}
	
	/**
	 * 画笔对象的引用
	 */
	private Paint paint;
	
	/**
	 * 圆环的宽度
	 */
	private float roundWidth;
	
	/**
	 * 圆环的颜色
	 */
	private int roundColor;
	
	/**
	 * 中间进度百分比的字符串的颜色
	 */
	private int textColor;
	
	/**
	 * 中间进度百分比的字符串的字体
	 */
	private float textSize;
	
	private String mText = "";
	
	@Override
	protected void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);
		
		/**
		 * 画最外层的大圆环
		 */
		final int centre = getWidth() / 2; // 获取圆心的x坐标
		int radius = (int)(centre - roundWidth / 2); // 圆环的半径
		paint.setColor(roundColor); // 设置圆环的颜色
		paint.setStyle(Paint.Style.STROKE); // 设置空心
		paint.setStrokeWidth(roundWidth); // 设置圆环的宽度
		paint.setAntiAlias(true); // 消除锯齿
		canvas.drawCircle(centre, centre, radius, paint); // 画出圆环
		
		/**
		 * 画文字
		 */
		paint.setStrokeWidth(0);
		paint.setColor(textColor);
		paint.setTextSize(textSize);
		paint.setTypeface(Typeface.SANS_SERIF); // 设置字体
		FontMetrics fm = paint.getFontMetrics();
		float textHeight = (float)Math.ceil(fm.descent - fm.ascent);
		float textWidth = paint.measureText(mText);
		canvas.drawText(mText, centre - textWidth / 2 - 1, centre + (DensityUtil.px2sp(getContext(), textHeight) / 2) + 1, paint); // 画出文字
	}
	
	public void setText(String text)
	{
		mText = text;
		postInvalidate();
	}
}
