package com.kdf.web.server.service.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.kdf.web.server.bean.PbDepartmentRole;
import com.kdf.web.server.bean.PbMenu;
import com.kdf.web.server.bean.PbRoleMenu;
import com.kdf.web.server.bean.PbUserDepartment;
import com.kdf.web.server.dto.MenuDTO;
import com.kdf.web.server.repository.DepartmentRoleRepository;
import com.kdf.web.server.repository.MenuRepository;
import com.kdf.web.server.repository.RoleMenuRepository;
import com.kdf.web.server.repository.UserDepartmentRepository;
import com.kdf.web.server.service.MenuService;
@Service
public class MenuServiceImpl implements MenuService{

	@Autowired
	private MenuRepository menuRepository;
	
	@Autowired
	private UserDepartmentRepository userDepartmentRepository;

	@Autowired
	private DepartmentRoleRepository departmentRoleRepository;
	
	@Autowired
	private RoleMenuRepository roleMenuRepository;

	@Override
	@Transactional(readOnly = true)
	public List<MenuDTO> findByUserId(Integer userId) {
		List<PbRoleMenu> roleMenuList = new ArrayList<>();
		List<PbUserDepartment> userDepartmentList = userDepartmentRepository.findByUserId(userId);
		userDepartmentList.forEach(pbUserDepartment ->{
			List<PbDepartmentRole> list = departmentRoleRepository
					.findByDepartmentId(pbUserDepartment.getDepartmentId());
			list.forEach(departmentRole ->{
				Integer roleId = departmentRole.getRoleId();
				// 获取用户菜单
				List<PbRoleMenu> pbRoleMenuList = roleMenuRepository.findByRoleId(roleId);
				roleMenuList.addAll(pbRoleMenuList);
			});
		});
		List<PbMenu> menuBeanList = new ArrayList<PbMenu>();
		// 获取部门角色
		roleMenuList.stream()
				.collect(Collectors.collectingAndThen(
						Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(PbRoleMenu::getMenuId))),
						ArrayList::new))
				.forEach(roleMenuBean -> {
					PbMenu menu = menuRepository.getOne(roleMenuBean.getMenuId());
					menuBeanList.add(menu);
				});
		return this.listToTree(menuBeanList, 0, null);
	}
	
	private List<MenuDTO> listToTree(List<PbMenu> menuBeanList, Integer parentId, Integer roleId) {
		List<MenuDTO> menuDTOList = new ArrayList<MenuDTO>();
		for (PbMenu menuBean : menuBeanList) {
			if (parentId == menuBean.getParentId()) {
				Integer id = menuBean.getMenuId();
				MenuDTO menuDTO = new MenuDTO();
				menuDTO.setId(id);
				menuDTO.setTitle(menuBean.getName());
				menuDTO.setUrl(menuBean.getUrl());
				if(roleId ==null) {
					menuDTO.setChecked(menuBean.getType() == 0 ? false : true);
				}
				else {
					PbRoleMenu roleMenuBean = roleMenuRepository.findByMenuIdAndRoleId(id, roleId);
					menuDTO.setChecked(menuBean.getType() == 0 || roleMenuBean == null ? false : true);
				}
				for (PbMenu menuBean2 : menuBeanList) {
					if (menuBean.getMenuId().equals(menuBean2.getParentId())) {
						menuDTO.setChildren(this.listToTree(menuBeanList, menuBean.getMenuId(), roleId));
						break;
					}
				}
				menuDTOList.add(menuDTO);
			}
		}
		return menuDTOList;
	}
	
	
	@Override
	public PbMenu save(PbMenu bean) {
		return menuRepository.save(bean);
	}

	@Override
	public Page<PbMenu> findAllPage(Integer page, Integer limit) {
		Pageable pageable = PageRequest.of(page - 1, limit);
		return menuRepository.findAll(pageable);
	}

	@Override
	public void delById(Integer id, Integer userId) {
		PbMenu menuBean = menuRepository.findById(id).get();
		menuBean.setDelFlag(1);
		menuBean.setDeleteUserId(userId);
		menuBean.setDeleteTime(new Date());
		menuRepository.save(menuBean);
	}

	@Override
	public Boolean updataById(PbMenu bean, Integer userId) {
		Integer menuId = bean.getMenuId();
		PbMenu menuBean = menuRepository.getOne(menuId);
		// 未解绑角色菜单表中的按钮关系 不可修改
		if (!(StringUtils.isEmpty(menuBean.getButtons()) || bean.getButtons().equals(menuBean.getButtons()))) {
			List<PbRoleMenu> roleMenuList = roleMenuRepository.findByMenuId(menuId);
			for (PbRoleMenu roleMenu : roleMenuList) {
				if (!StringUtils.isEmpty(roleMenu.getButtons())) {
					return false;
				}
			}
		}
		menuBean.setUpdateUserId(userId);
		menuBean.setUpdateTime(new Date());
		String name = bean.getName();
		if (!StringUtils.isEmpty(name)) {
			menuBean.setName(name);
		}
		String url = bean.getUrl();
		if (null != url) {
			menuBean.setUrl(url);
		}
		Integer sort = bean.getSort();
		if (!StringUtils.isEmpty(sort)) {
			menuBean.setSort(sort);
		}
		Integer type = bean.getType();
		if (!StringUtils.isEmpty(type)) {
			menuBean.setType(type);
		}
		String buttons = bean.getButtons();
		menuBean.setButtons(buttons);
		menuRepository.save(menuBean);
		return true;
	}

	@Override
	public List<PbMenu> findAll() {
		return menuRepository.findAll();
	}

	@Override
	public List<PbMenu> findByDelFlag(Integer delFlag) {
		return menuRepository.findByDelFlagOrderBySort(delFlag);
	}

	@Override
	public List<MenuDTO> findByDelFlagAndRoleIdToTree(Integer delFlag, Integer roleId) {
		List<PbMenu> menuBeanList = menuRepository.findByDelFlag(delFlag);
		return this.listToTree(menuBeanList, 0,roleId);
	}


	@Override
	@Transactional
	public void addRoleMenu(Integer userId, Integer roleId, Integer[] ids) {
		List<PbRoleMenu> pbRoleMenuList = roleMenuRepository.findByRoleId(roleId);
		for (Integer menuId : ids) {
			PbRoleMenu roleMenu = new PbRoleMenu();
			roleMenu.setMenuId(menuId);
			roleMenu.setRoleId(roleId);
			PbRoleMenu pbRoleMenu = roleMenuRepository.findByMenuIdAndRoleId(menuId, roleId);
			if(pbRoleMenu != null) {
				roleMenu.setButtons(pbRoleMenu.getButtons());
			}
			roleMenuRepository.save(roleMenu);
		}
		roleMenuRepository.deleteInBatch(pbRoleMenuList);
	}

	@Override
	@Transactional(readOnly = true) 
	public List<MenuDTO> buttonTree(Integer roleId) {
		List<PbMenu> list= new ArrayList<PbMenu>();
		List<PbRoleMenu> roleMenuList=roleMenuRepository.findByRoleId(roleId);
		for (PbRoleMenu pbRoleMenu : roleMenuList) {
			list.add(menuRepository.getOne(pbRoleMenu.getMenuId()));
		}
		return this.listToTree(list, 0,null);
	}

	@Override
	public void addRoleMenuButtons(Integer menuId, Integer roleId, Integer[] ids) {
		PbRoleMenu pbRoleMenu = roleMenuRepository.findByMenuIdAndRoleId(menuId, roleId);
		String str="";
		if(ids == null) {
			pbRoleMenu.setButtons(null);
		}
		else {
			for (int i = 0; i < ids.length; i++) {
				if(i == 0){
					str+=ids[i];
				}
				else {
					str+=","+ids[i];
				}
			}
			pbRoleMenu.setButtons(str);
		}
		roleMenuRepository.save(pbRoleMenu);
	}
	
	
}
