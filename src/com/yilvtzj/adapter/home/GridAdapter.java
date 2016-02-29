package com.yilvtzj.adapter.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.yilvtzj.R;
import com.yilvtzj.util.MyImageLoader;

public class GridAdapter extends BaseAdapter {
	private Context mContext;
	private String[] mThumbIds;

	// Constructor
	public GridAdapter(Context c, String[] strings) {
		mContext = c;
		this.mThumbIds = strings;
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
		MyImageLoader imageLoader = new MyImageLoader();
		ImageView imageView = (ImageView) convertView.findViewById(R.id.icon);

		imageLoader.mImageView = imageView;
		imageLoader.displayImage(mThumbIds[position]);
		return convertView;

	}

}