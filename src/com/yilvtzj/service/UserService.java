package com.yilvtzj.service;

import com.yilvtzj.http.PostThread;
import com.yilvtzj.http.PostThread.PostThreadListener;
import com.yilvtzj.util.Global;

public class UserService {
	private static final String getUserInfo = Global.getServletUrl("/travel/getuserinfo");

	private static final UserService service = new UserService();

	private UserService() {
	}

	public static UserService newInstance() {
		return service;
	}

	public void getUserInfo(PostThreadListener listener) {
		new Thread(new PostThread(null, getUserInfo, null).setListener(listener)).start();
	}

	public void login(PostThreadListener listener) {
		new Thread(new PostThread(null, getUserInfo, null).setListener(listener)).start();
	}
}
