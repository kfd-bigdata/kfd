package com.kdf.web.server.controller.system;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.kdf.web.server.bean.PbRole;
import com.kdf.web.server.bean.PbUser;
import com.kdf.web.server.dto.ReturnDTO;
import com.kdf.web.server.service.RoleService;
import com.kdf.web.server.utils.GetUserInfoUtil;

/**    
 * 角色Controller
 * @Package: com.admin.server.controller.system 
 * @author: LiWenLong  
 * @date: 2019年8月5日 下午1:41:17 
 */
@RestController
@RequestMapping("role")
public class RoleController {
	
	@Autowired
	private RoleService roleService;
	
	@GetMapping("")
	public ModelAndView toRole() {
		return new ModelAndView("html/system/role");
	}
	

	/**
	 * list 角色列表
	 * 
	 * @param page  页码
	 * @param limit 数量
	 * @return
	 */
	@GetMapping("list")
	public ReturnDTO list(Integer page, Integer limit) {
		Page<PbRole> roleBeanPage = roleService.findByDelFlagPage(0, page, limit);
		ReturnDTO returnBean = new ReturnDTO();
		returnBean.setCode(200);
		returnBean.setMsg("成功");
		returnBean.setCount(roleBeanPage.getTotalElements());
		returnBean.setData(roleBeanPage.getContent());
		return returnBean;
	}

	/**
	 * add 添加角色
	 * 
	 * @param session
	 * @param bean    角色信息
	 * @return
	 */
	@PostMapping("add")
	public Boolean add(HttpSession session, PbRole bean) {
		Boolean b = true;
		try {
			PbUser user = GetUserInfoUtil.getUserInfo(session);
			bean.setDelFlag(0);
			bean.setCreateTime(new Date());
			bean.setCreateUserId(user.getUserId());
			roleService.save(bean);
		} catch (Exception e) {
			e.printStackTrace();
			b = false;
		}
		return b;
	}

	/**
	 * del删除
	 * 
	 * @param session
	 * @param ids     角色id集合
	 * @return
	 */
	@PostMapping("del")
	public Boolean del(HttpSession session, Integer roleId) {
		Boolean b = true;
		try {
			PbUser user = GetUserInfoUtil.getUserInfo(session);
			roleService.del(roleId, user.getUserId());
		} catch (Exception e) {
			e.printStackTrace();
			b = false;
		}
		return b;
	}
	
	@PostMapping("getButtonsByMenuId")
	public List<Map<String,Object>> getButtonsByMenuId(Integer menuId, Integer roleId) {
		return roleService.selectButtonsByMenuId(menuId,roleId);
	}
}
