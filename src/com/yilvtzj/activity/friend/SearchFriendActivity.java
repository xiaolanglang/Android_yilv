package com.yilvtzj.activity.friend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

import com.common.util.ActivityUtil;
import com.common.util.ToastUtil;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.yilvtzj.R;
import com.yilvtzj.activity.common.MyActivity;
import com.yilvtzj.adapter.friends.SearchFriendAdapter;
import com.yilvtzj.entity.Account;
import com.yilvtzj.entity.DataResult;
import com.yilvtzj.service.IFriendService;
import com.yilvtzj.service.ServiceListener;
import com.yilvtzj.service.impl.FriendService;
import com.yilvtzj.view.LoadingDialog;

public class SearchFriendActivity extends MyActivity implements OnClickListener, OnItemClickListener {
	private ListView listV;
	private TextView searchTv;
	private Button searchBtn;
	private SearchFriendAdapter adapter;
	private PullToRefreshScrollView mPullRefreshScrollView;
	private List<Account> list = new ArrayList<>();
	private IFriendService searchFriendService = FriendService.newInstance();
	private LoadingDialog loadingDialog;
	HashMap<String, Object> param = new HashMap<>();

	private int pageNum = 1;
	private boolean isLoading = false;
	private String searchMsg;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_friends);

		setCommonActionBar("搜索好友");
		loadingDialog = new LoadingDialog(this);

		initView();

		mPullRefreshScrollView = (PullToRefreshScrollView) findViewById(R.id.swipe_container);
		mPullRefreshScrollView.setMode(Mode.PULL_FROM_END);
		mPullRefreshScrollView.setOnRefreshListener(new PullToRefreshScrollView.OnRefreshListener<ScrollView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
				if (!isLoading) {
					param.clear();
					param.put("pageNum", pageNum);
					param.put("nickname", searchMsg);
					searchFriendService.searchFriend(param, listener);
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
			param.clear();
			param.put("pageNum", pageNum);
			param.put("nickname", searchMsg);
			searchFriendService.searchFriend(param, listener);
		}
	}

	private ServiceListener<DataResult<Account>> listener = new ServiceListener<DataResult<Account>>() {

		@Override
		public void preExecute() {
			loadingDialog.show();
		}

		@Override
		public void onSuccess(DataResult<Account> result) {
			List<Account> listTemp = result.getList();
			if (listTemp != null && listTemp.size() > 0) {
				if (pageNum == 1) {
					list.clear();
				}
				for (Account comment : listTemp) {
					list.add(comment);
				}
				pageNum += 1;
				adapter.notifyDataSetChanged();
			} else {
				ToastUtil.show(SearchFriendActivity.this, "暂无更多数据", null).show();
			}
		}

		@Override
		public void onFinally() {
			isLoading = false;
			mPullRefreshScrollView.onRefreshComplete();
			loadingDialog.cancel();
		}

		@Override
		public void onFailed(int code, String message) {
			mPullRefreshScrollView.onRefreshComplete();
			ToastUtil.show(SearchFriendActivity.this, "获取数据失败", null).show();
		}
	};

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Intent intent = new Intent();
		intent.putExtra("account", list.get(position));
		ActivityUtil.startActivity(intent, SearchFriendActivity.this, SelfInfoActivity.class);

	}
}
