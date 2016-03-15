package com.yilvtzj.app;

import java.io.File;

import android.app.Application;
import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.androidpn.service.ServiceManager;
import com.baidu.mapapi.SDKInitializer;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.yilvtzj.R;
import com.yilvtzj.util.Global;
import com.yilvtzj.util.SimpleHandler;

public class MyApplication extends Application {
	public static ImageLoader imageLoader;
	public static RequestQueue requestQueue;
	public static int memoryCacheSize;
	public static SimpleHandler handler;

	@Override
	public void onCreate() {
		super.onCreate();
		SDKInitializer.initialize(getApplicationContext());
		// 初始化图片加载
		imageLoader = initImageLoader(getApplicationContext());

		// 不必为每一次HTTP请求都创建一个RequestQueue对象，推荐在application中初始化
		requestQueue = Volley.newRequestQueue(this);
		// 计算内存缓存
		memoryCacheSize = getMemoryCacheSize();

		Global.CONTEXT = this;

		ServiceManager serviceManager = new ServiceManager(this);
		serviceManager.setNotificationIcon(R.drawable.ic_launcher);
		serviceManager.startService();
		handler = new SimpleHandler(this);
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

	private final static ImageLoader initImageLoader(Context context) {
		File cacheDir = StorageUtils.getOwnCacheDirectory(context, "imageloader/Cache");
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
				.memoryCacheExtraOptions(480, 480)
				// maxwidth, max height，即保存的每个缓存文件的最大长宽
				.defaultDisplayImageOptions(getDefaultDisplayOption()).threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory().memoryCache(new UsingFreqLimitedMemoryCache(10 * 1024 * 1024))
				// 你可以通过自己的内存缓存实现
				.memoryCacheSize(10 * 1024 * 1024).diskCacheFileCount(500).imageDownloader(new BaseImageDownloader(context))
				.writeDebugLogs()
				// Remove for releaseapp
				.discCache(new UnlimitedDiskCache(cacheDir)).tasksProcessingOrder(QueueProcessingType.LIFO).build();
		ImageLoader imageLoader = ImageLoader.getInstance();
		imageLoader.init(config);
		return imageLoader;
	}

	private final static DisplayImageOptions getDefaultDisplayOption() {
		DisplayImageOptions options = new DisplayImageOptions.Builder()
		// 设置图片在下载期间显示的图片
				.showImageOnLoading(R.drawable.ic_loading_gray)
				// 设置图片Uri为空或是错误的时候显示的图片
				.showImageForEmptyUri(R.drawable.icon_img_error)
				// 设置图片加载/解码过程中错误时候显示的图片
				.showImageOnFail(R.drawable.icon_img_error).cacheInMemory(true) // 设置下载的图片是否缓存在内存中
				.cacheOnDisk(true) // 设置下载的图片是否缓存在SD卡中
				.build(); // 创建配置过得DisplayImageOption对象
		return options;
	}

}
