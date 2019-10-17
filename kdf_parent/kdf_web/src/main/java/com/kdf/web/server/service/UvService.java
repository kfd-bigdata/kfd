package com.kdf.web.server.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface UvService {

	/**
	 * countByRequestTimeAndAppid 按时间和appid统计
	 * 
	 * @param date  时间
	 * @param appid appid
	 * @return
	 */
	Long countByRequestTimeAndAppid(Date date, String appid);

	/**
	 * avgByRequestTimeAndAppid 按时间和appid求平均
	 * 
	 * @param date  时间
	 * @param appid appid
	 * @return
	 */
	Long avgByRequestTimeAndAppid(Date date, String appid);

	/**
	 * countGroupByAppid 按照appid统计项目
	 * 
	 * @return 项目名，pv总数
	 */
	List<Map<String, Object>> countGroupByAppid();

}
