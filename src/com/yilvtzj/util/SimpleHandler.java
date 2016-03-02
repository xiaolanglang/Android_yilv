package com.yilvtzj.util;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;

public class SimpleHandler {

	private final static int SHOWTOAST = 0;
	private static Toast toast;
	private Activity activity;
	private static Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SHOWTOAST:
				toast.show();
				break;

			}
		}
	};

	public SimpleHandler(Activity activity) {
		this.activity = activity;
	}

	public void sendMessage(final String message) {
		Message msg = new Message();
		msg.obj = message;
		msg.what = SHOWTOAST;
		toast = new Toast() {

			@Override
			public void show() {
				ToastUtil.show(activity, message, null);
			}
		};
		handler.sendMessage(msg);
	}

	private interface Toast {
		void show();
	}
}
