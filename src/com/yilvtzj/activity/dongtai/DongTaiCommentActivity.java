package com.yilvtzj.activity.dongtai;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;

import com.yilvtzj.R;
import com.yilvtzj.activity.FullPageImageViewActivity;
import com.yilvtzj.adapter.home.GridAdapter;
import com.yilvtzj.pojo.DongtaiMsg;
import com.yilvtzj.util.DateUtil;
import com.yilvtzj.util.StringUtil;

public class DongTaiCommentActivity extends Activity {

	private DongtaiMsg msg;
	private TextView name, time, content;
	private GridView gridView;
	private Activity mActivity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dongtai_comment);
		setActionBarLayout(R.layout.actionbar_layout);

		Intent intent = getIntent();
		msg = (DongtaiMsg) intent.getExtras().getSerializable("msg");
		mActivity = this;

		initView();
		setValue();
	}

	private void initView() {
		name = (TextView) findViewById(R.id.name);
		time = (TextView) findViewById(R.id.time);
		content = (TextView) findViewById(R.id.content);
		gridView = (GridView) findViewById(R.id.gridview);
	}

	private void setValue() {
		name.setText(msg.getAccount().getNickname());
		time.setText(DateUtil.rangeTime(msg.getCreateTime(), "yyyy-MM-dd hh:mm"));
		content.setText(msg.getContent());
		gridView.setAdapter(new GridAdapter(this.getBaseContext(), StringUtil.toStrings(msg.getImageUrls())));
		gridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				Intent intent = new Intent(mActivity.getBaseContext(), FullPageImageViewActivity.class);
				intent.putExtra("pictures", StringUtil.toStrings(msg.getImageUrls()));
				intent.putExtra("selectId", position);
				mActivity.startActivity(intent);
			}
		});
	}

	/**
	 * 设置ActionBar的布局
	 * 
	 * @param layoutId
	 *            布局Id
	 * 
	 * */
	public void setActionBarLayout(int layoutId) {
		ActionBar actionBar = getActionBar();
		if (null != actionBar) {
			actionBar.setDisplayShowHomeEnabled(false);
			actionBar.setDisplayShowCustomEnabled(true);

			LayoutInflater inflator = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View v = inflator.inflate(layoutId, null);
			@SuppressWarnings("deprecation")
			ActionBar.LayoutParams layout = new ActionBar.LayoutParams(LayoutParams.FILL_PARENT,
					LayoutParams.FILL_PARENT);
			actionBar.setCustomView(v, layout);
			TextView textView = (TextView) v.findViewById(R.id.title);
			textView.setText("评论");
		}
	}
}
