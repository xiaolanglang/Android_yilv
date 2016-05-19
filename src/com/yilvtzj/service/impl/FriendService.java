package com.yilvtzj.service.impl;

import java.util.Map;

import com.common.okhttp.HttpConnect;
import com.google.gson.reflect.TypeToken;
import com.yilvtzj.entity.Account;
import com.yilvtzj.entity.DataResult;
import com.yilvtzj.entity.Result;
import com.yilvtzj.service.IFriendService;
import com.yilvtzj.service.ServiceListener;

public class FriendService implements IFriendService {
	private final static FriendService service = new FriendService();

	private FriendService() {
	}

	public static FriendService newInstance() {
		return service;
	}

	@Override
	public void searchFriend(Map<String, Object> param, ServiceListener<DataResult<Account>> listener) {
		HttpConnect.getInstance().connect(listener, searchFriendUrl, param, new TypeToken<DataResult<Account>>() {
		});
	}

	@Override
	public void checkFriend(Map<String, Object> param, ServiceListener<Result> listener) {
		HttpConnect.getInstance().connect(listener, checkFriendUrl, param, new TypeToken<Result>() {
		});
	}
}
