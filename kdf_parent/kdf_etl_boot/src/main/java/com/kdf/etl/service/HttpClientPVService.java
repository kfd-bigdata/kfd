package com.kdf.etl.service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.hadoop.hive.HiveClient;
import org.springframework.data.hadoop.hive.HiveClientCallback;
import org.springframework.data.hadoop.hive.HiveTemplate;
import org.springframework.stereotype.Service;

import com.beust.jcommander.internal.Lists;
import com.google.common.collect.Maps;
import com.kdf.etl.bean.PvHttpClient;
import com.kdf.etl.bean.UvTimeDistribution;
import com.kdf.etl.repository.HttpClientPVRepository;
import com.kdf.etl.utils.DateUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @ClassName: HttpClientPVService
 * @Description: 请求pv统计service
 * @author 刘岩
 * @date 2019年10月10日 上午11:04:44
 * 
 */
@Slf4j
@Service
public class HttpClientPVService {
	
	@Autowired
	private HiveTemplate hiveTemplate;

	@Autowired
	private HttpClientPVRepository httpClientPVRepository;

	public void saveHttpClient(String yearMonthDayHour) {
		log.info("star->saveHttpClient:yearMonthDayHour={}", yearMonthDayHour);

		String sql = "select appid,method,count(1) as pv_count from aapv_log_hive_" + yearMonthDayHour + " group by appid, method";
		log.info("sql={}", sql);

		List<Map<String, String>> list = hiveTemplate.execute(new HiveClientCallback<List<Map<String, String>>>() {
			@Override
			public List<Map<String, String>> doInHive(HiveClient hiveClient) throws Exception {
				List<Map<String, String>> list = Lists.newArrayList();

				Connection conn = hiveClient.getConnection();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql);

				Map<String, String> map = Maps.newHashMap();
				while (rs.next()) {
					map.keySet().removeIf(key -> key != "1");
					map.put("appid", rs.getString("appid"));
					map.put("pvCount", rs.getString("pv_count"));
					map.put("method", rs.getString("method"));
					list.add(map);
				}
				return list;
			}
		});

		list.forEach(m -> {
			PvHttpClient pvHttpClient = new PvHttpClient();
			pvHttpClient.setAppid(m.get("appid"));
			pvHttpClient.setMethod(m.get("method"));
			pvHttpClient.setPvCount( Long.parseLong( m.get("pvCount") ) );
			pvHttpClient.setRequestTime(DateUtils.strToDate(yearMonthDayHour));
			pvHttpClient.setCreateTime(new Date());
			httpClientPVRepository.save(pvHttpClient);
		});

		log.info("end->saveHttpClient");
	}

}
