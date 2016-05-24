package com.yilvtzj.activity;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.common.util.ToastUtil;
import com.yilvtzj.R;
import com.yilvtzj.activity.common.BaseActivity;
import com.yilvtzj.entity.Result;
import com.yilvtzj.service.IDongTaiService;
import com.yilvtzj.service.ServiceListener;
import com.yilvtzj.service.impl.DongTaiService;

public class CommentWriteActivity extends BaseActivity implements OnClickListener {
	private TextView cancelTv, sendTv;
	private EditText content;
	private String dongTaiId;
	private InputMethodManager imm;
	private boolean send = true;

	private IDongTaiService commentService = DongTaiService.newInstance();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dongtai_comment_write);
		dongTaiId = getIntent().getExtras().getString("dongTaiId");
		imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

		initView();
	}

	private void initView() {
		cancelTv = (TextView) findViewById(R.id.cancle);
		sendTv = (TextView) findViewById(R.id.send);
		content = (EditText) findViewById(R.id.content);

		cancelTv.setOnClickListener(this);
		sendTv.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.cancle:
			imm.hideSoftInputFromWindow(content.getWindowToken(), 0);
			finishActivity();
			break;
		case R.id.send:
			send();
			break;

		}
	}

	private void send() {
		if (send) {
			Map<String, Object> params = new HashMap<>();
			params.put("dongtaiId", dongTaiId);
			params.put("content", content.getText().toString());
			commentService.saveComment(listener, params);
		}
	}

	private ServiceListener<Result> listener = new ServiceListener<Result>() {

		@Override
		public void preExecute() {
			showLoadingDialog();
		}

		@Override
		public void onSuccess(Result result) {
			if (result.getCode() == 200) {
				imm.hideSoftInputFromWindow(content.getWindowToken(), 0);
				finishActivity();
			}
		}

		@Override
		public void onFinally() {
			send = true;
			cancelLoadingDialog();
		}

		@Override
		public void onFailed(int code, String message) {
			ToastUtil.show(CommentWriteActivity.this, "评论失败", null);
		}
	};

}
