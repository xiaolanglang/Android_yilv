package com.yilvtzj.activity;

import android.os.Bundle;

import com.yilvtzj.activity.common.MyActivity;
import com.yilvtzj.util.AccountUtil;
import com.yilvtzj.webview.JsInterface.JsInterfaceMethod;

public class LoginActivity extends MyActivity {

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
		webView.loadUrl("file:///android_asset/page/mine/login.html");
		webView.getSettings().setAllowUniversalAccessFromFileURLs(true);
		jsInterface.setJsInterfaceListener(new JsInterfaceMethod() {

			@Override
			public void runMethod(String... params) {
				String cookie = params[0];
				AccountUtil.setCookie(LoginActivity.this, cookie);
			}
		});
	}
}
