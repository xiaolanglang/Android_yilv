package com.yilvtzj.activity;

import android.os.Bundle;

import com.yilvtzj.activity.common.WebActivity;
import com.yilvtzj.util.AccountUtil;
import com.yilvtzj.webview.JsInterface.JsInterfaceMethod;
import com.yilvtzj.webview.MyWebViewClient;

public class LoginActivity extends WebActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		initWebView();
	}

	protected void initWebView() {
		super.initWebView();
		webView.setWebViewClient(new MyWebViewClient(this, false, false, webView, jsInterface, null));
		webView.loadUrl("file:///android_asset/page/mine/login.html");
		webView.getSettings().setAllowUniversalAccessFromFileURLs(true);
		jsInterface.setJsInterfaceListener(new JsInterfaceMethod() {

			@Override
			public void runMethod(String... params) {
				String cookie = params[0];
				AccountUtil.setCookie(cookie);
			}
		});
	}

}
