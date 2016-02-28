package com.yilvtzj.activity;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.yilvtzj.R;
import com.yilvtzj.baidumap.MyAddress;
import com.yilvtzj.http.FormFile;
import com.yilvtzj.http.SocketHttpRequester;
import com.yilvtzj.http.SocketHttpRequester.SocketListener;
import com.yilvtzj.util.Global;
import com.yilvtzj.util.LoadingDialogUtil;
import com.yilvtzj.util.PhotoGalleryUtil;
import com.yilvtzj.util.ToastUtil;
import com.yilvtzj.view.LoadingDialog;
import com.yilvtzj.webview.JsInterface;
import com.yilvtzj.webview.JsInterface.JsInterfaceMethod;
import com.yilvtzj.webview.MyWebChromeClient;
import com.yilvtzj.webview.MyWebViewClient;

public class DongTaiWriteActivity extends Activity implements OnClickListener, JsInterfaceMethod, SocketListener {

	private WebView webView;
	private FrameLayout flImage, flPisition;
	private TextView tv;
	private ImageView imageView;
	boolean positionFlag = true, sendFlag = true;
	private JsInterface jsInterface = new JsInterface();
	private Map<String, String> map = new HashMap<String, String>();
	private static String url = Global.getServletUrl("/travel/dongtai/save");
	private List<String> list = null;
	private Toast toast;

	public static final int MESSAGE_UPLOAD_SUCCESS = 0;
	public static final int MESSAGE_UPLOAD_FAILED = 1;
	public static final int MESSAGE_UPLOAD_ING = 2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		pickBuilder();

		setContentView(R.layout.activity_dongtai_write);
		super.onCreate(savedInstanceState);

		initModules();

		webView.setWebViewClient(new MyWebViewClient(this, false, false, webView, jsInterface, null));
		webView.setWebChromeClient(new MyWebChromeClient(this, false));
		webView.loadUrl("file:///android_asset/page/dongtai/write.html");
		webView.getSettings().setAllowFileAccess(true);

		jsInterface.setJsInterfaceListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.selectImage:
			selectImage();
			break;
		case R.id.selectPosition:
			selectPosition();
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		List<String> listTemp = PhotoGalleryUtil.getImagePaths(requestCode, resultCode, data, RESULT_OK);
		if (listTemp == null) {
			return;
		}
		list = listTemp;
		webView.loadUrl("javascript:jsMethod.clearImgs()");
		for (String url : list) {
			url = Global.urlContentLocalImage + url;
			webView.loadUrl("javascript:jsMethod.setImgs('" + url + "')");
		}
		webView.loadUrl("javascript:jsMethod.showImgs()");

	}

	@Override
	public void runMethod(String... params) {
		if (!sendFlag) {
			return;
		}
		sendFlag = false;
		if (params != null) {
			map.put("content", params[0]);
			map.put("range", params[1]);
		}
		postThread.run();
	}

	private void pickBuilder() {
		Intent intent = getIntent();
		String imgFlag = intent.getStringExtra("imgFlag");
		if ("true".equals(imgFlag)) {
			PhotoGalleryUtil.pickBuilder(this);
		}
	}

	private void initModules() {
		webView = (WebView) findViewById(R.id.webView);
		flImage = (FrameLayout) findViewById(R.id.selectImage);
		flPisition = (FrameLayout) findViewById(R.id.selectPosition);
		tv = (TextView) findViewById(R.id.position_wenzi);
		imageView = (ImageView) findViewById(R.id.position_image);

		flImage.setOnClickListener(this);
		flPisition.setOnClickListener(this);

		imageView.setSelected(positionFlag);
	}

	private void selectImage() {
		PhotoGalleryUtil.pickBuilder(this);
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

	private Thread postThread = new Thread(new Runnable() {

		@Override
		public void run() {
			FormFile[] formFiles = null;
			if (list != null && list.size() > 0) {
				int l = list.size();
				formFiles = new FormFile[l];
				for (int i = 0; i < l; i++) {
					String path = list.get(i);
					String ext = path.substring(path.lastIndexOf("."), path.length());
					String filename = i + ext;
					formFiles[i] = new FormFile(filename, new File(path), String.valueOf(i), null);
				}
			}

			if (positionFlag) {
				map.put("position", MyAddress.getAddrStr());
			}
			try {
				handler.sendEmptyMessage(MESSAGE_UPLOAD_ING);
				new SocketHttpRequester().post(DongTaiWriteActivity.this, url, map, formFiles).setSocketListener(
						DongTaiWriteActivity.this);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	});

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		private LoadingDialog dialog;

		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case MESSAGE_UPLOAD_SUCCESS:
				dialog.cancel();
				toast = ToastUtil.show(DongTaiWriteActivity.this, "上传成功", toast);
				finish();
				break;
			case MESSAGE_UPLOAD_FAILED:
				dialog.cancel();
				toast = ToastUtil.show(DongTaiWriteActivity.this, "上传失败", toast);
				break;
			case MESSAGE_UPLOAD_ING:
				dialog = LoadingDialogUtil.show(DongTaiWriteActivity.this, dialog);
				toast = ToastUtil.show(DongTaiWriteActivity.this, "上传中...", toast);
				break;
			}
		};
	};

	@Override
	public void result(String JSON) {
		if (JSON.indexOf("200") != -1) {
			handler.sendEmptyMessage(MESSAGE_UPLOAD_SUCCESS);
		} else {
			handler.sendEmptyMessage(MESSAGE_UPLOAD_FAILED);
			sendFlag = true;
		}

	}

}
