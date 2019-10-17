package com.kdf.etl.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * UnternalIpUtil 判断是否是局域网IP
 * 
 * @author mengpp
 * @date 2019年10月11日08:49:31
 */
public class InternalIpUtil {

	/**
	 * innerIP 判断是否是局域网IP
	 * 
	 * @param ip IP
	 * @return
	 */
	public static boolean innerIP(String ip) {
		Pattern reg = Pattern.compile(
				"^(127\\.0\\.0\\.1)|(localhost)|(10\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3})|(172\\.((1[6-9])|(2\\d)|(3[01]))\\.\\d{1,3}\\.\\d{1,3})|(192\\.168\\.\\d{1,3}\\.\\d{1,3})$");
		Matcher match = reg.matcher(ip);
		return match.find();
	}

	public static void main(String[] args) {
		boolean innerIP = innerIP("192.168.31.66");
		System.out.println(innerIP);
	}

}
