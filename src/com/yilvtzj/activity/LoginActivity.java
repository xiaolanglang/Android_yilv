package com.yilvtzj.activity;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.common.util.AccountUtil;
import com.common.util.StringUtil;
import com.common.util.ToastUtil;
import com.yilvtzj.R;
import com.yilvtzj.entity.Account;
import com.yilvtzj.service.ServiceListener;
import com.yilvtzj.service.impl.UserService;

public class LoginActivity extends Activity implements OnClickListener {
	private EditText username, password;
	private Button login, register;
	private UserService userService = UserService.newInstance();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		initView();
		initListener();
	}

	private void initListener() {
		login.setOnClickListener(this);
		register.setOnClickListener(this);
	}

	private void initView() {
		username = (EditText) findViewById(R.id.username);
		password = (EditText) findViewById(R.id.password);
		login = (Button) findViewById(R.id.login);
		register = (Button) findViewById(R.id.register);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.login:
			Map<String, Object> param = new HashMap<>();
			param.put("username", username.getText().toString());
			param.put("password", password.getText().toString());
			userService.login(serviceResult, param);
			break;
		case R.id.register:

			break;
		}
	}

	private ServiceListener<Account> serviceResult = new ServiceListener<Account>() {

		@Override
		public void preExecute() {

		}

		@Override
		public void onSuccess(Account account) {
			if (StringUtil.isEmpty(account.getId())) {
				ToastUtil.show(LoginActivity.this, account.getMessage(), null);
			} else {
				AccountUtil.setAccount(account);
				finish();
			}

		}

		@Override
		public void onFinally() {

		}

		@Override
		public void onFailed(int code, String message) {

		}
	};

}
