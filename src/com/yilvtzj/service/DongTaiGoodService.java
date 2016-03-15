package com.yilvtzj.service;

import java.util.Map;

import com.yilvtzj.http.SocketHttpRequester;
import com.yilvtzj.http.SocketHttpRequester.SocketListener;
import com.yilvtzj.util.Global;

public class DongTaiGoodService {
	private final static String goodSave = Global.getServletUrl("/travel/dongtai/good/save");

	public static void saveGood(SocketListener socketListener, Map<String, String> params) throws Exception {
		new SocketHttpRequester().setSocketListener(socketListener).post(goodSave, params);
	}

}
