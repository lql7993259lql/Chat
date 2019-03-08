package com.jixiang.chat.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CommonUtils {
	/**
	 * 格式化时间的工具类
	 */
	private static SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm:ss", Locale.CHINA);
	/**
	 * 被格式化的时间对象
	 */
	private static Date date = new Date();

	/**
	 * 获取被格式化为 mm:ss 格式的时间的字符串
	 * 
	 * @param timeMillis
	 *            被格式化的时间，单位为毫秒
	 * @return 被格式化为 mm:ss 格式的时间的字符串
	 */
	public static String getFormattedTime(long timeMillis) {
		// 设置时间
		date.setTime(timeMillis);
		// 返回格式化后的结果
		return sdf.format(date);
	}
	
	
}
