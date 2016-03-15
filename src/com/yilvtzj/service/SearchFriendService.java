package com.yilvtzj.service;

import java.util.HashMap;
import java.util.Map;

import com.yilvtzj.http.SocketHttpRequester;
import com.yilvtzj.http.SocketHttpRequester.SocketListener;
import com.yilvtzj.util.Global;

public class SearchFriendService {
	private final static String list = Global.getServletUrl("/travel/account/list");

	public static void getList(SocketListener socketListener, int pageNum, String searchMsg) throws Exception {
		Map<String, String> params = new HashMap<>();
		params.put("pageNum", String.valueOf(pageNum));
		params.put("nickname", searchMsg);
		new SocketHttpRequester().setSocketListener(socketListener).post(list, params);
	}
}
