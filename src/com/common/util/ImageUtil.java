package com.common.util;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
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

	/**
	 * 以最省内存的方式读取本地资源的图片
	 * 
	 * @param context
	 * @param resId
	 * @return
	 */
	public static Bitmap readBitMap(Context context, int resId) {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Bitmap.Config.RGB_565;
		opt.inSampleSize = 2;
		// 获取资源图片
		InputStream is = context.getResources().openRawResource(resId);
		return BitmapFactory.decodeStream(is, null, opt);
	}

	/**
	 * 保存文件
	 * 
	 * @param bm
	 * @param fileName
	 * @throws IOException
	 */
	public void saveFile(Bitmap bm, String fileName) throws IOException {
		String path = "/revoeye/";
		File dirFile = new File(path);
		if (!dirFile.exists()) {
			dirFile.mkdir();
		}
		File myCaptureFile = new File(path + fileName);
		BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
		bm.compress(Bitmap.CompressFormat.JPEG, 100, bos);
		bos.flush();
		bos.close();
	}

	/**
	 * @param dir
	 *            文件夹路径
	 * @param fileName
	 *            文件名称
	 * @param bitmap
	 *            要保存的文件
	 * @throws IOException
	 */
	public static void bitmap2File(String dir, String fileName, Bitmap bitmap) throws IOException {
		if (bitmap == null) {
			return;
		}
		File folderFile = new File(dir);
		if (!folderFile.exists()) {
			folderFile.mkdirs();
		}
		File file = new File(dir + "/" + fileName);
		file.createNewFile();
		FileOutputStream fos = new FileOutputStream(file);
		bitmap.compress(CompressFormat.JPEG, 100, fos);
		fos.flush();
		fos.close();
	}

	/**
	 * 图片文件转bitmap
	 * 
	 * @param filePath
	 * @return
	 */
	public static Bitmap file2Bitmap(String filePath) {
		Bitmap bitmap = BitmapFactory.decodeFile(filePath, getBitmapOption(1)); // 将图片的长和宽缩小味原来的1/2
		return bitmap;
	}

	private static Options getBitmapOption(int inSampleSize) {
		System.gc();
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inPurgeable = true;
		options.inSampleSize = inSampleSize;
		return options;
	}
}
