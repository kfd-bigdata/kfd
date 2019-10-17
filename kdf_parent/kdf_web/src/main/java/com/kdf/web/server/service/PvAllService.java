package com.kdf.web.server.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface PvAllService {

	List<Map<String, Object>> findAllPvMap(String appid);

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
	 * findByAppidAndAndRequestTime 通过appid和时间查询
	 * 
	 * @param appid appid
	 * @param date  时间
	 * @return
	 */
	List<Long> findByAppidAndAndRequestTime(String appid, Date date);

	/**
	 * countGroupByAppid 按照appid统计项目
	 * 
	 * @return 项目名，pv总数
	 */
	List<Map<String, Object>> countGroupByAppid();

}
