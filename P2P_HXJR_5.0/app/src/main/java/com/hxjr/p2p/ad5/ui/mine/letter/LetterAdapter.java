package com.hxjr.p2p.ad5.ui.mine.letter;

import java.util.ArrayList;
import java.util.List;

import com.hxjr.p2p.ad5.bean.Letter;
import com.hxjr.p2p.ad5.utils.DMConstant;
import com.hxjr.p2p.ad5.R;
import com.dm.widgets.TextViewFixTouchConsume;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author  tangjian
 * @date 2015-6-4
 */
public class LetterAdapter extends BaseAdapter
{
	private Context context;
	
	private List<Letter> letterList;
	
	private ViewHolder holder;
	
	public LetterAdapter(Context context, List<Letter> letters)
	{
		this.context = context;
		letterList = new ArrayList<Letter>(DMConstant.DigitalConstant.TEN);
		letterList.addAll(letters);
	}
	
	public void updateList(List<Letter> letters)
	{
		if (null != letterList && null != letters)
		{
			letterList.clear();
			letterList.addAll(letters);
			notifyDataSetChanged();
		}
	}
	
	/**
	 * 添加记录
	 * @param list
	 */
	public void addAll(List<Letter> list)
	{
		letterList.addAll(list);
		this.notifyDataSetChanged();
	}
	
	/**
	 * 清除记录
	 */
	public void clearList()
	{
		letterList.clear();
	}
	
	@Override
	public int getCount()
	{
		return letterList.size();
	}
	
	@Override
	public Object getItem(int position)
	{
		return letterList.get(position);
	}
	
	@Override
	public long getItemId(int position)
	{
		return position;
	}
	
	List<ViewHolder> holders = new ArrayList<ViewHolder>();
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		if (null == convertView)
		{
			convertView = LayoutInflater.from(context).inflate(R.layout.letter_in_station_item, parent, false);
			holder = new ViewHolder();
			holder.img = (ImageView)convertView.findViewById(R.id.letter_item_img);
			holder.arrowImage = (ImageView)convertView.findViewById(R.id.letter_arrow);
			holder.title = (TextView)convertView.findViewById(R.id.letter_item_title);
			holder.date = (TextView)convertView.findViewById(R.id.letter_date_tv);
			//			holder.time = (TextView)convertView.findViewById(R.id.letter_time_tv);
			holder.detail = (TextViewFixTouchConsume)convertView.findViewById(R.id.letter_in_station_detail);
			convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder)convertView.getTag();
		}
		
		holder.title.setText(letterList.get(position).getTitle());
		//		String temp = TimeUtils.getTime(Long.parseLong(letterList.get(position).getSendTime()));
		//		int index = temp.indexOf(" ");
		holder.date.setText(letterList.get(position).getSendTime());
		//		holder.time.setText(temp.substring(index));
		
		String htmlLinkText = letterList.get(position).getContent();
		holder.detail.setText(Html.fromHtml(htmlLinkText));
		holder.detail.setMovementMethod(TextViewFixTouchConsume.LocalLinkMovementMethod.getInstance());
		CharSequence text = holder.detail.getText();
		if (text instanceof Spannable)
		{
			Spannable sp = (Spannable)holder.detail.getText();
			int end = text.length();
			URLSpan[] urls = sp.getSpans(0, end, URLSpan.class);
			SpannableStringBuilder style = new SpannableStringBuilder(text);
			style.clearSpans(); // should clear old spans      
			for (URLSpan url : urls)
			{
				MyURLSpan myURLSpan = new MyURLSpan(url.getURL());
				style.setSpan(myURLSpan, sp.getSpanStart(url), sp.getSpanEnd(url), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
			holder.detail.setText(style);
		}
		
		if (letterList.get(position).getStatus().equals("WD"))
		{
			holder.img.setBackgroundResource(R.drawable.letter_unread);
		}
		else
		{
			holder.img.setBackgroundResource(R.drawable.letter_open);
		}
		
		if (letterList.get(position).isLetterShowing())
		{
			holder.detail.setVisibility(View.VISIBLE);
			holder.arrowImage.setBackgroundResource(R.drawable.arrow_x);
		}
		else
		{
			holder.detail.setVisibility(View.GONE);
			holder.arrowImage.setBackgroundResource(R.drawable.icon_jt3);
		}
		return convertView;
	}
	
	public void onItemClick(View view)
	{
		ViewHolder holder = (ViewHolder)view.getTag();
		if (holder.isDetailShowed)
		{
			holder.isDetailShowed = false;
			holder.detail.setVisibility(View.GONE);
			holder.arrowImage.setBackgroundResource(R.drawable.icon_jt3);
		}
		else
		{
			holder.isDetailShowed = true;
			holder.detail.setVisibility(View.VISIBLE);
			holder.arrowImage.setBackgroundResource(R.drawable.arrow_x);
		}
	}
	
	class ViewHolder
	{
		private ImageView img;
		
		private ImageView arrowImage;
		
		private TextView title;
		
		private TextView date;
		
		//		private TextView time;
		
		private TextViewFixTouchConsume detail;
		
		private boolean isDetailShowed = false;
	}
	
	private class MyURLSpan extends ClickableSpan
	{
		private String mUrl;
		
		MyURLSpan(String url)
		{
			//			if (!url.contains("http://") && null != P2PApplication.getInstance().getSysConfig())
			//			{
			//				mUrl = P2PApplication.getInstance().getSysConfig().getImageUrlPre() + url;
			//			}
			//			else
			//			{
			mUrl = url;
			//			}
		}
		
		@Override
		public void onClick(View widget)
		{
			//			ToastUtil.getInstant().show(context, " hello! ");
			Intent intent = new Intent(context, ThirdWebPageActivity.class);
			intent.putExtra("url", mUrl);
			intent.putExtra("title", context.getString(R.string.letter_in_station));
			context.startActivity(intent);
		}
	}
}
