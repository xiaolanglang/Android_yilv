package com.yilvtzj.fragments;

import android.webkit.WebView;

import com.yilvtzj.R;
import com.yilvtzj.util.Global;
import com.yilvtzj.webview.MyWebViewClient;

public class FragmentZiXun extends MyFragment {

	@Override
	public void onStart() {
		super.onStart();

		if (!isload) {
			return;
		}

		webView = (WebView) getView().findViewById(R.id.webView);
		webView.loadUrl(Global.getServletUrl("/travel/zixun"));
		webView.setWebViewClient(new MyWebViewClient(getActivity(), webView, jsInterface));

		isload = false;
	}

}
