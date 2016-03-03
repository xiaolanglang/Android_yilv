package com.yilvtzj.util;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;

public class SimpleHandler {

	private final static int SHOWTOAST = 0;
	private final static int RUNMETHOD = 1;
	private static Toast toast;
	private Activity activity;
	private static Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SHOWTOAST:
				toast.show();
				break;
			case RUNMETHOD:
				if (msg.obj != null) {
					RunMethod method = (RunMethod) msg.obj;
					if (method != null) {
						method.run();
					}
				}
				break;

			}
		}
	};

	public SimpleHandler(Activity activity) {
		this.activity = activity;
	}

	public void sendMessage(final String message) {
		toast = new Toast() {

			@Override
			public void show() {
				ToastUtil.show(activity, message, null);
			}
		};
		handler.sendEmptyMessage(SHOWTOAST);
	}

	public void runMethod(RunMethod method) {
		Message message = new Message();
		message.what = RUNMETHOD;
		message.obj = method;
		handler.sendMessage(message);
	}

	private interface Toast {
		void show();
	}

	public interface RunMethod {
		void run();
	}
}
