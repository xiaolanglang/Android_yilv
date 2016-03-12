package com.yilvtzj.util;

import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

public class Utils {
	public static void showLongToast(Context context, String pMsg) {
		Toast.makeText(context, pMsg, Toast.LENGTH_LONG).show();
	}

	public static void showShortToast(Context context, String pMsg) {
		Toast.makeText(context, pMsg, Toast.LENGTH_SHORT).show();
	}

	/**
	 * 判断是否有网络
	 */
	public static boolean isNetworkAvailable(Context context) {
		if (context.checkCallingOrSelfPermission(Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
			return false;
		} else {
			ConnectivityManager connectivity = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);

			if (connectivity == null) {
				Log.w("Utility", "couldn't get connectivity manager");
			} else {
				NetworkInfo[] info = connectivity.getAllNetworkInfo();
				if (info != null) {
					for (int i = 0; i < info.length; i++) {
						if (info[i].isAvailable()) {
							Log.d("Utility", "network is available");
							return true;
						}
					}
				}
			}
		}
		Log.d("Utility", "network is not available");
		return false;
	}

	/**
	 * 移除SharedPreference
	 * 
	 * @param context
	 * @param key
	 */
	public static final void RemoveValue(Context context, String key) {
		Editor editor = getSharedPreference(context).edit();
		editor.remove(key);
		boolean result = editor.commit();
		if (!result) {
			Log.e("移除Shared", "save " + key + " failed");
		}
	}

	private static final SharedPreferences getSharedPreference(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context);
	}

	/**
	 * 获取SharedPreference 值
	 * 
	 * @param context
	 * @param key
	 * @return
	 */
	public static final String getValue(Context context, String key) {
		return getSharedPreference(context).getString(key, "");
	}

	public static final Boolean getBooleanValue(Context context, String key) {
		return getSharedPreference(context).getBoolean(key, false);
	}

	public static final void putBooleanValue(Context context, String key, boolean bl) {
		Editor edit = getSharedPreference(context).edit();
		edit.putBoolean(key, bl);
		edit.commit();
	}

	public static final int getIntValue(Context context, String key) {
		return getSharedPreference(context).getInt(key, 0);
	}

	public static final long getLongValue(Context context, String key, long default_data) {
		return getSharedPreference(context).getLong(key, default_data);
	}

	public static final boolean putLongValue(Context context, String key, Long value) {
		Editor editor = getSharedPreference(context).edit();
		editor.putLong(key, value);
		return editor.commit();
	}

	public static final Boolean hasValue(Context context, String key) {
		return getSharedPreference(context).contains(key);
	}

	/**
	 * 设置SharedPreference 值
	 * 
	 * @param context
	 * @param key
	 * @param value
	 */
	public static final boolean putValue(Context context, String key, String value) {
		value = value == null ? "" : value;
		Editor editor = getSharedPreference(context).edit();
		editor.putString(key, value);
		boolean result = editor.commit();
		if (!result) {
			return false;
		}
		return true;
	}

	/**
	 * 设置SharedPreference 值
	 * 
	 * @param context
	 * @param key
	 * @param value
	 */
	public static final boolean putIntValue(Context context, String key, int value) {
		Editor editor = getSharedPreference(context).edit();
		editor.putInt(key, value);
		boolean result = editor.commit();
		if (!result) {
			return false;
		}
		return true;
	}

	/**
	 * 验证邮箱
	 * 
	 * @param email
	 * @return
	 */
	public static boolean isEmail(String email) {
		String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
		Pattern p = Pattern.compile(str);
		Matcher m = p.matcher(email);

		return m.matches();
	}

	/**
	 * 验证手机号
	 * 
	 * @param mobiles
	 * @return
	 */
	public static boolean isMobileNO(String mobiles) {
		Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(17[^4,\\D])|(18[0-9]))\\d{8}$");
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}

	/**
	 * 验证是否是数字
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNumber(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		java.util.regex.Matcher match = pattern.matcher(str);
		if (match.matches() == false) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 获取版本号
	 * 
	 * @return 当前应用的版本号
	 */
	public static String getVersion(Context context) {
		try {
			PackageManager manager = context.getPackageManager();
			PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
			String version = info.versionName;
			return version;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	private static float sDensity = 0;

	/**
	 * DP转换为像素
	 * 
	 * @param context
	 * @param nDip
	 * @return
	 */
	public static int dipToPixel(Context context, int nDip) {
		if (sDensity == 0) {
			final WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
			DisplayMetrics dm = new DisplayMetrics();
			wm.getDefaultDisplay().getMetrics(dm);
			sDensity = dm.density;
		}
		return (int) (sDensity * nDip);
	}

	/**
	 * 将内容生成二维码
	 * 
	 * @param content
	 *            要转换的字符串,可以是中文
	 * @param QR_WIDTH
	 * @param QR_HEIGHT
	 */
	public Bitmap createQRImage(String content, int QR_WIDTH, int QR_HEIGHT) {
		try {
			// 判断URL合法性
			if (StringUtil.isEmpty(content)) {

			}
			Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
			hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
			// 图像数据转换，使用了矩阵转换
			BitMatrix bitMatrix = new QRCodeWriter().encode(content, BarcodeFormat.QR_CODE, QR_WIDTH, QR_HEIGHT, hints);
			int[] pixels = new int[QR_WIDTH * QR_HEIGHT];
			// 下面这里按照二维码的算法，逐个生成二维码的图片，
			// 两个for循环是图片横列扫描的结果
			for (int y = 0; y < QR_HEIGHT; y++) {
				for (int x = 0; x < QR_WIDTH; x++) {
					if (bitMatrix.get(x, y)) {
						pixels[y * QR_WIDTH + x] = 0xff000000;
					} else {
						pixels[y * QR_WIDTH + x] = 0xffffffff;
					}
				}
			}
			// 生成二维码图片的格式，使用ARGB_8888
			Bitmap bitmap = Bitmap.createBitmap(QR_WIDTH, QR_HEIGHT, Bitmap.Config.ARGB_8888);
			bitmap.setPixels(pixels, 0, QR_WIDTH, 0, 0, QR_WIDTH, QR_HEIGHT);
			// 显示到一个ImageView上面
			return bitmap;
		} catch (WriterException e) {
			e.printStackTrace();
		}
		return null;
	}

}
