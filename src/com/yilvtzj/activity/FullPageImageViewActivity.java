package com.yilvtzj.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.yilvtzj.R;
import com.yilvtzj.util.MyImageLoader;
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

	static class SamplePagerAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return mPictures.length;
		}

		@Override
		public View instantiateItem(ViewGroup container, final int position) {

			ViewGroup view = (ViewGroup) mActivity.getLayoutInflater().inflate(R.layout.full_page_viewpager, null);
			final ZoomImageView zoomImageView = new ZoomImageView(container.getContext());
			ProgressBar progressBar = new ProgressBar(mActivity);

			RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
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
	}

	public static void actionStart(Context context, String[] strings, int selectId) {
		Intent intent = new Intent(context, FullPageImageViewActivity.class);
		intent.putExtra("pictures", strings);
		intent.putExtra("selectId", selectId);
		context.startActivity(intent);
	}

}
