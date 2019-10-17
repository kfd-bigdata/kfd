package com.kdf.web.server.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.kdf.web.server.bean.PbButton;
import com.kdf.web.server.bean.PbDepartmentRole;
import com.kdf.web.server.bean.PbRoleMenu;
import com.kdf.web.server.bean.PbUserDepartment;
import com.kdf.web.server.repository.ButtonRepository;
import com.kdf.web.server.repository.DepartmentRoleRepository;
import com.kdf.web.server.repository.RoleMenuRepository;
import com.kdf.web.server.repository.UserDepartmentRepository;
import com.kdf.web.server.service.ButtonService;
@Service
public class ButtonServiceImpl implements ButtonService{

	@Autowired
	private ButtonRepository buttonRepository;
	
	@Autowired
	private DepartmentRoleRepository departmentRoleRepository;
	
	@Autowired
	private RoleMenuRepository roleMenuRepository;

	@Autowired
	private UserDepartmentRepository userDepartmentRepository;
	
	@Override
	public  Page<PbButton> findByDelFlag(int i,Integer page, Integer limit) {
		Pageable pageable = PageRequest.of(page - 1, limit);
		return buttonRepository.findAllByDelFlag(i,pageable);
	}

	@Override
	public void save(PbButton bean) {
		buttonRepository.save(bean);
	}

	@Override
	public void delById(Integer buttonId, Integer userId) {
		PbButton buttonBean = buttonRepository.findById(buttonId).get();
		buttonBean.setDelFlag(1);
		buttonRepository.save(buttonBean);
	}

	@Override
	public PbButton updataById(PbButton bean, Integer userId) {
		PbButton buttonBean = buttonRepository.findById(bean.getButtonId()).get();
		String name = bean.getButtonName();
		if (!StringUtils.isEmpty(name)) {
			buttonBean.setButtonName(name);
		}
		String event = bean.getEvent();
		if (null != event) {
			buttonBean.setEvent(event);
		}
		String colour = bean.getColour();
		if (null != colour) {
			buttonBean.setColour(colour);
		}
		Integer type = bean.getType();
		if (!StringUtils.isEmpty(type)) {
			buttonBean.setType(type);
		}
		return buttonRepository.save(buttonBean);
		
	}

	@Override
	@Transactional(readOnly = true)  
	public List<PbButton> getButtons(Integer userId, Integer menuId) {
		List<PbButton> list = null;
		List<PbUserDepartment> userDepartmentList = userDepartmentRepository.findByUserId(userId);
		if (!CollectionUtils.isEmpty(userDepartmentList)) {
			// 获取所有岗位下角色菜单按钮
			StringBuilder btnStr = new StringBuilder();
			for (PbUserDepartment userDepartment : userDepartmentList) {
				List<PbDepartmentRole> departmentRoleList = departmentRoleRepository.findByDepartmentId(userDepartment.getDepartmentId());
				if (!CollectionUtils.isEmpty(departmentRoleList)) {
					for (PbDepartmentRole departmentRole : departmentRoleList) {
						PbRoleMenu roleMenu = roleMenuRepository.findByMenuIdAndRoleId(menuId,departmentRole.getRoleId());
						if (!StringUtils.isEmpty(roleMenu.getButtons())) {
							btnStr.append(roleMenu.getButtons());
							btnStr.append(",");
						} 
					}
				}
			}
			String[] split = btnStr.toString().split(",");
			if (split != null && split.length > 0) {
				// 去重
				list = Arrays.stream(split).distinct().map(btnIdstr -> Integer.parseInt(btnIdstr))
						.map(btnId -> buttonRepository.getOne(btnId)).collect(Collectors.toList());
			}
		}
		return list;
	}

}
