package com.kdf.etl.service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.hadoop.hive.HiveClient;
import org.springframework.data.hadoop.hive.HiveClientCallback;
import org.springframework.data.hadoop.hive.HiveTemplate;
import org.springframework.stereotype.Service;

import com.beust.jcommander.internal.Lists;
import com.beust.jcommander.internal.Maps;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @title: UvHiveService.java 
 * @package com.kdf.etl.service 
 * @description: uv统计的hiveService
 * @author: 、T
 * @date: 2019年10月11日 下午2:50:02 
 * @version: V1.0
 */
@Slf4j
@Service
public class UvHiveService {

	@Autowired
	private HiveTemplate hiveTemplate;
	
	public List<Map<String, String>> getBrowserUv(String yearMonthDayHour) {
		StringBuffer hiveSql = new StringBuffer();
		hiveSql.append("select appid appid, browser_name browserName, count(browser_name) count from pv_log_hive_");
		hiveSql.append(yearMonthDayHour);
		hiveSql.append(" group by appid, browser_name");
		if(log.isInfoEnabled()) {
			log.info("getBrowserUv  hiveSql=[{}]", hiveSql.toString());
		}
		List<Map<String, String>> resultList = hiveTemplate.execute(hiveClient -> {
			List<Map<String, String>> uvList = Lists.newArrayList();
			Connection conn = hiveClient.getConnection();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(hiveSql.toString());
			while(rs.next()) {
				String browserName = rs.getString("browserName");
				String count = rs.getString("count");
				String appid = rs.getString("appid");
				Map<String, String> reMap = Maps.newHashMap();
				reMap.put("browserName", browserName);
				reMap.put("count", count);
				reMap.put("appid", appid);
				uvList.add(reMap);
			}
			return uvList;
		});
		return resultList;
	}
	
	
	public List<Map<String, String>> getOsUv(String yearMonthDayHour) {
		StringBuilder hiveSql = new StringBuilder();
		hiveSql.append("select appid appid, os_name osName,os_version osVersion, count(osName) count from pv_log_hive_");
		hiveSql.append(yearMonthDayHour);
		hiveSql.append(" group by os_name,os_version, appid");
		if(log.isInfoEnabled()) {
			log.info("getOsUv  hiveSql=[{}]", hiveSql.toString());
		}
		List<Map<String, String>> resultList = hiveTemplate.execute(hiveClient -> {
			List<Map<String, String>> uvList = Lists.newArrayList();
			Connection conn = hiveClient.getConnection();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(hiveSql.toString());
			while(rs.next()) {
				String osName = rs.getString("osName");
				String osVersion = rs.getString("osVersion");
				String appid = rs.getString("appid");
				String count = rs.getString("count");
				Map<String, String> reMap = Maps.newHashMap();
				reMap.put("osName", osName + osVersion);
				reMap.put("count", count);
				reMap.put("appid", appid);
				uvList.add(reMap);
			}
			return uvList;
		});
		return resultList;
	}
	
	public List<Map<String, String>> getClientTypeUv(String yearMonthDayHour) {
		StringBuilder hiveSql = new StringBuilder();
		hiveSql.append("select distinct appid appId,count(select count(device_type) from pv_log_hive_");
		hiveSql.append(yearMonthDayHour);
		hiveSql.append(" where device_type = 'mobile' and appid = appId) mobileCount ,");
		hiveSql.append(" count(select count(device_type) from pv_log_hive_");
		hiveSql.append(yearMonthDayHour);
		hiveSql.append(" where device_type = 'pc' and appid = appId) pcCount");
		hiveSql.append(" from pv_log_hive_");
		hiveSql.append(yearMonthDayHour);
		if(log.isInfoEnabled()) {
			log.info("getOsUv  hiveSql=[{}]", hiveSql.toString());
		}
		List<Map<String, String>> resultList = hiveTemplate.execute(hiveClient -> {
			List<Map<String, String>> uvList = Lists.newArrayList();
			Connection conn = hiveClient.getConnection();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(hiveSql.toString());
			while(rs.next()) {
				String appid = rs.getString("appId");
				String mobileCount = rs.getString("mobileCount");
				String pcCount = rs.getString("pcCount");
				Map<String, String> reMap = Maps.newHashMap();
				reMap.put("mobileCount", mobileCount);
				reMap.put("pcCount", pcCount);
				reMap.put("appid", appid);
				uvList.add(reMap);
			}
			return uvList;
		});
		return resultList;
	}
}
