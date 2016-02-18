package com.yilvtzj.util;

import android.content.Context;

import com.yilvtzj.dialog.LoadingDialog;

public class LoadingDialogUtil {
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
