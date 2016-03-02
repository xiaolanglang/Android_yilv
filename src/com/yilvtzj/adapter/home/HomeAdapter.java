package com.yilvtzj.adapter.home;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nineoldandroids.animation.ObjectAnimator;
import com.yilvtzj.R;
import com.yilvtzj.activity.FullPageImageViewActivity;
import com.yilvtzj.activity.dongtai.DongTaiCommentActivity;
import com.yilvtzj.http.SocketHttpRequester.SocketListener;
import com.yilvtzj.pojo.DongtaiMsg;
import com.yilvtzj.service.DongTaiGoodService;
import com.yilvtzj.util.ActivityUtil;
import com.yilvtzj.util.DateUtil;
import com.yilvtzj.util.StringUtil;
import com.yilvtzj.util.ToastUtil;
import com.yilvtzj.view.NoScrolGridView;

public class HomeAdapter extends BaseAdapter {
	private static Context context;
	private List<DongtaiMsg> list;
	private static final int GOODSECESS = 0;
	private static final int GOODFAILED = 1;

	public HomeAdapter(Context context, List<DongtaiMsg> list) {
		HomeAdapter.context = context;
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int i) {
		return list.get(i);
	}

	@Override
	public long getItemId(int i) {
		return i;
	}

	@Override
	public View getView(int i, View view, ViewGroup viewGroup) {
		ViewHolder viewHolder = null;
		if (view == null) {
			view = LayoutInflater.from(context).inflate(R.layout.item_home, null);
			viewHolder = new ViewHolder();
			viewHolder.name = (TextView) view.findViewById(R.id.name);
			viewHolder.content = (TextView) view.findViewById(R.id.content);
			viewHolder.attitudesCountLay = (LinearLayout) view.findViewById(R.id.attitudesCountLay);
			viewHolder.attitudesCount = (TextView) view.findViewById(R.id.attitudesCount);
			viewHolder.commentCountLay = (LinearLayout) view.findViewById(R.id.commentCountLay);
			viewHolder.commentCount = (TextView) view.findViewById(R.id.commentCount);
			viewHolder.time = (TextView) view.findViewById(R.id.time);
			viewHolder.gridView = (NoScrolGridView) view.findViewById(R.id.gridview);
			view.setTag(viewHolder);
		} else {
			return view;
		}

		final DongtaiMsg dongtaiMsg = list.get(i);
		viewHolder.name.setText(dongtaiMsg.getAccount().getNickname());
		viewHolder.content.setText(dongtaiMsg.getContent());
		viewHolder.attitudesCount.setText(String.valueOf(dongtaiMsg.getAttitudesCount()));
		viewHolder.attitudesCount.setTag(dongtaiMsg.getId());
		viewHolder.commentCount.setText(String.valueOf(dongtaiMsg.getCommentCount()));
		viewHolder.time.setText(DateUtil.rangeTime(dongtaiMsg.getCreateTime(), "yyyy-MM-dd hh:mm"));

		final GridViewAdapter wallAdapter = new GridViewAdapter(context, StringUtil.toStrings(dongtaiMsg.getImageUrls()));
		final ViewHolder viewHolder2 = viewHolder;
		viewHolder.gridView.post(new Runnable() {

			@Override
			public void run() {
				int columnWidth = (viewHolder2.gridView.getWidth() / 3) - 4 * 2;
				wallAdapter.setItemHeight(columnWidth);

			}
		});
		viewHolder.gridView.setAdapter(wallAdapter);
		viewHolder.gridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				FullPageImageViewActivity.actionStart(context, StringUtil.toStrings(dongtaiMsg.getImageUrls()), position);
			}
		});

		viewHolder.attitudesCountLay.setOnClickListener(new Animate(view));
		if ("1".equals(dongtaiMsg.getIsGood())) {
			good(view, false);
		} else {
			notGood(view, false);
		}

		viewHolder.content.setOnClickListener(new CommentListener(dongtaiMsg));
		viewHolder.commentCountLay.setOnClickListener(new CommentListener(dongtaiMsg));

		return view;
	}

	private class ViewHolder {
		private TextView name, content, time, attitudesCount, commentCount;
		private NoScrolGridView gridView;
		private LinearLayout attitudesCountLay, commentCountLay;
	}

	/**
	 * @param view
	 * @param change
	 *            是否向后台发起请求
	 */
	private static void good(View view, boolean change) {
		ViewHolder viewHolder = (ViewHolder) view.getTag();
		viewHolder.attitudesCount.setTextColor(view.getResources().getColor(R.color.like_on));
		viewHolder.attitudesCountLay.setSelected(true);
		if (change) {
			changeCount(viewHolder.attitudesCount, view);
		}
	}

	/**
	 * @param view
	 * @param change
	 *            是否向后台发起请求
	 */
	private static void notGood(View view, boolean change) {
		ViewHolder viewHolder = (ViewHolder) view.getTag();
		viewHolder.attitudesCount.setTextColor(view.getResources().getColor(R.color.ccc));
		viewHolder.attitudesCountLay.setSelected(false);
		if (change) {
			changeCount(viewHolder.attitudesCount, view);
		}
	}

	private static Handler handler = new Handler() {
		private Toast toast;

		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case GOODSECESS:
				toast = ToastUtil.show(context, "点赞成功", toast);
				break;
			case GOODFAILED:
				toast = ToastUtil.show(context, "点赞失败", toast);
				break;

			}
		}
	};

	private static void changeCount(TextView attitudesCount, final View view) {
		String count = attitudesCount.getText().toString();
		int count2 = 0;
		if (attitudesCount.isSelected()) {
			count2 = Integer.parseInt(count) + 1;
		} else {
			count2 = Integer.parseInt(count) - 1;
		}
		attitudesCount.setText(String.valueOf(count2));
		final Map<String, String> params = new HashMap<>();
		params.put("id", attitudesCount.getTag().toString());
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					DongTaiGoodService.saveGood(new SocketListener() {

						@Override
						public void result(String JSON) {
							if (JSON.indexOf("200") != -1) {
								handler.sendEmptyMessage(GOODSECESS);
							} else {
								handler.sendEmptyMessage(GOODFAILED);
								notGood(view, true);
							}
						}

					}, context, params);
				} catch (Exception e) {
					handler.sendEmptyMessage(GOODFAILED);
					notGood(view, true);
					e.printStackTrace();
				}

			}
		}).start();
	}

	private class Animate implements OnClickListener {

		private int duration = 1 * 1000;
		private ObjectAnimator animatorX;
		private ObjectAnimator animatorY;
		private ViewHolder viewHolder;
		private View view;

		public Animate(View view) {
			this.view = view;
			viewHolder = (ViewHolder) view.getTag();
			animatorX = ObjectAnimator.ofFloat(viewHolder.attitudesCountLay, "scaleX", 1, 2, 1).setDuration(duration);
			animatorY = ObjectAnimator.ofFloat(viewHolder.attitudesCountLay, "scaleY", 1, 2, 1).setDuration(duration);
		}

		@Override
		public void onClick(View v) {
			if (!animatorX.isRunning() && !animatorY.isRunning()) {
				if (viewHolder.attitudesCount.isSelected()) {
					notGood(view, true);
				} else {
					good(view, true);
					animatorX.start();
					animatorY.start();
				}
			}
		}

	}

	private class CommentListener implements OnClickListener {
		private DongtaiMsg msg;

		public CommentListener(DongtaiMsg msg) {
			this.msg = msg;
		}

		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			intent.putExtra("msg", msg);
			ActivityUtil.startActivity(intent, (Activity) context, DongTaiCommentActivity.class);
		}

	}

}
