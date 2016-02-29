package com.yilvtzj.service;

import java.util.Map;

import android.content.Context;

import com.yilvtzj.http.SocketHttpRequester;
import com.yilvtzj.http.SocketHttpRequester.SocketListener;
import com.yilvtzj.util.Global;

public class DongTaiGoodService {
	private final static String goodSave = Global.getServletUrl("/travel/dongtaigood/save");

	public static boolean saveGood(SocketListener socketListener, Context context, Map<String, String> params) throws Exception {
		return new SocketHttpRequester().setSocketListener(socketListener).post(goodSave, context, params);
	}

}
