package com.kdf.etl.repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.kdf.etl.bean.UvTimeDistribution;

@Repository
public interface UvTimeDistributionRepository extends JpaRepository<UvTimeDistribution, Long> {

	UvTimeDistribution findByRequestTime(Date strToDate);

	List<UvTimeDistribution> findByRequestTimeBetween(Date date, Date date2);

	@Query(value = "select ifnull(sum(uv_count), 0) from kfd_uv_time_distribution where 1=1 and if(?1 != '', request_time = ?1, 1=1) and if(?2 != '', appid = ?2 , 1=1)", nativeQuery = true)
	Long countByRequestTimeAndAppid(Date date, String appid);

	@Query(value = "select ifnull(floor(avg(uv_count)), 0) from kfd_uv_time_distribution where 1=1 and if(?1 != '', request_time = ?1, 1=1) and if(?2 != '', appid = ?2 , 1=1)", nativeQuery = true)
	Long avgByRequestTimeAndAppid(Date date, String appid);

	@Query(value = "select t2.project_name as projectName, ifnull(date_format(request_time, '%Y-%m-%d'), 0) AS requestTime, ifnull(sum(t1.uv_count), 0) as countNum from kfd_uv_time_distribution t1 right join kfd_project t2 on t1.appid = t2.appid group by t2.project_name, date_format(request_time, '%Y-%m-%d') order by t2.project_name, request_time", nativeQuery = true)
	List<Map<String, Object>> countGroupByAppid();

}
