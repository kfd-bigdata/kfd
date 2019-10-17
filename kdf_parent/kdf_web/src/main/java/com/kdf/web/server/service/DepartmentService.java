package com.kdf.web.server.service;

import java.util.List;

import com.kdf.web.server.bean.PbDepartment;
import com.kdf.web.server.bean.PbUserDepartment;

public interface DepartmentService {

	List<PbDepartment> findByDelFlag(Integer delFlag);

	PbDepartment save(PbDepartment department);

	void delById(Integer departmentId, int userId);

	PbDepartment updataById(PbDepartment department, int userId);

	/**
	 * 修改部门角色关联关系
	 * @Title: updateDepartRole   
	 * @param departmentId
	 * @param roleIdStrs
	 * @param userId      
	 * @return: void      
	 */
	void updateDepartRole(Integer departmentId, String roleIdStrs, Integer userId);

	List<PbDepartment> findByDepartmentParentIdAndDelFlag(Integer departmentId, Integer delFlag);

	List<PbUserDepartment> finduserDepartListByDepartmentId(Integer departmentId);


}
