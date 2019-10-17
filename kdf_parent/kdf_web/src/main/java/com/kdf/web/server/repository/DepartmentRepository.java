package com.kdf.web.server.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kdf.web.server.bean.PbDepartment;

@Repository
public interface DepartmentRepository extends JpaRepository<PbDepartment, Integer> {

	List<PbDepartment> findByDelFlagOrderBySort(Integer delFlag);

    PbDepartment findByDepartmentId(Integer departmentId);

	List<PbDepartment> findByDelFlag(Integer delFlag);

	List<PbDepartment> findByDepartmentParentIdAndDelFlag(Integer departmentId, Integer delFlag);

}
