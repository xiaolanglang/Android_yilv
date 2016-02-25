package com.yilvtzj.activity.fragment;

import com.yilvtzj.activity.LoginActivity;
import com.yilvtzj.activity.common.MyFragment;
import com.yilvtzj.util.Global;
import com.yilvtzj.webview.MyWebViewClient;

public class FragmentMine extends MyFragment {

	@Override
	protected void hookOnStart() {
		webView.loadUrl(Global.getServletUrl("/travel/mine"));
		webView.setWebViewClient(new MyWebViewClient(getActivity(), false, false, webView, jsInterface,
				LoginActivity.class.getName()));
	}

}
