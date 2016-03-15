package com.yilvtzj.service;

import com.yilvtzj.http.SocketHttpRequester;
import com.yilvtzj.http.SocketHttpRequester.SocketListener;
import com.yilvtzj.util.Global;

public class DongTaiService {
	private static final String getDongtaiList = Global.getServletUrl("/travel/dongtai/list");

	public static void getDongtaiList(SocketListener socketListener) throws Exception {
		new SocketHttpRequester().setSocketListener(socketListener).post(getDongtaiList, null);
	}
}
