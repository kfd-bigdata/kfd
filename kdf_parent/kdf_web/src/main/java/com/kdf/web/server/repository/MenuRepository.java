package com.kdf.web.server.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kdf.web.server.bean.PbMenu;


@Repository
public interface MenuRepository extends JpaRepository<PbMenu, Integer> {

	List<PbMenu> findByMenuIdIn(Collection<Integer> ids);

	List<PbMenu> findByDelFlag(Integer delFlag);

	List<PbMenu> findByDelFlagOrderBySort(Integer delFlag);

	List<PbMenu> findByMenuIdInOrderBySort(Collection<Integer> ids);
	
}
