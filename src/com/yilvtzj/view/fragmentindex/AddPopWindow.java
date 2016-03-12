package com.yilvtzj.view.fragmentindex;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.mining.app.zxing.activity.MipcaActivityCapture;
import com.yilvtzj.R;
import com.yilvtzj.util.ToastUtil;

public class AddPopWindow extends PopupWindow implements OnClickListener {
	private View conentView;
	private Activity activity;
	private RelativeLayout addfriends, scan;
	private final static int SCANNIN_GREQUEST_CODE = 1;

	public AddPopWindow(final Activity context) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		conentView = inflater.inflate(R.layout.popupwindow_add, null);

		// 设置SelectPicPopupWindow的View
		this.setContentView(conentView);
		// 设置SelectPicPopupWindow弹出窗体的宽
		this.setWidth(LayoutParams.WRAP_CONTENT);
		// 设置SelectPicPopupWindow弹出窗体的高
		this.setHeight(LayoutParams.WRAP_CONTENT);
		// 设置SelectPicPopupWindow弹出窗体可点击
		this.setFocusable(true);
		this.setOutsideTouchable(true);
		// 刷新状态
		this.update();
		// 实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(0000000000);
		// 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
		this.setBackgroundDrawable(dw);

		// 设置SelectPicPopupWindow弹出窗体动画效果
		this.setAnimationStyle(R.style.AnimationPreview);

		addfriends = (RelativeLayout) conentView.findViewById(R.id.addfriends);
		scan = (RelativeLayout) conentView.findViewById(R.id.scan);

		addfriends.setOnClickListener(this);
		scan.setOnClickListener(this);

	}

	/**
	 * 显示popupWindow
	 * 
	 * @param parent
	 */
	public void showPopupWindow(View parent, Activity activity) {
		this.activity = activity;
		if (!this.isShowing()) {
			// 以下拉方式显示popupwindow
			this.showAsDropDown(parent, 0, 0);
		} else {
			this.dismiss();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.addfriends:
			ToastUtil.show(activity, "添加好友", null);
			break;

		case R.id.scan:
			MipcaActivityCapture.startScan(activity, SCANNIN_GREQUEST_CODE);
			break;
		}
		AddPopWindow.this.dismiss();
	}
}
