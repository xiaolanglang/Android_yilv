package com.yilvtzj.activity.chat;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.androidpn.service.NotificationReceiver;
import com.androidpn.service.NotificationReceiver.NotificationReceiverListener;
import com.androidpn.util.Constants;
import com.yilvtzj.R;
import com.yilvtzj.activity.common.MyActivity;
import com.yilvtzj.adapter.chat.FaceAdapter;
import com.yilvtzj.adapter.chat.FacePageAdeapter;
import com.yilvtzj.adapter.chat.MessageAdapter;
import com.yilvtzj.db.DBManager;
import com.yilvtzj.entity.MessageItem;
import com.yilvtzj.util.FaceUtil;
import com.yilvtzj.view.chat.CirclePageIndicator;
import com.yilvtzj.view.chat.JazzyViewPager;
import com.yilvtzj.view.chat.JazzyViewPager.TransitionEffect;

public class ChatActivity extends MyActivity implements OnTouchListener, OnClickListener, NotificationReceiverListener {
	public static final int NEW_MESSAGE = 0x001;// 收到消息
	private TransitionEffect mEffects[] = { TransitionEffect.Standard, TransitionEffect.Tablet, TransitionEffect.CubeIn,
			TransitionEffect.CubeOut, TransitionEffect.FlipVertical, TransitionEffect.FlipHorizontal, TransitionEffect.Stack,
			TransitionEffect.ZoomIn, TransitionEffect.ZoomOut, TransitionEffect.RotateUp, TransitionEffect.RotateDown,
			TransitionEffect.Accordion, };// 表情翻页效果
	private int currentPage = 0;
	private boolean isFaceShow = false;
	private Button sendBtn;
	private ImageButton faceBtn;
	private EditText msgEt;
	private LinearLayout faceLinearLayout;
	private JazzyViewPager faceViewPager;
	private WindowManager.LayoutParams params;
	private InputMethodManager imm;
	private List<String> keys;
	private MessageAdapter adapter;
	private ListView mMsgListView;
	private List<MessageItem> msgList = new ArrayList<>();
	private DBManager dbManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat);
		setCommonActionBar("聊天");
		dbManager = new DBManager(this);

		initView();
		initFacePage();

		initMsgData();
	}

	@Override
	protected void onResume() {
		super.onResume();
		NotificationReceiver receiver = (NotificationReceiver) Constants.notificationService.getNotificationReceiver();
		receiver.setNotificationReceiverListener(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		imm.hideSoftInputFromWindow(msgEt.getWindowToken(), 0);
		faceLinearLayout.setVisibility(View.GONE);
		isFaceShow = false;

		NotificationReceiver receiver = (NotificationReceiver) Constants.notificationService.getNotificationReceiver();
		receiver.setNotificationReceiverListener(null);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.face_btn:
			if (!isFaceShow) {
				imm.hideSoftInputFromWindow(msgEt.getWindowToken(), 0);
				try {
					Thread.sleep(80);// 解决此时会黑一下屏幕的问题
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				faceLinearLayout.setVisibility(View.VISIBLE);
				isFaceShow = true;
			} else {
				faceLinearLayout.setVisibility(View.GONE);
				isFaceShow = false;
			}
			break;
		case R.id.send_btn:// 发送消息
			sendMessage();
			break;
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch (v.getId()) {
		case R.id.msg_listView:
			imm.hideSoftInputFromWindow(msgEt.getWindowToken(), 0);
			faceLinearLayout.setVisibility(View.GONE);
			isFaceShow = false;
			break;
		case R.id.msg_et:
			imm.showSoftInput(msgEt, 0);
			faceLinearLayout.setVisibility(View.GONE);
			isFaceShow = false;
			break;

		default:
			break;
		}
		return false;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		dbManager.closeDB();
		adapter.recycleBitMap();
	}

	private void initData() {
		Set<String> keySet = FaceUtil.getFaceMap().keySet();
		keys = new ArrayList<String>();
		keys.addAll(keySet);
	}

	/**
	 * 加载消息历史，从数据库中读出
	 */
	private void initMsgData() {
		msgList = dbManager.findAllList(DBManager.MESSAGEITEM, MessageItem.class);
		if (msgList == null) {
			return;
		}
		adapter.setMessageList(msgList);
		adapter.notifyDataSetChanged();
		mMsgListView.setSelection(adapter.getCount() - 1);
	}

	private void initFacePage() {
		initData();
		List<View> lv = new ArrayList<View>();
		for (int i = 0; i < 6; ++i)
			lv.add(getGridView(i));
		FacePageAdeapter adapter = new FacePageAdeapter(lv, faceViewPager);
		faceViewPager.setAdapter(adapter);
		faceViewPager.setCurrentItem(currentPage);
		faceViewPager.setTransitionEffect(mEffects[0]);
		CirclePageIndicator indicator = (CirclePageIndicator) findViewById(R.id.indicator);
		indicator.setViewPager(faceViewPager);
		adapter.notifyDataSetChanged();
		faceLinearLayout.setVisibility(View.GONE);
		indicator.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				currentPage = arg0;
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// do nothing
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// do nothing
			}
		});

	}

	private GridView getGridView(int i) {
		GridView gv = new GridView(this);
		gv.setNumColumns(7);
		gv.setSelector(new ColorDrawable(Color.TRANSPARENT));// 屏蔽GridView默认点击效果
		gv.setBackgroundColor(Color.TRANSPARENT);
		gv.setCacheColorHint(Color.TRANSPARENT);
		gv.setHorizontalSpacing(1);
		gv.setVerticalSpacing(1);
		gv.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		gv.setGravity(Gravity.CENTER);
		gv.setAdapter(new FaceAdapter(this, i));
		gv.setOnTouchListener(forbidenScroll());
		gv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				if (arg2 == 20) {// 删除键的位置
					int selection = msgEt.getSelectionStart();
					String text = msgEt.getText().toString();
					if (selection > 0) {
						String text2 = text.substring(selection - 1);
						if ("]".equals(text2)) {
							int start = text.lastIndexOf("[");
							int end = selection;
							msgEt.getText().delete(start, end);
							return;
						}
						msgEt.getText().delete(selection - 1, selection);
					}
				} else {
					int count = currentPage * 20 + arg2;
					// 注释的部分，在EditText中显示字符串
					// String ori = msgEt.getText().toString();
					// int index = msgEt.getSelectionStart();
					// StringBuilder stringBuilder = new StringBuilder(ori);
					// stringBuilder.insert(index, keys.get(count));
					// msgEt.setText(stringBuilder.toString());
					// msgEt.setSelection(index + keys.get(count).length());

					// 下面这部分，在EditText中显示表情
					Bitmap bitmap = BitmapFactory.decodeResource(getResources(), (Integer) FaceUtil.getFaceMap().values().toArray()[count]);
					if (bitmap != null) {
						int rawHeigh = bitmap.getHeight();
						int rawWidth = bitmap.getHeight();
						int newHeight = 40;
						int newWidth = 40;
						// 计算缩放因子
						float heightScale = ((float) newHeight) / rawHeigh;
						float widthScale = ((float) newWidth) / rawWidth;
						// 新建立矩阵
						Matrix matrix = new Matrix();
						matrix.postScale(heightScale, widthScale);
						// 设置图片的旋转角度
						// matrix.postRotate(-30);
						// 设置图片的倾斜
						// matrix.postSkew(0.1f, 0.1f);
						// 将图片大小压缩
						// 压缩后图片的宽和高以及kB大小均会变化
						Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, rawWidth, rawHeigh, matrix, true);
						ImageSpan imageSpan = new ImageSpan(ChatActivity.this, newBitmap);
						String emojiStr = keys.get(count);
						SpannableString spannableString = new SpannableString(emojiStr);
						spannableString.setSpan(imageSpan, emojiStr.indexOf('['), emojiStr.indexOf(']') + 1,
								Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
						msgEt.append(spannableString);
					} else {
						String ori = msgEt.getText().toString();
						int index = msgEt.getSelectionStart();
						StringBuilder stringBuilder = new StringBuilder(ori);
						stringBuilder.insert(index, keys.get(count));
						msgEt.setText(stringBuilder.toString());
						msgEt.setSelection(index + keys.get(count).length());
					}
				}
			}
		});
		return gv;
	}

	private void initView() {
		mMsgListView = (ListView) findViewById(R.id.msg_listView);
		sendBtn = (Button) findViewById(R.id.send_btn);
		faceBtn = (ImageButton) findViewById(R.id.face_btn);
		msgEt = (EditText) findViewById(R.id.msg_et);
		faceLinearLayout = (LinearLayout) findViewById(R.id.face_ll);
		faceViewPager = (JazzyViewPager) findViewById(R.id.face_pager);
		faceViewPager = (JazzyViewPager) findViewById(R.id.face_pager);

		imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
		params = getWindow().getAttributes();

		adapter = new MessageAdapter(this, msgList);
		// 触摸ListView隐藏表情和输入法
		mMsgListView.setOnTouchListener(this);
		mMsgListView.setAdapter(adapter);
		msgEt.setOnTouchListener(this);
		msgEt.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_BACK) {
					if (params.softInputMode == WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE || isFaceShow) {
						faceLinearLayout.setVisibility(View.GONE);
						isFaceShow = false;
						return true;
					}
				}
				return false;
			}
		});
		msgEt.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				if (s.length() > 0) {
					sendBtn.setEnabled(true);
				} else {
					sendBtn.setEnabled(false);
				}
			}
		});
		faceBtn.setOnClickListener(this);
		sendBtn.setOnClickListener(this);
	}

	// 防止乱pageview乱滚动
	private OnTouchListener forbidenScroll() {
		return new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_MOVE) {
					return true;
				}
				return false;
			}
		};
	}

	/**
	 * 发送消息
	 */
	private void sendMessage() {
		String msg = msgEt.getText().toString();
		MessageItem item = new MessageItem("昵称", System.currentTimeMillis(), msg, R.drawable.c1, false, 0);
		adapter.upDateMsg(item);
		mMsgListView.setSelection(adapter.getCount() - 1);
		msgEt.setText("");
		ContentValues cv = new ContentValues();
		cv.put("fromwho", "昵称");
		cv.put("message", msg);
		dbManager.insert(DBManager.MESSAGEITEM, cv);
	}

	@Override
	public void receiverData(Intent intent) {
		String notificationTitle = intent.getStringExtra(Constants.NOTIFICATION_TITLE);
		String notificationMessage = intent.getStringExtra(Constants.NOTIFICATION_MESSAGE);
	}

}