package com.kdf.etl.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kdf.etl.bean.UvBrowser;
import com.kdf.etl.bean.UvClientType;
import com.kdf.etl.bean.UvOs;
import com.kdf.etl.repository.UvBrowserRepository;
import com.kdf.etl.repository.UvClientTypeRepository;
import com.kdf.etl.repository.UvOsRepository;
import com.kdf.etl.utils.DateUtils;

/**
 * 
 * @title: UvService.java 
 * @package com.kdf.etl.service 
 * @description: uv统计service
 * @author: 、T
 * @date: 2019年10月11日 下午2:20:54 
 * @version: V1.0
 */
@Service
public class UvService {

	@Autowired
	private UvHiveService uvHiveService;
	
	@Autowired
	private UvBrowserRepository uvBrowerRepository;
	@Autowired
	private UvOsRepository uvOsRepository;
	@Autowired
	private UvClientTypeRepository uvClientTypeRepository;
	
	/**
	 * 
	 * @title: browserUv2MySql 
	 * @description: 统计浏览器的uv到mysql
	 * @author: 、T
	 * @date: 2019年10月11日 下午3:27:55
	 * @param yearMonthDayHour
	 * @throws:
	 */
	public void browserUv2Mysql(String yearMonthDayHour) {
		List<Map<String, String>> uvList = uvHiveService.getBrowserUv(yearMonthDayHour);
		if(CollectionUtils.isNotEmpty(uvList)) {
			uvList.stream().forEach(map -> {
				String appid = String.valueOf(map.get("appid"));
				String browserName = String.valueOf(map.get("browserName"));
				Long count = Long.valueOf(String.valueOf(map.get("count")));
				UvBrowser uvBrowser = new UvBrowser();
				uvBrowser.setBrowserName(browserName);
				uvBrowser.setUvCount(count);
				uvBrowser.setAppid(appid);
				uvBrowser.setRequestTime(DateUtils.strToDate(yearMonthDayHour));
				uvBrowerRepository.save(uvBrowser);
			});
		}
	}
	/**
	 * 
	 * @title: osUv2Mysql 
	 * @description: 统计os名称和版本到mysql
	 * @author: ShiYulong
	 * @date: 2019年10月12日13:40:19
	 * @param yearMonthDayHour
	 * @throws:
	 */
	public void osUv2Mysql(String yearMonthDayHour) {
		List<Map<String, String>> uvList = uvHiveService.getOsUv(yearMonthDayHour);
		if(CollectionUtils.isNotEmpty(uvList)) {
			uvList.stream().forEach(map -> {
				String appid = String.valueOf(map.get("appid"));
				String osName = String.valueOf(map.get("osName"));
				Long count = Long.valueOf(String.valueOf(map.get("count")));
				UvOs uvOs = new UvOs();
				uvOs.setAppid(appid);
				uvOs.setOsName(osName);
				uvOs.setRequestTime(DateUtils.strToDate(yearMonthDayHour));
				uvOs.setCreateTime(new Date());
				uvOs.setUvCount(count);
				uvOsRepository.save(uvOs);
			});
		}
	}
	/**
	 * 
	 * @title: clientTypeUv2Mysql 
	 * @description: 统计连接类型到mysql
	 * @author: ShiYulong
	 * @date: 2019年10月12日14:14:37
	 * @param yearMonthDayHour
	 * @throws:
	 */
	public void clientTypeUv2Mysql(String yearMonthDayHour) {
		List<Map<String, String>> uvList = uvHiveService.getClientTypeUv(yearMonthDayHour);
		if(CollectionUtils.isNotEmpty(uvList)) {
			uvList.stream().forEach(map -> {
				String appid = String.valueOf(map.get("appid"));
				Long mobileCount = Long.valueOf(map.get("mobileCount"));
				Long pcCount = Long.valueOf(String.valueOf(map.get("pcCount")));
				UvClientType uvClientType = new UvClientType();
				uvClientType.setAppid(appid);
				uvClientType.setRequestTime(DateUtils.strToDate(yearMonthDayHour));
				uvClientType.setCreateTime(new Date());
				uvClientType.setMobileUvCount(mobileCount);
				uvClientType.setPcUvCount(pcCount);
				uvClientTypeRepository.save(uvClientType);
			});
	}
	}
	
	
	
}
