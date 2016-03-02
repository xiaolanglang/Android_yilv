package com.yilvtzj.activity.dongtai;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
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
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.yilvtzj.R;
import com.yilvtzj.activity.FullPageImageViewActivity;
import com.yilvtzj.activity.common.MyActivity;
import com.yilvtzj.adapter.dongtaicomment.ListAdapter;
import com.yilvtzj.adapter.home.GridViewAdapter;
import com.yilvtzj.pojo.Account;
import com.yilvtzj.pojo.DongtaiComment;
import com.yilvtzj.pojo.DongtaiMsg;
import com.yilvtzj.util.ActivityUtil;
import com.yilvtzj.util.DateUtil;
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
	private PullToRefreshScrollView mPullRefreshScrollView;;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dongtai_comment);
		setCommonActionBar("动态正文");

		Intent intent = getIntent();
		msg = (DongtaiMsg) intent.getExtras().getSerializable("msg");
		mActivity = this;

		initView();
		setValue();

		mPullRefreshScrollView = (PullToRefreshScrollView) findViewById(R.id.swipe_container);
		mPullRefreshScrollView.setOnRefreshListener(new PullToRefreshScrollView.OnRefreshListener<ScrollView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
				new GetDataTask().execute();
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

		View view2 = LayoutInflater.from(this).inflate(R.layout.item_home_listview_footer, null);
		view2.setVisibility(View.GONE);
		// 这边如果不设置一下，后面再加载的时候是不会显示的
		listView.addHeaderView(view2);

		listView.removeHeaderView(view2);

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

		List<DongtaiComment> list = new ArrayList<>();
		DongtaiComment comment = new DongtaiComment();
		Account account = new Account();
		account.setNickname("测试人员");
		comment.setUser(account);
		comment.setCreateDate("2015-1-1 10:12");
		comment.setContent("这是一条评论");
		comment.setId("1243dadsadas");
		list.add(comment);
		list.add(comment);
		list.add(comment);
		list.add(comment);
		list.add(comment);
		listAdapter = new ListAdapter(mActivity, list);
		listView.setAdapter(listAdapter);
	}

	private class GetDataTask extends AsyncTask<Void, Void, String[]> {

		@Override
		protected String[] doInBackground(Void... params) {
			// Simulates a background job.
			try {
				Thread.sleep(4000);
			} catch (InterruptedException e) {
			}
			return null;
		}

		@Override
		protected void onPostExecute(String[] result) {
			// Do some stuff here

			// Call onRefreshComplete when the list has been refreshed.
			mPullRefreshScrollView.onRefreshComplete();

			super.onPostExecute(result);
		}
	}
}
