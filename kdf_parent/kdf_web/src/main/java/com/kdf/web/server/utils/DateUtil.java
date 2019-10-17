package com.kdf.web.server.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 
 * @title: DateUtil.java 
 * @package com.kdf.web.server.utils 
 * @description: 时间工具类
 * @author: 、T
 * @date: 2019年10月14日 下午3:15:24 
 * @version: V1.0
 */
public class DateUtil {

	public static final String HOUR_PATTERN = "HH:mm";
	
	public static final String COMMON_PATTERN = "yyyy-MM-dd HH:mm:ss";
	/**
	 * 
	 * @title: getHourStr 
	 * @description: 将yyyy-MM-dd HH:mm:ss格式的时间转换为1小时时间段的拼接字符串HH:mm-HH:mm
	 * @author: 、T
	 * @date: 2019年10月14日 下午3:16:02
	 * @return String
	 * @throws:
	 */
	public static String getHourStr(String date) {
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(COMMON_PATTERN);
		LocalDateTime localDateTime = LocalDateTime.parse(date, dateTimeFormatter);
		dateTimeFormatter = DateTimeFormatter.ofPattern(HOUR_PATTERN);
		StringBuffer hourStr = new StringBuffer();
		hourStr.append(dateTimeFormatter.format(localDateTime.minusHours(1L)));
		hourStr.append("-");
		hourStr.append(dateTimeFormatter.format(localDateTime.minusMinutes(1L)));
		return hourStr.toString();
	}
}
