package com.yilvtzj.util;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.yilvtzj.app.LocationApplication;

public class MyImageLoader {
	private ImageView mImageView;
	private MyImageLoadingListener mImageLoadingListener = new MyImageLoadingListener();
	private View mProgress_View, mReload_View;
	private ImageLoadFinish loadFinish;

	public void get(String uri, ImageView mImageView) {
		this.mImageView = mImageView;
		LocationApplication.imageLoader.displayImage(uri, mImageView, mImageLoadingListener);
	}

	public View getmProgress_View() {
		return mProgress_View;
	}

	public void setmProgress_View(View mProgress_View) {
		this.mProgress_View = mProgress_View;
	}

	public View getmReload_View() {
		return mReload_View;
	}

	public void setmReload_View(View mReload_View) {
		this.mReload_View = mReload_View;
	}

	private class MyImageLoadingListener implements ImageLoadingListener {

		private void mProgress_View(int visiable) {
			if (mProgress_View != null) {
				mProgress_View.setVisibility(visiable);
			}
		}

		private void mReload_View(int visiable) {
			if (mReload_View != null) {
				mReload_View.setVisibility(visiable);
			}
		}

		@Override
		public void onLoadingStarted(String arg0, View arg1) {
			mImageView.setTag(arg0);
			mProgress_View(View.VISIBLE);
			mReload_View(View.GONE);

		}

		@Override
		public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
			mProgress_View(View.GONE);
			mReload_View(View.VISIBLE);
		}

		@Override
		public void onLoadingComplete(String tag, View view, Bitmap bitmap) {
			mProgress_View(View.GONE);
			mReload_View(View.GONE);
			if (mImageView.getTag().equals(tag)) {
				ImageView img = (ImageView) view;
				img.setImageBitmap(bitmap);
			}
			if (loadFinish != null) {
				loadFinish.result(bitmap);
			}

		}

		@Override
		public void onLoadingCancelled(String arg0, View arg1) {
			mProgress_View(View.GONE);
			mReload_View(View.GONE);
		}
	}

	public interface ImageLoadFinish {
		void result(Bitmap bitmap);
	}

}
