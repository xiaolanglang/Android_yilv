package com.yilvtzj.service;

import java.util.HashMap;
import java.util.Map;

import com.yilvtzj.http.SocketHttpRequester;
import com.yilvtzj.http.SocketHttpRequester.SocketListener;
import com.yilvtzj.util.Global;

public class DongTaiCommentService {
	private final static String save = Global.getServletUrl("/travel/dongtai/comment/save");
	private final static String list = Global.getServletUrl("/travel/dongtai/comment/list");

	public static void saveComment(SocketListener socketListener, Map<String, String> params) throws Exception {
		new SocketHttpRequester().setSocketListener(socketListener).post(save, params);
	}

	public static void getList(SocketListener socketListener, int pageNum, String dongtaiId) throws Exception {
		Map<String, String> params = new HashMap<>();
		params.put("pageNum", String.valueOf(pageNum));
		params.put("dongtaiId", dongtaiId);
		new SocketHttpRequester().setSocketListener(socketListener).post(list, params);
	}
}
