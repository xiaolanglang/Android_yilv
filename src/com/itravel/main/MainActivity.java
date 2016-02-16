package com.itravel.main;

import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.itravel.baidumap.MyBaiduMap;
import com.itravel.fragments.FragmentIndex;
import com.itravel.fragments.FragmentMine;
import com.itravel.fragments.FragmentNews;
import com.itravel.fragments.FragmentZiXun;
import com.itravel.popup.PopupWindowView;
import com.itravel.util.ClassManagerUtil;
import com.itravel.util.PhotoGalleryUtil;
import com.yilvtzj.R;

public class MainActivity extends FragmentActivity implements OnClickListener {
	// 定义Fragment页面
	private FragmentIndex fragmentIndex = ClassManagerUtil.newInstance(FragmentIndex.class);
	private FragmentZiXun fragmentZiXun = ClassManagerUtil.newInstance(FragmentZiXun.class);
	private FragmentNews fragmentNews = ClassManagerUtil.newInstance(FragmentNews.class);
	private FragmentMine fragmentMine = ClassManagerUtil.newInstance(FragmentMine.class);
	private Fragment currentFragment = null;
	// 定义布局对象
	private FrameLayout indexFl, ziXunFl, fastFl, newsFl, mineFl;

	// 定义图片组件对象
	private ImageView indexIv, ziXunIv, newsIv, mineIv;

	// 定义一个变量，来标识是否退出
	private static boolean isExit = false;

	// 百度地图
	private MyBaiduMap baiduMap;
	// 广播
	private BroadcastReceiver mReceiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_main);
		super.onCreate(savedInstanceState);

		baiduMap = new MyBaiduMap(this);
		mReceiver = baiduMap.getSDKReceiver();
		registerReceiver(mReceiver, baiduMap.getIntentFilter());
		baiduMap.onStart();

		initModules();

		new PopupWindowView(this, fastFl, findViewById(R.id.home_container_top));

		initListener();

		clickIndexBtn();
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onStop() {
		super.onStop();
		baiduMap.onStop();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(mReceiver);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			exit();
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 点击事件
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.layout_index:
			clickIndexBtn();
			break;
		case R.id.layout_zixun:
			clickZiXunBtn();
			break;
		case R.id.layout_news:
			clickNewsBtn();
			break;
		case R.id.layout_mine:
			clickMineBtn();
			break;
		}
	}

	/**
	 * 初始化组件
	 */
	private void initModules() {
		// 实例化布局对象
		indexFl = (FrameLayout) findViewById(R.id.layout_index);
		ziXunFl = (FrameLayout) findViewById(R.id.layout_zixun);
		fastFl = (FrameLayout) findViewById(R.id.layout_fast);
		newsFl = (FrameLayout) findViewById(R.id.layout_news);
		mineFl = (FrameLayout) findViewById(R.id.layout_mine);

		// 实例化图片组件对象
		indexIv = (ImageView) findViewById(R.id.image_index);
		ziXunIv = (ImageView) findViewById(R.id.image_zixun);
		newsIv = (ImageView) findViewById(R.id.image_news);
		mineIv = (ImageView) findViewById(R.id.image_mine);

	}

	/**
	 * 初始监听
	 */
	private void initListener() {
		// 给布局对象设置监听
		indexFl.setOnClickListener(this);
		ziXunFl.setOnClickListener(this);
		newsFl.setOnClickListener(this);
		mineFl.setOnClickListener(this);
	}

	/**
	 * 点击了“主页”按钮
	 */
	private void clickIndexBtn() {
		switchFragment(fragmentIndex);

		changeSelected();
		indexFl.setSelected(true);
		indexIv.setSelected(true);
	}

	/**
	 * 点击了“资讯”按钮
	 */
	private void clickZiXunBtn() {
		switchFragment(fragmentZiXun);

		changeSelected();
		ziXunFl.setSelected(true);
		ziXunIv.setSelected(true);

	}

	/**
	 * 点击了“消息”按钮
	 */
	private void clickNewsBtn() {
		switchFragment(fragmentNews);

		changeSelected();
		newsFl.setSelected(true);
		newsIv.setSelected(true);
	}

	/**
	 * 点击了“我的”按钮
	 */
	private void clickMineBtn() {
		switchFragment(fragmentMine);

		changeSelected();
		mineFl.setSelected(true);
		mineIv.setSelected(true);
	}

	private void changeSelected() {
		indexFl.setSelected(false);
		indexIv.setSelected(false);

		ziXunFl.setSelected(false);
		ziXunIv.setSelected(false);

		newsFl.setSelected(false);
		newsIv.setSelected(false);

		mineFl.setSelected(false);
		mineIv.setSelected(false);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		List<String> list = PhotoGalleryUtil
				.getImagePaths(requestCode, resultCode, data, RESULT_OK);
		System.out.println(list);
	}

	// --------------- 切换fragment

	/**
	 * 切换fragment
	 * 
	 * @param to
	 */
	private void switchFragment(android.support.v4.app.Fragment to) {
		if (currentFragment == to) {
			return;
		}
		// 得到Fragment事务管理器
		FragmentTransaction fragmentTransaction = this.getSupportFragmentManager()
				.beginTransaction();
		// 替换当前的页面
		if (currentFragment != null) {
			fragmentTransaction.hide(currentFragment);
		}
		if (to.isAdded()) {
			fragmentTransaction.show(to);
		} else {
			fragmentTransaction.add(R.id.frame_content, to);
		}
		// 事务管理提交
		fragmentTransaction.commit();
		currentFragment = to;
	}

	// ---------------退出功能

	private static Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			isExit = false;
		}
	};

	private void exit() {
		if (!isExit) {
			isExit = true;
			Toast.makeText(getApplicationContext(), "再按一次退出爱旅行", Toast.LENGTH_SHORT).show();
			// 利用handler延迟发送更改状态信息
			mHandler.sendEmptyMessageDelayed(0, 2000);
		} else {
			finish();
			System.exit(0);
		}
	}

}
