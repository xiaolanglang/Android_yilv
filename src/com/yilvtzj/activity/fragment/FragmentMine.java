package com.yilvtzj.activity.fragment;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yilvtzj.R;
import com.yilvtzj.activity.LoginActivity;
import com.yilvtzj.activity.mine.MyQrActivity;
import com.yilvtzj.entity.Account;
import com.yilvtzj.http.SocketHttpRequester.SocketListener;
import com.yilvtzj.service.UserService;
import com.yilvtzj.util.AccountUtil;
import com.yilvtzj.util.ActivityUtil;
import com.yilvtzj.util.JSONHelper;
import com.yilvtzj.util.StringUtil;
import com.yilvtzj.util.ToastUtil;

public class FragmentMine extends Fragment implements OnClickListener, SocketListener {
	private ImageView loginBtn;
	private SocketListener socketListener;
	private TextView nicknameTV;
	private final int SHOWINFO = 0;
	private Account account;
	private LinearLayout myqr;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.fragment_mine, container, false);
		initView(view);
		socketListener = this;
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
		}
	}

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case SHOWINFO:
				showInfo(account);
				break;
			}
		};
	};

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
						UserService.getUserInfo(socketListener, getActivity());
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
		loginBtn = (ImageView) view.findViewById(R.id.loginBtn);
		nicknameTV = (TextView) view.findViewById(R.id.nickname);
		myqr = (LinearLayout) view.findViewById(R.id.myqr);

		loginBtn.setOnClickListener(this);
		myqr.setOnClickListener(this);
	}

	private void showInfo(Account account) {
		if (account == null) {
			return;
		}
		if (!StringUtil.isEmpty(account.getNickname())) {
			nicknameTV.setText(account.getNickname());
			ToastUtil.show(getActivity(), account.getNickname(), null);
		}
	}

	@Override
	public void result(String JSON) {
		try {
			account = JSONHelper.JSONToBean(new JSONObject(JSON), Account.class);
			handler.sendEmptyMessage(SHOWINFO);
			AccountUtil.setAccount(getActivity(), account);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

}
