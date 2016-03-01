package com.yilvtzj.activity.common;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebView;

import com.yilvtzj.R;
import com.yilvtzj.util.ActivityUtil;
import com.yilvtzj.webview.JsInterface;
import com.yilvtzj.webview.MyWebChromeClient;
import com.yilvtzj.webview.MyWebViewClient;

public class WebActivity extends Activity {
	protected WebView webView;
	protected JsInterface jsInterface = new JsInterface();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_webview);
		super.onCreate(savedInstanceState);

		setActionBarLayout(R.layout.common_actionbar_layout);
	}

	/**
	 * 初始化webview
	 */
	protected void initWebView() {
		webView = (WebView) findViewById(R.id.webView);
		webView.setWebViewClient(new MyWebViewClient(this, false, true, webView, jsInterface, null));
		webView.setWebChromeClient(new MyWebChromeClient(this));
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			exit();
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		webView.destroy();
	}

	/**
	 * 设置ActionBar的布局
	 * 
	 * @param layoutId
	 *            布局Id
	 * 
	 * */
	public void setActionBarLayout(int layoutId) {
		ActionBar actionBar = getActionBar();
		if (null != actionBar) {
			actionBar.setDisplayShowHomeEnabled(false);
			actionBar.setDisplayShowCustomEnabled(true);

			LayoutInflater inflator = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View v = inflator.inflate(layoutId, null);
			@SuppressWarnings("deprecation")
			ActionBar.LayoutParams layout = new ActionBar.LayoutParams(LayoutParams.FILL_PARENT,
					LayoutParams.FILL_PARENT);
			actionBar.setCustomView(v, layout);
		}
	}

	/**
	 * 实现onClick方法，在这里面监听actionbar中按钮的点击事件
	 * 
	 * */
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back: {
			exit();
		}
			break;
		default:
			break;
		}
	}

	/**
	 * 退出界面
	 */
	private void exit() {
		ActivityUtil.closeActivity(this);
	}
}
