package com.yilvtzj.util;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

public class SimpleHandler {

	private final static int SHOWTOAST = 0;
	private final static int RUNMETHOD = 1;
	private static Toast toast;
	private Context context;
	private android.widget.Toast toast2;
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
						method.runMethod();
					}
				}
				break;

			}
		}
	};

	public SimpleHandler(Context context) {
		this.context = context;
	}

	public void sendMessage(final String message) {
		toast = new Toast() {

			@Override
			public void show() {
				toast2 = ToastUtil.show(context, message, toast2);
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
		void runMethod();
	}
}
