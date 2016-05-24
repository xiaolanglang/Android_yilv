package com.common.util;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.yilvtzj.R;

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
		} else {
			toast = new Toast(context);
		}
		View view = LayoutInflater.from(context).inflate(R.layout.common_toast, null);
		TextView tv = (TextView) view.findViewById(R.id.content);
		tv.setText(text);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.setDuration(Toast.LENGTH_SHORT);
		toast.setView(view);
		toast.show();
		return toast;
	}

}
