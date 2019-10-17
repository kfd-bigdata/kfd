package com.kdf.etl.contoller.uv;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.beust.jcommander.internal.Lists;
import com.beust.jcommander.internal.Maps;
import com.kdf.etl.service.UvUserAreaDistributionService;

import lombok.extern.slf4j.Slf4j;

/**
 * 用户地域分布控制器
 * @ClassName: UvUserAreaDistributionController   
 * @author: PéiGǔangTíng QQ：1396968024
 * @date: 2019年10月12日 上午11:12:10
 */
@RestController
@RequestMapping("/area")
public class UvUserAreaDistributionController {
    
    @Autowired
    private UvUserAreaDistributionService areaDistributionService;
	
    /**
     * 获取地区访问量
     * @Title: getUserAreaByTime     
     * @throws
     */
    @GetMapping("/getAreaInfo")
    public List<Map<String, Object>> getUserAreaByTime(@RequestParam("yearMonthDayHour") String yearMonthDayHour) {
        List<Map<String, Object>> resultListMap = Lists.newArrayList();
        try {
            resultListMap = areaDistributionService.getAreaDistributionHive(yearMonthDayHour);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultListMap;
    }
}
