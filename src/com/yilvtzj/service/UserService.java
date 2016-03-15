package com.yilvtzj.service;

import com.yilvtzj.http.SocketHttpRequester;
import com.yilvtzj.http.SocketHttpRequester.SocketListener;
import com.yilvtzj.util.Global;

public class UserService {
	private static final String getUserInfo = Global.getServletUrl("/travel/getuserinfo");

	public static void getUserInfo(SocketListener socketListener) throws Exception {
		new SocketHttpRequester().setSocketListener(socketListener).post(getUserInfo, null);
	}
}
