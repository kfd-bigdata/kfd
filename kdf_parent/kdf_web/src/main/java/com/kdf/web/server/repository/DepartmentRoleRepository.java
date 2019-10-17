package com.kdf.web.server.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kdf.web.server.bean.PbDepartmentRole;

@Repository
public interface DepartmentRoleRepository extends JpaRepository<PbDepartmentRole, Integer>{

	List<PbDepartmentRole> findByDepartmentId(Integer departmentId);

	void deleteByDepartmentId(Integer departmentId);
	
	void deleteByRoleId(Integer roleId);

	/**   
	 * @Title: findByDepartmentIdAndRoleId   
	 * @Description: TODO(描述这个方法的作用)   
	 * @param: @param departmentId
	 * @param: @param roleId
	 * @param: @return      
	 * @return: PbDepartmentRole      
	 * @throws   
	 */  
	PbDepartmentRole findByDepartmentIdAndRoleId(Integer departmentId, Integer roleId);

}
