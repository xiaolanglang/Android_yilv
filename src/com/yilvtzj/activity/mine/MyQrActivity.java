package com.yilvtzj.activity.mine;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;

import com.google.zxing.WriterException;
import com.yilvtzj.R;
import com.yilvtzj.activity.common.MyActivity;
import com.yilvtzj.util.ImageUtil;
import com.yilvtzj.util.Utils;

public class MyQrActivity extends MyActivity {
	private ImageView qrIv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_myqr);
		setCommonActionBar("我的二维码");

		initView();
	}

	@Override
	protected void onStart() {
		super.onStart();
		initQr();
	}

	private void initView() {
		qrIv = (ImageView) findViewById(R.id.qr);
	}

	private void initQr() {
		Bitmap bitmap = null;
		try {
			bitmap = Utils.createQRImage("23123", ImageUtil.readBitMap(this, R.drawable.ic_launcher_logo), 50);
		} catch (WriterException e) {
			e.printStackTrace();
		}
		qrIv.setImageBitmap(bitmap);
	}
}
