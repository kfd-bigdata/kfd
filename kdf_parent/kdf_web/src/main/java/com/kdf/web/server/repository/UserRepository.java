package com.kdf.web.server.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kdf.web.server.bean.PbUser;

@Repository
public interface UserRepository extends JpaRepository<PbUser, Integer> {

	List<PbUser> findTop10ByOrderByLastLoginTime();

	PbUser findByUserNameAndPassword(String username, String password);

	Page<PbUser> findByDelFlag(Integer delFlag, Pageable pageable);

	PbUser findByUserName(String username);

}
