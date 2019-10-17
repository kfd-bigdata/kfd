package com.kdf.web.server.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.kdf.web.server.bean.PbMenu;
import com.kdf.web.server.dto.MenuDTO;

public interface MenuService {

	List<PbMenu> findAll();

	List<MenuDTO> findByUserId(Integer userId);

	List<PbMenu> findByDelFlag(Integer delFlag);

	List<MenuDTO> findByDelFlagAndRoleIdToTree(Integer delFlag, Integer roleId);

	PbMenu save(PbMenu bean);

	Page<PbMenu> findAllPage(Integer page, Integer limit);

	void delById(Integer id, Integer userId);

	Boolean updataById(PbMenu bean, Integer userId);

	void addRoleMenu(Integer userId, Integer roleId, Integer[] ids);

	List<MenuDTO> buttonTree(Integer roleId);

	void addRoleMenuButtons(Integer menuId, Integer roleId, Integer[] ids);
	

}
