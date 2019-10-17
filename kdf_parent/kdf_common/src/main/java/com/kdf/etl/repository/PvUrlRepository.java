package com.kdf.etl.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.kdf.etl.bean.PvUrl;

@Repository
public interface PvUrlRepository extends JpaRepository<PvUrl, Long> {
	
	@Query(value="SELECT SUM(pv_count) AS count ,url AS url FROM kfd_pv_url WHERE app_id=?1 GROUP BY url ORDER BY count DESC LIMIT 0,50", nativeQuery = true)
    List<Map<String, Object>> selectPvUrlList(@Param("appId") String appId);

	@Query(value="SELECT COUNT(1) AS count,DATE_FORMAT(request_time, '%Y-%m-%d %h') AS time FROM kfd_pv_url GROUP BY time LIMIT 0,50", nativeQuery = true)
	List<Map<String, Object>> selectPvUrlTime(String appId);
}
