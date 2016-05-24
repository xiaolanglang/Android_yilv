package com.yilvtzj.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import com.common.util.ActivityUtil;
import com.common.util.DateUtil;
import com.common.util.StringUtil;
import com.common.util.ToastUtil;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.yilvtzj.R;
import com.yilvtzj.activity.common.BaseActivity;
import com.yilvtzj.activity.common.FullPageImageViewActivity;
import com.yilvtzj.adapter.ListAdapter;
import com.yilvtzj.adapter.NetWorkGridViewAdapter;
import com.yilvtzj.entity.DataResult;
import com.yilvtzj.entity.DongtaiComment;
import com.yilvtzj.entity.DongtaiMsg;
import com.yilvtzj.service.IDongTaiService;
import com.yilvtzj.service.ServiceListener;
import com.yilvtzj.service.impl.DongTaiService;

public class CommentActivity extends BaseActivity implements OnClickListener {

	private DongtaiMsg msg;
	private TextView name, time, content;
	private FrameLayout commentF;
	private GridView gridView;
	private Activity mActivity;
	private ListView listView;
	private ListAdapter listAdapter;
	private NetWorkGridViewAdapter wallAdapter;
	private PullToRefreshScrollView mPullRefreshScrollView;
	private List<DongtaiComment> list = new ArrayList<>();

	private IDongTaiService commentService = DongTaiService.newInstance();
	private int pageNum = 1;
	private boolean isLoading = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dongtai_comment);
		setCommonActionBar("动态正文");
		mActivity = this;

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
					getList();
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

		wallAdapter = new NetWorkGridViewAdapter(this.getBaseContext(), StringUtil.toStrings(msg.getImageUrls()));
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
		showLoadingDialog();
		getList();
	}

	private void getList() {
		Map<String, Object> params = new HashMap<>();
		params.put("pageNum", pageNum);
		params.put("dongtaiId", msg.getId());
		commentService.getList(postThreadListener, params);
	}

	private ServiceListener<DataResult<DongtaiComment>> postThreadListener = new ServiceListener<DataResult<DongtaiComment>>() {

		@Override
		public void preExecute() {

		}

		@Override
		public void onSuccess(DataResult<DongtaiComment> result) {
			List<DongtaiComment> listTemp = result.getList();
			if (listTemp != null && listTemp.size() > 0) {
				for (DongtaiComment comment : listTemp) {
					list.add(comment);
				}
				pageNum += 1;
				listAdapter.notifyDataSetChanged();
			} else {
				ToastUtil.show(CommentActivity.this, "暂无更多数据", null);
			}
		}

		@Override
		public void onFailed(int code, String message) {
			ToastUtil.show(CommentActivity.this, "获取数据失败", null);
		}

		@Override
		public void onFinally() {
			mPullRefreshScrollView.onRefreshComplete();
			isLoading = false;
			cancelLoadingDialog();
		}

	};

}
