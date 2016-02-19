package com.yilvtzj.fragments;

import com.yilvtzj.util.Global;

public class FragmentNews extends MyFragment {

	@Override
	protected void hookOnStart() {
		webView.loadUrl(Global.getServletUrl("/travel/news"));
	}

}
