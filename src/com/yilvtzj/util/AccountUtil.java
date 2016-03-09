package com.yilvtzj.util;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;

import com.yilvtzj.entity.Account;

public class AccountUtil {

	public static String getCookie() {
		return SharePreferenceUtil.get(SharePreferenceUtil.COOKIE, "cookie", "");
	}

	public static void setCookie(String cookie) {
		SharePreferenceUtil.put(SharePreferenceUtil.COOKIE, "cookie", cookie);
	}

	public static Account getAccount() {
		if (StringUtil.isEmpty(getCookie())) {
			return null;
		}

		String id = SharePreferenceUtil.get(SharePreferenceUtil.COOKIE_ACCOUNT, "id", "");
		if (StringUtil.isEmpty(id)) {
			return null;
		}
		String nickname = SharePreferenceUtil.get(SharePreferenceUtil.COOKIE_ACCOUNT, "nickname", "");
		String img = SharePreferenceUtil.get(SharePreferenceUtil.COOKIE_ACCOUNT, "img", "");
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
		SharePreferenceUtil.put(SharePreferenceUtil.COOKIE_ACCOUNT, map);
	}
}
