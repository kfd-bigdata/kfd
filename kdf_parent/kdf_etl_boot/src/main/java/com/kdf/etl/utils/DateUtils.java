package com.kdf.etl.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * DateUtils 日期转换工具类
 * 
 * @author mengpp
 * @date 2019年10月11日15:14:07
 */
public class DateUtils {

	/**
	 * strToDate 字符串转日期
	 * 
	 * @param yearMonthDayHour yy_MM_dd_HH格式
	 * @return
	 */
	public static Date strToDate(String yearMonthDayHour) {
		Date d;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yy_MM_dd_HH");
			d = sdf.parse(yearMonthDayHour);
		} catch (Exception e) {
			d = null;
			e.printStackTrace();
		}
		return d;
	}

}
