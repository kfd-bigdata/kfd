package com.kdf.web.server.service.impl;

import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.druid.util.StringUtils;
import com.kdf.web.server.bean.PbDepartment;
import com.kdf.web.server.bean.PbDepartmentRole;
import com.kdf.web.server.bean.PbUserDepartment;
import com.kdf.web.server.repository.DepartmentRepository;
import com.kdf.web.server.repository.DepartmentRoleRepository;
import com.kdf.web.server.repository.UserDepartmentRepository;
import com.kdf.web.server.service.DepartmentService;

@Service
public class DepartmentServiceImpl implements DepartmentService {

	@Autowired
	private DepartmentRepository departmentRepository;
	
	@Autowired
	private DepartmentRoleRepository departmentRoleRepository;
	
	@Autowired
	private UserDepartmentRepository userDepartmentRepository;
	
	@Override
	public List<PbDepartment> findByDelFlag(Integer delFlag) {
		return departmentRepository.findByDelFlagOrderBySort(delFlag);
	}

	@Override
	public PbDepartment save(PbDepartment department) {
		return departmentRepository.save(department);
	}

	@Override
	public void delById(Integer departmentId, int userId) {
		PbDepartment departBean = departmentRepository.findById(departmentId).get();
		departBean.setDelFlag(1);
		departBean.setModifyTime(new Date());
		departmentRepository.save(departBean);
		
	}

	@Override
	public PbDepartment updataById(PbDepartment department, int userId) {
		PbDepartment departBean = departmentRepository.findById(department.getDepartmentId()).get();
		departBean.setModifyTime(new Date());
		
		String name = department.getDepartmentName();
		if (!StringUtils.isEmpty(name)) {
			departBean.setDepartmentName(name);
		}
		String departmentRemark = department.getDepartmentRemark();
		if (!StringUtils.isEmpty(departmentRemark)) {
			departBean.setDepartmentRemark(departmentRemark);
		}
		Integer type = department.getType();
		if (type != null) {
			departBean.setType(type);
		}
		Integer sort = department.getSort();
		if (sort != null) {
			departBean.setSort(sort);
		}
		return departmentRepository.save(departBean);
	}

	@Override
	@Transactional
	public void updateDepartRole(Integer departmentId, String roleIdStrs, Integer userId) {
		departmentRoleRepository.deleteByDepartmentId(departmentId);
		String[] roleIdArray = roleIdStrs.split(",");
		Stream.of(roleIdArray).filter(roleId -> !StringUtils.isEmpty(roleId)).forEach(roleId ->{
			PbDepartmentRole departmentRole = new PbDepartmentRole();
			departmentRole.setDepartmentId(departmentId);
			departmentRole.setRoleId(Integer.parseInt(roleId));
			departmentRoleRepository.save(departmentRole);
		});
	}

	@Override
	public List<PbDepartment> findByDepartmentParentIdAndDelFlag(Integer departmentId, Integer delFlag) {
		return departmentRepository.findByDepartmentParentIdAndDelFlag(departmentId, delFlag);
	}

	@Override
	public List<PbUserDepartment> finduserDepartListByDepartmentId(Integer departmentId) {
		return userDepartmentRepository.findByDepartmentId(departmentId);
	}

}
