package com.yilvtzj.util;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.yilvtzj.app.LocationApplication;

public class MyImageLoader {
	public ImageView mImageView;
	private MyImageLoadingListener mImageLoadingListener = new MyImageLoadingListener();
	public ProgressBar mProgress_img;
	public TextView mReload_tv;

	public void displayImage(String uri) {
		LocationApplication.imageLoader.displayImage(uri, mImageView, mImageLoadingListener);
	}

	private class MyImageLoadingListener implements ImageLoadingListener {

		private void mProgress_img(int visiable) {
			if (mProgress_img != null) {
				mProgress_img.setVisibility(visiable);
			}
		}

		private void mReload_tv(int visiable) {
			if (mReload_tv != null) {
				mReload_tv.setVisibility(visiable);
			}
		}

		@Override
		public void onLoadingStarted(String arg0, View arg1) {
			mImageView.setTag(arg0);
			mProgress_img(View.VISIBLE);
			mReload_tv(View.GONE);

		}

		@Override
		public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
			mProgress_img(View.GONE);
			mReload_tv(View.VISIBLE);
		}

		@Override
		public void onLoadingComplete(String tag, View view, Bitmap bitmap) {
			mProgress_img(View.GONE);
			mReload_tv(View.GONE);
			if (mImageView.getTag().equals(tag)) {
				ImageView img = (ImageView) view;
				img.setImageBitmap(bitmap);
			}

		}

		@Override
		public void onLoadingCancelled(String arg0, View arg1) {
			mProgress_img(View.GONE);
			mReload_tv(View.GONE);
		}
	}

}
