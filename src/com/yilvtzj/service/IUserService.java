package com.yilvtzj.service;

import java.util.Map;

import com.common.util.Global;
import com.yilvtzj.entity.Account;

public interface IUserService {
	static String loginUrl = Global.getServletUrl("/travel/login");
	static String loginOutUrl = Global.getServletUrl("/travel/loginout");

	public void login(ServiceListener<Account> listener, Map<String, Object> param);

}
