package com.yilvtzj.adapter.home;

import android.content.ClipData.Item;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.yilvtzj.R;

public class GridAdapter extends BaseAdapter {
	private Context mContext;
	private int[] mThumbIds;

	// Constructor
	public GridAdapter(Context c, int[] mThumbIds2) {
		mContext = c;
		this.mThumbIds = mThumbIds2;
	}

	public int getCount() {
		return mThumbIds.length;
	}

	public Object getItem(int position) {
		return null;
	}

	public long getItemId(int position) {
		return 0;
	}

	// create a new ImageView for each item referenced by the Adapter
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.home_item_image, null);
		}
		ImageView image = (ImageView) convertView.findViewById(R.id.icon);
		Item item = (Item) getItem(position);
		image.setImageResource(mThumbIds[position]);
		return convertView;

	}

}