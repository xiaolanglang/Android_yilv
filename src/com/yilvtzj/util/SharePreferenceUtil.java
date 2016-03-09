package com.yilvtzj.util;

import java.util.Map;
import java.util.Set;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SharePreferenceUtil {
	public static final String COOKIE = "cookie";
	public static final String COOKIE_ACCOUNT = "cookie_account";
	public static final String HOMEPAGE = "homepage";

	public static void put(String name, Map<String, String> map) {
		Set<String> keys = map.keySet();
		Editor editor = Global.CONTEXT.getSharedPreferences(name, 0).edit();
		for (String key : keys) {
			editor.putString(key, map.get(key));
		}
		editor.commit();
	}

	public static void put(String name, String key, String value) {
		Editor editor = Global.CONTEXT.getSharedPreferences(name, 0).edit();
		editor.putString(key, value);
		editor.commit();
	}

	public static String get(String name, String key, String defValue) {
		SharedPreferences preferences = Global.CONTEXT.getSharedPreferences(name, 0);
		return preferences.getString(key, defValue);
	}
}
