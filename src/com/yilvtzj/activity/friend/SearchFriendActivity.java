package com.yilvtzj.activity.friend;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.yilvtzj.R;
import com.yilvtzj.activity.common.MyActivity;
import com.yilvtzj.adapter.friends.SearchFriendAdapter;
import com.yilvtzj.entity.Account;
import com.yilvtzj.http.SocketHttpRequester.SocketListener;
import com.yilvtzj.service.SearchFriendService;
import com.yilvtzj.util.ActivityUtil;
import com.yilvtzj.util.JSONHelper;
import com.yilvtzj.util.SimpleHandler;
import com.yilvtzj.util.SimpleHandler.RunMethod;

public class SearchFriendActivity extends MyActivity implements OnClickListener, OnItemClickListener {
	private ListView listV;
	private TextView searchTv;
	private Button searchBtn;
	private SearchFriendAdapter adapter;
	private PullToRefreshScrollView mPullRefreshScrollView;
	private List<Account> list = new ArrayList<>();
	private SimpleHandler handler;
	private int pageNum = 1;
	private boolean isLoading = false;
	private String searchMsg;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_friends);

		setCommonActionBar("搜索好友");
		handler = new SimpleHandler(this);

		initView();

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

	private void initView() {
		searchTv = (TextView) findViewById(R.id.txt_search);
		searchBtn = (Button) findViewById(R.id.search);
		listV = (ListView) findViewById(R.id.list);

		searchBtn.setOnClickListener(this);

		adapter = new SearchFriendAdapter(this, list);
		listV.setAdapter(adapter);
		listV.setOnItemClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.search:
			search();
			break;

		}

	}

	private void search() {
		if (!isLoading) {
			searchMsg = searchTv.getText().toString();
			pageNum = 1;
			new Thread(new GetListThread()).start();
		}
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
			final List<Account> listTemp = JSONHelper.JSONArrayToBeans(array, Account.class);
			handler.runMethod(new RunMethod() {

				@Override
				public void run() {
					if (listTemp != null && listTemp.size() > 0) {
						for (Account comment : listTemp) {
							list.add(comment);
						}
						pageNum += 1;
						adapter.notifyDataSetChanged();
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
				SearchFriendService.getList(new SocketListener() {

					@Override
					public void result(String JSON) {
						try {
							setList(JSON);
						} catch (JSONException e) {
							error();
							e.printStackTrace();
						}
					}
				}, pageNum, searchMsg);
			} catch (Exception e) {
				error();
				e.printStackTrace();
			}

		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Intent intent = new Intent();
		intent.putExtra("account", list.get(position));
		ActivityUtil.startActivity(intent, SearchFriendActivity.this, SelfInfoActivity.class);

	}
}
