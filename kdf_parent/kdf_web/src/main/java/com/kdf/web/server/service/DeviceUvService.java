package com.kdf.web.server.service;

import java.time.LocalDate;
import java.util.List;

import com.kdf.etl.vo.BrowserAndOsUvVO;
import com.kdf.etl.vo.ClientTypeVO;

/**
 * 
 * @title: DeviceUvService.java 
 * @package com.kdf.web.server.service 
 * @description: 终端页面service接口
 * @author: 、T
 * @date: 2019年10月15日 上午11:15:28 
 * @version: V1.0
 */
public interface DeviceUvService {
	/**
	 * 
	 * @title: getBrowserOrOsVoList 
	 * @description: 获取浏览器或操作系统的uv
	 * @author: 、T
	 * @date: 2019年10月15日 上午11:15:44
	 * @param appid				项目appid
	 * @param searchDate	查询时间(目前只支持按天查询)
	 * @param barType			柱状图类型(browserBar为浏览器uv，osBar为操作系统uv)
	 * @return  BrowserAndOsUvVO
	 * @throws:
	 */
	public List<BrowserAndOsUvVO> getBrowserOrOsVoList(String appid, LocalDate searchDate, String barType);
	
	/**
	 * 
	 * @title: getClientTypeVoList 
	 * @description: 获取客户端类型的uv
	 * @author: 、T
	 * @date: 2019年10月15日 上午11:16:43
	 * @param appid				项目appid
	 * @param searchDate	查询时间(目前只支持按天查询)
	 * @return  ClientTypeVO
	 * @throws:
	 */
	public List<ClientTypeVO> getClientTypeVoList(String appid, LocalDate searchDate);
}
