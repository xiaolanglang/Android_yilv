package com.yilvtzj.activity.mine;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.common.util.AccountUtil;
import com.common.util.StringUtil;
import com.common.util.ToastUtil;
import com.yilvtzj.R;
import com.yilvtzj.entity.Account;
import com.yilvtzj.service.IUserService;
import com.yilvtzj.service.ServiceListener;
import com.yilvtzj.service.impl.UserService;

public class LoginActivity extends Activity implements OnClickListener {
	private EditText username, password;
	private Button login, register;
	private IUserService userService = UserService.newInstance();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		if (AccountUtil.getAccount() != null) {
			finish();
			return;
		}

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		initView();
		initListener();
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

	private void initListener() {
		login.setOnClickListener(this);
		register.setOnClickListener(this);

		TextWatcher textWatcher = new MyTextWatcher();
		username.addTextChangedListener(textWatcher);
		password.addTextChangedListener(textWatcher);
	}

	private void initView() {
		username = (EditText) findViewById(R.id.username);
		password = (EditText) findViewById(R.id.password);
		login = (Button) findViewById(R.id.login);
		register = (Button) findViewById(R.id.register);

	}

	private ServiceListener<Account> serviceResult = new ServiceListener<Account>() {

		@Override
		public void preExecute() {
			login.setEnabled(false);
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
			login.setEnabled(true);
		}

		@Override
		public void onFailed(int code, String message) {
			ToastUtil.show(LoginActivity.this, message, null);
		}
	};

	private class MyTextWatcher implements TextWatcher {

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {

		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {

		}

		@Override
		public void afterTextChanged(Editable s) {
			int name = username.getText().length();
			int pas = password.getText().length();
			if (name > 0 && pas >= 6) {
				login.setEnabled(true);
			} else {
				login.setEnabled(false);
			}
		}

	}

}
