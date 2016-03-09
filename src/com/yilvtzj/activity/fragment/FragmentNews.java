package com.yilvtzj.activity.fragment;

import java.util.ArrayList;
import java.util.List;

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
import com.yilvtzj.adapter.news.HorizontalSlideAdapter;
import com.yilvtzj.entity.MessageItem;

public class FragmentNews extends Fragment implements OnRefreshListener {

	private SwipeRefreshLayout swipeRefreshLayout;
	private ListView listV;
	private HorizontalSlideAdapter adapter;
	private List<MessageItem> list = new ArrayList<MessageItem>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_news, container, false);
		initView(view);
		initSwipeRefresh(view);

		return view;
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	@SuppressWarnings("deprecation")
	private void initSwipeRefresh(View view) {
		swipeRefreshLayout.setOnRefreshListener(this);
		swipeRefreshLayout.setColorScheme(R.color.holo_blue_bright, R.color.holo_green_light, R.color.holo_orange_light,
				R.color.holo_red_light);
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
		Toast.makeText(FragmentNews.this.getActivity(), "refresh", Toast.LENGTH_SHORT).show();

		swipeRefreshLayout.postDelayed(new Runnable() {

			@Override
			public void run() {
				list.clear();
				for (int i = 0; i < 10; i++) {
					list.add(new MessageItem());
				}
				adapter.notifyDataSetChanged();
				swipeRefreshLayout.setRefreshing(false);
			}
		}, 1000);

	}

	private void initView(View view) {
		swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
		listV = (ListView) view.findViewById(R.id.list);

		adapter = new HorizontalSlideAdapter(this.getActivity(), list);
		listV.setAdapter(adapter);
	}
}
