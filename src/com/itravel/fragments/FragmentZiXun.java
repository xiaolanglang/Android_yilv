package com.itravel.fragments;

import android.webkit.WebView;

import com.itravel.util.Global;
import com.itravel.webview.MyWebViewClient;
import com.yilvtzj.R;

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
