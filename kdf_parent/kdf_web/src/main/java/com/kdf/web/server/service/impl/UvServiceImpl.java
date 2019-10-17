package com.kdf.web.server.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kdf.etl.repository.UvTimeDistributionRepository;
import com.kdf.web.server.service.UvService;

@Service
public class UvServiceImpl implements UvService {

	@Autowired
	private UvTimeDistributionRepository uvTimeDistributionRepository;

	@Override
	public Long countByRequestTimeAndAppid(Date date, String appid) {
		return uvTimeDistributionRepository.countByRequestTimeAndAppid(date, appid);
	}

	@Override
	public Long avgByRequestTimeAndAppid(Date date, String appid) {
		return uvTimeDistributionRepository.avgByRequestTimeAndAppid(date, appid);
	}

	@Override
	public List<Map<String, Object>> countGroupByAppid() {
		return uvTimeDistributionRepository.countGroupByAppid();
	}

}
