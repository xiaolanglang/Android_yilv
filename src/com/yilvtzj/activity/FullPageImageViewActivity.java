package com.yilvtzj.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageContainer;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.yilvtzj.R;
import com.yilvtzj.app.LocationApplication;
import com.yilvtzj.view.ZoomImageView;
import com.yilvtzj.volley.cache.LruBitmapCache;

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
	private static ImageLoader mImageLoader;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.full_page_image);
		mPictures = getIntent().getExtras().getStringArray("pictures");
		mSelectId = getIntent().getExtras().getInt("selectId");
		mImageLoader = new ImageLoader(LocationApplication.requestQueue, new LruBitmapCache());

		ViewPager vp = (ViewPager) findViewById(R.id.viewpager);
		vp.setOffscreenPageLimit(5);
		vp.setAdapter(new SamplePagerAdapter());
		vp.setCurrentItem(mSelectId);
		mActivity = FullPageImageViewActivity.this;
	}

	static class SamplePagerAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return mPictures.length;
		}

		@Override
		public View instantiateItem(ViewGroup container, final int position) {

			ViewGroup view = (ViewGroup) mActivity.getLayoutInflater().inflate(R.layout.full_page_viewpager, null);
			final ZoomImageView zoomImageView = new ZoomImageView(container.getContext());
			ProgressBar progress_img = new ProgressBar(mActivity);
			TextView reload_tv = new TextView(mActivity);
			reload_tv.setText("加载失败点击重试");
			reload_tv.setTextColor(mActivity.getResources().getColor(R.color.black));

			RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
			lp.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
			view.addView(zoomImageView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			view.addView(progress_img, lp);
			view.addView(reload_tv, lp);

			mImageLoader.get(mPictures[position], new ImageListener() {

				@Override
				public void onErrorResponse(VolleyError arg0) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onResponse(ImageContainer container, boolean b) {
					zoomImageView.setImageBitmap(container.getBitmap());
				}
			});
			reload_tv.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO
				}
			});
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
	}

	public static void actionStart(Context context, String[] strings, int selectId) {
		Intent intent = new Intent(context, FullPageImageViewActivity.class);
		intent.putExtra("pictures", strings);
		intent.putExtra("selectId", selectId);
		context.startActivity(intent);
	}

}
