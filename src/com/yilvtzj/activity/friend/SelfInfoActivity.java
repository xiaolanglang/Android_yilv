package com.yilvtzj.activity.friend;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.yilvtzj.R;
import com.yilvtzj.activity.chat.ChatActivity;
import com.yilvtzj.activity.common.MyActivity;
import com.yilvtzj.entity.Account;
import com.yilvtzj.util.ActivityUtil;
import com.yilvtzj.util.ToastUtil;

public class SelfInfoActivity extends MyActivity implements OnClickListener {
	private Button addFriendBtn, sendMesgBtn;
	private TextView nickname, mark;
	private Account account;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_self_info);

		boolean flag = getAccount();
		if (!flag) {
			return;
		}
		setCommonActionBar(account.getNickname() + "的信息");
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

	private boolean getAccount() {
		Bundle bundle = getIntent().getExtras();
		if (bundle == null) {
			finish();
			return false;
		}
		Object ac = bundle.get("account");
		if (ac == null) {
			finish();
			return false;
		}
		account = (Account) ac;
		return true;
	}

	private void initView() {
		addFriendBtn = (Button) findViewById(R.id.sendmsg);
		sendMesgBtn = (Button) findViewById(R.id.addfriend);
		nickname = (TextView) findViewById(R.id.nickname);
		mark = (TextView) findViewById(R.id.mark);

		addFriendBtn.setOnClickListener(this);
		sendMesgBtn.setOnClickListener(this);

		nickname.setText(account.getNickname());
		mark.setText(account.getNickname());
	}

	private void sendMsg() {
		ActivityUtil.startActivity(new Intent(), this, ChatActivity.class, true);
	}

	private void addFriend() {
		ToastUtil.show(this, "添加好友", null);
	}
}
