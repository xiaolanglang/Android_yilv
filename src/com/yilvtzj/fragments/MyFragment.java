package com.yilvtzj.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.yilvtzj.R;
import com.yilvtzj.webview.JsInterface;
import com.yilvtzj.webview.MyWebViewClient;

public abstract class MyFragment extends Fragment {
	protected WebView webView;
	protected boolean isload = true;
	protected JsInterface jsInterface = new JsInterface();
	protected SwipeRefreshLayout swipeLayout;

	public void setIsload(boolean isload) {
		this.isload = isload;
	}

	public JsInterface getJsInterface() {
		return jsInterface;
	}

	public void setJsInterface(JsInterface jsInterface) {
		this.jsInterface = jsInterface;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return createRefresh(inflater, container);
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
		View view = inflater.inflate(R.layout.fragment_web, container, false);

		swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
		swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				// 重新刷新页面
				webView.reload();
			}
		});
		swipeLayout.setColorScheme(R.color.holo_blue_bright, R.color.holo_green_light, R.color.holo_orange_light,
				R.color.holo_red_light);

		webView = (WebView) view.findViewById(R.id.webView);

		// 设置进度条
		webView.setWebChromeClient(new MyWebChromeClient());

		return view;

	}

	@Override
	public void onStart() {
		super.onStart();
		initWebView();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		webView.destroy();
	}

	private void initWebView() {
		if (!isload) {
			return;
		}

		webView = (WebView) getView().findViewById(R.id.webView);
		webView.setWebViewClient(new MyWebViewClient(getActivity(), false, false, webView, jsInterface, null));
		hookOnStart();

		isload = false;

	}

	protected abstract void hookOnStart();

	private class MyWebChromeClient extends WebChromeClient {
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
	}
}
