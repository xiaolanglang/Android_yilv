package com.yilvtzj.adapter.home;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yilvtzj.R;
import com.yilvtzj.activity.FullPageImageViewActivity;
import com.yilvtzj.pojo.DongtaiMsg;
import com.yilvtzj.util.DateUtil;
import com.yilvtzj.view.NoScrolGridView;

/**
 * Created by IntelliJ IDEA Project: com.ht.mynote.adapters Author: 安诺爱成长 Email:
 * 1399487511@qq.com Date: 2015/5/2
 */
public class HomeAdapter extends BaseAdapter {
	private Context context;
	private List<DongtaiMsg> list;

	public HomeAdapter(Context context, List<DongtaiMsg> list) {
		this.context = context;
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int i) {
		return list.get(i);
	}

	@Override
	public long getItemId(int i) {
		return i;
	}

	@Override
	public View getView(int i, View view, ViewGroup viewGroup) {
		ViewHolder viewHolder = null;
		if (view == null) {
			view = LayoutInflater.from(context).inflate(R.layout.home_item, null);
			viewHolder = new ViewHolder();
			viewHolder.name = (TextView) view.findViewById(R.id.name);
			viewHolder.content = (TextView) view.findViewById(R.id.content);
			viewHolder.time = (TextView) view.findViewById(R.id.time);
			viewHolder.gridView = (NoScrolGridView) view.findViewById(R.id.gridview);
			viewHolder.gridView.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					FullPageImageViewActivity.actionStart(context, mThumbIds, position);
				}
			});
			view.setTag(viewHolder);
		} else
			viewHolder = (ViewHolder) view.getTag();

		DongtaiMsg dongtaiMsg = list.get(i);
		viewHolder.name.setText(dongtaiMsg.getAccount().getNickname());
		viewHolder.content.setText(dongtaiMsg.getContent());
		viewHolder.time.setText(DateUtil.rangeTime(dongtaiMsg.getCreateTime(), "yyyy-MM-dd hh:mm"));
		viewHolder.gridView.setAdapter(new GridAdapter(context, mThumbIds));
		return view;
	}

	private class ViewHolder {
		private TextView name, content, time;
		private NoScrolGridView gridView;
	}

	// Keep all Images in array
	private int[] mThumbIds = { R.drawable.c2, R.drawable.c3, R.drawable.c4, R.drawable.c5, R.drawable.c6,
			R.drawable.c7, R.drawable.c1, R.drawable.c8, R.drawable.c9, };
}
