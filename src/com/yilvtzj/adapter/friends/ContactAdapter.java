package com.yilvtzj.adapter.friends;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.yilvtzj.R;
import com.yilvtzj.entity.Account;
import com.yilvtzj.util.PingYinUtil;

public class ContactAdapter extends BaseAdapter implements SectionIndexer {
	private Context mContext;
	private List<Account> UserInfos;// 好友信息

	public ContactAdapter(Context mContext, List<Account> UserInfos) {
		this.mContext = mContext;
		this.UserInfos = UserInfos;
		sort();
	}

	public void sort() {
		// 排序(实现了中英文混排)
		Collections.sort(UserInfos, new PinyinComparator());
	}

	public List<Account> getUserInfos() {
		return UserInfos;
	}

	public void setUserInfos(List<Account> userInfos) {
		UserInfos = userInfos;
	}

	@Override
	public int getCount() {
		return UserInfos.size();
	}

	@Override
	public Object getItem(int position) {
		return UserInfos.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Account user = UserInfos.get(position);
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_friends, null);
		}
		ImageView ivAvatar = ViewHolder.get(convertView, R.id.contactitem_avatar_iv);
		TextView tvCatalog = ViewHolder.get(convertView, R.id.contactitem_catalog);
		TextView tvNick = ViewHolder.get(convertView, R.id.contactitem_nick);
		String catalog = "";
		if (TextUtils.isEmpty(user.getNickname()))
			catalog = "#";
		else
			catalog = PingYinUtil.converterToFirstSpell(user.getNickname()).substring(0, 1);
		if (position == 0) {
			tvCatalog.setVisibility(View.VISIBLE);
			tvCatalog.setText(catalog);
		} else {
			Account Nextuser = UserInfos.get(position - 1);
			String lastCatalog = "";
			if (TextUtils.isEmpty(Nextuser.getNickname()))
				lastCatalog = "#";
			else
				lastCatalog = PingYinUtil.converterToFirstSpell(Nextuser.getNickname()).substring(0, 1);
			if (catalog.equals(lastCatalog)) {
				tvCatalog.setVisibility(View.GONE);
			} else {
				tvCatalog.setVisibility(View.VISIBLE);
				tvCatalog.setText(catalog);
			}
		}

		ivAvatar.setImageResource(R.drawable.c1);
		tvNick.setText(user.getNickname());
		return convertView;
	}

	@Override
	public int getPositionForSection(int section) {
		for (int i = 0; i < UserInfos.size(); i++) {
			Account user = UserInfos.get(i);
			String catalog = "";
			if (TextUtils.isEmpty(user.getNickname()))
				catalog = "#";
			else
				catalog = PingYinUtil.converterToFirstSpell(user.getNickname()).substring(0, 1);
			char firstChar = catalog.toUpperCase().charAt(0);
			if (firstChar == section) {
				return i;
			}
		}
		return 0;
	}

	@Override
	public int getSectionForPosition(int position) {
		return 0;
	}

	@Override
	public Object[] getSections() {
		return null;
	}

	private static class ViewHolder {
		@SuppressWarnings("unchecked")
		public static <T extends View> T get(View view, int id) {
			SparseArray<View> viewHolder = (SparseArray<View>) view.getTag();
			if (viewHolder == null) {
				viewHolder = new SparseArray<View>();
				view.setTag(viewHolder);
			}
			View childView = viewHolder.get(id);
			if (childView == null) {
				childView = view.findViewById(id);
				viewHolder.put(id, childView);
			}
			return (T) childView;
		}
	}

	private class PinyinComparator implements Comparator {

		@Override
		public int compare(Object arg0, Object arg1) {
			// 按照名字排序
			Account user0 = (Account) arg0;
			Account user1 = (Account) arg1;
			String catalog0 = "";
			String catalog1 = "";

			if (user0 != null && user0.getNickname() != null && user0.getNickname().length() > 1)
				catalog0 = PingYinUtil.converterToFirstSpell(user0.getNickname()).substring(0, 1);

			if (user1 != null && user1.getNickname() != null && user1.getNickname().length() > 1)
				catalog1 = PingYinUtil.converterToFirstSpell(user1.getNickname()).substring(0, 1);
			int flag = catalog0.compareTo(catalog1);
			return flag;

		}

	}
}
