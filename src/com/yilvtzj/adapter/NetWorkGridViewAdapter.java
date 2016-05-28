package com.yilvtzj.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.android.volley.toolbox.NetworkImageView;
import com.yilvtzj.R;
import com.yilvtzj.aplication.MyApplication;

public class NetWorkGridViewAdapter extends BaseAdapter {

	private Context mContext;
	private String[] mThumbIds;

	public void setmThumbIds(String[] mThumbIds) {
		this.mThumbIds = mThumbIds;
	}

	/**
	 * 记录每个子项的高度。
	 */
	private int mItemHeight = 0;

	public NetWorkGridViewAdapter(Context context) {
		this(context, null);
	}

	public NetWorkGridViewAdapter(Context context, String[] mThumbIds) {
		this.mContext = context;
		this.mThumbIds = mThumbIds;
	}

	@Override
	public int getCount() {
		return mThumbIds.length; // 返回item的个数
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	/*
	 * 重要的方法。通过viewHolder复用view，并且设置好item的宽度
	 * 
	 * @param position
	 * 
	 * @param convertView
	 * 
	 * @param parent
	 * 
	 * @return
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			// init convertView by layout
			LayoutInflater inflater = LayoutInflater.from(mContext);
			convertView = inflater.inflate(R.layout.item_network_image, null);
		}
		// 得到item中显示图片的view
		NetworkImageView networkImageView = ViewHolder.get(convertView, R.id.icon);
		// 得到item中的textview
		// 设置默认的图片
		networkImageView.setDefaultImageResId(R.drawable.ic_loading_gray);
		// 设置图片加载失败后显示的图片
		networkImageView.setErrorImageResId(R.drawable.ic_loading_gray);

		if (networkImageView.getLayoutParams().height != mItemHeight) {
			// 通过activity中计算出的结果，在这里设置networkImageview的高度（得到的是正方形）
			networkImageView.getLayoutParams().height = mItemHeight;
		}

		// 开始加载网络图片
		networkImageView.setImageUrl(mThumbIds[position], MyApplication.volleyImageLoader);
		return convertView;
	}

	/**
	 * 设置item子项的高度。
	 */
	public void setItemHeight(int height) {
		if (height == mItemHeight) {
			return;
		}
		mItemHeight = height;
		notifyDataSetChanged();
	}

}
