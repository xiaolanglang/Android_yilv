package com.itravel.activity;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.itravel.util.Global;
import com.itravel.util.PhotoGalleryUtil;
import com.itravel.webview.JsInterface;
import com.itravel.webview.MyWebChromeClient;
import com.itravel.webview.MyWebViewClient;
import com.yilvtzj.R;

public class DongTaiWriteActivity extends Activity implements OnClickListener {

	private WebView webView;
	private FrameLayout flImage, flPisition;
	private TextView tv;
	private ImageView imageView;
	boolean flag = true;
	private JsInterface jsInterface = new JsInterface();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_dongtai_write);
		super.onCreate(savedInstanceState);

		initModules();

		webView.setWebViewClient(new MyWebViewClient(this, webView, jsInterface));
		webView.setWebChromeClient(new MyWebChromeClient(this, false));
		webView.loadUrl(Global.getServletUrl("/travel/dongtai/local/page/dongtai/write"));
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
		List<String> list = PhotoGalleryUtil
				.getImagePaths(requestCode, resultCode, data, RESULT_OK);
		if (list == null) {
			return;
		}
		webView.loadUrl("javascript:jsMethod.clearImgs()");
		for (String url : list) {
			url = Global.urlContentLocalImage + url;
			webView.loadUrl("javascript:jsMethod.setImgs('" + url + "')");
		}
		webView.loadUrl("javascript:jsMethod.showImgs()");

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
}
