package com.kdf.web.server.controller.system;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.kdf.web.server.bean.PbButton;
import com.kdf.web.server.bean.PbUser;
import com.kdf.web.server.dto.ReturnDTO;
import com.kdf.web.server.service.ButtonService;
import com.kdf.web.server.utils.GetUserInfoUtil;
/**
 * 
 * @ClassName:  ButtonController   
 * @Description:按钮控制器
 * @author: xubo 976341983@qq.com
 * @date:   2019年8月13日 上午10:07:05
 */
@RestController
@RequestMapping("button")
public class ButtonController {
	
	@Autowired
	private ButtonService buttonService;

	/**
	 * toMenu 按钮页
	 * 
	 * @return
	 */
	@GetMapping("")
	public ModelAndView toButton() {
		return new ModelAndView("html/system/button");
	}

	/**
	 * list 按钮列表
	 * 
	 * @return
	 */
	@GetMapping("list")
	public ReturnDTO list(Integer page, Integer limit) {
		Page<PbButton> buttonBeanList = buttonService.findByDelFlag(0,page,limit);
		ReturnDTO returnBean = new ReturnDTO();
		returnBean.setCode(200);
		returnBean.setMsg("成功");
		returnBean.setCount(buttonBeanList.getTotalElements());
		returnBean.setData(buttonBeanList.getContent());
		return returnBean;
	}

	/**
	 * add 添加按钮
	 * 
	 * @param session
	 * @param bean    按钮信息
	 * @return
	 */
	@PostMapping("add")
	public Boolean add(HttpSession session, PbButton bean) {
		Boolean b = true;
		try {
			PbUser user = GetUserInfoUtil.getUserInfo(session);
			bean.setCreateUserId(user.getUserId());
			bean.setCreateTime(new Date());
			bean.setDelFlag(0);
			buttonService.save(bean);
		} catch (Exception e) {
			e.printStackTrace();
			b = false;
		}
		return b;
	}

	/**
	 * del 删除按钮
	 * 
	 * @param session
	 * @param id      按钮id
	 * @return
	 */
	@PostMapping("del")
	public Boolean del(HttpSession session, Integer buttonId) {
		Boolean b = true;
		try {
			PbUser user = GetUserInfoUtil.getUserInfo(session);
			buttonService.delById(buttonId, user.getUserId());
		} catch (Exception e) {
			e.printStackTrace();
			b = false;
		}
		return b;
	}

	/**
	 * updata 修改按钮
	 * 
	 * @param session
	 * @param bean    按钮信息
	 * @return
	 */
	@PostMapping("update")
	public Boolean updata(HttpSession session, PbButton bean) {
		Boolean b = true;
		try {
			PbUser user = GetUserInfoUtil.getUserInfo(session);
			buttonService.updataById(bean, user.getUserId());
		} catch (Exception e) {
			e.printStackTrace();
			b = false;
		}
		return b;
	}
	
	/**
	 * 
	 * @Title: getButtons   
	 * @Description: 获取当前菜单按钮集
	 * @param:  session
	 * @param:  menuId
	 * @param: @return      
	 * @return: List<PbButton>      
	 * @throws
	 */
	@PostMapping("getMenuButtons")
	public List<PbButton> getButtons(HttpSession session, Integer menuId){
		PbUser user = GetUserInfoUtil.getUserInfo(session);
		List<PbButton> list = buttonService.getButtons(user.getUserId(),menuId);
		return list;
	}
	
}
