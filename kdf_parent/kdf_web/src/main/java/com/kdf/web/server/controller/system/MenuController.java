package com.kdf.web.server.controller.system;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.kdf.web.server.base.BaseResponse;
import com.kdf.web.server.base.ResultCode;
import com.kdf.web.server.bean.PbButton;
import com.kdf.web.server.bean.PbMenu;
import com.kdf.web.server.bean.PbUser;
import com.kdf.web.server.dto.MenuDTO;
import com.kdf.web.server.dto.ReturnDTO;
import com.kdf.web.server.service.ButtonService;
import com.kdf.web.server.service.MenuService;
import com.kdf.web.server.utils.GetUserInfoUtil;

@RestController
@RequestMapping("menu")
public class MenuController {

	
	@Autowired
	private MenuService menuService;

	@Autowired
	private ButtonService buttonService;
	
	/**
	 * toMenu 菜单页
	 * 
	 * @return
	 */
	@GetMapping("")
	public ModelAndView toMenu() {
		return new ModelAndView("html/system/menu");
	}

	/**
	 * list 菜单列表
	 * 
	 * @return
	 */
	@GetMapping("list")
	public ReturnDTO list() {
		List<PbMenu> menuBeanList = menuService.findByDelFlag(0);
		ReturnDTO returnBean = new ReturnDTO();
		returnBean.setCode(200);
		returnBean.setMsg("成功");
		returnBean.setData(menuBeanList);
		return returnBean;
	}

	/**
	 * tree 菜单树
	 * 
	 * @param roleId 角色id
	 * @return
	 */
	@PostMapping("tree")
	public Map<String, Object> tree(Integer roleId) {
		List<MenuDTO> menuDTOList = menuService.findByDelFlagAndRoleIdToTree(0, roleId);
		Map<String, Object> map = new HashMap<String, Object>(2);
		map.put("roleId", roleId);
		map.put("menu", menuDTOList);
		return map;
	}

	@PostMapping("buttonTree")
	public Map<String, Object> buttonTree(Integer roleId) {
		List<MenuDTO> menuDTOList = menuService.buttonTree(roleId);
		Map<String, Object> map = new HashMap<String, Object>(2);
		map.put("roleId", roleId);
		map.put("menu", menuDTOList);
		return map;
	}
	/**
	 * addRoleMenu 保存关联菜单
	 * 
	 * @param session
	 * @param roleId  角色id
	 * @param ids     菜单id集合
	 * @return
	 */
	@PostMapping("addRoleMenu")
	public Boolean addRoleMenu(HttpSession session, Integer roleId, Integer[] ids) {
		Boolean b = true;
		try {
			PbUser user = GetUserInfoUtil.getUserInfo(session);
			menuService.addRoleMenu(user.getUserId(), roleId, ids);
		} catch (Exception e) {
			e.printStackTrace();
			b = false;
		}
		return b;
	}
	
	@PostMapping("addRoleMenuButtons")
	public Boolean addRoleMenuButtons(Integer menuId, Integer roleId, Integer[] ids) {
		Boolean b = true;
		try {
			menuService.addRoleMenuButtons(menuId, roleId, ids);
		} catch (Exception e) {
			e.printStackTrace();
			b = false;
		}
		return b;
	}

	/**
	 * add 添加菜单
	 * 
	 * @param session
	 * @param bean    菜单信息
	 * @return
	 */
	@PostMapping("add")
	public Boolean add(HttpSession session, PbMenu bean) {
		Boolean b = true;
		try {
			PbUser user = GetUserInfoUtil.getUserInfo(session);
			bean.setCreateUserId(user.getUserId());
			bean.setCreateTime(new Date());
			bean.setDelFlag(0);
			menuService.save(bean);
		} catch (Exception e) {
			e.printStackTrace();
			b = false;
		}
		return b;
	}

	/**
	 * del 关联菜单
	 * 
	 * @param session
	 * @param id      菜单id
	 * @return
	 */
	@PostMapping("del")
	public Boolean del(HttpSession session, Integer menuId) {
		Boolean b = true;
		try {
			PbUser user = GetUserInfoUtil.getUserInfo(session);
			menuService.delById(menuId, user.getUserId());
		} catch (Exception e) {
			e.printStackTrace();
			b = false;
		}
		return b;
	}

	/**
	 * updata 修改菜单
	 * 
	 * @param session
	 * @param bean    菜单信息
	 * @return
	 */
	@PostMapping("update")
	public ResponseEntity<BaseResponse<PbMenu>> updata(HttpSession session, PbMenu bean) {
		BaseResponse<PbMenu> bp = new BaseResponse<>(ResultCode.OK.getCode(), "ok");
		Boolean b = true;
		try {
			PbUser user = GetUserInfoUtil.getUserInfo(session);
			b=menuService.updataById(bean, user.getUserId());
			if (!b) {
				bp.setCode(ResultCode.HAS_BEEN_ASSOCIATED.getCode());
				bp.setMessage("请先删除角色菜单关联的按钮后再进行修改");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bp.sendResponse();
	}
	
	@PostMapping("getAllButton")
	public List<PbButton> getAllButton(){
		return buttonService.findByDelFlag(0, 1, 999).getContent();
	}
	
}
