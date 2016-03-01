package com.yilvtzj.activity;

import android.content.Intent;
import android.os.Bundle;

import com.yilvtzj.activity.common.WebActivity;
import com.yilvtzj.webview.MyWebChromeClient;
import com.yilvtzj.webview.MyWebViewClient;

public class WebViewActivity extends WebActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		initWebView();
	}

	/**
	 * 初始化webview
	 */
	protected void initWebView() {
		super.initWebView();

		Intent intent = getIntent();
		webView.setWebViewClient(new MyWebViewClient(this, false, true, webView, jsInterface, null));
		webView.setWebChromeClient(new MyWebChromeClient(this));
		webView.loadUrl(intent.getStringExtra("url"));
	}
}
