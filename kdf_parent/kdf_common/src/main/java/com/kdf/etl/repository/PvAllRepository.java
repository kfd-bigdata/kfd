package com.kdf.etl.repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.kdf.etl.bean.PvAll;

@Repository
public interface PvAllRepository extends JpaRepository<PvAll, Long> {

	@Query(value = "select sum(pv_count) as cnt, date_format(request_time, '%Y-%m-%d %h') as time from kfd_pv_all where appid = ? group by date_format(request_time, '%Y-%m-%d %h')", nativeQuery = true)
	List<Map<String, Object>> findAllPvMap(String appid);

	@Query(value = "select ifnull(sum(pv_count), 0) from kfd_pv_all where 1=1 and if(?1 != '', request_time = ?1, 1=1) and if(?2 != '', appid = ?2 , 1=1)", nativeQuery = true)
	Long countByRequestTimeAndAppid(Date date, String appid);

	@Query(value = "select ifnull(floor(avg(pv_count)), 0) from kfd_pv_all where 1=1 and if(?1 != '', request_time = ?1, 1=1) and if(?2 != '', appid = ?2 , 1=1)", nativeQuery = true)
	Long avgByRequestTimeAndAppid(Date date, String appid);

	@Query(value = "select ifnull(t2.pv_count, 0) from kfd_time t1 left join (select sum(pv_count) as pv_count, request_time from kfd_pv_all where 1=1 and date_format(request_time, '%Y-%m-%d') = date_format(?2, '%Y-%m-%d') and if(?1 != '', appid = ?1, 1=1) group by request_time) t2 on date_format(t1.time, '%H') = date_format(t2.request_time, '%H') order by t1.time asc", nativeQuery = true)
	List<Long> findByAppidAndAndRequestTimeOrderByRequestTime(String appid, Date date);

	@Query(value = "select t2.project_name as projectName, date_format(request_time, '%Y-%m-%d') AS requestTime, ifnull(sum(t1.pv_count), 0) as countNum from kfd_pv_all t1 right join kfd_project t2 on t1.appid = t2.appid group by t2.project_name, date_format(request_time, '%Y-%m-%d') order by t2.project_name, request_time", nativeQuery = true)
	List<Map<String, Object>> countGroupByAppid();

}
