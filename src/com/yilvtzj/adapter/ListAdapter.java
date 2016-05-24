package com.yilvtzj.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yilvtzj.R;
import com.yilvtzj.entity.DongtaiComment;

public class ListAdapter extends BaseAdapter {
	private Context mContext;
	private List<DongtaiComment> list;

	public ListAdapter(Context mContext, List<DongtaiComment> list) {
		this.mContext = mContext;
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_dongtai_comment, null);
		} else {
			return convertView;
		}
		TextView name = (TextView) convertView.findViewById(R.id.name);
		TextView time = (TextView) convertView.findViewById(R.id.time);
		TextView content = (TextView) convertView.findViewById(R.id.content);
		DongtaiComment comment = list.get(position);
		name.setText(comment.getUser().getNickname());
		time.setText(comment.getCreateDate());
		content.setText(comment.getContent());
		return convertView;
	}

}
