package com.yilvtzj.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;

public class ListViewNoScroll extends ListView {

	public ListViewNoScroll(Context context) {
		super(context);
	}

	public ListViewNoScroll(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ListViewNoScroll(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	// 不出现滚动条
	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {

		return super.dispatchTouchEvent(ev);
	}
}
