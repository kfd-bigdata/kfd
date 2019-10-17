package com.kdf.etl.job;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.kdf.etl.base.BaseHadoop;
import com.kdf.etl.service.HttpClientPVService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class HttpPvJob extends BaseHadoop {

	@Autowired
	private HttpClientPVService httpClientPVService;
	
	@Scheduled(fixedRate = 1000000)
	public void saveUvTimeDistribution() {
		String yyyyMmDdHh = getYyyyMmDdHh();
		log.info("start:uv->saveHttpClient,time={}", yyyyMmDdHh);
		httpClientPVService.saveHttpClient(yyyyMmDdHh);
		log.info("end:uv->saveHttpClient");
	}
	
}
