package com.yilvtzj.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class AccountUtil {

	public static String getCookie(Context context) {
		SharedPreferences preferences = context.getSharedPreferences("cookie", 0);
		String cookie = preferences.getString("cookie", null);
		return cookie;
	}

	public static void setCookie(Context context, String cookie) {
		Editor editor = context.getSharedPreferences("cookie", 0).edit();
		editor.putString("cookie", cookie);
		editor.commit();
	}
}
