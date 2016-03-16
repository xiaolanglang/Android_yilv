package com.yilvtzj.service;

import java.util.Map;

import com.yilvtzj.http.PostThread;
import com.yilvtzj.http.PostThread.PostThreadListener;
import com.yilvtzj.util.Global;

public class DongTaiGoodService {
	private final static String goodSave = Global.getServletUrl("/travel/dongtai/good/save");
	private static final DongTaiGoodService service = new DongTaiGoodService();

	private DongTaiGoodService() {
	}

	public static DongTaiGoodService newInstance() {
		return service;
	}

	public void saveGood(PostThreadListener listener, Map<String, String> params) {
		new Thread(new PostThread(params, goodSave, null).setListener(listener)).start();
	}

}
