package com.kdf.web.server.controller.statistics;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.druid.util.StringUtils;
import com.kdf.web.server.bean.PbUser;
import com.kdf.web.server.service.PvAllService;
/**
 * 
 * @ClassName:  AllPvController   
 * @Description:总pv分布统计
 * @author: LiShuangshuang
 * @date:   2019年10月12日 下午3:24:29
 */
@RestController
@RequestMapping("allpv")
public class AllPvController {

	@Autowired
	private PvAllService pvAllService;
	
	@GetMapping("")
	public ModelAndView allPv() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("html/allpv");
		return modelAndView;
	}
	/**
	 * 
	 * @Title: getPvAllList   
	 * @Description: 获取总pv数量
	 * @param startTime
	 * @param endTime
	 * @return: List<Map<String,Object>>      
	 * @throws
	 */
	@PostMapping("getPvAllList")
	public List<Map<String, Object>> getPvAllList(HttpSession session, String startTime, String endTime) {
		// 查找用户所属的项目，找到对应的appid
		PbUser user = (PbUser) session.getAttribute("user");
		// TODO
		// 查找当前项目的总pv数
		String appid = "pbkj_123";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		List<Map<String, Object>> listss = null;
		try {
			if (!StringUtils.isEmpty(startTime) && !StringUtils.isEmpty(endTime)) {
				Date startTimeDate = sdf.parse(startTime);
				Date endTimeDate = sdf.parse(endTime);
				//pvAllList = pvAllService.getPvAllList(appid, startTimeDate, endTimeDate);
				listss = pvAllService.findAllPvMap(appid);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return listss;
	}
}
