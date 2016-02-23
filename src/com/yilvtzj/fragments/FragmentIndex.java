package com.yilvtzj.fragments;

public class FragmentIndex extends MyFragment {

	@Override
	protected void hookOnStart() {
		webView.loadUrl("file:///android_asset/page/dongtai/index.html");
		webView.getSettings().setAllowUniversalAccessFromFileURLs(true);
	}
}
