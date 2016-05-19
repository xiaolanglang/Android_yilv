package com.common.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Toast工具类，提供样式统一的toast提示
 * 
 * @author Administrator
 *
 */
public class ToastUtil {
	public static Toast show(Context context, String text, Toast toast) {
		if (toast != null) {
			toast.cancel();
		}
		toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
		toast.show();
		return toast;
	}
}
