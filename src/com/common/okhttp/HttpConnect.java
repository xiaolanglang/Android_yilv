package com.common.okhttp;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import android.os.AsyncTask;

import com.common.okhttp.cookie.CookiesManager;
import com.common.util.Global;
import com.common.util.LogUtil;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.yilvtzj.service.ServiceListener;

public class HttpConnect {
	private static final String TAG = HttpConnect.class.getSimpleName();
	private static final okhttp3.OkHttpClient.Builder builder = new OkHttpClient.Builder();
	private static HttpConnect connect = new HttpConnect();

	private HttpConnect() {
	}

	public static HttpConnect getInstance() {
		return connect;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void connect(ServiceListener<?> callback, String url, Map<String, Object> param, TypeToken<?> type) {
		OkHttpClient mOkHttpClient = builder.connectTimeout(60, TimeUnit.SECONDS)
				.cookieJar(new CookiesManager(Global.CONTEXT)).build();
		// OkHttpClient mOkHttpClient = builder.connectTimeout(60,
		// TimeUnit.SECONDS).build();
		new MyAsyMask(mOkHttpClient, callback, type).execute(url, param);
	}

	private Request getPraRequest(String url, Map<String, Object> param) {
		okhttp3.FormBody.Builder builder = new FormBody.Builder(); // 表单
		if (param != null) {
			Set<String> keys = param.keySet();
			for (String key : keys) {
				builder.add(key, param.get(key).toString());
			}
		}
		// 通过Request.Builder()去build()一个实例
		return new Request.Builder().url(url).post(builder.build()).build();
	}

	private class MyAsyMask<Result> extends AsyncTask<Object, Void, Result> {

		private OkHttpClient mOkHttpClient;
		private ServiceListener<Result> callback;
		private TypeToken<?> type;

		private static final int JSONException = 0;
		private static final int IOException = 1;

		private int resultCode = -1;

		public MyAsyMask(OkHttpClient mOkHttpClient, ServiceListener<Result> callback, TypeToken<Result> type) {
			this.mOkHttpClient = mOkHttpClient;
			this.callback = callback;
			this.type = type;
		}

		@Override
		protected void onPreExecute() {
			callback.preExecute();
		}

		@SuppressWarnings("unchecked")
		@Override
		protected Result doInBackground(Object... params) {
			okhttp3.Response response = null;
			try {
				response = mOkHttpClient.newCall(getPraRequest(params[0].toString(), (Map<String, Object>) params[1]))
						.execute();
				if (response.isSuccessful()) {
					String json = response.body().string();
					LogUtil.e(TAG, "服务器返回：" + json);
					Gson gson = new Gson();
					return gson.fromJson(json, type.getType());
				}

			} catch (IOException e) {
				resultCode = IOException;
				e.printStackTrace();
			} catch (JsonSyntaxException e) {
				resultCode = JSONException;
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Result result) {
			if (result == null) {
				String msg = "未知错误";
				if (resultCode != -1) {
					switch (resultCode) {
					case JSONException:
						msg = "解析JSON错误";
						break;
					case IOException:
						msg = "IO异常";
						break;
					}
				}
				callback.onFailed(500, msg);
			} else {
				callback.onSuccess(result);
			}
			callback.onFinally();
		}

	}

}
