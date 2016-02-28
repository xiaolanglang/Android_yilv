package com.yilvtzj.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

	private static int[] mPictures;
	private static int mSelectId;
	private static Activity mActivity;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.full_page_image);
		mPictures = (int[]) getIntent().getExtras().getSerializable("pictures");
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
			ZoomImageView zoomImageView = new ZoomImageView(container.getContext());
			ProgressBar progress_img = new ProgressBar(mActivity);
			TextView reload_tv = new TextView(mActivity);
			reload_tv.setText("加载失败点击重试");

			RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
			lp.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
			view.addView(zoomImageView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			// view.addView(progress_img, lp);
			// view.addView(reload_tv, lp);

			final MyImageLoader myImageLoader = new MyImageLoader();
			reload_tv.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// myImageLoader.displayImage(Global.TIANGOU_SERVICE +
					// mPictures.get(position).getSrc());
				}
			});
			myImageLoader.mReload_tv = reload_tv;
			myImageLoader.mImageView = zoomImageView;
			myImageLoader.mProgress_img = progress_img;
			Bitmap bmp = BitmapFactory.decodeResource(mActivity.getResources(), mPictures[position]);
			zoomImageView.setImageBitmap(bmp);
			// myImageLoader.displayImage(Global.TIANGOU_SERVICE +
			// mPictures.get(position).getSrc());
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

	public static void actionStart(Context context, int[] pictures, int selectId) {
		Intent intent = new Intent(context, FullPageImageViewActivity.class);
		intent.putExtra("pictures", pictures);
		intent.putExtra("selectId", selectId);
		context.startActivity(intent);
	}

}
