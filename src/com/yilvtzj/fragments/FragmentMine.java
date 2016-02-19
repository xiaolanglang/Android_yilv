package com.yilvtzj.fragments;

import com.yilvtzj.activity.LoginActivity;
import com.yilvtzj.util.Global;
import com.yilvtzj.webview.MyWebViewClient;

public class FragmentMine extends MyFragment {

	@Override
	protected void hookOnStart() {
		webView.loadUrl(Global.getServletUrl("/travel/mine"));
		webView.setWebViewClient(new MyWebViewClient(false, getActivity(), webView, jsInterface, LoginActivity.class
				.getName()));
	}

}
