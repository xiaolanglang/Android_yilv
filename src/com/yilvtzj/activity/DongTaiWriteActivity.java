package com.yilvtzj.activity;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.yilvtzj.R;
import com.yilvtzj.http.FormFile;
import com.yilvtzj.http.SocketHttpRequester;
import com.yilvtzj.util.Global;
import com.yilvtzj.util.PhotoGalleryUtil;
import com.yilvtzj.webview.JsInterface;
import com.yilvtzj.webview.JsInterface.JsInterfaceMethod;
import com.yilvtzj.webview.MyWebChromeClient;
import com.yilvtzj.webview.MyWebViewClient;

public class DongTaiWriteActivity extends Activity implements OnClickListener, JsInterfaceMethod {

	private WebView webView;
	private FrameLayout flImage, flPisition;
	private TextView tv;
	private ImageView imageView;
	boolean flag = true;
	private JsInterface jsInterface = new JsInterface();
	private Map<String, String> map = new HashMap<String, String>();
	private static String url = Global.getServletUrl("/travel/dongtai/save");
	List<String> list = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_dongtai_write);
		super.onCreate(savedInstanceState);

		initModules();

		webView.setWebViewClient(new MyWebViewClient(this, webView, jsInterface));
		webView.setWebChromeClient(new MyWebChromeClient(this, false));
		webView.loadUrl(Global.getServletUrl("/travel/dongtai/local/page/dongtai/write"));

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
		list = PhotoGalleryUtil.getImagePaths(requestCode, resultCode, data, RESULT_OK);
		if (list == null) {
			map.put("images", null);
			return;
		}
		map.put("images", new JSONArray(list).toString());
		webView.loadUrl("javascript:jsMethod.clearImgs()");
		for (String url : list) {
			url = Global.urlContentLocalImage + url;
			webView.loadUrl("javascript:jsMethod.setImgs('" + url + "')");
		}
		webView.loadUrl("javascript:jsMethod.showImgs()");

	}

	@Override
	public void runMethod(String... params) {
		if (params != null) {
			map.put("content", params[0]);
			map.put("range", params[0]);
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

		imageView.setSelected(flag);
	}

	private void selectImage() {
		PhotoGalleryUtil.pickBuilder(this);
	}

	private void selectPosition() {
		if (flag) {
			flag = false;
			tv.setText("不显示位置");
			tv.setTextColor(getResources().getColor(R.color.ccc));
		} else {
			flag = true;
			tv.setText("显示位置");
			tv.setTextColor(getResources().getColor(android.R.color.black));
		}
		imageView.setSelected(flag);
	}

	Thread postThread = new Thread(new Runnable() {

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
			try {
				boolean flag = SocketHttpRequester.post(url, map, formFiles);
				String content = "";
				if (flag) {
					content = "上传成功";
				} else {
					content = "上传失败";
				}
				Message msg = handler.obtainMessage();
				msg.obj = content;
				handler.sendMessage(msg);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	});

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			Toast.makeText(DongTaiWriteActivity.this, msg.obj.toString(), Toast.LENGTH_LONG).show();
		};
	};

}
