package com.yilvtzj.activity.fragment;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.mining.app.zxing.activity.MipcaActivityCapture;
import com.yilvtzj.R;
import com.yilvtzj.adapter.home.HomeAdapter;
import com.yilvtzj.entity.DongtaiMsg;
import com.yilvtzj.http.SocketHttpRequester.SocketListener;
import com.yilvtzj.service.DongTaiService;
import com.yilvtzj.util.JSONHelper;
import com.yilvtzj.util.SharedPreferencesUtil;
import com.yilvtzj.util.ToastUtil;
import com.yilvtzj.view.MySwipeRefreshLayout;
import com.yilvtzj.view.MySwipeRefreshLayout.OnLoadListener;

public class FragmentIndex extends Fragment implements OnRefreshListener, OnLoadListener, OnClickListener {

	private MySwipeRefreshLayout myRefreshListView;
	private ListView listView;
	private ImageView scanIV;
	private List<DongtaiMsg> list;
	private HomeAdapter homeAdapter;
	private final static int SCANNIN_GREQUEST_CODE = 1;
	private final static int GETDATA_SUCCESS = 1;
	private final static int GETDATA_FAILED = 2;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_index, container, false);

		loadLocalData();
		init(view);

		return view;
	}

	private void loadLocalData() {
		String JSONArray = SharedPreferencesUtil.get(SharedPreferencesUtil.HOMEPAGE, "pageList", "");

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
				new Thread(new GetListThread()).start();
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
		case R.id.scan:
			MipcaActivityCapture.startScan(getActivity(), SCANNIN_GREQUEST_CODE);
			break;
		}
	}

	//
	public void init(View view) {
		listView = (ListView) view.findViewById(R.id.home);
		myRefreshListView = (MySwipeRefreshLayout) view.findViewById(R.id.swipe_container);
		scanIV = (ImageView) view.findViewById(R.id.scan);

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

		scanIV.setOnClickListener(this);

	}

	class GetListThread implements Runnable {

		@Override
		public void run() {
			try {
				DongTaiService.getDongtaiList(getActivity(), new SocketListener() {

					@Override
					public void result(String JSON) {
						try {
							JSONObject jsonObject = new JSONObject(JSON);
							JSONArray jsonArray = jsonObject.getJSONArray("list");

							list.clear();
							list = JSONHelper.JSONArrayToBeans(jsonArray, DongtaiMsg.class);
							SharedPreferencesUtil.put(SharedPreferencesUtil.HOMEPAGE, "pageList", jsonArray.toString());
							handler.sendEmptyMessage(GETDATA_SUCCESS);
						} catch (JSONException e) {
							handler.sendEmptyMessage(GETDATA_FAILED);
							e.printStackTrace();
						}

					}

				});
			} catch (Exception e) {
				handler.sendEmptyMessage(GETDATA_FAILED);
				e.printStackTrace();
			}

		}

	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case GETDATA_SUCCESS:
				if (list.size() > 0) {
					homeAdapter.setList(list);
					homeAdapter.notifyDataSetChanged();
					// 更新完后调用该方法结束刷新
					myRefreshListView.setRefreshing(false);
				}
				break;
			case GETDATA_FAILED:
				myRefreshListView.setRefreshing(false);
				ToastUtil.show(getActivity(), "获取数据失败,稍后再试", null);
				break;
			}
		};
	};

}
