package com.yilvtzj.activity.common;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.common.util.ActivityUtil;
import com.yilvtzj.R;
import com.yilvtzj.view.LoadingDialog;

public class BaseActivity extends Activity {
	private TextView titleTV;
	private RelativeLayout backLay;
	private LoadingDialog loadingDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initDialog();
	}

	private void initDialog() {
		loadingDialog = new LoadingDialog(this);
		loadingDialog.setCanceledOnTouchOutside(false);
	}

	protected void setCommonActionBar(String title) {
		titleTV = (TextView) findViewById(R.id.title);
		backLay = (RelativeLayout) findViewById(R.id.back);
		titleTV.setText(title);
		backLay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ActivityUtil.closeActivity(BaseActivity.this);
			}
		});
	}

	protected void showLoadingDialog() {
		loadingDialog.show();
	}

	protected void cancelLoadingDialog() {
		loadingDialog.cancel();
	}

	protected void finishActivity() {
		ActivityUtil.closeActivity(BaseActivity.this);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			ActivityUtil.closeActivity(this);
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}
}
