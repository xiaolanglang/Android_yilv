package com.yilvtzj.activity.common;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yilvtzj.R;
import com.yilvtzj.util.ActivityUtil;

public class MyActivity extends Activity {
	private TextView titleTV;
	private RelativeLayout backLay;

	protected void setCommonActionBar(String title) {
		titleTV = (TextView) findViewById(R.id.title);
		backLay = (RelativeLayout) findViewById(R.id.back);
		titleTV.setText(title);
		backLay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ActivityUtil.closeActivity(MyActivity.this);
			}
		});
	}
}
