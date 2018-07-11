package com.hxjr.p2p.ad5.ui.discovery.spread;

import java.util.List;

import com.hxjr.p2p.ad5.bean.InvitedUserInfo;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class SpreadListAdapter extends BaseAdapter
{
	private Context mContext;
	
	private List<InvitedUserInfo> invitedUserInfos;
	
	public SpreadListAdapter(Context context, List<InvitedUserInfo> invitedUserInfos)
	{
		mContext = context;
		this.invitedUserInfos = invitedUserInfos;
	}
	
	@Override
	public int getCount()
	{
		return invitedUserInfos.size();
	}
	
	@Override
	public Object getItem(int position)
	{
		return invitedUserInfos.get(position);
	}
	
	@Override
	public long getItemId(int position)
	{
		return position;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		// TODO Auto-generated method stub
		return null;
	}
	
}
