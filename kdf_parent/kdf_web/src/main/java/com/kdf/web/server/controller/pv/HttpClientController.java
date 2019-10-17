package com.kdf.web.server.controller.pv;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.druid.util.StringUtils;
import com.kdf.etl.bean.PvHttpClient;
import com.kdf.web.server.service.HttpClientService;

/**
 * 客户端请求方式pv总统计
 */
@RestController
@RequestMapping("httpClient")
public class HttpClientController {

	@Autowired
	private HttpClientService httpClientService;
	
	/**
	 * GET请求
	 */
	private static final String GET = "GET";
	
	/**
	 * POST请求
	 */
	private static final String POST = "POST";
	
	/**
	 * DELETE请求
	 */
	private static final String DELETE = "DELETE";
	
	/**
	 * PUT请求
	 */
	private static final String PUT = "PUT";
	
	/**
	 * toMenu 跳转客户端请求方式pv总统计页面
	 * 
	 * @return
	 */
	@GetMapping("")
	public ModelAndView toMenu(HttpSession session,String startTime,String endTime) {
		ModelAndView mv = new ModelAndView("html/pv/httpClient");
		mv.addObject("startTime", startTime);
		mv.addObject("endTime", endTime);
		return new ModelAndView("html/pv/httpClient");
	}
	
	/**
	 * 客户端请求方式pv总统计查询
	 */
	@PostMapping("list")
	public Map<String,List<PvHttpClient>> list(HttpSession session,String startTime,String endTime) {
		String appid = "pbkj_ly";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Map<String,List<PvHttpClient>> map = null;
		try {
			map = new HashMap<>();
			if (!StringUtils.isEmpty(startTime) && !StringUtils.isEmpty(endTime)) {
				Date startTimeDate = sdf.parse(startTime);
				Date endTimeDate = sdf.parse(endTime);
				List<PvHttpClient> list = httpClientService.findByAppidAndRequestTimeBetween(appid,startTimeDate,endTimeDate);
				List<PvHttpClient> getList = list.stream().filter(a -> GET.equals(a.getMethod())).collect(Collectors.toList());
				map.put(GET, getList);
				List<PvHttpClient> postList = list.stream().filter(a -> POST.equals(a.getMethod())).collect(Collectors.toList());
				map.put(POST, postList);
				List<PvHttpClient> deleteList = list.stream().filter(a -> DELETE.equals(a.getMethod())).collect(Collectors.toList());
				map.put(DELETE, deleteList);
				List<PvHttpClient> putList = list.stream().filter(a -> PUT.equals(a.getMethod())).collect(Collectors.toList());
				map.put(PUT, putList);
			} else {
//				list = httpClientService.findByAppid(appid);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	
}
