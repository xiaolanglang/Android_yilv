package com.yilvtzj.service;

import java.util.Map;

import com.common.util.Global;
import com.yilvtzj.entity.Account;
import com.yilvtzj.entity.DataResult;
import com.yilvtzj.entity.Result;

public interface IFriendService {
	static String searchFriendUrl = Global.getServletUrl("/travel/account/list");
	static String checkFriendUrl = Global.getServletUrl("/travel/account/list");

	public void searchFriend(Map<String, Object> param, ServiceListener<DataResult<Account>> listener);

	public void checkFriend(Map<String, Object> param, ServiceListener<Result> listener);
}
