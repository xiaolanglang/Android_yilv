package com.yilvtzj.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import com.yilvtzj.R;

public class LoadingDialog extends Dialog {

	public LoadingDialog(Context context, String message) {
		super(context, R.style.loadingDialogStyle);
	}

	public LoadingDialog(Context context) {
		super(context, R.style.loadingDialogStyle);
	}

	private LoadingDialog(Context context, int theme) {
		super(context, theme);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.common_dialog_loading);
	}
}
