package com.yilvtzj.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.common.baidumap.MyAddress;
import com.common.util.DicUtil;
import com.common.util.PhotoGalleryUtil;
import com.common.util.ToastUtil;
import com.yilvtzj.R;
import com.yilvtzj.activity.common.BaseActivity;
import com.yilvtzj.adapter.GridAdapter;
import com.yilvtzj.entity.Result;
import com.yilvtzj.service.IDongTaiService;
import com.yilvtzj.service.ServiceListener;
import com.yilvtzj.service.impl.DongTaiService;
import com.yilvtzj.view.NoScrolGridView;

public class WriteDongtaiActivity extends BaseActivity implements OnClickListener {

	private FrameLayout flImage, flPisition;
	private TextView tv, send, cancle;
	private EditText content;
	private ImageView imageView;
	boolean positionFlag = true, sendFlag = true;
	private Map<String, Object> params = new HashMap<String, Object>();
	private List<File> files = new ArrayList<>(9);
	private boolean finish = false;
	private IDongTaiService dongTaiService = DongTaiService.newInstance();
	private GridAdapter wallAdapter;
	private NoScrolGridView gridView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		pickBuilder();

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dongtai_write);

		initView();

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.selectImage:
			PhotoGalleryUtil.pickBuilder(this);
			break;
		case R.id.selectPosition:
			selectPosition();
			break;
		case R.id.send:
			send();
			break;
		case R.id.cancle:
			finishActivity();
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		List<String> listTemp = PhotoGalleryUtil.getImagePaths(requestCode, resultCode, data, RESULT_OK);
		if (listTemp != null && listTemp.size() > 0) {
			files.clear();
			List<Bitmap> bitmaps = new ArrayList<>(9);
			for (String path : listTemp) {
				files.add(new File(path));
				Bitmap bitmap = BitmapFactory.decodeFile(path, getBitmapOption(2)); // 将图片的长和宽缩小味原来的1/2
				bitmaps.add(bitmap);
			}
			wallAdapter.setBitmaps(bitmaps);
			wallAdapter.notifyDataSetChanged();
		} else {
			if (finish) {
				finish();
			}
		}

	}

	private Options getBitmapOption(int inSampleSize) {
		System.gc();
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inPurgeable = true;
		options.inSampleSize = inSampleSize;
		return options;
	}

	private void pickBuilder() {
		Intent intent = getIntent();
		String imgFlag = intent.getStringExtra("imgFlag");
		if ("true".equals(imgFlag)) {
			finish = true;
			PhotoGalleryUtil.pickBuilder(this);
		}
	}

	private void initView() {
		flImage = (FrameLayout) findViewById(R.id.selectImage);
		flPisition = (FrameLayout) findViewById(R.id.selectPosition);
		tv = (TextView) findViewById(R.id.position_wenzi);
		imageView = (ImageView) findViewById(R.id.position_image);
		send = (TextView) findViewById(R.id.send);
		cancle = (TextView) findViewById(R.id.cancle);
		content = (EditText) findViewById(R.id.content);
		gridView = (NoScrolGridView) findViewById(R.id.gridview);

		flImage.setOnClickListener(this);
		flPisition.setOnClickListener(this);
		send.setOnClickListener(this);
		cancle.setOnClickListener(this);

		imageView.setSelected(positionFlag);

		wallAdapter = new GridAdapter(this);
		gridView.setAdapter(wallAdapter);
	}

	private void send() {
		params.clear();
		params.put("content", content.getText().toString());
		params.put("range", DicUtil.RANGE_PUBLIC);
		if (positionFlag) {
			params.put("position", MyAddress.getAddrStr());
		}
		dongTaiService.save(listener, params, files);
	}

	private void selectPosition() {
		if (positionFlag) {
			positionFlag = false;
			tv.setText("不显示位置");
			tv.setTextColor(getResources().getColor(R.color.ccc));
		} else {
			positionFlag = true;
			tv.setText("显示位置");
			tv.setTextColor(getResources().getColor(android.R.color.black));
		}
		imageView.setSelected(positionFlag);
	}

	private ServiceListener<Result> listener = new ServiceListener<Result>() {

		@Override
		public void preExecute() {
			showLoadingDialog();
		}

		@Override
		public void onSuccess(Result result) {
			ToastUtil.show(WriteDongtaiActivity.this, result.getMessage(), null);
		}

		@Override
		public void onFinally() {
			cancelLoadingDialog();
			finishActivity();
		}

		@Override
		public void onFailed(int code, String message) {
			ToastUtil.show(WriteDongtaiActivity.this, message, null);
		}
	};

}
