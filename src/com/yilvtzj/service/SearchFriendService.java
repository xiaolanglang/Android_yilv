package com.yilvtzj.service;

import java.util.HashMap;
import java.util.Map;

import android.app.Dialog;

import com.yilvtzj.http.PostThread;
import com.yilvtzj.http.PostThread.PostThreadListener;
import com.yilvtzj.util.Global;

public class SearchFriendService {
	private final static String listUrl = Global.getServletUrl("/travel/account/list");
	private final static SearchFriendService service = new SearchFriendService();

	private SearchFriendService() {
	}

	public static SearchFriendService newInstance() {
		return service;
	}

	public void getList(int pageNum, String searchMsg, PostThreadListener listener, Dialog dialog) {
		Map<String, String> params = new HashMap<>();
		params.put("pageNum", String.valueOf(pageNum));
		params.put("nickname", searchMsg);
		new Thread(new PostThread(params, listUrl, dialog).setListener(listener)).start();
	}
}
