package com.yilvtzj.activity.fragment;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.yilvtzj.R;
import com.yilvtzj.adapter.home.HomeAdapter;
import com.yilvtzj.entity.DongtaiMsg;
import com.yilvtzj.http.PostThread.PostThreadListener;
import com.yilvtzj.service.DongTaiService;
import com.yilvtzj.util.JSONHelper;
import com.yilvtzj.util.SharePreferenceUtil;
import com.yilvtzj.util.ToastUtil;
import com.yilvtzj.view.MySwipeRefreshLayout;
import com.yilvtzj.view.MySwipeRefreshLayout.OnLoadListener;
import com.yilvtzj.view.fragmentindex.AddPopWindow;

public class FragmentIndex extends Fragment implements OnRefreshListener, OnLoadListener, OnClickListener {

	private MySwipeRefreshLayout myRefreshListView;
	private ListView listView;
	private ImageView addTv;
	private List<DongtaiMsg> list;
	private HomeAdapter homeAdapter;
	private final static int GETDATA_SUCCESS = 1;
	private final static int GETDATA_FAILED = 2;
	private AddPopWindow addPopWindow;

	private DongTaiService dongTaiService = DongTaiService.newInstance();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_index, container, false);

		addPopWindow = new AddPopWindow(this.getActivity());

		loadLocalData();
		init(view);

		return view;
	}

	private void loadLocalData() {
		String JSONArray = SharePreferenceUtil.get(SharePreferenceUtil.HOMEPAGE, "pageList", "");

		try {
			list = JSONHelper.JSONArrayToBeans(new JSONArray(JSONArray), DongtaiMsg.class);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		if (list == null) {
			list = new ArrayList<>();
		}
	}

	// 设置下拉刷新监听器
	@Override
	public void onRefresh() {
		Toast.makeText(FragmentIndex.this.getActivity(), "refresh", Toast.LENGTH_SHORT).show();

		myRefreshListView.postDelayed(new Runnable() {

			@Override
			public void run() {
				// 更新数据
				dongTaiService.getDongtaiList(postThreadListener);
			}
		}, 1000);

	}

	// 设置上拉加载监听器
	@Override
	public void onLoad() {
		Toast.makeText(FragmentIndex.this.getActivity(), "load", Toast.LENGTH_SHORT).show();

		myRefreshListView.postDelayed(new Runnable() {

			@Override
			public void run() {
				myRefreshListView.setLoading(false);
				// getData();
				homeAdapter.notifyDataSetChanged();
			}
		}, 1500);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.add:
			addPopWindow.showPopupWindow(addTv, FragmentIndex.this.getActivity());
			break;
		}
	}

	//
	public void init(View view) {
		listView = (ListView) view.findViewById(R.id.home);
		myRefreshListView = (MySwipeRefreshLayout) view.findViewById(R.id.swipe_container);
		addTv = (ImageView) view.findViewById(R.id.add);

		View view2 = LayoutInflater.from(this.getActivity()).inflate(R.layout.item_home_listview_footer, null);
		view2.setVisibility(View.GONE);
		// 这边如果不设置一下，后面再加载的时候是不会显示的
		listView.addHeaderView(view2);
		// 构造适配器
		homeAdapter = new HomeAdapter(getActivity(), list);
		listView.setAdapter(homeAdapter);
		listView.removeHeaderView(view2);

		// 构建下拉刷新
		myRefreshListView.setColorScheme(R.color.holo_blue_bright, R.color.holo_green_light, R.color.holo_orange_light,
				R.color.holo_red_light);
		myRefreshListView.setOnRefreshListener(this);
		myRefreshListView.setOnLoadListener(this);

		addTv.setOnClickListener(this);

	}

	private PostThreadListener postThreadListener = new PostThreadListener() {

		@Override
		public boolean postThreadSuccess(JSONObject JSON) throws JSONException {
			JSONArray jsonArray = JSON.getJSONArray("list");
			list = JSONHelper.JSONArrayToBeans(jsonArray, DongtaiMsg.class);
			if (list != null || list.size() > 0) {
				homeAdapter.setList(list);
				homeAdapter.notifyDataSetChanged();
				// 将得到的数据保存起来
				SharePreferenceUtil.put(SharePreferenceUtil.HOMEPAGE, "pageList", jsonArray.toString());
			}
			return false;
		}

		@Override
		public boolean postThreadFinally() {
			myRefreshListView.setRefreshing(false);
			return false;
		}

		@Override
		public boolean postThreadFailed() {
			ToastUtil.show(getActivity(), "获取数据失败,稍后再试", null);
			return false;
		}
	};

}
