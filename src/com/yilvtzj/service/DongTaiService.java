package com.yilvtzj.service;

import com.yilvtzj.http.PostThread;
import com.yilvtzj.http.PostThread.PostThreadListener;
import com.yilvtzj.util.Global;

public class DongTaiService {
	private static final String getDongtaiList = Global.getServletUrl("/travel/dongtai/list");

	private static final DongTaiService service = new DongTaiService();

	private DongTaiService() {
	}

	public static DongTaiService newInstance() {
		return service;
	}

	public void getDongtaiList(PostThreadListener listener) {
		new Thread(new PostThread(null, getDongtaiList, null).setListener(listener)).start();
	}
}
