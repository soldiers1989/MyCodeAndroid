package com.hxjr.p2p.ad5.ui.mine;

import java.util.List;

import com.hxjr.p2p.ad5.bean.LoanNode;
import com.hxjr.p2p.ad5.utils.AmountUtil;
import com.hxjr.p2p.ad5.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 
 * 
 * @author  sushuo
 * @date 2015年8月19日
 */
public class RepaymentListAdapter extends BaseExpandableListAdapter
{
	
	private Context context;
	
	private LayoutInflater inflater = null;
	
	private List<LoanNode> nodeList;
	
	public RepaymentListAdapter(Context context, List<LoanNode> nodeList)
	{
		this.context = context;
		this.nodeList = nodeList;
		inflater = LayoutInflater.from(context);
	}
	
	@Override
	public LoanNode getChild(int groupPosition, int childPosition)
	{
		// TODO Auto-generated method stub
		return getGroup(groupPosition).getChildren().get(childPosition);
	}
	
	@Override
	public long getChildId(int groupPosition, int childPosition)
	{
		// TODO Auto-generated method stub
		return childPosition;
	}
	
	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent)
	{
		// TODO Auto-generated method stub
		ParentViewHolder holder;
		if (convertView == null)
		{
			convertView = inflater.inflate(R.layout.repayment_list_item_group, parent, false);
			holder = new ParentViewHolder();
			holder.head_tv = (TextView)convertView.findViewById(R.id.head_tv);
			holder.title_tv = (TextView)convertView.findViewById(R.id.title_tv);
			holder.period_no_tv = (TextView)convertView.findViewById(R.id.period_no_tv);
			holder.period_total_tv = (TextView)convertView.findViewById(R.id.period_total_tv);
			holder.expand_iv = (ImageView)convertView.findViewById(R.id.expand_iv);
			convertView.setTag(holder);
		}
		else
		{
			holder = (ParentViewHolder)convertView.getTag();
		}
		
		LoanNode loanNode = nodeList.get(groupPosition);
		if (loanNode != null)
		{
			holder.title_tv.setText(loanNode.getTitle());
			holder.period_no_tv.setText(loanNode.getPeriodNo() + "");
			holder.period_total_tv.setText(loanNode.getPeriodTotal() + "");
		}
		
		holder.expand_iv.setBackgroundResource((isExpanded) ? R.drawable.arrow_x : R.drawable.icon_jt3);
		
		holder.head_tv.setVisibility(View.INVISIBLE);
		if (groupPosition == 0)
		{
			holder.head_tv.setVisibility(View.VISIBLE);
		}
		
		convertView.setBackgroundColor((groupPosition % 2 == 1) ? context.getResources().getColor(R.color.white)
			: context.getResources().getColor(R.color.transparent));
			
		return convertView;
	}
	
	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent)
	{
		// TODO Auto-generated method stub
		ChildViewHolder holder;
		if (convertView == null)
		{
			convertView = inflater.inflate(R.layout.repayment_list_item_child, parent, false);
			holder = new ChildViewHolder();
			holder.principal_tv = (TextView)convertView.findViewById(R.id.principal_tv);
			holder.interest_tv = (TextView)convertView.findViewById(R.id.interest_tv);
			convertView.setTag(holder);
		}
		else
		{
			holder = (ChildViewHolder)convertView.getTag();
		}
		
		LoanNode loanNode = nodeList.get(groupPosition);
		if (loanNode != null)
		{
			holder.principal_tv.setText(AmountUtil.numKbPointFormat(loanNode.getPrincipal()));
			holder.interest_tv.setText(AmountUtil.numKbPointFormat(loanNode.getInterest()));
		}
		
		convertView.setBackgroundColor((groupPosition % 2 == 1) ? context.getResources().getColor(R.color.white)
			: context.getResources().getColor(R.color.transparent));
		return convertView;
	}
	
	@Override
	public int getChildrenCount(int groupPosition)
	{
		// TODO Auto-generated method stub
		return nodeList.get(groupPosition).getChildren().size();
	}
	
	@Override
	public LoanNode getGroup(int groupPosition)
	{
		// TODO Auto-generated method stub
		return nodeList.get(groupPosition);
	}
	
	@Override
	public int getGroupCount()
	{
		// TODO Auto-generated method stub
		return nodeList.size();
	}
	
	@Override
	public long getGroupId(int groupPosition)
	{
		// TODO Auto-generated method stub
		return groupPosition;
	}
	
	@Override
	public boolean hasStableIds()
	{
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition)
	{
		// TODO Auto-generated method stub
		return false;
	}
	
	public class ParentViewHolder
	{
		TextView head_tv;
		
		TextView title_tv;
		
		TextView period_no_tv;
		
		TextView period_total_tv;
		
		ImageView expand_iv;
	}
	
	public class ChildViewHolder
	{
		TextView principal_tv;
		
		TextView interest_tv;
	}
	
}
