package com.kdf.web.server.controller.statistics;

import java.time.LocalDate;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.kdf.etl.vo.BrowserAndOsUvVO;
import com.kdf.etl.vo.ClientTypeVO;
import com.kdf.web.server.service.DeviceUvService;
import com.kdf.web.server.service.ProjectService;
/**
 * 
 * @title: DeviceUvController.java 
 * @package com.kdf.web.server.controller.statistics 
 * @description: 终端uv统计
 * @author: 、T
 * @date: 2019年10月15日 上午11:30:05 
 * @version: V1.0
 */
@RestController
@RequestMapping("deviceUv")
public class DeviceUvController {

	@Autowired
	private DeviceUvService deviceService;
	
	@Autowired
	 private ProjectService projectService;
	 
	
	/**
	 * 
	 * @title: toDevice 
	 * @description: 跳转终端页面
	 * @author: 、T
	 * @date: 2019年10月15日 上午11:12:49
	 * @return
	 * @throws:
	 */
	@GetMapping("")
	public ModelAndView toDevice() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("html/device_uv");
		return modelAndView;
	}
	
	/**
	 * 
	 * @title: getBrowserOrOsUvData 
	 * @description: 获取柱状图数据
	 * @author: 、T
	 * @date: 2019年10月15日 上午11:13:13
	 * @param appid				项目appid
	 * @param searchDate	查询时间(目前只支持按天查询)
	 * @param barType			柱状图类型(browserBar为浏览器uv，osBar为操作系统uv)
	 * @return
	 * @throws:
	 */
	@PostMapping("getUvBarData")
	public List<BrowserAndOsUvVO> getBrowserOrOsUvData(String searchDate, String barType) {
		String appid  = projectService.getCurrentAppid();
		LocalDate nowDate = LocalDate.now();
		if(StringUtils.isNotBlank(searchDate)) {
			nowDate = LocalDate.parse(searchDate);
		}
		return deviceService.getBrowserOrOsVoList(appid, nowDate, barType);
	}
	
	/**
	 * 
	 * @title: getClientTypeUvData 
	 * @description: 获取折线图数据
	 * @author: 、T
	 * @date: 2019年10月15日 上午11:14:53
	 * @param appid				项目appid
	 * @param searchDate	查询时间(目前只支持按天查询)
	 * @return
	 * @throws:
	 */
	@PostMapping("getUvLineData")
	public List<ClientTypeVO> getClientTypeUvData(String searchDate) {
		String appid  = projectService.getCurrentAppid();
		LocalDate nowDate = LocalDate.now();
		if(StringUtils.isNotBlank(searchDate)) {
			nowDate = LocalDate.parse(searchDate);
		}
		return deviceService.getClientTypeVoList(appid, nowDate);
	}
}
