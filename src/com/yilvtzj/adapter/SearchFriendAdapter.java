package com.yilvtzj.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yilvtzj.R;
import com.yilvtzj.entity.Account;
import com.yilvtzj.view.CircleImageView;

public class SearchFriendAdapter extends BaseAdapter {
	private Context context;
	private List<Account> list;

	public SearchFriendAdapter(Context context, List<Account> list) {
		this.context = context;
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.item_search_friends, null);
			holder.imageView = (CircleImageView) convertView.findViewById(R.id.image);
			holder.nickname = (TextView) convertView.findViewById(R.id.nickname);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.nickname.setText(list.get(position).getNickname());

		return convertView;
	}

	private class ViewHolder {
		CircleImageView imageView;
		TextView nickname;
	}

}
