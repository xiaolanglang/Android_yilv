package com.yilvtzj.service.impl;

import java.util.Map;

import com.common.okhttp.HttpConnect;
import com.google.gson.reflect.TypeToken;
import com.yilvtzj.entity.DataResult;
import com.yilvtzj.entity.DongtaiComment;
import com.yilvtzj.entity.DongtaiMsg;
import com.yilvtzj.entity.Result;
import com.yilvtzj.service.IDongTaiService;
import com.yilvtzj.service.ServiceListener;

public class DongTaiService implements IDongTaiService {

	private static final DongTaiService service = new DongTaiService();

	private DongTaiService() {
	}

	public static DongTaiService newInstance() {
		return service;
	}

	@Override
	public void getDongtaiList(ServiceListener<DataResult<DongtaiMsg>> listener) {
		HttpConnect.getInstance().connect(listener, getDongtaiList, null, new TypeToken<DataResult<DongtaiMsg>>() {
		});
	}

	@Override
	public void saveGood(ServiceListener<Result> listener, Map<String, Object> params) {
		HttpConnect.getInstance().connect(listener, goodSave, params, new TypeToken<Result>() {
		});
	}

	@Override
	public void saveComment(ServiceListener<Result> listener, Map<String, Object> params) {
		listener.preExecute();
		HttpConnect.getInstance().connect(listener, saveUrl, params, new TypeToken<Result>() {
		});
	}

	@Override
	public void getList(ServiceListener<DataResult<DongtaiComment>> listener, Map<String, Object> params) {
		HttpConnect.getInstance().connect(listener, listUrl, params, new TypeToken<DataResult<DongtaiComment>>() {
		});
	}
}
