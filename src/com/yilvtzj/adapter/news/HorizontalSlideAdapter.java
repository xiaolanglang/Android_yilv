package com.yilvtzj.adapter.news;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.yilvtzj.R;
import com.yilvtzj.entity.MessageItem;

public class HorizontalSlideAdapter extends BaseAdapter {

	/** 屏幕宽度 */
	private int mScreenWidth;

	/** 删除按钮事件 */
	private DeleteButtonOnclickImpl mDelOnclickImpl;
	/** HorizontalScrollView左右滑动事件 */
	private ScrollViewScrollImpl mScrollImpl;

	/** 布局参数,动态让HorizontalScrollView中的TextView宽度包裹父容器 */
	private LinearLayout.LayoutParams mParams;

	/** 记录滑动出删除按钮的itemView */
	public HorizontalScrollView mScrollView;

	/** touch事件锁定,如果已经有滑动出删除按钮的itemView,就屏蔽下一整次(down,move,up)的onTouch操作 */
	public boolean mLockOnTouch = false;

	private Context context;

	private List<MessageItem> list;

	public HorizontalSlideAdapter(Context context, List<MessageItem> list) {
		this.context = context;
		this.list = list;
		// 搞到屏幕宽度
		Display defaultDisplay = ((Activity) context).getWindowManager().getDefaultDisplay();
		DisplayMetrics metrics = new DisplayMetrics();
		defaultDisplay.getMetrics(metrics);
		mScreenWidth = metrics.widthPixels;
		mParams = new LinearLayout.LayoutParams(mScreenWidth, LinearLayout.LayoutParams.MATCH_PARENT);
		// 初始化删除按钮事件与item滑动事件
		mDelOnclickImpl = new DeleteButtonOnclickImpl();
		mScrollImpl = new ScrollViewScrollImpl();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = View.inflate(context, R.layout.item_fragment_news, null);
			holder.scrollView = (HorizontalScrollView) convertView;
			holder.scrollView.setOnTouchListener(mScrollImpl);
			holder.linearLayout = (LinearLayout) convertView.findViewById(R.id.left);
			// 设置item内容为fill_parent的
			holder.linearLayout.setLayoutParams(mParams);
			holder.deleteButton = (Button) convertView.findViewById(R.id.item_delete);
			holder.deleteButton.setOnClickListener(mDelOnclickImpl);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.position = position;
		holder.deleteButton.setTag(holder);
		holder.scrollView.scrollTo(0, 0);
		return convertView;
	}

	static class ViewHolder {
		private HorizontalScrollView scrollView;
		private LinearLayout linearLayout;
		private Button deleteButton;
		private int position;
	}

	/** HorizontalScrollView的滑动事件 */
	private class ScrollViewScrollImpl implements OnTouchListener {
		/** 记录开始时的坐标 */
		private float startX = 0;

		@SuppressLint("ClickableViewAccessibility")
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				// 如果有划出删除按钮的itemView,就让他滑回去并且锁定本次touch操作,解锁会在父组件的dispatchTouchEvent中进行
				if (mScrollView != null) {
					scrollView(mScrollView, HorizontalScrollView.FOCUS_LEFT);
					mScrollView = null;
					mLockOnTouch = true;
					return true;
				}
				startX = event.getX();
				break;
			case MotionEvent.ACTION_UP:
				HorizontalScrollView view = (HorizontalScrollView) v;
				// 如果滑动了>50个像素,就显示出删除按钮
				if (startX > event.getX() + 50) {
					startX = 0;// 因为公用一个事件处理对象,防止错乱,还原startX值
					scrollView(view, HorizontalScrollView.FOCUS_RIGHT);
					mScrollView = view;
				} else {
					scrollView(view, HorizontalScrollView.FOCUS_LEFT);
				}
				break;
			}
			return false;
		}
	}

	/** HorizontalScrollView左右滑动 */
	public void scrollView(final HorizontalScrollView view, final int parameter) {
		view.post(new Runnable() {
			@Override
			public void run() {
				view.pageScroll(parameter);
			}
		});
	}

	/** 删除事件 */
	private class DeleteButtonOnclickImpl implements OnClickListener {
		@Override
		public void onClick(View v) {
			final ViewHolder holder = (ViewHolder) v.getTag();
			if (v.getId() == R.id.item_delete) {
				Animation animation = AnimationUtils.loadAnimation(context, R.anim.anim_item_delete);
				holder.scrollView.startAnimation(animation);
				animation.setAnimationListener(new AnimationListener() {
					@Override
					public void onAnimationStart(Animation animation) {
					}

					@Override
					public void onAnimationRepeat(Animation animation) {
					}

					@Override
					public void onAnimationEnd(Animation animation) {
						list.remove(getItem(holder.position));
						notifyDataSetChanged();
					}
				});
			} else {

			}

		}
	}

	@Override
	public int getCount() {
		if (list == null) {
			return 0;
		}
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}
}
