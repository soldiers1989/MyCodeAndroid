package com.hxjr.p2p.ad5.ui.investment.adapter;

import java.util.List;

import com.hxjr.p2p.ad5.R;
import com.hxjr.p2p.ad5.bean.MfenleiBean;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class FlAdapter extends BaseAdapter {

	private List<MfenleiBean> flBeanList;
	private Context context;

	public FlAdapter(Context context, List<MfenleiBean> flBeanList) {
		this.context = context;
		this.flBeanList = flBeanList;
	}

	@Override
	public int getCount() {
		if (flBeanList.size() == 0) {

			return 0;
		} else {
			return flBeanList.size();
		}
	}

	@Override
	public Object getItem(int position) {

		return flBeanList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView,
		ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
				R.layout.mfl_list_item, parent, false);
			holder = new ViewHolder();
			holder.tvTitle=(TextView) convertView.findViewById(R.id.flbidTitle);
			holder.tvRate=(TextView) convertView.findViewById(R.id.flyearRate);
			holder.tvCycle=(TextView) convertView.findViewById(R.id.flcycle);
			holder.tvIco=(TextView) convertView.findViewById(R.id.flbidTypeImg);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tvIco.setText(flBeanList.get(position).getIcon());
		holder.tvTitle.setText(flBeanList.get(position).getName());
		holder.tvRate.setText(flBeanList.get(position).getMinInterestRates()+"-"+flBeanList.get(position).getMaxInterestRates());
		holder.tvCycle.setText(flBeanList.get(position).getMinLoanLife()+"-"+flBeanList.get(position).getMaxLoanLife());
		return convertView;
	}

	class ViewHolder {
		
		TextView tvIco;
		TextView tvTitle;
		TextView tvRate;
		TextView tvCycle;
	}

}
