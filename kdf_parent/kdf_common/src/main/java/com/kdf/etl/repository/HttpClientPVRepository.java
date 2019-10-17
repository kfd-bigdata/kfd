package com.kdf.etl.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kdf.etl.bean.PvHttpClient;

@Repository
public interface HttpClientPVRepository extends JpaRepository<PvHttpClient, Long> {

	PvHttpClient findByMethod(String method);
	
	List<PvHttpClient> findByAppidAndRequestTimeBetween(String appid, Date startTime, Date endTime);

	List<PvHttpClient> findByAppid(String appid);

}
