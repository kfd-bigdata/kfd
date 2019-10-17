package com.kdf.etl.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.kdf.etl.bean.UvUserAreaDistribution;

@Repository
public interface UvUserAreaDistributionRepository extends JpaRepository<UvUserAreaDistribution, Long> {

	@Query(value = "select sum(start_count) AS 'value', province as 'name' from kfd_uv_user_area_distribution where if(?1 != '', appid = ?1, 1=1) group by province", nativeQuery = true)
	List<Map<String, Object>> countGroupByProvince(String appid);

}
