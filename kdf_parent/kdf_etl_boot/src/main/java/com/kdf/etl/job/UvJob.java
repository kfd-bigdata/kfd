package com.kdf.etl.job;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.kdf.etl.base.BaseHadoop;
import com.kdf.etl.service.UvTimeDistributionService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class UvJob extends BaseHadoop {

	@Autowired
	private UvTimeDistributionService uvTimeDistributionService;

	@Scheduled(fixedRate = 1000000)
	public void saveUvTimeDistribution() {
		String yyyyMmDdHh = getYyyyMmDdHh();
		log.info("start:uv->saveUvTimeDistribution,time={}", yyyyMmDdHh);
		uvTimeDistributionService.saveUvTimeDistribution(yyyyMmDdHh);
		log.info("end:uv->saveUvTimeDistribution");
	}

}
