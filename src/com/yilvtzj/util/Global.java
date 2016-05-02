package com.yilvtzj.util;

import android.content.Context;

public class Global {
	public static final String urlContent = "static/mobile/";
	public static final String urlContentLocalImage = "file://";
	public static final String urlCss = ".css";
	public static final String urlJs = ".js";
	public static final String urlPng = ".png";
	public static final String mimeTypeJs = "application/javascript";
	public static final String mimeTypeCss = "text/css";
	public static final String mimeTypeImg = "image/png";
	public static final String character = "UTF-8";
	private static final String SERVLETNAME = "yilv.cgotravel.com";
	public static final String JSINTERFACE = "jsInterface";
	public static final String TIANGOU_SERVICE = "";
	public static Context CONTEXT;

	public final static int SCANNIN_GREQUEST_CODE = 1;

	/**
	 * 获得请求路径
	 * 
	 * @param url
	 *            请求路径 例如：/order/second <br>
	 *            最前面不要有斜杠
	 */
	public static String getServletUrl(String url) {
		return "http://" + SERVLETNAME + url;
	}

}
