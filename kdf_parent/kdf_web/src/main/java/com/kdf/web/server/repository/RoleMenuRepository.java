package com.kdf.web.server.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.kdf.web.server.bean.PbRoleMenu;

@Repository
public interface RoleMenuRepository extends JpaRepository<PbRoleMenu, Integer> {

	List<PbRoleMenu> findByRoleId(Integer roleId);

	PbRoleMenu findByMenuIdAndRoleId(Integer id, Integer roleId);

	@Modifying
	@Transactional
	@Query(value="delete from pb_role_menu where role_id =?1",nativeQuery = true)
	void deleteByRoleId(Integer roleId);

	List<PbRoleMenu> findByMenuId(Integer menuId);

	@Modifying
	@Transactional
	@Query(value="delete from pb_role_menu where menu_id =?1 and role_id =?2",nativeQuery = true)
	void deletByMenuIdAndRoleId(Integer menuId, Integer roleId);


}
