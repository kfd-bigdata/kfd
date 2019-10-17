package com.kdf.web.server.service.impl;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kdf.etl.repository.UvBrowserRepository;
import com.kdf.etl.repository.UvClientTypeRepository;
import com.kdf.etl.repository.UvOsRepository;
import com.kdf.etl.vo.BrowserAndOsUvVO;
import com.kdf.etl.vo.ClientTypeVO;
import com.kdf.web.server.service.DeviceUvService;
import com.kdf.web.server.utils.DateUtil;

import cn.hutool.core.collection.CollectionUtil;
/**
 * 
 * @title: DeviceUvServiceImpl.java 
 * @package com.kdf.web.server.service.impl 
 * @description: 终端页面service实现类
 * @author: 、T
 * @date: 2019年10月15日 上午11:17:28 
 * @version: V1.0
 */
@Service
public class DeviceUvServiceImpl implements DeviceUvService {
	// 浏览器柱状图类型
	private static final String BROWSER_BAR = "browserBar";
	// 操作系统柱状图类型
	private static final String OS_BAR = "osBar";
	
	@Autowired
	private UvBrowserRepository browserRepository;
	
	@Autowired
	private UvOsRepository osRepository;
	
	@Autowired
	private UvClientTypeRepository clientTypeRepository;
	
	/**
	 * 
	 * @title: getBrowserOrOsVoList
	 * @description:  获取浏览器或操作系统的uv
	 * @param appid				项目appid
	 * @param searchDate	查询时间(目前只支持按天查询)
	 * @param barType			柱状图类型(browserBar为浏览器uv，osBar为操作系统uv)
	 * @return  BrowserAndOsUvVO
	 * @see com.kdf.web.server.service.DeviceUvService#getBrowserOrOsVoList(java.lang.String, java.time.LocalDate, java.lang.String)
	 */
	@Override
	public List<BrowserAndOsUvVO> getBrowserOrOsVoList(String appid, LocalDate searchDate, String barType) {
		Date startDate = Date.from(searchDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
		Date endDate = Date.from(searchDate.plusDays(1L).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
		List<BrowserAndOsUvVO> voList = new ArrayList<>(16);
		if(BROWSER_BAR.equals(barType)) {
			voList = browserRepository.getBrowserVoList(appid, startDate, endDate);
		}
		if(OS_BAR.equals(barType)) {
			voList = osRepository.getOsVoList(appid, startDate, endDate);
		}
		return voList;
	}

	/**
	 * 
	 * @title: getClientTypeVoList
	 * @description: 获取客户端类型的uv
	 * @param appid				项目appid
	 * @param searchDate	查询时间(目前只支持按天查询)
	 * @return   ClientTypeVO
	 * @see com.kdf.web.server.service.DeviceUvService#getClientTypeVoList(java.lang.String, java.time.LocalDate)
	 */
	@Override
	public List<ClientTypeVO> getClientTypeVoList(String appid, LocalDate searchDate) {
		Date startDate = Date.from(searchDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
		Date endDate = Date.from(searchDate.plusDays(1L).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
		List<ClientTypeVO> voList = clientTypeRepository.getClientTypeVoList(appid, startDate, endDate);
		List<ClientTypeVO> resultList = new ArrayList<>(voList.size());
		if(CollectionUtil.isNotEmpty(voList)) {
			voList.stream().forEach(vo -> {
				vo.setRequestTime(DateUtil.getHourStr(vo.getRequestTime()));
				resultList.add(vo);
			});
		}
		return resultList;
	}

}
