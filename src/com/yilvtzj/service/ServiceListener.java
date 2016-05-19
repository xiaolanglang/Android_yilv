package com.yilvtzj.service;


public interface ServiceListener<Result> {
	void preExecute();

	void onSuccess(Result result);

	void onFailed(int code, String message);

	void onFinally();
}
