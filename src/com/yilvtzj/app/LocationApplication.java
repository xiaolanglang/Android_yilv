package com.yilvtzj.app;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.baidu.mapapi.SDKInitializer;

public class LocationApplication extends Application {
	public static RequestQueue requestQueue;
	public static int memoryCacheSize;

	@Override
	public void onCreate() {
		super.onCreate();
		SDKInitializer.initialize(getApplicationContext());

		// 不必为每一次HTTP请求都创建一个RequestQueue对象，推荐在application中初始化
		requestQueue = Volley.newRequestQueue(this);
		// 计算内存缓存
		memoryCacheSize = getMemoryCacheSize();
	}

	/**
	 * @description
	 *
	 * @param context
	 * @return 得到需要分配的缓存大小，这里用八分之一的大小来做
	 */
	public int getMemoryCacheSize() {
		// Get memory class of this device, exceeding this amount will throw an
		// OutOfMemory exception.
		int maxMemory = (int) Runtime.getRuntime().maxMemory();
		// Use 1/8th of the available memory for this memory cache.
		return maxMemory / 8;
	}

}
