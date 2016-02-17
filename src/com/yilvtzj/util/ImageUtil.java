package com.yilvtzj.util;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

public class ImageUtil {
	public static String ImageToBase64(String url) {
		Bitmap bitmap = getBitMBitmap(url);
		return bitmapToBase64(bitmap);
	}

	/**
	 * bitmap转为base64
	 * 
	 * @param bitmap
	 * @return
	 */
	public static String bitmapToBase64(Bitmap bitmap) {

		String result = null;
		ByteArrayOutputStream baos = null;
		if (bitmap != null) {
			baos = new ByteArrayOutputStream();
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
			byte[] bitmapBytes = baos.toByteArray();
			result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
		}
		try {
			if (baos != null) {
				baos.flush();
				baos.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	public static Bitmap getBitMBitmap(String path) {
		Bitmap map = null;
		InputStream in = null;
		try {
			in = new FileInputStream(path);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		map = BitmapFactory.decodeStream(in);
		if (in != null) {
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return map;
	}
}
