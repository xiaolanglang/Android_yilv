package com.yilvtzj.activity.mine;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.yilvtzj.R;
import com.yilvtzj.activity.chat.ChatActivity;
import com.yilvtzj.activity.common.MyActivity;
import com.yilvtzj.util.ActivityUtil;
import com.yilvtzj.util.ToastUtil;

public class SelfInfoActivity extends MyActivity implements OnClickListener {
	private Button addFriendBtn, sendMesgBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_self_info);
		setCommonActionBar("咖喱饭的信息");

		initView();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.sendmsg:
			sendMsg();
			break;
		case R.id.addfriend:
			addFriend();
			break;
		}

	}

	private void initView() {
		addFriendBtn = (Button) findViewById(R.id.sendmsg);
		sendMesgBtn = (Button) findViewById(R.id.addfriend);

		addFriendBtn.setOnClickListener(this);
		sendMesgBtn.setOnClickListener(this);
	}

	private void sendMsg() {
		ActivityUtil.startActivity(new Intent(), this, ChatActivity.class, true);
	}

	private void addFriend() {
		ToastUtil.show(this, "添加好友", null);
	}
}
