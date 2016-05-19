package com.yilvtzj.activity.common;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.yilvtzj.R;
import com.yilvtzj.app.MyApplication;
import com.yilvtzj.view.ZoomImageView;

/**
 * 全屏查看页面
 * 
 * @author Administrator
 *
 */
public class FullPageImageViewActivity extends Activity {

	private static String[] mPictures;
	private static int mSelectId;
	private static Activity mActivity;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.full_page_image);
		mPictures = getIntent().getExtras().getStringArray("pictures");
		mSelectId = getIntent().getExtras().getInt("selectId");

		ViewPager vp = (ViewPager) findViewById(R.id.viewpager);
		vp.setOffscreenPageLimit(5);
		vp.setAdapter(new SamplePagerAdapter());
		vp.setCurrentItem(mSelectId);
		mActivity = FullPageImageViewActivity.this;
	}

	private class SamplePagerAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return mPictures.length;
		}

		@Override
		public View instantiateItem(ViewGroup container, final int position) {

			ViewGroup view = (ViewGroup) mActivity.getLayoutInflater().inflate(R.layout.full_page_viewpager, null);
			final ZoomImageView zoomImageView = new ZoomImageView(container.getContext());
			ProgressBar progressBar = new ProgressBar(mActivity);

			RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
			lp.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
			view.addView(zoomImageView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			view.addView(progressBar, lp);

			final MyImageLoader myImageLoader = new MyImageLoader();
			myImageLoader.setmProgress_View(progressBar);
			myImageLoader.get(mPictures[position], zoomImageView);
			container.addView(view, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			return view;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

		// ------

		private class MyImageLoader {
			private ImageView mImageView;
			private MyImageLoadingListener mImageLoadingListener = new MyImageLoadingListener();
			private View mProgress_View, mReload_View;

			public void get(String uri, ImageView mImageView) {
				this.mImageView = mImageView;
				MyApplication.imageLoader.displayImage(uri, mImageView, mImageLoadingListener);
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

				}

				@Override
				public void onLoadingCancelled(String arg0, View arg1) {
					mProgress_View(View.GONE);
					mReload_View(View.GONE);
				}
			}

		}
	}

	public static void actionStart(Context context, String[] strings, int selectId) {
		Intent intent = new Intent(context, FullPageImageViewActivity.class);
		intent.putExtra("pictures", strings);
		intent.putExtra("selectId", selectId);
		context.startActivity(intent);
	}

}
