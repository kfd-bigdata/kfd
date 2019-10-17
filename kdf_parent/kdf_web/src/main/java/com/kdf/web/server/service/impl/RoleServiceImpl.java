package com.kdf.web.server.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kdf.web.server.bean.PbButton;
import com.kdf.web.server.bean.PbDepartmentRole;
import com.kdf.web.server.bean.PbMenu;
import com.kdf.web.server.bean.PbRole;
import com.kdf.web.server.bean.PbRoleMenu;
import com.kdf.web.server.dto.RoleDTO;
import com.kdf.web.server.repository.ButtonRepository;
import com.kdf.web.server.repository.DepartmentRoleRepository;
import com.kdf.web.server.repository.MenuRepository;
import com.kdf.web.server.repository.RoleMenuRepository;
import com.kdf.web.server.repository.RoleRepository;
import com.kdf.web.server.service.RoleService;

/**    
 * 角色service
 * @Package: com.admin.server.service.impl 
 * @author: LiWenLong  
 * @date: 2019年8月5日 下午2:00:17 
 */
@Service
public class RoleServiceImpl implements RoleService {

	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private MenuRepository menuRepository;
	
	@Autowired
	private ButtonRepository buttonRepository;
	
	@Autowired
	private RoleMenuRepository roleMenuRepository;
	
	@Autowired
	private DepartmentRoleRepository departmentRoleRepository;

	@Override
	public Page<PbRole> findByDelFlagPage(Integer delflag, Integer page, Integer limit) {
		Pageable pageable = PageRequest.of(page - 1, limit);
		return roleRepository.findByDelFlag(delflag, pageable);
	}

	@Override
	public PbRole save(PbRole roleBean) {
		return roleRepository.save(roleBean);
	}

	@Override
	@Transactional
	public void del(Integer id, Integer userId) {
		departmentRoleRepository.deleteByRoleId(id);
		roleMenuRepository.deleteByRoleId(id);
		PbRole role = roleRepository.getOne(id);
		role.setDelFlag(1);
		role.setDeleteTime(new Date());
		role.setDeleteUserId(userId);
		roleRepository.save(role);
	}

	@Override
	public List<PbRole> findByDelFlag(Integer delflag) {
		return roleRepository.findByDelFlag(delflag);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Map<String,Object>> selectButtonsByMenuId(Integer menuId, Integer roleId) {
		List<Map<String,Object>> result= new ArrayList<Map<String,Object>>();
		//先去查menu表
		PbMenu pbMenu = menuRepository.getOne(menuId);
		//判断当前munu下是否有按钮集
		if(pbMenu.getButtons() != null) {
			String []buttons = pbMenu.getButtons().split(",");
			Stream.of(buttons).forEach(buttonId ->{
				Map<String,Object> temp = new HashMap<String, Object>(3);
				PbButton button=buttonRepository.getOne(Integer.parseInt(buttonId));
				temp.put("buttonId", button.getButtonId());
				temp.put("buttonName", button.getButtonName());
				temp.put("checkedStatus", false);
				result.add(temp);
			});
		}
		//通过menuId和roleId去查询角色菜单中间表
		PbRoleMenu pbRoleMenu = roleMenuRepository.findByMenuIdAndRoleId(menuId, roleId);
		if(pbRoleMenu != null && pbRoleMenu.getButtons() != null) {
			String []buttonIds=pbRoleMenu.getButtons().split(",");
			result.stream().forEach(map ->{
				String buttonId=map.get("buttonId").toString();
				Stream.of(buttonIds).filter(tempButtonId -> tempButtonId.equals(buttonId)).forEach(tempButtonId ->{
					map.put("checkedStatus", true);
				});
			});
			
		}
		return result;
	}
		
	@Override
	public List<RoleDTO> findByDelFlag(Integer delFlag, Integer departmentId) {
		List<PbRole> roleBeanList = roleRepository.findByDelFlag(delFlag);
		List<RoleDTO> roleDtoList = new ArrayList<RoleDTO>();
		roleBeanList.stream().forEach(role -> {
			RoleDTO roleDTO = new RoleDTO();
			roleDTO.setRoleId(role.getRoleId());
			roleDTO.setRoleName(role.getRoleName());
			roleDTO.setRoleDescribe(role.getRoleDescribe());
			// 判断是否被选中
			boolean checked = false;
			PbDepartmentRole departRole = departmentRoleRepository.findByDepartmentIdAndRoleId(departmentId, role.getRoleId());
			if(departRole != null) {
				checked = true;
			}
			roleDTO.setChecked(checked);
			roleDtoList.add(roleDTO);
		});
		return roleDtoList;
	}
	
}
