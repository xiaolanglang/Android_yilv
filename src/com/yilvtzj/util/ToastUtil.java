package com.yilvtzj.util;

import android.content.Context;
import android.widget.Toast;

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
