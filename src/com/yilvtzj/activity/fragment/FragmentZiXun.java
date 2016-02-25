package com.yilvtzj.activity.fragment;

import com.yilvtzj.activity.common.MyFragment;
import com.yilvtzj.util.Global;

public class FragmentZiXun extends MyFragment {

	@Override
	protected void hookOnStart() {
		webView.loadUrl(Global.getServletUrl("/travel/zixun"));
	}
}
