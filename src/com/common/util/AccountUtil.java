package com.common.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yilvtzj.entity.Account;

public class AccountUtil {

	public static Account getAccount() {
		String json = SharePreferenceUtil.get(SharePreferenceUtil.COOKIE_ACCOUNT, "account", null);
		if (StringUtil.isEmpty(json)) {
			return null;
		}
		Gson gson = new Gson();
		Account account = gson.fromJson(json, new TypeToken<Account>() {
		}.getType());
		return account;
	}

	public static void setAccount(Account account) {
		if (account == null) {
			SharePreferenceUtil.put(SharePreferenceUtil.COOKIE_ACCOUNT, "account", null);
		}
		Gson gson = new Gson();
		String json = gson.toJson(account, new TypeToken<Account>() {
		}.getType());
		SharePreferenceUtil.put(SharePreferenceUtil.COOKIE_ACCOUNT, "account", json);
	}
}
