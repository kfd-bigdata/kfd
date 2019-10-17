package com.kdf.etl.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kdf.etl.bean.PvAll;
import com.kdf.etl.repository.PvAllRepository;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @ClassName: PvService
 * @Description: pv统计service
 * @author 王者の南が少ない 1715656022@qq.com
 * @date 2019年10月10日 上午11:04:44
 * 
 */
@Slf4j
@Service
public class PvService {

	@Autowired
	private HiveService hiveService;
 
	@Autowired
	private PvAllRepository pvAllRepository;

	public void saveAllPv(String yearMonthDayHour) {
		log.info("===============数据allpv执行开始==============");
		List<PvAll> pvAllList = hiveService.getAllPv(yearMonthDayHour);

		pvAllRepository.saveAll(pvAllList);
		log.info("===============数据allpv执行完成==============" + pvAllList);
	}

}
