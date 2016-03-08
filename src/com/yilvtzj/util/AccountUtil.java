package com.yilvtzj.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.yilvtzj.entity.Account;

public class AccountUtil {

	public static String getCookie(Context context) {
		SharedPreferences preferences = context.getSharedPreferences("cookie", 0);
		String cookie = preferences.getString("cookie", "");
		return cookie;
	}

	public static void setCookie(Context context, String cookie) {
		Editor editor = context.getSharedPreferences("cookie", 0).edit();
		editor.putString("cookie", cookie);
		editor.commit();
	}

	public static Account getAccount(Context context) {
		if (StringUtil.isEmpty(getCookie(context))) {
			return null;
		}
		SharedPreferences preferences = context.getSharedPreferences("cookie_account", 0);
		String id = preferences.getString("id", "");
		if (StringUtil.isEmpty(id)) {
			return null;
		}
		String nickname = preferences.getString("nickname", "");
		String img = preferences.getString("img", "");
		Account account = new Account();
		account.setId(id);
		account.setNickname(nickname);
		account.setImg(img);
		return account;
	}

	public static void setAccount(Context context, Account account) {
		Editor editor = context.getSharedPreferences("cookie_account", 0).edit();
		editor.putString("id", account.getId());
		editor.putString("nickname", account.getNickname());
		editor.putString("img", account.getImg());
		editor.commit();
	}
}
