package com.kdf.etl.job;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.kdf.etl.base.BaseHadoop;
import com.kdf.etl.service.PvService;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @ClassName: PvJob
 * @Description: pv总统计 定时job
 * @author 王者の南が少ない 1715656022@qq.com
 * @date 2019年10月10日 上午11:04:44
 * 
 */
@Component
@Slf4j
public class PvJob extends BaseHadoop {
	@Autowired
	private PvService pvService;

//	@Scheduled(fixedRate = 1000000)
	@Scheduled(cron = "0 0 */1 * * ?")
	public void allpv() throws Exception {

		boolean debugFlag = log.isDebugEnabled();
		if (debugFlag) {
			log.debug("pvjob,allpv job start ");
		}
		String yearMonthDayHour = getYyyyMmDdHh();
		pvService.saveAllPv(yearMonthDayHour);
		if (debugFlag) {
			log.debug("pvjob,allpv job end");
		}
	}
}
