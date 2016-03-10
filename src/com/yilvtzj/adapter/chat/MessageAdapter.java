package com.yilvtzj.adapter.chat;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TextView.BufferType;

import com.nostra13.universalimageloader.utils.L;
import com.yilvtzj.R;
import com.yilvtzj.entity.MessageItem;
import com.yilvtzj.util.FaceUtil;

public class MessageAdapter extends BaseAdapter {

	public static final Pattern EMOTION_URL = Pattern.compile("\\[(\\S+?)\\]");

	private Context mContext;
	private LayoutInflater mInflater;
	private List<MessageItem> mMsgList;

	public MessageAdapter(Context context, List<MessageItem> msgList) {
		this.mContext = context;
		mMsgList = msgList;
		mInflater = LayoutInflater.from(context);
	}

	public void removeHeadMsg() {
		L.i("before remove mMsgList.size() = " + mMsgList.size());
		if (mMsgList.size() - 10 > 10) {
			for (int i = 0; i < 10; i++) {
				mMsgList.remove(i);
			}
			notifyDataSetChanged();
		}
		L.i("after remove mMsgList.size() = " + mMsgList.size());
	}

	public void setMessageList(List<MessageItem> msgList) {
		mMsgList = msgList;
		notifyDataSetChanged();
	}

	public void upDateMsg(MessageItem msg) {
		mMsgList.add(msg);
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mMsgList.size();
	}

	@Override
	public Object getItem(int position) {
		return mMsgList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		MessageItem item = mMsgList.get(position);
		boolean isComMsg = item.isComMeg();
		ViewHolder holder;
		if (convertView == null || convertView.getTag(R.drawable.ic_launcher + position) == null) {
			holder = new ViewHolder();
			if (isComMsg) {
				convertView = mInflater.inflate(R.layout.item_chat_left, null);
			} else {
				convertView = mInflater.inflate(R.layout.item_chat_right, null);
			}
			holder.head = (ImageView) convertView.findViewById(R.id.icon);
			holder.msg = (TextView) convertView.findViewById(R.id.textView2);
			convertView.setTag(R.drawable.ic_launcher + position);
		} else {
			holder = (ViewHolder) convertView.getTag(R.drawable.ic_launcher + position);
		}
		holder.head.setImageBitmap(BitmapFactory.decodeResource(mContext.getResources(), item.getHeadImg()));

		holder.msg.setText(convertNormalStringToSpannableString(item.getMessage()), BufferType.SPANNABLE);
		return convertView;
	}

	/**
	 * 另外一种方法解析表情
	 * 
	 * @param message
	 *            传入的需要处理的String
	 * @return
	 */
	private CharSequence convertNormalStringToSpannableString(String message) {
		String hackTxt;
		if (message.startsWith("[") && message.endsWith("]")) {
			hackTxt = message + " ";
		} else {
			hackTxt = message;
		}
		SpannableString value = SpannableString.valueOf(hackTxt);

		Matcher localMatcher = EMOTION_URL.matcher(value);
		while (localMatcher.find()) {
			String str2 = localMatcher.group(0);
			int k = localMatcher.start();
			int m = localMatcher.end();
			if (m - k < 8) {
				if (FaceUtil.getFaceMap().containsKey(str2)) {
					int face = FaceUtil.getFaceMap().get(str2);
					Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), face);
					if (bitmap != null) {
						ImageSpan localImageSpan = new ImageSpan(mContext, bitmap, ImageSpan.ALIGN_BASELINE);
						value.setSpan(localImageSpan, k, m, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
					}
				}
			}
		}
		return value;
	}

	static class ViewHolder {
		ImageView head;
		TextView msg;
		ImageView imageView;

	}
}