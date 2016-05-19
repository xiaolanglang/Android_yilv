package com.common.util;

import android.app.Activity;
import android.content.Intent;

import com.yilvtzj.R;

public class ActivityUtil {

	/**
	 * 打开一个activity
	 * 
	 * @param intent
	 * @param from
	 * @param to
	 */
	public static void startActivity(Intent intent, Activity from, Class<?> to) {
		startActivity(intent, from, to, false);
	}

	/**
	 * 打开一个activity
	 * 
	 * @param intent
	 * @param from
	 * @param to
	 * @param ifFinish
	 */
	public static void startActivity(Intent intent, Activity from, Class<?> to, boolean ifFinish) {
		intent.setClass(from, to);
		from.startActivity(intent);
		if (ifFinish) {
			from.finish();
		}
		from.overridePendingTransition(R.anim.myslide_in_right, R.anim.myslide_out_left);
	}

	/**
	 * 关闭一个activity
	 * 
	 * @param activity
	 */
	public static void closeActivity(Activity activity) {
		activity.finish();
		activity.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
	}
}
