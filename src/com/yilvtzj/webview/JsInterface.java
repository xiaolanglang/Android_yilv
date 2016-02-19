package com.yilvtzj.webview;

import android.webkit.JavascriptInterface;

import com.yilvtzj.fragments.FragmentMine;
import com.yilvtzj.util.ClassManagerUtil;

public class JsInterface {
	private JsInterfaceMethod method;

	public interface JsInterfaceMethod {
		void runMethod(String... params);
	}

	public void setJsInterfaceListener(JsInterfaceMethod method) {
		this.method = method;

	}

	/**
	 * 让页面刷新
	 */
	@JavascriptInterface
	public void pageReload() {
		if (method != null) {
			method.runMethod();
		}
	}

	/**
	 * 发送动态的时候要用
	 * 
	 * @param content
	 * @param range
	 */
	@JavascriptInterface
	public void setParams(String content, String range) {
		if (method != null) {
			method.runMethod(content, range);
		}
	}

	/**
	 * 让"我的"页面重新刷新
	 */
	@JavascriptInterface
	public void setMineRefresh() {
		FragmentMine fragmentMine = ClassManagerUtil.newInstance(FragmentMine.class);
		fragmentMine.setIsload(true);
	}

	@JavascriptInterface
	public void setCookie(String cookie) {
		if (method != null) {
			method.runMethod(cookie);
		}
	}

	@JavascriptInterface
	public void imageFullPage(String select) {
		if (method != null) {
			method.runMethod(select);
		}
	}

}