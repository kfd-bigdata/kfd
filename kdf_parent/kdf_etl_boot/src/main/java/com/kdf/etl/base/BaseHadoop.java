package com.kdf.etl.base;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 
 * @ClassName: BaseHadoop
 * @Description: BaseHadoop
 * @author 王者の南が少ない 1715656022@qq.com
 * @date 2019年10月10日 上午11:04:44
 * 
 */
public class BaseHadoop {
	static {
		// 这里修改成自己的路径
		System.setProperty("hadoop.home.dir", "D:\\Java\\winutils-master\\hadoop-3.0.0");
	}

	private final static String DATE_FORMATTER = "yyyy_MM_dd_HH";

	public String getYyyyMmDdHh() {
		LocalDateTime ldtOne = LocalDateTime.now().minusHours(1);
		String nowDate = DateTimeFormatter.ofPattern(DATE_FORMATTER).format(ldtOne);
		return nowDate;
	}
}
