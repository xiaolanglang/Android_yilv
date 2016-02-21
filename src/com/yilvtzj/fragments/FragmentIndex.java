package com.yilvtzj.fragments;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.view.View;

import com.yilvtzj.R;
import com.yilvtzj.util.Global;
import com.yilvtzj.webview.JsInterface.JsInterfaceMethod;

public class FragmentIndex extends MyFragment implements JsInterfaceMethod {

	@Override
	public void runMethod(String... params) {
		if ("false".equals(params[0])) {
			handler.sendEmptyMessage(0);
		} else if ("true".equals(params[0])) {
			handler.sendEmptyMessage(1);
		}
	}

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			int what = msg.what;
			if (0 == what) {
				getActivity().findViewById(R.id.frameMenu).setVisibility(View.VISIBLE);
				swipeLayout.setEnabled(true);
			} else if (1 == what) {
				View v = getActivity().findViewById(R.id.frameMenu);
				v.setVisibility(View.GONE);
				swipeLayout.setEnabled(false);
			}
		}
	};

	@Override
	protected void hookOnStart() {
		webView.loadUrl(Global.getServletUrl("/"));
		jsInterface.setJsInterfaceListener(this);
	}
}
