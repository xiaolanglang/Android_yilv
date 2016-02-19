package com.yilvtzj.baidumap;

public class MyAddress {
	private static String addrStr = null;

	public static String getAddrStr() {
		return addrStr;
	}

	public static void setAddrStr(String addrStr) {
		if (addrStr != null && !addrStr.equals(MyAddress.getAddrStr())) {
			MyAddress.addrStr = addrStr;
		}
	}
}
