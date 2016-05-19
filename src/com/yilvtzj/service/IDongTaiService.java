package com.yilvtzj.service;

import java.util.Map;

import com.common.util.Global;
import com.yilvtzj.entity.DataResult;
import com.yilvtzj.entity.DongtaiComment;
import com.yilvtzj.entity.DongtaiMsg;
import com.yilvtzj.entity.Result;

public interface IDongTaiService {
	static String getDongtaiList = Global.getServletUrl("/travel/dongtai/list");
	static String saveUrl = Global.getServletUrl("/travel/dongtai/comment/save");
	static String listUrl = Global.getServletUrl("/travel/dongtai/comment/list");
	static String goodSave = Global.getServletUrl("/travel/dongtai/good/save");

	/**
	 * 获得首页的用户动态
	 * 
	 * @param listener
	 */
	public void getDongtaiList(ServiceListener<DataResult<DongtaiMsg>> listener);

	/**
	 * 动态点赞
	 * 
	 * @param listener
	 * @param params
	 */
	public void saveGood(ServiceListener<Result> listener, Map<String, Object> params);

	/**
	 * 保存动态的评论
	 * 
	 * @param listener
	 * @param params
	 */
	public void saveComment(ServiceListener<Result> listener, Map<String, Object> params);

	/**
	 * 获得动态的评论列表
	 * 
	 * @param listener
	 * @param params
	 */
	public void getList(ServiceListener<DataResult<DongtaiComment>> listener, Map<String, Object> params);
}
