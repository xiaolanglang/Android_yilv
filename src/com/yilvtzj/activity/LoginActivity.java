package com.yilvtzj.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.yilvtzj.R;
import com.yilvtzj.http.PostThread.PostThreadListener;
import com.yilvtzj.service.UserService;

public class LoginActivity extends Activity implements OnClickListener, PostThreadListener {
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
			userService.login(this);
			break;
		case R.id.register:

			break;
		}
	}

	@Override
	public boolean postThreadSuccess(JSONObject JSON) throws JSONException {
		return false;
	}

	@Override
	public boolean postThreadFailed() {
		return false;
	}

	@Override
	public boolean postThreadFinally() {
		return false;
	}

}
