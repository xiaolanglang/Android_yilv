package com.yilvtzj.popup;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.PopupWindow;

import com.yilvtzj.R;
import com.yilvtzj.activity.DongTaiWriteActivity;
import com.yilvtzj.util.PhotoGalleryUtil;

public class PopupWindowView implements OnClickListener {

	private Activity activity;
	private View contentView, close, open, showView;
	private PopupWindow popupWindow;
	private ImageButton btnWenZi, btnImage;

	public PopupWindowView(Activity activity, View open, View showView) {
		this.activity = activity;
		this.showView = showView;
		this.open = open;
		initPopupWindowView();
		initModules();

	}

	private void initPopupWindowView() {
		contentView = LayoutInflater.from(activity).inflate(R.layout.fast, null, false);
		popupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT, 450);
		popupWindow.setAnimationStyle(R.style.AnimationPopupWindow);

		popupWindow.setTouchable(true);
		popupWindow.setFocusable(true);
		popupWindow.setOnDismissListener(new poponDismissListener());

		backgroundAlpha(1f);

		close = contentView.findViewById(R.id.image_close);
		close.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				closePopupWindow();
			}
		});

		open.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (popupWindow != null && !popupWindow.isShowing()) {
					showPopupWindow();
				}
			}
		});
	}

	private void initModules() {
		btnWenZi = (ImageButton) contentView.findViewById(R.id.btn_fawenzi);
		btnImage = (ImageButton) contentView.findViewById(R.id.btn_faimage);

		initListener();
	}

	private void initListener() {
		btnWenZi.setOnClickListener(this);
		btnImage.setOnClickListener(this);

	}

	private void showPopupWindow() {
		backgroundAlpha(0.4f);
		popupWindow.showAsDropDown(showView, 0, 0);
	}

	private void closePopupWindow() {
		popupWindow.dismiss();
	}

	public void backgroundAlpha(float bgAlpha) {
		WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
		lp.alpha = bgAlpha; // 0.0-1.0
		activity.getWindow().setAttributes(lp);
	}

	class poponDismissListener implements PopupWindow.OnDismissListener {
		@Override
		public void onDismiss() {
			backgroundAlpha(1f);
		}
	}

	@Override
	public void onClick(View view) {
		closePopupWindow();
		switch (view.getId()) {
		case R.id.btn_fawenzi:
			faWenZi();
			break;
		case R.id.btn_faimage:
			faiImage();
			break;
		}

	}

	private void faWenZi() {
		Intent intent = new Intent(activity, DongTaiWriteActivity.class);
		activity.startActivity(intent);
	}

	private void faiImage() {
		PhotoGalleryUtil.pickBuilder(activity);
	}

}
