package com.common.okhttp;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
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
	private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");

	private HttpConnect() {
	}

	public static HttpConnect getInstance() {
		return connect;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void connect(ServiceListener<?> callback, String url, Map<String, Object> param, TypeToken<?> type) {
		new MyAsyMask(getClient(), callback, type).execute(url, param, null);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void connect(ServiceListener<?> callback, String url, Map<String, Object> param, List<File> files,
			TypeToken<?> type) {
		new MyAsyMask(getClient(), callback, type).execute(url, param, files);
	}

	private OkHttpClient getClient() {
		OkHttpClient mOkHttpClient = builder.connectTimeout(60, TimeUnit.SECONDS)
				.cookieJar(new CookiesManager(Global.CONTEXT)).build();
		return mOkHttpClient;
	}

	private Request getPraRequest(String url, Map<String, Object> param, List<File> files) {

		if (files != null && files.size() > 0) {
			okhttp3.MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
			if (param != null) {
				Set<String> keys = param.keySet();
				for (String key : keys) {
					builder.addFormDataPart(key, param.get(key).toString());
				}
			}
			for (File file : files) {
				builder.addFormDataPart(file.getName(), file.getName(), RequestBody.create(MEDIA_TYPE_PNG, file));
			}
			return new Request.Builder().url(url).post(builder.build()).build();
		} else {
			okhttp3.FormBody.Builder builder = new FormBody.Builder(); // 表单
			if (param != null) {
				Set<String> keys = param.keySet();
				for (String key : keys) {
					builder.add(key, param.get(key).toString());
				}
			}
			return new Request.Builder().url(url).post(builder.build()).build();
		}

	}

	private class MyAsyMask<Result> extends AsyncTask<Object, Void, Result> {

		private OkHttpClient mOkHttpClient;
		private ServiceListener<Result> callback;
		private TypeToken<?> type;

		private String errorMsg = null;

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
				response = mOkHttpClient.newCall(
						getPraRequest(params[0].toString(), (Map<String, Object>) params[1], (List<File>) params[2]))
						.execute();
				if (response.isSuccessful()) {
					String json = response.body().string();
					LogUtil.e(TAG, "服务器返回：" + json);
					Gson gson = new Gson();
					return gson.fromJson(json, type.getType());
				}

				String a = response.body().string();
				System.out.println(">>>>>>>>>>>" + a);

			} catch (IOException e) {
				errorMsg = e.getMessage();
				e.printStackTrace();
			} catch (JsonSyntaxException e) {
				errorMsg = e.getMessage();
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Result result) {
			if (result == null) {
				callback.onFailed(500, errorMsg);
			} else {
				callback.onSuccess(result);
			}
			callback.onFinally();
		}

	}

}
