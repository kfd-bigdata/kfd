package com.kdf.web.server.controller.pvurl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.kdf.web.server.service.ProjectService;
import com.kdf.web.server.service.PvUrlService;

/**
 * pvurl Controller
 * @author 李文龙
 *
 */
@RestController
@RequestMapping("pvUrl")
public class PvUrlController {

    @Autowired
    private PvUrlService pvService;
    
    @Autowired
    private ProjectService projectService;
    
    
    /**
     * 跳转页面方法
     * @return
     */
    @GetMapping("")
    public ModelAndView toPvUrl(){
        return new ModelAndView("html/pvurl/pvurl");
    }
    
    /**
     * 获取当前项目的url总pv
     * @return
     */
    @PostMapping("getPvUrlList")
    public List<Map<String, Object>> getPvUrlList(){
        return pvService.selectPvUrlList(projectService.getCurrentAppid());
    }
    
    /**
     * 跳转页面方法
     * @return
     */
    @GetMapping("toPvUrlTime")
    public ModelAndView toPvUrlTime(){
        return new ModelAndView("html/pvurl/pvurltime");
    }
    
    /**
     * 获取urlpv时间分布图
     * @return
     */
    @PostMapping("getPvUrlTime")
    public List<Map<String, Object>> getPvUrlTime(){
        return pvService.selectPvUrlTime(projectService.getCurrentAppid());
    }
}
