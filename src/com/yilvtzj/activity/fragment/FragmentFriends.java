package com.yilvtzj.activity.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.yilvtzj.R;
import com.yilvtzj.activity.friend.SelfInfoActivity;
import com.yilvtzj.adapter.friends.ContactAdapter;
import com.yilvtzj.entity.Account;
import com.yilvtzj.util.ActivityUtil;
import com.yilvtzj.view.friends.SideBar;

public class FragmentFriends extends Fragment implements OnItemClickListener {

	private ListView lvContact;
	private SideBar indexBar;
	private TextView mDialogText;
	private WindowManager mWindowManager;
	private List<Account> list = new ArrayList<Account>();
	private ContactAdapter contactAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_friends, container, false);

		mWindowManager = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
		initViews(view);
		initData();
		initTxtSearchListen(view);

		return view;
	}

	@Override
	public void onDestroy() {
		mWindowManager.removeView(mDialogText);
		super.onDestroy();
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		ActivityUtil.startActivity(new Intent(), FragmentFriends.this.getActivity(), SelfInfoActivity.class);
	}

	private void initTxtSearchListen(View view) {
		((TextView) view.findViewById(R.id.txt_search)).addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				filterData(s.toString());
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});
	}

	private void initViews(View layout) {
		lvContact = (ListView) layout.findViewById(R.id.lvContact);
		lvContact.setOnItemClickListener(this);

		mDialogText = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.item_friends_list_position, null);
		mDialogText.setVisibility(View.INVISIBLE);
		indexBar = (SideBar) layout.findViewById(R.id.sideBar);
		indexBar.setListView(lvContact);
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.TYPE_APPLICATION,
				WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
				PixelFormat.TRANSLUCENT);
		mWindowManager.addView(mDialogText, lp);
		indexBar.setTextView(mDialogText);

	}

	private void initData() {
		contactAdapter = new ContactAdapter(getActivity(), list);

		lvContact.setAdapter(contactAdapter);

		Account account = new Account();
		account.setNickname("%adfsd");
		list.add(account);

		Account account1 = new Account();
		account1.setNickname("爱丽丝");
		list.add(account1);

		Account account22 = new Account();
		account22.setNickname("周结论");
		list.add(account22);

		Account account2 = new Account();
		account2.setNickname("发明家");
		list.add(account2);

		Account account3 = new Account();
		account3.setNickname("周董");
		list.add(account3);

		Account account4 = new Account();
		account4.setNickname("花花");
		list.add(account4);

		Account account5 = new Account();
		account5.setNickname("小明");
		list.add(account5);

		Account account6 = new Account();
		account6.setNickname("笑话");
		list.add(account6);

		Account account7 = new Account();
		account7.setNickname("卡梅隆");
		list.add(account7);

		Account account8 = new Account();
		account8.setNickname("emeilong");
		list.add(account8);

		Account account9 = new Account();
		account9.setNickname("kmeilong");
		list.add(account9);

		contactAdapter.sort();

	}

	private void filterData(String filterStr) {
		List<Account> filterDateList = new ArrayList<Account>();

		if (TextUtils.isEmpty(filterStr)) {
			filterDateList = list;
		} else {
			filterDateList.clear();
			for (Account sortModel : list) {
				String name = sortModel.getNickname();
				if (name.indexOf(filterStr.toString()) != -1) {
					filterDateList.add(sortModel);
				}
			}
		}

		contactAdapter.setUserInfos(filterDateList);
		contactAdapter.sort();
		contactAdapter.notifyDataSetChanged();
	}

}
