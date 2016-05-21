package com.yilvtzj.activity.mine;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import com.common.okhttp.cookie.CookiesManager;
import com.common.util.AccountUtil;
import com.yilvtzj.R;
import com.yilvtzj.activity.common.BaseActivity;

public class SetActivity extends BaseActivity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_set);

		initView();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.loginout:
			new CookiesManager(SetActivity.this).removeAll();
			AccountUtil.setAccount(null);
			finish();
			break;

		}
	}

	private void initView() {
		if (AccountUtil.getAccount() != null) {
			LinearLayout loginout = (LinearLayout) findViewById(R.id.loginout);
			loginout.setOnClickListener(this);
			loginout.setVisibility(View.VISIBLE);
		}

	}

}
