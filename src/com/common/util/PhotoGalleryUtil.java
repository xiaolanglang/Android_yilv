package com.common.util;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;

import com.wq.photo.widget.PickConfig;

/**
 * 防微信查看图片工具类，帮助快速打开图片查看，获得选择的图片路径
 * 
 * @author Administrator
 *
 */
public class PhotoGalleryUtil {

	/**
	 * 当前activity
	 * 
	 * @param activity
	 */
	public static void pickBuilder(Activity activity) {
		new PickConfig.Builder(activity).maxPickSize(9)// 最多选择几张
				.isneedcamera(true)// 是否需要第一项是相机
				.spanCount(3)// 一行显示几张照片
				.isneedcrop(false)// 受否需要剪裁
				.isneedactionbar(false)// 是否需要actionbar;多选默认不能隐藏
				.pickMode(PickConfig.MODE_MULTIP_PICK)// 单选还是多选;这里是多选
				.build();
	}

	public static List<String> getImagePaths(int requestCode, int resultCode, Intent data, int result_ok) {
		if (resultCode == result_ok && requestCode == PickConfig.PICK_REQUEST_CODE) {
			// 在data中返回 选择的图片列表
			ArrayList<String> paths = data.getStringArrayListExtra("data");
			return paths;
		}
		return null;
	}
}
