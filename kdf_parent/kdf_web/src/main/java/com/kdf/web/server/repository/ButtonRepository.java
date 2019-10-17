package com.kdf.web.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kdf.web.server.bean.PbButton;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
@Repository
public interface ButtonRepository extends JpaRepository<PbButton, Integer> {

	 Page<PbButton> findAllByDelFlag(int i,Pageable pageable);

	
	
}
