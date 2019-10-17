package com.kdf.etl.service;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.hadoop.hive.HiveTemplate;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.kdf.etl.bean.UvTimeDistribution;
import com.kdf.etl.repository.UvTimeDistributionRepository;
import com.kdf.etl.utils.DateUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UvTimeDistributionService {

	@Autowired
	private HiveTemplate hiveTemplate;

	@Autowired
	private UvTimeDistributionRepository uvTimeDistributionRepository;

	public void saveUvTimeDistribution(String yearMonthDayHour) {
		String sql = "select count(1) as uvCount, appid from pv_log_hive_" + yearMonthDayHour + " group by appid, ip";
		log.info("star->getUvTimeDistribution:yearMonthDayHour={},sql={}", yearMonthDayHour, sql);
		try {
			ResultSet rs = hiveTemplate.execute(h -> h.getConnection().createStatement().executeQuery(sql));

			List<Map<String, String>> list = Lists.newArrayList();
			Map<String, String> map = Maps.newHashMap();
			while (rs.next()) {
				map.keySet().removeIf(key -> key != "1");

				map.put("appid", rs.getString("appid"));
				map.put("uvCount", rs.getString("uvCount"));
				list.add(map);
			}

			list.forEach(m -> {
				UvTimeDistribution uvTimeDistribution = new UvTimeDistribution();
				uvTimeDistribution.setAppid(m.get("appid"));
				uvTimeDistribution.setUvCount(Long.valueOf(m.get("uvCount")));
				uvTimeDistribution.setRequestTime(DateUtils.strToDate(yearMonthDayHour));
				uvTimeDistributionRepository.save(uvTimeDistribution);
			});
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		log.info("end->getUvTimeDistribution");
	}

	public UvTimeDistribution getUvTimeDistribution(String yearMonthDayHour) {
		return uvTimeDistributionRepository.findByRequestTime(DateUtils.strToDate(yearMonthDayHour));
	}

	public List<UvTimeDistribution> getAreaUvTimeDistribution(String startYearMonthDayHour,
			String endYearMonthDayHour) {
		return uvTimeDistributionRepository.findByRequestTimeBetween(DateUtils.strToDate(startYearMonthDayHour),
				DateUtils.strToDate(endYearMonthDayHour));
	}

}
