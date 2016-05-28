package com.common.volley.cache;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.toolbox.ImageLoader.ImageCache;
import com.common.util.ImageUtil;
import com.yilvtzj.aplication.MyApplication;

public class LruBitmapCache implements ImageCache {

	private LruCache<String, Bitmap> mMemoryCache;

	private boolean diskCache = true;

	public boolean isDiskCache() {
		return diskCache;
	}

	public LruBitmapCache setDiskCache(boolean diskCache) {
		this.diskCache = diskCache;
		return this;
	}

	public LruBitmapCache(int memoryCacheSize) {
		mMemoryCache = new LruCache<String, Bitmap>(memoryCacheSize) {
			@Override
			protected int sizeOf(String key, Bitmap bitmap) {
				return bitmap.getRowBytes() * bitmap.getHeight();
			}
		};
	}

	@Override
	public Bitmap getBitmap(String url) {
		Bitmap bitmap = mMemoryCache.get(url);
		if (diskCache && bitmap == null) {
			bitmap = getBitmapFromDisk(url);
		}
		return bitmap;
	}

	@Override
	public void putBitmap(String url, Bitmap bitmap) {
		mMemoryCache.put(url, bitmap);
		if (diskCache) {
			putBitmap2Disk(url, bitmap);
		}
	}

	private Bitmap getBitmapFromDisk(String url) {
		String key = hashKeyForDisk(url);
		String filePath = MyApplication.packageName + "/cache/" + key;
		Bitmap bitmap = ImageUtil.file2Bitmap(filePath);
		return bitmap;
	}

	private void putBitmap2Disk(String url, Bitmap bitmap) {
		String key = hashKeyForDisk(url);
		String dir = MyApplication.packageName + "/cache";
		try {
			ImageUtil.bitmap2File(dir, key, bitmap);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String hashKeyForDisk(String key) {
		String cacheKey;
		try {
			final MessageDigest mDigest = MessageDigest.getInstance("MD5");
			mDigest.update(key.getBytes());
			cacheKey = bytesToHexString(mDigest.digest());
		} catch (NoSuchAlgorithmException e) {
			cacheKey = String.valueOf(key.hashCode());
		}
		return cacheKey;
	}

	private String bytesToHexString(byte[] bytes) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < bytes.length; i++) {
			String hex = Integer.toHexString(0xFF & bytes[i]);
			if (hex.length() == 1) {
				sb.append('0');
			}
			sb.append(hex);
		}
		return sb.toString();
	}
}
