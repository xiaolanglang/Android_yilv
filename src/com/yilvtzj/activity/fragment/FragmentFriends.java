package com.yilvtzj.activity.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.yilvtzj.R;

public class FragmentFriends extends Fragment implements OnRefreshListener {

	private SwipeRefreshLayout swipeRefreshLayout;
	private ListView list;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_news, container, false);
		initView(view);
		initSwipeRefresh(view);

		return view;
	}

	private void initView(View view) {
		swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
		list = (ListView) view.findViewById(R.id.list);
	}

	@SuppressWarnings("deprecation")
	private void initSwipeRefresh(View view) {
		swipeRefreshLayout.setOnRefreshListener(this);
		swipeRefreshLayout.setColorScheme(R.color.holo_blue_bright, R.color.holo_green_light,
				R.color.holo_orange_light, R.color.holo_red_light);
		swipeRefreshLayout.post(new Thread(new Runnable() {

			@Override
			public void run() {
				swipeRefreshLayout.setRefreshing(true);
			}
		}));
		onRefresh();
	}

	@Override
	public void onRefresh() {
		Toast.makeText(FragmentFriends.this.getActivity(), "refresh", Toast.LENGTH_SHORT).show();

		swipeRefreshLayout.postDelayed(new Runnable() {

			@Override
			public void run() {
				swipeRefreshLayout.setRefreshing(false);
			}
		}, 1000);

	}

}
