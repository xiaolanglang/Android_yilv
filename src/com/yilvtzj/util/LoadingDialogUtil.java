package com.yilvtzj.util;

import android.content.Context;

import com.yilvtzj.view.LoadingDialog;

public class LoadingDialogUtil {

	public static LoadingDialog get(Context context) {
		LoadingDialog dialog = new LoadingDialog(context);
		dialog.setCanceledOnTouchOutside(false);
		return dialog;
	}

	public static LoadingDialog show(Context context, LoadingDialog dialog) {
		if (dialog != null) {
			dialog.cancel();
		}
		dialog = new LoadingDialog(context);
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
		return dialog;
	}
}
