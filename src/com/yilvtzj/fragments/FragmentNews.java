package com.yilvtzj.fragments;

import android.annotation.SuppressLint;
import android.webkit.WebView;

import com.yilvtzj.R;
import com.yilvtzj.util.Global;
import com.yilvtzj.webview.MyWebViewClient;

@SuppressLint("SetJavaScriptEnabled")
public class FragmentNews extends MyFragment {

	@Override
	public void onStart() {
		super.onStart();

		if (!isload) {
			return;
		}

		webView = (WebView) getView().findViewById(R.id.webView);
		webView.loadUrl(Global.getServletUrl("/travel/news"));
		webView.setWebViewClient(new MyWebViewClient(getActivity(), webView, jsInterface));

		isload = false;
	}

}
