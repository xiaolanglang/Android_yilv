package com.itravel.fragments;

import android.annotation.SuppressLint;
import android.webkit.WebView;

import com.itravel.util.Global;
import com.itravel.webview.MyWebViewClient;
import com.yilvtzj.R;

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
