package com.itravel.fragments;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.itravel.util.Global;
import com.itravel.webview.MyWebViewClient;
import com.yilvtzj.R;

public class FragmentIndex extends MyFragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return createRefresh(inflater, container);
	}

	@Override
	public void onStart() {
		super.onStart();
		initWebView();
	}

	/**
	 * 创建下拉刷新
	 * 
	 * @param inflater
	 * @param container
	 * @return
	 */
	@SuppressWarnings("deprecation")
	private View createRefresh(LayoutInflater inflater, ViewGroup container) {
		final SwipeRefreshLayout swipeLayout;
		View view = inflater.inflate(R.layout.fragment_index, container, false);

		swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
		swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				// 重新刷新页面
				webView.loadUrl(webView.getUrl());
			}
		});
		swipeLayout.setColorScheme(R.color.holo_blue_bright, R.color.holo_green_light,
				R.color.holo_orange_light, R.color.holo_red_light);

		webView = (WebView) view.findViewById(R.id.webView);

		// 设置进度条
		webView.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				if (newProgress == 100) {
					// 隐藏进度条
					swipeLayout.setRefreshing(false);
				} else {
					if (!swipeLayout.isRefreshing())
						swipeLayout.setRefreshing(true);
				}

				super.onProgressChanged(view, newProgress);
			}
		});

		return view;

	}

	/**
	 * 初始化webView
	 */
	private void initWebView() {
		if (!isload) {
			return;
		}
		webView = (WebView) getView().findViewById(R.id.webView);
		webView.setWebViewClient(new MyWebViewClient(false, getActivity(), webView, jsInterface));
		webView.loadUrl(Global.getServletUrl("/"));
		isload = false;
	}

}
