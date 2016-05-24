package com.yilvtzj.activity.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.common.util.AccountUtil;
import com.common.util.ActivityUtil;
import com.common.util.StringUtil;
import com.yilvtzj.R;
import com.yilvtzj.activity.LoginActivity;
import com.yilvtzj.activity.MyQrActivity;
import com.yilvtzj.activity.SearchFriendActivity;
import com.yilvtzj.activity.SettingActivity;
import com.yilvtzj.entity.Account;

public class FragmentMine extends Fragment implements OnClickListener {
	private TextView nicknameTV;
	private Account account;

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
			Account account = AccountUtil.getAccount();
			if (account == null) {
				ActivityUtil.startActivity(new Intent(), getActivity(), LoginActivity.class);
			}
			break;
		case R.id.myqr:
			ActivityUtil.startActivity(new Intent(), this.getActivity(), MyQrActivity.class);
			break;
		case R.id.searchfriends:
			ActivityUtil.startActivity(new Intent(), getActivity(), SearchFriendActivity.class);
			break;
		case R.id.setting:
			ActivityUtil.startActivity(new Intent(), getActivity(), SettingActivity.class);
			break;
		}
	}

	/**
	 * 获得用户登录信息,并显示
	 */
	private void refreshInfo() {
		account = AccountUtil.getAccount();
		showInfo(account);
	}

	private void initView(View view) {
		nicknameTV = (TextView) view.findViewById(R.id.nickname);

		view.findViewById(R.id.myqr).setOnClickListener(this);
		view.findViewById(R.id.loginBtn).setOnClickListener(this);
		view.findViewById(R.id.searchfriends).setOnClickListener(this);
		view.findViewById(R.id.setting).setOnClickListener(this);
	}

	private void showInfo(Account account) {
		if (account == null) {
			nicknameTV.setText(getString(R.string.login_more));
			return;
		}
		if (!StringUtil.isEmpty(account.getNickname())) {
			nicknameTV.setText(account.getNickname());
		}
	}

}
