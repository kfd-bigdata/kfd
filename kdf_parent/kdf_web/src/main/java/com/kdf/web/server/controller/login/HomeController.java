package com.kdf.web.server.controller.login;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.kdf.web.server.base.BaseResponse;
import com.kdf.web.server.base.ResultCode;
import com.kdf.web.server.service.ProjectService;
import com.kdf.web.server.service.PvAllService;
import com.kdf.web.server.service.UvService;
import com.kdf.web.server.service.UvUserAreaDistributionService;

import lombok.extern.slf4j.Slf4j;

/**
 * HomeController home页控制器
 * 
 * @author mengpp
 * @date 2019年10月16日08:55:25
 */
@Slf4j
@RestController
@RequestMapping("home")
public class HomeController {

	@Autowired
	private UvService uvService;

	@Autowired
	private PvAllService pvAllService;

	@Autowired
	private UvUserAreaDistributionService uvUserAreaDistributionService;

	@Autowired
	private ProjectService projectService;

	@GetMapping("")
	public ModelAndView home() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("html/home");
		return modelAndView;
	}

	/**
	 * count 统计pv、uv各项指标数据
	 * 
	 * @return
	 */
	@GetMapping("count")
	public ResponseEntity<BaseResponse<Map<String, Long>>> count() {
		ResultCode code = ResultCode.OK;
		BaseResponse<Map<String, Long>> br = new BaseResponse<Map<String, Long>>(code);
		try {
			String appid = projectService.getCurrentAppid();

			// 时间
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new Date());
			Date time;

			// 统计总uv
			Long uvCount = uvService.countByRequestTimeAndAppid(null, appid);
			Long uvAvg = uvService.avgByRequestTimeAndAppid(null, appid);

			// 统计总pv
			Long pvCount = pvAllService.countByRequestTimeAndAppid(null, appid);
			Long pvAvg = pvAllService.avgByRequestTimeAndAppid(null, appid);

			// 今日总uv/pv
			time = calendar.getTime();
			Long uvToday = uvService.countByRequestTimeAndAppid(time, appid);
			Long pvToday = pvAllService.countByRequestTimeAndAppid(time, appid);

			// 昨日总uv/pv
			calendar.add(Calendar.DATE, -1);
			time = calendar.getTime();
			Long uvYesterday = uvService.countByRequestTimeAndAppid(time, appid);
			Long pvYesterday = pvAllService.countByRequestTimeAndAppid(time, appid);

			Map<String, Long> returnMap = new HashMap<String, Long>(8);
			returnMap.put("uvCount", uvCount);
			returnMap.put("uvAvg", uvAvg);
			returnMap.put("pvCount", pvCount);
			returnMap.put("pvAvg", pvAvg);
			returnMap.put("uvToday", uvToday);
			returnMap.put("pvToday", pvToday);
			returnMap.put("uvYesterday", uvYesterday);
			returnMap.put("pvYesterday", pvYesterday);

			br.setData(returnMap);
		} catch (Exception e) {
			code = ResultCode.ERROR;
			br.setCode(code.getCode());
			br.setMessage(code.getMsg());
			br.setData(null);
			log.error(e.getMessage());
		}
		return br.sendResponse();
	}

	/**
	 * todayYesterdayContrast pv昨日今日对比
	 * 
	 * @return
	 */
	@GetMapping("todayYesterdayContrast")
	public ResponseEntity<BaseResponse<Map<String, Object>>> todayYesterdayContrast() {
		ResultCode code = ResultCode.OK;
		BaseResponse<Map<String, Object>> br = new BaseResponse<Map<String, Object>>(code);
		try {
			String appid = projectService.getCurrentAppid();

			Map<String, Object> map = new HashMap<String, Object>(3);
			List<String> dateList = new ArrayList<String>(2);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new Date());
			Date time;

			// 今日
			time = calendar.getTime();
			dateList.add(sdf.format(time));

			List<Long> todayData = pvAllService.findByAppidAndAndRequestTime(appid, time);

			// 昨日
			calendar.add(Calendar.DATE, -1);
			time = calendar.getTime();
			dateList.add(sdf.format(time));

			List<Long> yesterdayData = pvAllService.findByAppidAndAndRequestTime(appid, time);

			map.put("time", dateList);
			map.put("todayData", todayData);
			map.put("yesterdayData", yesterdayData);

			br.setData(map);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return br.sendResponse();
	}

	/**
	 * projectPv 统计项目总pv或uv
	 * 
	 * @param type 类型（1：pv，2：uv）
	 * @return
	 */
	@GetMapping("projectCount")
	public ResponseEntity<BaseResponse<List<Object>>> projectPv(Integer type) {
		ResultCode code = ResultCode.OK;
		BaseResponse<List<Object>> br = new BaseResponse<List<Object>>(code);
		try {
			List<Object> project = new ArrayList<Object>();
			List<Map<String, Object>> mapList;

			if (type.equals(1)) {
				mapList = pvAllService.countGroupByAppid();
			} else {
				mapList = uvService.countGroupByAppid();
			}

			// 获取日期
			List<Object> product = mapList.stream().map(m -> m.get("requestTime")).distinct().sorted()
					.collect(Collectors.toList());
			product.add(0, "product");
			project.add(product);

			// 获取项目及项目pv
			mapList.stream().collect(Collectors.groupingBy(e -> e.get("projectName"),
					Collectors.mapping(e -> e.get("countNum"), Collectors.toList()))).forEach((k, v) -> {
						v.add(0, k);
						project.add(v);
					});

			br.setData(project);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return br.sendResponse();
	}

	@GetMapping("mapCount")
	public ResponseEntity<BaseResponse<List<Map<String, Object>>>> mapCount() {
		ResultCode code = ResultCode.OK;
		BaseResponse<List<Map<String, Object>>> br = new BaseResponse<List<Map<String, Object>>>(code);
		try {
			String appid = projectService.getCurrentAppid();
			List<Map<String, Object>> mapList = uvUserAreaDistributionService.countGroupByProvince(appid);
			br.setData(mapList);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return br.sendResponse();
	}

}
