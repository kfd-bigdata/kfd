package com.kdf.web.server.controller.statistics;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.beust.jcommander.internal.Lists;
import com.beust.jcommander.internal.Maps;
import com.kdf.etl.bean.UvUserAreaDistribution;
import com.kdf.web.server.dto.ReturnDTO;
import com.kdf.web.server.service.UvUserAreaDistributionService;

/**
 * 用户控制器 
 * @ClassName: UserController   
 * @author: PéiGǔangTíng
 * @date: 2019年8月5日 下午1:59:17
 */
@RestController
@RequestMapping("area")
public class UvUserAreaDistributionController {
    
    @Autowired
    private UvUserAreaDistributionService uvUserAreaDistributionService;
    
    @GetMapping("")
    private ModelAndView toArea() {
        return new ModelAndView("html/user/userAreaDistribution");
    }
    
    /**
     * 查询所有地域分布数据
     * @Title: areaInfo     
     * @return Map<String,Object>    
     * @throws
     */
    @GetMapping("areaInfo")
    public ReturnDTO areaInfo() {
        // 查询所有省份及个数
        List<UvUserAreaDistribution> userAreaDistributionList = uvUserAreaDistributionService.findAll();
        ReturnDTO returnBean = new ReturnDTO();
        returnBean.setCode(200);
        returnBean.setMsg("成功");
        JSONArray jsonArray = new JSONArray();
        userAreaDistributionList.forEach(userAreaDistribution -> {
            JSONObject obj = new JSONObject();
            obj.put("name", userAreaDistribution.getProvince());
            obj.put("value", userAreaDistribution.getStartCount());
            jsonArray.add(obj);
        });
        returnBean.setData(jsonArray);
        return returnBean;
    }
}
