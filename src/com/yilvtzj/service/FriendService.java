package com.yilvtzj.service;

import java.util.HashMap;
import java.util.Map;

import android.app.Dialog;

import com.yilvtzj.http.PostThread;
import com.yilvtzj.http.PostThread.PostThreadListener;
import com.yilvtzj.util.Global;

public class FriendService {
	private final static String searchFriendUrl = Global.getServletUrl("/travel/account/list");
	private final static String checkFriendUrl = Global.getServletUrl("/travel/account/list");
	private final static FriendService service = new FriendService();

	private FriendService() {
	}

	public static FriendService newInstance() {
		return service;
	}

	public void searchFriend(int pageNum, String searchMsg, PostThreadListener listener, Dialog dialog) {
		Map<String, String> params = new HashMap<>();
		params.put("pageNum", String.valueOf(pageNum));
		params.put("nickname", searchMsg);
		new Thread(new PostThread(params, searchFriendUrl, dialog).setListener(listener)).start();
	}

	public void checkFriend(String id, PostThreadListener listener, Dialog dialog) {
		Map<String, String> params = new HashMap<>();
		params.put("friendId", id);
		new Thread(new PostThread(params, checkFriendUrl, dialog).setListener(listener)).start();
	}
}
