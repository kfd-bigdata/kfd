package com.kdf.web.server.controller.department;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.druid.util.StringUtils;
import com.kdf.web.server.bean.PbDepartment;
import com.kdf.web.server.bean.PbUser;
import com.kdf.web.server.bean.PbUserDepartment;
import com.kdf.web.server.dto.ReturnDTO;
import com.kdf.web.server.dto.RoleDTO;
import com.kdf.web.server.service.DepartmentService;
import com.kdf.web.server.service.RoleService;
import com.kdf.web.server.utils.GetUserInfoUtil;

@RestController
@RequestMapping("department")
public class DepartmentController {

	@Autowired
	private DepartmentService departmentService;
	
	@Autowired
	private RoleService roleService;
	
	/**
	 * toMenu 部门页
	 * 
	 * @return
	 */
	@GetMapping("")
	public ModelAndView toMenu() {
		return new ModelAndView("html/department/department");
	}

	/**
	 * list 部门列表
	 * 
	 * @return
	 */
	@GetMapping("list")
	public ReturnDTO list() {
		List<PbDepartment> departList = departmentService.findByDelFlag(0);
		ReturnDTO returnBean = new ReturnDTO();
		returnBean.setCode(200);
		returnBean.setMsg("成功");
		returnBean.setData(departList);
		return returnBean;
	}
	
	
	/**
	 * add 添加部门
	 * 
	 * @param session
	 * @param department 部门信息
	 * @return
	 */
	@PostMapping("add")
	public Boolean add(HttpSession session, PbDepartment department) {
		Boolean b = true;
		try {
			PbUser user = GetUserInfoUtil.getUserInfo(session);
			if (department.getDepartmentParentId() == null) {
				department.setDepartmentParentId(0);
			}
			department.setCreateTime(new Date());
			department.setCreateUserId(user.getUserId());
			department.setDelFlag(0);
			departmentService.save(department);
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
	public Integer del(HttpSession session, Integer departmentId) {
		Integer b = 1;
		try {
			PbUser user = GetUserInfoUtil.getUserInfo(session);
			List<PbUserDepartment> userDepartList = departmentService.finduserDepartListByDepartmentId(departmentId);
			if (!CollectionUtils.isEmpty(userDepartList)) {
				// 该岗位下已绑定用户，不可删除
				b = 3;
				return b;
			}
			departmentService.delById(departmentId, user.getUserId());
		} catch (Exception e) {
			e.printStackTrace();
			b = 0;
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
	public Integer updata(HttpSession session, PbDepartment department) {
		Integer b = 1;
		try {
			PbUser user = GetUserInfoUtil.getUserInfo(session);
			if (department.getType().equals(0)) {
				// 修改为岗位时，需要判断是否有子集，有的话，禁止修改
				List<PbDepartment> childList = departmentService.findByDepartmentParentIdAndDelFlag(department.getDepartmentId(), 0);
				if (!CollectionUtils.isEmpty(childList)) {
					// 有子集不能修改
					b = 2;
					return b;
				}
			}
			if (department.getType().equals(1)) {
				// 修改为部门时，判断当前岗位是否绑定用户，如果绑定，则禁止修改为部门
				List<PbUserDepartment> userDepartList = departmentService.finduserDepartListByDepartmentId(department.getDepartmentId());
				if (!CollectionUtils.isEmpty(userDepartList)) {
					// 该岗位下已绑定用户，不可更改
					b = 3;
					return b;
				}
			}
			departmentService.updataById(department, user.getUserId());
		} catch (Exception e) {
			e.printStackTrace();
			b = 0;
		}
		return b;
	}
	
	/**
	 * tree 可选的角色列表
	 * 
	 * @param departmentId 部门id
	 * @return
	 */
	@PostMapping("getRoleList")
	public Map<String, Object> getRoleList(Integer departmentId) {
		List<RoleDTO> roleList = roleService.findByDelFlag(0, departmentId);
		Map<String, Object> map = new HashMap<String, Object>(2);
		map.put("departmentId", departmentId);
		map.put("roleList", roleList);
		return map;
	}
	/**
	 * 
	 * @Title: updateDepartRole   
	 * @Description: TODO(这里用一句话描述这个方法的作用)   
	 * @param session
	 * @param departmentId 部门id
	 * @param roleIdStrs 角色ids
	 * @return: Boolean      
	 * @throws
	 */
	@PostMapping("updateDepartRole")
	public Boolean updateDepartRole(HttpSession session, Integer departmentId, String roleIdStrs) {
		Boolean b = true;
		try {
			if (departmentId == null || StringUtils.isEmpty(roleIdStrs)) {
				b = false;
			} else {
				PbUser user = GetUserInfoUtil.getUserInfo(session);
				departmentService.updateDepartRole(departmentId, roleIdStrs, user.getUserId());
			}
		} catch (Exception e) {
			e.printStackTrace();
			b = false;
		}
		return b;
	}
	
}
