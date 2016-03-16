package com.yilvtzj.activity.fragment;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yilvtzj.R;
import com.yilvtzj.activity.LoginActivity;
import com.yilvtzj.activity.friend.SearchFriendActivity;
import com.yilvtzj.activity.mine.MyQrActivity;
import com.yilvtzj.entity.Account;
import com.yilvtzj.http.PostThread.PostThreadListener;
import com.yilvtzj.service.UserService;
import com.yilvtzj.util.AccountUtil;
import com.yilvtzj.util.ActivityUtil;
import com.yilvtzj.util.JSONHelper;
import com.yilvtzj.util.StringUtil;

public class FragmentMine extends Fragment implements OnClickListener {
	private TextView nicknameTV;
	private Account account;

	private UserService userService = UserService.newInstance();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.fragment_mine, container, false);
		initView(view);
		return view;
	}

	@Override
	public void onStart() {
		super.onStart();
		refreshInfo();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.loginBtn:
			ActivityUtil.startActivity(new Intent(), getActivity(), LoginActivity.class);
			break;
		case R.id.myqr:
			ActivityUtil.startActivity(new Intent(), this.getActivity(), MyQrActivity.class);
			break;
		case R.id.searchfriends:
			ActivityUtil.startActivity(new Intent(), getActivity(), SearchFriendActivity.class);
			break;
		}
	}

	/**
	 * 获得用户登录信息,并显示
	 */
	private void refreshInfo() {
		account = AccountUtil.getAccount();
		if (account == null) {
			new Thread(new Runnable() {

				@Override
				public void run() {
					try {
						userService.getUserInfo(postThreadListener);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}).start();
		} else {
			showInfo(account);
		}
	}

	private void initView(View view) {
		nicknameTV = (TextView) view.findViewById(R.id.nickname);

		view.findViewById(R.id.myqr).setOnClickListener(this);
		view.findViewById(R.id.loginBtn).setOnClickListener(this);
		view.findViewById(R.id.searchfriends).setOnClickListener(this);
	}

	private void showInfo(Account account) {
		if (account == null) {
			return;
		}
		if (!StringUtil.isEmpty(account.getNickname())) {
			nicknameTV.setText(account.getNickname());
		}
	}

	private PostThreadListener postThreadListener = new PostThreadListener() {

		@Override
		public boolean postThreadSuccess(JSONObject JSON) throws JSONException {
			account = JSONHelper.JSONToBean(JSON, Account.class);
			showInfo(account);
			AccountUtil.setAccount(getActivity(), account);
			return false;
		}

		@Override
		public boolean postThreadFinally() {
			return false;
		}

		@Override
		public boolean postThreadFailed() {
			return false;
		}
	};

}
