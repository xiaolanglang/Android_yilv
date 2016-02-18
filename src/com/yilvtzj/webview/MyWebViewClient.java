package com.yilvtzj.webview;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.yilvtzj.R;
import com.yilvtzj.activity.WebViewActivity;
import com.yilvtzj.dialog.LoadingDialog;
import com.yilvtzj.util.Global;
import com.yilvtzj.webview.JsInterface.JsInterfaceMethod;

public class MyWebViewClient extends WebViewClient {
	private Activity activity;
	// 提示信息
	private LoadingDialog dialog;
	// 是否显示在当前页
	private boolean currentActivity;
	private boolean ifDialog;
	private AssetManager assetManager;
	private JsInterface jsInterface;
	private String jumpTo;

	public MyWebViewClient(Activity activity, WebView webView, JsInterface jsInterface, String jumpTo) {
		myWebViewClient(activity, false, true, webView, jsInterface, jumpTo);
	}

	public MyWebViewClient(Activity activity, WebView webView, JsInterface jsInterface) {
		myWebViewClient(activity, false, true, webView, jsInterface, null);
	}

	public MyWebViewClient(Activity activity, boolean currentActivity, WebView webView, JsInterface jsInterface,
			String jumpTo) {
		myWebViewClient(activity, currentActivity, true, webView, jsInterface, null);
	}

	public MyWebViewClient(boolean ifDialog, Activity activity, WebView webView, JsInterface jsInterface, String jumpTo) {
		myWebViewClient(activity, false, ifDialog, webView, jsInterface, null);
	}

	/**
	 * @param activity
	 * @param currentActivity
	 *            是否在当前activity中显示
	 * @param ifDialog
	 *            是否显示对话框
	 */
	@SuppressWarnings("deprecation")
	@SuppressLint("SetJavaScriptEnabled")
	private void myWebViewClient(Activity activity, boolean currentActivity, boolean ifDialog, WebView webView,
			JsInterface jsInterface, String jumpTo) {
		this.activity = activity;
		this.currentActivity = currentActivity;
		this.ifDialog = ifDialog;
		this.assetManager = activity.getAssets();
		this.jsInterface = jsInterface;
		this.jumpTo = jumpTo;
		if (jumpTo == null) {
			this.jumpTo = WebViewActivity.class.getName();
		}
		WebSettings setting = webView.getSettings();
		setting.setJavaScriptEnabled(true);
		setting.setCacheMode(WebSettings.LOAD_NO_CACHE);
		setting.setSaveFormData(false);
		setting.setSavePassword(false);
		setting.setSupportZoom(false);
		setting.setSupportZoom(false);
		webView.addJavascriptInterface(jsInterface, Global.JSINTERFACE);
	}

	public boolean shouldOverrideUrlLoading(WebView webView, String url) {
		super.shouldOverrideUrlLoading(webView, url);

		if (!currentActivity) {
			Intent intent = new Intent();
			// 第一参数取的是这个应用程序的activity，生命周期是整个应用
			// 第二个参数是要跳转的页面的全路径
			intent.setClassName(activity, jumpTo);
			// Bundle类用作携带数据，它类似于Map，用于存放key-value名值对形式的值
			Bundle b = new Bundle();
			b.putString("url", url);
			// 此处使用putExtras，接受方就响应的使用getExtra
			intent.putExtras(b);
			activity.startActivity(intent);
			// 设置切换效果
			activity.overridePendingTransition(R.anim.myslide_in_right, R.anim.myslide_out_left);
		} else {
			webView.loadUrl(url);
		}
		return true;
	}

	// 开始加载网页时要做的工作
	public void onPageStarted(WebView webView, String url, Bitmap favicon) {
		System.out.println("onPageStarted>>>>>>>>>>>>>>>url:>>>" + url);
		showDialog(ifDialog);
	}

	// 加载完成时要做的工作
	public void onPageFinished(WebView view, String url) {
		System.out.println("onPageFinished>>>>>>>>>>>>>>>url:>>>" + url);
		cancleDialog();
	}

	// 加载错误时要做的工作
	public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
		cancleDialog();
		Toast.makeText(activity, "哎呀!页面没打开,刷新一下试试", Toast.LENGTH_LONG).show();
		view.loadUrl("file:///android_asset/page/error/index.html");
		final WebView webView = view;
		final String failUrl = failingUrl;
		jsInterface.setJsInterfaceListener(new JsInterfaceMethod() {

			@Override
			public void runMethod(String... params) {
				webView.post(new Runnable() {
					@Override
					public void run() {
						webView.loadUrl(failUrl);
					}
				});
			}
		});
	}

	private void showDialog(boolean ifDialog) {
		if (ifDialog) {
			dialog = new LoadingDialog(activity);
			dialog.setCanceledOnTouchOutside(false);
			dialog.show();

		}
	}

	private void cancleDialog() {
		if (dialog != null) {
			dialog.cancel();
		}
	}

	// 拦截请求，加载本地静态文件，例如加载本地js文件或者css文件，加快页面加载速度
	@Override
	public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
		WebResourceResponse response = null;

		// 拦截请求，加载本地资源
		if (checkUrlForIntercept(url)) {
			InputStream inputStream = null;
			String type = getMimeType(url);
			String path = getFilePath(url);

			if (url.contains(Global.urlContentLocalImage)) {
				try {
					inputStream = new FileInputStream(path);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}

			try {
				if (inputStream == null) {
					inputStream = assetManager.open(path, AssetManager.ACCESS_BUFFER);
				}
				response = new WebResourceResponse(type, Global.character, inputStream);
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				System.out.println("拦截静态文件>>>>>>>>>>>>>>" + url);
				if (response == null) {
					System.out.println(">>>>>>>>>>>拦截但是没有找到的文件：" + path);
				}
			}
		}

		return response;
	}

	private static boolean checkUrlForIntercept(String url) {
		if (url.contains(Global.urlContent) || url.contains(Global.urlContentLocalImage)) {
			return true;
		}
		return false;
	}

	private static String getMimeType(String url) {
		int css = url.lastIndexOf(Global.urlCss);
		int js = url.lastIndexOf(Global.urlJs);
		int urlpng = url.lastIndexOf(Global.urlPng);
		if (css != -1) {
			return Global.mimeTypeCss;
		} else if (js != -1) {
			return Global.mimeTypeJs;
		} else if (urlpng != -1) {
			return Global.mimeTypeImg;
		}
		return "text/html,application/xhtml+xml,application/xml";
	}

	private static String getFilePath(String url) {
		int start = 0, end = url.length();
		if (url.contains(Global.urlContent)) {
			start = url.indexOf(Global.urlContent) + Global.urlContent.length();
		} else if (url.contains(Global.urlContentLocalImage)) {
			start = url.indexOf(Global.urlContentLocalImage) + Global.urlContentLocalImage.length();
		}
		return url.substring(start, end);
	}
}
