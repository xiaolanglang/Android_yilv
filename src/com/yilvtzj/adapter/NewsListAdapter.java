package com.yilvtzj.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.yilvtzj.R;
import com.yilvtzj.entity.MessageItem;

public class NewsListAdapter extends BaseAdapter {

	private Context context;
	private List<MessageItem> list;

	public NewsListAdapter(Context context, List<MessageItem> list) {
		this.context = context;
		this.list = list;
	}

	@Override
	public int getCount() {
		if (list == null) {
			return 0;
		}
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
			LayoutInflater inflater = LayoutInflater.from(context);
			convertView = inflater.inflate(R.layout.item_fragment_news, null);
		}
		return convertView;
	}

}
