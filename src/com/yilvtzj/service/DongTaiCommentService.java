package com.yilvtzj.service;

import java.util.HashMap;
import java.util.Map;

import android.app.Dialog;

import com.yilvtzj.http.PostThread;
import com.yilvtzj.http.PostThread.PostThreadListener;
import com.yilvtzj.util.Global;

public class DongTaiCommentService {
	private final static String saveUrl = Global.getServletUrl("/travel/dongtai/comment/save");
	private final static String listUrl = Global.getServletUrl("/travel/dongtai/comment/list");
	private static final DongTaiCommentService service = new DongTaiCommentService();

	private DongTaiCommentService() {
	}

	public static DongTaiCommentService newInstance() {
		return service;
	}

	public void saveComment(PostThreadListener listener, Map<String, String> params, Dialog dialog) {
		new Thread(new PostThread(params, saveUrl, dialog).setListener(listener)).start();
	}

	public void getList(PostThreadListener listener, int pageNum, String dongtaiId) {
		Map<String, String> params = new HashMap<>();
		params.put("pageNum", String.valueOf(pageNum));
		params.put("dongtaiId", dongtaiId);
		new Thread(new PostThread(params, listUrl, null).setListener(listener)).start();
	}
}
