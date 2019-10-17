package com.kdf.web.server.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kdf.etl.bean.PvHttpClient;
import com.kdf.etl.repository.HttpClientPVRepository;
import com.kdf.web.server.service.HttpClientService;

@Service
public class HttpClientServiceImpl implements HttpClientService {

	@Autowired
	private HttpClientPVRepository httpClientPVRepository;
	
	@Override
	public List<PvHttpClient> findByAppidAndRequestTimeBetween(String appid, Date startTime, Date endTime) {
		return httpClientPVRepository.findByAppidAndRequestTimeBetween(appid,startTime,endTime);
	}

	@Override
	public List<PvHttpClient> findByAppid(String appid) {
		return httpClientPVRepository.findByAppid(appid);
	}

}
