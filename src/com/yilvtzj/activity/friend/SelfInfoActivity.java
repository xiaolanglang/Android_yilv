package com.yilvtzj.activity.friend;

import org.json.JSONException;
import org.json.JSONObject;

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
import com.yilvtzj.http.PostThread.PostThreadListener;
import com.yilvtzj.service.FriendService;
import com.yilvtzj.util.ActivityUtil;
import com.yilvtzj.util.ToastUtil;
import com.yilvtzj.view.LoadingDialog;

public class SelfInfoActivity extends MyActivity implements OnClickListener {
	private Button addFriendBtn, sendMesgBtn;
	private TextView nickname, mark;
	private Account account;
	private LoadingDialog loadingDialog;

	private FriendService friendService = FriendService.newInstance();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_self_info);
		loadingDialog = new LoadingDialog(this);

		boolean flag = getAccount();
		if (!flag) {
			return;
		}
		setCommonActionBar(account.getNickname() + "的信息");
		initView();

		checkFriend();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.sendmsg:
			ActivityUtil.startActivity(new Intent(), this, ChatActivity.class, true);
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

	private void checkFriend() {
		friendService.checkFriend(account.getId(), listener, loadingDialog);
	}

	private PostThreadListener listener = new PostThreadListener() {

		@Override
		public boolean postThreadSuccess(JSONObject JSON) throws JSONException {
			int type = JSON.getInt("type");
			switch (type) {
			case 0:
				// 搜索到的是自己
				break;
			case 1:
				// 搜索到了自己的好友
				sendMesgBtn.setVisibility(View.VISIBLE);
				break;
			case 2:
				// 搜索到的不是好友
				addFriendBtn.setVisibility(View.VISIBLE);
				break;
			}
			return false;
		}

		@Override
		public boolean postThreadFinally() {
			return false;
		}

		@Override
		public boolean postThreadFailed() {
			ToastUtil.show(SelfInfoActivity.this, "获取信息失败", null);
			return false;
		}
	};

	private void addFriend() {
		ToastUtil.show(this, "添加好友", null);
	}
}
