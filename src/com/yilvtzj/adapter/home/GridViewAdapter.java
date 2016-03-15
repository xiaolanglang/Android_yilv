package com.yilvtzj.adapter.home;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.yilvtzj.R;
import com.yilvtzj.app.MyApplication;
import com.yilvtzj.volley.cache.LruBitmapCache;

public class GridViewAdapter extends BaseAdapter {

	private Context mContext;
	private String[] mThumbIds;
	private static ImageLoader mImageLoader; // imageLoader对象，用来初始化NetworkImageView

	public void setmThumbIds(String[] mThumbIds) {
		this.mThumbIds = mThumbIds;
	}

	/**
	 * 记录每个子项的高度。
	 */
	private int mItemHeight = 0;

	public GridViewAdapter(Context context) {
		this(context, null);
	}

	public GridViewAdapter(Context context, String[] mThumbIds) {
		this.mContext = context;
		this.mThumbIds = mThumbIds;
		// 初始化mImageLoader，并且传入了自定义的内存缓存
		mImageLoader = new ImageLoader(MyApplication.requestQueue, new LruBitmapCache()); // 初始化一个loader对象，可以进行自定义配置
		// 配置是否进行磁盘缓存
		// mImageLoader.setShouldCache(true); // 设置允许磁盘缓存，默认是true
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
			convertView = inflater.inflate(R.layout.item_home_image, null);
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
		networkImageView.setImageUrl(mThumbIds[position], mImageLoader);
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

	private static class ViewHolder {
		// I added a generic return type to reduce the casting noise in client
		// code
		@SuppressWarnings("unchecked")
		public static <T extends View> T get(View view, int id) {
			SparseArray<View> viewHolder = (SparseArray<View>) view.getTag();
			if (viewHolder == null) {
				viewHolder = new SparseArray<View>();
				view.setTag(viewHolder);
			}
			View childView = viewHolder.get(id);
			if (childView == null) {
				childView = view.findViewById(id);
				viewHolder.put(id, childView);
			}
			return (T) childView;
		}
	}

}
