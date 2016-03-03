package com.yilvtzj.activity.dongtai;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.yilvtzj.R;
import com.yilvtzj.activity.common.MyActivity;
import com.yilvtzj.http.SocketHttpRequester.SocketListener;
import com.yilvtzj.service.DongTaiCommentService;
import com.yilvtzj.util.ActivityUtil;
import com.yilvtzj.util.LoadingDialogUtil;
import com.yilvtzj.util.SimpleHandler;
import com.yilvtzj.view.LoadingDialog;

public class CommentWriteActivity extends MyActivity implements OnClickListener {
	private TextView cancelTv, sendTv;
	private EditText content;
	private String dongTaiId;
	private InputMethodManager imm;
	private boolean send = true;
	private LoadingDialog dialog;
	private SimpleHandler simpleHandler = new SimpleHandler(CommentWriteActivity.this);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dongtai_comment_write);
		dongTaiId = getIntent().getExtras().getString("dongTaiId");
		imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		dialog = LoadingDialogUtil.get(CommentWriteActivity.this);

		initView();
	}

	private void initView() {
		cancelTv = (TextView) findViewById(R.id.cancel);
		sendTv = (TextView) findViewById(R.id.send);
		content = (EditText) findViewById(R.id.content);

		cancelTv.setOnClickListener(this);
		sendTv.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.cancel:
			close();
			break;
		case R.id.send:
			send();
			break;

		}
	}

	private void send() {
		if (send) {
			dialog.show();
			new Thread(new SendHread()).start();
		}
	}

	private void close() {
		imm.hideSoftInputFromWindow(content.getWindowToken(), 0);
		ActivityUtil.closeActivity(this);
	}

	private class SendHread implements Runnable {

		private void canReSend() {
			if (dialog != null) {
				dialog.cancel();
			}
			simpleHandler.sendMessage("评论失败");
			send = true;
		}

		@Override
		public void run() {
			send = false;
			Map<String, String> params = new HashMap<>();
			params.put("dongtaiId", dongTaiId);
			params.put("content", content.getText().toString());
			try {
				DongTaiCommentService.saveComment(new SocketListener() {

					@Override
					public void result(String JSON) {
						if (JSON.indexOf("200") != -1) {
							dialog.cancel();
							close();
						} else {
							canReSend();
						}
					}
				}, CommentWriteActivity.this, params);
			} catch (Exception e) {
				canReSend();
				e.printStackTrace();
			}
		}
	}

}
