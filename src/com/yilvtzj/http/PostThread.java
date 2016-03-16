package com.yilvtzj.http;

import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;

import com.yilvtzj.app.MyApplication;
import com.yilvtzj.http.SocketHttpRequester.SocketListener;
import com.yilvtzj.util.SimpleHandler.RunMethod;

public class PostThread implements Runnable {
	private Map<String, String> params;
	private String url;
	private PostThreadListener listener;
	private Dialog dialog;
	private boolean isShow = false;

	public PostThread(Map<String, String> params, String url, Dialog dialog) {
		this.params = params;
		this.url = url;
		this.dialog = dialog;
	}

	public PostThreadListener getListener() {
		return listener;
	}

	public PostThread setListener(PostThreadListener listener) {
		this.listener = listener;
		return this;
	}

	/**
	 * 请求成功回调方法
	 */
	private void success(final String JSON) {
		if (listener != null) {
			MyApplication.handler.runMethod(new RunMethod() {

				@Override
				public void runMethod() {
					try {
						listener.postThreadSuccess(new JSONObject(JSON));
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			});

		}
	}

	/**
	 * 请求出错回调方法
	 */
	private void error() {
		if (listener != null) {
			MyApplication.handler.runMethod(new RunMethod() {

				@Override
				public void runMethod() {
					listener.postThreadFailed();
				}
			});
		}
	}

	/**
	 * 无论请求成功还是失败，都会回调的方法
	 */
	private void finallyMethod() {
		toggleDialog();
		if (listener != null) {
			MyApplication.handler.runMethod(new RunMethod() {

				@Override
				public void runMethod() {
					listener.postThreadFinally();
				}
			});
		}
	}

	/**
	 * 显示或者隐藏dialog
	 */
	private void toggleDialog() {
		if (dialog == null) {
			return;
		}
		if (isShow) {
			MyApplication.handler.runMethod(new RunMethod() {

				@Override
				public void runMethod() {
					dialog.cancel();
				}
			});

			isShow = false;
		} else {
			MyApplication.handler.runMethod(new RunMethod() {

				@Override
				public void runMethod() {
					dialog.show();
				}
			});

			isShow = true;
		}
	}

	@Override
	public void run() {
		toggleDialog();
		try {
			new SocketHttpRequester().setSocketListener(new SocketListener() {
				@Override
				public void result(String JSON) {
					success(JSON);
				}
			}).post(url, params);
		} catch (Exception e) {
			error();
			e.printStackTrace();
		} finally {
			finallyMethod();
		}

	}

	/**
	 * 请求的回调方法，方法已经放在handler中，可以直接操作主线程中的视图元素
	 * 
	 * @author Administrator
	 *
	 */
	public interface PostThreadListener {
		public boolean postThreadSuccess(JSONObject JSON) throws JSONException;

		public boolean postThreadFailed();

		public boolean postThreadFinally();
	}

}
