package com.yilvtzj.http;

import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.yilvtzj.http.SocketHttpRequester.SocketListener;

public class PostThread implements Runnable {
	private Map<String, String> params;
	private String url;
	private PostThreadListener listener;

	/**
	 * 请求成功回调方法
	 */
	private void success(JSONObject JSON) {
		if (listener != null) {
			listener.postThreadSuccess(JSON);
		}
	}

	/**
	 * 请求出错回调方法
	 */
	private void error() {
		if (listener != null) {
			listener.postThreadFailed();
		}
	}

	/**
	 * 无论请求成功还是失败，都会回调的方法
	 */
	private void finallyMethod() {
		if (listener != null) {
			listener.postThreadFinally();
		}
	}

	@Override
	public void run() {
		try {
			new SocketHttpRequester().setSocketListener(new SocketListener() {

				@Override
				public void result(String JSON) {
					try {
						success(new JSONObject(JSON));
					} catch (JSONException e) {
						error();
						e.printStackTrace();
					}
				}
			}).post(url, params);
		} catch (Exception e) {
			error();
			e.printStackTrace();
		} finally {
			finallyMethod();
		}

	}

	public interface PostThreadListener {
		public boolean postThreadSuccess(JSONObject JSON);

		public boolean postThreadFailed();

		public boolean postThreadFinally();
	}

}
