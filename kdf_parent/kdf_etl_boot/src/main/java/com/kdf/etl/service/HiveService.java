package com.kdf.etl.service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.hadoop.hive.HiveTemplate;
import org.springframework.stereotype.Service;

import com.beust.jcommander.internal.Lists;
import com.kdf.etl.bean.PvAll;
import com.kdf.etl.utils.DateUtils;

import lombok.extern.slf4j.Slf4j;
/**
 * 
 * @ClassName: HiveService
 * @Description: HiveService
 * @author 王者の南が少ない 1715656022@qq.com
 * @date 2019年10月10日 上午11:04:44
 * 
 */
@Service
@Slf4j
public class HiveService {

	@Autowired
	private HiveTemplate hiveTemplate;

	/**
	 * 总pv计算
	 * 
	 * @param yearMonthDayHour 年月日小时 时间戳
	 * @return 汇总结果
	 */
	public List<PvAll> getAllPv(String yearMonthDayHour) {
		String hiveSql = "select count(1) pvCount,appid from pv_log_hive_" + yearMonthDayHour + " group by appid ";
		if (log.isDebugEnabled()) {
			log.debug("getAllPv sql={}", hiveSql);
		}
		List<PvAll> list = Lists.newArrayList();
		List<PvAll> pvAllList = hiveTemplate.execute((hiveClient) -> {
			Connection conn = hiveClient.getConnection();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(hiveSql);
			PvAll pvAll = new PvAll();
			Date date = DateUtils.strToDate(yearMonthDayHour);
			while (rs.next()) {
				pvAll.setPvCount(rs.getLong("pvCount"));
				pvAll.setAppid(rs.getString("appid"));
				pvAll.setRequestTime(date);
				list.add(pvAll);
			}
			return list;
		});
		return pvAllList;
	}
}
