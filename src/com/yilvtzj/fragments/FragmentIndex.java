package com.yilvtzj.fragments;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.yilvtzj.R;
import com.yilvtzj.adapter.home.HomeAdapter;
import com.yilvtzj.pojo.DongTai;
import com.yilvtzj.view.MySwipeRefreshLayout;
import com.yilvtzj.view.MySwipeRefreshLayout.OnLoadListener;

public class FragmentIndex extends Fragment implements OnRefreshListener, OnLoadListener {

	private MySwipeRefreshLayout myRefreshListView;
	private ListView listView;
	private List<DongTai> list = new ArrayList<>();
	private HomeAdapter homeAdapter;
	private String str = "广播接收者和activity间如何传递消息，我想显示到activity上：通过接口，但是具体的流程和方式还是不懂。"
			+ "activity和服务之间如何传递消息,我想显示到activity上.activity之间又是怎么传递数据ude.fragment和activity之间的。"
			+ "交互也是一个问题，这里都涉及了回调，待再继续的深入的学习";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_home, container, false);
		init(view);
		myRefreshListView.post(new Thread(new Runnable() {

			@Override
			public void run() {
				myRefreshListView.setRefreshing(true);
			}
		}));
		onRefresh();

		return view;
	}

	// 设置下拉刷新监听器
	@Override
	public void onRefresh() {
		Toast.makeText(FragmentIndex.this.getActivity(), "refresh", Toast.LENGTH_SHORT).show();

		myRefreshListView.postDelayed(new Runnable() {

			@Override
			public void run() {
				// 更新数据
				list.clear();
				getData();
				homeAdapter.notifyDataSetChanged();
				// 更新完后调用该方法结束刷新
				myRefreshListView.setRefreshing(false);
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

	public void init(View view) {
		listView = (ListView) view.findViewById(R.id.home);
		myRefreshListView = (MySwipeRefreshLayout) view.findViewById(R.id.swipe_container);

		View view2 = LayoutInflater.from(this.getActivity()).inflate(R.layout.listview_footer, null);
		view2.setVisibility(View.GONE);
		// 这边如果不设置一下，后面再加载的时候是不会显示的
		listView.addHeaderView(view2);
		// 构造适配器
		homeAdapter = new HomeAdapter(getActivity(), list);
		listView.setAdapter(homeAdapter);
		listView.removeHeaderView(view2);

		myRefreshListView.setColorScheme(R.color.holo_blue_bright, R.color.holo_green_light, R.color.holo_orange_light,
				R.color.holo_red_light);
		myRefreshListView.setOnRefreshListener(this);
		myRefreshListView.setOnLoadListener(this);

	}

	private void getData() {
		list.add(new DongTai("天心月圆", str));
		list.add(new DongTai("水月洞天", str));
		list.add(new DongTai("四海为家", str));
		list.add(new DongTai("冰心玉壶", str));
	}

}
