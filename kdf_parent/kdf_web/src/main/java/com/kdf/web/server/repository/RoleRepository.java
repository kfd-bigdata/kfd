package com.kdf.web.server.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.kdf.web.server.bean.PbRole;

public interface RoleRepository extends JpaRepository<PbRole, Integer> {

	Page<PbRole> findByDelFlag(Integer delflag, Pageable pageable);

	List<PbRole> findByDelFlag(Integer delflag);

	void deleteByRoleId(Integer id);

}
