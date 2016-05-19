package com.yilvtzj.baidumap;

import com.common.util.LogUtil;

public class MyAddress {
	private static String addrStr = null;

	public static String getAddrStr() {
		return addrStr;
	}

	public static void setAddrStr(String addrStr) {
		if (addrStr != null && !addrStr.equals(MyAddress.getAddrStr())) {
			LogUtil.e(">>>>>>>>>>>", addrStr);
			MyAddress.addrStr = addrStr;
		}
	}
}
