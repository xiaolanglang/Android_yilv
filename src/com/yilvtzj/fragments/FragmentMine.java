package com.yilvtzj.fragments;

import android.webkit.WebView;

import com.yilvtzj.R;
import com.yilvtzj.activity.LoginActivity;
import com.yilvtzj.webview.MyWebViewClient;

public class FragmentMine extends MyFragment {

	@Override
	public void onStart() {
		super.onStart();

		if (!isload) {
			return;
		}

		webView = (WebView) getView().findViewById(R.id.webView);
		webView.loadUrl("file:///android_asset/page/mine/index.html");
		webView.setWebViewClient(new MyWebViewClient(getActivity(), webView, jsInterface, LoginActivity.class.getName()));

		isload = false;
	}

}
