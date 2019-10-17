package com.kdf.etl.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.kdf.etl.bean.KfdUserProjectRelation;

@Repository
public interface KfdUserProjectRelationRepository extends JpaRepository<KfdUserProjectRelation, Long> {

	List<KfdUserProjectRelation> findByUserId(Long userId);
}
