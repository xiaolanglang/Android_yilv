package com.yilvtzj.http;

import java.util.Map;

import android.content.Context;

import com.yilvtzj.http.SocketHttpRequester.SocketListener;

public class PostThread implements Runnable {
	private Context context;
	private Map<String, String> params;
	private String url;
	private PostThreadListener listener;
	private String resultJSON;

	/**
	 * 请求成功回调方法
	 */
	private void success() {
		if (listener != null) {
			listener.postThreadSuccess(resultJSON);
		}
	}

	/**
	 * 请求出错回调方法
	 */
	private void error() {
		if (listener != null) {
			listener.postThreadSuccess(resultJSON);
		}
	}

	/**
	 * 无论请求成功还是失败，都会回调的方法
	 */
	private void finallyMethod() {
		if (listener != null) {
			listener.postThreadFinally(resultJSON);
		}
	}

	@Override
	public void run() {
		try {
			new SocketHttpRequester().setSocketListener(new SocketListener() {

				@Override
				public void result(String JSON) {
					resultJSON = JSON;
					success();
				}
			}).post(url, context, params);
		} catch (Exception e) {
			error();
			e.printStackTrace();
		} finally {
			finallyMethod();
		}

	}

	public interface PostThreadListener {
		public boolean postThreadSuccess(String JSON);

		public boolean postThreadFailed(String JSON);

		public boolean postThreadFinally(String JSON);
	}

}
