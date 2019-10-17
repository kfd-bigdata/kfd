package com.kdf.etl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kdf.etl.bean.KfdProject;

@Repository
public interface KfdProjectRepository extends JpaRepository<KfdProject, Long> {

}
