package com.hxjr.p2p.ad5.ui.mine.bank;

import java.util.ArrayList;
import java.util.List;

import com.hxjr.p2p.ad5.bean.BankCard;
import com.hxjr.p2p.ad5.utils.DMConstant;
import com.hxjr.p2p.ad5.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * @author  tangjian
 * @date 2015-6-4
 */
public class BankCardAdapter extends BaseAdapter
{
	private List<BankCard> cardList;
	
	private Context context;
	
	public BankCardAdapter(Context context, List<BankCard> cards)
	{
		this.context = context;
		cardList = new ArrayList<BankCard>(DMConstant.DigitalConstant.TEN);
		cardList.addAll(cards);
	}
	
	public void updateList(List<BankCard> list)
	{
		if (null != cardList && null != list)
		{
			cardList.clear();
			cardList.addAll(list);
			notifyDataSetChanged();
		}
	}
	public void clearAll(){
		cardList.clear();
		notifyDataSetChanged();
	}
	
	@Override
	public int getCount()
	{
		return cardList.size();
	}
	
	@Override
	public Object getItem(int position)
	{
		return cardList.get(position);
	}
	
	@Override
	public long getItemId(int position)
	{
		return position;
	}
	
	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		ViewHolder holder;
		if (null == convertView)
		{
			convertView = LayoutInflater.from(context).inflate(R.layout.bank_card_item, null);
			holder = new ViewHolder();
			convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder)convertView.getTag();
		}
		holder.bankName = (TextView)convertView.findViewById(R.id.bank_name);
		holder.cardTailNum = (TextView)convertView.findViewById(R.id.bank_card_tail_num);
		
		holder.bankName.setText(cardList.get(position).getBankname());
		String cardNum = cardList.get(position).getBankNumber();
		holder.cardTailNum.setText("尾号" + cardNum.substring(cardNum.length() - 4, cardNum.length()).trim());
		return convertView;
	}
	
	class ViewHolder
	{
		private TextView bankName;
		
		private TextView cardTailNum;
	}
}
