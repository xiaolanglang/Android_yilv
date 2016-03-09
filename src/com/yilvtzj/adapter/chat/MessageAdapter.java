package com.yilvtzj.adapter.chat;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.utils.L;
import com.yilvtzj.entity.MessageItem;
import com.yilvtzj.util.SharePreferenceUtil;

public class MessageAdapter extends BaseAdapter {

	public static final Pattern EMOTION_URL = Pattern.compile("\\[(\\S+?)\\]");

	private Context mContext;
	private LayoutInflater mInflater;
	private List<MessageItem> mMsgList;
	private SharePreferenceUtil mSpUtil;

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
		// TODO Auto-generated method stub
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
				// if
				// (PushApplication.getInstance().getFaceMap().containsKey(str2))
				// {
				// int face =
				// PushApplication.getInstance().getFaceMap().get(str2);
				// Bitmap bitmap =
				// BitmapFactory.decodeResource(mContext.getResources(), face);
				// if (bitmap != null) {
				// ImageSpan localImageSpan = new ImageSpan(mContext, bitmap,
				// ImageSpan.ALIGN_BASELINE);
				// value.setSpan(localImageSpan, k, m,
				// Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				// }
				// }
			}
		}
		return value;
	}

	static class ViewHolder {
		ImageView head;
		TextView msg;
		ImageView imageView;
		ProgressBar progressBar;

	}
}