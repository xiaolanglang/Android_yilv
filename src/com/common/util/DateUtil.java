package com.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 时间工具类，提供和时间相关的方法
 * 
 * @author Administrator
 *
 */
public class DateUtil {

	private static final String TIMEFORMAT = "yyyy-MM-dd hh:mm:ss";

	/**
	 * 指定时间段
	 * 
	 * @param from
	 * @param to
	 * @return
	 */
	public static String rangeTime(Date from, Date to) {
		long l = to.getTime() - from.getTime();
		long d = l / (24 * 60 * 60 * 1000);
		if (d == 0) {
			d = l / (60 * 60 * 1000);
			if (d == 0) {
				return "刚刚";
			}
			return d + "小时前";
		}
		return d + "天前";
	}

	/**
	 * 指定时间段
	 * 
	 * @param from
	 * @param to
	 * @return
	 */
	public static String rangeTime(String from, String to, String format) {
		if (format == null) {
			format = TIMEFORMAT;
		}
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		Date date1 = null;
		Date date2 = null;
		try {
			date1 = formatter.parse(from);
			date2 = formatter.parse(to);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return rangeTime(date1, date2);
	}

	/**
	 * 指定时间到当前时间的距离
	 * 
	 * @param from
	 * @return
	 */
	public static String rangeTime(String from, String format) {
		if (format == null) {
			format = TIMEFORMAT;
		}
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		Date date1 = null;
		try {
			date1 = formatter.parse(from);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return rangeTime(date1, new Date());
	}
}
