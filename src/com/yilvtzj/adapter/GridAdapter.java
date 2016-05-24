package com.yilvtzj.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.yilvtzj.R;

public class GridAdapter extends BaseAdapter {

	private List<Bitmap> bitmaps;
	private Context mContext;

	public GridAdapter(Context mContext) {
		this.mContext = mContext;
	}

	public List<Bitmap> getBitmaps() {
		return bitmaps;
	}

	public void setBitmaps(List<Bitmap> bitmaps) {
		this.bitmaps = bitmaps;
	}

	@Override
	public int getCount() {
		if (bitmaps != null) {
			return bitmaps.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		return bitmaps.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_image, null);
		}
		ImageView image = ViewHolder.get(convertView, R.id.img);
		image.setImageBitmap(bitmaps.get(position));
		return convertView;
	}

}