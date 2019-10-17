package com.kdf.web.server.service;

import java.util.Date;
import java.util.List;

import com.kdf.etl.bean.PvHttpClient;


public interface HttpClientService {

	List<PvHttpClient> findByAppidAndRequestTimeBetween(String appid, Date startTime, Date endTime);

	List<PvHttpClient> findByAppid(String appid);

}
