package com.yilvtzj.util;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;

import com.yilvtzj.entity.Account;

public class AccountUtil {

	public static String getCookie() {
		return SharedPreferencesUtil.get(SharedPreferencesUtil.COOKIE, "cookie", "");
	}

	public static void setCookie(String cookie) {
		SharedPreferencesUtil.put(SharedPreferencesUtil.COOKIE, "cookie", cookie);
	}

	public static Account getAccount() {
		if (StringUtil.isEmpty(getCookie())) {
			return null;
		}

		String id = SharedPreferencesUtil.get(SharedPreferencesUtil.COOKIE_ACCOUNT, "id", "");
		if (StringUtil.isEmpty(id)) {
			return null;
		}
		String nickname = SharedPreferencesUtil.get(SharedPreferencesUtil.COOKIE_ACCOUNT, "nickname", "");
		String img = SharedPreferencesUtil.get(SharedPreferencesUtil.COOKIE_ACCOUNT, "img", "");
		Account account = new Account();
		account.setId(id);
		account.setNickname(nickname);
		account.setImg(img);
		return account;
	}

	public static void setAccount(Context context, Account account) {
		Map<String, String> map = new HashMap<>();
		map.put("id", account.getId());
		map.put("nickname", account.getNickname());
		map.put("img", account.getImg());
		SharedPreferencesUtil.put(SharedPreferencesUtil.COOKIE_ACCOUNT, map);
	}
}
