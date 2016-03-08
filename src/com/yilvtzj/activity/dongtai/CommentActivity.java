package com.yilvtzj.activity.dongtai;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.yilvtzj.R;
import com.yilvtzj.activity.FullPageImageViewActivity;
import com.yilvtzj.activity.common.MyActivity;
import com.yilvtzj.adapter.dongtaicomment.ListAdapter;
import com.yilvtzj.adapter.home.GridViewAdapter;
import com.yilvtzj.entity.DongtaiComment;
import com.yilvtzj.entity.DongtaiMsg;
import com.yilvtzj.http.SocketHttpRequester.SocketListener;
import com.yilvtzj.service.DongTaiCommentService;
import com.yilvtzj.util.ActivityUtil;
import com.yilvtzj.util.DateUtil;
import com.yilvtzj.util.JSONHelper;
import com.yilvtzj.util.SimpleHandler;
import com.yilvtzj.util.SimpleHandler.RunMethod;
import com.yilvtzj.util.StringUtil;

public class CommentActivity extends MyActivity implements OnClickListener {

	private DongtaiMsg msg;
	private TextView name, time, content;
	private FrameLayout commentF;
	private GridView gridView;
	private Activity mActivity;
	private ListView listView;
	private ListAdapter listAdapter;
	private GridViewAdapter wallAdapter;
	private PullToRefreshScrollView mPullRefreshScrollView;
	private List<DongtaiComment> list = new ArrayList<>();
	private SimpleHandler handler;
	private int pageNum = 1;
	private boolean isLoading = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dongtai_comment);
		setCommonActionBar("动态正文");
		mActivity = this;
		handler = new SimpleHandler(this);

		Intent intent = getIntent();
		msg = (DongtaiMsg) intent.getExtras().getSerializable("msg");

		initView();
		setValue();

		mPullRefreshScrollView = (PullToRefreshScrollView) findViewById(R.id.swipe_container);
		mPullRefreshScrollView.setMode(Mode.PULL_FROM_END);
		mPullRefreshScrollView.setOnRefreshListener(new PullToRefreshScrollView.OnRefreshListener<ScrollView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
				if (!isLoading) {
					new Thread(new GetListThread()).start();
				} else {
					mPullRefreshScrollView.onRefreshComplete();
				}
			}
		});

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.comment:
			Intent intent = new Intent();
			intent.putExtra("dongTaiId", msg.getId());
			ActivityUtil.startActivity(intent, CommentActivity.this, CommentWriteActivity.class);
			break;

		}

	}

	private void initView() {
		name = (TextView) findViewById(R.id.name);
		time = (TextView) findViewById(R.id.time);
		content = (TextView) findViewById(R.id.content);
		gridView = (GridView) findViewById(R.id.gridview);
		listView = (ListView) findViewById(R.id.list);
		commentF = (FrameLayout) findViewById(R.id.comment);

		commentF.setOnClickListener(this);
	}

	private void setValue() {
		name.setText(msg.getAccount().getNickname());
		time.setText(DateUtil.rangeTime(msg.getCreateTime(), "yyyy-MM-dd hh:mm"));
		content.setText(msg.getContent());

		wallAdapter = new GridViewAdapter(this.getBaseContext(), StringUtil.toStrings(msg.getImageUrls()));
		gridView.post(new Runnable() {

			@Override
			public void run() {
				int columnWidth = (gridView.getWidth() / 3) - 4 * 2;
				wallAdapter.setItemHeight(columnWidth);

			}
		});
		gridView.setAdapter(wallAdapter);
		gridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				Intent intent = new Intent(mActivity.getBaseContext(), FullPageImageViewActivity.class);
				intent.putExtra("pictures", StringUtil.toStrings(msg.getImageUrls()));
				intent.putExtra("selectId", position);
				mActivity.startActivity(intent);
			}
		});

		listAdapter = new ListAdapter(mActivity, list);
		listView.setAdapter(listAdapter);
		new Thread(new GetListThread()).start();
	}

	private class GetListThread implements Runnable {

		private void error() {
			mPullRefreshScrollView.onRefreshComplete();
			handler.sendMessage("获取数据失败");
			isLoading = false;
		}

		private void setList(String JSON) throws JSONException {
			JSONObject object = new JSONObject(JSON);
			JSONArray array = object.getJSONArray("list");
			final List<DongtaiComment> listTemp = JSONHelper.JSONArrayToBeans(array, DongtaiComment.class);
			handler.runMethod(new RunMethod() {

				@Override
				public void run() {
					if (listTemp != null && listTemp.size() > 0) {
						for (DongtaiComment comment : listTemp) {
							list.add(comment);
						}
						pageNum += 1;
						listAdapter.notifyDataSetChanged();
					} else {
						handler.sendMessage("暂无更多数据");
					}

					isLoading = false;
					mPullRefreshScrollView.onRefreshComplete();
				}
			});
		}

		@Override
		public void run() {
			try {
				isLoading = true;
				DongTaiCommentService.getList(new SocketListener() {

					@Override
					public void result(String JSON) {
						try {
							setList(JSON);
						} catch (JSONException e) {
							error();
							e.printStackTrace();
						}
					}
				}, mActivity, pageNum, msg.getId());
			} catch (Exception e) {
				error();
				e.printStackTrace();
			}

		}
	}
}
