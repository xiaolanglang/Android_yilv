package com.yilvtzj.service.impl;

import java.util.Map;

import com.common.okhttp.HttpConnect;
import com.google.gson.reflect.TypeToken;
import com.yilvtzj.entity.Account;
import com.yilvtzj.service.IUserService;
import com.yilvtzj.service.ServiceListener;

public class UserService implements IUserService {

	private static final UserService service = new UserService();

	private UserService() {
	}

	public static UserService newInstance() {
		return service;
	}

	@Override
	public void login(ServiceListener<Account> listener, Map<String, Object> param) {
		HttpConnect.getInstance().connect(listener, loginUrl, param, new TypeToken<Account>() {
		});
	}
}
